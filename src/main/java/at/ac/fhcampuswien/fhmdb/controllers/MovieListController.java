package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.api.MovieApiException;
import at.ac.fhcampuswien.fhmdb.database.DataBaseException;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.states.UnsortedState;
import at.ac.fhcampuswien.fhmdb.states.State;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class MovieListController implements Initializable,Observer {

    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingFromComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;

    // changed from protected to public for access
    public ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    private State state;

    private final ClickEventHandler onAddToWatchlistClicked = (clickedItem) -> {
        if (clickedItem instanceof Movie movie) {
            WatchlistMovieEntity watchlistMovieEntity = new WatchlistMovieEntity(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDescription(),
                    movie.getReleaseYear(),
                    movie.getGenres(),
                    movie.getImgUrl(),
                    movie.getLengthInMinutes(),
                    movie.getRating());
            try {
                WatchlistRepository repository = WatchlistRepository.getInstance();

                // Observer (also der aktuelle MovieListController) wird der Subscriber Liste hinzugefügt.
                // Ohne diesem Befehl würde die Update-Methode vom MovieListController nicht aufgerufen werden.
                repository.subscribe(this);
                repository.addToWatchlist(watchlistMovieEntity);
            } catch (DataBaseException e) {
                UserDialog dialog = new UserDialog("Database Error", "Could not add movie to watchlist");
                dialog.show();
                e.printStackTrace();
            }
        }
    };

    // setzt aktuellen Sortierzustand des Controllers
    public void setSortState(State state){
        this.state = state;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();
        initializeLayout();
    }

    public void initializeState() {
        List<Movie> result = new ArrayList<>();
        try {
            result = MovieAPI.getAllMovies();
        } catch (MovieApiException e){
            UserDialog dialog = new UserDialog("MovieAPI Error", "Could not load movies from api");
            dialog.show();
        }

        setMovies(result);
        setMovieList(result);

        // Controller startet im unsortierten Zustand
        setSortState(new UnsortedState(this));
        // UI: Buttontext wird initialisiert
        state.updateSortButtonText();
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked)); // apply custom cells to the listview

        // genre combobox
        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");

        // year combobox
        releaseYearComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        // fill array with numbers from 1900 to 2023
        Integer[] years = new Integer[124];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }
        releaseYearComboBox.getItems().addAll(years);    // add all years to the combobox
        releaseYearComboBox.setPromptText("Filter by Release Year");

        // rating combobox
        ratingFromComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        // fill array with numbers from 0 to 10
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }
        ratingFromComboBox.getItems().addAll(ratings);    // add all ratings to the combobox
        ratingFromComboBox.setPromptText("Filter by Rating");
    }

    public void setMovies(List<Movie> movies) {
        allMovies = movies;
    }

    public void setMovieList(List<Movie> movies) {
        observableMovies.clear();
        observableMovies.addAll(movies);
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre){
        if(genre == null) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie -> movie.getGenres().contains(genre)).toList();
    }

    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim().toLowerCase();
        String releaseYear = validateComboboxValue(releaseYearComboBox.getSelectionModel().getSelectedItem());
        String ratingFrom = validateComboboxValue(ratingFromComboBox.getSelectionModel().getSelectedItem());
        String genreValue = validateComboboxValue(genreComboBox.getSelectionModel().getSelectedItem());

        Genre genre = null;
        if(genreValue != null) {
            genre = Genre.valueOf(genreValue);
        }

        List<Movie> movies = getMovies(searchQuery, genre, releaseYear, ratingFrom);

        setMovies(movies);
        setMovieList(movies);

        applyCurrentSortState(); // notwendig damit auch gefilterte Liste sortiert ist
    }


    private void applyCurrentSortState() { // notwendig damit auch gefilterte Liste sortiert ist
        state.sort();
    }

    public String validateComboboxValue(Object value) {
        if(value != null && !value.toString().equals("No filter")) {
            return value.toString();
        }
        return null;
    }

    public List<Movie> getMovies(String searchQuery, Genre genre, String releaseYear, String ratingFrom) {
        try{
            return MovieAPI.getAllMovies(searchQuery, genre, releaseYear, ratingFrom);
        }catch (MovieApiException e){
            System.out.println(e.getMessage());
            UserDialog dialog = new UserDialog("MovieApi Error", "Could not load movies from api.");
            dialog.show();
            return new ArrayList<>();
        }
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        state.changeSortStates();
        state.updateSortButtonText();
        state.sort();
    }

    // Update-Methode wird von Observable-Methode "notifySubscribers()" aufgerufen.
    // Im konkreten Fall übergibt die Klasse WatchlistRepository den Text "infromationToDisplay"
    // welcher dann in einem UserDialog dem Benutzer angezeigt wird.
    @Override
    public void update(String informationToDisplay) {
        UserDialog dialog = new UserDialog("Watchlist", informationToDisplay);
        dialog.show();
    }
}

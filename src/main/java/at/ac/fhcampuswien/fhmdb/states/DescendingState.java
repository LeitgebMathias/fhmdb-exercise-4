package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.controllers.MovieListController;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;

public class DescendingState extends State {
    public DescendingState(MovieListController movieListController) {
        super(movieListController);
    }
    @Override
    public void sort() {
        movieListController.observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
    }
    @Override
    public void updateSortButtonText() {
        movieListController.sortBtn.setText("Unsort");
    }

    // nächster Sortierzustand wird auf Unsorted festgelegt
    @Override
    public void changeSortStates() {
        movieListController.setSortState(new UnsortedState(movieListController));
    }
}

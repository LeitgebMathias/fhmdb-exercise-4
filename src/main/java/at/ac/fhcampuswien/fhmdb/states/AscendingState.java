package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.controllers.MovieListController;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;

public class AscendingState extends State {

    public AscendingState(MovieListController movieListController) {
        super(movieListController);
    }

    @Override
    public void sort() {
        movieListController.observableMovies.sort(Comparator.comparing(Movie::getTitle));
    }

    @Override
    public void updateSortButtonText() {
        movieListController.sortBtn.setText("Sort (desc)");
    }

    // nächster Sortierzustand wird auf Descending festgelegt
    @Override
    public void changeSortStates() {
        movieListController.setSortState(new DescendingState(movieListController));
    }
}
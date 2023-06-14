package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.controllers.MovieListController;

import java.util.Collections;

public class UnsortedState extends State {
    public UnsortedState(MovieListController mainController) {
        super(mainController);
    }

    @Override
    public void sort() {
    }

    @Override
    public void updateSortButtonText() {
        movieListController.sortBtn.setText("Sort (asc)");
    }

    // nächster Sortierzustand wird auf Ascending festgelegt
    @Override
    public void changeSortStates() {
        movieListController.setSortState(new AscendingState(movieListController));
    }
}

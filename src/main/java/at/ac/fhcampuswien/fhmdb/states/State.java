package at.ac.fhcampuswien.fhmdb.states;

import at.ac.fhcampuswien.fhmdb.controllers.MovieListController;

public abstract class State {
    protected MovieListController movieListController;

    public State(MovieListController movieListController) {
        this.movieListController = movieListController;
    }

    // sortiert Liste entsprechend
    public abstract void sort();

    // UI: Buttontext aktualiseren
    public abstract void updateSortButtonText();

    // um den nächsten Sortierzustand festzulegen; Übergang zu ermöglichen
    public abstract void changeSortStates();
}
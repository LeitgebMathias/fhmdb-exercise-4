package at.ac.fhcampuswien.fhmdb.controllers;

import javafx.util.Callback;

public class FactoryMovieListController implements Callback<Class<?>, Object> {
    private MovieListController controllerInstance;

    public FactoryMovieListController() {
    }

    @Override
    public Object call(Class<?> aClass) {
        if (controllerInstance == null) {
            try {
                System.out.println("Es wurde ein neuer MovieListController erstellt!");
                controllerInstance = (MovieListController) aClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return controllerInstance;
    }
}


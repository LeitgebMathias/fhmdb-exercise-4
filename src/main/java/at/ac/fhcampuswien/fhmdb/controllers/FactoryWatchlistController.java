package at.ac.fhcampuswien.fhmdb.controllers;

import javafx.util.Callback;

public class FactoryWatchlistController implements Callback<Class<?>, Object> {
    private WatchlistController controllerInstance;

    public FactoryWatchlistController() {
    }

    @Override
    public Object call(Class<?> aClass) {
        if (controllerInstance == null) {
            try {
                System.out.println("Es wurde ein neuer WatchlistController erstellt!");
                controllerInstance = (WatchlistController) aClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return controllerInstance;
    }
}


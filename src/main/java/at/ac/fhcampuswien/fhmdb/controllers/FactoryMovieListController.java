package at.ac.fhcampuswien.fhmdb.controllers;

import javafx.util.Callback;

public class FactoryMovieListController implements Callback<Class<?>, Object> {
    private MovieListController controllerInstance;

    // Der Konstruktor ist öffentlich, da es ja nicht nur eine Factory geben soll, sondern einen Controller
    public FactoryMovieListController() {
    }

    // Call Methode wird immer vom JavaFX Framework aufgerufen, wenn ein Controller benötigt wird.
    // In der Call-Methode ist dann das Singleton Pattern implementiert.
    @Override
    public Object call(Class<?> aClass) {
        // Es wird nur dann eine neue Instanz erstellt, wenn diese noch nicht existiert.
        if (controllerInstance == null) {
            try {
                System.out.println("Es wurde ein neuer MovieListController erstellt!");
                // Da der Rückgabewert von newInstance in einen MovieListController "getypecasted" wird,
                // gibt es für jeden Controller eine eigene Factory.
                controllerInstance = (MovieListController) aClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Es wird ein MovieListController zurückgegeben.");
        return controllerInstance;
    }
}


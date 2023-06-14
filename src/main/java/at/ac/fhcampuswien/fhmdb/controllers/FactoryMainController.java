package at.ac.fhcampuswien.fhmdb.controllers;

import javafx.util.Callback;

public class FactoryMainController implements Callback<Class<?>, Object> {
    private MainController controllerInstance;

    public FactoryMainController() {
    }

    @Override
    public Object call(Class<?> aClass) {
        if (controllerInstance == null) {
            try {
                System.out.println("Es wurde ein neuer MainController erstellt!");
                controllerInstance = (MainController) aClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Es wird ein MainController zurückgegeben.");
        return controllerInstance;
    }
}


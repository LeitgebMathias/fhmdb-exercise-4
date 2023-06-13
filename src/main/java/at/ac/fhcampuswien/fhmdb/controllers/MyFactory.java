package at.ac.fhcampuswien.fhmdb.controllers;

import javafx.util.Callback;

public class MyFactory implements Callback<Class<?>, Object> {
    private static MyFactory instance;
    private Object controllerInstance;

    private MyFactory() {
        // private Konstruktor
    }

    public static synchronized MyFactory getInstance() {
        if (instance == null) {
            instance = new MyFactory();
        }
        return instance;
    }

    @Override
    public Object call(Class<?> aClass) {
        if (controllerInstance == null) {
            try {
                controllerInstance = aClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return controllerInstance;
    }
}


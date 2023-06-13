package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.controllers.Observer;

public interface Observable {

    public void subscribe(Observer observer);
    public void unsubscribe(Observer observer);
    public void notifySubscribers();

}

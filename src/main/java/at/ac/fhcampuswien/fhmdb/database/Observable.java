package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.controllers.Observer;

public interface Observable {

     void subscribe(Observer observer);
     void unsubscribe(Observer observer);
     void notifySubscribers(String informationToDisplay);

}

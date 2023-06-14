package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.controllers.Observer;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable {

    Dao<WatchlistMovieEntity, Long> dao; //Verwaltung von DB-Zugriffen


    private static WatchlistRepository instance; //Singleton-Instanz der WatchlistRepository KLasse

    // Privater Konstruktor, der nur innerhalb der Klasse aufgerufen werden kann
    private WatchlistRepository() throws DataBaseException {
        try {
            this.dao = DatabaseManager.getInstance().getWatchlistDao();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public static WatchlistRepository getInstance() throws DataBaseException { //Gibt Singleton Instanz zurück.
        // Nur wenn die Instanz noch nicht initialisiert wurde, passiert es.
        if(instance == null){
            instance = new WatchlistRepository();
        }
        return instance;
    }

    // Liste in der alle Subscribers hinterlegt sind.
    private final List<Observer> subscribers = new ArrayList<>();

    public List<WatchlistMovieEntity> readWatchlist() throws DataBaseException {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while reading watchlist");
        }
    }
    public void addToWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            // only add movie if it does not exist yet
            long count = dao.queryBuilder().where().eq("apiId", movie.getApiId()).countOf();
            if (count == 0) {
                dao.create(movie);
                // Subscriber (nur MovieListController) werden informiert, dass der übergebene Film
                // erfolgreich zur Watchlist hinzugefügt wurde.
                notifySubscribers("Movie successfully added to watchlist");
            } else{
                // Subscriber (nur MovieListController) werden informiert, dass der übergebene Film
                // nicht zur Watchlist hinzugefügt wurde, da er schon darin enthalten ist.
                notifySubscribers("Movie already on watchlist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while adding to watchlist");
        }
    }

    public void removeFromWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            dao.delete(movie);
        } catch (Exception e) {
            throw new DataBaseException("Error while removing from watchlist");
        }
    }

    public boolean isOnWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            return dao.queryForMatching(movie).size() > 0;
        } catch (Exception e) {
            throw new DataBaseException("Error while checking if movie is on watchlist");
        }
    }

    // Methode dient dazu, Observer hinzuzufügen, welche Informiert werden, sobald "notifySubscribers"
    // aufgerufen wird.
    @Override
    public void subscribe(Observer observer) {
        // Es wird abgefragt, ob ein Observer schon ein Subscriber ist, bevor er als
        // Subscriber hinzugefügt wird.
        // Abfrage kann mit "contains" gemacht werden, da die dahinterstehende Equals
        // - Funktion per Default die Objektreferenzen vergleicht, was hier gewollt ist.
        if(!(subscribers.contains(observer))) subscribers.add(observer);
    }

    // Methode entfernt Observer aus der Liste, welche beim Aufruf von "notifySubscribers" aufgerufen werden.
    @Override
    public void unsubscribe(Observer observer) {
        subscribers.remove(observer);
    }

    // Jeder Subscriber wird über "InformationToDisplay" informiert.
    @Override
    public void notifySubscribers(String informationToDisplay) {
        for (Observer subscriber : subscribers) {
            // update() - Methode ist bei jedem Observer implementiert,
            // da sie im Interface Observer enthalten ist.
            subscriber.update(informationToDisplay);
        }
    }
}

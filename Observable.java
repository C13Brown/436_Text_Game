package objectAdventure.common;
// $Id: Observable.java 326 2023-10-19 14:07:09Z aconover $

/**
 * Observer pattern interface (Observable, also known as "Subject")
 *
 * @param <T> The Datatype for the message being passed.
 * @author Adam J. Conover, COSC436/COSC716
 */
public interface Observable<T> {

    /**
     * Add an observer
     *
     * @param theObserver The Observer
     */
    void addObserver(Observer<T> theObserver);

    /**
     * Remove an observer
     *
     * @param theObserver The Observer
     */
    void removeObserver(Observer<T> theObserver);

    /**
     * Notify the observer with an "event" parameter of a polymorphic type.
     *
     * @param notificationObject The object to be used when notifying all observers.
     */
    void notifyObservers(T notificationObject);
}

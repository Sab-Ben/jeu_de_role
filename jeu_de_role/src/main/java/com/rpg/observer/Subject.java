package com.rpg.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Sujet observable : gère add/remove/notify.
 * Hérité par Battle et par nos modèles MVC pour émettre des événements.
 */
public class Subject {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) { observers.add(o); }
    public void removeObserver(Observer o) { observers.remove(o); }

    protected void notifyObservers(GameEvent e) {
        for (Observer o : List.copyOf(observers)) o.update(e);
    }
}

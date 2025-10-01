package com.rpg.mvc.model;

import com.rpg.composite.GroupComponent;
import com.rpg.composite.GroupComposite;
import com.rpg.observer.GameEvent;
import com.rpg.observer.Subject;

/**
 * Modèle MVC qui contient un GroupComposite racine (ex : "Armée Alpha").
 * Notifie les observateurs à chaque modification (add/remove).
 */
public class PartyModel extends Subject {
    private final GroupComposite root;

    public PartyModel(String name) {
        this.root = new GroupComposite(name);
    }

    public GroupComposite getRoot() { return root; }

    public void notifyChanged(String what) {
        notifyObservers(GameEvent.modelChanged(what));
    }

    public void add(GroupComponent parent, GroupComponent child) {
        parent.add(child);
        notifyChanged("Groupe '" + parent.getName() + "'");
    }

    public void remove(GroupComponent parent, GroupComponent child) {
        parent.remove(child);
        notifyChanged("Groupe '" + parent.getName() + "'");
    }
}


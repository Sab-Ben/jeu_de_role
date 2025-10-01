package com.rpg.observer;

/**
 * Observateur qui logue les événements sur la console.
 * Tu pourras plus tard remplacer par un JTextArea (Swing) facilement.
 */
public class BattleLogObserver implements Observer {
    @Override
    public void update(GameEvent event) {
        System.out.println(event);
    }
}


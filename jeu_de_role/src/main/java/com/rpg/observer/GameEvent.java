package com.rpg.observer;

/**
 * Petit POJO d'événements pour le journal et la vue.
 */
public class GameEvent {
    public enum Type { ATTACK, DEFEND, POWER, MODEL_CHANGED }

    private final Type type;
    private final String message;

    private GameEvent(Type type, String message) {
        this.type = type; this.message = message;
    }

    public static GameEvent attack(String from, String to, int dmg, int hpLeft) {
        return new GameEvent(Type.ATTACK,
                from + " inflige " + dmg + " à " + to + " (HP " + to + " = " + hpLeft + ")");
    }

    public static GameEvent defend(String who, int boost) {
        return new GameEvent(Type.DEFEND, who + " se met en garde (+" + boost + " DEF temporaire)");
    }

    public static GameEvent power(String from, String to, int dmg, int hpLeft) {
        return new GameEvent(Type.POWER,
                from + " utilise un pouvoir sur " + to + " (" + dmg + " dégâts, HP " + to + " = " + hpLeft + ")");
    }

    public static GameEvent modelChanged(String what) {
        return new GameEvent(Type.MODEL_CHANGED, what + " mis à jour.");
    }

    public Type getType() { return type; }
    public String getMessage() { return message; }

    @Override public String toString() { return "["+type+"] "+message; }
}


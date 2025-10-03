// src/main/java/com/rpg/observer/GameEvent.java
package com.rpg.observer;

public class GameEvent {
    public enum Type { ATTACK, DEFEND, POWER, MODEL_CHANGED, STALEMATE }

    private final Type type;
    private final String actor, target;
    private final int damage, hpLeft, round;
    private final String what;

    private GameEvent(Type t, String actor, String target, int damage, int hpLeft, int round, String what) {
        this.type=t; this.actor=actor; this.target=target; this.damage=damage; this.hpLeft=hpLeft; this.round=round; this.what=what;
    }

    public static GameEvent attack(String from, String to, int dmg, int hpLeft, int round) {
        return new GameEvent(Type.ATTACK, from, to, dmg, hpLeft, round, null);
    }
    public static GameEvent defend(String who, int round) {
        return new GameEvent(Type.DEFEND, who, null, 0, 0, round, null);
    }
    public static GameEvent power(String from, String to, int dmg, int hpLeft, int round) {
        return new GameEvent(Type.POWER, from, to, dmg, hpLeft, round, null);
    }
    public static GameEvent modelChanged(String what) {
        return new GameEvent(Type.MODEL_CHANGED, null, null, 0, 0, 0, what);
    }
    public static GameEvent stalemate(int round) {
        return new GameEvent(Type.STALEMATE, null, null, 0, 0, round, "Cap de tours atteint");
    }

    public Type getType(){ return type; }
    public String getActor(){ return actor; }
    public String getTarget(){ return target; }
    public int getDamage(){ return damage; }
    public int getHpLeft(){ return hpLeft; }
    public int getRound(){ return round; }
    public String getWhat(){ return what; }

    @Override public String toString() {
        // Log structuré (lisible + exploitable)
        return String.format("{type:%s, round:%d, actor:%s, target:%s, dmg:%d, hpLeft:%d, what:%s}",
                type, round, actor, target, damage, hpLeft, what);
    }
}

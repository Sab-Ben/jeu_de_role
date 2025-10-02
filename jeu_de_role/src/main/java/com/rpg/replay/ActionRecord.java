package com.rpg.replay;

/**
 * Représente une action "compacte" pour l'historique.
 * Format de ligne: TYPE;ACTOR;TARGET
 *   - TYPE ∈ {ATTACK, DEFEND, POWER}
 *   - ACTOR : nom du personnage qui agit
 *   - TARGET : nom de la cible (peut être vide pour DEFEND)
 */
public class ActionRecord {

    public enum Type { ATTACK, DEFEND, POWER }

    private final Type type;
    private final String actor;
    private final String target; // peut être ""

    public ActionRecord(Type type, String actor, String target) {
        if (type == null) throw new IllegalArgumentException("type null");
        if (actor == null || actor.isBlank()) throw new IllegalArgumentException("actor vide");
        this.type = type;
        this.actor = actor;
        this.target = (target == null) ? "" : target;
    }

    public Type getType()   { return type; }
    public String getActor(){ return actor; }
    public String getTarget(){ return target; }

    /** Sérialise vers une ligne texte: TYPE;ACTOR;TARGET */
    public String toLine() {
        return type.name() + ";" + actor + ";" + target;
    }

    /** Parse une ligne texte ; lève IllegalArgumentException si invalide. */
    public static ActionRecord parse(String line) {
        String[] p = line.trim().split(";", -1);
        if (p.length < 2) throw new IllegalArgumentException("Ligne invalide: " + line);
        Type t = Type.valueOf(p[0].trim());
        String actor = p[1].trim();
        String target = (p.length >= 3 ? p[2].trim() : "");
        return new ActionRecord(t, actor, target);
    }

    @Override public String toString() { return toLine(); }
}


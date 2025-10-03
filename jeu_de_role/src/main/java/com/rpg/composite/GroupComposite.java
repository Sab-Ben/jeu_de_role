// src/main/java/com/rpg/composite/GroupComposite.java
package com.rpg.composite;

import com.rpg.core.Character;
import com.rpg.settings.GameSettings;

import java.util.*;

public class GroupComposite implements GroupComponent {
    private final String name;
    private final List<GroupComponent> children = new ArrayList<>();

    public GroupComposite(String name) { this.name = Objects.requireNonNull(name); }

    @Override public String getName(){ return name; }

    @Override
    public int getPowerLevel() {
        return children.stream().mapToInt(GroupComponent::getPowerLevel).sum();
    }

    @Override
    public List<Character> flatten() {
        List<Character> all = new ArrayList<>();
        for (GroupComponent c : children) all.addAll(c.flatten());
        return Collections.unmodifiableList(all);
    }

    @Override
    public void add(GroupComponent child) {
        Objects.requireNonNull(child);
        // Cap max depuis GameSettings
        int cap = GameSettings.getInstance().getMaxMembersPerGroup();
        if (children.size() >= cap) throw new IllegalStateException("Cap membres atteint: " + cap);

        // Pas de doublon direct
        if (children.contains(child)) throw new IllegalArgumentException("Doublon interdit");

        // Pas de cycle (on interdit d’ajouter un ancêtre indirect)
        if (createsCycle(child)) throw new IllegalArgumentException("Cycle détecté");

        children.add(child);
    }

    private boolean createsCycle(GroupComponent candidate) {
        if (candidate == this) return true;
        if (candidate instanceof GroupComposite gc) {
            for (GroupComponent c : gc.children) {
                if (createsCycle(c)) return true;
            }
        }
        return false;
    }

    @Override public void remove(GroupComponent child) { children.remove(child); }

    public List<GroupComponent> getChildren(){ return Collections.unmodifiableList(children); }
}

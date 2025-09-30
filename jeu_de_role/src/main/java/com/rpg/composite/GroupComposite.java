package com.rpg.composite;

import com.rpg.core.Character;
import com.rpg.settings.GameSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite : peut contenir d'autres GroupComponent (GroupComposite ou CharacterLeaf).
 * - Respecte la limite max de membres (directs) définie dans GameSettings.
 * - getPowerLevel() somme récursive.
 */
public class GroupComposite implements GroupComponent {
    private final String name;
    private final List<GroupComponent> children = new ArrayList<>();

    public GroupComposite(String name) { this.name = name; }

    @Override public String getName() { return name; }

    @Override
    public int getPowerLevel() {
        return children.stream().mapToInt(GroupComponent::getPowerLevel).sum();
    }

    @Override
    public List<Character> flatten() {
        List<Character> out = new ArrayList<>();
        for (GroupComponent gc : children) out.addAll(gc.flatten());
        return Collections.unmodifiableList(out);
    }

    @Override
    public void add(GroupComponent child) {
        int limit = GameSettings.getInstance().getMaxMembersPerGroup();
        if (children.size() >= limit) {
            throw new IllegalStateException("Trop de membres dans '" + name + "' (max=" + limit + ").");
        }
        children.add(child);
    }

    @Override
    public void remove(GroupComponent child) {
        children.remove(child);
    }

    public List<GroupComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }
}


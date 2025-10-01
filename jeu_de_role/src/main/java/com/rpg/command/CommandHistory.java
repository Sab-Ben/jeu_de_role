package com.rpg.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Historique/replay des commandes.
 * - push/pop pour undo
 * - replay() pour rejouer toutes les actions dans l'ordre
 */
public class CommandHistory {
    private final List<GameCommand> stack = new ArrayList<>();

    public void push(GameCommand c) { stack.add(c); }
    public GameCommand pop() { return stack.isEmpty() ? null : stack.remove(stack.size()-1); }
    public List<GameCommand> all() { return Collections.unmodifiableList(stack); }

    public void undoLast() {
        GameCommand c = pop();
        if (c != null) c.undo();
    }

    public void replay() {
        for (GameCommand c : stack) c.execute();
    }
}


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
        // Copie immuable de l'historique pour ne pas le modifier pendant le replay
        List<GameCommand> history = new ArrayList<>(stack);

        // 1) Revenir à l'état initial (undo du dernier au premier) SANS toucher à la pile
        for (int i = history.size() - 1; i >= 0; i--) {
            history.get(i).undo();
        }

        // 2) Rejouer dans l'ordre ; on retrouve les mêmes calculs/HP que la 1ère fois
        for (GameCommand c : history) {
            c.execute();
        }
    }

}


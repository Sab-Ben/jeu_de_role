package com.rpg.command;

/**
 * Contrat Command : chaque action est exécutable et réversible.
 * - execute() applique l'effet
 * - undo() tente d'annuler (ici on garde de la simplicité)
 */
public interface GameCommand {
    void execute();
    void undo();
    String label();
}


package com.rpg.command;

import com.rpg.replay.ActionRecord;

/**
 * Marqueur "enregistrable" des commandes :
 * - permet d'extraire un ActionRecord pour sauvegarde/replay de la séquence.
 */
public interface RecordableCommand extends GameCommand {
    ActionRecord toRecord();
}


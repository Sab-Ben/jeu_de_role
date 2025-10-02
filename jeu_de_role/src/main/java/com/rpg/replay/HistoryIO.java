package com.rpg.replay;

import com.rpg.battle.Battle;
import com.rpg.battle.BattleUnit;
import com.rpg.command.AttackCommand;
import com.rpg.command.CommandHistory;
import com.rpg.command.DefendCommand;
import com.rpg.command.GameCommand;
import com.rpg.command.RecordableCommand;
import com.rpg.command.UsePowerCommand;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitaires de :
 *  - sauvegarde d'une liste d'ActionRecord -> fichier texte,
 *  - lecture d'un fichier texte -> liste d'ActionRecord,
 *  - conversion ActionRecord -> vraies GameCommand pour un Battle donné (matching par noms).
 */
public final class HistoryIO {
    private HistoryIO() {}

    // ---------- FICHIER <-> LISTE DE RECORDS ----------

    /** Écrit les actions dans un fichier texte (1 action = 1 ligne). */
    public static void save(Path file, List<ActionRecord> records) throws IOException {
        List<String> lines = new ArrayList<>(records.size());
        for (ActionRecord r : records) lines.add(r.toLine());
        Files.write(file, lines, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    /** Lit un fichier texte d'actions en ignorant lignes vides/commentaires (#). */
    public static List<ActionRecord> load(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<ActionRecord> out = new ArrayList<>(lines.size());
        for (String l : lines) {
            l = l.trim();
            if (l.isEmpty() || l.startsWith("#")) continue;
            out.add(ActionRecord.parse(l));
        }
        return out;
    }

    // ---------- RECORDS -> COMMANDES POUR UN BATTLE DONNÉ ----------

    /**
     * Transforme des ActionRecord en GameCommand liées au Battle 'battle'.
     * On retrouve l'acteur/la cible via Battle.getUnitByName(String).
     */
    public static List<GameCommand> toCommands(Battle battle, List<ActionRecord> records) {
        List<GameCommand> cmds = new ArrayList<>(records.size());
        for (ActionRecord r : records) {
            BattleUnit actor = battle.getUnitByName(r.getActor());
            switch (r.getType()) {
                case ATTACK -> {
                    BattleUnit target = battle.getUnitByName(r.getTarget());
                    cmds.add(new AttackCommand(battle, actor, target));
                }
                case DEFEND -> cmds.add(new DefendCommand(battle, actor));
                case POWER  -> {
                    BattleUnit target = battle.getUnitByName(r.getTarget());
                    cmds.add(new UsePowerCommand(battle, actor, target));
                }
            }
        }
        return cmds;
    }

    /** Extrait la liste sérialisable depuis un CommandHistory (RecordableCommand uniquement). */
    public static List<ActionRecord> fromHistory(CommandHistory history) {
        List<ActionRecord> out = new ArrayList<>();
        for (GameCommand c : history.all()) {
            if (c instanceof RecordableCommand rc) out.add(rc.toRecord());
        }
        return out;
    }
}

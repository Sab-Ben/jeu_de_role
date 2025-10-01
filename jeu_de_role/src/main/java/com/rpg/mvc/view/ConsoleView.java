package com.rpg.mvc.view;

import com.rpg.composite.GroupComponent;
import com.rpg.composite.GroupComposite;
import com.rpg.core.Character;
import com.rpg.observer.GameEvent;
import com.rpg.observer.Observer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Vue console :
 * - affiche l'arbre de groupes (Composite)
 * - affiche une liste de personnages triée (par nom ou par puissance)
 *
 * IMPORTANT (fix) :
 *   root.flatten() renvoie une liste non modifiable.
 *   On en fait donc une COPIE modifiable via new ArrayList<>(...) avant de trier,
 *   sinon Collections$UnmodifiableList.sort(...) lève UnsupportedOperationException.
 */
public class ConsoleView implements Observer {

    /**
     * Affiche récursivement l'arborescence des groupes et feuilles.
     * @param node   noeud de départ (groupe racine ou sous-groupe)
     * @param indent indentation textuelle pour l'affichage hiérarchique
     */
    public void printTree(GroupComponent node, String indent) {
        System.out.printf("%s- %s (Power=%d)%n", indent, node.getName(), node.getPowerLevel());
        if (node instanceof GroupComposite gc) {
            for (GroupComponent child : gc.getChildren()) {
                printTree(child, indent + "  ");
            }
        }
    }

    /**
     * Affiche la liste des personnages triée.
     * @param root         racine du composite (armée, brigade, etc.)
     * @param byPowerDesc  true => trier par puissance décroissante, false => trier par nom (A→Z)
     */
    public void printSorted(GroupComponent root, boolean byPowerDesc) {
        // Copie modifiable : root.flatten() est (volontairement) non modifiable
        List<Character> chars = new ArrayList<>(root.flatten());

        if (byPowerDesc) {
            // Tri par puissance décroissante (plus lisible que l'astuce du -power)
            chars.sort(Comparator.comparingInt(Character::getPowerLevel).reversed());
        } else {
            // Tri par nom (insensible à la casse)
            chars.sort(Comparator.comparing(c -> c.getName().toLowerCase(Locale.ROOT)));
        }

        System.out.println("--- Personnages ---");
        for (Character c : chars) {
            System.out.printf("%-18s  Power=%3d  %s%n",
                    c.getName(), c.getPowerLevel(), c.getDescription());
        }
    }

    /**
     * Réception des notifications (Observer) :
     * on affiche simplement le message. La vue est ainsi tenue à jour
     * quand le modèle ou le combat émettent des événements.
     */
    @Override
    public void update(GameEvent event) {
        System.out.println("[Vue] " + event.getMessage());
    }
}

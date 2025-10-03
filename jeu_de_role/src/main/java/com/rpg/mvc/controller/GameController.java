package com.rpg.mvc.controller;

import com.rpg.builder.CharacterBuilder;
import com.rpg.composite.CharacterLeaf;
import com.rpg.composite.GroupComposite;
import com.rpg.core.Character;
import com.rpg.dao.CharacterDAO;
import com.rpg.mvc.model.PartyModel;
import com.rpg.validation.ValidationException;
import com.rpg.validation.ValidationResult;

/**
 * Contrôleur : coordonne création/validation/persistance et mise à jour du modèle.
 * - AUCUNE I/O ici (console/GUI gérées ailleurs) -> testable facilement.
 * - La validation est faite par CharacterBuilder.build() (Chain of Responsibility).
 * - En cas d'erreurs, on renvoie un ValidationResult rempli au lieu de crasher.
 */
public class GameController {

    /** Petit pont pour injecter GameSettings sans coupler au singleton en dur. */
    public interface GameSettingsAware {
        com.rpg.settings.GameSettings getSettings();
    }

    private final CharacterDAO dao;
    private final PartyModel model;
    private final GameSettingsAware settingsAware;

    public GameController(CharacterDAO dao, PartyModel model, GameSettingsAware settingsAware) {
        this.dao = dao;
        this.model = model;
        this.settingsAware = settingsAware;
    }

    /**
     * Crée et persiste un personnage à partir d'un "setup" de builder fourni par l'appelant.
     * - Injecte Settings + DAO dans le builder (regex nom, somme max, unicité...).
     * - build() lance la validation (CoR) et lève ValidationException si erreurs multiples.
     * - On attrape l'exception pour peupler un ValidationResult lisible côté IHM.
     *
     * @param builderSetup lambda qui configure le builder (setName, setStrength, etc.)
     * @return ValidationResult : vide = OK ; sinon liste d'erreurs utilisateur.
     */
    public ValidationResult createAndSaveCharacter(java.util.function.Consumer<CharacterBuilder> builderSetup) {
        ValidationResult vr = new ValidationResult();

        try {
            // 1) Builder prêt à l’emploi avec Settings + DAO (pour l’unicité)
            CharacterBuilder b = new CharacterBuilder()
                    .withSettings(settingsAware.getSettings())
                    .withDao(dao);

            // 2) Configuration fournie par l'appelant (console/GUI)
            builderSetup.accept(b);

            // 3) Construction + validation (CoR) -> lève ValidationException si erreurs
            Character c = b.build();

            // 4) Persistance si OK
            dao.save(c);

            // 5) Optionnel : notifier la vue via le modèle (si PartyModel hérite de Subject)
            // model.notifyObservers(GameEvent.modelChanged("Personnage " + c.getName()));

            // Rien à ajouter dans vr => OK
        } catch (ValidationException ve) {
            // On transforme l’exception en messages lisibles pour l’IHM
            for (String err : ve.getErrors()) {
                vr.addError(err);
            }
            // Pas de rethrow : on laisse l’appelant afficher les erreurs et revenir au menu
        }

        return vr;
    }

    /**
     * Ajoute un personnage existant (trouvé par nom dans le DAO) dans un groupe.
     * Si le nom n’existe pas, cette méthode ne fait rien (comportement silencieux).
     * À toi de gérer le retour utilisateur côté IHM si besoin.
     */
    public void addCharacterTo(GroupComposite parent, String characterName) {
        dao.findByName(characterName).ifPresent(c -> {
            model.add(parent, new CharacterLeaf(c));
            // Optionnel : model.notifyObservers(GameEvent.modelChanged("Ajout de " + c.getName() + " à " + parent.getName()));
        });
    }

    public PartyModel getModel() { return model; }
}

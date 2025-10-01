package com.rpg.mvc.controller;

import com.rpg.builder.CharacterBuilder;
import com.rpg.composite.CharacterLeaf;
import com.rpg.composite.GroupComposite;
import com.rpg.core.Character;
import com.rpg.dao.CharacterDAO;
import com.rpg.mvc.model.PartyModel;
import com.rpg.validation.CharacterValidationService;
import com.rpg.validation.ValidationContext;
import com.rpg.validation.ValidationResult;

import java.util.function.Consumer;

/**
 * Contrôleur : coordonne création/validation/persistance et mise à jour du modèle.
 * Ici on reste "console friendly" (pas d'IO direct), le Main jouera le rôle d'IHM.
 */
public class
GameController {
    private final CharacterDAO dao;
    private final PartyModel model;
    private final CharacterValidationService validator;

    public GameController(CharacterDAO dao, PartyModel model, GameSettingsAware settingsAware) {
        this.dao = dao;
        this.model = model;

        ValidationContext ctx = new ValidationContext(settingsAware.getSettings(), dao);
        this.validator = new CharacterValidationService(ctx);
    }

    /** Helpers pour injecter GameSettings sans coupler aux singletons. */
    public interface GameSettingsAware { com.rpg.settings.GameSettings getSettings(); }

    public ValidationResult createAndSaveCharacter(Consumer<CharacterBuilder> builderSetup) {
        CharacterBuilder b = new CharacterBuilder();
        builderSetup.accept(b);
        Character c = b.build();

        ValidationResult vr = validator.validate(c);
        if (vr.isValid()) dao.save(c);
        return vr;
    }

    public void addCharacterTo(GroupComposite parent, String characterName) {
        dao.findByName(characterName).ifPresent(c -> {
            model.add(parent, new CharacterLeaf(c));
        });
    }

    public PartyModel getModel() { return model; }
}


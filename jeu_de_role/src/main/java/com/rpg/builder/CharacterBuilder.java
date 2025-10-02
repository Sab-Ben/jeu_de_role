package com.rpg.builder;

import java.util.ArrayList;
import java.util.List;

import com.rpg.core.Character;
import com.rpg.dao.CharacterDAO;
import com.rpg.settings.GameSettings;
import com.rpg.validation.*;
import com.rpg.validation.chain.ValidationChainBuilder;

/**
 * Builder fluide + validation systématique sur build().
 */
public class CharacterBuilder {
    private String name = "SansNom";
    private int strength = 1, agility = 1, intelligence = 1, attack = 1, defense = 0;
    private final List<String> equipment = new ArrayList<>();

    // Contexte de validation injecté (DAO + Settings)
    private GameSettings settings = GameSettings.getInstance();
    private CharacterDAO dao; // à setter si on veut vérifier l’unicité par DAO

    public CharacterBuilder setName(String name){ this.name = name; return this; }
    public CharacterBuilder setStrength(int v){ this.strength = v; return this; }
    public CharacterBuilder setAgility(int v){ this.agility = v; return this; }
    public CharacterBuilder setIntelligence(int v){ this.intelligence = v; return this; }
    public CharacterBuilder setAttack(int v){ this.attack = v; return this; }
    public CharacterBuilder setDefense(int v){ this.defense = v; return this; }
    public CharacterBuilder addEquipment(String item){ if(item!=null && !item.isBlank()) equipment.add(item); return this; }

    /** Injection optionnelle du contexte */
    public CharacterBuilder withSettings(GameSettings s){ this.settings = s; return this; }
    public CharacterBuilder withDao(CharacterDAO dao){ this.dao = dao; return this; }

    public Character build() {
        Character c = new Character(name, strength, agility, intelligence, attack, defense, equipment);

        // 1) Construire la chaîne selon les règles actives
        Validator chain = ValidationChainBuilder.start()
                .add(new NameFormatValidator())   // utilise regex de GameSettings via NameFormatValidator
                .add(dao != null ? new UniqueNameValidator() : new NoopValidator()) // unicité si DAO présent
                .add(new StatTotalValidator())
                .build();

        // 2) Valider et agréger
        ValidationResult result = new ValidationResult();
        ValidationContext ctx = new ValidationContext(settings, dao);
        chain.validate(c, ctx, result);

        if (!result.isValid()) {
            // 3) Une seule exception qui porte toutes les erreurs
            throw new ValidationException(result.getErrors());
        }
        return c;
    }

    /** Maillon neutre quand un validateur est optionnel */
    static class NoopValidator extends BaseValidator {
        @Override public void validate(Character c, ValidationContext ctx, ValidationResult res) { next(c, ctx, res); }
    }
}

package com.izinvest_common.main;

import java.util.List;
import java.util.function.BiConsumer;

import com.izinvest_common.builder.CharacterBuilder;
import com.izinvest_common.core.Character;
import com.izinvest_common.core.Party;
import com.izinvest_common.dao.CharacterDAO;
import com.izinvest_common.decorator.BaseComponent;
import com.izinvest_common.decorator.CharacterComponent;
import com.izinvest_common.decorator.FireResistance;
import com.izinvest_common.decorator.Invisibility;
import com.izinvest_common.decorator.Telepathy;
import com.izinvest_common.settings.GameSettings;

public class Main {

    public static void main(String[] args) {

        GameSettings settings = GameSettings.getInstance();
        settings.setMaxStatPoints(300); // on assouplit la règle pour la démo

        CharacterDAO dao = new CharacterDAO();

        // Petite lambda utilitaire : sauvegarde si valide + logging
        BiConsumer<String, Character> saveIfValid = (label, ch) -> {
            if (settings.isValid(ch)) {
                dao.save(ch);
                System.out.println("OK  : " + label + " => " + ch);
            } else {
                System.out.println("ERREUR: " + label + " viole les règles (max=" + settings.getMaxStatPoints() + ")");
            }
        };

        // ---- Création des personnages via le BUILDER ----
        Character chevalier = new CharacterBuilder()
                .setName("Chevalier")
                .setStrength(18).setAgility(12).setIntelligence(10)
                .setAttack(20).setDefense(22)
                .addEquipment("Épée longue").addEquipment("Bouclier").addEquipment("Armure de plates")
                .build();

        Character guerrier = new CharacterBuilder()
                .setName("Guerrier")
                .setStrength(20).setAgility(14).setIntelligence(8)
                .setAttack(22).setDefense(16)
                .addEquipment("Hache double").addEquipment("Casque de fer")
                .build();

        Character magicien = new CharacterBuilder()
                .setName("Magicien")
                .setStrength(8).setAgility(12).setIntelligence(24)
                .setAttack(16).setDefense(10)
                .addEquipment("Bâton runique").addEquipment("Grimoire ancien")
                .build();

        Character dragon = new CharacterBuilder()
                .setName("Dragon")
                .setStrength(28).setAgility(16).setIntelligence(14)
                .setAttack(30).setDefense(26)
                .addEquipment("Écailles légendaires")
                .build();

        Character zombi = new CharacterBuilder()
                .setName("Zombi")
                .setStrength(12).setAgility(6).setIntelligence(2)
                .setAttack(10).setDefense(8)
                .addEquipment("Mâchoires pourries")
                .build();

        Character robot = new CharacterBuilder()
                .setName("Robot")
                .setStrength(22).setAgility(10).setIntelligence(16)
                .setAttack(24).setDefense(24)
                .addEquipment("Blindage composite").addEquipment("Servo-moteurs")
                .build();

        Character ours = new CharacterBuilder()
                .setName("Ours")
                .setStrength(24).setAgility(12).setIntelligence(6)
                .setAttack(18).setDefense(14)
                .addEquipment("Griffes").addEquipment("Fourrure épaisse")
                .build();

        Character tigre = new CharacterBuilder()
                .setName("Tigre")
                .setStrength(20).setAgility(22).setIntelligence(8)
                .setAttack(20).setDefense(12)
                .addEquipment("Crocs affûtés").addEquipment("Pelage camouflé")
                .build();

        // ---- Persistance si valides ----
        saveIfValid.accept("Chevalier", chevalier);
        saveIfValid.accept("Guerrier", guerrier);
        saveIfValid.accept("Magicien", magicien);
        saveIfValid.accept("Dragon", dragon);
        saveIfValid.accept("Zombi", zombi);
        saveIfValid.accept("Robot", robot);
        saveIfValid.accept("Ours", ours);
        saveIfValid.accept("Tigre", tigre);

        System.out.println("\n--- Application de capacités (Decorator) ---");
        CharacterComponent chevalierCampe = new Invisibility(new BaseComponent(chevalier));
        CharacterComponent magicienPsy = new Telepathy(new BaseComponent(magicien));
        CharacterComponent dragonIgnifuge = new FireResistance(new BaseComponent(dragon));

        System.out.println(chevalierCampe.getDescription() + " | Power=" + chevalierCampe.getPowerLevel());
        System.out.println(magicienPsy.getDescription() + " | Power=" + magicienPsy.getPowerLevel());
        System.out.println(dragonIgnifuge.getDescription() + " | Power=" + dragonIgnifuge.getPowerLevel());

        // ---- Collections / Party ----
        System.out.println("\n--- Équipe A (Chevalier, Magicien, Tigre) ---");
        Party partyA = new Party();
        partyA.add(chevalier);
        partyA.add(magicien);
        partyA.add(tigre);
        partyA.sortByPowerDesc();
        partyA.getMembers().forEach(System.out::println);
        System.out.println("Puissance totale A = " + partyA.getTotalPower());

        System.out.println("\n--- Équipe B (Guerrier, Dragon, Robot) ---");
        Party partyB = new Party();
        partyB.add(guerrier);
        partyB.add(dragon);
        partyB.add(robot);
        partyB.sortByPowerDesc();
        partyB.getMembers().forEach(System.out::println);
        System.out.println("Puissance totale B = " + partyB.getTotalPower());

        // ---- Tri global par nom ----
        System.out.println("\n--- Tous les personnages triés par nom ---");
        List<Character> all = dao.findAll();
        all.sort((a,b) -> a.getName().compareToIgnoreCase(b.getName()));
        all.forEach(System.out::println);

        // ---- Mini simulation de combat ----
        System.out.println("\n--- Duel: Chevalier vs Dragon ---");
        simulateDuel(chevalier, dragon);

        System.out.println("\n--- Duel: Magicien (Télépathie) vs Robot ---");
        // Pour la simulation, on pourrait utiliser la puissance décorée pour une variante ;
        // ici on reste sur l'objet "nu" (sans effet) pour illustrer.
        simulateDuel(magicienPsy.getInner(), robot);
    }

    /**
     * Simulation ultra-simple : 5 rounds max, à chaque round
     * - dégâts = ATK + (AGI/4) - (DEF/2), minimum 1
     * - points de vie virtuels = 100 au départ
     */
    private static void simulateDuel(Character a, Character b) {
        int hpA = 100, hpB = 100;
        for (int round = 1; round <= 5 && hpA > 0 && hpB > 0; round++) {
            int dmgA = Math.max(1, a.getAttack() + (a.getAgility()/4) - (b.getDefense()/2));
            int dmgB = Math.max(1, b.getAttack() + (b.getAgility()/4) - (a.getDefense()/2));
            hpB -= dmgA;
            hpA -= dmgB;
            System.out.printf("Round %d: %s inflige %d | %s inflige %d | HP(%s)=%d, HP(%s)=%d%n",
                    round, a.getName(), dmgA, b.getName(), dmgB, a.getName(), hpA, b.getName(), hpB);
        }
        if (hpA == hpB) {
            System.out.println("Résultat: Égalité !");
        } else if (hpA > hpB) {
            System.out.println("Vainqueur: " + a.getName());
        } else {
            System.out.println("Vainqueur: " + b.getName());
        }
    }
}
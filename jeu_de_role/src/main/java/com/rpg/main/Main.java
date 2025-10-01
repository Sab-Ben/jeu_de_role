package com.rpg.main;

import com.rpg.battle.Battle;
import com.rpg.battle.SimpleDamageStrategy;
import com.rpg.command.*;
import com.rpg.composite.GroupComposite;
import com.rpg.core.Character;
import com.rpg.dao.CharacterDAO;
import com.rpg.mvc.controller.GameController;
import com.rpg.mvc.model.PartyModel;
import com.rpg.mvc.view.ConsoleView;
import com.rpg.observer.BattleLogObserver;
import com.rpg.settings.GameSettings;
import com.rpg.validation.ValidationResult;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // --- Settings globaux ---
        GameSettings settings = GameSettings.getInstance();
        settings.setMaxStatPoints(250);
        settings.setMaxMembersPerGroup(10);

        // --- DAO / Modèle / Vue / Contrôleur ---
        CharacterDAO dao = new CharacterDAO();
        PartyModel model = new PartyModel("Armée Alpha");
        ConsoleView view = new ConsoleView();
        model.addObserver(view);

        GameController ctrl = new GameController(dao, model, () -> settings);

        // --- Root group ---
        GroupComposite armee = model.getRoot();

        // --- Menu interactif ---
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Créer un personnage");
            System.out.println("2. Afficher tous les personnages");
            System.out.println("3. Ajouter un personnage à un groupe");
            System.out.println("4. Lancer un combat");
            System.out.println("5. Rejouer la dernière séquence de combat");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            String choix = sc.nextLine();

            switch (choix) {
                case "1" -> {
                    System.out.print("Nom du personnage : ");
                    String name = sc.nextLine();

                    System.out.print("Force : ");
                    int str = Integer.parseInt(sc.nextLine());
                    System.out.print("Agilité : ");
                    int agi = Integer.parseInt(sc.nextLine());
                    System.out.print("Intelligence : ");
                    int intel = Integer.parseInt(sc.nextLine());
                    System.out.print("Attaque : ");
                    int atk = Integer.parseInt(sc.nextLine());
                    System.out.print("Défense : ");
                    int def = Integer.parseInt(sc.nextLine());

                    // --- Création avec Builder ---
                    ValidationResult result = ctrl.createAndSaveCharacter(b -> {
                        b.setName(name)
                                .setStrength(str)
                                .setAgility(agi)
                                .setIntelligence(intel)
                                .setAttack(atk)
                                .setDefense(def);

                        // Boucle pour ajouter les équipements
                        boolean ajoutEquipement = true;
                        while (ajoutEquipement) {
                            System.out.print("Ajouter un équipement (ou taper ENTER pour arrêter) : ");
                            String equip = sc.nextLine();
                            if (equip.isBlank()) {
                                ajoutEquipement = false;
                            } else {
                                b.addEquipment(equip);
                            }
                        }
                    });

                    System.out.println("Résultat : " + result);
                }


                case "2" -> {
                    System.out.println("\n== PERSONNAGES ENREGISTRÉS ==");
                    dao.findAll().forEach(c ->
                            System.out.println(c.getName() + " (Puissance=" + c.getPowerLevel() + ")"));

                    System.out.println("\n== LISTE TRIEE DANS L'ARBORESCENCE ==");
                    view.printSorted(armee, false);
                }


                case "3" -> {
                    System.out.print("Nom du personnage à ajouter : ");
                    String name = sc.nextLine();
                    ctrl.addCharacterTo(armee, name);
                }

                case "4" -> {
                    System.out.print("Nom du premier combattant : ");
                    String n1 = sc.nextLine();
                    System.out.print("Nom du second combattant : ");
                    String n2 = sc.nextLine();

                    Optional<Character> c1 = dao.findByName(n1);
                    Optional<Character> c2 = dao.findByName(n2);

                    if (c1.isPresent() && c2.isPresent()) {
                        Battle battle = new Battle(c1.get(), c2.get(), new SimpleDamageStrategy());
                        battle.addObserver(new BattleLogObserver());
                        battle.addObserver(view);

                        CommandHistory history = new CommandHistory();

                        System.out.println("\n=== COMBAT TOUR PAR TOUR ===");
                        boolean turnA = true; // alterne entre A et B

                        while (!battle.isOver()) {
                            Character activeChar = turnA ? battle.getUnitA().getCharacter() : battle.getUnitB().getCharacter();
                            Character targetChar = turnA ? battle.getUnitB().getCharacter() : battle.getUnitA().getCharacter();

                            System.out.println("\nTour de " + activeChar.getName() + " (HP=" +
                                    (turnA ? battle.getUnitA().getHp() : battle.getUnitB().getHp()) + ")");
                            System.out.println("Actions disponibles : 1=Attaquer, 2=Défendre, 3=Pouvoir");
                            System.out.print("Choisir une action : ");
                            String action = sc.nextLine();

                            GameCommand cmd;
                            switch (action) {
                                case "1" -> cmd = new AttackCommand(battle, turnA ? battle.getUnitA() : battle.getUnitB(),
                                        turnA ? battle.getUnitB() : battle.getUnitA());
                                case "2" -> cmd = new DefendCommand(battle, turnA ? battle.getUnitA() : battle.getUnitB());
                                case "3" -> cmd = new UsePowerCommand(battle, turnA ? battle.getUnitA() : battle.getUnitB(),
                                        turnA ? battle.getUnitB() : battle.getUnitA());
                                default -> {
                                    System.out.println("Action invalide, attaque par défaut");
                                    cmd = new AttackCommand(battle, turnA ? battle.getUnitA() : battle.getUnitB(),
                                            turnA ? battle.getUnitB() : battle.getUnitA());
                                }
                            }

                            history.push(cmd);
                            cmd.execute();

                            turnA = !turnA; // passe au joueur suivant
                        }

                        System.out.println("\n== FIN DU COMBAT ==");
                        String winner = battle.winnerNameOrNull();
                        System.out.println(winner != null ? "Vainqueur : " + winner : "Égalité !");
                    } else {
                        System.out.println("Un ou les deux personnages sont introuvables.");
                    }
                }



                case "5" -> {
                    // à stocker globalement si tu veux rejouer après un combat
                    System.out.println("(TODO) Rejouer la séquence de combat");
                }

                case "0" -> {
                    running = false;
                    System.out.println("Au revoir !");
                }

                default -> System.out.println("Choix invalide.");
            }
        }

        sc.close();
    }
}

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

// ✅ US 4.3 : imports pour l'enregistrement / replay depuis fichier
import com.rpg.replay.ActionRecord;
import com.rpg.replay.HistoryIO;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    /**
     * Point d'entrée console unifié :
     * - 1) Création de personnage (Builder + Validation + DAO)
     * - 2) Listing/affichage (Vue)
     * - 3) Composite : ajouter un personnage au groupe racine
     * - 4) Combat tour par tour (Command + Observer) -> enregistre l'historique en mémoire
     * - 5) Replay "en mémoire" du dernier combat (rejoue les mêmes commandes)
     * - 6) 💾 Enregistrer le dernier combat dans un fichier texte (US 4.3)
     * - 7) 📖 Charger un fichier texte et rejouer la séquence (US 4.3)
     */
    public static void main(String[] args) {
        // Pointeur vers le dernier historique de combat joué (en mémoire)
        CommandHistory lastHistory = null;

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
            System.out.println("5. Rejouer la dernière séquence de combat (mémoire)");
            System.out.println("6. Enregistrer la dernière séquence dans un fichier (US 4.3)");
            System.out.println("7. Charger un fichier et rejouer (US 4.3)");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            String choix = sc.nextLine().trim();

            switch (choix) {
                case "1" -> {
                    System.out.print("Nom du personnage : ");
                    String name = sc.nextLine().trim();

                    System.out.print("Force : ");
                    int str = safeInt(sc.nextLine());
                    System.out.print("Agilité : ");
                    int agi = safeInt(sc.nextLine());
                    System.out.print("Intelligence : ");
                    int intel = safeInt(sc.nextLine());
                    System.out.print("Attaque : ");
                    int atk = safeInt(sc.nextLine());
                    System.out.print("Défense : ");
                    int def = safeInt(sc.nextLine());

                    // --- Création avec Builder ---
                    ValidationResult result = ctrl.createAndSaveCharacter(b -> {
                        b.setName(name)
                                .setStrength(str)
                                .setAgility(agi)
                                .setIntelligence(intel)
                                .setAttack(atk)
                                .setDefense(def);

                        // Boucle pour ajouter les équipements
                        while (true) {
                            System.out.print("Ajouter un équipement (ENTER pour arrêter) : ");
                            String equip = sc.nextLine();
                            if (equip.isBlank()) break;
                            b.addEquipment(equip);
                        }
                    });

                    System.out.println("Résultat : " + result);
                }

                case "2" -> {
                    System.out.println("\n== PERSONNAGES ENREGISTRÉS ==");
                    dao.findAll().forEach(c ->
                            System.out.println(c.getName() + " (Puissance=" + c.getPowerLevel() + ")"));

                    System.out.println("\n== ARMEE ALPHA ==");
                    view.printSorted(armee, false);
                }

                case "3" -> {
                    System.out.print("Nom du personnage à ajouter : ");
                    String name = sc.nextLine().trim();
                    ctrl.addCharacterTo(armee, name);
                }

                case "4" -> {
                    // === Combat TOUR PAR TOUR (Command + Observer) ===
                    System.out.print("Nom du premier combattant : ");
                    String n1 = sc.nextLine().trim();
                    System.out.print("Nom du second combattant : ");
                    String n2 = sc.nextLine().trim();

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
                            System.out.println("\nTour de " + activeChar.getName() + " (HP=" +
                                    (turnA ? battle.getUnitA().getHp() : battle.getUnitB().getHp()) + ")");
                            System.out.println("Actions : 1=Attaquer, 2=Défendre, 3=Pouvoir");
                            System.out.print("Choisir une action : ");
                            String action = sc.nextLine().trim();

                            GameCommand cmd;
                            switch (action) {
                                case "1" -> cmd = new AttackCommand(
                                        battle,
                                        turnA ? battle.getUnitA() : battle.getUnitB(),
                                        turnA ? battle.getUnitB() : battle.getUnitA()
                                );
                                case "2" -> cmd = new DefendCommand(
                                        battle,
                                        turnA ? battle.getUnitA() : battle.getUnitB()
                                );
                                case "3" -> cmd = new UsePowerCommand(
                                        battle,
                                        turnA ? battle.getUnitA() : battle.getUnitB(),
                                        turnA ? battle.getUnitB() : battle.getUnitA()
                                );
                                default -> {
                                    System.out.println("Action invalide, attaque par défaut.");
                                    cmd = new AttackCommand(
                                            battle,
                                            turnA ? battle.getUnitA() : battle.getUnitB(),
                                            turnA ? battle.getUnitB() : battle.getUnitA()
                                    );
                                }
                            }

                            // Enregistre l'action DANS L'HISTORIQUE puis exécute
                            history.push(cmd);
                            cmd.execute();

                            turnA = !turnA; // passe au joueur suivant
                        }

                        System.out.println("\n== FIN DU COMBAT ==");
                        String winner = battle.winnerNameOrNull();
                        System.out.println(winner != null ? "Vainqueur : " + winner : "Égalité !");

                        // ➕ Sauvegarde de l'historique en mémoire (sera exploité par 5/6)
                        lastHistory = history;

                        // ➕ Question rapide : proposer d'écrire le fichier tout de suite
                        System.out.print("Enregistrer l'historique dans un fichier maintenant ? (o/N) ");
                        String ans = sc.nextLine().trim().toLowerCase(Locale.ROOT);
                        if (ans.equals("o") || ans.equals("oui") || ans.equals("y")) {
                            handleSaveHistoryInteractive(sc, lastHistory);
                        }

                    } else {
                        System.out.println("Un ou les deux personnages sont introuvables.");
                    }
                }

                case "5" -> {
                    // == REPLAY MÉMOIRE sur un ÉTAT NEUF ==
                    if (lastHistory == null || lastHistory.all().isEmpty()) {
                        System.out.println("Aucun combat n'a encore été joué.");
                        break;
                    }

                    try {
                        // 1) On sérialise l'historique mémoire en records
                        var records = com.rpg.replay.HistoryIO.fromHistory(lastHistory);

                        // 2) On récupère les 2 noms de combattants présents dans ces records
                        String[] names = extractTwoNames(records); // helper ci-dessous
                        String nA = names[0], nB = names[1];

                        var cA = dao.findByName(nA);
                        var cB = dao.findByName(nB);
                        if (cA.isEmpty() || cB.isEmpty()) {
                            System.out.println("Personnage(s) introuvable(s) dans le DAO : " + java.util.Arrays.toString(names));
                            System.out.println("Crée-les (menu 1) ou corrige les noms dans le fichier/records.");
                            break;
                        }

                        // 3) On relance un Battle tout neuf
                        Battle replay = new Battle(cA.get(), cB.get(), new SimpleDamageStrategy());
                        replay.addObserver(new BattleLogObserver());
                        replay.addObserver(view);

                        // 4) On reconstruit des Command pour CE nouveau Battle et on exécute
                        var cmds = com.rpg.replay.HistoryIO.toCommands(replay, records);
                        System.out.println("\n== REPLAY DU DERNIER COMBAT (mémoire, état neuf) ==");
                        for (var cmd : cmds) cmd.execute();

                        if (replay.isOver()) {
                            String w = replay.winnerNameOrNull();
                            System.out.println("\n== FIN DU COMBAT (replay) ==");
                            System.out.println(w == null ? "Égalité !" : "Vainqueur : " + w);
                        }
                    } catch (Exception ex) {
                        System.out.println("Erreur pendant le replay mémoire : " + ex.getMessage());
                    }
                }

                case "6" -> {
                    // === US 4.3 : Enregistrer le dernier combat dans un fichier ===
                    if (lastHistory == null) {
                        System.out.println("Aucun combat à enregistrer (joue d'abord un combat).");
                    } else {
                        handleSaveHistoryInteractive(sc, lastHistory);
                    }
                }

                case "7" -> {
                    // === US 4.3 : Charger un fichier d'actions et rejouer ===
                    System.out.print("Chemin du fichier à charger : ");
                    String path = sc.nextLine().trim();
                    try {
                        var records = HistoryIO.load(Path.of(path));
                        if (records.isEmpty()) {
                            System.out.println("Fichier vide : rien à rejouer.");
                            break;
                        }

                        // On récupère les NOMS présents dans le fichier et on vérifie qu'il y en a 2.
                        Set<String> names = records.stream()
                                .flatMap(r -> {
                                    if (r.getTarget() == null || r.getTarget().isBlank())
                                        return java.util.stream.Stream.of(r.getActor());
                                    return java.util.stream.Stream.of(r.getActor(), r.getTarget());
                                })
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toCollection(LinkedHashSet::new));

                        if (names.size() != 2) {
                            System.out.println("Le fichier doit contenir exactement 2 combattants (trouvés: " + names + ").");
                            break;
                        }

                        Iterator<String> it = names.iterator();
                        String nA = it.next();
                        String nB = it.next();

                        Optional<Character> cA = dao.findByName(nA);
                        Optional<Character> cB = dao.findByName(nB);

                        if (cA.isEmpty() || cB.isEmpty()) {
                            System.out.println("Personnage(s) introuvable(s) dans le DAO : " + names);
                            System.out.println("Crée-les d'abord (menu 1) ou corrige les noms dans le fichier.");
                            break;
                        }

                        // Nouveau duel dédié au replay
                        Battle replay = new Battle(cA.get(), cB.get(), new SimpleDamageStrategy());
                        replay.addObserver(new BattleLogObserver());
                        replay.addObserver(view);

                        var cmds = HistoryIO.toCommands(replay, records);
                        System.out.println("\n== REPLAY (depuis fichier) ==");
                        for (var cmd : cmds) cmd.execute();

                        if (replay.isOver()) {
                            String w = replay.winnerNameOrNull();
                            System.out.println("\n== FIN DU COMBAT (replay) ==");
                            System.out.println(w == null ? "Égalité !" : "Vainqueur : " + w);
                        }

                    } catch (Exception ex) {
                        System.out.println("Échec lecture/replay : " + ex.getMessage());
                    }
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

    // ----------------------- Helpers -----------------------

    /** Convertit une saisie en entier ; renvoie 0 si invalide (sécurise la console). */
    private static int safeInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception ignored) { return 0; }
    }

    /** Demande un chemin et enregistre l'historique dans un fichier texte (US 4.3). */
    private static void handleSaveHistoryInteractive(Scanner sc, CommandHistory history) {
        if (history == null || history.all().isEmpty()) {
            System.out.println("Historique vide.");
            return;
        }
        System.out.print("Chemin du fichier à écrire : ");
        String path = sc.nextLine().trim();
        try {
            var records = HistoryIO.fromHistory(history); // nécessite que les commandes implémentent RecordableCommand
            HistoryIO.save(Path.of(path), records);
            System.out.println("Historique enregistré dans " + path);
        } catch (Exception ex) {
            System.out.println("Échec sauvegarde : " + ex.getMessage());
        }
    }

    /**
     * Extrait exactement 2 noms de combattants depuis la liste de records.
     * Garde l'ordre d'apparition (LinkedHashSet). Lève une erreur sinon.
     */
    private static String[] extractTwoNames(java.util.List<com.rpg.replay.ActionRecord> records) {
        java.util.LinkedHashSet<String> set = new java.util.LinkedHashSet<>();
        for (com.rpg.replay.ActionRecord r : records) {
            if (r.getActor() != null && !r.getActor().isBlank()) set.add(r.getActor().trim());
            if (r.getTarget() != null && !r.getTarget().isBlank()) set.add(r.getTarget().trim());
        }
        if (set.size() != 2)
            throw new IllegalStateException("L'historique doit contenir exactement 2 combattants, trouvés: " + set);
        return set.toArray(new String[0]);
    }
}


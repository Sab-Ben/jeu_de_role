# Jeu De Role App
Role-playing game application that generates customized characters for a role-playing game. The
characters have basic characteristics and can be dynamically assigned special abilities.
They are stored in a persistent system, and certain game rules are accessible
globally.

# Technical constraints (mandatory):
This application goes beyond a simple generator:
- Advanced character creation and validation (Builder, Singleton, Chain of Responsibility).
- Dynamically added abilities (Decorator).
- Actions and combat (Command, Observer).
- Organization into hierarchical armies/teams (Composite).
- Persistence with generic DAO.
- Minimal interface (console or Swing) in MVC.

# Authors
Our code developer's squad: 
- Litica AMANI
- Sabrina BENSEGHIR
- Julie CAUSSE 


# Product Backlog : 
## Épic 1 : Création et gestion des personnages
- US 1.1 : En tant qu’utilisateur, je veux créer un personnage en choisissant ses caractéristiques
(Builder) pour disposer d’une base jouable => Julie CAUSSE
- US 1.2 : En tant qu’utilisateur, je veux ajouter ou retirer dynamiquement des capacités spéciales
(Decorator) pour personnaliser mon personnage => Sabrina BENSEGHIR
- US 1.3 : En tant qu’utilisateur, je veux stocker mes personnages et les retrouver plus tard (DAO) => Liticia AMANI 
- US 1.4 : En tant qu’utilisateur, je veux pouvoir organiser mes personnages dans des groupes
hiérarchisés (Composite → ex. une armée composée de plusieurs parties). => Liticia AMANI

## Épic 2 : Gestion des règles du jeu et des actions
- US 2.1 : En tant qu’utilisateur, je veux que les règles globales du jeu (limite de points de stats, max
de personnages par groupe, etc.) soient centralisées et accessibles partout (Singleton). => Julie CAUSSE
- US 2.2 : En tant qu’utilisateur, je veux déclencher des actions de jeu via des commandes (Command
→ ex. attaquer, défendre, utiliser un pouvoir) pour simuler des tours de jeu. => Sabrina BENSEGHIR
- US 2.3 : En tant qu’utilisateur, je veux qu’un système de validation applique des règles dans un
enchaînement (Chain of Responsibility → ex. validation des points, validation du nom, validation
des capacités). => Sabrina BENSEGHIR

## Épic 3 : Interaction et affichage (MVC)
- US 3.1 : En tant qu’utilisateur, je veux disposer d’une interface simple (console ou Swing) suivant le
pattern MVC pour gérer mes personnages. => Sabrina BENSEGHIR
- US 3.2 : En tant qu’utilisateur, je veux visualiser les personnages et leurs pouvoirs dans une liste
triable (MVC + Observer). => Julie CAUSSE

## Épic 4 : Combat et simulation
- US 4.1 : En tant qu’utilisateur, je veux lancer un combat entre deux personnages pour comparer leurs
niveaux de puissance (Command + Strategy optionnelle si tu veux aller plus loin). => Julie CAUSSE
- US 4.2 : En tant qu’utilisateur, je veux observer l’évolution du combat (Observer → les spectateurs
ou le journal de combat reçoivent les infos). => Liticia AMANI
- US 4.3 : En tant qu’utilisateur, je veux pouvoir sauvegarder et rejouer une séquence d’actions
(Command → historique/replay des actions). => Liticia AMANI


# Technologies
- Java 17+


# Contribute to the project
Jeu De Role App is available on github via the following link "https://github.com/Sab-Ben/jeu_de_role", 
it must be clone with command line "git clone https://github.com/Sab-Ben/jeu_de_role".

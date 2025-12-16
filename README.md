# Jest Card Game

## Description du projet

Jest est un jeu de cartes stratégique implémenté en Java avec une interface graphique JavaFX. Le jeu propose deux modes d'exécution : une version console (noyau du jeu) et une version graphique complète avec interface utilisateur.

### Fonctionnalités principales

- **Mode graphique** : Interface utilisateur complète avec JavaFX
  - Menu principal avec options New Game et Load Game
  - Affichage visuel des cartes avec images
  - Animations pour les actions des IA
  - Sauvegarde et chargement de partie
  - Affichage des trophées et scores
  
- **Mode console** : Version texte du jeu
  - Interaction par ligne de commande
  - Toutes les fonctionnalités du jeu

- **Variantes de jeu** : Plusieurs variantes disponibles incluant le mode Classic
- **Joueurs IA** : Possibilité de jouer contre des intelligences artificielles
- **Cartes d'extension** : Support des cartes spéciales (Joker, cartes avec effets)

## Prérequis

- **Java 11 ou supérieur**
- **JavaFX SDK** (pour le mode graphique)

## Bibliothèques utilisées

### JavaFX
Framework pour l'interface graphique :
- `javafx.application.Application` - Gestion de l'application
- `javafx.stage.Stage` - Fenêtres de l'application
- `javafx.scene.Scene` - Scènes et contenus
- `javafx.scene.layout.*` - Layouts (VBox, HBox, BorderPane, etc.)
- `javafx.scene.control.*` - Composants UI (Button, Label, TextField, etc.)
- `javafx.scene.image.*` - Gestion des images des cartes
- `javafx.animation.*` - Animations pour les déplacements de cartes

### Bibliothèques Java Standard
- `java.io.*` - Gestion des fichiers (sauvegarde/chargement)
- `java.util.*` - Collections et utilitaires

## Comment lancer le projet

### Mode graphique (Interface utilisateur)

Pour lancer le jeu avec l'interface graphique :

**Avec un IDE (IntelliJ IDEA, Eclipse, etc.) :**
1. Ouvrez le projet
2. Localisez le fichier `src/ui/GameWindow.java`
3. Cliquez droit et sélectionnez "Run 'GameWindow.main()'"

**En ligne de commande :**
```bash
java ui.GameWindow
```

### Mode console (Noyau du jeu)

Pour lancer le jeu en mode console :

**Avec un IDE :**
1. Localisez le fichier `src/model/game/Game.java`
2. Cliquez droit et sélectionnez "Run 'Game.main()'"

**En ligne de commande :**
```bash
java model.game.Game
```

## Structure du projet

```
Jest/
├── src/
│   ├── model/
│   │   ├── game/
│   │   │   └── Game.java              # Noyau du jeu (mode console)
│   │   ├── cards/                      # Classes des cartes
│   │   │   ├── Card.java
│   │   │   ├── SuitCard.java
│   │   │   └── ...
│   │   └── enums/                      # Énumérations (couleurs, enseignes)
│   ├── player/
│   │   ├── Player.java
│   │   ├── Human.java
│   │   ├── HumanUIPlayer.java
│   │   ├── AIPlayer.java
│   │   └── AI.java
│   ├── strategy/                       # Stratégies de jeu des IA
│   │   ├── IPlayStrategy.java
│   │   ├── RandomStrategy.java
│   │   ├── RiskyStrategy.java
│   │   └── SafeStrategy.java
│   ├── ui/                             # Interface graphique
│   │   ├── MainMenuUI.java            # Menu principal
│   │   ├── CardSelectionUI.java       # Interface de sélection des cartes
│   │   ├── AnimatedGameBoardUI.java   # Plateau de jeu animé
│   │   ├── GameWindow.java            # Fenêtre principale du jeu
│   │   └── ...
│   ├── variant/                        # Variantes du jeu
│   │   ├── ClassicVariant.java
│   │   └── ...
│   ├── visitor/                        # Pattern Visitor pour les cartes
│   └── util/
│       └── GameSaver.java             # Sauvegarde/chargement de partie
├── Assets/
│   └── Images/                         # Images des cartes
│       ├── 1_black_club.png
│       ├── 1_black_spade.png
│       ├── back.png                   # Image pour les cartes cachées
│       └── ...
├── jest_saves/                         # Dossier des sauvegardes
│   ├── game1.jest
│   └── game2.jest
└── doc/                                # Documentation et diagrammes UML
```

## Comment jouer

### Mode graphique

1. **Lancer le jeu** : Exécutez `ui.GameWindow`
2. **Menu principal** :
   - **New Game** : Commencer une nouvelle partie
   - **Load Game** : Charger une partie sauvegardée
3. **Configuration** :
   - Sélectionnez une variante de jeu
   - Entrez les noms des joueurs (2-4 joueurs)
   - Pour ajouter une IA, cochez la case "IA" lors de la saisie d'un nom
4. **Déroulement du jeu** :
   - Chaque joueur choisit une carte à piocher
   - Chaque joueur choisit une carte à cacher
   - Visualisez les animations des IA
   - Consultez les trophées et scores en cours de partie
5. **Sauvegarde** : Utilisez le bouton "Save Game" à la fin de chaque round
6. **Fin de partie** : Les scores finaux et jests sont affichés

### Mode console

1. **Lancer le jeu** : Exécutez `model.game.Game`
2. Suivez les instructions affichées dans la console
3. Saisissez vos choix en utilisant les numéros proposés

## Auteurs

- **Gatien Genevois**
- **Sacha Himber**

## Version

1.0

## License

Projet éducatif - Tous droits réservés


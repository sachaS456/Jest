# Jest Card Game

## Project Description

Jest is a strategic card game implemented in Java with a JavaFX graphical interface. The game offers two execution modes: a console version (game core) and a complete graphical version with user interface.

### Main Features

- **Graphical mode**: Complete user interface with JavaFX
  - Main menu with New Game and Load Game options
  - Visual card display with images
  - Animations for AI actions
  - Save and load game
  - Trophy and score display
  
- **Console mode**: Text version of the game
  - Command-line interaction
  - All game features

- **Game variants**: Multiple variants available including Classic mode
- **AI players**: Ability to play against artificial intelligences
- **Extension cards**: Support for special cards (Joker, cards with effects)

## Prerequisites

- **Java 11 or higher**
- **JavaFX SDK** (for graphical mode)

## Libraries Used

### JavaFX
Framework for graphical interface:
- `javafx.application.Application` - Application management
- `javafx.stage.Stage` - Application windows
- `javafx.scene.Scene` - Scenes and content
- `javafx.scene.layout.*` - Layouts (VBox, HBox, BorderPane, etc.)
- `javafx.scene.control.*` - UI components (Button, Label, TextField, etc.)
- `javafx.scene.image.*` - Card image management
- `javafx.animation.*` - Animations for card movements

### Standard Java Libraries
- `java.io.*` - File management (save/load)
- `java.util.*` - Collections and utilities

## How to Run the Project

### Graphical mode (User interface)

To launch the game with the graphical interface:

**With an IDE (IntelliJ IDEA, Eclipse, etc.):**
1. Open the project
2. Locate the file `src/ui/GameWindow.java`
3. Right-click and select "Run 'GameWindow.main()'"

**Command line:**
```bash
java ui.GameWindow
```

### Console mode (Game core)

To launch the game in console mode:

**With an IDE:**
1. Locate the file `src/model/game/Game.java`
2. Right-click and select "Run 'Game.main()'"

**Command line:**
```bash
java model.game.Game
```

## Project Structure

```
Jest/
├── src/
│   ├── model/
│   │   ├── game/
│   │   │   └── Game.java              # Game core (console mode)
│   │   ├── cards/                      # Card classes
│   │   │   ├── Card.java
│   │   │   ├── SuitCard.java
│   │   │   └── ...
│   │   └── enums/                      # Enumerations (colors, suits)
│   ├── player/
│   │   ├── Player.java
│   │   ├── Human.java
│   │   ├── HumanUIPlayer.java
│   │   ├── AIPlayer.java
│   │   └── AI.java
│   ├── strategy/                       # AI game strategies
│   │   ├── IPlayStrategy.java
│   │   ├── RandomStrategy.java
│   │   ├── RiskyStrategy.java
│   │   └── SafeStrategy.java
│   ├── ui/                             # Graphical interface
│   │   ├── MainMenuUI.java            # Main menu
│   │   ├── CardSelectionUI.java       # Card selection interface
│   │   ├── AnimatedGameBoardUI.java   # Animated game board
│   │   ├── GameWindow.java            # Main game window
│   │   └── ...
│   ├── variant/                        # Game variants
│   │   ├── ClassicVariant.java
│   │   └── ...
│   ├── visitor/                        # Visitor pattern for cards
│   └── util/
│       └── GameSaver.java             # Save/load game
├── Assets/
│   └── Images/                         # Card images
│       ├── 1_black_club.png
│       ├── 1_black_spade.png
│       ├── back.png                   # Image for hidden cards
│       └── ...
├── jest_saves/                         # Save files folder
│   ├── game1.jest
│   └── game2.jest
└── doc/                                # Documentation and UML diagrams
```

## How to Play

### Graphical mode

1. **Launch the game**: Run `ui.GameWindow`
2. **Main menu**:
   - **New Game**: Start a new game
   - **Load Game**: Load a saved game
3. **Configuration**:
   - Select a game variant
   - Enter player names (2-4 players)
   - To add an AI, check the "AI" box when entering a name
4. **Gameplay**:
   - Each player chooses a card to draw
   - Each player chooses a card to hide
   - Watch AI animations
   - View trophies and scores during the game
5. **Save**: Use the "Save Game" button at the end of each round
6. **End of game**: Final scores and jests are displayed

### Console mode

1. **Launch the game**: Run `model.game.Game`
2. Follow the instructions displayed in the console
3. Enter your choices using the proposed numbers

## Authors

- **Gatien Genevois**
- **Sacha Himber**

## Version

1.0

## License

Educational project - All rights reserved
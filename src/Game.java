import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the main Jest card game engine and orchestrator.
 * This class manages the entire game flow, from initialization to final scoring,
 * including player management, card distribution, round execution, and game state persistence.
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Game initialization with player setup and variant selection</li>
 *   <li>Round management and turn execution</li>
 *   <li>Card distribution and trophy management</li>
 *   <li>Score calculation using the Visitor pattern</li>
 *   <li>Game saving and loading functionality</li>
 *   <li>Variant-specific rule application</li>
 * </ul>
 *
 * <p>The game supports 3-4 players (human or AI) and can be played with
 * different variants (Classic, Speed, High Stakes) and optional expansion cards.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Player
 * @see GameVariant
 * @see GameState
 * @see Card
 */
public class Game {
    /** The current round number (1-indexed) */
    private int roundNumber;
    /** The collection of all cards remaining in the deck */
    private ArrayList<Card> cards;
    /** The array of trophy cards (special high-value cards awarded at game end) */
    private Card[] trophies;
    /** The list of all players in the game */
    private ArrayList<Player> players;
    /** The game variant being played (determines scoring and rules) */
    private GameVariant variant;

    /**
     * Main entry point for the Jest card game application.
     * Handles the complete game setup flow including:
     * <ul>
     *   <li>Save game detection and loading</li>
     *   <li>Expansion pack selection</li>
     *   <li>Game variant selection</li>
     *   <li>Game initialization and execution</li>
     * </ul>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        final String RESET  = "\u001B[0m";
        final String RED    = "\u001B[31m";
        final String YELLOW = "\u001B[33m";
        final String GREEN  = "\u001B[32m";
        final String BLUE   = "\u001B[34m";
        final String CYAN   = "\u001B[36m";
        final String PURPLE = "\u001B[35m";

        Scanner scanner = new Scanner(System.in);

        System.out.println(GREEN + "\n======================================" + RESET);
        System.out.println(YELLOW + "🎴  Welcome to the Jest Card Game! 🎴" + RESET);
        System.out.println(GREEN + "======================================\n" + RESET);

        String[] saves = GameSaver.listSaves();
        Game game = null;
        boolean includeExpansion = false;

        if (saves.length > 0) {
            System.out.println(PURPLE + "💾 Saved games found!" + RESET);
            System.out.println(YELLOW + "Do you want to:" + RESET);
            System.out.println(BLUE + "1. Start a new game" + RESET);
            System.out.println(BLUE + "2. Load a saved game" + RESET);
            System.out.print(BLUE + "-> " + RESET);

            int loadChoice;
            do {
                while (!scanner.hasNextInt()) {
                    System.out.println(RED + "Please enter 1 or 2" + RESET);
                    System.out.print(BLUE + "-> " + RESET);
                    scanner.next();
                }
                loadChoice = scanner.nextInt();
                if (loadChoice != 1 && loadChoice != 2) {
                    System.out.println(RED + "Please enter 1 or 2" + RESET);
                    System.out.print(BLUE + "-> " + RESET);
                }
            } while (loadChoice != 1 && loadChoice != 2);

            if (loadChoice == 2) {
                System.out.println("\n" + CYAN + "Available saves:" + RESET);
                for (int i = 0; i < saves.length; i++) {
                    System.out.println(BLUE + (i + 1) + ". " + saves[i] + RESET);
                }

                System.out.println(GREEN + "\nSelect save to load (1-" + saves.length + "):" + RESET);
                System.out.print(BLUE + "-> " + RESET);

                int saveChoice;
                do {
                    while (!scanner.hasNextInt()) {
                        System.out.println(RED + "Please enter a number between 1 and " + saves.length + RESET);
                        System.out.print(BLUE + "-> " + RESET);
                        scanner.next();
                    }
                    saveChoice = scanner.nextInt();
                    if (saveChoice < 1 || saveChoice > saves.length) {
                        System.out.println(RED + "Please enter a number between 1 and " + saves.length + RESET);
                        System.out.print(BLUE + "-> " + RESET);
                    }
                } while (saveChoice < 1 || saveChoice > saves.length);

                GameState loadedState = GameSaver.loadGame(saves[saveChoice - 1]);
                if (loadedState != null) {
                    game = restoreGame(loadedState);
                    includeExpansion = loadedState.isIncludeExpansion();
                    System.out.println(GREEN + "✅ Game loaded successfully!" + RESET);
                    System.out.println(YELLOW + "Resuming from Round " + game.getRoundNumber() + RESET);

                    System.out.println(CYAN + "\nPlayers in this game:" + RESET);
                    for (Player player : game.getPlayers()) {
                        String playerType = player instanceof AI ? "🤖 AI" : "👤 Human";
                        System.out.println(BLUE + "  - " + player.getName() + " " + playerType + RESET);
                    }
                    System.out.println();

                    game.playGame(true);
                    return;
                } else {
                    System.out.println(RED + "❌ Failed to load game. Starting new game..." + RESET);
                }
            }
        }

        System.out.println(YELLOW + "Do you want to include expansion cards? (1 = Yes, 2 = No)" + RESET);
        System.out.println(BLUE + "Expansion cards include new mechanics and effects!" + RESET);
        System.out.print(BLUE + "-> " + RESET);

        int expansionChoice;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println(RED + "Please enter 1 or 2" + RESET);
                System.out.print(BLUE + "-> " + RESET);
                scanner.next();
            }
            expansionChoice = scanner.nextInt();
            if (expansionChoice != 1 && expansionChoice != 2) {
                System.out.println(RED + "Please enter 1 for Yes or 2 for No" + RESET);
                System.out.print(BLUE + "-> " + RESET);
            }
        } while (expansionChoice != 1 && expansionChoice != 2);

        includeExpansion = (expansionChoice == 1);

        if (includeExpansion) {
            System.out.println(GREEN + "🎉 Great! Playing with expansion cards (" + CardDeckFactory.getFullDeckSize() + " total cards)" + RESET);
        } else {
            System.out.println(GREEN + "🎯 Playing with standard cards only (" + CardDeckFactory.getStandardDeckSize() + " cards)" + RESET);
        }

        System.out.println("\n" + CYAN + "======================================" + RESET);
        System.out.println(YELLOW + "🎮  Choose a Game Variant  🎮" + RESET);
        System.out.println(CYAN + "======================================" + RESET);

        GameVariant[] variants = {
            new ClassicVariant(),
            new SpeedVariant(),
            new HighStakesVariant()
        };

        for (int i = 0; i < variants.length; i++) {
            System.out.println(BLUE + (i + 1) + ". " + variants[i].getName() + RESET);
            System.out.println(YELLOW + "   " + variants[i].getDescription() + RESET);
        }

        System.out.println(GREEN + "\nSelect variant (1-" + variants.length + "):" + RESET);
        System.out.print(BLUE + "-> " + RESET);

        int variantChoice;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println(RED + "Please enter a number between 1 and " + variants.length + RESET);
                System.out.print(BLUE + "-> " + RESET);
                scanner.next();
            }
            variantChoice = scanner.nextInt();
            if (variantChoice < 1 || variantChoice > variants.length) {
                System.out.println(RED + "Please enter a number between 1 and " + variants.length + RESET);
                System.out.print(BLUE + "-> " + RESET);
            }
        } while (variantChoice < 1 || variantChoice > variants.length);

        GameVariant selectedVariant = variants[variantChoice - 1];
        System.out.println(GREEN + "✅ Variant selected: " + CYAN + selectedVariant.getName() + RESET);
        System.out.println(YELLOW + selectedVariant.getDescription() + RESET + "\n");

        game = new Game(includeExpansion, selectedVariant);
        game.playGame();
    }

    /**
     * Restores a Game instance from a saved GameState.
     * Recreates all game components including players, cards, trophies, and variant
     * to resume a previously saved game.
     *
     * @param state the GameState to restore from
     * @return a fully restored Game instance ready to continue play
     */
    private static Game restoreGame(GameState state) {
        Game game = new Game(state.isIncludeExpansion());

        game.setRoundNumber(state.getRoundNumber());
        game.setCards(state.getCards());
        game.setTrophies(state.getTrophies());

        GameVariant variant;
        switch (state.getVariantName()) {
            case "Speed":
                variant = new SpeedVariant();
                break;
            case "High Stakes":
                variant = new HighStakesVariant();
                break;
            default:
                variant = new ClassicVariant();
                break;
        }
        game.setVariant(variant);

        ArrayList<Player> restoredPlayers = new ArrayList<>();
        for (PlayerState playerState : state.getPlayerStates()) {
            Player player;
            if (playerState.isAI()) {
                player = new AI(playerState.getName());
            } else {
                player = new Human(playerState.getName());
            }

            player.setJest(playerState.getJest());
            Card[] offer = playerState.getOffer();
            if (offer.length >= 2) {
                player.setOffer(offer[0], offer[1]);
            } else if (offer.length == 1) {
                player.setOffer(offer[0], null);
            }

            restoredPlayers.add(player);
        }
        game.setPlayers(restoredPlayers);

        return game;
    }

    /**
     * Converts the trophy cards to a formatted string representation.
     * Displays each trophy card's details including value, color, sign, and effect code.
     *
     * @return a formatted string showing all trophy cards
     */
    public String trophiesToString(){
        StringBuilder text = new StringBuilder();
        for(Card card : this.trophies) {
            if (card != null){
                if(card instanceof SuitCard) {
                    text.append(((SuitCard) card).getValue() + " ");
                    text.append(((SuitCard) card).getColor() + " ");
                    text.append(((SuitCard) card).getSign() + " ");
                    text.append(card.getCardEffectCode() + " ");
                    text.append("\n");
                }
                else{
                    text.append("Joker");
                    text.append(card.getCardEffectCode() + " ");
                    text.append("\n");
                }
            }
        }

        return text.toString();
    }

    /**
     * Constructs a new Game with default settings.
     * Initializes with standard deck, classic variant, and empty player list.
     */
    public Game() {
        this.roundNumber = 0;
        this.trophies = new  Card[2];
        this.cards = CardDeckFactory.createStandardDeck();
        this.players = new ArrayList<>();
        this.variant = new ClassicVariant();
    }

    /**
     * Constructs a new Game with optional expansion cards.
     *
     * @param includeExpansion true to include expansion cards, false for standard deck only
     */
    public Game(boolean includeExpansion) {
        this.roundNumber = 0;
        this.trophies = new Card[2];
        this.cards = includeExpansion ? CardDeckFactory.createFullDeck() : CardDeckFactory.createStandardDeck();
        this.players = new ArrayList<>();
        this.variant = new ClassicVariant();
    }

    /**
     * Constructs a new Game with expansion selection and specific variant.
     *
     * @param includeExpansion true to include expansion cards, false for standard deck only
     * @param variant the game variant to play (Classic, Speed, or High Stakes)
     */
    public Game(boolean includeExpansion, GameVariant variant) {
        this.roundNumber = 0;
        this.trophies = new Card[2];
        this.cards = includeExpansion ? CardDeckFactory.createFullDeck() : CardDeckFactory.createStandardDeck();
        this.players = new ArrayList<>();
        this.variant = variant;
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to add (Human or AI)
     */
    public void addPlayer(Player player){
        this.players.add(player);
    }

    /**
     * Awards trophy cards to players based on trophy card effects.
     * Each trophy card determines its winner through its effect logic,
     * and the card is added to the winner's jest pile.
     * Trophy values may be modified by the game variant.
     */
    public void giveTrophyCard()
    {
        for (Card card : trophies) {
            if(card != null){
                Player winner = card.checkEffect(this.players);
                winner.AddCardToJest(card);
            }
        }
    }

    /**
     * Distributes cards to all players at the start of a round.
     *
     * <p>Distribution logic:</p>
     * <ul>
     *   <li>Round 1: Distributes cards directly from the deck</li>
     *   <li>Round 2+: Creates a pool from remaining offer cards and new deck cards</li>
     *   <li>Each player receives 2 cards and chooses which to hide</li>
     *   <li>Handles insufficient cards gracefully with warnings</li>
     * </ul>
     */
    public void distribute()
    {
        ArrayList<Card> distributionPool = new ArrayList<>();
        if(this.roundNumber > 1){
            ArrayList<Card> gameCards = new ArrayList<>();
            ArrayList<Card> playerCards = new ArrayList<>();

            for(int i = 0; i < this.players.size() && !this.cards.isEmpty(); i++){
                int random = (int) (this.cards.size() * Math.random());
                gameCards.add(this.cards.remove(random));
            }

            for(Player player : this.players){
                Card removedCard = player.removeLastCardFromOffer();
                if(removedCard != null) {
                    playerCards.add(removedCard);
                }
            }

            distributionPool.addAll(gameCards);
            distributionPool.addAll(playerCards);

            this.cards.removeAll(gameCards);
        }else{
            distributionPool = this.cards;
        }

        int cardsNeeded = this.players.size() * 2;
        if (distributionPool.size() < cardsNeeded) {
            System.out.println("⚠️ Warning: Not enough cards for full distribution. Adjusting...");
            for(Player player : this.players){
                if (!distributionPool.isEmpty()) {
                    int random = (int) (distributionPool.size() * Math.random());
                    Card card1 = distributionPool.remove(random);
                    player.chooseCardToHide(card1, null);
                }
            }
        } else {
            for(Player player : this.players){
                if (distributionPool.size() >= 2) {
                    int random = (int) (distributionPool.size() * Math.random());
                    Card card1 = distributionPool.remove(random);
                    random = (int) (distributionPool.size() * Math.random());
                    Card card2 = distributionPool.remove(random);
                    player.chooseCardToHide(card1, card2);
                } else if (distributionPool.size() == 1) {
                    Card card1 = distributionPool.remove(0);
                    player.chooseCardToHide(card1, null);
                } else {
                    System.out.println("⚠️ Critical: No cards available for player " + player.getName());
                    break;
                }
            }
        }
    }

    /**
     * Executes a complete game round.
     *
     * <p>Round flow:</p>
     * <ol>
     *   <li>Increment round number</li>
     *   <li>Apply variant-specific round start rules</li>
     *   <li>Distribute cards to all players</li>
     *   <li>Determine starting player based on visible card values</li>
     *   <li>Execute player turns until round ends</li>
     *   <li>Apply variant-specific round end rules</li>
     * </ol>
     */
    public void playRound() {
        final String RESET  = "\u001B[0m";
        final String RED    = "\u001B[31m";
        final String YELLOW = "\u001B[33m";
        final String BLUE   = "\u001B[34m";
        final String GREEN  = "\u001B[32m";

        this.setRoundNumber(this.getRoundNumber() + 1);

        System.out.println(GREEN + "===== ROUND " + this.getRoundNumber() + " =====" + RESET);

        variant.applyRoundStartRules(this);

        System.out.println(YELLOW + "Let's give the cards! Please don't watch the chosen hidden card of the player!" + RESET + "\n");

        this.distribute();

        System.out.println(YELLOW + "Now, let's determine the first player to play ..." + RESET + "\n");

        Player currentPlayer = this.getPlayersOrder();

        System.out.println(GREEN + "The player that starts is " + RED + currentPlayer.getName() + RESET + " 🎮");

        currentPlayer = currentPlayer.playTurn(this);
        while (currentPlayer != null) {
            System.out.println(BLUE + "\nNext turn!" + RESET);
            currentPlayer = currentPlayer.playTurn(this);
        }

        variant.applyRoundEndRules(this);

        System.out.println(GREEN + "\nThe round has ended!" + RESET);
    }

    /**
     * Gets the current round number.
     *
     * @return the round number (1-indexed)
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Sets the current round number.
     *
     * @param roundNumber the round number to set
     */
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    /**
     * Starts and executes the complete game from beginning to end.
     * Equivalent to calling {@link #playGame(boolean)} with false.
     */
    public void playGame() {
        playGame(false);
    }

    /**
     * Starts or resumes a complete game from beginning to end.
     *
     * <p>Game flow:</p>
     * <ol>
     *   <li>If not resumed: Set up players and initialize trophies</li>
     *   <li>Execute rounds until no cards remain</li>
     *   <li>Offer save game option after each round</li>
     *   <li>Calculate final scores with trophies</li>
     *   <li>Determine and announce the winner</li>
     * </ol>
     *
     * @param isResumed true if resuming from a saved game, false for new game
     */
    public void playGame(boolean isResumed) {
        final String RESET  = "\u001B[0m";
        final String RED    = "\u001B[31m";
        final String YELLOW = "\u001B[33m";
        final String GREEN  = "\u001B[32m";
        final String BLUE   = "\u001B[34m";
        final String PURPLE = "\u001B[35m";

        Scanner scanner = new Scanner(System.in);
        boolean includeExpansion = this.cards.size() > CardDeckFactory.getStandardDeckSize();

        if (!isResumed) {
            System.out.println(YELLOW + "How many players want to play? (3 or 4)" + RESET);
            System.out.print(BLUE + "-> " + RESET);
            int playerNumber = this.makeChoice(3, 4);

            for (int i = 0; i < playerNumber; i++) {
                System.out.println(BLUE + "Player " + (i + 1) + ", what's your name?:" + RESET);
                System.out.print(BLUE + "-> " + RESET);
                String playerName = scanner.next();

                if (playerName.toLowerCase().contains("bot")) {
                    this.addPlayer(new AI(playerName));
                    System.out.println(GREEN + "🤖 Added bot player: " + RED + playerName + RESET);
                } else {
                    this.addPlayer(new Human(playerName));
                    System.out.println(GREEN + "👤 Added human player: " + RED + playerName + RESET);
                }
                sleep(500);
            }

            this.setTrophies();
            sleep(500);

            System.out.println();
            System.out.println(BLUE + "----------------------------------" + RESET);
            System.out.println(YELLOW + "🏆  Trophies have been updated!  🏆" + RESET);
            System.out.println(GREEN + this.trophiesToString() + RESET);
            System.out.println(BLUE + "----------------------------------" + RESET);
            System.out.println();
            sleep(1000);
        }

        while (!this.getCards().isEmpty()) {
            this.playRound();
            System.out.println(GREEN + "\nEnd of Round " + this.roundNumber + RESET);
            System.out.println(BLUE + "Remaining cards: " + this.getCards().size() + RESET + "\n");

            System.out.println(PURPLE + "💾 Do you want to save the game? (1 = Yes, 2 = No)" + RESET);
            System.out.print(BLUE + "-> " + RESET);

            int saveChoice = this.makeChoice(1, 2);
            if (saveChoice == 1) {
                System.out.println(YELLOW + "Enter a name for this save (or press Enter for auto-generated name):" + RESET);
                System.out.print(BLUE + "-> " + RESET);
                scanner.nextLine();
                String saveName = scanner.nextLine().trim();

                if (GameSaver.saveGame(this, includeExpansion, saveName.isEmpty() ? null : saveName)) {
                    System.out.println(GREEN + "✅ Game saved successfully!" + RESET);
                }
            }

            sleep(1000);
        }

        System.out.println(YELLOW + "Let's reveal players' Jests! 👀" + RESET);
        sleep(1000);

        for (Player player : this.getPlayers()) {
            player.addLastCardToJest();
            int points = variant.calculatePoints(player);
            System.out.println(GREEN + "Points: " + RESET + points);
            System.out.println(RED + player + RESET);
            sleep(500);
        }

        this.giveTrophyCard();
        sleep(1000);

        System.out.println(YELLOW + "\nLet's reveal players' Jests with Trophies! 👀" + RESET);
        Player winner = null;
        int maxPoints = 0;
        for (Player player : this.getPlayers()) {
            int points = variant.calculatePoints(player);
            System.out.println(GREEN + "Points: " + RESET + points);
            if (winner == null || points > maxPoints) {
                winner = player;
                maxPoints = points;
            }
            System.out.println(RED + player + RESET);
            sleep(500);
        }

        System.out.println();
        System.out.println(BLUE + "======================================" + RESET);
        System.out.println(YELLOW + "🏅  The winner is " + RED + winner.getName() + YELLOW + "!  🎉" + RESET);
        System.out.println(BLUE + "======================================" + RESET);
        System.out.println(GREEN + "Thanks for playing Jest! 👏" + RESET);
    }

    /**
     * Pauses execution for a specified duration.
     * Used to add delays for better user experience and readability.
     *
     * @param millis the number of milliseconds to pause
     */
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Prompts for and validates a user choice within a specified range.
     * Repeatedly asks for input until a valid integer within [min, max] is entered.
     *
     * @param min the minimum valid choice value (inclusive)
     * @param max the maximum valid choice value (inclusive)
     * @return the validated user choice
     */
    public int makeChoice(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            while (!scanner.hasNextInt()) {
                scanner.next();
            }

            choice = scanner.nextInt();

            if (choice < min || choice > max) {
                System.out.println("Please make a choice between " + min + " and " + max + ".");
            }
        } while (choice < min || choice > max);

        return choice;
    }

    /**
     * Determines which player should take the next turn based on visible card values.
     * Players are ordered by their visible card's value, with ties broken by suit priority
     * (Hearts > Diamonds > Clubs > Spades).
     *
     * <p>Selection criteria:</p>
     * <ul>
     *   <li>Highest value visible card wins</li>
     *   <li>Joker cards are excluded from consideration</li>
     *   <li>Players who have already played this round are excluded</li>
     *   <li>On equal values, suit order determines priority</li>
     * </ul>
     *
     * @return the player with the highest priority visible card, or null if no valid player exists
     */
    public Player getPlayersOrder() {
        Player highScorePlayer = null;
        for(Player player : this.players){
            if(player.getVisibleCard() != null &&
               !(player.getVisibleCard() instanceof JokerCard) &&
               !(this.getPlayersThatHavePlayedThisRound().contains(player))){

                if(highScorePlayer == null){
                    highScorePlayer = player;
                } else {
                    if(highScorePlayer.getVisibleCard() == null) {
                        highScorePlayer = player;
                    } else {
                        SuitCard highScorePlayerVisibleCard = (SuitCard) highScorePlayer.getVisibleCard();
                        SuitCard playerVisibleCard = (SuitCard) player.getVisibleCard();

                        if(playerVisibleCard.getValue() > highScorePlayerVisibleCard.getValue()){
                            highScorePlayer = player;
                        }else if(playerVisibleCard.getValue() == highScorePlayerVisibleCard.getValue()){
                            Sign[] cardsSignOrder = {Sign.HEARTH, Sign.DIAMOND, Sign.CLUB, Sign.SPADE};
                            List<Sign> signOrderList = Arrays.asList(cardsSignOrder);
                            int playerCardIndex = signOrderList.indexOf(playerVisibleCard.getSign());
                            int highScorePlayerCardIndex = signOrderList.indexOf(highScorePlayerVisibleCard.getSign());
                            if(playerCardIndex > highScorePlayerCardIndex){
                                highScorePlayer = player;
                            }
                        }
                    }
                }
            }
        }
        return highScorePlayer;
    }

    /**
     * Gets the collection of remaining cards in the deck.
     *
     * @return an ArrayList containing all cards not yet distributed or used
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Sets the collection of cards in the deck.
     *
     * @param cards the new card collection to set
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Gets the array of trophy cards.
     *
     * @return an array containing the trophy cards for this game
     */
    public Card[] getTrophies() {
        return trophies;
    }

    /**
     * Sets the trophy cards for the game.
     *
     * @param trophies the array of trophy cards to set
     */
    public void setTrophies(Card[] trophies) {
        this.trophies = trophies;
    }

    /**
     * Gets the list of all players in the game.
     *
     * @return an ArrayList containing all players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the list of players for the game.
     *
     * @param players the ArrayList of players to set
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Gets the current game variant.
     *
     * @return the GameVariant being played
     */
    public GameVariant getVariant() {
        return variant;
    }

    /**
     * Sets the game variant.
     *
     * @param variant the GameVariant to set
     */
    public void setVariant(GameVariant variant) {
        this.variant = variant;
    }

    /**
     * Calculates the base Jest points for a player using the Visitor pattern.
     * This method traverses all cards in the player's jest pile and applies
     * the Jest scoring rules through the JestScoreVisitor.
     *
     * <p>Note: This returns the base score before variant multipliers are applied.</p>
     *
     * @param player the player whose points are being calculated
     * @return the base Jest score for the player's jest pile
     * @see JestScoreVisitor
     */
    public static int getJestPoints(Player player) {
        JestScoreVisitor scoreVisitor = new JestScoreVisitor();

        for (Card card : player.getJest()) {
            if (card != null) {
                card.accept(scoreVisitor);
            }
        }

        return scoreVisitor.getScore();
    }

    /**
     * Randomly selects and sets the trophy cards for the game.
     * Removes the selected cards from the deck.
     *
     * <p>Trophy selection:</p>
     * <ul>
     *   <li>Always selects 1 trophy card</li>
     *   <li>Selects a 2nd trophy if 3 or fewer players</li>
     * </ul>
     */
    public void setTrophies(){
        int random = (int) (((this.cards.size()-1) * Math.random()));
        this.trophies[0] = this.cards.remove(random);

        if(players.size()<=3){
            random = (int) (((this.cards.size()-1) * Math.random()));
            this.trophies[1] = this.cards.remove(random);
        }
    }

    /**
     * Gets the list of players who have completed their turn in the current round.
     * A player is considered to have played if their jest pile size equals the round number.
     *
     * @return an ArrayList of players who have already played this round
     */
    public ArrayList<Player> getPlayersThatHavePlayedThisRound()
    {
        ArrayList<Player> playersThatHavePlayed = new ArrayList<>();
        for(Player player : this.getPlayers()){
            if(player.getJest().size() == this.getRoundNumber()){
                playersThatHavePlayed.add(player);
            }
        }
        return playersThatHavePlayed;
    }

    /**
     * Counts how many players currently have both a visible and hidden card in their offer.
     * Used to determine if the round should continue or end.
     *
     * @return the number of players with complete offers (2 cards)
     */
    public int countPlayersWithFullOffer(){
        int  count = 0;
        for(Player player : this.getPlayers()){
            if(player.getVisibleCard() != null && player.getHiddenCard() != null){
                count++;
            }
        }
        return count;
    }
}

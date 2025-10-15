import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {
    private int roundNumber;
    private ArrayList<Card> cards;
    private Card[] trophies;
    private ArrayList<Player> players;
    private GameVariant variant;

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

    public Game() {
        this.roundNumber = 0;
        this.trophies = new  Card[2];
        this.cards = CardDeckFactory.createStandardDeck();
        this.players = new ArrayList<>();
        this.variant = new ClassicVariant();
    }

    public Game(boolean includeExpansion) {
        this.roundNumber = 0;
        this.trophies = new Card[2];
        this.cards = includeExpansion ? CardDeckFactory.createFullDeck() : CardDeckFactory.createStandardDeck();
        this.players = new ArrayList<>();
        this.variant = new ClassicVariant();
    }

    public Game(boolean includeExpansion, GameVariant variant) {
        this.roundNumber = 0;
        this.trophies = new Card[2];
        this.cards = includeExpansion ? CardDeckFactory.createFullDeck() : CardDeckFactory.createStandardDeck();
        this.players = new ArrayList<>();
        this.variant = variant;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void giveTrophyCard()
    {
        for (Card card : trophies) {
            if(card != null){
                Player winner = card.checkEffect(this.players);
                winner.AddCardToJest(card);
            }
        }
    }

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

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void playGame() {
        playGame(false);
    }

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

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

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
                            Sign[] cardsSignOrder = {Sign.HEARTH, Sign.TILE, Sign.CLOVER, Sign.SPIKE};
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

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Card[] getTrophies() {
        return trophies;
    }

    public void setTrophies(Card[] trophies) {
        this.trophies = trophies;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public GameVariant getVariant() {
        return variant;
    }

    public void setVariant(GameVariant variant) {
        this.variant = variant;
    }

    public static int getJestPoints(Player player) {
        JestScoreVisitor scoreVisitor = new JestScoreVisitor();

        for (Card card : player.getJest()) {
            if (card != null) {
                card.accept(scoreVisitor);
            }
        }

        return scoreVisitor.getScore();
    }

    public void setTrophies(){
        int random = (int) (((this.cards.size()-1) * Math.random()));
        this.trophies[0] = this.cards.remove(random);

        if(players.size()<=3){
            random = (int) (((this.cards.size()-1) * Math.random()));
            this.trophies[1] = this.cards.remove(random);
        }
    }

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

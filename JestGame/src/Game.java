import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Game {
    private int roundNumber;
    private ArrayList<Card> cards;
    private Card[] trophies;
    private ArrayList<Player> players;

    public static void main(String[] args) {
       Game game = new Game();
       game.playGame();
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
        this.cards = Game.cardsList();
        this.players = new ArrayList<>();
    }

    public static ArrayList<Card> cardsList(){
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.SPIKE, 1, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.HEARTH, 2, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.HEARTH, 3, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.SPIKE, 4, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.SPIKE, 1, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, CardEffect.MAJORITY, 3, 2, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, CardEffect.MAJORITY, 2, 3, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.CLOVER, 4, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, CardEffect.JOKER, 1, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, CardEffect.JOKER, 2, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, CardEffect.JOKER, 3, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, CardEffect.JOKER, 4, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, CardEffect.MAJORITY, 4, 1, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.TILE, 2, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.TILE, 3, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, CardEffect.BEST_JEST_WITHOUT_JOKER, 4, Color.RED, Sign.TILE));
        cards.add(new JokerCard(true, CardEffect.BEST_JEST));
        return cards;
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
        // merge players cards of offer left with a number of cards of the Game equals to the number of players in round >1
        if(this.roundNumber > 1){
            ArrayList<Card> gameCards = new ArrayList<>();
            ArrayList<Card> playerCards = new ArrayList<>();
            for(int i = 0; i < this.players.size(); i++){
                int random = (int) ((this.cards.size() * Math.random()));
                gameCards.add(this.cards.remove(random));
            }
            for(Player player : this.players){
                playerCards.add(player.removeLastCardFromOffer());
            }



            distributionPool.addAll(gameCards);
            distributionPool.addAll(playerCards);

            this.cards.removeAll(gameCards);
        }else{
            distributionPool = this.cards;
        }

        for(Player player : this.players){
            int random = (int) ((distributionPool.size() * Math.random()));
            Card card1 = distributionPool.remove(random);
            random = (int) ((distributionPool.size() * Math.random()));
            Card card2 = distributionPool.remove(random);
            player.chooseCardToHide(card1, card2);
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

        System.out.println(GREEN + "\nThe round has ended!" + RESET);
    }


    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void playGame() {
        final String RESET  = "\u001B[0m";
        final String RED    = "\u001B[31m";
        final String YELLOW = "\u001B[33m";
        final String GREEN  = "\u001B[32m";
        final String BLUE   = "\u001B[34m";

        Scanner scanner = new Scanner(System.in);

        System.out.println(GREEN + "\n======================================" + RESET);
        System.out.println(YELLOW + "🎴  Welcome to the Jest Card Game! 🎴" + RESET);
        System.out.println(GREEN + "======================================\n" + RESET);
        sleep(1000);

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

        while (!this.getCards().isEmpty()) {
            this.playRound();
            System.out.println(GREEN + "\nEnd of Round " + this.roundNumber + RESET);
            System.out.println(BLUE + "Remaining cards: " + this.getCards().size() + RESET + "\n");
            sleep(1000);
        }

        System.out.println(YELLOW + "Let's reveal players' Jests! 👀" + RESET);
        sleep(1000);

        for (Player player : this.getPlayers()) {
            player.addLastCardToJest();
            System.out.println(GREEN + "Points: " + RESET + Game.getJestPoints(player));
            System.out.println(RED + player + RESET);
            sleep(500);
        }

        this.giveTrophyCard();
        sleep(1000);

        System.out.println(YELLOW + "\nLet's reveal players' Jests with Trophies! 👀" + RESET);
        Player winner = null;
        for (Player player : this.getPlayers()) {
            System.out.println(GREEN + "Points: " + RESET + Game.getJestPoints(player));
            if (winner == null || Game.getJestPoints(player) > Game.getJestPoints(winner)) {
                winner = player;
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
            // the starting player can't have the Joker because it is the only card valued at 0 and must have not play
            if(!(player.getVisibleCard() instanceof JokerCard) && !(this.getPlayersThatHavePlayedThisRound().contains(player))){
                if(highScorePlayer == null){
                    highScorePlayer = player;
                }
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

    public static int getJestPoints(Player player) {
        JestScoreVisitor scoreVisitor = new JestScoreVisitor();

        // visit all player's cards in jest
        for (Card card : player.getJest()) {
            card.accept(scoreVisitor);
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

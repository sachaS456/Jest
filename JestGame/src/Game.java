import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {
    private int roundNumber;
    private ArrayList<Card> cards;
    // private Player[] players;
    private Card[] trophies;
    private ArrayList<Player> players;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game();
        System.out.println("Welcome to Jest Game!");
        System.out.println("How many players want to play ?");
        int playerNumber = scanner.nextInt();
        for(int i = 0; i < playerNumber; i++){
            System.out.println("Player " + (i + 1) + ", what's your name?:");
            String playerName = scanner.next();
            game.addPlayer(new Player(playerName));
        }
        game.setTrophies();
        game.playRound();

    }


    public Game() {
        this.roundNumber = 0;
        this.trophies = new  Card[2];
        this.cards = Game.cardsList();
        this.players = new ArrayList<>();
    }

    public static ArrayList<Card> cardsList(){
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new SuitCard(true, null, 1, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, null, 2, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, null, 3, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, null, 4, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, null, 5, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, null, 6, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, null, 7, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, null, 1, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, null, 2, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, null, 3, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, null, 4, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, null, 5, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, null, 6, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, null, 7, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, null, 1, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, null, 2, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, null, 3, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, null, 4, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, null, 5, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, null, 6, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, null, 7, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, null, 1, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, null, 2, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, null, 3, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, null, 4, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, null, 5, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, null, 6, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, null, 7, Color.RED, Sign.TILE));
        cards.add(new JokerCard(true, null));
        return cards;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void giveTrophyCard()
    {
        // TODO:
    }

    public void distribute()
    {
        Scanner scanner = new Scanner(System.in);
        for(Player player : this.players){
            int random = (int) ((this.cards.size() * Math.random()));
            Card card1 = this.cards.remove(random);
            random = (int) ((this.cards.size() * Math.random()));
            Card card2 = this.cards.remove(random);
            System.out.println(player.getName() + ", which card do you want to hide ? (1, 2)");
            System.out.println("(1) Card 1: " + card1);
            System.out.println("(2) Card 2: " + card2);
            int cardToHide = scanner.nextInt();
            if(cardToHide == 1){
                player.setHiddenCard(card1);
                player.setVisibleCard(card2);
            }else {
                player.setHiddenCard(card2);
                player.setVisibleCard(card1);
            }
        }
    }

    public void playRound()
    {
        this.setRoundNumber(this.getRoundNumber() + 1);
        System.out.println("Let's give the cards, please don't watch the chosen hidden card of the player !");
        this.distribute();
        System.out.println("Now, let's determine the first player that play ...");
        Player currentPlayer = this.getPlayersOrder();
        System.out.println("The player that start is " + currentPlayer.getName());
        currentPlayer = this.playerTurn(currentPlayer);
        while(currentPlayer != null){
            currentPlayer = this.playerTurn(currentPlayer);
        }
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void playGame(){
        // TODO:
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

    public int getJestPoints(Card[] playerJest){
        // TODO:
        throw new UnsupportedOperationException("Not supported yet. !");
    }

    public void addTrophy(Player player){
        // TODO:
    }

    public void setTrophies(){
        int random = (int) ((this.cards.size() * Math.random()));
        this.trophies[0] = this.cards.remove(random);

        if(players.size()>3){
            random = (int) ((this.cards.size() * Math.random()));
            this.trophies[1] = this.cards.remove(random);
        }
    }
    
    public Player playerTurn(Player currentPlayer){
        System.out.println(currentPlayer.getName() + ", which card do you want to pick ?");
        int cardNumber = 1;
        ArrayList<Card> possibleCardsToPick = new ArrayList<>();
        ArrayList<Player> cardOwners = new ArrayList<>();

        for (Player player : this.getPlayers()) {
            if(player != currentPlayer){
                if (player.getVisibleCard() != null && player.getHiddenCard() != null) {
                    System.out.println(player.getName() + ": " + "(" + cardNumber + ") "
                            + player.getVisibleCard() + "(" + (cardNumber + 1) + ") hidden card 🫣");
                    possibleCardsToPick.add(player.getVisibleCard());
                    cardOwners.add(player);
                    possibleCardsToPick.add(player.getHiddenCard());
                    cardOwners.add(player);

                    cardNumber += 2;
                } else {
                    System.out.println(player.getName() + " has only 1 card, so you can't pick it");
                }
            }
        }

        System.out.println("-> ");
        Scanner scanner = new Scanner(System.in);
        int cardToPick = scanner.nextInt();

        Card pickedCard = possibleCardsToPick.get(cardToPick - 1);
        Player nextPlayer = cardOwners.get(cardToPick - 1);

        currentPlayer.pickCard(pickedCard, nextPlayer);

        if(this.countPlayersWithFullOffer() == 0){
            return null;
        }

        // next player is the player with current highest card if next player has already play
        if(this.getPlayersThatHavePlayedThisRound().contains(nextPlayer)){
            nextPlayer = getPlayersOrder();
        }
        return nextPlayer;
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

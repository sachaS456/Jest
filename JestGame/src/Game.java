import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {
    private int roundNumber;
    private ArrayList<Card> cards;
    private Card[] trophies;
    private ArrayList<Player> players;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game();
        System.out.println("Welcome to Jest Game!");
        System.out.println("How many players want to play (3 or 4) ?");
        int playerNumber = scanner.nextInt();
        for(int i = 0; i < playerNumber; i++){
            System.out.println("Player " + (i + 1) + ", what's your name?:");
            String playerName = scanner.next();
            game.addPlayer(new Player(playerName));
        }
        game.setTrophies();
        System.out.println(game.trophiesToString());

        while(!game.getCards().isEmpty()){
            game.playRound();
            System.out.println("End of Round " + game.roundNumber);
            System.out.println("Number of cards " + game.getCards().size());
        }

        System.out.println("Let's reveal players Jest ! 👀");
        for(Player player : game.getPlayers()){
            player.addLastCardToJest();
            System.out.println("Points " + Game.getJestPoints(player));
            System.out.println(player);
        }

        game.giveTrophyCard();

        System.out.println("Let's reveal players Jest with Trophies ! 👀");
        for(Player player : game.getPlayers()){
            System.out.println("Points " + Game.getJestPoints(player));
            System.out.println(player);
        }
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
            Player winner = card.checkEffect(this.players);
            winner.AddCardToJest(card);
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

        Scanner scanner = new Scanner(System.in);
        for(Player player : this.players){
            int random = (int) ((distributionPool.size() * Math.random()));
            Card card1 = distributionPool.remove(random);
            random = (int) ((distributionPool.size() * Math.random()));
            Card card2 = distributionPool.remove(random);
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

    public static int getJestPoints(Player player){
        int score = 0;
        int heartNumber = 0;
        int spikeNumber = 0;
        int cloverNumber = 0;
        int tileNumber = 0;
        ArrayList<Integer> spikeValues = new ArrayList<>();
        ArrayList<Integer> cloverValues = new ArrayList<>();


        // count the hearts
        for(Card card : player.getJest()){
            if(card instanceof SuitCard){
                if(((SuitCard) card).getSign() == Sign.HEARTH){
                    heartNumber++;
                }
                if(((SuitCard) card).getSign() == Sign.TILE){
                    tileNumber++;
                }
                if(((SuitCard) card).getSign() == Sign.CLOVER){
                    cloverNumber++;
                    cloverValues.add(((SuitCard) card).getValue());
                }
                if(((SuitCard) card).getSign() == Sign.SPIKE){
                    spikeNumber++;
                    spikeValues.add(((SuitCard) card).getValue());
                }
            }
        }

        if(player.hasJokerCard()){
            for (Card card : player.getJest()){
                if(card instanceof SuitCard){
                    // Spike or clover
                    if(((SuitCard) card).getSign() == Sign.SPIKE || ((SuitCard) card).getSign() == Sign.CLOVER){
                        score += ((SuitCard) card).getValue();
                    }
                    // tile
                    if(((SuitCard) card).getSign() == Sign.TILE){
                        score -=  ((SuitCard) card).getValue();
                    }
                    // heart
                    if(((SuitCard) card).getSign() == Sign.HEARTH){
                        if(heartNumber >= 1 && heartNumber <= 3){
                            score -= ((SuitCard) card).getValue();
                        }
                        if(heartNumber == 4){
                            score +=  ((SuitCard) card).getValue();
                        }
                    }

                    // if card is an Ace with unique Sign in Jest
                    if(((SuitCard) card).getSign() == Sign.HEARTH && heartNumber == 1){
                        score += 4;
                    }
                    if(((SuitCard) card).getSign() == Sign.SPIKE && spikeNumber == 1){
                        score += 4;
                    }
                    if(((SuitCard) card).getSign() == Sign.CLOVER && cloverNumber == 1){
                        score += 4;
                    }
                    if(((SuitCard) card).getSign() == Sign.TILE && tileNumber == 1){
                        score += 4;
                    }
                }
            }
            if(heartNumber == 0){
                score += 4;
            }
        }
        else {
            // if the player doesn't have the Joker
            for (Card card : player.getJest()) {
                // Spike or clover
                if (((SuitCard) card).getSign() == Sign.SPIKE || ((SuitCard) card).getSign() == Sign.CLOVER) {
                    score += ((SuitCard) card).getValue();
                }
                // tile
                if (((SuitCard) card).getSign() == Sign.TILE) {
                    score -= ((SuitCard) card).getValue();
                }

                // if card is an Ace with unique Sign in Jest
                if(((SuitCard) card).getSign() == Sign.HEARTH && heartNumber == 1){
                    score += 4;
                }
                if(((SuitCard) card).getSign() == Sign.SPIKE && spikeNumber == 1){
                    score += 4;
                }
                if(((SuitCard) card).getSign() == Sign.CLOVER && cloverNumber == 1){
                    score += 4;
                }
                if(((SuitCard) card).getSign() == Sign.TILE && tileNumber == 1){
                    score += 4;
                }
            }
        }

        // spike and clover with the same face value worth a bonus of 2 points
        for(int value : spikeValues){
            if(cloverValues.contains(value)){
                score += 2;
            }
        }

        return score;

    }

    public void addTrophy(Player player){
        // TODO:
    }

    public void setTrophies(){
        int random = (int) (((this.cards.size()-1) * Math.random()));
        this.trophies[0] = this.cards.remove(random);

        if(players.size()<=3){
            random = (int) (((this.cards.size()-1) * Math.random()));
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

        if(possibleCardsToPick.isEmpty()){
            System.out.println("There is no cards available to pick in your opponents offers. Please chose one of your card :");
            System.out.println(currentPlayer.getName() + ": " + "(" + cardNumber + ") "
                    + currentPlayer.getVisibleCard() + "(" + (cardNumber + 1) + ") hidden card 🫣");
            possibleCardsToPick.add(currentPlayer.getHiddenCard());
            cardOwners.add(currentPlayer);
            possibleCardsToPick.add(currentPlayer.getVisibleCard());
            cardOwners.add(currentPlayer);
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

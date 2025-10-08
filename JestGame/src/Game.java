import java.util.ArrayList;

public class Game {
    private int roundNumber;
    private ArrayList<Card> cards;
    // private Player[] players;
    private Card[] trophies;
    private ArrayList<Player> players;

    public static void main(String[] args) {
        Game game = new Game();
    }


    public Game() {
        this.roundNumber = 0;
        this.trophies = new  Card[2];
        this.cards = Game.cardsList();
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

    public void giveTrophyCard()
    {
        // TODO:
    }

    public void setupTrophies()
    {
        // TODO:
    }

    public void distribute()
    {
        // TODO:
    }

    public void playRound()
    {
        // TODO:
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

    public Player[] getPlayersOrder(){
        // TODO:
        return null;
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
            this.trophies[1] = this.cards.remove(random);
        }
    }
}

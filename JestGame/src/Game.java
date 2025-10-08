import java.util.ArrayList;

public class Game {
    private int roundNumber;
    private ArrayList<Card> cards;
    // private Player[] players;
    private Card[] trophees;


    public Game() {
        this.roundNumber = 0;
        this.trophees = new  Card[2];
        this.cards = Game.cardsList();
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void playGame(){

    }

    public void setTrophee(){
        int random = (int) ((this.cards.size() * Math.random()));
        this.trophees[0] = this.cards.remove(random);

        // si count players > 3 définir trophee[1]
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
}

import java.util.ArrayList;

public class Player {

    public static void main(String args[]) {
        Card c1 = new SuitCard(true, null, 10, Color.RED, Sign.CLOVER);
        Card c2 = new SuitCard(true, null, 11, Color.RED, Sign.CLOVER);
        Card c3 = new SuitCard(true, null, 12, Color.RED, Sign.CLOVER);
        Card c4 = new SuitCard(true, null, 13, Color.RED, Sign.CLOVER);
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<Card> cards2 = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards2.add(c3);
        cards2.add(c4);
        player1.setOffer(c1, c2);
        player2.setOffer(c3, c4);
        System.out.println("Player before picking:");
        System.out.println(player1);
        System.out.println(player2);
        player1.pickCard(c3, player2);
        System.out.println("Player after picking:");
        System.out.println(player1);
        System.out.println(player2);
    }

    private String name;
    private ArrayList<Card> jest;
    private Card[] offer;

    public Player(String name) {
        this.name = name;
        this.offer = new Card[2];
        this.jest = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append("Name: ").append(name).append("\n");
        text.append("Jest: \n");
        for(Card card : jest) {
            if (card != null){
                if(card instanceof SuitCard) {
                    text.append(((SuitCard) card).getValue() + " ");
                    text.append(((SuitCard) card).getColor() + " ");
                    text.append(((SuitCard) card).getSign() + " ");
                    text.append("\n");
                }
                else{
                    text.append("Joker");
                    text.append("\n");
                }
            }
        }
        text.append("Offers: \n");
        if(offer != null && offer.length > 0) {
            for(Card card : offer) {
                if(card != null){
                    if(card instanceof SuitCard) {
                        text.append(((SuitCard) card).getValue() + " ");
                        text.append(((SuitCard) card).getColor() + " ");
                        text.append(((SuitCard) card).getSign() + " ");
                        text.append("\n");
                    }
                    else{
                        text.append("Joker");
                        text.append("\n");
                    }
                }
            }
        }
        return text.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getJest() {
        return jest;
    }

    public void setJest(ArrayList<Card> jest) {
        this.jest = jest;
    }

    public Card[] getOffer() {
        return offer;
    }

    public void setOffer(Card visibleCard, Card hiddenCard) {
        if(hiddenCard != null) {
            hiddenCard.setVisible(false);
            this.offer[1] = hiddenCard;
        }
        if(visibleCard != null) {
            this.offer[0] = visibleCard;
        }
    }

    public void setVisibleCard(Card visibleCard) {
        this.offer[0] =  visibleCard;
    }

    public void setHiddenCard(Card hiddenCard) {
        hiddenCard.setVisible(false);
        this.offer[1] = hiddenCard;
    }

    public void pickCard(Card card, Player pickedPlayer) {
        this.jest.add(card);
        if(card == pickedPlayer.getOffer()[0]) {
            pickedPlayer.setVisibleCard(null);
        }else{
            pickedPlayer.setHiddenCard(null);
        }
    }

    public void addLastCardToJest() {
        if(this.offer[0] != null ^ this.offer[1] != null) {
            this.jest.add((this.offer[0] != null) ? this.offer[0] : this.offer[1]);
            this.offer =  null;
        }else {
            System.out.println("You still have two cards in your offer");
        }
    }

    public Card getVisibleCard() {
        return this.offer[0];
    }

    public Card getHiddenCard() {
        return this.offer[1];
    }
}

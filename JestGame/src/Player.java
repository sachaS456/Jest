import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public abstract class Player {

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
        if(hiddenCard != null) {
            hiddenCard.setVisible(false);
        }
        this.offer[1] = hiddenCard;
    }

    public void pickCard(Card card, Player pickedPlayer) {
        this.jest.add(card);
        if(card == pickedPlayer.getOffer()[0]) {
            pickedPlayer.setVisibleCard(null);
            System.out.println(this.name + " picked " + card + " of "+pickedPlayer.getName());
        }else{
            pickedPlayer.setHiddenCard(null);
            System.out.println(this.name + " has picked hidden card of "+pickedPlayer.getName());
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

    public Card removeLastCardFromOffer() {
        Card removedCard = null;
        if(this.offer[0] != null) {
            removedCard = this.offer[0];
            this.offer[0] = null;
        }
        if(this.offer[1] != null) {
            removedCard = this.offer[1];
            this.offer[1] = null;
        }

        return removedCard;
    }

    public boolean hasJokerCard() {
        for (Card card : jest) {
            if(card instanceof JokerCard) {
                return true;
            }
        }

        return false;
    }

    public void AddCardToJest(Card card) {
        this.jest.add(card);
    }

    public Player playTurn(Game game) {
        final String RESET  = "\u001B[0m";
        final String RED    = "\u001B[31m";
        final String YELLOW = "\u001B[33m";
        final String BLUE   = "\u001B[34m";
        final String GREEN  = "\u001B[32m";

        System.out.println(RED + this.getName() + RESET + ", " + YELLOW + "which card do you want to pick ?" + RESET);

        int cardNumber = 1;
        ArrayList<Card> possibleCardsToPick = new ArrayList<>();
        ArrayList<Player> cardOwners = new ArrayList<>();

        for (Player player : game.getPlayers()) {
            if (player != this) {
                if (player.getVisibleCard() != null && player.getHiddenCard() != null) {
                    System.out.println(
                            RED + player.getName() + RESET + ": \n" +
                                    BLUE + "(" + cardNumber + ") " + RESET + player.getVisibleCard() + " " +
                                    BLUE + "(" + (cardNumber + 1) + ") " + RESET + "hidden card 🫣"
                    );

                    possibleCardsToPick.add(player.getVisibleCard());
                    cardOwners.add(player);
                    possibleCardsToPick.add(player.getHiddenCard());
                    cardOwners.add(player);

                    cardNumber += 2;
                } else {
                    System.out.println(
                            RED + player.getName() + RESET +
                                    YELLOW + " has only 1 card, so you can't pick it" + RESET
                    );
                }
            }
        }

        if (possibleCardsToPick.isEmpty()) {
            System.out.println(
                    YELLOW + "There are no cards available to pick from your opponents. Please choose one of your own cards:" + RESET
            );
            System.out.println(
                    RED + this.getName() + RESET + ": \n" +
                            BLUE + "(" + cardNumber + ") " + RESET + this.getVisibleCard() + " " +
                            BLUE + "(" + (cardNumber + 1) + ") " + RESET + "hidden card 🫣"
            );

            possibleCardsToPick.add(this.getHiddenCard());
            cardOwners.add(this);
            possibleCardsToPick.add(this.getVisibleCard());
            cardOwners.add(this);
        }

        System.out.print(BLUE + "-> " + RESET);
        int cardToPick = this.makeChoice(1, possibleCardsToPick.size() + 1);

        Card pickedCard = possibleCardsToPick.get(cardToPick - 1);
        Player nextPlayer = cardOwners.get(cardToPick - 1);

        this.pickCard(pickedCard, nextPlayer);

        if (game.countPlayersWithFullOffer() == 0) {
            return null;
        }

        if (game.getPlayersThatHavePlayedThisRound().contains(nextPlayer)) {
            nextPlayer = game.getPlayersOrder();
        }

        System.out.println(GREEN + "You picked a card from " + nextPlayer.getName() + "!" + RESET);
        return nextPlayer;
    }


    public abstract int makeChoice(int min, int max);

    public void chooseCardToHide(Card card1, Card card2) {
        final String RESET  = "\u001B[0m";
        final String RED    = "\u001B[31m";
        final String YELLOW = "\u001B[33m";
        final String BLUE   = "\u001B[34m";
        final String GREEN  = "\u001B[32m";

        System.out.println(
                RED + this.getName() + RESET + ", " +
                        YELLOW + "which card do you want to hide? " +
                        BLUE + "(1, 2)" + RESET
        );

        System.out.println(BLUE + "(1) " + RESET + card1);
        System.out.println(BLUE + "(2) " + RESET + card2);

        System.out.print(BLUE + "-> " + RESET);
        int cardToHide = this.makeChoice(1, 2);

        if (cardToHide == 1) {
            this.setHiddenCard(card1);
            this.setVisibleCard(card2);
        } else {
            this.setHiddenCard(card2);
            this.setVisibleCard(card1);
        }

        System.out.println(GREEN + "You chose to hide card " + cardToHide + "." + RESET);
    }

}
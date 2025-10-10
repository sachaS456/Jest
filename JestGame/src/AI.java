import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class AI extends Player {
    public AI(String name) {
        super(name);
    }

    public Player playTurn(Game game){
        System.out.println(this.getName() + ", which card do you want to pick ?");
        int cardNumber = 1;
        ArrayList<Card> possibleCardsToPick = new ArrayList<>();
        ArrayList<Player> cardOwners = new ArrayList<>();

        for (Player player : game.getPlayers()) {
            if(player != this){
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
            System.out.println(this.getName() + ": " + "(" + cardNumber + ") "
                    + this.getVisibleCard() + "(" + (cardNumber + 1) + ") hidden card 🫣");
            possibleCardsToPick.add(this.getHiddenCard());
            cardOwners.add(this);
            possibleCardsToPick.add(this.getVisibleCard());
            cardOwners.add(this);
        }

        System.out.println("-> ");
        Random randomNumbers = new Random();
        int cardToPick = randomNumbers.nextInt(1, possibleCardsToPick.size() + 1);
        System.out.println(cardToPick);

                Card pickedCard = possibleCardsToPick.get(cardToPick - 1);
        Player nextPlayer = cardOwners.get(cardToPick - 1);

        this.pickCard(pickedCard, nextPlayer);

        if(game.countPlayersWithFullOffer() == 0){
            return null;
        }

        // next player is the player with current highest card if next player has already play
        if(game.getPlayersThatHavePlayedThisRound().contains(nextPlayer)){
            nextPlayer = game.getPlayersOrder();
        }
        return nextPlayer;
    }

    public void chooseCardToHide(Card card1, Card card2){
        System.out.println(this.getName() + ", which card do you want to hide ? (1, 2)");
        System.out.println("(1) Card 1: " + card1);
        System.out.println("(2) Card 2: " + card2);
        Random randomNumbers = new Random();
        int cardToHide = randomNumbers.nextInt(1, 2);
        if(cardToHide == 1){
            this.setHiddenCard(card1);
            this.setVisibleCard(card2);
        }else {
            this.setHiddenCard(card2);
            this.setVisibleCard(card1);
        }
    }
}

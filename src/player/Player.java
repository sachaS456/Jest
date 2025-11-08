package player;

import java.util.ArrayList;
import model.cards.Card;
import model.cards.SuitCard;
import model.cards.JokerCard;
import model.game.Game;

/**
 * Represents an abstract player in the Jest card game.
 * This class provides the core functionality for both human and AI players,
 * including card management, turn execution, and game interactions.
 *
 * <p>Concrete implementations must provide the {@link #makeChoice(int, int, ArrayList, boolean)}
 * method to define how the player makes decisions during the game.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Human
 * @see AI
 */
public abstract class Player {

    /** The name of the player */
    private String name;
    /** The collection of cards in the player's jest pile (scoring pile) */
    private ArrayList<Card> jest;
    /** The array of cards the player is currently offering (index 0: visible, index 1: hidden) */
    private Card[] offer;

    /**
     * Constructs a new Player with the specified name.
     * Initializes the player with an empty jest pile and a 2-card offer array.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.offer = new Card[2];
        this.jest = new ArrayList<>();
    }

    /**
     * Returns a string representation of the player's current state.
     * Displays the player's name, jest pile contents, and current offers.
     *
     * @return a formatted string showing the player's name, jest cards, and offer cards
     */
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

    /**
     * Gets the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player.
     *
     * @param name the new name for the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the player's jest pile (collection of scored cards).
     *
     * @return an ArrayList containing the cards in the player's jest pile
     */
    public ArrayList<Card> getJest() {
        return jest;
    }

    /**
     * Sets the player's jest pile.
     *
     * @param jest the new jest pile to set
     */
    public void setJest(ArrayList<Card> jest) {
        this.jest = jest;
    }

    /**
     * Gets the player's current offer array.
     *
     * @return an array containing the visible card (index 0) and hidden card (index 1)
     */
    public Card[] getOffer() {
        return offer;
    }

    /**
     * Sets both the visible and hidden cards in the player's offer.
     * The hidden card's visibility is automatically set to false.
     *
     * @param visibleCard the card to be visible to other players
     * @param hiddenCard the card to be hidden from other players
     */
    public void setOffer(Card visibleCard, Card hiddenCard) {
        if(hiddenCard != null) {
            hiddenCard.setVisible(false);
            this.offer[1] = hiddenCard;
        }
        if(visibleCard != null) {
            this.offer[0] = visibleCard;
        }
    }

    /**
     * Sets the visible card in the player's offer (index 0).
     *
     * @param visibleCard the card to be visible to other players
     */
    public void setVisibleCard(Card visibleCard) {
        this.offer[0] =  visibleCard;
    }

    /**
     * Sets the hidden card in the player's offer (index 1).
     * The card's visibility is automatically set to false.
     *
     * @param hiddenCard the card to be hidden from other players
     */
    public void setHiddenCard(Card hiddenCard) {
        if(hiddenCard != null) {
            hiddenCard.setVisible(false);
        }
        this.offer[1] = hiddenCard;
    }

    /**
     * Picks a card from another player's offer and adds it to this player's jest pile.
     * Removes the card from the picked player's offer and displays a message.
     *
     * @param card the card being picked
     * @param pickedPlayer the player from whom the card is being picked
     */
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

    /**
     * Adds the last remaining card from the player's offer to their jest pile.
     * This is typically called at the end of a round when one card remains.
     * If two cards are still in the offer, an error message is displayed.
     */
    public void addLastCardToJest() {
        if(this.offer[0] != null ^ this.offer[1] != null) {
            this.jest.add((this.offer[0] != null) ? this.offer[0] : this.offer[1]);
            this.offer =  null;
        }else {
            System.out.println("You still have two cards in your offer");
        }
    }

    /**
     * Gets the visible card from the player's offer.
     *
     * @return the visible card (index 0 of the offer array)
     */
    public Card getVisibleCard() {
        return this.offer[0];
    }

    /**
     * Gets the hidden card from the player's offer.
     *
     * @return the hidden card (index 1 of the offer array)
     */
    public Card getHiddenCard() {
        return this.offer[1];
    }

    /**
     * Removes and returns the last remaining card from the player's offer.
     * Checks both positions and removes whichever card exists.
     *
     * @return the card that was removed from the offer, or null if no card exists
     */
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

    /**
     * Checks if the player has at least one Joker card in their jest pile.
     *
     * @return true if the player has a Joker card, false otherwise
     */
    public boolean hasJokerCard() {
        for (Card card : jest) {
            if(card instanceof JokerCard) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a card directly to the player's jest pile.
     *
     * @param card the card to add to the jest pile
     */
    public void AddCardToJest(Card card) {
        this.jest.add(card);
    }

    /**
     * Executes the player's turn in the game.
     * Displays available cards from other players, prompts for a card choice,
     * and determines the next player based on whose card was picked.
     *
     * <p>Turn flow:</p>
     * <ol>
     *   <li>Display all available cards from opponents</li>
     *   <li>If no opponent cards available, allow picking own cards</li>
     *   <li>Player makes a choice via {@link #makeChoice(int, int, ArrayList, boolean)}</li>
     *   <li>Pick the selected card and update game state</li>
     *   <li>Determine the next player based on whose card was picked</li>
     * </ol>
     *
     * @param game the current game instance
     * @return the next player to take a turn, or null if the round should end
     */
    public Player playTurn(Game game) {
        final String RESET  = "\u001B[0m";
        final String RED    = "\u001B[31m";
        final String YELLOW = "\u001B[33m";
        final String BLUE   = "\u001B[34m";
        final String GREEN  = "\u001B[32m";

        System.out.println(RED + this.getName() + RESET + ", " + YELLOW + "which card do you want to pick ?" + RESET);
        sleep(500);

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
                    sleep(300);

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
                    sleep(300);
                }
            }
        }

        if (possibleCardsToPick.isEmpty()) {
            System.out.println(
                    YELLOW + "There are no cards available to pick from your opponents. Please choose one of your own cards:" + RESET
            );
            sleep(500);
            System.out.println(
                    RED + this.getName() + RESET + ": \n" +
                            BLUE + "(" + cardNumber + ") " + RESET + this.getVisibleCard() + " " +
                            BLUE + "(" + (cardNumber + 1) + ") " + RESET + "hidden card 🫣"
            );
            sleep(300);

            possibleCardsToPick.add(this.getHiddenCard());
            cardOwners.add(this);
            possibleCardsToPick.add(this.getVisibleCard());
            cardOwners.add(this);
        }

        System.out.print(BLUE + "-> " + RESET);
        int cardToPick = this.makeChoice(1, possibleCardsToPick.size(), possibleCardsToPick, false);

        if (cardToPick < 1 || cardToPick > possibleCardsToPick.size()) {
            return null;
        }

        Card pickedCard = possibleCardsToPick.get(cardToPick - 1);
        Player nextPlayer = cardOwners.get(cardToPick - 1);

        this.pickCard(pickedCard, nextPlayer);
        sleep(500);

        if (game.countPlayersWithFullOffer() == 0) {
            return null;
        }

        if (game.getPlayersThatHavePlayedThisRound().contains(nextPlayer)) {
            nextPlayer = game.getPlayersOrder();
        }

        if (nextPlayer == null) {
            sleep(500);
            return null;
        }

        System.out.println(GREEN + "You picked a card from " + nextPlayer.getName() + "!" + RESET);
        sleep(500);
        return nextPlayer;
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
     * Abstract method for making choices during the game.
     * Concrete implementations (Human, AI) must define how decisions are made.
     *
     * @param min the minimum valid choice value (inclusive)
     * @param max the maximum valid choice value (inclusive)
     * @param cards the list of cards available for the decision
     * @param isHidingCard true if choosing which card to hide, false if choosing which card to pick
     * @return the chosen option (typically 1-indexed)
     */
    public abstract int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard);

    /**
     * Prompts the player to choose which of two cards to hide in their offer.
     * Displays both cards and sets one as visible and the other as hidden based on the choice.
     *
     * <p>Special case: If only one card is provided (card2 is null), that card
     * is automatically set as visible with no hidden card.</p>
     *
     * @param card1 the first card option
     * @param card2 the second card option (can be null)
     */
    public void chooseCardToHide(Card card1, Card card2) {
        final String RESET  = "\u001B[0m";
        final String RED    = "\u001B[31m";
        final String YELLOW = "\u001B[33m";
        final String BLUE   = "\u001B[34m";
        final String GREEN  = "\u001B[32m";

        // Cas spécial : si card2 est null (pas assez de cartes disponibles)
        if (card2 == null) {
            System.out.println(
                RED + this.getName() + RESET + ", " +
                        YELLOW + "you only have one card available. It will be your visible card." + RESET
            );
            sleep(500);
            this.setVisibleCard(card1);
            this.setHiddenCard(null);
            System.out.println(GREEN + "Card set as visible: " + card1 + RESET);
            sleep(500);
            return;
        }

        System.out.println(
                RED + this.getName() + RESET + ", " +
                        YELLOW + "which card do you want to hide? " +
                        BLUE + "(1, 2)" + RESET
        );
        sleep(500);

        System.out.println(BLUE + "(1) " + RESET + card1);
        sleep(300);
        System.out.println(BLUE + "(2) " + RESET + card2);
        sleep(300);

        System.out.print(BLUE + "-> " + RESET);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        int cardToHide = this.makeChoice(1, 2, cards, true);
        sleep(300);

        if (cardToHide == 1) {
            this.setHiddenCard(card1);
            this.setVisibleCard(card2);
        } else {
            this.setHiddenCard(card2);
            this.setVisibleCard(card1);
        }

        System.out.println(GREEN + "You chose to hide card " + cardToHide + "." + RESET);
        sleep(500);
    }

}
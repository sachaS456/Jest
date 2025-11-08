import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a serializable snapshot of a player's state at a specific moment in the game.
 * This class is used for saving and loading game states, capturing all relevant
 * information about a player including their cards and identity.
 *
 * <p>This class implements Serializable to allow game state persistence.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Player
 * @see GameState
 */
public class PlayerState implements Serializable {
    /** Serial version UID for serialization compatibility */
    private static final long serialVersionUID = 1L;

    /** The name of the player */
    private String name;
    /** The collection of cards in the player's jest pile */
    private ArrayList<Card> jest;
    /** The array of cards the player is currently offering */
    private Card[] offer;
    /** Flag indicating whether this player is controlled by AI */
    private boolean isAI;

    /**
     * Constructs a PlayerState snapshot from a Player object.
     * Creates a deep copy of the player's jest collection and offer array
     * to preserve the state at the moment of creation.
     *
     * @param player the player whose state is being captured
     */
    public PlayerState(Player player) {
        this.name = player.getName();
        this.jest = new ArrayList<>(player.getJest());
        this.offer = player.getOffer().clone();
        this.isAI = player instanceof AI;
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
     * Gets the collection of cards in the player's jest pile.
     *
     * @return an ArrayList containing the cards in the jest pile
     */
    public ArrayList<Card> getJest() {
        return jest;
    }

    /**
     * Gets the array of cards the player is currently offering.
     *
     * @return an array of cards in the player's offer
     */
    public Card[] getOffer() {
        return offer;
    }

    /**
     * Checks whether this player is controlled by AI.
     *
     * @return true if the player is an AI, false if it's a human player
     */
    public boolean isAI() {
        return isAI;
    }
}

package model.game;

import java.io.Serializable;
import java.util.ArrayList;
import model.cards.Card;
import player.Player;
import variant.GameVariant;

/**
 * Represents a serializable snapshot of the complete game state at a specific moment.
 * This class is used for saving and loading games, capturing all necessary information
 * to restore the game to its exact state later.
 *
 * <p>The game state includes:</p>
 * <ul>
 *   <li>Current round number</li>
 *   <li>All cards in the deck</li>
 *   <li>Trophy cards</li>
 *   <li>State of all players (cards, offers, names)</li>
 *   <li>Game variant being played</li>
 *   <li>Expansion pack inclusion flag</li>
 * </ul>
 *
 * <p>This class implements Serializable to allow game state persistence to disk.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Game
 * @see PlayerState
 * @see GameSaver
 */
public class GameState implements Serializable {
    /** Serial version UID for serialization compatibility */
    private static final long serialVersionUID = 1L;

    /** The current round number in the game */
    private int roundNumber;
    /** The collection of all cards in the game deck */
    private ArrayList<Card> cards;
    /** The array of trophy cards */
    private Card[] trophies;
    /** The states of all players in the game */
    private ArrayList<PlayerState> playerStates;
    /** The name of the game variant being played */
    private String variantName;
    /** Flag indicating whether the expansion pack is included */
    private boolean includeExpansion;

    /**
     * Constructs a GameState snapshot from a Game object.
     * Creates deep copies of all mutable game components to preserve the state
     * at the moment of creation.
     *
     * @param game the game instance whose state is being captured
     * @param includeExpansion flag indicating whether the expansion pack is included
     */
    public GameState(Game game, boolean includeExpansion) {
        this.roundNumber = game.getRoundNumber();
        this.cards = new ArrayList<>(game.getCards());
        this.trophies = game.getTrophies().clone();
        this.variantName = game.getVariant().getName();
        this.includeExpansion = includeExpansion;

        this.playerStates = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            playerStates.add(new PlayerState(player));
        }
    }

    /**
     * Gets the current round number.
     *
     * @return the round number when this state was saved
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Gets the collection of all cards in the game deck.
     *
     * @return an ArrayList containing all cards in the deck
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Gets the array of trophy cards.
     *
     * @return an array containing the trophy cards
     */
    public Card[] getTrophies() {
        return trophies;
    }

    /**
     * Gets the states of all players.
     *
     * @return an ArrayList of PlayerState objects representing each player's state
     */
    public ArrayList<PlayerState> getPlayerStates() {
        return playerStates;
    }

    /**
     * Gets the name of the game variant being played.
     *
     * @return the variant name (e.g., "Classic", "Speed", "High Stakes")
     */
    public String getVariantName() {
        return variantName;
    }

    /**
     * Checks whether the expansion pack is included in this game.
     *
     * @return true if the expansion pack is included, false otherwise
     */
    public boolean isIncludeExpansion() {
        return includeExpansion;
    }
}

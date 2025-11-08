package variant;

import player.Player;
import model.game.Game;

/**
 * Defines the contract for game variants in the Jest card game.
 * This interface allows different rule sets and scoring mechanics to be implemented,
 * following the Strategy design pattern for game variations.
 *
 * <p>Game variants can modify several aspects of gameplay:</p>
 * <ul>
 *   <li>Point calculation and multipliers</li>
 *   <li>Number of cards dealt per round</li>
 *   <li>Trophy card values</li>
 *   <li>Special rules at round start and end</li>
 * </ul>
 *
 * <p>Available implementations:</p>
 * <ul>
 *   <li>{@link ClassicVariant} - Standard Jest rules with no modifications</li>
 *   <li>{@link SpeedVariant} - Fast-paced mode with fewer cards and 1.5x points</li>
 *   <li>{@link HighStakesVariant} - High-risk mode with 2x points and 3x trophy values</li>
 * </ul>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Game
 * @see ClassicVariant
 * @see SpeedVariant
 * @see HighStakesVariant
 */
public interface GameVariant {
    /**
     * Gets the name of this game variant.
     * Used for display and identification purposes in menus and game state.
     *
     * @return the name of the variant (e.g., "Classic", "Speed", "High Stakes")
     */
    String getName();

    /**
     * Gets a description of this game variant.
     * Provides players with information about the variant's special rules or features.
     *
     * @return a brief description explaining the variant's characteristics
     */
    String getDescription();

    /**
     * Calculates the total points for a player according to this variant's rules.
     * Different variants may apply different multipliers or bonuses to the base score.
     *
     * @param player the player whose points are being calculated
     * @return the total points for the player, including any variant-specific modifications
     */
    int calculatePoints(Player player);

    /**
     * Determines the number of cards each player receives at the start of a round.
     * Some variants may change the number of cards based on the round number.
     *
     * @param roundNumber the current round number (1-indexed)
     * @return the number of cards each player should receive
     */
    int getCardsPerPlayer(int roundNumber);

    /**
     * Applies special rules or displays messages at the start of each round.
     * This method is called before cards are dealt and offers are made.
     * Variants can use this to display announcements or modify game state.
     *
     * @param game the current game instance, allowing access to game state
     */
    void applyRoundStartRules(Game game);

    /**
     * Applies special rules or displays messages at the end of each round.
     * This method is called after all cards have been picked and before scoring.
     * Variants can use this to display results or apply end-of-round bonuses.
     *
     * @param game the current game instance, allowing access to game state
     */
    void applyRoundEndRules(Game game);

    /**
     * Indicates whether this variant modifies trophy card calculations.
     * Trophy cards are special high-value cards that may have different values
     * depending on the variant.
     *
     * @return true if trophy values are modified by this variant, false otherwise
     */
    boolean hasTrophyModification();

    /**
     * Gets the multiplier applied to trophy card values in this variant.
     * Only relevant if {@link #hasTrophyModification()} returns true.
     *
     * @return the trophy multiplier (1 for no modification, higher for increased value)
     */
    int getTrophyMultiplier();
}

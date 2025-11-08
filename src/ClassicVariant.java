/**
 * Represents the Classic variant of the Jest card game.
 * This variant implements the standard game rules without any modifications
 * to scoring, card distribution, or trophy values.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Standard Jest scoring rules (no multipliers)</li>
 *   <li>Always 2 cards per player each round</li>
 *   <li>No special round start or end rules</li>
 *   <li>Normal trophy card values (no modification)</li>
 * </ul>
 *
 * <p>This is the default variant and serves as the baseline for other variants.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see GameVariant
 * @see SpeedVariant
 * @see HighStakesVariant
 */
public class ClassicVariant implements GameVariant {

    /**
     * Gets the name of this game variant.
     *
     * @return the string "Classic"
     */
    @Override
    public String getName() {
        return "Classic";
    }

    /**
     * Gets a description of this game variant.
     *
     * @return a description explaining the basic game mode with standard rules
     */
    @Override
    public String getDescription() {
        return "Basic game mode with standard rules.";
    }

    /**
     * Calculates the points for a player using standard Jest scoring rules.
     * No multipliers or modifications are applied in Classic mode.
     *
     * @param player the player whose points are being calculated
     * @return the standard Jest points without any modifications
     */
    @Override
    public int calculatePoints(Player player) {
        return Game.getJestPoints(player);
    }

    /**
     * Determines the number of cards each player receives per round.
     * In Classic mode, players always receive 2 cards per round.
     *
     * @param roundNumber the current round number (not used in this variant)
     * @return always returns 2
     */
    @Override
    public int getCardsPerPlayer(int roundNumber) {
        return 2;
    }

    /**
     * Applies special rules at the start of each round.
     * Classic variant has no special round start rules.
     *
     * @param game the current game instance
     */
    @Override
    public void applyRoundStartRules(Game game) {
    }

    /**
     * Applies special rules at the end of each round.
     * Classic variant has no special round end rules.
     *
     * @param game the current game instance
     */
    @Override
    public void applyRoundEndRules(Game game) {
    }

    /**
     * Indicates whether this variant modifies trophy calculation.
     *
     * @return false, as Classic variant does not modify trophy mechanics
     */
    @Override
    public boolean hasTrophyModification() {
        return false;
    }

    /**
     * Gets the trophy multiplier for this variant.
     *
     * @return 1, as trophies have standard value in Classic mode
     */
    @Override
    public int getTrophyMultiplier() {
        return 1;
    }
}

package variant;

import player.Player;
import model.game.Game;

/**
 * Represents the High Stakes variant of the Jest card game.
 * This variant amplifies scoring with doubled base points and tripled trophy values,
 * creating a high-risk, high-reward gameplay experience.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>All base points are multiplied by 2x</li>
 *   <li>Trophy cards are worth 3x their normal value</li>
 *   <li>Standard 2 cards per player each round</li>
 * </ul>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see GameVariant
 * @see ClassicVariant
 * @see SpeedVariant
 */
public class HighStakesVariant implements GameVariant {

    /**
     * Gets the name of this game variant.
     *
     * @return the string "High Stakes"
     */
    @Override
    public String getName() {
        return "High Stakes";
    }

    /**
     * Gets a description of this game variant.
     *
     * @return a description explaining the doubled points and triple trophy values
     */
    @Override
    public String getDescription() {
        return "All points are doubled and trophy cards are worth triple!";
    }

    /**
     * Calculates the points for a player with the High Stakes multiplier applied.
     * The base Jest points are multiplied by 2.
     *
     * @param player the player whose points are being calculated
     * @return the calculated points with the 2x multiplier applied
     */
    @Override
    public int calculatePoints(Player player) {
        int basePoints = Game.getJestPoints(player);
        return basePoints * 2;
    }

    /**
     * Determines the number of cards each player receives per round.
     * In High Stakes mode, players always receive 2 cards per round.
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
     * On the first round, displays prominent messages announcing High Stakes mode
     * with doubled points and tripled trophy values.
     *
     * @param game the current game instance
     */
    @Override
    public void applyRoundStartRules(Game game) {
        final String RESET  = "\u001B[0m";
        final String RED    = "\u001B[31m";
        final String YELLOW = "\u001B[33m";

        if (game.getRoundNumber() == 1) {
            System.out.println(RED + "💰 HIGH STAKES MODE ACTIVE!" + RESET);
            System.out.println(YELLOW + "💰 All points are DOUBLED!" + RESET);
            System.out.println(YELLOW + "🏆 Trophy cards are worth TRIPLE!" + RESET);
        }
    }

    /**
     * Applies special rules at the end of each round.
     * This variant has no special end-of-round rules.
     *
     * @param game the current game instance
     */
    @Override
    public void applyRoundEndRules(Game game) {
    }

    /**
     * Indicates whether this variant modifies trophy calculation.
     *
     * @return true, as High Stakes variant triples trophy values
     */
    @Override
    public boolean hasTrophyModification() {
        return true;
    }

    /**
     * Gets the trophy multiplier for this variant.
     *
     * @return 3, indicating that trophy cards are worth triple their normal value
     */
    @Override
    public int getTrophyMultiplier() {
        return 3;
    }
}

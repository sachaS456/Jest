/**
 * Represents the Speed variant of the Jest game.
 * This variant features faster gameplay with fewer cards dealt per round
 * and a 1.5x point multiplier for all scored points.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>2 cards per player for the first 2 rounds, then 1 card per player</li>
 *   <li>All points are multiplied by 1.5x</li>
 *   <li>No trophy modifications</li>
 * </ul>
 *
 * @author Jest Game & Gatien Genevois && Sacha Himber
 * @version 1.0
 */
public class SpeedVariant implements GameVariant {

    /**
     * Gets the name of this game variant.
     *
     * @return the string "Speed"
     */
    @Override
    public String getName() {
        return "Speed";
    }

    /**
     * Gets a description of this game variant.
     *
     * @return a description explaining the fast mode with fewer cards and bonus points
     */
    @Override
    public String getDescription() {
        return "Fast Mode - Fewer cards dealt, shorter games, x1.5 points";
    }

    /**
     * Calculates the points for a player with the Speed variant multiplier applied.
     * The base Jest points are multiplied by 1.5.
     *
     * @param player the player whose points are being calculated
     * @return the calculated points with the 1.5x multiplier applied
     */
    @Override
    public int calculatePoints(Player player) {
        int basePoints = Game.getJestPoints(player);
        return (int) (basePoints * 1.5);
    }

    /**
     * Determines the number of cards each player receives per round.
     * In Speed mode, players receive 2 cards for rounds 1-2, then 1 card per round.
     *
     * @param roundNumber the current round number
     * @return 2 if roundNumber is 1 or 2, otherwise 1
     */
    @Override
    public int getCardsPerPlayer(int roundNumber) {
        return roundNumber <= 2 ? 2 : 1;
    }

    /**
     * Applies special rules at the start of each round.
     * Displays a Speed mode message and notifies players when only 1 card
     * will be dealt (rounds 3+).
     *
     * @param game the current game instance
     */
    @Override
    public void applyRoundStartRules(Game game) {
        final String RESET  = "\u001B[0m";
        final String YELLOW = "\u001B[33m";
        final String CYAN   = "\u001B[36m";

        System.out.println(CYAN + "⚡ Speed Mode: Fast-paced round!" + RESET);
        if (game.getRoundNumber() > 2) {
            System.out.println(YELLOW + "⚡ Only 1 card per player this round!" + RESET);
        }
    }

    /**
     * Applies special rules at the end of each round.
     * Displays a message indicating that the Speed bonus (+50%) has been applied.
     *
     * @param game the current game instance
     */
    @Override
    public void applyRoundEndRules(Game game) {
        final String RESET  = "\u001B[0m";
        final String GREEN  = "\u001B[32m";
        System.out.println(GREEN + "⚡ Speed bonus applied: +50% points!" + RESET);
    }

    /**
     * Indicates whether this variant modifies trophy calculation.
     *
     * @return false, as Speed variant does not modify trophy mechanics
     */
    @Override
    public boolean hasTrophyModification() {
        return false;
    }

    /**
     * Gets the trophy multiplier for this variant.
     *
     * @return 1, as trophies are not modified in Speed mode
     */
    @Override
    public int getTrophyMultiplier() {
        return 1;
    }
}

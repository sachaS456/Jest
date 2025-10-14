/**
 * Variante High Stakes du jeu Jest
 * Tous les points sont doublés et les trophées valent triple
 */
public class HighStakesVariant implements GameVariant {

    @Override
    public String getName() {
        return "High Stakes";
    }

    @Override
    public String getDescription() {
        return "All points are doubled and trophy cards are worth triple!";
    }

    @Override
    public int calculatePoints(Player player) {
        int basePoints = Game.getJestPoints(player);
        return basePoints * 2;
    }

    @Override
    public int getCardsPerPlayer(int roundNumber) {
        return 2;
    }

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

    @Override
    public void applyRoundEndRules(Game game) {
    }

    @Override
    public boolean hasTrophyModification() {
        return true;
    }

    @Override
    public int getTrophyMultiplier() {
        return 3;
    }
}


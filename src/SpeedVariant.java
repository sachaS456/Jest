public class SpeedVariant implements GameVariant {

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public String getDescription() {
        return "Fast Mode - Fewer cards dealt, shorter games, x1.5 points";
    }

    @Override
    public int calculatePoints(Player player) {
        int basePoints = Game.getJestPoints(player);
        return (int) (basePoints * 1.5);
    }

    @Override
    public int getCardsPerPlayer(int roundNumber) {
        return roundNumber <= 2 ? 2 : 1;
    }

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

    @Override
    public void applyRoundEndRules(Game game) {
        final String RESET  = "\u001B[0m";
        final String GREEN  = "\u001B[32m";
        System.out.println(GREEN + "⚡ Speed bonus applied: +50% points!" + RESET);
    }

    @Override
    public boolean hasTrophyModification() {
        return false;
    }

    @Override
    public int getTrophyMultiplier() {
        return 1;
    }
}


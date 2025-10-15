public class ClassicVariant implements GameVariant {

    @Override
    public String getName() {
        return "Classic";
    }

    @Override
    public String getDescription() {
        return "Basic game mode with standard rules.";
    }

    @Override
    public int calculatePoints(Player player) {
        return Game.getJestPoints(player);
    }

    @Override
    public int getCardsPerPlayer(int roundNumber) {
        return 2;
    }

    @Override
    public void applyRoundStartRules(Game game) {
    }

    @Override
    public void applyRoundEndRules(Game game) {
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


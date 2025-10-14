import java.util.ArrayList;

public interface GameVariant {
    String getName();

    String getDescription();

    int calculatePoints(Player player);

    int getCardsPerPlayer(int roundNumber);

    void applyRoundStartRules(Game game);

    void applyRoundEndRules(Game game);

    boolean hasTrophyModification();

    int getTrophyMultiplier();
}


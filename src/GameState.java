import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private int roundNumber;
    private ArrayList<Card> cards;
    private Card[] trophies;
    private ArrayList<PlayerState> playerStates;
    private String variantName;
    private boolean includeExpansion;

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

    public int getRoundNumber() {
        return roundNumber;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Card[] getTrophies() {
        return trophies;
    }

    public ArrayList<PlayerState> getPlayerStates() {
        return playerStates;
    }

    public String getVariantName() {
        return variantName;
    }

    public boolean isIncludeExpansion() {
        return includeExpansion;
    }
}


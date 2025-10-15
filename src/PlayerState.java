import java.io.Serializable;
import java.util.ArrayList;

public class PlayerState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private ArrayList<Card> jest;
    private Card[] offer;
    private boolean isAI;

    public PlayerState(Player player) {
        this.name = player.getName();
        this.jest = new ArrayList<>(player.getJest());
        this.offer = player.getOffer().clone();
        this.isAI = player instanceof AI;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getJest() {
        return jest;
    }

    public Card[] getOffer() {
        return offer;
    }

    public boolean isAI() {
        return isAI;
    }
}


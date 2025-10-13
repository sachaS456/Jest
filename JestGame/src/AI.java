import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class AI extends Player {
    private final IPlayStrategy strategy;

    public AI(String name, IPlayStrategy strategy) {
        super(name);
        this.strategy = strategy;
    }


    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards) {
        return this.strategy.makeChoice(min, max, cards);
    }
}

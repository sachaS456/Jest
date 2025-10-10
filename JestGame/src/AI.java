import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class AI extends Player {
    public AI(String name) {
        super(name);
    }

    @Override
    public int makeChoice(int min, int max) {
        Random randomNumbers = new Random();
        int cardToPick = randomNumbers.nextInt(min, max);
        System.out.println(cardToPick);
        return cardToPick;
    }
}

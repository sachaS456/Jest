import java.util.ArrayList;
import java.util.Scanner;

public class Human extends Player {
    public Human(String name) {
        super(name);
    }

    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {

            while (!scanner.hasNextInt()) {
                scanner.next();
            }

            choice = scanner.nextInt();

            if (choice < min || choice > max) {
                System.out.println("Please make a choice between " + min + " and " + max + ".");
            }
        } while (choice < min || choice > max);

        return choice;
    }

}

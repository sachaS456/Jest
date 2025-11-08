import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a human player in the Jest card game.
 * This class extends Player and implements decision-making through user input
 * via the console, allowing a real person to play the game.
 *
 * <p>Human players make choices by entering numbers through the keyboard,
 * with input validation to ensure only valid choices are accepted.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Player
 * @see AI
 */
public class Human extends Player {
    /**
     * Constructs a new Human player with the specified name.
     *
     * @param name the name of the human player
     */
    public Human(String name) {
        super(name);
    }

    /**
     * Prompts the human player to make a choice via console input.
     * Repeatedly asks for input until a valid choice within the specified range is entered.
     *
     * <p>Input validation:</p>
     * <ul>
     *   <li>Ignores non-integer input</li>
     *   <li>Rejects values outside the [min, max] range</li>
     *   <li>Continues prompting until a valid choice is made</li>
     * </ul>
     *
     * @param min the minimum valid choice value (inclusive)
     * @param max the maximum valid choice value (inclusive)
     * @param cards the list of cards relevant to the decision (not used for human input)
     * @param isHidingCard true if choosing which card to hide, false if choosing which card to pick
     *                     (not used for human input, but required by the interface)
     * @return the player's chosen option as an integer between min and max (inclusive)
     */
    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard) {
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

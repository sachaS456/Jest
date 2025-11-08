import java.util.ArrayList;

/**
 * Defines the various special effects that can be associated with trophy cards in the Jest game.
 * Each effect determines how a trophy card is awarded to a player based on different criteria
 * evaluated at the end of the game.
 *
 * <p>Effect categories:</p>
 * <ul>
 *   <li><b>Sign-based:</b> HIGHEST, LOWEST (target specific suit cards)</li>
 *   <li><b>Value-based:</b> MAJORITY (most cards of a specific value)</li>
 *   <li><b>Joker-based:</b> JOKER (requires having a Joker card)</li>
 *   <li><b>Score-based:</b> BEST_JEST, BEST_JEST_WITHOUT_JOKER</li>
 *   <li><b>Quantity-based:</b> MOST_CARDS, LEAST_CARDS (expansion)</li>
 *   <li><b>Parity-based:</b> EVEN_VALUES, ODD_VALUES (expansion)</li>
 *   <li><b>Uniqueness-based:</b> NO_DUPLICATES (expansion)</li>
 * </ul>
 *
 * <p>Each effect implements the {@link #CheckEffect(int, Sign, ArrayList)} method
 * to determine the winner of the trophy card.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Card
 * @see Player
 * @see Game
 */
public enum CardEffect {
    /**
     * Awards the trophy to the player with the highest value card of a specific suit.
     *
     * <p>Requirements:</p>
     * <ul>
     *   <li>sign parameter must not be null</li>
     *   <li>Player must have at least one card of the specified suit</li>
     * </ul>
     *
     * @throws NullPointerException if sign is null
     */
    HIGHEST {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            if (sign == null)
                throw new NullPointerException("Sign can't be null");
            Player winner = null;
            SuitCard higher = null;
            for(Player player : players){
                ArrayList<Card> jest = player.getJest();
                for (Card card : jest) {
                    if (card instanceof SuitCard suitCard) {
                        if (suitCard.getSign() == sign && (higher == null || suitCard.getValue() > higher.getValue())) {
                            higher = suitCard;
                            winner = player;
                        }
                    }
                }
            }

            return winner;
        }
    },
    /**
     * Awards the trophy to the player with the lowest value card of a specific suit.
     *
     * <p>Requirements:</p>
     * <ul>
     *   <li>sign parameter must not be null</li>
     *   <li>Player must have at least one card of the specified suit</li>
     * </ul>
     *
     * @throws NullPointerException if sign is null
     */
    LOWEST {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            if (sign == null)
                throw new NullPointerException("Sign can't be null");
            Player winner = null;
            SuitCard higher = null;
            for(Player player : players){
                ArrayList<Card> jest = player.getJest();
                for (Card card : jest) {
                    if (card instanceof SuitCard suitCard) {
                        if (suitCard.getSign() == sign && (higher == null || suitCard.getValue() < higher.getValue())) {
                            higher = suitCard;
                            winner = player;
                        }
                    }
                }
            }

            return winner;
        }
    },
    /**
     * Awards the trophy to the player with the most cards of a specific value.
     * For example, most cards with value 3, or most cards with value 7.
     *
     * <p>Requirements:</p>
     * <ul>
     *   <li>value parameter must be between 1 and 4 (inclusive)</li>
     *   <li>Player must have at least one card with the specified value</li>
     * </ul>
     *
     * @throws IllegalArgumentException if value is not between 1 and 4
     */
    MAJORITY {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            if (value <= 0 || value > 4)
                throw new IllegalArgumentException("Value must be between 0 and 4");
            Player winner = null;
            int countMajority = 0;
            for(Player player : players){
                ArrayList<Card> jest = player.getJest();
                int countCardInJest = 0;
                for (Card card : jest) {
                    if (card instanceof SuitCard suitCard) {
                        if (suitCard.getValue() == value) {
                            countCardInJest++;
                        }
                    }
                }

                if (countMajority < countCardInJest){
                    winner = player;
                    countMajority = countCardInJest;
                }
            }

            return winner;
        }
    },
    /**
     * Awards the trophy to the first player who has a Joker card in their jest pile.
     * If no player has a Joker, returns null.
     *
     * <p>Note: This effect ignores the value and sign parameters.</p>
     */
    JOKER {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            for(Player player : players){
                if (player.hasJokerCard()) {
                    return player;
                }
            }

            return null;
        }
    },
    /**
     * Awards the trophy to the player with the highest total Jest score.
     * Uses the standard Jest scoring rules to calculate points.
     * In case of a tie, the last player checked wins.
     *
     * <p>Note: This effect ignores the value and sign parameters.</p>
     */
    BEST_JEST {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            Player winner = null;
            int besJest = 0;
            for(Player player : players){
                int playerJest = Game.getJestPoints(player);
                if (playerJest >= besJest){
                    winner = player;
                    besJest = playerJest;
                }
            }

            return winner;
        }
    },
    /**
     * Awards the trophy to the player with the highest total Jest score
     * among players who do NOT have a Joker card.
     * Players with Joker cards are excluded from consideration.
     *
     * <p>Note: This effect ignores the value and sign parameters.</p>
     */
    BEST_JEST_WITHOUT_JOKER {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            Player winner = null;
            int besJest = 0;
            for(Player player : players){
                if (player.hasJokerCard()){
                    continue;
                }
                int playerJest = Game.getJestPoints(player);
                if (playerJest >= besJest){
                    winner = player;
                    besJest = playerJest;
                }
            }

            return winner;
        }
    },
    /**
     * Awards the trophy to the player with the most total cards in their jest pile.
     * This is an expansion pack effect.
     *
     * <p>Note: This effect ignores the value and sign parameters.</p>
     */
    MOST_CARDS {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            Player winner = null;
            int mostCards = 0;
            for(Player player : players){
                int cardCount = player.getJest().size();
                if (cardCount > mostCards){
                    winner = player;
                    mostCards = cardCount;
                }
            }
            return winner;
        }
    },
    /**
     * Awards the trophy to the player with the fewest total cards in their jest pile.
     * This is an expansion pack effect.
     *
     * <p>Note: This effect ignores the value and sign parameters.</p>
     */
    LEAST_CARDS {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            Player winner = null;
            int leastCards = Integer.MAX_VALUE;
            for(Player player : players){
                int cardCount = player.getJest().size();
                if (cardCount < leastCards){
                    winner = player;
                    leastCards = cardCount;
                }
            }
            return winner;
        }
    },
    /**
     * Awards the trophy to the player with the most cards having even values (2, 4, 6, 8, etc.).
     * Only SuitCards are considered; Joker cards are ignored.
     * This is an expansion pack effect.
     *
     * <p>Note: This effect ignores the value and sign parameters.</p>
     */
    EVEN_VALUES {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            Player winner = null;
            int mostEvenCards = 0;
            for(Player player : players){
                int evenCount = 0;
                for (Card card : player.getJest()) {
                    if (card instanceof SuitCard suitCard && suitCard.getValue() % 2 == 0) {
                        evenCount++;
                    }
                }
                if (evenCount > mostEvenCards){
                    winner = player;
                    mostEvenCards = evenCount;
                }
            }
            return winner;
        }
    },
    /**
     * Awards the trophy to the player with the most cards having odd values (1, 3, 5, 7, etc.).
     * Only SuitCards are considered; Joker cards are ignored.
     * This is an expansion pack effect.
     *
     * <p>Note: This effect ignores the value and sign parameters.</p>
     */
    ODD_VALUES {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            Player winner = null;
            int mostOddCards = 0;
            for(Player player : players){
                int oddCount = 0;
                for (Card card : player.getJest()) {
                    if (card instanceof SuitCard suitCard && suitCard.getValue() % 2 == 1) {
                        oddCount++;
                    }
                }
                if (oddCount > mostOddCards){
                    winner = player;
                    mostOddCards = oddCount;
                }
            }
            return winner;
        }
    },
    /**
     * Awards the trophy to the first player who has no duplicate values in their jest pile.
     * A player qualifies if all their SuitCards have unique values.
     * Joker cards are ignored in the uniqueness check.
     * This is an expansion pack effect.
     *
     * <p>Note: This effect ignores the value and sign parameters.</p>
     */
    NO_DUPLICATES {
        @Override
        public Player CheckEffect(int value, Sign sign, ArrayList<Player> players) {
            Player winner = null;
            for(Player player : players){
                boolean hasDuplicates = false;
                ArrayList<Integer> values = new ArrayList<>();
                for (Card card : player.getJest()) {
                    if (card instanceof SuitCard suitCard) {
                        if (values.contains(suitCard.getValue())) {
                            hasDuplicates = true;
                            break;
                        }
                        values.add(suitCard.getValue());
                    }
                }
                if (!hasDuplicates) {
                    winner = player;
                    break;
                }
            }
            return winner;
        }
    };

    /**
     * Evaluates the effect condition and determines which player wins the trophy.
     * Each effect constant implements this method with its specific logic.
     *
     * <p>Parameter usage varies by effect:</p>
     * <ul>
     *   <li>HIGHEST, LOWEST: require sign parameter</li>
     *   <li>MAJORITY: requires value parameter (1-4)</li>
     *   <li>Other effects: ignore value and sign parameters</li>
     * </ul>
     *
     * @param value the value parameter for effects that use it (e.g., MAJORITY)
     * @param sign the sign parameter for effects that use it (e.g., HIGHEST, LOWEST)
     * @param players the list of all players to evaluate
     * @return the Player who wins the trophy, or null if no player qualifies
     * @throws NullPointerException if sign is required but null
     * @throws IllegalArgumentException if value is required but out of valid range
     */
    public abstract Player CheckEffect(int value, Sign sign, ArrayList<Player> players);
}

import java.util.ArrayList;

public enum CardEffect {
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
    // Effect for extension cards
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


    public abstract Player CheckEffect(int value, Sign sign, ArrayList<Player> players);

    /* public Joueur CheckCondition(ArrayList<Player> players) { a mettre dans game

    }*/
}

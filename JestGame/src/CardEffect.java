import java.util.ArrayList;

public enum CardEffect {
    HIGHEST {
        @Override
        public Player CheckEffect(int value, Sign sign, Player[] players) {
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
        public Player CheckEffect(int value, Sign sign, Player[] players) {
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
        public Player CheckEffect(int value, Sign sign, Player[] players) {
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
        public Player CheckEffect(int value, Sign sign, Player[] players) {
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
        public Player CheckEffect(int value, Sign sign, Player[] players) {
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
        public Player CheckEffect(int value, Sign sign, Player[] players) {
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
    };


    public abstract Player CheckEffect(int value, Sign sign, Player[] players);

    /* public Joueur CheckCondition(Player[] players) { a mettre dans game

    }*/
}

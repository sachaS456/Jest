import java.util.ArrayList;

public enum CardEffect {
    HIGHEST {
        @Override
        public Player CheckEffect(int value, Color color, Player[] players) {
            return null;
        }
    },
    LOWEST {
        @Override
        public Player CheckEffect(int value, Color color, Player[] players) {
            return null;
        }
    },
    MAJORITY {
        @Override
        public Player CheckEffect(int value, Color color, Player[] players) {
            return null;
        }
    },
    JOKER {
        @Override
        public Player CheckEffect(int value, Color color, Player[] players) {
            return null;
        }
    },
    BEST_JEST {
        @Override
        public Player CheckEffect(int value, Color color, Player[] players) {
            return null;
        }
    },
    BEST_JEST_WITHOUT_JOKER {
        @Override
        public Player CheckEffect(int value, Color color, Player[] players) {
            return null;
        }
    };


    public abstract Player CheckEffect(int value, Color color, Player[] players);

    /* public Joueur CheckCondition(Player[] players) { a mettre dans game

    }*/
}

public enum CardEffect {
    ONE("10000"),
    TWO("20000"),
    THREE("30000"),
    FOUR("40000"),
    FIVE("50000"),
    SIX("60000"),
    SEVEN("70000"),
    CLOVER("01000"),
    HEART("02000"),
    TILE("03000"),
    SPIKE("04000"),
    MAJORITY("00100"),
    WITH_JOKER("00010"),
    WITHOUT_JOKER("00000"),
    BEST_JEST("00001"),
    HIGHEST("00002"),
    LOWEST("00003");

    private final String hexCode;

    CardEffect(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getHexCode() {
        return hexCode;
    }

    /* public Joueur CheckCondition(Player[] players) { a mettre dans game

    }*/
}

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class JestScoreVisitor implements CardVisitor {
    private int score = 0;
    private boolean hasJoker = false;
    private Map<Sign, List<Integer>> cardsBySign;

    public JestScoreVisitor() {
        cardsBySign = new EnumMap<>(Sign.class);
        for (Sign sign : Sign.values()) {
            cardsBySign.put(sign, new ArrayList<>());
        }
    }

    @Override
    public void visit(SuitCard card) {
        cardsBySign.get(card.getSign()).add(card.getValue());
    }

    @Override
    public void visit(JokerCard card) {
        hasJoker = true;
    }

    @Override
    public int getScore() {
        int heartCount = cardsBySign.get(Sign.HEARTH).size();
        int spikeCount = cardsBySign.get(Sign.SPIKE).size();
        int cloverCount = cardsBySign.get(Sign.CLOVER).size();
        int tileCount = cardsBySign.get(Sign.TILE).size();

        for (Integer value : cardsBySign.get(Sign.SPIKE)) {
            score += value;
        }
        for (Integer value : cardsBySign.get(Sign.CLOVER)) {
            score += value;
        }
        for (Integer value : cardsBySign.get(Sign.TILE)) {
            score -= value;
        }

        if (hasJoker) {
            if (heartCount >= 1 && heartCount <= 3) {
                for (Integer value : cardsBySign.get(Sign.HEARTH)) {
                    score -= value;
                }
            } else if (heartCount == 4) {
                for (Integer value : cardsBySign.get(Sign.HEARTH)) {
                    score += value;
                }
            }

            if (heartCount == 0) {
                score += 4;
            }
        }

        if (heartCount == 1) score += 4;
        if (spikeCount == 1) score += 4;
        if (cloverCount == 1) score += 4;
        if (tileCount == 1) score += 4;

        for (Integer spikeValue : cardsBySign.get(Sign.SPIKE)) {
            if (cardsBySign.get(Sign.CLOVER).contains(spikeValue)) {
                score += 2;
            }
        }

        return score;
    }
}

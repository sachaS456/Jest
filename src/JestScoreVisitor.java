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
        int spadeCount = cardsBySign.get(Sign.SPADE).size();
        int clubCount = cardsBySign.get(Sign.CLUB).size();
        int diamondCount = cardsBySign.get(Sign.DIAMOND).size();

        for (Integer value : cardsBySign.get(Sign.SPADE)) {
            score += value;
        }
        for (Integer value : cardsBySign.get(Sign.CLUB)) {
            score += value;
        }
        for (Integer value : cardsBySign.get(Sign.DIAMOND)) {
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
        if (spadeCount == 1) score += 4;
        if (clubCount == 1) score += 4;
        if (diamondCount == 1) score += 4;

        for (Integer SPADEValue : cardsBySign.get(Sign.SPADE)) {
            if (cardsBySign.get(Sign.CLUB).contains(SPADEValue)) {
                score += 2;
            }
        }

        return score;
    }
}

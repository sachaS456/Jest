public interface CardVisitor {
    void visit(SuitCard card);
    void visit(JokerCard card);
    int getScore();
}


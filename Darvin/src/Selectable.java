interface Selectable<T> {
    void mutation(MutationType mutationType, int powerOfMutation);
    T crossing(T second);
    int getPower();
    void setPower();
    int fight(T enemy);
    String toString();
    T clone();
}

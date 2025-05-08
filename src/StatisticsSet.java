public interface StatisticsSet<E> {
    Object DELETED = new Object();
    void insert(E obj) throws NullPointerException, OutOfSpaceException;
    boolean contains(E obj) throws NullPointerException;
    void delete(E obj) throws NullPointerException;
    void resetAccesses();
    int accessCount();
    int size();
    int load();
    int capacity();
}

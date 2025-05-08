/**
 * Name: Yash Parmar
 * Pledge: I pledge my honor that I have abided by the Stevens Honor System.
 *
 * A hash table implementation using quadratic probing.
 * Supports insertion, deletion, and lookup operations.
 * Tracks access statistics and handles collisions with quadratic probing.
 *
 * @author: Yash Parmar
 * @param <E> the type of elements stored in this hash table
 */

public class QuadraticHashTable<E> implements StatisticsSet<E> {

    /** The array representing the hash table. */
    protected Object[] table;

    /** The number of elements currently stored in the table. */
    protected int size;

    /** The number of slots that are currently occupied (including DELETED slots). */
    protected int occupiedSlots;

    /** The number of times the table has been accessed during operations. */
    protected int accesses;

    /**
     * Constructs a new QuadraticHashTable with the specified initial capacity.
     *
     * @param capacity the initial capacity of the table
     */

    public QuadraticHashTable(int capacity) {
        table = new Object[capacity];
        size = 0;
        occupiedSlots = 0;
        accesses = 0;
    }

    /**
     * Computes the hash index of an object.
     *
     * @param obj the object to hash
     * @return the hash index within the table
     */

    protected int getHash(E obj) {
        return Math.floorMod(obj.hashCode(), table.length);
    }

    /**
     * Finds the index of the specified object using quadratic probing.
     *
     * @param obj the object to find
     * @return the index of the object if found; otherwise -1
     */

    protected int findIndex(E obj) {
        int hash = getHash(obj);

        for (int i = 0; i < table.length; i++) {
            int currentIndex = Math.floorMod(hash + (i * (i + 1)) / 2, table.length);
            Object current = table[currentIndex];
            accesses++;

            if (current == null) {
                return -1;
            }

            if (current == DELETED) {
                continue;
            }

            if (obj.equals(current)) {
                return currentIndex;
            }
        }
        return -1;
    }

    /**
     * Inserts the specified object into the hash table.
     * If the object already exists, the table remains unchanged.
     *
     * @param obj the object to insert
     * @throws NullPointerException if the object is null
     * @throws OutOfSpaceException if the table is full
     */

    @Override
    public void insert(E obj) throws NullPointerException, OutOfSpaceException {
        if (obj == null) {
            throw new NullPointerException("Cannot insert null object");
        }

        if (contains(obj)) {
            return;
        }

        if (size == table.length) {
            throw new OutOfSpaceException("Hash table is full");
        }

        int hash = getHash(obj);

        int firstAvailable = -1;
        for (int i = 0; i < table.length; i++) {
            int currentIndex = Math.floorMod(hash + (i * (i + 1)) / 2, table.length);
            Object current = table[currentIndex];

            if (current == null || current == DELETED) {
                if (firstAvailable == -1) {
                    firstAvailable = currentIndex;
                }
                break;
            }

            if (obj.equals(current)) {
                return;
            }
        }

        if (firstAvailable != -1) {
            boolean wasEmpty = table[firstAvailable] == null;
            table[firstAvailable] = obj;
            size++;
            if (wasEmpty) {
                occupiedSlots++;
            }
        } else {
            throw new OutOfSpaceException("No available slot found");
        }
    }

    /**
     * Checks if the specified object exists in the table.
     *
     * @param obj the object to search for
     * @return true if the object exists, false otherwise
     * @throws NullPointerException if the object is null
     */

    @Override
    public boolean contains(E obj) throws NullPointerException {
        if (obj == null) {
            throw new NullPointerException("Cannot check for null object");
        }
        return findIndex(obj) >= 0;
    }

    /**
     * Deletes the specified object from the table if it exists.
     *
     * @param obj the object to delete
     * @throws NullPointerException if the object is null
     */

    @Override
    public void delete(E obj) throws NullPointerException {
        if (obj == null) {
            throw new NullPointerException("Cannot delete null object");
        }

        int index = findIndex(obj);
        if (index >= 0) {
            table[index] = DELETED;
            size--;
        }
    }

    /**
     * Resets the count of accesses.
     */

    @Override
    public void resetAccesses() {
        accesses = 0;
    }

    /**
     * Returns the number of accesses made.
     *
     * @return the number of accesses
     */

    @Override
    public int accessCount() {
        return accesses;
    }

    /**
     * Returns the number of elements in the table.
     *
     * @return the number of elements
     */

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the number of occupied slots in the table.
     *
     * @return the number of occupied slots
     */

    @Override
    public int load() {
        return occupiedSlots;
    }

    /**
     * Returns the total capacity of the table.
     *
     * @return the capacity of the table
     */

    @Override
    public int capacity() {
        return table.length;
    }
}
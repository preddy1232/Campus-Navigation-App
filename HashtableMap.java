// == CS400 Spring 2024 File Header Information ==
// Name: Prithvi Reddy
// Email: pdreddy@wisc.edu
// Lecturer: Professor Florian
// Notes to Grader: <optional extra notes>
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * HashtableMap class implements the MapADT interface using an array of linked lists for handling
 * collisions through chaining.
 */
public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
  protected LinkedList<Pair>[] table;
  private int size;
  private static final int DEFAULT_CAPACITY = 64;
  private static final double LOAD_FACTOR_THRESHOLD = 0.8;

  protected class Pair {
    KeyType key;
    ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }
  }

  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity) {
    table = (LinkedList<Pair>[]) new LinkedList[capacity];
    for (int i = 0; i < capacity; i++) {
      table[i] = new LinkedList<>();
    }
    size = 0;
  }

  public HashtableMap() {
    this(DEFAULT_CAPACITY);
  }

  @Override
  public void put(KeyType key, ValueType value) {
    if (key == null)
      throw new NullPointerException("Key cannot be null.");
    int index = getIndex(key);
    for (Pair pair : table[index]) {
      if (pair.key.equals(key))
        throw new IllegalArgumentException("Duplicate key.");
    }
    table[index].addFirst(new Pair(key, value));
    size++;
    if (size >= LOAD_FACTOR_THRESHOLD * table.length) {
      resize();
    }
  }

  @Override
  public boolean containsKey(KeyType key) {
    int index = getIndex(key);
    for (Pair pair : table[index]) {
      if (pair.key.equals(key))
        return true;
    }
    return false;
  }

  @Override
  public ValueType get(KeyType key) {
    int index = getIndex(key);
    for (Pair pair : table[index]) {
      if (pair.key.equals(key))
        return pair.value;
    }
    throw new NoSuchElementException("Key not found.");
  }

  @Override
  public ValueType remove(KeyType key) {
    int index = getIndex(key);
    LinkedList<Pair> bucket = table[index];
    for (Pair pair : bucket) {
      if (pair.key.equals(key)) {
        bucket.remove(pair);
        size--;
        return pair.value;
      }
    }
    throw new NoSuchElementException("Key not found for removal.");
  }

  @Override
  public void clear() {
    for (LinkedList<Pair> bucket : table) {
      bucket.clear();
    }
    size = 0;
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public int getCapacity() {
    return table.length;
  }

  private int getIndex(KeyType key) {
    return Math.abs(key.hashCode()) % table.length;
  }

  @SuppressWarnings("unchecked")
  private void resize() {
    LinkedList<Pair>[] oldTable = table;
    table = (LinkedList<Pair>[]) new LinkedList[oldTable.length * 2];
    for (int i = 0; i < table.length; i++) {
      table[i] = new LinkedList<>();
    }
    size = 0; // Reset size to correctly add elements without duplication
    for (LinkedList<Pair> bucket : oldTable) {
      for (Pair pair : bucket) {
        put(pair.key, pair.value); // rehash
      }
    }
  }

  /**
   * Tests that put() successfully adds a new key-value pair and that the key can be retrieved.
   */
  @Test
  public void testPutAndGet() {
    HashtableMap<String, Integer> map = new HashtableMap<>();
    map.put("key1", 100);
    Integer value = map.get("key1");
    Assertions.assertEquals(100, value, "Value associated with 'key1' should be 100.");
  }

  /**
   * Tests that put() throws IllegalArgumentException when adding a duplicate key.
   */
  @Test
  public void testPutThrowsExceptionOnDuplicateKey() {
    HashtableMap<String, Integer> map = new HashtableMap<>();
    map.put("key1", 100);
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      map.put("key1", 200);
    }, "Should throw IllegalArgumentException on duplicate key.");
  }

  /**
   * Tests that containsKey() returns true for existing keys and false for non-existing keys.
   */
  @Test
  public void testContainsKey() {
    HashtableMap<String, Integer> map = new HashtableMap<>();
    map.put("key1", 100);
    Assertions.assertTrue(map.containsKey("key1"), "Map should contain key 'key1'.");
    Assertions.assertFalse(map.containsKey("key2"), "Map should not contain key 'key2'.");
  }

  /**
   * Tests that remove() correctly removes the mapping for a key and returns the correct value.
   */
  @Test
  public void testRemove() {
    HashtableMap<String, Integer> map = new HashtableMap<>();
    map.put("key1", 100);
    Assertions.assertEquals(100, map.remove("key1"), "Remove should return the value 100.");
    Assertions.assertFalse(map.containsKey("key1"), "Map should no longer contain key 'key1'.");
  }

  /**
   * Tests that clear() removes all entries.
   */
  @Test
  public void testClear() {
    HashtableMap<String, Integer> map = new HashtableMap<>();
    map.put("key1", 100);
    map.put("key2", 200);
    map.clear();
    Assertions.assertEquals(0, map.getSize(), "Map size should be 0 after clear.");
    Assertions.assertFalse(map.containsKey("key1"),
        "Map should not contain key 'key1' after clear.");
    Assertions.assertFalse(map.containsKey("key2"),
        "Map should not contain key 'key2' after clear.");
  }
}

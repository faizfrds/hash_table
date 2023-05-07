package structures;

import java.util.Iterator;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

//This class implements a hashtable for a generic key and value type using an array backend.
public class ArrayHashTable<K, V> implements HashTable<K, V> {

  //Default constructor values- DO NOT CHANGE.
  public static final int DEFAULT_CAPACITY = 5;
  public static final double DEFAULT_LOAD_FACTOR = 0.7;

  //Provided instance variables- DO NOT CHANGE
  protected K[] keyArray;  //stores all keys 
  protected V[] valueArray;  // stores all values
  protected boolean[] isActiveArray;  // if true the cell is active, false the cell has data to be deleted.
  protected CollisionHandler <K> collisionHandler;  // collision handler can be either linear or quadratic.
  protected int count; // counts the number of key/value pairs on the hashtable
  protected int capacity;  // length of the arrays
  private double loadFactor;  // the ratio (0 < loadFactor < 1) that determines when to resize the hashtable.

  /**
   *   Initialize count to 0, capacity and loadFactor to the respective DEFAULT values,
   *   collisionHandler to the collisionHdler argument, create the three arrays
   *   for keys, values, and the isActive boolean array.
   */
  public ArrayHashTable(CollisionHandler <K> collisionHdler) {
      //TODO: Implement this method.
      count = 0;
      capacity = DEFAULT_CAPACITY;
      loadFactor = DEFAULT_LOAD_FACTOR;
      this.collisionHandler = collisionHdler;
      keyArray = (K[]) new Object[capacity];
      valueArray = (V[]) new Object[capacity];
      isActiveArray = new boolean[capacity];
  }

  /**
   *   Initialize count to 0, capacity to the argument, loadFactor to the respective DEFAULT values,
   *   collisionHandler to the collisionHdler argument, create the three arrays
   *   for keys, values, and the isActive boolean array.
   */
  public ArrayHashTable(int capacity, CollisionHandler <K> collisionHdler){
      //TODO: Implement this method.

      count = 0;
      this.capacity = capacity;
      loadFactor = DEFAULT_LOAD_FACTOR;
      this.collisionHandler = collisionHdler;
      keyArray = (K[]) new Object[capacity];
      valueArray = (V[]) new Object[capacity];
      isActiveArray = new boolean[capacity];
  }

  /**
   *   Initialize count to 0, capacity and loadFactor to the respective argument values,
   *   collisionHandler to the collisionHdler argument, create the three arrays
   *   for keys, values, and the isActive boolean array.
   */
  public ArrayHashTable(int capacity, double loadFactor, CollisionHandler <K> collisionHdler){
      //TODO: Implement this method.

      count = 0;
      this.capacity = capacity;
      this.loadFactor = loadFactor;
      this.collisionHandler = collisionHdler;
      keyArray = (K[]) new Object[capacity];
      valueArray = (V[]) new Object[capacity];
      isActiveArray = new boolean[capacity];
  }

  /** 
   * Returns the number of elements in the hash table.
   */
  public int getSize() {
      //TODO: Implement this method.

      int size = 0;
      for (int i = 0; i < isActiveArray.length; i++){

        if (isActiveArray[i]) size++;
      }
      return size;
  }

   /**
   * One approach: Double the capacity of the hashtable. Create new, larger arrays.
   * For each key in the key array, if that cell is active (true) in
   * the activeArray, get the index by calling the hash function, use the collisionHandler
   * probe method to get the new index (it may be different from the original hash if
   * a collision occurred). Enter the key in the new keyArray, value in the new valueArray,
   * and set the new activeArray to true. Finally, assign the current keyArray, valueArray,
   * and activeArray to the new arrays.
   */
  private void resizeArray() {
   //TODO: Implement this method

      int newCapacity = capacity*2;
      boolean[] newActiveArray = new boolean[newCapacity];
      K[] newKeyArray = (K[]) new Object[newCapacity];  //stores all keys 
      V[] newValueArray = (V[]) new Object[newCapacity];  // stores all values
      
      for (int i = 0; i < capacity; i++){

        if (isActiveArray[i]){

          int index = getHashOfKey(keyArray[i]);
          //index = collisionHandler.probe(index, isActiveArray, capacity);
          
          newKeyArray[index] = keyArray[i];
          newValueArray[index] = valueArray[i];
          newActiveArray[index] = true;
        }

      }

      keyArray = newKeyArray;
      valueArray = newValueArray;
      isActiveArray = newActiveArray;
      capacity = newCapacity;

  }

  /**
  * Calculates and returns the load factor of the hash table.
  * This is the ratio of the number of elements stored in the hashtable
  * divided by the total capacity of the hashtable.
  */
  private double calcLoadFactor() {
   //TODO: Implement this method

      return getSize()/keyArray.length;
  } 

  /**
  * This method ensures that the input number maps to a valid array index.
  */  
  private int getBoundedHash(int input) {
    return Math.abs(input) % this.capacity;
  }

  /**
  * This method uses the hashCode() method to get the standard hash of the given key.
  */
  private int getHashOfKey(K key) {
    int index = getBoundedHash(key.hashCode());
    return index;
  }

  /** 
   * Insert the provided key value pair into the hashtable. If the key exists in 
   * the table, the stored value is overwritten with the new value passed in.
   * Please use the provided getHash method to get the hash for the array index.
   * Steps: check is current load is > loadFactor. If true, call resizeArray.
   * Then see if key is in the table by calling getIndex. If key exists, overwrite value
   * in the valueArray. 
   * Then call resolveCollision to see if a collision occurs and if so, that methos will use the 
   * collision handler to resolve it. Then enter the key in the keyArray using the index returned from 
   * resolveCollision, enter the value in the valueArray, and set the isActiveArray to true. Finally,
   * increment the count.
   */
  public void put(K key, V value) {
      //TODO: Implement this method.

      if (key == null) throw new IllegalArgumentException();
      if (calcLoadFactor() > loadFactor) resizeArray();

      int index = getHashOfKey(key);
      index = collisionHandler.probe(index, isActiveArray, capacity);


      if (index == -1) throw new IllegalStateException("Full table");
      
      else if(keyArray[index] == key){
        valueArray[index] = value;
      }

      else{
        isActiveArray[index] = true;
        keyArray[index] = key;
        valueArray[index] = value;
      }
  }

  /**
   * Returns the value associated with the key if it exists in the hashtable.
   * Get the index by calling the hash function, then check if key exists by calling getIndex.
   * Return the value in the valueArray if key is in the table.
   * Returns null if the key is not in the table.
   */
  public V getValue(K target) {
   //TODO: Implement this method

      int index = getHashOfKey(target);

      if (isActiveArray[index]) return valueArray[index];
      return null;
  }

  /**
   * Removes the key-value pair specified by the targetKey from the hashtable and returns the value. 
   * The method gets the hash on the targetKey, then checks if the key is in the table by calling getIndex.
   * Then, if targetKey is in the table, get the value associated with the targetKey from the valueArray,
   * then set the activeArray to false as this cell is now open, decrement the count, and return the value.
   * 
   * @throws ElementNotFoundException if the target does not exist in the table.
   */
  public V remove(K targetKey)throws ElementNotFoundException {
   //TODO: Implement this method

      if (targetKey == null) throw new IllegalArgumentException();

      int startIndex = getHashOfKey(targetKey);

      int index = collisionHandler.search(startIndex, targetKey, keyArray, isActiveArray, capacity);
      V removed = valueArray[index];
      isActiveArray[index] = false;


      return removed;
  }

  /**
   *  Returns a KeyIterator for the keys in this HashTable. 
   */
  public Iterator<K> keyIterator(){
       return new KeyIterator<K>(keyArray, count);
  }

    /******** methods used for testing purposes ***********/
    public int getCapacity(){
      return capacity;
    }

    public double getLoadFactor(){
      return loadFactor;
    }
  
    public K[] getKeyArray(){
       return keyArray;
    }  
  
    public V[] getValueArray(){
      return valueArray;
   }

   public boolean[] getIsActiveArray(){
     return isActiveArray;
   }

   public CollisionHandler <K>  getCollisionHandler(){
     return collisionHandler;
   }
  /************************************************************/

   /**  
    * Use this code for testing your ArrayHashTable. 
    * Comment out statements as needed. Use the debugger to see
    * what your code is doing by stepping in, out, and over methods
    * and statements, and checking the variables pane to see the state 
    * of your table and its variables.
    */
  public static void main(String[] args) throws Exception {
      // Init the type of collision handler: linear or quadratic.
    // CollisionHandler <String> collisionHdler = new LinearCollisionHandler<>();
      CollisionHandler <String> collisionHdler = new QuadraticCollisionHandler<>();
     ArrayHashTable<String, String> table = new ArrayHashTable<String, String> (collisionHdler);
     table.put("fatimeh", "ef4#B%k");
     // test the get method after the put method was called:
     System.out.println("test get after put: "+table.getValue("fatimeh"));
     table.put("matt", "A9d%&b");
     table.put("fadhil", "2h*k9s");
     table.put("rumeng", "j8*shX2");
    // this next call should cause a resize- uncomment when the table works on the above data:
     table.put("harper", "m8Ut6%#a");
    // test the keyIterator
     Iterator<String> keyIter = table.keyIterator();
     while(keyIter.hasNext()){
       String curKey = keyIter.next();
       System.out.print(curKey+ " ");
       System.out.println(table.getValue(curKey)+", ");
     }
    // test removing 
    System.out.println(table.getValue("matt"));
    table.remove("matt");
    System.out.println("test remove: "+table.getValue("matt"));
  }
}

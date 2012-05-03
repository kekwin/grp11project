package dk.itu.grp11.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Dynamic array structure. This is implemented to speed up the system.
 * This is to avoid using ArrayList. The datastructure can contain any type
 * but does not guarantee order.
 * 
 * @author Group 11
 *
 * @param <T> The type that the datastructure can contain
 */
public class DynArray<T> implements Iterable<T>  {
  private T[] data; //Containing the data of the dynamic array
  private int size = 0; //The size of the array containing the data
  
  //This is used to create an instance of the generic array.
  //It also makes it possible to export using copyOf returning the correct type
  //If not an array of type Object is returned, and problems occur casting it correctly
  private Class<T[]> TClass; 
  
  /**
   * Constructor of Dynamic Array class
   * 
   * @param GenericClass The class of the generic type that the array can contain
   */
  public DynArray(Class<T[]> GenericClass)
  {
    this.TClass = GenericClass;
    //Create an array of the generic type with 1 space for an element
    data = TClass.cast(Array.newInstance(TClass.getComponentType(), 1));
  }
  
  @SuppressWarnings("unchecked")
  public DynArray()
  {
    this.TClass = null;
    //Create an array of the generic type with 1 space for an element
    data = (T[])new Object[1];
  }
  
  @SuppressWarnings("unchecked")
  private T[] createArray(int size) {
    if (TClass == null) return (T[])new Object[size];
    else return TClass.cast(Array.newInstance(TClass.getComponentType(), size));
  }
  
  /**
   * Add an element of generic type to the array
   * 
   * @param input the element to add
   */
  public void add(T input)
  {
    //Check if there is space for more elements. If not the array should be rebuilt
    if (size == data.length) rebuild(size*2);
    data[size++] = input;
  }
  
  /**
   * Private function for rebuilding the array when it runs out of spaces
   * for new elements
   * 
   * @param newsize The new size of the array
   */
  private void rebuild(int newsize)
  {
    //Create an array with the new size of the generic type
    T[] tmp = createArray(newsize);
    for (int i = 0; i < size; i++) tmp[i] = data[i];
    data = tmp;
    tmp = null;
  }
  
  /**
   * Swaps two values in the data array
   * 
   * @param key1 The first value to swap
   * @param key2 The second value to swap
   */
  private void swap(int key1, int key2)
  {
    T tmp = data[key2];
    data[key2] = data[key1];
    data[key1] = tmp;
    tmp = null;
  }
  
  /**
   * Removes an element at position defined by key
   * 
   * @param key The position
   * @return The removed element
   */
  public T remove(int key)
  {
    swap(key, size-1);
    T tmpT = data[size-1];
    data[size-1] = null;
    size--;
    if (size == data.length/4) rebuild(size/2);
    return tmpT;
  }
  
  /**
   * Method for getting element at position defined by key
   * 
   * @param key The position
   * @return The element at the position
   */
  public T get(int key)
  {
    return data[key];
  }
  
  /**
   * Returns the size of the array
   * 
   * @return The size
   */
  public int length()
  {
    return size;
  }
  
  /**
   * Returns an array that contains all the elements of the datastructure
   * 
   * @return The array containing the elements
   */
  public T[] toArray()
  {
    return Arrays.copyOf(data, size);
  }
  
  /**
   * Returns the iterator to enable users to iterate the elements
   */
  public Iterator<T> iterator(){ //return an iterator over the items
      return new DynArrayIterator();
  }
  
  /**
   * Iterator as inner class
   * @author Group 11
   *
   */
  private class DynArrayIterator implements Iterator<T>
  {
    int pos = 0;
    public DynArrayIterator(){}
    public boolean hasNext() { return pos < size; }
    public T next() { return data[pos++]; }
    public void remove() { /* Not supported */ }
  }
}
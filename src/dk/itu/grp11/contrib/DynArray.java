package dk.itu.grp11.contrib;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

public class DynArray<T> implements Iterable<T>  {
  private T[] data;
  private int size = 0;
  private Class<T[]> TClass;
  public DynArray(Class<T[]> GenericClass)
  {
    this.TClass = GenericClass;
    data = TClass.cast(Array.newInstance(TClass.getComponentType(), 1));
  }
  public void add(T input)
  {
    if (size == data.length) rebuild(size*2);
    data[size++] = input;
  }
  private void rebuild(int newsize)
  {
    T[] tmp = TClass.cast(Array.newInstance(TClass.getComponentType(), newsize));
    for (int i = 0; i < size; i++) tmp[i] = data[i];
    data = tmp;
    tmp = null;
  }
  private void swap(int key1, int key2)
  {
    T tmp = data[key2];
    data[key2] = data[key1];
    data[key1] = tmp;
    tmp = null;
  }
  public T remove(int key)
  {
    swap(key, size-1);
    T tmpT = data[size-1];
    data[size-1] = null;
    size--;
    if (size == data.length/4) rebuild(size/2);
    return tmpT;
  }
  public T get(int key)
  {
    return data[key];
  }
  public int length()
  {
    return size;
  }
  public T[] toArray()
  {
    return Arrays.copyOf(data, size);
  }
  public Iterator<T> iterator(){ //return an iterator over the items
      return new DynArrayIterator();
  }
  private class DynArrayIterator implements Iterator<T>
  {
    int pos = 0;
    public DynArrayIterator(){}
    public boolean hasNext() { return pos < size; }
    public T next() { return data[pos++]; }
    public void remove() { /* Not supported */ }
  }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Core.Structs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego Catalano
 * @param <E>
 */
public class BinaryHeap<E extends Comparable<E>> {
    
    private int count = 0;
    List<E> h = new ArrayList<E>();

    public int count() {
        return count;
    }
    
    public int size(){
        return h.size();
    }

  public BinaryHeap() {
  }

  public BinaryHeap(E[] keys) {
    for (E key : keys) {
      h.add(key);
    }
    for (int k = h.size() / 2 - 1; k >= 0; k--) {
      downHeap(k, h.get(k));
    }
  }

  public void add(E node) {
    h.add(null);
    int k = h.size() - 1;
    upHeap(k, node);
    count++;
  }

  public E remove() {
    E removedNode = h.get(0);
    E lastNode = h.remove(h.size() - 1);
    downHeap(0, lastNode);
    count--;
    return removedNode;
  }
  
  public void remove(E item){
      h.remove(item);
  }

  public E min() {
    return h.get(0);
  }

  public boolean isEmpty() {
    return h.isEmpty();
  }
  
  private void upHeap(int k, E node){
    while (k > 0) {
      int parent = (k - 1) / 2;
      E p = h.get(parent);
      if (node.compareTo(p) >= 0) {
        break;
      }
      h.set(k, p);
      k = parent;
    }
    h.set(k, node);
  }

  private void downHeap(int k, E node) {
    if (h.isEmpty()) {
      return;
    }
    while (k < h.size() / 2) {
      int child = 2 * k + 1;
      if (child < h.size() - 1 && h.get(child).compareTo(h.get(child + 1)) > 0) {
        child++;
      }
      if (node.compareTo(h.get(child)) < 0) {
        break;
      }
      h.set(k, h.get(child));
      k = child;
    }
    h.set(k, node);
  }
}
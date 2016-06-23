// Catalano Core Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Core.Structs;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary Heap.
 * @author Diego Catalano
 * @param <E> Item.
 */
public class BinaryHeap<E extends Comparable<E>> {
    
    private int count = 0;
    List<E> heap = new ArrayList<E>();

    /**
     * Get the count actually in the heap.
     * @return Count.
     */
    public int count() {
        return count;
    }
    
    /**
     * Get the size of the heap.
     * @return Size.
     */
    public int size(){
        return heap.size();
    }

    /**
     * Initializes a new instance of the BinaryHeap class.
     */
    public BinaryHeap() {}

    /**
     * Initializes a new instance of the BinaryHeap class.
     * @param keys Items.
     */
    public BinaryHeap(E[] keys) {
      for (E key : keys) {
        heap.add(key);
      }
      for (int k = heap.size() / 2 - 1; k >= 0; k--) {
        downHeap(k, heap.get(k));
      }
    }

    /**
     * Adds an item in the heap.
     * @param node Item as node.
     */
    public void add(E node) {
      heap.add(null);
      int k = heap.size() - 1;
      upHeap(k, node);
      count++;
    }

    /**
     * Remove the last node from the heap.
     * @return Item from the last node.
     */
    public E remove() {
      E removedNode = heap.get(0);
      E lastNode = heap.remove(heap.size() - 1);
      downHeap(0, lastNode);
      count--;
      return removedNode;
    }
  
    /**
     * Remove a specified item from the heap.
     * @param item Item.
     */
    public void remove(E item){
        heap.remove(item);
    }

    /**
     * Get the minimum item from the heap.
     * @return Item.
     */
    public E min() {
      return heap.get(0);
    }

    /**
     * Check if the heap is empty.
     * @return True if the heap is empty, otherwise false.
     */
    public boolean isEmpty() {
      return heap.isEmpty();
    }
  
    private void upHeap(int k, E node){
      while (k > 0) {
        int parent = (k - 1) / 2;
        E p = heap.get(parent);
        if (node.compareTo(p) >= 0) {
          break;
        }
        heap.set(k, p);
        k = parent;
      }
      heap.set(k, node);
    }

    private void downHeap(int k, E node) {
      if (heap.isEmpty()) {
        return;
      }
      while (k < heap.size() / 2) {
        int child = 2 * k + 1;
        if (child < heap.size() - 1 && heap.get(child).compareTo(heap.get(child + 1)) > 0) {
          child++;
        }
        if (node.compareTo(heap.get(child)) < 0) {
          break;
        }
        heap.set(k, heap.get(child));
        k = child;
      }
      heap.set(k, node);
    }
}
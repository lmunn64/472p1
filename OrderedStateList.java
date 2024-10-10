package edu.iastate.cs472.proj1;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  
 * @author
 * Luke Munn
 */

/**
 * This class describes a circular doubly-linked list of states to represent both the OPEN and CLOSED lists
 * used by the A* algorithm.  The states on the list are sorted in the  
 * 
 *     a) order of non-decreasing cost estimate for the state if the list is OPEN, or 
 *     b) lexicographic order of the state if the list is CLOSED.  
 * 
 */
public class OrderedStateList 
{

	/**
	 * Implementation of a circular doubly-linked list with a dummy head node.
	 */
	  private State head;           // dummy node as the head of the sorted linked list 
	  private int size = 0;
	  
	  private boolean isOPEN;       // true if this OrderedStateList object is the list OPEN and false 
	                                // if the list CLOSED.

	  /**
	   *  Default constructor constructs an empty list. Initialize heuristic. Set the fields next and 
	   *  previous of head to the node itself. Initialize instance variables size and heuristic. 
	   * 
	   * @param h 
	   * @param isOpen   
	   */
	  public OrderedStateList(Heuristic h, boolean isOpen)
	  {
		State.heu = h;   // initialize heuristic used for evaluating all State objects.
		int[][] empty = {{0,0,0},{0,0,0},{0,0,0}};
		head = new State(empty);
		isOPEN = isOpen;
		head.previous = head;
		head.next = head;
		size = 1;
	  }

	  
	  public int size()
	  {
		  return size; 
	  }
	  
	  
	  /**
	   * A new state is added to the sorted list.  Traverse the list starting at head.  Stop 
	   * right before the first state t such that compareStates(s, t) <= 0, and add s before t.  
	   * If no such state exists, simply add s to the end of the list. 
	   * 
	   * Precondition: s does not appear on the sorted list. 
	   * 
	   * @param s
	   */
	  public void addState(State s)
	  {
		State f;
		f = head;
		while(f.next != head){
			if(compareStates(s, f.next) <= 0){	
				f.next.previous = s;
				s.previous = f;
				s.next = f.next;
				f.next = s;
				size++;
				return;
			}
			f = f.next;
		}  
			// No such state <= 0
			s.next = head;
			s.previous = f;
			f.next = s;
			head.previous = s;
			size++;
				
	  }
	  
	  
	  /**
	   * Conduct a sequential search on the list for a state that has the same board configuration 
	   * as the argument state s.  
	   * 
	   * Calls equals() from the State class. 
	   * 
	   * @param s
	   * @return the state on the list if found
	   *         null if not found 
	   */
	  public State findState(State s)
	  {
		State f = head;
		while(f.next != head){
			if(f.next.equals(s)){
				return f.next;
			}
			f = f.next;
		}  
		  return null; 
	  }
	  
	  
	  /**
	   * Remove the argument state s from the list.  It is used by the A* algorithm in maintaining 
	   * both the OPEN and CLOSED lists. 
	   * 
	   * @param s
	   * @throws IllegalStateException if s is not on the list 
	   */
	  public void removeState(State s) throws IllegalStateException
	  {
		State f;
		f = head;
		while(f.next != head){
			if(f.next.equals(s)){
				State temp = f.next.clone();
				f.next = temp.next;
				temp.next.previous = f;
				size--;
			}
		}  
		throw new IllegalStateException("ORDERED_STATE_LIST: removeState(State s) No state s in list");
	  }
	  
	  
	  /**
	   * Remove the first state on the list and return it.  This is used by the A* algorithm in maintaining
	   * the OPEN list. 
	   * 
	   * @return  
	   */
	  public State remove()
	  {
		  State returnState = head.next;
		  head.next = returnState.next;
		  returnState.next.previous = head;
		  size--;
		  return returnState; 
	  }
	  
	  
	  /**
	   * Compare two states depending on whether this OrderedStateList object is the list OPEN 
	   * or the list CLOSE used by the A* algorithm.  More specifically,  
	   * 
	   *     a) call the method compareTo() of the State if isOPEN == true, or 
	   *     b) create a StateComparator object to call its compare() method if isOPEN == false. 
	   * 
	   * @param s1
	   * @param s2
	   * @return -1 if s1 is less than s2 as determined by the corresponding comparison method
	   *         0  if they are equal 
	   *         1  if s1 is greater than s2
	   */
	  private int compareStates(State s1, State s2)
	  {
		  if(isOPEN){
			return s1.compareTo(s2);
		  }
		  StateComparator s = new StateComparator();
		  return s.compare(s1, s2); 
	  }
}

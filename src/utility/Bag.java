package utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Bag<T extends Comparable<T>> implements Iterable<T>, Comparable<Bag<T>>{

	private Map<T, Integer> mapping = new HashMap<T, Integer>();
	private int size;
	
	public Bag(){
		size = 0;
	}

	@Override
	public Iterator<T> iterator() {
		return mapping.keySet().iterator();
	}

	public Set<T> contentsAsSet(){
		return this.mapping.keySet();
	}
	
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean contains(T key) {
		return mapping.containsKey(key);
	}
	
	public void add(T t) {
		if (mapping.containsKey(t)){
			mapping.put(t, 1 + mapping.get(t));
		} else {
			mapping.put(t, 1);
		}
		size++;
	}

	public void remove(T t) {
		mapping.remove(t);
	}

	public void clear(){
		mapping.clear();
		size = 0;
	}
	
	public boolean equals(Object o){
		if (!(o instanceof Bag)){
			return false;
		}
		Bag<T> b = (Bag<T>) o;
		return equals(b);
	}
	
	public boolean equals(Bag<T> other){
		return this.compareTo(other) == 0;
	}
	
	public int compareTo(Bag<T> other){
		Set<T> toCompare = new HashSet<T>();
		toCompare.addAll(mapping.keySet());
		toCompare.addAll(other.mapping.keySet());
		List<T> listToCompare = new ArrayList<T>(toCompare);
		Collections.sort(listToCompare);
		for (T t : listToCompare){
			if (!other.mapping.containsKey(t)){
				return 1;
			} else if (!this.mapping.containsKey(t)){
				return -1;
			}
			int thisVal = mapping.get(t);
			int otherVal = other.mapping.get(t);
			if (thisVal > otherVal){
				return 1;
			} else if (thisVal < otherVal){
				return -1;
			}
		}
		return 0;
	}
	
	public void addAll(Collection<T> collection){
		for (T t : collection){
			add(t);
		}
	}
	
	public String toString(){
		return mapping.toString();
	}
}

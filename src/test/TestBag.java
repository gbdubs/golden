package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import utility.Bag;

public class TestBag {

	@Test
	public void testSetProperties(){
		
		Bag<Integer> bag = new Bag<Integer>();
		Set<Integer> set = new HashSet<Integer>();
		
		int n = 10000;
		
		for(int i = 0; i < n; i++){
			int rand = (int) (Math.random() * n);
			bag.add(rand);
			set.add(rand);
		}
		
		Set<Integer> bagSet = bag.contentsAsSet();
		
		Set<Integer> equalityTest = new HashSet<Integer>();
		
		equalityTest.addAll(bagSet);
		equalityTest.addAll(set);
		
		assertEquals(equalityTest, bagSet);
		assertEquals(equalityTest, set);
	}
	
	@Test
	public void testOrderInvariance(){
		
		List<Integer> list = new ArrayList<Integer>();
		
		Bag<Integer> bag1 = new Bag<Integer>();
		Bag<Integer> bag2 = new Bag<Integer>();
		
		int n = 10000;
		int max = 1000;
		
		for(int i = 0; i < n; i++){
			list.add((int)(Math.random() * max));
		}
		
		Collections.shuffle(list);
		
		bag1.addAll(list);
		
		Collections.shuffle(list);
		
		bag2.addAll(list);
		
		assertEquals(bag1, bag2);
	}
}

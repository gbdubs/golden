package test;

import static org.junit.Assert.*;
import graph.Graph;
import graph.Vertex;
import hurdles.MayTenthErrors;

import org.junit.Test;

import random.RandomGraphGenerator;
import topo.TopologicalSort;

public class TestTopoSortConsistency {

	@Test
	public void testRandomGraphConsistent(){
		int v = 10;
		int e = 30;
		int nTrials = 100;
		
		Graph g = RandomGraphGenerator.generateRandomConnectedGraph(v, e);
		
		Vertex meekest = g.getMeekestNode();
		
		TopologicalSort original = new TopologicalSort(meekest);
		
		for (int i = 0; i < nTrials; i++) {
			System.out.println("Trial " + i  + " / " + nTrials);
			TopologicalSort duplicate = new TopologicalSort(meekest);
			boolean equivalent = original.rankedEquals(duplicate);
			assertTrue(equivalent);
		}
	}
	
	@Test
	public void testMalignantConsistent(){
		
		Graph g = MayTenthErrors.getTenExample();
		int nTrials = 1000;
		
		for (Vertex meekest : g.getMeekestNodes()){
			
			if (meekest.toString().equals("VERTEX Q") || meekest.toString().equals("VERTEX K")){
				System.out.println(meekest.toString());
			}
			TopologicalSort original = new TopologicalSort(meekest);
			
			for (int i = 0; i < nTrials; i++) {
				System.out.println("Trial " + i  + " / " + nTrials);
				TopologicalSort duplicate = new TopologicalSort(meekest);
				boolean equivalent = original.rankedEquals(duplicate);
				assertTrue(equivalent);
			}
	
		}
	}
	
}

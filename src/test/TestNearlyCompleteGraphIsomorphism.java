package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import graph.Graph;
import graph.Vertex;

import java.util.Map;

import org.junit.Test;

import random.RandomGraphGenerator;
import solver.IsomorphismSolver;

public class TestNearlyCompleteGraphIsomorphism {

	@Test
	public void testFindsLargeIsomorphism(){
		int nVerticies = 100;
		int maxEdges = 100 * 99 / 2;
		int maxTakeaway = maxEdges / 10;
		int nTrials = 100;
		
		int trial = 0;
		
		while (trial++ < nTrials){
			int nEdges = maxEdges - (int) (Math.random() * maxTakeaway);
			System.out.println("NEARLY FULL TRIAL NUMBER " + trial + " V = " + nVerticies + ", E = " + nEdges);
			Graph g = RandomGraphGenerator.generateRandomConnectedGraph(nVerticies, nEdges);
			Graph h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);
			Map<Vertex, Vertex> iso = IsomorphismSolver.findIsomorphism(g, h);
			assertNotNull(iso);
			boolean success = IsomorphismSolver.checkIsomorphism(g, h, iso);
			assertTrue(success);
		}
	}
	
}

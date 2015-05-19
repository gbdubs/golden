package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import graph.Graph;
import graph.Vertex;
import graphviz.GraphMaker;

import java.util.Map;

import org.junit.Test;

import random.RandomGraphGenerator;
import solver.IsomorphismSolver;
import topo.TopologicalSort;

public class TestIsomorphism {

	@Test
	public void testFindsSmallIsomorphism(){
		int nVerticies = 7;
		int nEdges = 10;
		int nTrials = 10000;
		
		int trial = 0;
		
		while (trial++ < nTrials){
			System.out.println("SMALL TRIAL NUMBER " + trial);
			Graph g = RandomGraphGenerator.generateRandomConnectedGraph(nVerticies, nEdges);
			Graph h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);
			Map<Vertex, Vertex> iso = IsomorphismSolver.findIsomorphism(g, h);
			if (iso == null){
				showAllPossbileCombinations(g, h);
				IsomorphismSolver.findIsomorphism(g, h);
			}
			assertNotNull(iso);
			boolean success = IsomorphismSolver.checkIsomorphism(g, h, iso);
			assertTrue(success);
		}
	}
	
	@Test
	public void testFindsMediumIsomorphism(){
		int nVerticies = 50;
		int nEdges = 80;
		int nTrials = 1000;
		
		int trial = 0;
		
		while (trial++ < nTrials){
			System.out.println("MEDIUM TRIAL NUMBER " + trial);
			Graph g = RandomGraphGenerator.generateRandomConnectedGraph(nVerticies, nEdges);
			Graph h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);
			Map<Vertex, Vertex> iso = IsomorphismSolver.findIsomorphism(g, h);
			if (iso == null){
				showAllPossbileCombinations(g, h);
				IsomorphismSolver.findIsomorphism(g, h);
			}
			assertNotNull(iso);
			boolean success = IsomorphismSolver.checkIsomorphism(g, h, iso);
			assertTrue(success);
		}
	}
	
	@Test
	public void testFindsLargeIsomorphism(){
		int nVerticies = 200;
		int nEdges = 500;
		int nTrials = 100;
		
		int trial = 0;
		
		while (trial++ < nTrials){
			System.out.println("LARGE TRIAL NUMBER " + trial);
			Graph g = RandomGraphGenerator.generateRandomConnectedGraph(nVerticies, nEdges);
			Graph h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);
			Map<Vertex, Vertex> iso = IsomorphismSolver.findIsomorphism(g, h);
			if (iso == null){
				showAllPossbileCombinations(g, h);
				IsomorphismSolver.findIsomorphism(g, h);
			}
			assertNotNull(iso);
			boolean success = IsomorphismSolver.checkIsomorphism(g, h, iso);
			assertTrue(success);
		}
	}
	
	@Test
	public void testFindRandomSizeIsoMorphsim(){
		int minVerticies = 5;
		int maxVerticies = 200;
		
		int nTrials = 100;
		
		int trial = 0;
		
		while (trial++ < nTrials){
			int nVerticies = (int) (Math.random() * (maxVerticies - minVerticies) + minVerticies);
			int minEdges = nVerticies - 1;
			int maxEdges = (nVerticies * (nVerticies - 1)) / 2;
			int nEdges = (int) (Math.random() * (maxEdges - minEdges) + minEdges);
			
			System.out.println("RANDOM TRIAL NUMBER " + trial + " V = " + nVerticies + ", E = " + nEdges);
			Graph g = RandomGraphGenerator.generateRandomConnectedGraph(nVerticies, nEdges);
			Graph h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);
			Map<Vertex, Vertex> iso = IsomorphismSolver.findIsomorphism(g, h);
			if (iso == null){
				showAllPossbileCombinations(g, h);
				IsomorphismSolver.findIsomorphism(g, h);
			}
			assertNotNull(iso);
			boolean success = IsomorphismSolver.checkIsomorphism(g, h, iso);
			assertTrue(success);
		}
	}
	
	public void showAllPossbileCombinations(Graph g, Graph h){
		TopologicalSort ts = new TopologicalSort(g.getVertices().iterator().next());
		GraphMaker.print(ts, "BULK Target Sort");
		int i = 0;
		for (Vertex v : h.getVertices()){
			GraphMaker.print(new TopologicalSort(v), "BULK Possibility " + i++);
		}
	}
}

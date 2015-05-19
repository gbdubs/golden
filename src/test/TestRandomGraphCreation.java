package test;

import static org.junit.Assert.*;

import java.util.Map;

import graph.Graph;
import graph.Vertex;

import org.junit.Test;

import random.RandomGraphGenerator;
import solver.IsomorphismSolver;

public class TestRandomGraphCreation {

	
	@Test
	public void testCorrectNumberOfEdgesAndNodes(){
		for (int i = 0; i < 10; i++){
			System.out.println("Correct Number of Edges and Nodes, Test " + i);
			int nVertices = (int) (Math.random() * 500) + 2;
			int nEdges = (int) (Math.random() * ((nVertices) * (nVertices-1) / 2 - nVertices + 1)) + nVertices - 1;
			Graph g = RandomGraphGenerator.generateRandomConnectedGraph(nVertices, nEdges);
			assertEquals(nVertices, g.nVerticies());
			assertEquals(nEdges, g.nEdges());
		}
	}
	
	@Test
	public void testFullyConnectedAndRightNumbers(){
		for (int i = 0; i < 100; i++){
			
			int nVertices = (int) (Math.random() * 50) + 2;
			int nEdges = (int) (Math.random() * ((nVertices) * (nVertices-1) / 2 - nVertices + 1)) + nVertices - 1;
			Graph g = RandomGraphGenerator.generateRandomConnectedGraph(nVertices, nEdges);
		
			assertEquals(nVertices, g.nVerticies());
			assertEquals(nEdges, g.nEdges());
			
			for (Vertex v : g.getVertices()){
				int relatives = v.numberOfRelativesOfDistance(g.nVerticies());
				int expected = g.nVerticies();
				assertEquals(expected, relatives);
			}
		}
	}
	
	@Test
	public void testSmallSizeGraphps(){
		int nVertices = 7;
		int nEdges = 10;
		int nTrials = 10000;
		
		for (int i = 0; i < nTrials; i++){
			
			Graph g = RandomGraphGenerator.generateRandomConnectedGraph(nVertices, nEdges);
		
			assertEquals(nVertices, g.nVerticies());
			assertEquals(nEdges, g.nEdges());
			
			for (Vertex v : g.getVertices()){
				int relatives = v.numberOfRelativesOfDistance(g.nVerticies());
				int expected = g.nVerticies();
				assertEquals(expected, relatives);
			}
		}
	}
}

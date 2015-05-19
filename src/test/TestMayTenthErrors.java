package test;

import static org.junit.Assert.*;
import graph.Graph;
import hurdles.MayTenthErrors;

import org.junit.Test;

import random.RandomGraphGenerator;
import solver.IsomorphismSolver;

public class TestMayTenthErrors {

	@Test
	public void testTenCase(){
		int runs = 0;
		int goalRuns = 1000;
		
		Graph g = null;
		Graph h = MayTenthErrors.getTenExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testEighteenCase(){
		int runs = 0;
		int goalRuns = 1000;
		
		Graph g = null;
		Graph h = MayTenthErrors.getEighteenExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testTwentyThreeCase(){
		int runs = 0;
		int goalRuns = 1000;
		
		Graph g = null;
		Graph h = MayTenthErrors.getTwentyThreeExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testTwentySixCase(){
		int runs = 0;
		int goalRuns = 1000;
		
		Graph g = null;
		Graph h = MayTenthErrors.getTwentySixExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testTwentyEightCase(){
		int runs = 0;
		int goalRuns = 1000;
		
		Graph g = null;
		Graph h = MayTenthErrors.getTwentyEightExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testThirtyOneCase(){
		int runs = 0;
		int goalRuns = 800;
		
		Graph g = null;
		Graph h = MayTenthErrors.getThirtyOneExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testFortyEightCase(){
		int runs = 0;
		int goalRuns = 500;
		
		Graph g = null;
		Graph h = MayTenthErrors.getFortyEightExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testSeventySevenCase(){
		int runs = 0;
		int goalRuns = 300;
		
		Graph g = null;
		Graph h = MayTenthErrors.getSeventySevenExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testEightySevenCase(){
		int runs = 0;
		int goalRuns = 250;
		
		Graph g = null;
		Graph h = MayTenthErrors.getEightySevenExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testOneThirtyOneCase(){
		int runs = 0;
		int goalRuns = 150;
		
		Graph g = null;
		Graph h = MayTenthErrors.getOneThirtyOneExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testOneSixtyFiveCase(){
		int runs = 0;
		int goalRuns = 150;
		
		Graph g = null;
		Graph h = MayTenthErrors.getOneSixtyFiveExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testOneSeventyCase(){
		int runs = 0;
		int goalRuns = 50;
		
		Graph g = null;
		Graph h = MayTenthErrors.getOneSeventyExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
	
	@Test
	public void testTwoTwentyCase(){
		int runs = 0;
		int goalRuns = 100;
		
		Graph g = null;
		Graph h = MayTenthErrors.getTwoTwentyExample();
		int nVerticies = h.getVertices().size();
		while (runs++ < goalRuns){
			g = h;
			h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);	
			assertNotNull(IsomorphismSolver.findIsomorphism(g, h));
			System.out.println("V="+nVerticies+" Run " + runs + "/" + goalRuns);
		}
	}
}

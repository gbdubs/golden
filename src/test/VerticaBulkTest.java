package test;

import graph.Graph;
import graph.Vertex;
import graphviz.GraphMaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import random.RandomGraphGenerator;
import solver.IsomorphismSolver;

public class VerticaBulkTest {

	public static void main(String[] args){
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d h:mm aa");
		String today = sdf.format(new Date());
		
		new File("Batch " + today).mkdir();
		
		File f = new File("Batch " + today + "/results.txt");
		PrintWriter pw;
		try {
			pw = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			return;
		}
		
		int nTrials = 100000;
		
		int minVerticiesSqrt = 2;
		int maxVerticiesSqrt = 15;
		
		int trial = 0;
		
		int failures = 0;
		
		long startedAt = System.currentTimeMillis();
		while (trial++ < nTrials){
			int nVerticies = (int) Math.pow((Math.random() * (maxVerticiesSqrt - minVerticiesSqrt) + minVerticiesSqrt), 2);
			
			int minEdges = nVerticies -1;
			int maxEdges = ((nVerticies - 1) * nVerticies ) / 2;
			
			int nEdges = (int) (Math.random() * (maxEdges - minEdges) + minEdges);
			
			System.out.println("Failures = " + failures + " RANDOM TRIAL NUMBER " + trial + " V = " + nVerticies + ", E = " + nEdges);

			Graph g = RandomGraphGenerator.generateRandomConnectedGraph(nVerticies, nEdges);
			Graph h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);
			long l = System.currentTimeMillis();
			Map<Vertex, Vertex> iso = IsomorphismSolver.findIsomorphism(g, h);
			int time = (int) (System.currentTimeMillis() - l);
			boolean success = iso != null && IsomorphismSolver.checkIsomorphism(g, h, iso);
			
			if (success){
				pw.println(nVerticies + ", " + nEdges + ", " + time + ", " + success);
			} else {
				failures++;
				GraphMaker.print(g, "Batch " + today + "/n" + nVerticies + " e" + nEdges + ".txt");
			}
			
			if (trial % 10 == 0){
				double timeSince = (double) (System.currentTimeMillis() - startedAt);
				double timePer = timeSince / trial;
				long remaining = (long) (timePer * (nTrials - trial));
				String estimatedCompletion = sdf.format(new Date(System.currentTimeMillis() + remaining));
				System.out.println("--- Estimated Completion Time: "+ estimatedCompletion + " ---");
			}
		}
		pw.close();
	}
	
}

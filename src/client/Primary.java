package client;

import graph.Graph;
import graph.Vertex;
import graphviz.GraphMaker;
import random.RandomGraphGenerator;
import topo.TopologicalSort;

public class Primary {

	public static void main(String[] args){
		
	
		Graph g = RandomGraphGenerator.generateRandomConnectedGraph(20, 25);
		
		
		TopologicalSort gts = new TopologicalSort(g.getVertices().iterator().next());
	
		GraphMaker.print(gts, "TopologicalSortG");
		
		int i = 0;
		Graph h = RandomGraphGenerator.generateRandomConnectedIsomorphism(g);
		for(Vertex v : h.getVertices()){
			System.out.println("NOW PROCESSING " + i++);
			TopologicalSort hts = new TopologicalSort(v);
			if (!hts.fullyRanked){
				System.out.println("FOUND ONE!");
				GraphMaker.print(hts, "Abberation");
				TopologicalSort ts2 = new TopologicalSort(v);
				
			}
		}
	}
}

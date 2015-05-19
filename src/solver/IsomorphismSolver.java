package solver;

import graph.Graph;
import graph.Vertex;
import graphviz.GraphMaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import topo.TopoNode;
import topo.TopologicalSort;

public class IsomorphismSolver {

	
	public static Map<Vertex, Vertex> findIsomorphism(Graph g, Graph h){
		
		int edges = g.nEdges();
		int nodes = g.nVerticies();
		
		if (edges != h.nEdges() || nodes != h.nVerticies()){
			return null;
		}
		
		Map<Vertex, Vertex> isomorphism = mapAndRemoveFullyConnectedNodes(new HashMap<Vertex, Vertex>(), g, h);
		
		if (isomorphism == null){
			return null;
		}
		
		Vertex pivot = g.getMeekestNode();

		TopologicalSort pivotalSort = new TopologicalSort(pivot);
		
		for (Vertex v : h.getMeekestNodes()){
			if (v.getNeighbors().size() == pivot.getNeighbors().size()){
				TopologicalSort otherSort = new TopologicalSort(v);
				if (pivotalSort.rankedEquals(otherSort)){
					Map<Integer, TopoNode> gMap = pivotalSort.getAllRankings();
					Map<Integer, TopoNode> hMap = otherSort.getAllRankings();
					Set<Integer> allKeys = new HashSet<Integer>();
					allKeys.addAll(gMap.keySet());
					allKeys.addAll(hMap.keySet());
					
					for (Integer i : allKeys){
						isomorphism.put(gMap.get(i).counterpart, hMap.get(i).counterpart);
					}
					if (isomorphism.size() != nodes){
						System.err.println("THE CARDINALITY OF THE RESULTING ISOMORPHISM WAS NOT THE CARDINALITY OF THE GRAPHS");
						return null;
					}
					return isomorphism;
				}
			}
		}
		return null;
	}
	
	public static boolean checkIsomorphism(Graph g, Graph h, Map<Vertex, Vertex> iso){
		
		List<String> gEdges = new ArrayList<String>();
		List<String> hEdges = new ArrayList<String>();
		
		List<Vertex> hVerticies = new ArrayList<Vertex>(h.getVertices());
		for (int i = 0; i < hVerticies.size(); i++){
			Vertex hB = hVerticies.get(i);
			for (int j = 0; j < i; j++){
				Vertex hA = hVerticies.get(j);
				if (hA.getNeighbors().contains(hB)){
					int a = hA.getId();
					int b = hB.getId();
					hEdges.add(Math.min(a, b) + " -- " + Math.max(a, b));
				}
			}
		}
		
		List<Vertex> gVerticies = new ArrayList<Vertex>(g.getVertices());
		for (int i = 0; i < gVerticies.size(); i++){
			Vertex gB = gVerticies.get(i);
			for (int j = 0; j < i; j++){
				Vertex gA = gVerticies.get(j);
				if (gA.getNeighbors().contains(gB)){
					int a = iso.get(gA).getId();
					int b = iso.get(gB).getId();
					gEdges.add(Math.min(a, b) + " -- " + Math.max(a, b));
				}
			}
		}
		
		Collections.sort(gEdges);
		Collections.sort(hEdges);
		
		Set<String> allEdges = new HashSet<String>();
		
		allEdges.addAll(gEdges);
		allEdges.addAll(hEdges);
		
		if (allEdges.size() == gEdges.size() && allEdges.size() == hEdges.size()){
			return true;
		}
		
		return false;
	}
	
	public static Map<Vertex, Vertex> mapAndRemoveFullyConnectedNodes(Map<Vertex, Vertex> isomorphism, Graph g, Graph h){
		List<Vertex> gTotallyConnected = g.totallyConnected();
		List<Vertex> hTotallyConnected = h.totallyConnected();
		
		if (gTotallyConnected.size() != hTotallyConnected.size()){
			return null;
		} else {
			for(int i = 0; i < gTotallyConnected.size(); i++){
				isomorphism.put(gTotallyConnected.get(i), hTotallyConnected.get(i));
				g.removeVertex(gTotallyConnected.get(i));
				h.removeVertex(hTotallyConnected.get(i));
			}
		}
		return isomorphism;
	}
	
}

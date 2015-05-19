package topo;

import graph.Vertex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utility.Bag;

public class TopoLevel {

	public int strataLevel;
	public TopologicalSort graph;
	public Map<Integer, TopoNode> mapping;
	public List<TopoNode> unboundVerticies;
	public List<TopoNode> verticies;
	public int rankedTo;
	
	public TopoLevel(List<Vertex> ordinary, TopologicalSort graph, int level){
		this.graph = graph;
		this.verticies = new ArrayList<TopoNode>();
		this.mapping = new HashMap<Integer, TopoNode>();
		for(Vertex v : ordinary){
			verticies.add(new TopoNode(this, v));
		}
		this.unboundVerticies = new ArrayList<TopoNode>();
		this.unboundVerticies.addAll(verticies);
		this.strataLevel = level;
		this.rankedTo = 0;
	}

	public void attachStrata(){
		TopoLevel nextStrata = this.graph.strata.get(strataLevel + 1);
		for (TopoNode v : verticies){
			for (TopoNode w : verticies){
				if (w.counterpart.getNeighbors().contains(v.counterpart)){
					v.siblings.add(w);
					w.siblings.add(v);
				}
			}
		}
		if (nextStrata != null){
			for (TopoNode v : verticies){
				for (TopoNode w : nextStrata.verticies){
					if (v.counterpart.getNeighbors().contains(w.counterpart)){
						v.children.add(w);
						w.parents.add(v);
					}
				}
			}
			nextStrata.attachStrata();
		}
	}
	
	public Bag<BigInteger> getStates(){
		Bag<BigInteger> result = new Bag<BigInteger>();
		for(TopoNode tn : verticies){
			result.add(tn.state);
		}
		return result;
	}
	
	public Set<String> getConnections(){
		Set<String> result = new HashSet<String>();
		for (TopoNode a : verticies){
			for (TopoNode b : a.children){
				result.add(a.rank + " -- " + b.rank);
			}
			for (TopoNode b : a.siblings){
				if (a.id.compareTo(b.id) == 1){
					result.add(a.rank + " -- " + b.rank);
				}
			}
		}
		return result;
	}
	
	public String toString(){
		String result = "";
		for(TopoNode v : verticies){
			result += ", " + v.toString();
		}
		result = result.substring(2);
		result = "Strata "+ strataLevel + "{" + result + "}";
		return result;
	}
	
	public Map<Integer, TopoNode> getRanking(){
		Map<Integer, TopoNode> thisRanking = new HashMap<Integer, TopoNode>();
		for (TopoNode tn : verticies){
			thisRanking.put(tn.rank, tn);
		}
		return thisRanking;
	}

	public boolean rankedEquals(TopoLevel that) {
		Map<Integer, TopoNode> thisRanking = this.getRanking();
		Map<Integer, TopoNode> thatRanking = that.getRanking();
		
		Set<Integer> allKeys = new HashSet<Integer>();
		allKeys.addAll(thisRanking.keySet());
		allKeys.addAll(thatRanking.keySet());
		if (allKeys.size() != thisRanking.keySet().size() || allKeys.size() != thatRanking.keySet().size()){
			return false;
		}
		 
		for (Integer key : allKeys){
			Bag<Integer> thisParents = thisRanking.get(key).getParentsRanks();
			Bag<Integer> thatParents = thatRanking.get(key).getParentsRanks();
			if (!thisParents.equals(thatParents)){
				return false;
			}
			Bag<Integer> thisSiblings = thisRanking.get(key).getSiblingsRanks();
			Bag<Integer> thatSiblings = thatRanking.get(key).getSiblingsRanks();
			if (!thisSiblings.equals(thatSiblings)){
				return false;
			}
			Bag<Integer> thisChildren = thisRanking.get(key).getChildrensRanks();
			Bag<Integer> thatChildren = thatRanking.get(key).getChildrensRanks();
			if (!thisChildren.equals(thatChildren)){
				return false;
			}
		}
		return true;
	}
}

package topo;

import graph.Vertex;
import graphviz.GraphVizCompatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utility.Bag;
import utility.StateEncoder;

public class TopologicalSort implements GraphVizCompatable{

	public Map<Integer, TopoLevel> strata;
	public TopoNode root;
	public boolean fullyRanked;
	public List<Set<TopoNode>> equivalencySets;
	
	public TopologicalSort(Vertex v){
		strata = new HashMap<Integer, TopoLevel>();
		
		int level = 0;
		List<Vertex> queue = new LinkedList<Vertex>();
		queue.add(v);
		v.clearAllMarks();
		v.mark();
		List<Vertex> toQueue = new LinkedList<Vertex>();
		while (!queue.isEmpty() || !toQueue.isEmpty()){
			TopoLevel s = new TopoLevel(queue, this, level);
			strata.put(level++, s);
			while (!queue.isEmpty()){
				Vertex w = queue.remove(0);
				for (Vertex x : w.getNeighbors()){
					if (!x.marked()){
						toQueue.add(x);
						x.mark();
					}
				}
			}
			queue = new LinkedList<Vertex>();
			queue.addAll(toQueue);
			toQueue = new LinkedList<Vertex>();
		}
		

		root = strata.get(0).verticies.iterator().next();
		
		strata.get(0).attachStrata();
		
		calculateProperties();
		
		createEquivalencySets();
		
		fullyRanked = assignRanks();
		
		v.clearAllMarks();
	}
	
	public void calculateProperties(){
		root.calculateDepth();
		root.calculateDownWeight();
		root.calculateHeight();
		root.calculateUpWeight();
		
		int depth = root.maxDepth();
		int iDeg = root.maxInDegree();
		int sDeg = root.maxSideDegree();
		int oDeg = root.maxOutDegree();
		int mHeight = root.maxHeight();
		int uWeight = root.maxUpWeight();
		int dWeight = root.maxDownWeight();
		
		StateEncoder se = new StateEncoder(depth, iDeg, sDeg, oDeg, mHeight, uWeight, dWeight);		
		root.calculateState(se);
	}
	
	public void createEquivalencySets(){
		List<TopoNode> topoNodes = new ArrayList<TopoNode>(root.getAllInSubtree());
		Collections.sort(topoNodes);
		equivalencySets = new ArrayList<Set<TopoNode>>();
		Set<TopoNode> theSame = new HashSet<TopoNode>();
		TopoNode last = topoNodes.get(0);
		for (int i = 0; i < topoNodes.size(); i++){
			if (topoNodes.get(i).compareTo(last) == 0){
				theSame.add(topoNodes.get(i));
			} else {
				equivalencySets.add(theSame);
				theSame = new HashSet<TopoNode>();
				theSame.add(topoNodes.get(i));
			}
			last = topoNodes.get(i);
		}
		equivalencySets.add(theSame);
	}
	
	public boolean assignRanks(){
		assignTrivialRanks();
		
		boolean allRanked = true;
		do {
			boolean anyFound = false;
			do {
				
				int parentRuns = 0;
				boolean parentFound = false;
				do{
					splitEquivalencySetsBasedOnParentsRanks();
					parentFound = assignTrivialRanks();
					parentRuns++;
				} while (parentFound); 
				
				
				int siblingRuns = 0;
				boolean siblingFound = false;
				do {
					splitEquivalencySetsBasedOnSiblingsRanks();
					siblingFound = assignTrivialRanks();
					siblingRuns++;
				} while (siblingFound);
				
				
				int childRuns = 0;
				boolean childFound = false;
				do {
					splitEquivalencySetsBasedOnChildrensRanks();
					childFound = assignTrivialRanks();
					childRuns++;
				} while (childFound);
				
				anyFound = parentRuns > 1 || childRuns > 1 || siblingRuns > 1;
				
			} while (anyFound == true);
			
			allRanked = true;
			for (TopoNode tn: root.getAllInSubtree()){
				if (tn.rank == 0){
					allRanked = false;
				}
			}
			
			if (allRanked == false){
				splitEquivlaencySetsByArbitraryChoice();
				assignTrivialRanks();
			//	splitEquivalencySetsBasedOnSiblingsRanks();
			//();
			}
			
		} while (allRanked == false);
		
		return true;
	}
	
	
	public boolean assignTrivialRanks(){
		int rank = 1;
		boolean success = false;
		for (Set<TopoNode> eSet : equivalencySets){
			if (eSet.size() == 1){
				TopoNode toRank = eSet.iterator().next();
				if (toRank.rank == 0){
					toRank.assignRank(rank);	
					success = true;
				}
				rank++;
			} else {
				rank += eSet.size();
			}
		}
		return success;
	}
	
	public boolean splitEquivlaencySetsByArbitraryChoice(){
		List<Set<TopoNode>> newEquivalencySets = new ArrayList<Set<TopoNode>>();
		boolean choiceMade = false;
		for(Set<TopoNode> eSet : equivalencySets){
			if (choiceMade){
				newEquivalencySets.add(eSet);
			} else {
				Set<TopoNode> unresolved = new HashSet<TopoNode>();
				for (TopoNode tn : eSet){
					if (tn.rank == 0 && !choiceMade){
						unresolved.add(tn);
						choiceMade = true;
					}
				}
				if (unresolved.size() == 0){
					newEquivalencySets.add(eSet);
				} else {
					newEquivalencySets.add(unresolved);
					eSet.removeAll(unresolved);
					newEquivalencySets.add(eSet);
				}
			}
		}
		this.equivalencySets = newEquivalencySets;
		return choiceMade;
	}
	
	public void splitEquivalencySetsBasedOnParentsRanks(){
		List<Set<TopoNode>> newEquivalencySets = new ArrayList<Set<TopoNode>>();
		for (Set<TopoNode> oldESet : equivalencySets){
			List<Bag<Integer>> allParentRanks = new ArrayList<Bag<Integer>>();	
			Map<Object, TopoNode> inverter = new HashMap<Object, TopoNode>();	
			for (TopoNode tn : oldESet){
				Bag<Integer> parentRanks = tn.getParentsRanks();
				allParentRanks.add(parentRanks);
				inverter.put(parentRanks, tn);
			}
			Collections.sort(allParentRanks);
			
			Set<TopoNode> theSame = new HashSet<TopoNode>();
			Bag<Integer> last = allParentRanks.get(0);
			
			for(int i = 0; i < allParentRanks.size(); i++){
				Bag<Integer> parentRanks = allParentRanks.get(i);
				if (parentRanks.equals(last)){
					theSame.add(inverter.get(parentRanks));
				} else {
					newEquivalencySets.add(theSame);
					theSame = new HashSet<TopoNode>();
					theSame.add(inverter.get(parentRanks));
				}
				last = allParentRanks.get(i);
			}
			newEquivalencySets.add(theSame);
		}
		this.equivalencySets = newEquivalencySets;
	}
	
	public void splitEquivalencySetsBasedOnSiblingsRanks(){
		List<Set<TopoNode>> newEquivalencySets = new ArrayList<Set<TopoNode>>();
		for (Set<TopoNode> oldESet : equivalencySets){
			List<Bag<Integer>> allSiblingRanks = new ArrayList<Bag<Integer>>();	
			Map<Object, TopoNode> inverter = new HashMap<Object, TopoNode>();	
			for (TopoNode tn : oldESet){
				Bag<Integer> siblingRanks = tn.getSiblingsRanks();
				allSiblingRanks.add(siblingRanks);
				inverter.put(siblingRanks, tn);
			}
			Collections.sort(allSiblingRanks);
			
			Set<TopoNode> theSame = new HashSet<TopoNode>();
			Bag<Integer> last = allSiblingRanks.get(0);
			
			for(int i = 0; i < allSiblingRanks.size(); i++){
				Bag<Integer> siblingRanks = allSiblingRanks.get(i);
				if (siblingRanks.equals(last)){
					theSame.add(inverter.get(siblingRanks));
				} else {
					newEquivalencySets.add(theSame);
					theSame = new HashSet<TopoNode>();
					theSame.add(inverter.get(siblingRanks));
				}
				last = allSiblingRanks.get(i);
			}
			newEquivalencySets.add(theSame);
		}
		this.equivalencySets = newEquivalencySets;
	}
	
	public void splitEquivalencySetsBasedOnChildrensRanks(){
		List<Set<TopoNode>> newEquivalencySets = new ArrayList<Set<TopoNode>>();
		for (Set<TopoNode> oldESet : equivalencySets){
			List<Bag<Integer>> allChildrenRanks = new ArrayList<Bag<Integer>>();	
			Map<Object, TopoNode> inverter = new HashMap<Object, TopoNode>();	
			for (TopoNode tn : oldESet){
				Bag<Integer> childRanks = tn.getChildrensRanks();
				allChildrenRanks.add(childRanks);
				inverter.put(childRanks, tn);
			}
			Collections.sort(allChildrenRanks);
			
			Set<TopoNode> theSame = new HashSet<TopoNode>();
			Bag<Integer> last = allChildrenRanks.get(0);
			
			for(int i = 0; i < allChildrenRanks.size(); i++){
				Bag<Integer> childRanks = allChildrenRanks.get(i);
				if (childRanks.equals(last)){
					theSame.add(inverter.get(childRanks));
				} else {
					newEquivalencySets.add(theSame);
					theSame = new HashSet<TopoNode>();
					theSame.add(inverter.get(childRanks));
				}
				last = allChildrenRanks.get(i);
			}
			newEquivalencySets.add(theSame);
		}
		this.equivalencySets = newEquivalencySets;
	}
	
	public boolean rankedEquals(TopologicalSort that){
		if (this.strata.size() != that.strata.size()){
			return false;
		}
		if (!this.fullyRanked || !that.fullyRanked){
			System.err.println("Trying to compare two non-fully-ranked TopoSorts!");
			return false;
		}
		for (int i = 0; i < strata.size(); i++){
			TopoLevel thisLevel = this.strata.get(i);
			TopoLevel thatLevel = that.strata.get(i);
			if (!thisLevel.rankedEquals(thatLevel)){
				return false;
			}
		}
		return true;
	}

	public Map<Integer, TopoNode> getAllRankings(){
		Map<Integer, TopoNode> result = new HashMap<Integer, TopoNode>();
		for (TopoLevel tl : strata.values()){
			result.putAll(tl.getRanking());
		}
		return result;
	}

	public String toString(){
		String result = "";
		for (Integer i : strata.keySet()){
			result += "\t" + strata.get(i).toString() + "\n";
		}
		result = "Stratified Graph {" + result + "}";
		return result;
	}

	@Override
	public String toGraphVizRepresenation() {
		String result = "rankdir=LR;\n{\n";
		String strataResult = "";
		for (TopoLevel s : strata.values()){
			String strataCode = "";
			for (TopoNode sv : s.verticies){
				for (TopoNode child : sv.children){
					result += sv.toString() + " -- " + child.toString() + ";\n";
				}
				for (TopoNode sibling : sv.siblings){
					if (sv.id.compareTo(sibling.id) != 0){
						String sub = sv.toString() + " -- " + sibling.toString() + ";\n";
						String alt = sibling.toString() + " -- " + sv.toString() + ";\n";
						if (!result.contains(sub) && !result.contains(alt)){
							result += sub;
						}
					}
				}
				strataCode += ", " + sv.toString();
			}
			strataResult += "{ rank=same;" + strataCode.substring(1) + "}\n";
		}
		result = result + "}\n" + strataResult;
		return result;
	}

	@Override
	public boolean isDigraph() {
		return false;
	}
}

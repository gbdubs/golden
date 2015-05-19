package topo;

import graph.Vertex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utility.Bag;
import utility.Namer;
import utility.StateEncoder;

public class TopoNode implements Comparable<TopoNode>{
	
	public TopoLevel strata;
	public Vertex counterpart;
	
	public List<TopoNode> parents;
	public List<TopoNode> siblings;
	public List<TopoNode> children;
	
	public int rank;
	public String id;
	
	public int upWeight;
	public int downWeight;
	
	public int height;
	public int depth;
	
	public BigInteger state;
	
	public TopoNode(TopoLevel strata, Vertex counterpart){
		this.id = Namer.intToString(counterpart.getId());
		
		this.parents = new ArrayList<TopoNode>();
		this.siblings = new ArrayList<TopoNode>();
		this.children = new ArrayList<TopoNode>();
		
		this.strata = strata;
		this.counterpart = counterpart;
		
		this.rank = 0;
		
		this.upWeight = -1;
		this.downWeight = -1;
		this.height = -1;
		this.depth = -1;
	} 
	
	public Bag<BigInteger> getParentsStates(){
		Bag<BigInteger> result = new Bag<BigInteger>();
		for(TopoNode tn : parents){
			result.add(tn.state);
		}
		return result;
	}
	
	public Bag<Integer> getParentsRanks(){
		Bag<Integer> result = new Bag<Integer>();
		for(TopoNode tn : parents){
			result.add(tn.rank);
		}
		return result;
	}
	
	public Bag<BigInteger> getSiblingsStates(){
		Bag<BigInteger> result = new Bag<BigInteger>();
		for(TopoNode tn : siblings){
			result.add(tn.state);
		}
		return result;
	}
	
	public Bag<Integer> getSiblingsRanks(){
		Bag<Integer> result = new Bag<Integer>();
		for(TopoNode tn : siblings){
			result.add(tn.rank);
		}
		return result;
	}
	
	public Bag<BigInteger> getChildrensStates(){
		Bag<BigInteger> result = new Bag<BigInteger>();
		for(TopoNode tn : children){
			result.add(tn.state);
		}
		return result;
	}

	public Bag<Integer> getChildrensRanks(){
		Bag<Integer> result = new Bag<Integer>();
		for(TopoNode tn : children){
			result.add(tn.rank);
		}
		return result;
	}
	
	public int calculateDownWeight(){
		if (downWeight != -1){
			return downWeight;
		} else {
			if (children.size() == 0){
				this.downWeight = 1;
				return 1;
			} else {
				int total = 0;
				for (TopoNode sv : children){
					total += sv.calculateDownWeight();
				}
				this.downWeight = total;
				return total;
			}
		}
	}
	
	public int calculateUpWeight(){
		if (upWeight != -1){
			return upWeight;
		} else {
			if (parents.size() == 0){
				this.upWeight = 1;
				for (TopoNode tn : children){
					if (tn.upWeight == -1){
						tn.calculateUpWeight();
					}
				}
				return upWeight;
			} else {
				int total = 1;
				for (TopoNode sv : parents){
					total += sv.calculateUpWeight();
				}
				this.upWeight = total;
				for (TopoNode tn : children){
					if (tn.upWeight == -1){
						tn.calculateUpWeight();
					}
				}
				return total;
			}
		}
	}
	
	public int calculateHeight(){
		if (height != -1){
			return height;
		} else {
			if (children.size() == 0){
				this.height = 1;
				return 1;
			} else {
				int max = 0;
				for (TopoNode sv : children){
					max = Math.max(max, sv.calculateHeight());
				}
				this.height = max + 1;
				return height;
			}
		}
	}
	
	public int calculateDepth(){
		if (depth != -1){
			return depth;
		} else {
			if (parents.size() == 0){
				this.depth = 1;
				for (TopoNode tn : children){
					if (tn.depth == -1){
						tn.calculateDepth();
					}
				}
				return 1;
			} else {
				int max = 0;
				for (TopoNode sv : parents){
					max = Math.max(max, sv.calculateDepth());
				}
				this.depth = max + 1;
				for (TopoNode tn : children){
					if (tn.depth == -1){
						tn.calculateDepth();
					}
				}
				return depth;
			}
		}
	}
	
	public void calculateState(StateEncoder se){
		if (state == null){
			state = se.encode(depth, parents.size(), siblings.size(), children.size(), height, downWeight, upWeight);
			for (TopoNode child : children){
				child.calculateState(se);
			}
		}
	}
	
	public int maxUpWeight(){
		int max = this.upWeight;
		for (TopoNode child : children){
			max = Math.max(max, child.maxUpWeight());
		}
		return max;
	}
	
	public int maxDownWeight(){
		int max = this.downWeight;
		for (TopoNode child : children){
			max = Math.max(max, child.maxDownWeight());
		}
		return max;
	}
	
	public int maxHeight(){
		int max = this.height;
		for (TopoNode child : children){
			max = Math.max(max, child.maxHeight());
		}
		return max;
	}
	
	public int maxDepth(){
		int max = this.depth;
		for (TopoNode child : children){
			max = Math.max(max, child.maxDepth());
		}
		return max;
	}
	
	public int maxInDegree(){
		int max = this.parents.size();
		for (TopoNode child : children){
			max = Math.max(max, child.maxInDegree());
		}
		return max;
	}
	
	public int maxSideDegree(){
		int max = this.siblings.size();
		for (TopoNode child : children){
			max = Math.max(max, child.maxSideDegree());
		}
		return max;
	}
	
	public int maxOutDegree(){
		int max = this.children.size();
		for (TopoNode child : children){
			max = Math.max(max, child.maxOutDegree());
		}
		return max;
	}
	
	public String toString(){
		return id + "_" + rank;
	}
	
	public String toLongString(){
		String result = id + " L" + depth +
				" P" + parents.size() + 
				" S" + siblings.size() + 
				" C" + children.size() + 
				" H" + height +
				" D" + downWeight + 
				" U" + upWeight + 
				" S" + state.toString() + 
				" = R" + rank;
		return result;
	}
	
	public int minimallyEquivalent(TopoNode other){
		return this.state.compareTo(other.state);
	}
	
	public int oneDegreeEquivalent(TopoNode other){
		int minimalResult = minimallyEquivalent(other);
		
		if (minimalResult != 0) return minimalResult;
		
		Bag<BigInteger> thisParents = this.getParentsStates();
		Bag<BigInteger> otherParents = other.getParentsStates();
		int parentalResult = thisParents.compareTo(otherParents);
		if (parentalResult != 0) return parentalResult;
		
		Bag<BigInteger> thisSiblings = this.getSiblingsStates();
		Bag<BigInteger> otherSiblings = other.getSiblingsStates();
		int siblingResult = thisSiblings.compareTo(otherSiblings);
		if (siblingResult != 0) return siblingResult;
		
		Bag<BigInteger> thisChildren = this.getChildrensStates();
		Bag<BigInteger> otherChildren = other.getChildrensStates();
		int childrenResult = thisChildren.compareTo(otherChildren);
		if (childrenResult != 0) return childrenResult;
		
		return 0;
	}

	public Set<TopoNode> getAllInSubtree(){
		Set<TopoNode> all = new HashSet<TopoNode>();
		all.add(this);
		for (TopoNode child : children){
			all.addAll(child.getAllInSubtree());
		}
		return all;
	}
	
	@Override
	public int compareTo(TopoNode other) {
		return oneDegreeEquivalent(other);
	}

	public void assignRank(int r) {
		if (r == 1 && depth != 1){
			System.err.println("ATTEMPTING TO ASSIGN RANK 1 TO A NON FIRST CLASS NODE");
		}
		
		this.rank = r;
	}
	
	public static Set<Integer> getSetRanks(Collection<TopoNode> nodes){
		Set<Integer> result = new HashSet<Integer>();
		for (TopoNode tn : nodes){
			result.add(tn.rank);
		}
		return result;
	}
}

package graph;

import java.util.HashSet;
import java.util.Set;

import utility.Namer;

/**
 * The Vertex Class offers the modular basis for the graph class. All operations
 * performed on vertices are primitive, and all higher level manipulation is
 * done at the graph level. Since almost all of these methods are trivial, there
 * isn't much justification for them.
 * 
 * @author Grady
 */
public class Vertex {

	private static int currentId = 1;
	private int id;
	private Set<Vertex> neighbors;
	private boolean marked;

	public Vertex() {
		this.id = currentId++;
		this.setNeighbors(new HashSet<Vertex>());
		this.marked = false;
	}

	public Vertex(Set<Vertex> neighbors) {
		this();
		this.getNeighbors().addAll(neighbors);
	}

	public void makeNeighbors(Vertex b) {
		this.getNeighbors().add(b);
		b.getNeighbors().add(this);
	}

	public Set<Vertex> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(Set<Vertex> neighbors) {
		this.neighbors = neighbors;
	}

	public boolean marked() {
		return marked;
	}

	public void mark() {
		marked = true;
	}

	public void clearAllMarks() {
		this.marked = false;
		for (Vertex v : getNeighbors()) {
			if (v.marked) {
				v.clearAllMarks();
			}
		}
	}

	public int getId() {
		return id;
	}

	public String toString() {
		return "VERTEX " + Namer.intToString(getId());
	}

	/**
	 * Finds the number of nodes that could be reached by an edge of a given
	 * length or shorter. For example, if i==3, then the resulting number would
	 * be the number of nodes accessible after a third traversal of a breadth
	 * first search.
	 * 
	 * @param vertex
	 *            The Vertex that the traversal is based from.
	 * @param radius
	 *            The "radius" of the search.
	 * @return
	 */
	public int numberOfRelativesOfDistance(int radius) {
		Set<Vertex> allAtPrevLevel = new HashSet<Vertex>();
		allAtPrevLevel.add(this);
		Set<Vertex> allAtNextLevel = new HashSet<Vertex>();
		int currentDepth = 0;
		while (currentDepth++ < radius) {
			for (Vertex w : allAtPrevLevel) {
				allAtNextLevel.addAll(w.getNeighbors());
			}
			allAtPrevLevel = allAtNextLevel;
			allAtNextLevel = new HashSet<Vertex>();
		}
		return allAtPrevLevel.size();
	}
}

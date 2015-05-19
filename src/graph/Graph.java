package graph;
import graphviz.GraphVizCompatable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utility.Namer;


public class Graph implements GraphVizCompatable{

	private Set<Vertex> verticies;
	private Set<Graph> connectedComponents;

	/**
	 * Constructs an undirected graph from a general string describing one.
	 * 
	 * Graphs do not need to be fully connected, but all nodes must be declared.
	 * Singleton nodes are declared as: "NodeName;"
	 * Edges (which imply their nodes): "FirstNode -- SecondNode;"
	 * 
	 * @param universalRepresentation A semicolon delimited string which describes a graph
	 * using the conventions specified above.
	 */
	public Graph (String universalRepresentation) {

		Map<String, Vertex> encoding = new HashMap<String, Vertex>();
		String[] commands = universalRepresentation.split(";");

		for (String s : commands) {
			if (s.contains("--")) {
				// We have two nodes, connect them, and only create new vertices if we
				// Haven't seen this particular string before.
				String[] parts = s.split("--");

				Vertex a = encoding.get(parts[0].trim());
				if (a == null) {
					a = new Vertex();
					encoding.put(parts[0].trim(), a);
				}

				Vertex b = encoding.get(parts[1].trim());
				if (b == null) {
					b = new Vertex();
					encoding.put(parts[1].trim(), b);
				}

				a.makeNeighbors(b);
			} else {
				// Otherwise we have a fully disconnected node, so just place it in the encoding.
				encoding.put(s.trim(), new Vertex());
			}
		}

		this.verticies = new HashSet<Vertex>(encoding.values());
		this.calculateConnectedComponents();

	}

	/**
	 * A private constructor to create CONNNECTED subgraphs for a larger graph.
	 * Note that this constructor explicitly claims that it is fully connected.
	 * 
	 * @param verticies The Vertices of your fully connected subgraph
	 */
	private Graph (Collection<Vertex> verticies) {

		this.verticies = new HashSet<Vertex>(verticies);
		this.connectedComponents = new HashSet<Graph>();
		this.connectedComponents.add(this);

	}


	/**
	 * Inverts a Graph.  If a connection between two nodes existed before, it now does not.  
	 * If no connection existed before, one now does. Note that this operation will very likely make
	 * a connected graph not connected, and might merge connected components. In Set Notation:
	 * {New Neighbors of V} = {All Vertexes} - {V} - {Previous Neighbors of V}
	 */
	public void invert() {
		for (Vertex v : verticies) {
			Set<Vertex> newNeighbors = new HashSet<Vertex>();
			newNeighbors.addAll(this.verticies);
			newNeighbors.removeAll(v.getNeighbors());
			newNeighbors.remove(v);
			v.setNeighbors(newNeighbors);
		}
		// Recalculates the connected components, which have almost certainly changed.
		this.calculateConnectedComponents();
	}

	/**
	 * The number of vertices in the graph.
	 * @return The Number of Vertices in the Graph.
	 */
	public int nVerticies() {
		return verticies.size();
	}

	/**
	 * The number of edges in the Graph, using the fact that each edge has two end-points.
	 * @return The Number of Edges in the Graph.
	 */
	public int nEdges() {
		int total = 0;
		for (Vertex v : verticies) {
			total += v.getNeighbors().size();
		}
		// # Edges = Sum(Total Degree of all Vertices) / 2, as there are 2 vertices per edge.
		return total / 2;
	}

	@Override
	public String toString() {
		return toGraphVizRepresenation();
	}

	/**
	 * Returns the graph-viz notation that will display the graph (including disconnected subgraphs)
	 */
	@Override
	public String toGraphVizRepresenation() {
		String representation = "";
		for (Vertex v : verticies) {
			for (Vertex neighbor : v.getNeighbors()) {
				if (v.getId() < neighbor.getId()) {
					representation += " " + Namer.intToString(v.getId()) + " -- " + Namer.intToString(neighbor.getId()) + " ;";
				}
			}
			if (v.getNeighbors().size() == 0) {
				representation += " " + Namer.intToString(v.getId()) + " ;";
			}
		}
		return representation;
	}

	@Override
	public boolean isDigraph() {
		// We deal solely with undirected graphs in this graph implementation.
		return false;
	}

	/**
	 * Finds all fully connected vertices in a Graph.
	 * Note that these vertices can be removed from any graph and still test isomorphism. This can be
	 * particularly helpful in that it dictates that any topological sort of a graph will have more than
	 * two levels.  This turns out to be important when we are trying to ensure that any such tree can
	 * be deterministically ranked.
	 * @return The list of all fully connected vertices in the graph
	 */
	public List<Vertex> totallyConnected() {
		List<Vertex> result = new ArrayList<Vertex>();
		for (Vertex v : verticies) {
			if (v.getNeighbors().size() == verticies.size() - 1) {
				result.add(v);
			}
		}
		return result;
	}

	/**
	 * Removes a specific vertex from the graph.
	 * @param toRemove The Vertex To Remove
	 */
	public void removeVertex(Vertex toRemove) {
		for (Vertex v : verticies) {
			v.getNeighbors().remove(toRemove);
		}
		verticies.remove(toRemove);
	}


	/**
	 * Gets the set of nodes which have the best probability of generating a deterministic tree. This allows
	 * us to limit the number of "outer loops" we do from V down to K (the number of meekest nodes),
	 * which should be a pretty small number. This is CRITICAL for running time improvements, and also
	 * will seriously increase our chances of finding an isomorphism when it exists.
	 * 
	 * We define meekness as follows (higher level trumps lower level)
	 * - Smallest number of Vertexes accessible in 1 step.
	 * - Smallest number of Vertexes accessible in 2 steps.
	 * - ...
	 * - Smallest number of Vertexes accessible in V steps (which in a fully connected graph will always be V)
	 * 
	 * Note that this method also is used in getMeekestNode, which selects one randomly from the set of equivalently
	 * meek nodes.
	 * @return One of the meekest nodes in the tree.
	 */
	public List<Vertex> getMeekestNodes() {
		List<Vertex> candidates = new ArrayList<Vertex>();
		candidates.addAll(verticies);
		List<Vertex> selectedCandidates = new ArrayList<Vertex>();

		for (int i = 1; i < verticies.size(); i++) {
			int meekestNumber = verticies.size();
			for (Vertex v : verticies) {
				int family = v.numberOfRelativesOfDistance(i);
				if (family < meekestNumber) {
					selectedCandidates = new ArrayList<Vertex>();
					meekestNumber = family;
					selectedCandidates.add(v);
				} else if (family == meekestNumber) {
					selectedCandidates.add(v);
				}
			}
			if (selectedCandidates.size() == 1) {
				return selectedCandidates;
			}
			candidates = selectedCandidates;
			selectedCandidates = new ArrayList<Vertex>();
		}
		return candidates;

	}

	/**
	 * Gets a node which is of the meekest set.  Returns an arbitrary one from this set.
	 * @return
	 */
	public Vertex getMeekestNode() {
		return getMeekestNodes().get(0);
	}

	/**
	 * Calculates all self-contained subgraphs within a graph.
	 * Note that this method can be called multiple times, and will recalculate based on the
	 * current neighbors of each node.
	 */
	private void calculateConnectedComponents() {
		this.connectedComponents = new HashSet<Graph>();
		Set<Vertex> disconnected = new HashSet<Vertex>(verticies);

		while (disconnected.size() != 0) {
			List<Vertex> connected = new ArrayList<Vertex>();

			connected.add(verticies.iterator().next());

			for (int i = 0; i < connected.size(); i++) {
				for (Vertex v : connected.get(i).getNeighbors()) {
					if (!connected.contains(v)) {
						connected.add(v);
					}
				}
			}

			disconnected.removeAll(connected);
			connectedComponents.add(new Graph(connected));
		}
	}

	public Set<Vertex> getVertices() {
		return verticies;
	}
}

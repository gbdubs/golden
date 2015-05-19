package random;

import graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utility.Namer;

/**
 * A Utility Factory class to create new graphs for randomized testing. Can
 * create: - Graphs (connected or random) - Random Isomorphisms of a given Graph
 * 
 * Because the validity of this class is fundamental to testing other classes,
 * it has been tested well.
 * 
 * @author Grady
 */
public class RandomGraphGenerator {

	/**
	 * Creates a random Connected Graph with V Vertices and E Edges. Note that a
	 * graph with V vertices that wishes to be fully connected must have at
	 * least (V-1 Edges), and cannot have more than (V * (V-1) / 2) Edges. Any
	 * input parameters which violate these constraints will throw a runtime
	 * exception.
	 * 
	 * @param nVerticies
	 *            The Number of Vertices in the Resulting Graph
	 * @param nEdges
	 *            The Number of Edges in the resulting Graph
	 * @return A Graph with the given number of Vertices and Edges
	 */
	public static Graph generateRandomConnectedGraph(int nVerticies, int nEdges) {
		// Asserts that we can construct a valid graph under these parameters
		assertValidFullyConnected(nVerticies, nEdges);

		// Creates a list of vertices not yet connected to the graph
		List<Integer> toConnect = generateRange(nVerticies);
		List<Integer> connected = new ArrayList<Integer>();

		// Our running expression is a Set to disallow redundancy
		Set<String> expression = new HashSet<String>();

		// Constructs new expressions until we have created the desired # of
		// edges
		while (expression.size() < nEdges) {
			expression.add(generateRandomExpression(nVerticies, toConnect, connected));
		}
		
		String textRepresentation = "";
		for (String s : expression) {
			textRepresentation += s;
		}

		return new Graph(textRepresentation);
	}

	/**
	 * Creates a random Isomorphism of a given connected Graph G.
	 * 
	 * @param g
	 *            The Original Graph
	 * @return An Isomorphism of the original graph.
	 */
	public static Graph generateRandomConnectedIsomorphism(Graph g) {
		String[] representation = g.toString().split(";");
		Map<String, String> allNodes = new HashMap<String, String>();
		Set<String> uniqueIds = new HashSet<String>();
		for (String rep : representation) {
			for (String part : rep.split(" -- ")) {
				if (!allNodes.containsKey(part.trim())) {
					String rand = Namer.intToString((int) (Math.random() * 1000 + 1));
					while (uniqueIds.contains(rand)) {
						rand = Namer.intToString((int) (Math.random() * 1000 + 1));
					}
					uniqueIds.add(rand);
					allNodes.put(part.trim(), rand);
				}
			}
		}

		// If our resulting Isomorphism does not have the same number of
		// vertices and edges
		// we have a serious problem. This catches lots of potential problems
		// (largely around connectivity)
		if (uniqueIds.size() != g.nVerticies()) {
			throw new RuntimeException(
					"Something is wrong here... Was this graph fully connected?");
		}

		String result = "";
		for (String rep : representation) {
			String newPart = "";
			if (rep.length() > 0) {
				for (String part : rep.split(" -- ")) {
					newPart += allNodes.get(part.trim()) + " -- ";
				}
				result += newPart.substring(0, newPart.length() - 4) + ";";
			}
		}

		return new Graph(result);
	}

	// Generates a random expression using a previously unused variable.
	private static String generateRandomExpression(int max, List<Integer> toConnect, List<Integer> connected) {
		if (toConnect.size() == 0) {
			int num = generateRandomNumber(max);
			int other = generateRandomNumberExclusive(max, num);
			return generateStatementFromIntegers(num, other);
		} else {
			Integer first = generateRandomNumberFromSet(toConnect);
			toConnect.remove(first);
			int second;
			if (connected.size() > 0){
				second = generateRandomNumberFromSet(connected);
			} else {
				second = generateRandomNumberFromSet(toConnect);
				toConnect.remove(new Integer(second));
				connected.add(second);
			}
			connected.add(first);
			return generateStatementFromIntegers(first, second);
		}
	}

	// Generates a random integer between [1, Max] (both inclusive), with
	// uniform probability
	private static int generateRandomNumber(int max) {
		return (int) (Math.random() * max) + 1;
	}

	// Generates a random integer between [1, Max] that is not a specific
	// (excluded) integer.
	private static int generateRandomNumberExclusive(int max, int exclude) {
		int result = exclude;
		while (result == exclude) {
			result = generateRandomNumber(max);
		}
		return result;
	}

	// Selects a random element of a set.
	private static Integer generateRandomNumberFromSet(List<Integer> l) {
		return l.get((int) (Math.random() * l.size()));
	}

	// Returns a sequential integer list containing [1, Max] (both inclusive)
	private static List<Integer> generateRange(int max) {
		List<Integer> l = new ArrayList<Integer>();
		for (int i = 1; i <= max; i++) {
			l.add(i);
		}
		return l;
	}

	// Generates a GraphViz Statement given two integers.
	private static String generateStatementFromIntegers(int i, int j) {
		int min = Math.min(i, j);
		int max = Math.max(i, j);
		return Namer.intToString(min) + " -- " + Namer.intToString(max) + ";";
	}

	// Checks that the number of edges is within [V - 1, (V - 1) * V / 2] (the
	// range possible for a fully connected graph.
	private static void assertValidFullyConnected(int nVerticies, int nEdges) {
		if (nEdges + 1 < nVerticies) {
			throw new RuntimeException(
					"Cannot generate CONNECTED graph under the conditions: EDGES["
							+ nEdges + "] VERTICIES[" + nVerticies + "]");
		} else if (nEdges > (nVerticies * (nVerticies - 1)) / 2) {
			throw new RuntimeException(
					"Cannot generate a graph under hyper-fully-connected conditions: EDGES["
							+ nEdges + "] VERTICIES[" + nVerticies + "]");
		}
	}
}

package test;

import static org.junit.Assert.*;
import graph.Vertex;

import org.junit.Test;

public class TestVertex {

	@Test
	public void testBreadthFirstSearchNeighborCounts(){
		
		Vertex a = new Vertex();
		Vertex b = new Vertex();
		Vertex c = new Vertex();
		Vertex d = new Vertex();
		Vertex e = new Vertex();
		Vertex f = new Vertex();
		Vertex g = new Vertex();
		Vertex h = new Vertex();
		Vertex i = new Vertex();
		Vertex j = new Vertex();
		
		a.makeNeighbors(b);
		b.makeNeighbors(c);
		b.makeNeighbors(e);
		c.makeNeighbors(d);
		c.makeNeighbors(e);
		c.makeNeighbors(f);
		d.makeNeighbors(f);
		e.makeNeighbors(g);
		f.makeNeighbors(g);
		g.makeNeighbors(h);
		i.makeNeighbors(j);
		
		/*
		 *            A     J
		 *            |     |
		 *            B     I
		 *           / \
		 *      D - C - E
		 *       \ /     \
		 *        F  ---  G - H
		 */
		
		assertEquals(1, a.numberOfRelativesOfDistance(0));
		assertEquals(2, a.numberOfRelativesOfDistance(1));
		assertEquals(4, a.numberOfRelativesOfDistance(2));
		assertEquals(7, a.numberOfRelativesOfDistance(3));
		assertEquals(8, a.numberOfRelativesOfDistance(4));
		assertEquals(8, a.numberOfRelativesOfDistance(10));
		assertEquals(8, a.numberOfRelativesOfDistance(100));
		
		assertEquals(1, g.numberOfRelativesOfDistance(0));
		assertEquals(4, g.numberOfRelativesOfDistance(1));
		assertEquals(7, g.numberOfRelativesOfDistance(2));
		assertEquals(8, g.numberOfRelativesOfDistance(3));
		assertEquals(8, g.numberOfRelativesOfDistance(10));
		assertEquals(8, g.numberOfRelativesOfDistance(100));
		
		assertEquals(1, j.numberOfRelativesOfDistance(0));
		assertEquals(2, j.numberOfRelativesOfDistance(1));
		assertEquals(2, j.numberOfRelativesOfDistance(10));
		assertEquals(2, j.numberOfRelativesOfDistance(100));
	}
}

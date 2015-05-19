package test;

import static org.junit.Assert.*;
import graph.Graph;
import graphviz.GraphMaker;

import java.io.File;
import java.util.UUID;

import org.junit.Test;

public class TestGraphCreation {
	
	@Test
	public void testGraphVizAPI(){
		String uuid = UUID.randomUUID().toString();
		assertFalse((new File(uuid + ".png")).exists());
	    String encoding = "World -- Peace; Peace -- World; Peace -- Sign; Sign -- Hello; Sign -- World";
		Graph g = new Graph(encoding);
		GraphMaker.print(g, uuid);
		File result = (new File(uuid + ".png"));
		assertTrue(result.exists());
		result.delete();
	}
}

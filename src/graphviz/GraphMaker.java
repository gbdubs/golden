package graphviz;

import java.io.File;

public class GraphMaker {
	
	public static void print(GraphVizCompatable gvc, String name){
		GraphViz gv = new GraphViz();
		if (gvc.isDigraph()){
			gv.addln("digraph G {");
		} else {
			gv.addln("graph G {");
		}
		gv.addln(gvc.toGraphVizRepresenation());
		gv.addln("}"); 
		File out = new File("/Users/Grady/Desktop/Iso/Golden/"+ name + ".png");   
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), "png"), out);
	}
	
}

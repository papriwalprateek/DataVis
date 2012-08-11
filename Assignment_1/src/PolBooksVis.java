import javax.swing.JFrame;
import java.io.*;
import java.util.*;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;


public class PolBooksVis {

	public static void main(String[] argv)
	{
		File file = new File("data/polbooks.gml");	
		Graph graph = new Graph();
		graph = createGraph(file);
		// 2. prepare the visualization

        Visualization vis = new Visualization();
        /* vis is the main object that will run the visualization */
        vis.add("socialnet", graph);
        /* add our data to the visualization */

        // 3. setup the renderers and the render factory

        // labels for name
        LabelRenderer nameLabel = new LabelRenderer("label");
        nameLabel.setRoundedCorner(8, 8);
        /* nameLabel decribes how to draw the data elements labeled as "name" */

        // create the render factory
        vis.setRendererFactory(new DefaultRendererFactory(nameLabel));

        // 4. process the actions

        // colour palette for nominal data type
        int[] palette = new int[]{ColorLib.rgb(255, 180, 180), ColorLib.rgb(190, 190, 255),ColorLib.rgb(0, 191, 255)};
        /* ColorLib.rgb converts the colour values to integers */


        // map data to colours in the palette
        DataColorAction fill = new DataColorAction("socialnet.nodes", "value", Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
        /* fill describes what colour to draw the graph based on a portion of the data */

        // node text
        ColorAction text = new ColorAction("socialnet.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0));
        /* text describes what colour to draw the text */

        // edge
        ColorAction edges = new ColorAction("socialnet.edges", VisualItem.STROKECOLOR, ColorLib.gray(200));
        /* edge describes what colour to draw the edges */

        // combine the colour assignments into an action list
        ActionList colour = new ActionList();
        colour.add(fill);
        colour.add(text);
        colour.add(edges);
        vis.putAction("colour", colour);
        /* add the colour actions to the visualization */

        // create a separate action list for the layout
        ActionList layout = new ActionList(Activity.INFINITY);
        layout.add(new ForceDirectedLayout("socialnet"));
        /* use a force-directed graph layout with default parameters */

        layout.add(new RepaintAction());
        /* repaint after each movement of the graph nodes */

        vis.putAction("layout", layout);
        /* add the laout actions to the visualization */

        // 5. add interactive controls for visualization

        Display display = new Display(vis);
        display.setSize(700, 700);
        display.pan(350, 350);	// pan to the middle
        display.addControlListener(new DragControl());
        /* allow items to be dragged around */

        display.addControlListener(new PanControl());
        /* allow the display to be panned (moved left/right, up/down) (left-drag)*/

        display.addControlListener(new ZoomControl());
        /* allow the display to be zoomed (right-drag) */

        // 6. launch the visualizer in a JFrame

        JFrame frame = new JFrame("prefuse tutorial: socialnet");
        /* frame is the main window */

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(display);
        /* add the display (which holds the visualization) to the window */

        frame.pack();
        frame.setVisible(true);

        /* start the visualization working */
        vis.run("colour");
        vis.run("layout");
		
    }
	
	public static Graph createGraph(File file)
	{
        Table nodeData = new Table();
        nodeData.addColumn("label", String.class);
        nodeData.addColumn("value", String.class);        
		Graph graph = new Graph(nodeData,true);
		String str = "";
		int source = -1;
		int target = -1;
		NodeStruct current_node = null;
		boolean newnode = false;
		boolean newedge = false;
		try {
			FileInputStream fin = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			while ((str = br.readLine()) != null) {
				str = str.trim();
				String[] temp = str.split("\\s+");
				if(temp[0].equals("node"))
				{
					newnode = true;
					newedge = false;
					current_node = new NodeStruct();
				}
				else if(temp[0].equals("edge"))
				{
					newedge = true;
					newnode = false;
				}
				else if(temp[0].equals("id"))
				{
					current_node.id = Integer.parseInt(temp[1]);					
				}
				else if(temp[0].equals("label"))
				{
					current_node.label = temp[1];			
				}
				else if(temp[0].equals("value"))
				{
					current_node.value = temp[1];
					Node node = graph.addNode();
					node.set("value",current_node.value);
					node.set("label",current_node.label);					
				}				
				else if(temp[0].equals("source"))
				{
					source = Integer.parseInt(temp[1]);
				}
				else if(temp[0].equals("target"))
				{
					target = Integer.parseInt(temp[1]);
					graph.addEdge(source,target);
				}
		    }
		    fin.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return graph;
	}
}

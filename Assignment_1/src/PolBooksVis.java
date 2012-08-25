import javax.swing.JFrame;

import java.awt.Container;
import java.io.*;
import java.util.*;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.assignment.DataShapeAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
public class PolBooksVis
{
	public static void main(String[] argv)
	{
		File file = new File("D:/Study/csp301/polbooks/polbooks.gml");
		File file1=new File("D:/Study/csp301/polblogs/polblogs.gml");
		Graph graph = new Graph();
		graph = createGraph(file);
		
		// 2. prepare the visualization
		vG(graph);

        
        compareGraph(graph);
	//	System.out.println("triads="+findTriads(graph));
	System.out.println("clustering coefficient is = "+ratioOfTriads(graph));
//		Edge e=(Edge)graph.edges().next();
//		System.out.println(isNeighbor(e.getSourceNode(),e.getTargetNode(),graph));
//		Graph g1=makeSample();
//		System.out.println("\n\n No.of triads Is = "+noOfTriads(makeSample()));
    }
	public static Graph createGraph(File file)
	{  int counta = 0;
	int countb = 0;
	int minid = 1000000;
	Node node = null;
	        Table nodeData = new Table();
	        Table edgeData = new Table();
	        nodeData.addColumn("label", String.class);
	        nodeData.addColumn("value", String.class);
	        nodeData.addColumn("source", String.class);
	        edgeData.addColumn(Graph.DEFAULT_SOURCE_KEY, int.class);
	        edgeData.addColumn(Graph.DEFAULT_TARGET_KEY, int.class);
	        edgeData.addColumn("type", String.class);
	Graph graph = new Graph(nodeData, edgeData, false);
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
	int id = Integer.parseInt(temp[1]);
	current_node.id = id;
	if(minid>=id)	
	minid = id;
	}
	else if(temp[0].equals("label"))
	{
	current_node.label = str.split("\"")[1];	
	}
	else if(temp[0].equals("value"))
	{
	current_node.value = temp[1];
	node = graph.addNode();
	node.set("value",current_node.value);
	node.set("label",current_node.label);	
	}	
	else if(temp[0].equals("source"))
	{
	if(newnode==true)
	{
	current_node.source = temp[1].split("\"")[1];
	node.set("source",current_node.source);
	}
	else
	{
	source = Integer.parseInt(temp[1]);
	}
	}
	else if(temp[0].equals("target"))
	{
	target = Integer.parseInt(temp[1]);
	Node sourceNode = graph.getNode(source-minid);
	Node targetNode = graph.getNode(target-minid);
	Edge edge=graph.addEdge(sourceNode,targetNode);
	if(sourceNode.get("value").equals(targetNode.get("value"))){
	edge.set("type", "a");
	counta = counta+1;
	}
	else{
	edge.set("type", "b");
	countb = countb+1;
	}
	}
	}
	fin.close();
	int total = (counta+countb);
	float result= ((float) counta/total);
	System.out.println(result);
	} catch (Exception e) {
	System.out.println(e.toString());
	}
	return graph;
	}
	public static void compareGraph(Graph g)
	{
		int n=g.getEdgeCount();
		int m=g.getNodeCount();
		int i=0;
		Random r1=new Random();
		double [] R=new double[31];
		double [] T=new double [31];
		R[0]=ratio(g);
		T[0]=ratioOfTriads(g);
		for(int k=1;k<31;k++)
		{
			i=r1.nextInt(m);
			Graph sample=randomGraph(i,n,g.getNodeTable(),m);
			R[k]=ratio(sample);
			T[k]=ratioOfTriads(sample);
			visGraph("value",sample,Integer.toString(k));
		}
		for(int j=0;j<31;j++)
		{
			hist(R,"edgeRatio.dat");
			hist(T,"triadsRatio.dat");
		}
	}
 	public static Graph randomGraph(int i,int n,Table nodeData,int m)                               // n= no. of edges,m=no of nodes
	{
		Graph g=new Graph(nodeData,false );
		Random r= new Random(i);
		int n1,n2;
		for(int j=0;j<n;j++)
		{
			n1=r.nextInt(m);
			n2=r.nextInt(m-1);
			g.addEdge(n1, n2);
		}
		return g;
	}
 	public static double ratio(Graph g)															//returns the ratio of edges joining same value nodes to total edges
 	{
 		double d=0.0;
 		int s=0,k=0;																					// s= no of edges connecting same value nodes
 		int n=g.getEdgeCount();
 		for(int j=0;j<n;j++)
 		{
 			Edge e=g.getEdge(j);
 			Node n1=e.getSourceNode();
 			Node n2=e.getAdjacentNode(n1);
 			if(n1.get("value").equals(n2.get("value")))
 				s++;
 		}
 		d=1.0*s/n;
 		Iterator<Edge> i=g.edges();
 		while(i.hasNext())
 		{	Edge e1=i.next();
 			Node n3=e1.getSourceNode();
			Node n4=e1.getTargetNode();
			if(n3.get("value").equals(n4.get("value")))
				k++;
 		}
 		if(k==s)
 			return d;
 		else
 		{
 			System.out.print("error in ratio");
 			return -1;
 		}
 	}
 	public static int findTriads(Graph g)
 	{
 		g.getEdgeTable().addColumn("visited",boolean.class);
 		
 			
// 		System.out.println(g.getEdge(0));
 		int n=0;
 		int m=g.getEdgeCount();
 		int p=g.getNodeCount();
 		for(int j=0;j<m;j++)
 		{
 			Edge e8=g.getEdge(j);
 			System.out.println(e8+" j="+j);
 		}
 		for(int j=0;j<m;j++)
 		{
 			Edge e=g.getEdge(j);
 //			System.out.println(e+" edge "+j);
 			e.set("visited",true);
 			Node n1=e.getSourceNode();
			Node n2=e.getTargetNode();
//			System.out.println("$$$$$$$$$$$$$$j="+j+" label="+n1.get("label")+"###"+n2.get("label"));
			Edge e1=null;
			Edge e2=null;
			for(int i=0;i<p;i++)
 			{
//				System.out.println("for2");
 				Node n3=g.getNode(i);
// 				System.out.println("i="+i+" label="+n3.get("label"));
 				if((n3.get("label").equals(n1.get("label")))&&(n3.get("label").equals(n2.get("label"))));
 				else
 				{
// 					System.out.println("in if1");
 					boolean b1=true,b2=true,b3;int k1=0,k2=0;
 					e1=g.getEdge(n1,n3);
 					e2=g.getEdge(n2,n3);
 					try
 					{
 						e1.equals(e1);
 					}
 					catch(NullPointerException E1)
 					{
 						b1=false;
 				//		System.out.print(".");
 					}
// 					System.out.println(b2);
// 					System.out.println(e2);
 					try
 					{
 						e2.equals(e2);
 					}
 					catch(NullPointerException E2)
 					{
 						b2=false;
 					}
 	//				System.out.print(b1+""+b2);
 					if(b1 && b2)
 					{
	//				System.out.println(e1+" "+e2+" "+"j="+j+" k1="+k1+" k2="+k2);
 						b3=e1.getBoolean("visited") || e2.getBoolean("visited");
 						if(!b3)
 							n++;
 //						else
// 							System.out.println("false");
 					}
 				}
 				
 			}
 		}
 		
 		return n;
 	}
 	public static double ratioOfTriads(Graph g)
 	{
 		int m=0;
 		Iterator<Node> i =g.nodes();
  		Node n;
		ArrayList<Node> C=new ArrayList<Node>();
		ArrayList<Node> B=new ArrayList<Node>();
		while(i.hasNext())
		{
			B.add(i.next());
		}
 		for(int p=0;p<B.size();p++)
 		{
 			if(!(C.isEmpty()))
 	 			System.out.print("C not empty!");
 			n=B.get(p);
 			Iterator<Node> j=g.neighbors(n);
 			int save=g.getDegree(n);
 			for(int q=0;q<save;q++)
 			{
 				Node temp=j.next();
				if(B.indexOf(temp)>p)
 					C.add(temp);
 			}
 			for(int a=0;a<C.size()-1;a++)
 				for(int b=a+1;b<C.size();b++)
 					if(isNeighbor(C.get(a),C.get(b),g))
 						m++;
 			C.removeAll(C);
 		}
 		double d=0.0;
 		int nodes=g.getNodeCount();
 		d=2.0*m/nodes/(nodes-1);
 		return d;
 	}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    	public static Node[] friends(Node n,Graph g) throws Exception
 	{
 		int d=g.getDegree(n);
 		Node[] N=new Node [d];
 		int k=0;
 		Iterator i=g.edges(n);
 		while(i.hasNext())
 		{
 			N[k]=g.getAdjacentNode((Edge)i.next(), n);
 			k++;
 		}
 		if(k!=d)
 			throw new Exception("something wrong. check how to use iterator properly");
 		return N;
 	}
 	public static boolean isNeighbor(Node n1,Node n2,Graph g)
 	{
 		boolean b1,b2;
 		Edge e1=g.getEdge(n2, n1);
 		try
 		{
 			e1.toString();
 			b1= true;
 		}
 		catch(NullPointerException NPE1)
 		{
 			b1= false;
 		}
 		Edge e2=g.getEdge(n1, n2);
 		try
 		{
 			e2.toString();
 			b2= true;
 		}
 		catch(NullPointerException NPE1)
 		{
 			b2= false;
 		}
 		return b1||b2;
 	}
 	public static Graph makeSample()
 	{
 		Graph g =new Graph();
 		Table nodeData=new Table();
 		Table edgeData=new Table();
 		nodeData.addColumn("n1",int.class);
 		edgeData.addColumn("n2",int.class);
 		Node n;
 		for(int i=0;i<10;i++)
 		{
 			n=g.addNode();
// 		n.set("n1", i);
 		}
 		connect(0,1,2,g);
 		connect(8,1,9,g);
 		connect(8,2,3,g);
 		g.addEdge(0,9);g.addEdge(2,9);
 		return g;
 	}
 	public static void connect(int n1,int n2,int n3,Graph g)
 	{
 		if(!isNeighbor(g.getNode(n3),g.getNode(n2),g))
 			g.addEdge(n3,n2);
 		if(!isNeighbor(g.getNode(n3),g.getNode(n1),g))
 			g.addEdge(n1,n3);
 		if(!isNeighbor(g.getNode(n1),g.getNode(n2),g))
 			g.addEdge(n2,n1);
 	}
 	public static void hist(double []A,String fileName)
 	{
 		try{
 		PrintWriter Pr=new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
 		for(int i=0;i<A.length;i++)
 			Pr.println(A[i]);
 		Pr.close();
 		}
 		catch (IOException IOE)
 		{
 			System.out.println("Error in generating file "+fileName+IOE.getMessage());
 		}
 	}
 	public static void visGraph(String nType,Graph g,String name)
 	{
 		Visualization v=new Visualization();
 		v.add(name,g);
 	}
 	public static void vG(Graph graph)
 	{
        Visualization vis = new Visualization();
        /* vis is the main object that will run the visualization */
        vis.add("socialnet", graph);
        /* add our data to the visualization */

        // 3. setup the renderers and the render factory

        // labels for name
       //LabelRenderer nameLabel = new LabelRenderer();
        //nameLabel.setTextField();
        //nameLabel.setHorizontalPadding(10);
        //nameLabel.setRoundedCorner(8, 8);
        /* nameLabel decribes how to draw the data elements labeled as "name" */

        // create the render factory
        //vis.setRendererFactory(new DefaultRendererFactory(nameLabel));
        // 4. process the actions

        int[] shapepalette = new int[]{4, 3, 2};  
        // colour palette for nominal data type
        int[] palette = new int[]{ColorLib.rgb(255, 0, 0), ColorLib.rgb(0, 0, 255),ColorLib.rgb(0, 255, 0)};
        /* ColorLib.rgb converts the colour values to integers */
        int[] palette1 = new int[]{ColorLib.rgb(0, 255, 255), ColorLib.rgb(0, 0, 0)};
        DataShapeAction sh = new DataShapeAction("socialnet.nodes", "value", shapepalette);
        
        // map data to colours in the palette
       DataColorAction fill = new DataColorAction("socialnet.nodes", "value", Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
        /* fill describes what colour to draw the graph based on a portion of the data */

        // node text
        ColorAction text = new ColorAction("socialnet.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0));
        /* text describes what colour to draw the text */

        // edge
        DataColorAction edges = new DataColorAction("socialnet.edges", "type", Constants.NOMINAL, VisualItem.STROKECOLOR, palette1);
        /* edge describes what colour to draw the edges */

        // combine the colour assignments into an action list
        ActionList colour = new ActionList();
        colour.add(sh);
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
        display.addControlListener(new Controller());
        // 6. launch the visualizer in a JFrame

        JFrame frame = new JFrame("prefuse");
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
}

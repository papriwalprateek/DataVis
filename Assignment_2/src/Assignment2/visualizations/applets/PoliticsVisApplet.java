package Assignment2.visualizations.applets;

import prefuse.util.ui.JPrefuseApplet;

public class PoliticsVisApplet extends JPrefuseApplet {
	public static String file_name = "/home/ankit/Desktop/CSP301/DataVis/Assignment_2/data/MPTrack-15.xls";
    public void init() {
        this.setContentPane(
            Assignment2.visualizations.PoliticsVis.demo(file_name, "label"));
    }   
} 



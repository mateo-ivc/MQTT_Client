package gui;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import application.Singleton;
import data.DataProcessing;

public class DrawChart {
	Singleton singleton = Singleton.getInstance();
	private HashMap<String, TimeSeriesCollection> collection;
	DataProcessing data = new DataProcessing();
	ChartPanel cp;

	public void lineChart(String topic) {
		this.collection = singleton.data.getCollection();
		XYDataset dataset = collection.get(topic);
		// Create chart
		JFreeChart chart = ChartFactory.createTimeSeriesChart(topic, // Chart
				"Date", // X-Axis Label
				"Value", // Y-Axis Label
				dataset);

		// Changes background color
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(255, 228, 196));

		cp = new ChartPanel(chart);
		cp.setBorder(BorderFactory.createLineBorder(Color.black));
		singleton.gui.graphPanel.add(cp);
		cp.setSize(singleton.gui.graphPanel.getSize());
	}

}

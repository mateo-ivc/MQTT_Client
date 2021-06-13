package application;

import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DrawChart extends JFrame {
	private static final long serialVersionUID = 1L;
	Singleton singleton = Singleton.getInstance();
	ChartPanel cp;
	private double x;
	private double y;
	private double z;
	private double time;

	XYSeries series1 = new XYSeries("X");
	XYSeries series2 = new XYSeries("Y");
	XYSeries series3 = new XYSeries("Z");
	XYSeries temp = new XYSeries("Temperature");
	XYSeries pres = new XYSeries("Pressure");
	XYSeries hum = new XYSeries("Humidity");

	public void lineChart(String topic) {
		System.out.println(topic);
		// we have to set them 0 so the values doesn't override the other charts
		this.x = 0;
		this.y = 0;
		this.z = 0;
		XYDataset dataset = setDataSet(topic);
		// create chart
		JFreeChart chart = ChartFactory.createXYLineChart(topic, "X-Axis", "Y-Axis", dataset, PlotOrientation.VERTICAL,
				true, true, false);
		cp = new ChartPanel(chart);

		// displaying chart on graphpanel
		singleton.gui.graphPanel.add(cp);
		cp.setSize(singleton.gui.graphPanel.getSize());
//		// setting the ranges and space between every tick on the axis
//		XYPlot plot = (XYPlot) chart.getPlot();
//		plot.setDomainCrosshairVisible(true);
//		plot.setRangeCrosshairVisible(true);
//
//		XYItemRenderer renderer = plot.getRenderer();
//		renderer.setSeriesPaint(0, Color.blue);
//
//		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
//		domain.setRange(0.00, 60);
//		domain.setTickUnit(new NumberTickUnit(2));
//		domain.setVerticalTickLabels(true);
//
//		NumberAxis range = (NumberAxis) plot.getRangeAxis();
//		range.setRange(-30, 30);
//		range.setTickUnit(new NumberTickUnit(5));
	}

	public XYDataset setDataSet(String topic) {
		XYSeriesCollection dataset = new XYSeriesCollection();

		// adding values to the correct dataset
		switch (topic) {
		case "Temperature":
			temp.add(time, x);
			dataset.addSeries(temp);
			break;
		case "Pressure":
			pres.add(time, x);
			dataset.addSeries(pres);
			break;
		case "Humidity":
			hum.add(time, x);
			dataset.addSeries(hum);
			break;
		case "Accelleration":
			series1.add(time, x);
			series2.add(time, y);
			series3.add(time, z);
			dataset.addSeries(series1);
			dataset.addSeries(series2);
			dataset.addSeries(series3);
			break;
		case "Gyrodata":
			series1.add(time, x);
			series2.add(time, y);
			series3.add(time, z);
			dataset.addSeries(series1);
			dataset.addSeries(series2);
			dataset.addSeries(series3);
			break;
		case "Magdate":
			series1.add(time, x);
			series2.add(time, y);
			series3.add(time, z);
			dataset.addSeries(series1);
			dataset.addSeries(series2);
			dataset.addSeries(series3);
			break;
		default:
			break;
		}
		return dataset;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public void setX(double x) {
		System.out.println("hi");
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

}

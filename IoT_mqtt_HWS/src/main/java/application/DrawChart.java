package application;

import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DrawChart extends JFrame {
	private static final long serialVersionUID = 1L;
	Singleton singleton = Singleton.getInstance();
	ChartPanel cp;
	private double x;
	private double y;

	private XYSeries temp = new XYSeries("Temperature");
	private XYSeries pres = new XYSeries("Pressure");
	private XYSeries hum = new XYSeries("Humidity");

	public void lineChart(String topic) {
		System.out.println(topic);
		XYDataset dataset = setDataSet(topic);
		JFreeChart chart = ChartFactory.createXYLineChart(topic, "X-Axis", "Y-Axis", dataset, PlotOrientation.VERTICAL,
				true, true, false);
		cp = new ChartPanel(chart);
		singleton.gui.graphPanel.add(cp);
		cp.setSize(singleton.gui.graphPanel.getSize());
	}

	public XYDataset setDataSet(String topic) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		System.out.println("test");
		switch (topic) {
		case "Temperature":
			temp.add(x, y);
			dataset.addSeries(temp);
			break;
		case "Pressure":
			pres.add(x, y);
			dataset.addSeries(pres);
			break;
		case "Humidity":
			hum.add(x, y);
			dataset.addSeries(hum);
			break;
		default:
			break;
		}
		return dataset;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

}

package Atom.JChart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class Chart extends JFrame {
	XYDataset dataset;
	private String Title, Xlable, Ylable;
	
	public Chart(String title, String xlable, String ylable) throws HeadlessException {
		Title = title;
		Xlable = xlable;
		Ylable = ylable;
	}
	
	public void spawn() {
		initUI();
		setVisible(true);
	}
	
	public static Color color = Color.gray, textColor = Color.white;
	
	public void initUI() {
		
		
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(color);
		setBackground(color);
		add(chartPanel);
		
		pack();
		setTitle(Title);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Override
	public void setVisible(boolean b) {
		if (b) initUI();
		super.setVisible(b);
	}
	
	
	public JFreeChart createChart(XYDataset dataset) {
		
		JFreeChart chart = ChartFactory.createXYLineChart(Title, Xlable, Ylable, dataset, PlotOrientation.VERTICAL, true, true, false);
		
		XYPlot plot = chart.getXYPlot();
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		
		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesStroke(0, new BasicStroke(1.0f));
		renderer.setSeriesPaint(1, Color.BLUE);
		renderer.setSeriesStroke(1, new BasicStroke(1.0f));
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(color);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);
		//chart.setBackgroundPaint(color);
		
		chart.getLegend().setFrame(BlockBorder.NONE);
		chart.setTitle(new TextTitle(Title, new Font("Serif", Font.BOLD, 18)));
		
		return chart;
	}
	
	public void setSeries(XYSeries... series) {
		XYSeriesCollection collection = new XYSeriesCollection();
		for (XYSeries s : series)
			collection.addSeries(s);
		setDataset(collection);
		
	}
	
	public void setDataset(XYDataset score) {
		dataset = score;
	}
}

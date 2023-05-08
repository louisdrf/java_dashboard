import java.awt.*;
import java.util.List;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

public class HistogramSpending extends ApplicationFrame {

    private List<DiagramCompany> clientList;

    public HistogramSpending(String title) {
        super(title);

        FileReaderDashboard dashboard = new FileReaderDashboard();
        clientList = dashboard.readClientsFromFile("dashboard.txt");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (DiagramCompany client : clientList) {
            dataset.addValue(client.getSpending(), "Amount", client.getName());
        }

        JFreeChart chart = ChartFactory.createBarChart("Client Spending", "Client", "Spending", dataset, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(Color.WHITE);
        Font font = new Font("Arial Rounded MT Bold", Font.PLAIN, 18);
        chart.getTitle().setFont(font);
        chart.getCategoryPlot().getDomainAxis().setLabelFont(font);
        chart.getCategoryPlot().getRangeAxis().setLabelFont(font);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.blue);
        plot.setBackgroundPaint(Color.white);
        CategoryAxis categoryAxis = plot.getDomainAxis();
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(650, 650));
        setContentPane(chartPanel);
    }


    public static void main(String[] args) {
        HistogramSpending demo = new HistogramSpending("Client Spending Histogram");
        demo.pack();
        demo.setVisible(true);
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class PieChartPresta {

    public static void main(String[] args) {

        List<DiagramCompany> prestaList;

        FileReaderDashboard dashboard = new FileReaderDashboard();
        prestaList = dashboard.readClientsFromFile("dashboard.txt");

        prestaList.sort(Comparator.comparing(DiagramCompany::getSpending).reversed());

        List<DiagramCompany> topFive = prestaList.subList(0, Math.min(prestaList.size(), 5));


        DefaultPieDataset dataset = new DefaultPieDataset();
        for (DiagramCompany presta : topFive) {
            dataset.setValue(presta.getName(), presta.getSpending());
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "Part de marché des principaux prestataires",
                 dataset,
                true,
                true,
                false
        );

        chart.getTitle().setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
        PiePlot plot = (PiePlot) chart.getPlot();
        Font font = new Font("Arial Rounded MT Bold", Font.PLAIN, 18);
        chart.getTitle().setFont(font);
        plot.setLabelGenerator(null);
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} €"));
        plot.setInteriorGap(0.04);

        ChartFrame frame = new ChartFrame("5 meilleurs prestataires de T&S", chart);
        frame.pack();
        frame.setVisible(true);
    }
}
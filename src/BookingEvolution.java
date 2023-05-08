import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.time.LocalDate;
import java.util.List;

public class BookingEvolution {

    public static void main(String[] args) {

            // on récupère les données du fichier texte pour le dataset

        FileReaderDashboard dashboard = new FileReaderDashboard();
        List<Bilan> bilanList = dashboard.readPrestasFromFile("dashboard.txt");

        double[] monthBookingCount = new double[12];

        for (Bilan bilan : bilanList) {
            String[] parts = bilan.getDate().split("-");
            int month = Integer.parseInt(parts[1]);
            monthBookingCount[month - 1] += bilan.getNbbooking();
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < 12; i++) {
            dataset.addValue((Number) monthBookingCount[i], "Nombre de réservations", (i+1));
        }

        //////////////////////////////////////////////////////////////////////

        JFreeChart chart = ChartFactory.createLineChart(
                "Evolution de la demande sur le site",
                "Mois de l'année",
                "Nombre de réservations",
                 dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartFrame frame = new ChartFrame("Evolution de la demande", chart);
        frame.pack();
        frame.setVisible(true);
    }
}
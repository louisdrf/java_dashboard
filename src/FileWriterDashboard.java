import java.io.IOException;
import java.io.PrintWriter;

public class FileWriterDashboard {
    public static void write(String mystring) {
        try {
            java.io.FileWriter dashboard = new java.io.FileWriter("dashboard.txt", true);
            PrintWriter out = new PrintWriter(dashboard);
            out.println(mystring);

            out.close();
            dashboard.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


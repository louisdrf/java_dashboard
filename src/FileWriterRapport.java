import java.io.IOException;
import java.io.PrintWriter;

public class FileWriterRapport {
    public static void write(String mystring) {
        try {
            java.io.FileWriter rapport = new java.io.FileWriter("rapport.txt", true);
            PrintWriter out = new PrintWriter(rapport);
            out.println(mystring);

            out.close();
            rapport.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


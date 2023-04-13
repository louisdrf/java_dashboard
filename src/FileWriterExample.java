import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWriterExample {
    public static void write(String mystring) {
        try {
            FileWriter writer = new FileWriter("rapport.txt", true);
            PrintWriter out = new PrintWriter(writer);
            out.println(mystring);

            out.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


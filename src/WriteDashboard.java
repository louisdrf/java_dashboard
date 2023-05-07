import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
Des statistiques exhaustives sur les prestataires et les clients (CA,
collaborateurs, etc.), avec un zoom sur les 5 plus importants prestataires et
les 5 clients les plus importants.
■ Avec histogrammes et camemberts nécessaires
○ Représenter une timeline annuelle pour observer les variations de
demandes et de CA au fur et à mesure de l’année. Observer les pics (min et
max) et saisonnalités.
 */

public class WriteDashboard {
    public static void main(String[] args) {

        Connection conn = null;

        Set<CompanyClient> clientCompaniesList = new HashSet<>();    // liste des entreprises clientes
        Set<PrestaCompany> prestaCompaniesList = new HashSet<>();   // liste des entreprises prestataires

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://141.94.76.71/togetherdb?" +                           // CONNEXION BDD
                    "user=root&password=(u4bZ*=b43Fud9hB@<p>");


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            FileWriterDashboard.write("Rapport généré le : " + dtf.format(LocalDateTime.now()));                  // AFFICHE HEURE DE GENERATION DU RAPPORT


            // RECUPERER LA LISTE DES ENTREPRISES CLIENTES

            Statement clients_statement = conn.createStatement();
            ResultSet clients = clients_statement.executeQuery("select spentAmount from users");
            while(clients.next()) {
                CompanyClient client = new CompanyClient();
                client.setSpentAmount(clients.getInt("spentAmount"));
                client.setName(clients.getString("company"));

                clientCompaniesList.add(client);
            }

            FileWriterDashboard.write("liste des clients\n\n");


            for(CompanyClient company : clientCompaniesList) {

                FileWriterDashboard.write("\nmontant depensé par : " + company.getName() + " -> " + company.getSpentAmount());
            }

            ///////////////////////////////////////


            // RECUPERER LA LISTE DES ENTREPRISES PRESTATAIRES ET LEUR CA

            Statement prestaCompany_statement = conn.createStatement();
            ResultSet companies = prestaCompany_statement.executeQuery("select company,income from activity");
            while(companies.next()) {

                PrestaCompany company = new PrestaCompany();
                company.setName(companies.getString("company"));
                company.setSpentAmount(companies.getInt("income"));

                prestaCompaniesList.add(company);
            }

            FileWriterDashboard.write("liste des presta\n\n");

            for(PrestaCompany company : prestaCompaniesList) {

                FileWriterDashboard.write("\nmontant depensé par : " + company.getName() + " -> " + company.getSpentAmount());
            }



        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

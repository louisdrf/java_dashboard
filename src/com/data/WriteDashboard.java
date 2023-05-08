package com.data;
import com.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
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
        Set<Bilan> bookingList = new HashSet<>();   // liste des reservations dans le temps

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://141.94.76.71/togetherdb?" +                           // CONNEXION BDD
                    "user=root&password=(u4bZ*=b43Fud9hB@<p>");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            FileWriterDashboard.write("Generating time : " + dtf.format(LocalDateTime.now()));                  // AFFICHE HEURE DE GENERATION DU RAPPORT

            // RECUPERER LA LISTE DES ENTREPRISES CLIENTES
            Statement clients_statement = conn.createStatement();
            ResultSet clients = clients_statement.executeQuery(
                    "SELECT company, SUM(spentAmount) AS totalSpentAmount\n" +
                    "FROM users\n" +
                    "GROUP BY company;");



            int turnoverClient = 0;

            while(clients.next()) {
                CompanyClient client = new CompanyClient();
                client.setSpentAmount(clients.getInt("totalSpentAmount"));
                client.setName(clients.getString("company"));
                turnoverClient+=client.getSpentAmount();
                clientCompaniesList.add(client);
            }
            FileWriterDashboard.write("\nClients\n");
            for(CompanyClient company : clientCompaniesList) {
                FileWriterDashboard.write("client:" + company.getName() + "/" + company.getSpentAmount());
            }

            // RECUPERER LA LISTE DES ENTREPRISES PRESTATAIRES ET LEUR REVENU

            Statement prestaCompany_statement = conn.createStatement();
            ResultSet companies = prestaCompany_statement.executeQuery("select distinct company,income from activity");
            while(companies.next()) {

                PrestaCompany company = new PrestaCompany();
                company.setName(companies.getString("company"));
                company.setSpentAmount(companies.getInt("income"));
                prestaCompaniesList.add(company);
            }
            FileWriterDashboard.write("\nPresta\n");
            for(PrestaCompany company : prestaCompaniesList) {
                FileWriterDashboard.write("presta:" + company.getName() + "/" + company.getSpentAmount());
            }
            FileWriterDashboard.write("\nCA:" + turnoverClient);

 /*
 RECUPERATION DE L'EVOLUTION DE LA DEMANDE SUR LE SITE
 */


            // RECUPERER LA DEMANDE SUR LE SITE

            Statement nbbooking_statement = conn.createStatement();
            ResultSet bookings = nbbooking_statement.executeQuery
                    (
                            "SELECT booking.idActivity, booking.dateBooking, COUNT(*) AS total_bookings, activity.name AS activity_name\n" +
                    "FROM booking\n" +
                    "INNER JOIN activity ON booking.idActivity = activity.idActivity\n" +
                    "GROUP BY booking.idActivity, booking.dateBooking; "
                    );

            while(bookings.next()) {

                Bilan bilan = new Bilan();
                bilan.setNbbooking(bookings.getInt("total_bookings"));
                bilan.setDate(bookings.getString("dateBooking"));
                bookingList.add(bilan);

                FileWriterDashboard.write("date:" + bilan.getDate() + "/" + bilan.getNbbooking());
            }




            FileWriterDashboard.write("\n-------------------------------------------------------------------------------------------------------\n\n\n");


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

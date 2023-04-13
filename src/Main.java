import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
1 / liste des entreprises presta
2 / liste des entreprises clientes
3 / nbSeats pour chaque évènement
4 / montant de la prestation
5 / date de la prestation
// scene builder

ex :
RAPPORT DE DONNEES
entreprise presta 1 : liste des clients
évènements proposés : liste des évènements pour l'entreprise presta1 avec montant + date + NbSeats

entreprise presta 2 : liste des clients
évènements proposés : liste des évènements pour l'entreprise presta2 avec montant + date + NbSeats

 */
public class Main {
    public static void main(String[] args) {

        Connection conn = null;
        int i = 1;
        int j = 0;

        List<Activity> activityList = new ArrayList<Activity>();                 // liste des activités
        List<Client> clientList = new ArrayList<Client>();                       // liste des clients
        Set<String> clientCompaniesList = new HashSet<String>();                 // liste des adresses mails entreprises clientes donc String
        Set<PrestaCompany> prestaCompaniesList = new HashSet<PrestaCompany>();   // liste des entreprises prestataires

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn =
                    DriverManager.getConnection("jdbc:mysql://141.94.76.71/togetherdb?" +                           // CONNEXION BDD
                            "user=root&password=(u4bZ*=b43Fud9hB@<p>");


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            FileWriterExample.write("Rapport généré le : " + dtf.format(LocalDateTime.now()));                  // AFFICHE HEURE DE GENERATION DU RAPPORT


            // RECUPERER LA LISTE DES CLIENTS
            Statement clients_statement = conn.createStatement();
            ResultSet clients = clients_statement.executeQuery("select distinct company,mail from booking");
            while(clients.next()) {
                Client client = new Client();
                client.setCompany(clients.getString("company"));
                client.setMail(clients.getString("mail"));

                clientList.add(client);
                clientCompaniesList.add(client.getCompany());
            }
            ///////////////////////////////////////

            // AFFICHER LA LISTE DES CLIENTS
            FileWriterExample.write("\nListe des clients :\n");

            for(Client client : clientList) {
                String line = "Client " + i + " : " + client.getMail() + " / Entreprise : " + client.getCompany();
                FileWriterExample.write(line);
                i++;
            }
            FileWriterExample.write("\nNombre de clients : " + clientList.size());


            // RECUPERER LA LISTE DES ENTREPRISES CLIENTES
            Statement clientCompany_statement = conn.createStatement();
            ResultSet clientcompanies = clientCompany_statement.executeQuery("select company from booking");
            while(clientcompanies.next()) {

                CompanyClient company = new CompanyClient();
                company.setName(clientcompanies.getString("company"));

                clientCompaniesList.add(company.getName());                                                             // ajoute le nom des entreprises à clientCompaniesList
            }
            ///////////////////////////////////////






            // RECUPERER LA LISTE DES ACTIVITES
            Statement activities_statement = conn.createStatement();
            ResultSet activities = activities_statement.executeQuery("select * from activity");
            while(activities.next()) {

                Activity activity = new Activity();

                activity.setId(activities.getInt("idActivity"));
                activity.setName(activities.getString("name"));                                              // INSTANCIE LES OBJETS activity DE LA TABLE activity en bdd
                activity.setCategory(activities.getString("category"));
                activity.setPricePerPerson(activities.getInt("pricePerPerson"));
                activity.setNumberOfSeats(activities.getInt("numberOfSeats"));
                activity.setCompany(activities.getString("company"));

                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("select dateBooking from booking where idActivity = " + activity.getId());
                if (resultSet.next()) {
                    Date date = resultSet.getDate("dateBooking");
                    activity.setActivityDate(date);
                }

                activityList.add(activity);
            }
            ///////////////////////////////////////





            // RECUPERER LA LISTE DES ENTREPRISES PRESTATAIRES
            Statement prestaCompany_statement = conn.createStatement();
            ResultSet companies = prestaCompany_statement.executeQuery("select company from activity");
            while(companies.next()) {

                PrestaCompany company = new PrestaCompany();
                company.setName(companies.getString("company"));

                prestaCompaniesList.add(company);
            }
            ///////////////////////////////////////





            // RECUPERER LA LISTE DES ACTIVITES POUR CHAQUE ENTREPRISE PRESTATAIRE
            for(PrestaCompany company : prestaCompaniesList) {

                List<Activity> activitiesForCompany = new ArrayList<Activity>();                                        //  UNE ENTREPRISE PEUT PROPOSER PLUSIEURS EVENEMENTS, DONC UNE LISTE

                    for(Activity activity : activityList) {                                                             // ON ITERE POUR CHAQUE ACTIVITE DANS LA LISTE GENERALE

                        if(activity.getCompany().equals(company.getName())) {
                                                                                                                        // SI L'ACTIVITE EST PROPOSEE PAR L'ENTREPRISE ACTUELLE DANS LA LISTE ON L'AJOUTE A LA LISTE D'ACTIVITES QUE L'ENTREPRISE PROPOSE
                            activitiesForCompany.add(activity);
                        }
                }
                company.setPrestaActivityList(activitiesForCompany);
            }




            // RECUPERER LISTE CLIENTS POUR CHAQUE ENTREPRISE PRESTATAIRE

            for (PrestaCompany prestaCompany : prestaCompaniesList) {

                Set<String> clientsForActivity = null;

                for (Activity activity : prestaCompany.getPrestaActivityList()) {

                    clientsForActivity = new HashSet<String>();

                    Statement clientStatement = conn.createStatement();
                    ResultSet clientsActivity = clientStatement.executeQuery("select registrantMail from booking where idActivity = " + activity.getId());

                    while (clientsActivity.next()) {
                        clientsForActivity.add(clientsActivity.getString("registrantMail"));
                    }
                }
                prestaCompany.setPrestaClientList(clientsForActivity);
            }






            // AFFICHE LISTE ENTREPRISES PRESTATAIRES + CLIENTS POUR CHAQUE ACTIVITE + MONTANT DE LA PRESTATION
            i = 1;
            j = 0;
            int nbClientsForActivity = 0;
            int currentAmountForActivity = 0;


            // Boucler sur la liste des entreprises prestataires
            for (PrestaCompany prestaCompany : prestaCompaniesList) {

                FileWriterExample.write("\n\nEntreprise prestataire " + i + " : " + prestaCompany.getName());

                // Afficher la liste des activités de l'entreprise prestataire
                List<Activity> prestaActivitiesList = prestaCompany.getPrestaActivityList();

                 FileWriterExample.write("Activités proposées :");

                        for (Activity activity : prestaActivitiesList) {
                            FileWriterExample.write("- " + activity.getName() + " (Prix : " + activity.getPricePerPerson() + ", Places : " + activity.getNumberOfSeats() + ") réservée le : " + activity.getActivityDate());

                                // Afficher la liste des clients pour chaque activité
                                Set<String> clientsForActivity = prestaCompany.getPrestaClientList(activity);
                                FileWriterExample.write("Liste des clients inscrits : ");

                                    // itérer pour le nombre de participants à une activité
                                    for (String client : clientsForActivity) {
                                        FileWriterExample.write("- " + client);
                                        nbClientsForActivity++;
                                    }
                            FileWriterExample.write("Clients inscrits à l'activité : " + nbClientsForActivity);
                            currentAmountForActivity = (nbClientsForActivity * activity.getPricePerPerson()); // calcul du montant actuel de la prestation
                            FileWriterExample.write("Montant actuel estimé de la prestation : " + currentAmountForActivity + "€");
                            nbClientsForActivity = 0;
                        }
                j++;
                i++;
            }
            FileWriterExample.write("\nNombre d'entreprise prestataires : " + j);
            FileWriterExample.write("\n----------------------------------------------------------------------------------\n\n\n");




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

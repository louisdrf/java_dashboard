import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class PrestaCompany {

    private String name;
    private int montantPresta;
    private int spentAmount;
    private int id;
    private String mail;
    private List<Activity> prestaActivityList = new ArrayList<Activity>(); // liste des activités proposées par l'entreprise
    private Set<String> prestaClientList = new HashSet<String>(); // liste des clients de l'entreprise


    public int getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(int spentAmount) {
        this.spentAmount = spentAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMontantPresta() {
        return montantPresta;
    }

    public void setMontantPresta(int montantPresta) {
        this.montantPresta = montantPresta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<Activity> getPrestaActivityList() {
        return prestaActivityList;
    }

    public void setPrestaActivityList(List<Activity> prestaActivityList) {
        this.prestaActivityList = prestaActivityList;
    }
        public Set<String> getPrestaClientList(Activity activity) {
            return prestaClientList;
        }

        public void setPrestaClientList(Set<String> clientList) {
            this.prestaClientList = clientList;
        }
    }

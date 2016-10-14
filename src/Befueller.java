import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denis on 14.10.2016.
 */
public class Befueller {

    private int anzahl = 0;
    private List<String> personenObjekte = new ArrayList<>();
    private List<String> adressObjekte = new ArrayList<>();
    private List<String> mailObjekte = new ArrayList<>();

    public Befueller(int anzahl) {
        this.anzahl = anzahl;
        befuellen();
    }

    private void befuellen() {
        for (int i = 0; i < anzahl; i++) {
            Person p = new Person();
            this.personenObjekte.add(i, p.getVorName() + " " + p.getNachName());
            this.adressObjekte.add(i, p.getStrasse() + " " + p.getWohnOrt());
            this.mailObjekte.add(i, p.geteMail());
        }
    }

    public List<String> getPersonenObjekte() {
        return personenObjekte;
    }

    public List<String> getAdressObjekte() {
        return adressObjekte;
    }

    public List<String> getMailObjekte() {
        return mailObjekte;
    }
}

import java.util.*;

/**
 * Created by Denis on 03.10.2016.
 */
public class Person {
    private HashSet<String> stringSet = new HashSet<String>();
    private Names nm = new Names();

    public Person() {
    }

    public HashSet<String> anzahlSchleife(int anzahl) {
        anzahl = anzahl;
        for (int i = 0; i < anzahl; i++) {
            this.stringSet.add(this.personErzeugen());
        }
        return this.stringSet;
    }

    private String personErzeugen() {
        StringBuilder p = new StringBuilder();

        String vorName = nm.vornamenErzeugen();
        String nachName = nm.nachNamenErzeugen();

        p.append(vorName);
        p.append("|");
        p.append(nachName);
        p.append("|");
        p.append(nm.strasseErzeugen());
        p.append("|");
        p.append(nm.hausNummerErzeugen());
        p.append("|");
        p.append(nm.wohnOrtErzeugen());
        p.append("|");
        p.append(nm.eMailErzeugen(vorName, nachName));

        return p.toString();
    }
}

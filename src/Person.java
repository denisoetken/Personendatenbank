/**
 * Created by Denis on 14.10.2016.
 */
public class Person {
    private String vorName = "";
    private String nachName = "";
    private String wohnOrt = "";
    private String strasse = "";
    private String hausnummer = "";
    private String eMail = "";
    private Names nm = new Names();

    public Person() {
        this.vorName = nm.vornamenErzeugen();
        this.nachName = nm.nachNamenErzeugen();
        this.wohnOrt = nm.wohnOrtErzeugen();
        this.strasse = nm.strasseErzeugen();
        this.hausnummer = nm.hausNummerErzeugen();
        this.eMail = nm.eMailErzeugen(this.vorName, this.nachName);
    }

    public String getVorName() {
        return vorName;
    }

    public String getNachName() {
        return nachName;
    }

    public String getWohnOrt() {
        return wohnOrt;
    }

    public String getStrasse() {
        return strasse + " " + hausnummer;
    }

    public String geteMail() {
        return eMail;
    }
}

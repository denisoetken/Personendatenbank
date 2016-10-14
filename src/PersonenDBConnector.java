/**
 * Created by Denis on 14.10.2016.
 */
public class PersonenDBConnector {
    private String url="\\localhost";
    private String user;
    private String pass;

    public PersonenDBConnector(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public void dbAnlegen() {

    }

    public void speichern(Person p) {

    }

    private int adresseIdHolen() {
        int adressID = 0;

        return adressID;
    }

    private int adresseSpeichern(Person p) {
        int settedID = 0;

        return settedID;
    }

    public void disconnect() {

    }
}

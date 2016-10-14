import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Denis on 14.10.2016.
 */
public class PersonenDBConnector {
    private String url = "jdbc:postgresql://localhost/wetter";
    private String user = "postgres";
    private String pass = "password";
    private Connection con;

    public PersonenDBConnector(String url, String user, String pass) throws SQLException {
        this.url = url;
        this.user = user;
        this.pass = pass;

        this.con = DriverManager.getConnection(url, user, pass);

    }
//todo: SQL-Highlighting...
    public void dbAnlegen() throws SQLException {
con.createStatement();

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

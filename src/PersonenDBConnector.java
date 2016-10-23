import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Created by Denis on 14.10.2016.
 */
public class PersonenDBConnector {
    private String url;
    private String user;
    private String pass;
    private Connection con;
    private Properties konfig = new Properties();

    public PersonenDBConnector() throws SQLException {
        // Property - Einsatz
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("src/konfig.properties"));
            this.konfig.load(bfr);
            bfr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
      this.url = konfig.getProperty("db_url");
       this.user = konfig.getProperty("db_user");
     this.pass = konfig.getProperty("db_pass");
  //      this.url="jdbc:postgresql://localhost/personen";
    //    this.user="postgres";
      //  this.pass="password";
        this.con = DriverManager.getConnection(url, user, pass);
    }

    public void dbAnlegen() throws SQLException {
        Statement st = con.createStatement();
        st.addBatch("DROP TABLE IF EXISTS t_adresse CASCADE;");
        st.addBatch("CREATE TABLE t_adresse (pk_adressID SERIAL, strasse TEXT, stadt TEXT," +
                " CONSTRAINT PK_t_adresse_adressID PRIMARY KEY(pk_adressID));");
        st.addBatch("DROP TABLE IF EXISTS t_person CASCADE;");
        st.addBatch("CREATE TABLE t_person (pk_personID SERIAL, name TEXT," +
                " vorname TEXT, email TEXT, fk_t_adresse_adressID INTEGER, " +
                "CONSTRAINT PK_t_person_personID PRIMARY KEY(pk_personID)," +
                " CONSTRAINT fk_t_adresse_adressID FOREIGN KEY(fk_t_adresse_adressID) REFERENCES t_adresse(pk_adressID)" +
                " ON UPDATE CASCADE ON DELETE CASCADE);");
        st.executeBatch();

        st.close();
    }

    public void speichern(Person p) {
        int verknuepfung = 0;
        adresseSpeichern(p);
        try (PreparedStatement stPers = con.prepareStatement("INSERT INTO t_person(name, vorname, email, fk_t_adresse_adressID) VALUES (?,?,?,?);")) {
            stPers.setString(1, p.getNachName());
            stPers.setString(2, p.getVorName());
            stPers.setString(3, p.geteMail());
            verknuepfung = adresseIdHolen(p);
            stPers.setInt(4, verknuepfung);
            stPers.addBatch();
            stPers.executeBatch();
        } catch (SQLException e1) {
            e1.printStackTrace();
            System.out.println("Fehler beim Speichern");
        }
    }

    private int adresseIdHolen(Person p) {
        int zahl = 0;
        String sql = "SELECT pk_adressid FROM t_adresse WHERE stadt=? AND strasse=?;";
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, p.getWohnOrt());
            pstm.setString(2, p.getStrasse());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                zahl = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Die ID ist nicht zu holen");
        }
        return zahl;
    }

    private int adresseSpeichern(Person p) {
        int settedID = 0;
        try (PreparedStatement psta = con.prepareStatement("INSERT INTO t_adresse(stadt,strasse) VALUES (?,?);")) {
            if (adresseIdHolen(p) > 0) {
                settedID = adresseIdHolen(p);
            } else {
                psta.setString(1, p.getWohnOrt());
                psta.setString(2, p.getStrasse());
                settedID = adresseIdHolen(p);
            }
            psta.addBatch();
            psta.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Adresse konnte nicht gespeichert werden");
        }
        return settedID;
    }

    public void disconnect() throws SQLException {
        this.con.close();
    }
}

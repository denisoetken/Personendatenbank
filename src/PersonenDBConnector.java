import java.sql.*;

/**
 * Created by Denis on 14.10.2016.
 */
public class PersonenDBConnector {
    private String url = "";
    private String user = "";
    private String pass = "";
    private Connection con;

    public PersonenDBConnector(String url, String user, String pass) throws SQLException {
        this.url = url;
        this.user = user;
        this.pass = pass;
//        Herstellen der Connection
        try {
            this.con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Die Verbindung konnte nicht hergestellt werden!");
        }
    }

    public void dbAnlegen() throws SQLException {
        Statement st = con.createStatement();
//        Create if not exists wäre auch möglich
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
//        Befüllen der Personenattribute mit den Getter Methoden
        try (PreparedStatement stPers = con.prepareStatement("INSERT INTO t_person(name, vorname, email, fk_t_adresse_adressID) VALUES (?,?,?,?);")) {
            stPers.setString(1, p.getNachName());
            stPers.setString(2, p.getVorName());
            stPers.setString(3, p.geteMail());
//        Verknüpfung ist der Fremdschlüssel aus der Adresstabelle (siehe IDHolen)
            verknuepfung = adresseIdHolen(p);
            stPers.setInt(4, verknuepfung);
//           Endgültige Ausführung des preparedStatements
            stPers.addBatch();
            stPers.executeBatch();
        } catch (SQLException e1) {
            e1.printStackTrace();
            System.out.println("Fehler beim Speichern der Person");
        }
    }

    private int adresseIdHolen(Person p) {
        int zahl = 0;
        String sql = "SELECT pk_adressid FROM t_adresse WHERE stadt=? AND strasse=?;";
//        Abfrage welche Adresse derjenigen entspricht, die der Person zugeordnet ist
//        und Rücklieferung der ID
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, p.getWohnOrt());
            pstm.setString(2, p.getStrasse());
            ResultSet rs = pstm.executeQuery();
//            Kann nur 1 Ergbenis liefern, daher ID des 1. Ergebnisses zurückliefern
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
        int settedID = adresseIdHolen(p);
        try (PreparedStatement psta = con.prepareStatement("INSERT INTO t_adresse(stadt,strasse) VALUES (?,?);")) {
//         Die ID wird mit 0 initialisiert, dieser Wert ändert sich nur, wenn es die Adresse schon gibt
//         , sonst liefert IDHolen ja nichts zurück und die Variable wird nach der Speicherung geholt
//            if (adresseIdHolen(p) > 0) {
//                settedID = adresseIdHolen(p);
//            } else {
            if (!(settedID != 0)) {
                psta.setString(1, p.getWohnOrt());
                psta.setString(2, p.getStrasse());
                psta.addBatch();
                psta.executeBatch();
                settedID = adresseIdHolen(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Adresse konnte nicht gespeichert werden");
        }
        return settedID;
    }

    public void disconnect() throws SQLException {
        this.con.close();
    }

    // Test, ob es doppelte Adressen gibt:
    public void test1() throws SQLException {
        PreparedStatement testST1 = con.prepareStatement("SELECT a.strasse, a.stadt FROM t_adresse AS a" +
                " GROUP BY a.strasse, a.stadt HAVING count(*)>1;");
        ResultSet testRes = testST1.executeQuery();
        while (testRes.next()) {
            System.out.println("Folgendce Adressen sind doppelt vorhanden:");
            System.out.println("Strasse: " + testRes.getString(2) + "Stadt: " + testRes.getString(3));
        }
    }

    //    Stimmt die Anzahl der Zeilen?
    public void test2(int anzahl) throws SQLException {
        int testWert = 0;
        PreparedStatement testST2 = con.prepareStatement("SELECT count(pk_personID) FROM t_person;");
        ResultSet result = testST2.executeQuery();
        while (result.next()) {
            testWert = result.getInt(1);
        }
        if (testWert != anzahl) {
            System.out.println("Die Anzahl der Personen stimmt nicht.");
        }
//        Frage nach der Anzahl der Adressen macht hier keinen Sinn,
//        da doppelte Einträge ja nicht geschrieben werden und ich so die effektive Anzahl gar nicht kennen kann
    }
}


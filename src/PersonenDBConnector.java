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
        this.con = DriverManager.getConnection(url, user, pass);
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
            System.out.println("Fehler beim Speichern");
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
        int settedID = 0;
        try (PreparedStatement psta = con.prepareStatement("INSERT INTO t_adresse(stadt,strasse) VALUES (?,?);")) {
//         Die ID wird mit 0 initialisiert, dieser Wert ändert sich nur, wenn es die Adresse schon gibt
//         , sonst liefert IDHolen ja nichts zurück und die Variable wird nach der Speicherung geholt
            if (adresseIdHolen(p) > 0) {
                settedID = adresseIdHolen(p);
            } else {
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
}

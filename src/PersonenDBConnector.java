import java.sql.*;

/**
 * Created by Denis on 14.10.2016.
 */
public class PersonenDBConnector {
    private String url = "jdbc:postgresql://localhost/personen";
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
        Statement st = con.createStatement();
        //  t_adresse löschen, wenn vorhanden, dann erstellen
        st.addBatch("DROP TABLE IF EXISTS t_adresse CASCADE;");
        st.addBatch("CREATE TABLE t_adresse (pk_adressID SERIAL NOT NULL, strasse TEXT NOT NULL, stadt TEXT NOT NULL," +
                " CONSTRAINT PK_t_adresse_adressID PRIMARY KEY(pk_adressID));");
        //  t_person löschen, wenn vorhanden, dann erstellen
        st.addBatch("DROP TABLE IF EXISTS t_person CASCADE;");
        st.addBatch("CREATE TABLE t_person (pk_personID SERIAL NOT NULL, name TEXT NOT NULL," +
                " vorname TEXT NOT NULL, email TEXT, fk_t_adresse_adressID INTEGER, " +
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

            if (adresseIdHolen(p) > 0) {
                verknuepfung = adresseIdHolen(p);
            } else {
                verknuepfung = adresseSpeichern(p);
            }
            stPers.setInt(4, verknuepfung);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private int adresseIdHolen(Person p) {
        int zahl = 0;
        String sql = "SELECT id FROM adresse WHERE stadt=? AND strasse_hausnummer=?;";
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, p.getWohnOrt());
            pstm.setString(2, p.getStrasse());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                zahl = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zahl;
    }

    private int adresseSpeichern(Person p) {
        int settedID = 0;
        try (PreparedStatement psta = con.prepareStatement("INSERT INTO t_adresse(stadt,strasse) VALUES (?,?);")) {
            psta.setString(1, p.getWohnOrt());
            psta.setString(2, p.getStrasse());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return settedID;
    }

    public void disconnect() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

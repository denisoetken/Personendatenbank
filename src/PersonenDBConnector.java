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
//        con.createStatement("\c db personen;");
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
    }

    public void speichern(Person p) {
        int verknuepfung = 0;
        try (PreparedStatement stAdress = con.prepareStatement("INSERT INTO t_adresse(strasse, stadt) VALUES (?,?);")) {
            stAdress.setString(1, p.getStrasse());
            stAdress.setString(2, p.getWohnOrt());
            String query = "select pk_adressID from t_adresse where strasse = " + p.getStrasse() + " && stadt = " + p.getWohnOrt() + ";";
            Statement stID = con.createStatement();
            ResultSet res = stID.executeQuery(query);
            res.next();
            res.getInt(1);
            System.out.println(res.getInt(1));
            verknuepfung = Integer.parseInt(stID.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement stPers = con.prepareStatement("INSERT INTO t_person(name, vorname, email, fk_t_adresse_adressID) VALUES (?,?,?,?);")) {
            stPers.setString(1, p.getNachName());
            stPers.setString(2, p.getVorName());
            stPers.setString(3, p.geteMail());
            stPers.setInt(4, verknuepfung);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    // todo Ist die Person eindeutig, wenn alle Attribute (außer der ID) gleich sind?
    // todo In der echten Welt ja (EMail), aber in der DB eigentlich nicht
    private int adresseIdHolen(Person p) {
        //Statement st = con.createStatement("SELECT fk_t_adresse_adressID from t_person where name="+p.getNachName()+"&&);");
        int adressID = 0;
//        String query = "select fk_t_adresse_adressID from t_person where (name=" + p.getNachName() + " && vorname = " + p.getVorName() + " && email= " + p.geteMail() + ");";
//        adressID = con.createStatement("SELECT fk_t_adresse_adressID from t_person where (name=" + p.getNachName() + " && vorname=" + p.getVorName() + " && email=" + p.geteMail() + "));");
        return adressID;
    }

    private int adresseSpeichern(Person p) {
        int settedID = 0;

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

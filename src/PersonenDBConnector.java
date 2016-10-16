import java.sql.*;

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
        Statement st = con.createStatement();
        //  t_adresse löschen, wenn vorhanden, dann erstellen
        st.addBatch("DROP TABLE IF EXISTS t_adresse CASCADE;");
        st.addBatch("CREATE TABLE t_adresse (pk_adressID SERIAL NOT NULL, strasse TEXT NOT NULL, stadt TEXT NOT NULL," +
                " CONSTRAINT PK_t_adresse_adressID PRIMARY KEY(pk_adressID));");
        //  t_person löschen, wenn vorhanden, dann erstellen
        st.addBatch("DROP TABLE IF EXISTS t_person CASCADE;");
        st.addBatch("CREATE TABLE t_person (pk_personID SERIAL NOT NULL, name TEXT NOT NULL," +
                " vorname TEXT NOT NULL, email TEXT, fk_t_adresse_adressID TEXT, " +
                "CONSTRAINT PK_t_person_personID PRIMARY KEY(pk_personID)," +
                " CONSTRAINT fk_t_adresse_adressID FOREIGN KEY(fk_t_adresse_adressID) REFERENCES t_adresse(pk_adressID)" +
                " ON UPDATE CASCADE ON DELETE CASCADE);");
        st.executeBatch();
    }

    public void speichern(Person p) {
        int verknuepfung;
        // todo: id der adresse rausziehen und an person übergeben
        try {
            try (PreparedStatement stAdress = con.prepareStatement("INSERT INTO t_adresse(strasse, stadt) VALUES (?,?);")) {
                stAdress.setString(1, p.getStrasse());
                stAdress.setString(2, p.getWohnOrt());
                verknuepfung = stAdress.getResultSet("select pk_adressID");
                // todo: durchlaufen bis null vorm befüllen, dann +1 = neue id?
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement stPers = con.prepareStatement("INSERT INTO t_person(name, vorname, email, fk_t_adresse_adressID) VALUES (?,?,?,?);")) {
            stPers.setString(1, p.getNachName());
            stPers.setString(2, p.getVorName());
            stPers.setString(3, p.geteMail());
            stPers.setString(4, p.getNachName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int adresseIdHolen(Person p) {
        //Statement st = con.createStatement("SELECT fk_t_adresse_adressID from t_person where name="+p.getNachName()+"&&);");
        int adressID;
        String query = "select fk_t_adresse_adressID from t_person where (name=" + p.getNachName() + " && vorname = " + p.getVorName() + " && email= " + p.geteMail() + ");";

        adressID = con.createStatement("SELECT fk_t_adresse_adressID from t_person where (name=" + p.getNachName() + " && vorname=" + p.getVorName() + " && email=" + p.geteMail() + "));");


        return adressID;
    }

    private int adresseSpeichern(Person p) {
        int settedID = 0;

        return settedID;
    }

    public void disconnect() {

    }
}

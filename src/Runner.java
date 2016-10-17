import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Denis on 14.10.2016.
 */
public class Runner {
    public static void main(String[] args) {
        PersonenDBConnector db = null;
        try {
            db = new PersonenDBConnector("jdbc:postgresql://localhost/personen", "postgres", "password");
            db.dbAnlegen();
            db.speichern(new Person());
            db.test();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.disconnect();
        }
    }
}


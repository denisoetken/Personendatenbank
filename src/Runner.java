import java.sql.SQLException;

/**
 * Created by Denis on 14.10.2016.
 */
public class Runner {
    public static void main(String[] args) {
        PersonenDBConnector db = null;
        try {
            db = new PersonenDBConnector("jdbc:postgresql://localhost/personen", "postgres", "password");
            db.dbAnlegen();
            for (int i = 0; i < 10000; i++) {
                Person p = new Person();
                db.speichern(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                db.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Disconnect war nicht erfolgreich");
            }
        }
    }
}


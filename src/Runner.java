import java.sql.SQLException;

/**
 * Created by Denis on 14.10.2016.
 */
public class Runner {
    public static void main(String[] args) {
        PersonenDBConnector db = null;
        try {
//            Aufruf der Connection mit Übergabe der Parameter für die Anmeldung an der Datenbank
            db = new PersonenDBConnector("jdbc:postgresql://localhost/personen", "postgres", "password");
            db.dbAnlegen();
//            Schleife = Anzahl der Personen, die anzulegen sind
            for (int i = 0; i < 10000; i++) {
                Person p = new Person();
                db.speichern(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            Disconnect auf jeden Fall
            try {
                db.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Disconnect war nicht erfolgreich");
            }
        }
    }
}


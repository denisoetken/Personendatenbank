/**
 * Created by Denis on 14.10.2016.
 */
public class Runner {
    public static void main(String[] args) {
        Befueller bf = new Befueller(10);
        int lesen = 5;
        for (int i = 0; i < lesen; i++) {
            System.out.println("Name: " + bf.getPersonenObjekte().get(i));
            System.out.println("Adresse: " + bf.getAdressObjekte().get(i));
            System.out.println("Mail: " + bf.getMailObjekte().get(i));

        }
    }
}

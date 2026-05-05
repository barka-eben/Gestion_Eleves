import menu.MainMenu;

/**
 * Point d'entrée du programme.
 * C'est ici que tout commence quand on lance le programme.
 */
public class Main {
    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        menu.start();
    }
}

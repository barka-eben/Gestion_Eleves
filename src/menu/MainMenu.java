package menu;

import service.*;

import java.io.File;
import java.util.Scanner;

/**
 * Menu principal du programme.
 * C'est ici qu'on crée tous les services et tous les sous-menus.
 * Le Scanner est créé une seule fois ici et partagé partout.
 */
public class MainMenu {

    // Un seul Scanner pour tout le programme (lire les entrées clavier)
    private Scanner scanner;

    // Les services (gestion des données)
    private EleveService      eleveService;
    private ProfesseurService professeurService;
    private NoteService       noteService;
    private CoursService      coursService;

    // Les menus (gestion de l'affichage)
    private EleveMenu        eleveMenu;
    private ProfesseurMenu   professeurMenu;
    private CoursMenu        coursMenu;
    private NoteMenu         noteMenu;
    private StatistiquesMenu statsMenu;

    public MainMenu() {
        // Création du dossier "data" pour stocker les fichiers CSV
        new File("data").mkdirs();

        // On crée le scanner une seule fois
        scanner = new Scanner(System.in);

        // On crée les services (ils chargent les données CSV au démarrage)
        eleveService      = new EleveService();
        professeurService = new ProfesseurService();
        noteService       = new NoteService();
        coursService      = new CoursService();

        // On crée les menus en leur donnant les services dont ils ont besoin
        eleveMenu      = new EleveMenu(eleveService, scanner);
        professeurMenu = new ProfesseurMenu(professeurService, scanner);
        coursMenu      = new CoursMenu(coursService, professeurService, scanner);
        noteMenu       = new NoteMenu(noteService, eleveService, scanner);
        statsMenu      = new StatistiquesMenu(eleveService, professeurService, noteService, coursService, scanner);
    }

    /**
     * Démarre le programme et affiche le menu principal en boucle.
     */
    public void start() {
        afficherBanniere();

        boolean continuer = true;

        while (continuer) {
            System.out.println("\n========================================");
            System.out.println("         MENU PRINCIPAL");
            System.out.println("========================================");
            System.out.println("  1. Gestion des Eleves");
            System.out.println("  2. Gestion des Professeurs");
            System.out.println("  3. Gestion des Cours");
            System.out.println("  4. Gestion des Notes");
            System.out.println("  5. Statistiques");
            System.out.println("  0. Quitter");
            System.out.println("----------------------------------------");
            System.out.print("Votre choix : ");

            String saisie = scanner.nextLine().trim();

            switch (saisie) {
                case "1": eleveMenu.afficher();      break;
                case "2": professeurMenu.afficher(); break;
                case "3": coursMenu.afficher();      break;
                case "4": noteMenu.afficher();       break;
                case "5": statsMenu.afficher();      break;
                case "0":
                    System.out.println("Au revoir !");
                    continuer = false;
                    break;
                default:
                    System.out.println("Option invalide. Choisissez entre 0 et 5.");
            }
        }

        scanner.close();
    }

    private void afficherBanniere() {
        System.out.println("========================================");
        System.out.println("  SYSTEME DE GESTION D'ECOLE SECONDAIRE");
        System.out.println("========================================");
        System.out.println("  Donnees stockees dans : data/");
        System.out.println("  Fichiers : eleves.csv, professeurs.csv,");
        System.out.println("             cours.csv, notes.csv");
        System.out.println("========================================");
    }
}

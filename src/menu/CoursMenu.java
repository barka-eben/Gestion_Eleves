package menu;

import model.Cours;
import model.Professeur;
import service.CoursService;
import service.ProfesseurService;

import java.util.List;
import java.util.Scanner;

/**
 * Menu de gestion des cours.
 */
public class CoursMenu {

    private CoursService coursService;
    private ProfesseurService professeurService;
    private Scanner scanner;

    public CoursMenu(CoursService coursService, ProfesseurService professeurService, Scanner scanner) {
        this.coursService = coursService;
        this.professeurService = professeurService;
        this.scanner = scanner;
    }

    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n========================================");
            System.out.println("          GESTION DES COURS");
            System.out.println("========================================");
            System.out.println("  1. Lister tous les cours");
            System.out.println("  2. Cours d'une classe");
            System.out.println("  3. Cours d'un professeur");
            System.out.println("  4. Ajouter un cours");
            System.out.println("  5. Modifier un cours");
            System.out.println("  6. Supprimer un cours");
            System.out.println("  0. Retour au menu principal");
            System.out.println("----------------------------------------");
            System.out.print("Votre choix : ");

            String saisie = scanner.nextLine().trim();

            switch (saisie) {
                case "1": listerTous();       break;
                case "2": parClasse();        break;
                case "3": parProfesseur();    break;
                case "4": ajouter();          break;
                case "5": modifier();         break;
                case "6": supprimer();        break;
                case "0": continuer = false;  break;
                default:
                    System.out.println("Option invalide. Choisissez entre 0 et 6.");
            }
        }
    }

    private void listerTous() {
        List<Cours> cours = coursService.getTous();
        System.out.println("\n--- Liste des cours (" + cours.size() + ") ---");

        if (cours.isEmpty()) {
            System.out.println("Aucun cours enregistre.");
        } else {
            for (Cours c : cours) {
                System.out.println(c);
            }
        }
    }

    private void parClasse() {
        System.out.print("Classe (ex: 3A) : ");
        String classe = scanner.nextLine().trim();

        List<Cours> cours = coursService.trouverParClasse(classe);
        System.out.println("\n--- Cours de la classe " + classe + " (" + cours.size() + ") ---");

        if (cours.isEmpty()) {
            System.out.println("Aucun cours pour cette classe.");
        } else {
            for (Cours c : cours) {
                System.out.println(c);
            }
        }
    }

    private void parProfesseur() {
        System.out.print("ID du professeur : ");
        String profId = scanner.nextLine().trim();

        Professeur prof = professeurService.trouverParId(profId);
        if (prof == null) {
            System.out.println("ERREUR : Aucun professeur avec l'ID " + profId);
            return;
        }

        List<Cours> cours = coursService.trouverParProfesseur(profId);
        System.out.println("\n--- Cours de " + prof.prenom + " " + prof.nom + " (" + cours.size() + ") ---");

        if (cours.isEmpty()) {
            System.out.println("Ce professeur n'a aucun cours.");
        } else {
            for (Cours c : cours) {
                System.out.println(c);
            }
        }
    }

    private void ajouter() {
        System.out.println("\n--- Ajouter un cours ---");

        // On affiche les professeurs disponibles pour faciliter la saisie de l'ID
        List<Professeur> profs = professeurService.getTous();
        if (profs.isEmpty()) {
            System.out.println("ERREUR : Aucun professeur disponible. Ajoutez d'abord un professeur.");
            return;
        }

        System.out.println("Professeurs disponibles :");
        for (Professeur p : profs) {
            System.out.println("  [" + p.id + "] " + p.prenom + " " + p.nom + " - " + p.matiere);
        }

        System.out.print("Nom du cours : ");
        String nom = scanner.nextLine().trim();

        System.out.print("ID du professeur : ");
        String profId = scanner.nextLine().trim();

        // Vérification que le professeur existe
        if (professeurService.trouverParId(profId) == null) {
            System.out.println("ERREUR : Aucun professeur avec l'ID " + profId);
            return;
        }

        System.out.print("Classe (ex: 3A) : ");
        String classe = scanner.nextLine().trim();

        System.out.print("Horaire (ex: Lundi 08h-10h) : ");
        String horaire = scanner.nextLine().trim();

        System.out.print("Salle : ");
        String salle = scanner.nextLine().trim();

        if (nom.isEmpty() || classe.isEmpty()) {
            System.out.println("ERREUR : Le nom et la classe sont obligatoires.");
            return;
        }

        coursService.ajouter(nom, profId, classe, horaire, salle);
        System.out.println("Cours ajoute avec succes !");
    }

    private void modifier() {
        System.out.print("ID du cours a modifier : ");
        String id = scanner.nextLine().trim();

        Cours c = coursService.trouverParId(id);
        if (c == null) {
            System.out.println("ERREUR : Aucun cours avec l'ID " + id);
            return;
        }

        System.out.println("Cours actuel : " + c);
        System.out.println("(Appuyez sur Entree pour garder la valeur actuelle)");

        System.out.print("Nouveau nom [" + c.nom + "] : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Nouvel ID professeur [" + c.professeurId + "] : ");
        String profId = scanner.nextLine().trim();

        System.out.print("Nouvelle classe [" + c.classe + "] : ");
        String classe = scanner.nextLine().trim();

        System.out.print("Nouvel horaire [" + c.horaire + "] : ");
        String horaire = scanner.nextLine().trim();

        System.out.print("Nouvelle salle [" + c.salle + "] : ");
        String salle = scanner.nextLine().trim();

        coursService.modifier(
            id,
            nom.isEmpty()    ? c.nom          : nom,
            profId.isEmpty() ? c.professeurId  : profId,
            classe.isEmpty() ? c.classe        : classe,
            horaire.isEmpty()? c.horaire       : horaire,
            salle.isEmpty()  ? c.salle         : salle
        );

        System.out.println("Cours modifie avec succes !");
    }

    private void supprimer() {
        System.out.print("ID du cours a supprimer : ");
        String id = scanner.nextLine().trim();

        Cours c = coursService.trouverParId(id);
        if (c == null) {
            System.out.println("ERREUR : Aucun cours avec l'ID " + id);
            return;
        }

        System.out.println("Cours trouve : " + c);
        System.out.print("Confirmer la suppression ? (oui/non) : ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("oui")) {
            coursService.supprimer(id);
            System.out.println("Cours supprime.");
        } else {
            System.out.println("Suppression annulee.");
        }
    }
}

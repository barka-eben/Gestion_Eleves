package menu;

import model.Professeur;
import service.ProfesseurService;

import java.util.List;
import java.util.Scanner;

/**
 * Menu de gestion des professeurs.
 */
public class ProfesseurMenu {

    private ProfesseurService service;
    private Scanner scanner;

    public ProfesseurMenu(ProfesseurService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n========================================");
            System.out.println("       GESTION DES PROFESSEURS");
            System.out.println("========================================");
            System.out.println("  1. Lister tous les professeurs");
            System.out.println("  2. Rechercher un professeur");
            System.out.println("  3. Ajouter un professeur");
            System.out.println("  4. Modifier un professeur");
            System.out.println("  5. Supprimer un professeur");
            System.out.println("  0. Retour au menu principal");
            System.out.println("----------------------------------------");
            System.out.print("Votre choix : ");

            String saisie = scanner.nextLine().trim();

            switch (saisie) {
                case "1": listerTous(); break;
                case "2": rechercher(); break;
                case "3": ajouter();    break;
                case "4": modifier();   break;
                case "5": supprimer();  break;
                case "0": continuer = false; break;
                default:
                    System.out.println("Option invalide. Choisissez entre 0 et 5.");
            }
        }
    }

    private void listerTous() {
        List<Professeur> profs = service.getTous();
        System.out.println("\n--- Liste des professeurs (" + profs.size() + ") ---");

        if (profs.isEmpty()) {
            System.out.println("Aucun professeur enregistre.");
        } else {
            for (Professeur p : profs) {
                System.out.println(p);
            }
        }
    }

    private void rechercher() {
        System.out.print("Entrez un nom, prenom ou matiere : ");
        String terme = scanner.nextLine().trim();

        List<Professeur> resultats = service.rechercher(terme);
        System.out.println("\n--- Resultats (" + resultats.size() + ") ---");

        if (resultats.isEmpty()) {
            System.out.println("Aucun resultat pour : " + terme);
        } else {
            for (Professeur p : resultats) {
                System.out.println(p);
            }
        }
    }

    private void ajouter() {
        System.out.println("\n--- Ajouter un professeur ---");

        System.out.print("Nom : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Prenom : ");
        String prenom = scanner.nextLine().trim();

        System.out.print("Matiere enseignee : ");
        String matiere = scanner.nextLine().trim();

        System.out.print("Email : ");
        String email = scanner.nextLine().trim();

        System.out.print("Telephone : ");
        String telephone = scanner.nextLine().trim();

        if (nom.isEmpty() || prenom.isEmpty() || matiere.isEmpty()) {
            System.out.println("ERREUR : Le nom, le prenom et la matiere sont obligatoires.");
            return;
        }

        service.ajouter(nom, prenom, matiere, email, telephone);
        System.out.println("Professeur ajoute avec succes !");
    }

    private void modifier() {
        System.out.print("ID du professeur a modifier : ");
        String id = scanner.nextLine().trim();

        Professeur p = service.trouverParId(id);
        if (p == null) {
            System.out.println("ERREUR : Aucun professeur avec l'ID " + id);
            return;
        }

        System.out.println("Professeur actuel : " + p);
        System.out.println("(Appuyez sur Entree pour garder la valeur actuelle)");

        System.out.print("Nouveau nom [" + p.nom + "] : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Nouveau prenom [" + p.prenom + "] : ");
        String prenom = scanner.nextLine().trim();

        System.out.print("Nouvelle matiere [" + p.matiere + "] : ");
        String matiere = scanner.nextLine().trim();

        System.out.print("Nouvel email [" + p.email + "] : ");
        String email = scanner.nextLine().trim();

        System.out.print("Nouveau telephone [" + p.telephone + "] : ");
        String telephone = scanner.nextLine().trim();

        service.modifier(
            id,
            nom.isEmpty()      ? p.nom      : nom,
            prenom.isEmpty()   ? p.prenom   : prenom,
            matiere.isEmpty()  ? p.matiere  : matiere,
            email.isEmpty()    ? p.email    : email,
            telephone.isEmpty()? p.telephone: telephone
        );

        System.out.println("Professeur modifie avec succes !");
    }

    private void supprimer() {
        System.out.print("ID du professeur a supprimer : ");
        String id = scanner.nextLine().trim();

        Professeur p = service.trouverParId(id);
        if (p == null) {
            System.out.println("ERREUR : Aucun professeur avec l'ID " + id);
            return;
        }

        System.out.println("Professeur trouve : " + p);
        System.out.print("Confirmer la suppression ? (oui/non) : ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("oui")) {
            service.supprimer(id);
            System.out.println("Professeur supprime.");
        } else {
            System.out.println("Suppression annulee.");
        }
    }
}

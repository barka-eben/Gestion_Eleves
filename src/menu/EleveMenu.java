package menu;

import model.Eleve;
import service.EleveService;

import java.util.List;
import java.util.Scanner;

/**
 * Menu de gestion des élèves.
 * Affiche les options et lit les saisies de l'utilisateur.
 */
public class EleveMenu {

    private EleveService service;
    private Scanner scanner;

    public EleveMenu(EleveService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    /**
     * Affiche le menu et répète jusqu'à ce que l'utilisateur choisisse "Retour".
     */
    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n========================================");
            System.out.println("         GESTION DES ELEVES");
            System.out.println("========================================");
            System.out.println("  1. Lister tous les eleves");
            System.out.println("  2. Rechercher un eleve");
            System.out.println("  3. Afficher les eleves d'une classe");
            System.out.println("  4. Ajouter un eleve");
            System.out.println("  5. Modifier un eleve");
            System.out.println("  6. Supprimer un eleve");
            System.out.println("  0. Retour au menu principal");
            System.out.println("----------------------------------------");
            System.out.print("Votre choix : ");

            String saisie = scanner.nextLine().trim();

            switch (saisie) {
                case "1": listerTous();       break;
                case "2": rechercher();       break;
                case "3": afficherParClasse();break;
                case "4": ajouter();          break;
                case "5": modifier();         break;
                case "6": supprimer();        break;
                case "0": continuer = false;  break;
                default:
                    System.out.println("Option invalide. Choisissez entre 0 et 6.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // OPTIONS DU MENU
    // -------------------------------------------------------------------------

    private void listerTous() {
        List<Eleve> eleves = service.getTous();
        System.out.println("\n--- Liste des eleves (" + eleves.size() + ") ---");

        if (eleves.isEmpty()) {
            System.out.println("Aucun eleve enregistre.");
        } else {
            for (Eleve e : eleves) {
                System.out.println(e);
            }
        }
    }

    private void rechercher() {
        System.out.print("Entrez un nom, prenom ou ID : ");
        String terme = scanner.nextLine().trim();

        List<Eleve> resultats = service.rechercher(terme);
        System.out.println("\n--- Resultats (" + resultats.size() + ") ---");

        if (resultats.isEmpty()) {
            System.out.println("Aucun resultat pour : " + terme);
        } else {
            for (Eleve e : resultats) {
                System.out.println(e);
            }
        }
    }

    private void afficherParClasse() {
        // On affiche d'abord les classes existantes pour aider l'utilisateur
        List<String> classes = service.getClassesDistinctes();
        if (classes.isEmpty()) {
            System.out.println("Aucune classe disponible.");
            return;
        }

        System.out.println("Classes disponibles : " + classes);
        System.out.print("Entrez le nom de la classe : ");
        String classe = scanner.nextLine().trim();

        List<Eleve> eleves = service.trouverParClasse(classe);
        System.out.println("\n--- Eleves de la classe " + classe + " (" + eleves.size() + ") ---");

        if (eleves.isEmpty()) {
            System.out.println("Aucun eleve dans cette classe.");
        } else {
            for (Eleve e : eleves) {
                System.out.println(e);
            }
        }
    }

    private void ajouter() {
        System.out.println("\n--- Ajouter un eleve ---");

        System.out.print("Nom : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Prenom : ");
        String prenom = scanner.nextLine().trim();

        System.out.print("Date de naissance (JJ/MM/AAAA) : ");
        String dateNaissance = scanner.nextLine().trim();

        System.out.print("Classe (ex: 3A, 4B) : ");
        String classe = scanner.nextLine().trim();

        System.out.print("Email : ");
        String email = scanner.nextLine().trim();

        System.out.print("Telephone : ");
        String telephone = scanner.nextLine().trim();

        // Vérification des champs obligatoires
        if (nom.isEmpty() || prenom.isEmpty() || classe.isEmpty()) {
            System.out.println("ERREUR : Le nom, le prenom et la classe sont obligatoires.");
            return;
        }

        service.ajouter(nom, prenom, dateNaissance, classe, email, telephone);
        System.out.println("Eleve ajoute avec succes !");
    }

    private void modifier() {
        System.out.print("ID de l'eleve a modifier : ");
        String id = scanner.nextLine().trim();

        Eleve e = service.trouverParId(id);
        if (e == null) {
            System.out.println("ERREUR : Aucun eleve avec l'ID " + id);
            return;
        }

        System.out.println("Eleve actuel : " + e);
        System.out.println("(Appuyez sur Entree pour garder la valeur actuelle)");

        System.out.print("Nouveau nom [" + e.nom + "] : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Nouveau prenom [" + e.prenom + "] : ");
        String prenom = scanner.nextLine().trim();

        System.out.print("Nouvelle date de naissance [" + e.dateNaissance + "] : ");
        String dateNaissance = scanner.nextLine().trim();

        System.out.print("Nouvelle classe [" + e.classe + "] : ");
        String classe = scanner.nextLine().trim();

        System.out.print("Nouvel email [" + e.email + "] : ");
        String email = scanner.nextLine().trim();

        System.out.print("Nouveau telephone [" + e.telephone + "] : ");
        String telephone = scanner.nextLine().trim();

        // Si l'utilisateur n'a rien tapé, on garde l'ancienne valeur
        service.modifier(
            id,
            nom.isEmpty()          ? e.nom          : nom,
            prenom.isEmpty()       ? e.prenom       : prenom,
            dateNaissance.isEmpty()? e.dateNaissance : dateNaissance,
            classe.isEmpty()       ? e.classe       : classe,
            email.isEmpty()        ? e.email        : email,
            telephone.isEmpty()    ? e.telephone    : telephone
        );

        System.out.println("Eleve modifie avec succes !");
    }

    private void supprimer() {
        System.out.print("ID de l'eleve a supprimer : ");
        String id = scanner.nextLine().trim();

        Eleve e = service.trouverParId(id);
        if (e == null) {
            System.out.println("ERREUR : Aucun eleve avec l'ID " + id);
            return;
        }

        System.out.println("Eleve trouve : " + e);
        System.out.print("Confirmer la suppression ? (oui/non) : ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("oui")) {
            service.supprimer(id);
            System.out.println("Eleve supprime.");
        } else {
            System.out.println("Suppression annulee.");
        }
    }
}

package model;

/**
 * Représente un élève de l'école.
 * Chaque élève a un identifiant unique, un nom, un prénom, etc.
 */
public class Eleve {

    // Les informations d'un élève
    public String id;
    public String nom;
    public String prenom;
    public String dateNaissance;
    public String classe;
    public String email;
    public String telephone;

    // Constructeur : crée un élève avec toutes ses informations
    public Eleve(String id, String nom, String prenom, String dateNaissance,
                 String classe, String email, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.classe = classe;
        this.email = email;
        this.telephone = telephone;
    }

    /**
     * Convertit l'élève en une ligne CSV pour l'enregistrer dans le fichier.
     * Exemple : EL0001,Tremblay,Marie,15/09/2008,4A,marie@email.ca,514-555-0101
     */
    public String versLigneCSV() {
        return id + "," + nom + "," + prenom + "," + dateNaissance + ","
             + classe + "," + email + "," + telephone;
    }

    /**
     * Crée un élève à partir d'une ligne CSV lue dans le fichier.
     * Exemple : "EL0001,Tremblay,Marie,15/09/2008,4A,marie@email.ca,514-555-0101"
     */
    public static Eleve depuisLigneCSV(String ligne) {
        // On découpe la ligne en morceaux en séparant par les virgules
        String[] morceaux = ligne.split(",", -1);

        // On vérifie qu'il y a bien 7 colonnes
        if (morceaux.length < 7) {
            return null; // Ligne invalide, on l'ignore
        }

        return new Eleve(morceaux[0], morceaux[1], morceaux[2], morceaux[3],
                         morceaux[4], morceaux[5], morceaux[6]);
    }

    /**
     * Retourne la première ligne du fichier CSV (les noms des colonnes).
     */
    public static String enteteCSV() {
        return "id,nom,prenom,dateNaissance,classe,email,telephone";
    }

    /**
     * Affiche l'élève de façon lisible dans le terminal.
     */
    @Override
    public String toString() {
        return "  [" + id + "] " + nom + " " + prenom
             + " | Classe: " + classe
             + " | Naissance: " + dateNaissance
             + " | Email: " + email
             + " | Tel: " + telephone;
    }
}

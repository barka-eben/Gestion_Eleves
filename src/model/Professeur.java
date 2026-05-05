package model;

/**
 * Représente un professeur de l'école.
 */
public class Professeur {

    public String id;
    public String nom;
    public String prenom;
    public String matiere;
    public String email;
    public String telephone;

    // Constructeur
    public Professeur(String id, String nom, String prenom,
                      String matiere, String email, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.matiere = matiere;
        this.email = email;
        this.telephone = telephone;
    }

    /**
     * Convertit le professeur en ligne CSV pour l'enregistrer.
     */
    public String versLigneCSV() {
        return id + "," + nom + "," + prenom + "," + matiere + ","
             + email + "," + telephone;
    }

    /**
     * Crée un professeur à partir d'une ligne CSV.
     */
    public static Professeur depuisLigneCSV(String ligne) {
        String[] morceaux = ligne.split(",", -1);

        if (morceaux.length < 6) {
            return null;
        }

        return new Professeur(morceaux[0], morceaux[1], morceaux[2],
                              morceaux[3], morceaux[4], morceaux[5]);
    }

    public static String enteteCSV() {
        return "id,nom,prenom,matiere,email,telephone";
    }

    @Override
    public String toString() {
        return "  [" + id + "] " + nom + " " + prenom
             + " | Matière: " + matiere
             + " | Email: " + email
             + " | Tel: " + telephone;
    }
}

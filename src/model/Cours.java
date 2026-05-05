package model;

/**
 * Représente un cours donné dans l'école.
 * Un cours est lié à un professeur (via son ID) et à une classe.
 */
public class Cours {

    public String id;
    public String nom;
    public String professeurId; // L'ID du professeur qui donne ce cours
    public String classe;
    public String horaire;
    public String salle;

    public Cours(String id, String nom, String professeurId,
                 String classe, String horaire, String salle) {
        this.id = id;
        this.nom = nom;
        this.professeurId = professeurId;
        this.classe = classe;
        this.horaire = horaire;
        this.salle = salle;
    }

    public String versLigneCSV() {
        return id + "," + nom + "," + professeurId + ","
             + classe + "," + horaire + "," + salle;
    }

    public static Cours depuisLigneCSV(String ligne) {
        String[] morceaux = ligne.split(",", -1);

        if (morceaux.length < 6) {
            return null;
        }

        return new Cours(morceaux[0], morceaux[1], morceaux[2],
                         morceaux[3], morceaux[4], morceaux[5]);
    }

    public static String enteteCSV() {
        return "id,nom,professeurId,classe,horaire,salle";
    }

    @Override
    public String toString() {
        return "  [" + id + "] " + nom
             + " | Prof ID: " + professeurId
             + " | Classe: " + classe
             + " | Horaire: " + horaire
             + " | Salle: " + salle;
    }
}

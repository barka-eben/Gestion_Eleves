package model;

/**
 * Représente une note attribuée à un élève dans une matière.
 */
public class Note {

    public String id;
    public String eleveId;   // L'ID de l'élève qui a eu cette note
    public String matiere;
    public double note;      // La note sur 20
    public String periode;   // Ex: T1, T2, T3, Examen
    public String commentaire;

    public Note(String id, String eleveId, String matiere,
                double note, String periode, String commentaire) {
        this.id = id;
        this.eleveId = eleveId;
        this.matiere = matiere;
        this.note = note;
        this.periode = periode;
        this.commentaire = commentaire;
    }

    public String versLigneCSV() {
        return id + "," + eleveId + "," + matiere + ","
             + note + "," + periode + "," + commentaire;
    }

    public static Note depuisLigneCSV(String ligne) {
        String[] morceaux = ligne.split(",", -1);

        if (morceaux.length < 6) {
            return null;
        }

        // On convertit le texte "85.5" en nombre décimal 85.5
        double valeurNote;
        try {
            valeurNote = Double.parseDouble(morceaux[3]);
        } catch (NumberFormatException e) {
            return null; // Si ce n'est pas un nombre, on ignore la ligne
        }

        return new Note(morceaux[0], morceaux[1], morceaux[2],
                        valeurNote, morceaux[4], morceaux[5]);
    }

    public static String enteteCSV() {
        return "id,eleveId,matiere,note,periode,commentaire";
    }

    @Override
    public String toString() {
        return "  [" + id + "] Élève: " + eleveId
             + " | Matière: " + matiere
             + " | Note: " + note + "/20"
             + " | Période: " + periode
             + " | Commentaire: " + commentaire;
    }
}

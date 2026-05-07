package service;

import model.Note;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gère toutes les opérations sur les notes.
 * Contient aussi les calculs de moyennes.
 */
public class NoteService {

    private static final String FICHIER = "data/notes.csv";
    private List<Note> notes;

    public NoteService() {
        notes = new ArrayList<>();
        chargerDepuisFichier();
    }

    // -------------------------------------------------------------------------
    // LECTURE ET ÉCRITURE CSV
    // -------------------------------------------------------------------------

    private void chargerDepuisFichier() {
        notes.clear();

        File fichier = new File(FICHIER);
        if (!fichier.exists()) {
            return;
        }

        try {
            BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
            String ligne;
            boolean premiereLigne = true;

            while ((ligne = lecteur.readLine()) != null) {
                if (premiereLigne) {
                    premiereLigne = false;
                    continue;
                }
                if (!ligne.trim().isEmpty()) {
                    Note n = Note.depuisLigneCSV(ligne.trim());
                    if (n != null) {
                        notes.add(n);
                    }
                }
            }

            lecteur.close();

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier notes : " + e.getMessage());
        }
    }

    private void sauvegarderDansFichier() {
        new File("data").mkdirs();

        try {
            BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER));
            ecrivain.write(Note.enteteCSV());
            ecrivain.newLine();

            for (Note n : notes) {
                ecrivain.write(n.versLigneCSV());
                ecrivain.newLine();
            }

            ecrivain.close();

        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier notes : " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // OPÉRATIONS SUR LES NOTES
    // -------------------------------------------------------------------------

    public List<Note> getTous() {
        return notes;
    }

    /**
     * Retourne toutes les notes d'un élève.
     */
    public List<Note> getNotesParEleve(String eleveId) {
        List<Note> resultat = new ArrayList<>();
        for (Note n : notes) {
            if (n.eleveId.equals(eleveId)) {
                resultat.add(n);
            }
        }
        return resultat;
    }

    /**
     * Retourne les notes d'un élève pour une période précise.
     */
    public List<Note> getNotesParEleveEtPeriode(String eleveId, String periode) {
        List<Note> resultat = new ArrayList<>();
        for (Note n : notes) {
            if (n.eleveId.equals(eleveId) && n.periode.equalsIgnoreCase(periode)) {
                resultat.add(n);
            }
        }
        return resultat;
    }

    /**
     * Retourne toutes les notes d'une matière donnée.
     */
    public List<Note> getNotesParMatiere(String matiere) {
        List<Note> resultat = new ArrayList<>();
        for (Note n : notes) {
            if (n.matiere.equalsIgnoreCase(matiere)) {
                resultat.add(n);
            }
        }
        return resultat;
    }

    /**
     * Cherche une note par son ID.
     */
    public Note trouverParId(String id) {
        for (Note n : notes) {
            if (n.id.equals(id)) {
                return n;
            }
        }
        return null;
    }

    public boolean ajouter(String eleveId, String matiere, double valeur,
                           String periode, String commentaire) {
        // Une note doit être entre 0 et 100
        if (valeur < 0 || valeur > 100) {
            return false;
        }

        String id = "NO" + String.format("%05d", notes.size() + 1);
        Note nouvelle = new Note(id, eleveId, matiere, valeur, periode, commentaire);
        notes.add(nouvelle);
        sauvegarderDansFichier();
        return true;
    }

    public boolean modifier(String id, double nouvelleValeur, String commentaire) {
        Note n = trouverParId(id);
        if (n == null) {
            return false;
        }

        n.note = nouvelleValeur;
        n.commentaire = commentaire;

        sauvegarderDansFichier();
        return true;
    }

    public boolean supprimer(String id) {
        Note n = trouverParId(id);
        if (n == null) {
            return false;
        }
        notes.remove(n);
        sauvegarderDansFichier();
        return true;
    }

    // -------------------------------------------------------------------------
    // CALCULS DE MOYENNES
    // -------------------------------------------------------------------------

    /**
     * Calcule la moyenne générale d'un élève sur toutes ses notes.
     */
    public double getMoyenneEleve(String eleveId) {
        List<Note> notesEleve = getNotesParEleve(eleveId);
        if (notesEleve.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Note n : notesEleve) {
            total = total + n.note;
        }
        return total / notesEleve.size();
    }

    /**
     * Calcule la moyenne d'un élève pour une période donnée.
     */
    public double getMoyenneEleveParPeriode(String eleveId, String periode) {
        List<Note> notesEleve = getNotesParEleveEtPeriode(eleveId, periode);
        if (notesEleve.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Note n : notesEleve) {
            total = total + n.note;
        }
        return total / notesEleve.size();
    }

    /**
     * Retourne la liste de toutes les matières distinctes d'un élève.
     * Utile pour afficher le bulletin par matière.
     */
    public List<String> getMatieresDistinctesEleve(String eleveId) {
        List<Note> notesEleve = getNotesParEleve(eleveId);
        List<String> matieres = new ArrayList<>();

        for (Note n : notesEleve) {
            if (!matieres.contains(n.matiere)) {
                matieres.add(n.matiere);
            }
        }
        return matieres;
    }

    /**
     * Calcule la moyenne d'un élève dans une matière précise.
     */
    public double getMoyenneEleveParMatiere(String eleveId, String matiere) {
        List<Note> notesEleve = getNotesParEleve(eleveId);
        List<Note> notesDansMatiere = new ArrayList<>();

        for (Note n : notesEleve) {
            if (n.matiere.equalsIgnoreCase(matiere)) {
                notesDansMatiere.add(n);
            }
        }

        if (notesDansMatiere.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Note n : notesDansMatiere) {
            total = total + n.note;
        }
        return total / notesDansMatiere.size();
    }

    public int getNombreTotal() {
        return notes.size();
    }
}

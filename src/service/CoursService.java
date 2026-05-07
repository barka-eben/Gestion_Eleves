package service;

import model.Cours;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gère toutes les opérations sur les cours.
 */
public class CoursService {

    private static final String FICHIER = "data/cours.csv";
    private List<Cours> coursList;

    public CoursService() {
        coursList = new ArrayList<>();
        chargerDepuisFichier();
    }

    // -------------------------------------------------------------------------
    // LECTURE ET ÉCRITURE CSV
    // -------------------------------------------------------------------------

    private void chargerDepuisFichier() {
        coursList.clear();

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
                    Cours c = Cours.depuisLigneCSV(ligne.trim());
                    if (c != null) {
                        coursList.add(c);
                    }
                }
            }

            lecteur.close();

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier cours : " + e.getMessage());
        }
    }

    private void sauvegarderDansFichier() {
        new File("data").mkdirs();

        try {
            BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER));
            ecrivain.write(Cours.enteteCSV());
            ecrivain.newLine();

            for (Cours c : coursList) {
                ecrivain.write(c.versLigneCSV());
                ecrivain.newLine();
            }

            ecrivain.close();

        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier cours : " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // OPÉRATIONS SUR LES COURS
    // -------------------------------------------------------------------------

    public List<Cours> getTous() {
        return coursList;
    }

    public Cours trouverParId(String id) {
        for (Cours c : coursList) {
            if (c.id.equals(id)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Retourne tous les cours d'une classe donnée.
     */
    public List<Cours> trouverParClasse(String classe) {
        List<Cours> resultat = new ArrayList<>();
        for (Cours c : coursList) {
            if (c.classe.equalsIgnoreCase(classe)) {
                resultat.add(c);
            }
        }
        return resultat;
    }

    /**
     * Retourne tous les cours donnés par un professeur.
     */
    public List<Cours> trouverParProfesseur(String professeurId) {
        List<Cours> resultat = new ArrayList<>();
        for (Cours c : coursList) {
            if (c.professeurId.equals(professeurId)) {
                resultat.add(c);
            }
        }
        return resultat;
    }

    public void ajouter(String nom, String professeurId, String classe,
                        String horaire, String salle) {
        String id = "CO" + String.format("%04d", coursList.size() + 1);
        Cours nouveau = new Cours(id, nom, professeurId, classe, horaire, salle);
        coursList.add(nouveau);
        sauvegarderDansFichier();
        System.out.println("Cours ajouté avec l'ID : " + id);
    }

    public boolean modifier(String id, String nom, String professeurId,
                            String classe, String horaire, String salle) {
        Cours c = trouverParId(id);
        if (c == null) {
            return false;
        }

        c.nom = nom;
        c.professeurId = professeurId;
        c.classe = classe;
        c.horaire = horaire;
        c.salle = salle;

        sauvegarderDansFichier();
        return true;
    }

    public boolean supprimer(String id) {
        Cours c = trouverParId(id);
        if (c == null) {
            return false;
        }
        coursList.remove(c);
        sauvegarderDansFichier();
        return true;
    }

    public int getNombreTotal() {
        return coursList.size();
    }
}

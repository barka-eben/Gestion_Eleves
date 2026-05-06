package service;

import model.Professeur;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gère toutes les opérations sur les professeurs.
 * Même logique que EleveService : lecture/écriture CSV + opérations CRUD.
 */
public class ProfesseurService {

    private static final String FICHIER = "data/professeurs.csv";
    private List<Professeur> professeurs;

    public ProfesseurService() {
        professeurs = new ArrayList<>();
        chargerDepuisFichier();
    }

    // -------------------------------------------------------------------------
    // LECTURE ET ÉCRITURE CSV
    // -------------------------------------------------------------------------

    private void chargerDepuisFichier() {
        professeurs.clear();

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
                    Professeur p = Professeur.depuisLigneCSV(ligne.trim());
                    if (p != null) {
                        professeurs.add(p);
                    }
                }
            }

            lecteur.close();

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier professeurs : " + e.getMessage());
        }
    }

    private void sauvegarderDansFichier() {
        new File("data").mkdirs();

        try {
            BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER));
            ecrivain.write(Professeur.enteteCSV());
            ecrivain.newLine();

            for (Professeur p : professeurs) {
                ecrivain.write(p.versLigneCSV());
                ecrivain.newLine();
            }

            ecrivain.close();

        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier professeurs : " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // OPÉRATIONS SUR LES PROFESSEURS
    // -------------------------------------------------------------------------

    public List<Professeur> getTous() {
        return professeurs;
    }

    public Professeur trouverParId(String id) {
        for (Professeur p : professeurs) {
            if (p.id.equals(id)) {
                return p;
            }
        }
        return null;
    }

    public List<Professeur> rechercher(String terme) {
        List<Professeur> resultat = new ArrayList<>();
        String termeLower = terme.toLowerCase();

        for (Professeur p : professeurs) {
            if (p.nom.toLowerCase().contains(termeLower)
             || p.prenom.toLowerCase().contains(termeLower)
             || p.matiere.toLowerCase().contains(termeLower)) {
                resultat.add(p);
            }
        }
        return resultat;
    }

    public void ajouter(String nom, String prenom, String matiere,
                        String email, String telephone) {
        String id = "PR" + String.format("%04d", professeurs.size() + 1);
        Professeur nouveau = new Professeur(id, nom, prenom, matiere, email, telephone);
        professeurs.add(nouveau);
        sauvegarderDansFichier();
        System.out.println("Professeur ajouté avec l'ID : " + id);
    }

    public boolean modifier(String id, String nom, String prenom, String matiere,
                            String email, String telephone) {
        Professeur p = trouverParId(id);
        if (p == null) {
            return false;
        }

        p.nom = nom;
        p.prenom = prenom;
        p.matiere = matiere;
        p.email = email;
        p.telephone = telephone;

        sauvegarderDansFichier();
        return true;
    }

    public boolean supprimer(String id) {
        Professeur p = trouverParId(id);
        if (p == null) {
            return false;
        }
        professeurs.remove(p);
        sauvegarderDansFichier();
        return true;
    }

    public int getNombreTotal() {
        return professeurs.size();
    }
}

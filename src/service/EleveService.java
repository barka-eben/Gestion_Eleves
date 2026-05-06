package service;

import model.Eleve;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gère toutes les opérations sur les élèves :
 * lire le fichier CSV, ajouter, modifier, supprimer, rechercher.
 */
public class EleveService {

    // Nom du fichier CSV où sont stockés les élèves
    private static final String FICHIER = "data/eleves.csv";

    // La liste des élèves chargés en mémoire
    private List<Eleve> eleves;

    // Constructeur : on charge les élèves depuis le fichier dès le départ
    public EleveService() {
        eleves = new ArrayList<>();
        chargerDepuisFichier();
    }

    // -------------------------------------------------------------------------
    // LECTURE ET ÉCRITURE DANS LE FICHIER CSV
    // -------------------------------------------------------------------------

    /**
     * Lit le fichier CSV ligne par ligne et remplit la liste d'élèves.
     * Si le fichier n'existe pas encore, la liste reste vide.
     */
    private void chargerDepuisFichier() {
        eleves.clear();

        File fichier = new File(FICHIER);
        if (!fichier.exists()) {
            return; // Pas de fichier = pas d'élèves, c'est normal au départ
        }

        try {
            BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
            String ligne;
            boolean premiereLigne = true;

            while ((ligne = lecteur.readLine()) != null) {
                // On saute la première ligne qui contient les noms des colonnes
                if (premiereLigne) {
                    premiereLigne = false;
                    continue;
                }

                // On crée un élève depuis la ligne CSV et on l'ajoute à la liste
                if (!ligne.trim().isEmpty()) {
                    Eleve e = Eleve.depuisLigneCSV(ligne.trim());
                    if (e != null) {
                        eleves.add(e);
                    }
                }
            }

            lecteur.close();

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier élèves : " + e.getMessage());
        }
    }

    /**
     * Réécrit tout le fichier CSV à partir de la liste en mémoire.
     * On appelle cette méthode après chaque ajout, modification ou suppression.
     */
    private void sauvegarderDansFichier() {
        // On crée le dossier "data" s'il n'existe pas
        new File("data").mkdirs();

        try {
            BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER));

            // On écrit d'abord la ligne d'en-tête
            ecrivain.write(Eleve.enteteCSV());
            ecrivain.newLine();

            // Puis on écrit chaque élève
            for (Eleve e : eleves) {
                ecrivain.write(e.versLigneCSV());
                ecrivain.newLine();
            }

            ecrivain.close();

        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier élèves : " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // OPÉRATIONS SUR LES ÉLÈVES
    // -------------------------------------------------------------------------

    /**
     * Retourne la liste complète des élèves.
     */
    public List<Eleve> getTous() {
        return eleves;
    }

    /**
     * Cherche un élève par son ID. Retourne null si non trouvé.
     */
    public Eleve trouverParId(String id) {
        for (Eleve e : eleves) {
            if (e.id.equals(id)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Retourne tous les élèves d'une classe donnée (ex: "4A").
     */
    public List<Eleve> trouverParClasse(String classe) {
        List<Eleve> resultat = new ArrayList<>();
        for (Eleve e : eleves) {
            if (e.classe.equalsIgnoreCase(classe)) {
                resultat.add(e);
            }
        }
        return resultat;
    }

    /**
     * Recherche des élèves dont le nom, le prénom ou l'ID contient le terme donné.
     */
    public List<Eleve> rechercher(String terme) {
        List<Eleve> resultat = new ArrayList<>();
        String termeLower = terme.toLowerCase();

        for (Eleve e : eleves) {
            if (e.nom.toLowerCase().contains(termeLower)
             || e.prenom.toLowerCase().contains(termeLower)
             || e.id.toLowerCase().contains(termeLower)) {
                resultat.add(e);
            }
        }
        return resultat;
    }

    /**
     * Ajoute un nouvel élève avec les informations données.
     */
    public void ajouter(String nom, String prenom, String dateNaissance,
                        String classe, String email, String telephone) {
        // On génère un ID automatique du type EL0001, EL0002, etc.
        String id = "EL" + String.format("%04d", eleves.size() + 1);

        Eleve nouvelEleve = new Eleve(id, nom, prenom, dateNaissance, classe, email, telephone);
        eleves.add(nouvelEleve);
        sauvegarderDansFichier();

        System.out.println("Élève ajouté avec l'ID : " + id);
    }

    /**
     * Modifie les informations d'un élève existant.
     * Retourne true si la modification a réussi, false si l'élève n'existe pas.
     */
    public boolean modifier(String id, String nom, String prenom, String dateNaissance,
                            String classe, String email, String telephone) {
        Eleve e = trouverParId(id);
        if (e == null) {
            return false;
        }

        e.nom = nom;
        e.prenom = prenom;
        e.dateNaissance = dateNaissance;
        e.classe = classe;
        e.email = email;
        e.telephone = telephone;

        sauvegarderDansFichier();
        return true;
    }

    /**
     * Supprime un élève par son ID.
     * Retourne true si la suppression a réussi.
     */
    public boolean supprimer(String id) {
        Eleve e = trouverParId(id);
        if (e == null) {
            return false;
        }

        eleves.remove(e);
        sauvegarderDansFichier();
        return true;
    }

    /**
     * Retourne la liste de toutes les classes distinctes (sans doublons).
     */
    public List<String> getClassesDistinctes() {
        List<String> classes = new ArrayList<>();
        for (Eleve e : eleves) {
            if (!classes.contains(e.classe)) {
                classes.add(e.classe);
            }
        }
        return classes;
    }

    public int getNombreTotal() {
        return eleves.size();
    }
}

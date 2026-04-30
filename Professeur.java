package model;

public class Professeur {
    private String id;
    private String nom;
    private String prenom;
    private String matiere;
    private String email;
    private String telephone;

    public Professeur(String id, String nom, String prenom, String matiere, String email, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.matiere = matiere;
        this.email = email;
        this.telephone = telephone;
    }

    public String toCSV() {
        return id + "," + nom + "," + prenom + "," + matiere + "," + email + "," + telephone;
    }

    public static Professeur fromCSV(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 6) return null;
        return new Professeur(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }

    public static String csvHeader() {
        return "id,nom,prenom,matiere,email,telephone";
    }

    // Getters
    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getMatiere() { return matiere; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setMatiere(String matiere) { this.matiere = matiere; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    @Override
    public String toString() {
        return String.format("  ID: %-8s | Nom: %-15s | Prénom: %-15s | Matière: %-20s | Email: %s",
                id, nom, prenom, matiere, email);
    }
}

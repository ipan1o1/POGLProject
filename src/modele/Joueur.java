package modele;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private int ligne;
    private int colonne;
    private int eau;
    private int actionsRestantes;
    private String nom;
    private List<String> inventaire;

    public static final int EAU_MAX = 5;
    public static final int ACTIONS_MAX = 4;

    public Joueur(String nom, int ligne, int colonne) {
        this.nom = nom;
        this.ligne = ligne;
        this.colonne = colonne;
        this.eau = EAU_MAX;
        this.actionsRestantes = ACTIONS_MAX;
        this.inventaire = new ArrayList<>();
    }

    public boolean deplacer(int newLigne, int newCol, Desert desert) {
        if (actionsRestantes <= 0) return false;
        Zone cible = desert.getZone(newLigne, newCol);
        if (cible.isBloquee()) return false;
        this.ligne = newLigne;
        this.colonne = newCol;
        actionsRestantes--;
        return true;
    }

    public boolean creuser(int i, int j, Desert desert) {
        if (actionsRestantes <= 0) return false;
        desert.getZone(i, j).enleverSable();
        actionsRestantes--;
        return true;
    }

    public void boire() {
        if (eau > 0) eau--;
    }

    public void remplir(int quantite) {
        eau = Math.min(EAU_MAX, eau + quantite);
    }

    public boolean explorer(int i, int j, Desert desert) {
        if (actionsRestantes <= 0) return false;
        desert.getZone(i, j).explorer();
        actionsRestantes--;
        return true;
    }

    public boolean estMort() { return eau <= 0; }

    public boolean ramasser(Desert desert) {
        if (actionsRestantes <= 0) return false;
        Zone zone = desert.getZone(ligne, colonne);
        if (!zone.hasPiece() || !zone.isPieceRevlee()) return false;
        inventaire.add(zone.ramasserPiece());
        actionsRestantes--;
        return true;
    }

    public List<String> getInventaire() { return inventaire; }

    public void consommerAction() {
        if (actionsRestantes > 0) actionsRestantes--;
    }

    public void resetActions() { actionsRestantes = ACTIONS_MAX; }

    public int getLigne() { return ligne; }
    public int getColonne() { return colonne; }
    public int getEau() { return eau; }
    public int getActionsRestantes() { return actionsRestantes; }
    public String getNom() { return nom; }

    @Override
    public String toString() {
        return nom + " pos:(" + ligne + "," + colonne + ") eau:" + eau + " actions:" + actionsRestantes;
    }
}
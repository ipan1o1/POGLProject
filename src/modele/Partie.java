package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Partie {
    private Desert desert;
    private List<Joueur> joueurs;
    private int joueurActuel;
    private boolean partieTerminee;
    private boolean victoire;
    private Random random;

    public Partie(int nbJoueurs) {
        this.desert = new Desert();
        this.joueurs = new ArrayList<>();
        this.joueurActuel = 0;
        this.partieTerminee = false;
        this.victoire = false;
        this.random = new Random();

        // all players start at crash site (0,2)
        for (int i = 0; i < nbJoueurs; i++) {
            joueurs.add(new Joueur("Joueur " + (i + 1), 0, 2));
        }
    }

    public void finDeTour() {
        // reset current player actions
        joueurs.get(joueurActuel).resetActions();

        // trigger desert actions
        int nbActions = (int) desert.getNiveauTempete();
        for (int i = 0; i < nbActions; i++) {
            declencherActionDesert();
        }

        // check win condition
        verifierVictoire();
        if (partieTerminee) return;

        // check lose conditions
        if (desert.isPerdu()) {
            partieTerminee = true;
            victoire = false;
            return;
        }

        for (Joueur j : joueurs) {
            if (j.estMort()) {
                partieTerminee = true;
                victoire = false;
                return;
            }
        }

        // next player's turn
        joueurActuel = (joueurActuel + 1) % joueurs.size();
    }

    private void declencherActionDesert() {
        int action = random.nextInt(3);
        switch (action) {
            case 0: // wind
                String[] directions = {"N", "S", "E", "O"};
                String dir = directions[random.nextInt(4)];
                int force = random.nextInt(3) + 1;
                ventSouffle(dir, force);
                break;
            case 1: // heatwave
                vagueDeChaleur();
                break;
            case 2: // storm intensifies
                desert.augmenterTempete();
                break;
        }
    }

    private void ventSouffle(String direction, int force) {
        int[] oeil = desert.getOeil();
        int oi = oeil[0];
        int oj = oeil[1];

        for (int f = 0; f < force; f++) {
            int ni = oi, nj = oj;
            switch (direction) {
                case "N": ni = oi - 1; break;
                case "S": ni = oi + 1; break;
                case "E": nj = oj + 1; break;
                case "O": nj = oj - 1; break;
            }
            if (ni < 0 || ni >= Desert.TAILLE || nj < 0 || nj >= Desert.TAILLE) break;
            desert.deplacerOeil(oi, oj, ni, nj);
            oi = ni;
            oj = nj;
        }
    }

    private void vagueDeChaleur() {
        for (Joueur j : joueurs) {
            // players in a tunnel are immune to heatwaves
            Zone zone = desert.getZone(j.getLigne(), j.getColonne());
            if (!zone.getType().equals("tunnel")) {
                j.boire();
            }
        }
    }

    // Explorer action triggered from the controller
    public void explorer() {
        Joueur current = getJoueurActuel();
        int li = current.getLigne();
        int col = current.getColonne();
        if (desert.getZone(li, col).isExploree()) return;
        if (current.explorer(li, col, desert)) {
            Zone zone = desert.getZone(li, col);
            if (zone.getType().equals("oasis")) {
                for (Joueur j : joueurs) {
                    if (j.getLigne() == li && j.getColonne() == col) {
                        j.remplir(2);
                    }
                }
            } else if (zone.getType().equals("indice")) {
                desert.revelerPieceDepuis(li, col);
            }
        }
    }

    public void ramasser() {
        getJoueurActuel().ramasser(desert);
    }

    public void verifierVictoire() {
        if (partieTerminee) return;
        Zone dec = desert.getZone(Desert.POSITION_DECOLLAGE[0], Desert.POSITION_DECOLLAGE[1]);
        if (dec.getSable() > 0) return;
        for (Joueur j : joueurs) {
            if (j.getLigne() != Desert.POSITION_DECOLLAGE[0] || j.getColonne() != Desert.POSITION_DECOLLAGE[1]) return;
        }
        int totalPieces = joueurs.stream().mapToInt(j -> j.getInventaire().size()).sum();
        if (totalPieces == 4) {
            partieTerminee = true;
            victoire = true;
        }
    }

    public Desert getDesert() { return desert; }
    public List<Joueur> getJoueurs() { return joueurs; }
    public Joueur getJoueurActuel() { return joueurs.get(joueurActuel); }
    public boolean isPartieTerminee() { return partieTerminee; }
    public boolean isVictoire() { return victoire; }
}
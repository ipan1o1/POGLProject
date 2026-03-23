package controleur;

import modele.Joueur;
import modele.Partie;
import vue.VueDesert;

import javax.swing.*;

public class Controleur {
    private Partie partie;
    private VueDesert vue;

    public Controleur(Partie partie, VueDesert vue) {
        this.partie = partie;
        this.vue = vue;

        // fin de tour button
        vue.getFinTourButton().addActionListener(e -> {
            partie.finDeTour();
            vue.mettreAJour();
        });

        // click on cells to move player
        JPanel[][] cases = vue.getCases();
        Joueur joueur = partie.getJoueurActuel();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                final int fi = i;
                final int fj = j;
                cases[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        Joueur current = partie.getJoueurActuel();
                        int li = current.getLigne();
                        int col = current.getColonne();

                        // only allow adjacent moves
                        boolean adjacent = (Math.abs(fi - li) + Math.abs(fj - col)) == 1;
                        if (adjacent) {
                            current.deplacer(fi, fj, partie.getDesert());
                        }
                        vue.mettreAJour();
                    }
                });
            }
        }
    }
}
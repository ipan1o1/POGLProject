package controleur;

import modele.Joueur;
import modele.Partie;
import vue.VueDesert;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class Controleur {
    public Controleur(Partie partie, VueDesert vue) {
        // fin de tour button
        vue.getFinTourButton().addActionListener(e -> {
            partie.finDeTour();
            vue.mettreAJour();
        });

        // click on cells to move player
        JPanel[][] cases = vue.getCases();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                final int fi = i;
                final int fj = j;
                cases[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Joueur current = partie.getJoueurActuel();
                        int li = current.getLigne();
                        int col = current.getColonne();
                        int dist = Math.abs(fi - li) + Math.abs(fj - col);

                        if (SwingUtilities.isLeftMouseButton(e) && dist == 1) {
                            // left click = move to adjacent zone
                            current.deplacer(fi, fj, partie.getDesert());
                        } else if (SwingUtilities.isRightMouseButton(e) && dist <= 1) {
                            // right click = dig current or adjacent zone
                            current.creuser(fi, fj, partie.getDesert());
                        }
                        vue.mettreAJour();
                    }
                });
            }
        }
    }
}
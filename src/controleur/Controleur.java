package controleur;

import modele.Joueur;
import modele.Partie;
import vue.VueDesert;

import modele.Desert;
import modele.Zone;

import javax.swing.*;
import java.awt.event.KeyEvent;
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
                        Desert desert = partie.getDesert();
                        Zone zoneCourante = desert.getZone(li, col);
                        Zone zoneCible   = desert.getZone(fi, fj);

                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (dist == 1) {
                                // normal move to adjacent zone
                                current.deplacer(fi, fj, desert);
                            } else if (zoneCourante.getType().equals("tunnel")
                                    && zoneCible.getType().equals("tunnel")
                                    && dist > 1) {
                                // tunnel teleport: costs 2 actions
                                if (current.getActionsRestantes() >= 2
                                        && current.deplacer(fi, fj, desert)) {
                                    current.consommerAction(); // consume 2nd action
                                }
                            }
                        } else if (SwingUtilities.isRightMouseButton(e) && dist <= 1) {
                            // right click = dig current or adjacent zone
                            current.creuser(fi, fj, desert);
                        }
                        vue.mettreAJour();
                    }
                });
            }
        }

        // E key = explore current zone
        vue.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "explorer");
        vue.getRootPane().getActionMap().put("explorer", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                partie.explorer();
                vue.mettreAJour();
            }
        });
    }
}
package controleur;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.*;
import modele.Desert;
import modele.Joueur;
import modele.Partie;
import modele.Zone;
import vue.VueDesert;

public class Controleur {
    public Controleur(Partie partie, VueDesert vue) {
        vue.getFinTourButton().addActionListener(e -> {
            partie.finDeTour();
            vue.mettreAJour();
        });

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
                                current.deplacer(fi, fj, desert);
                            } else if (zoneCourante.getType().equals("tunnel")
                                    && zoneCible.getType().equals("tunnel")
                                    && dist > 1) {
                                if (current.getActionsRestantes() >= 2
                                        && current.deplacer(fi, fj, desert)) {
                                    current.consommerAction(); 
                                }
                            }
                        } else if (SwingUtilities.isRightMouseButton(e) && dist <= 1) {
                            // right click = dig 
                            current.creuser(fi, fj, desert);
                        }
                        partie.verifierVictoire();
                        vue.mettreAJour();
                    }
                });
            }
        }

        // E = explore 
        vue.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "explorer");
        vue.getRootPane().getActionMap().put("explorer", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                partie.explorer();
                partie.verifierVictoire();
                vue.mettreAJour();
            }
        });

        // F = pick up 
        vue.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "ramasser");
        vue.getRootPane().getActionMap().put("ramasser", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                partie.ramasser();
                partie.verifierVictoire();
                vue.mettreAJour();
            }
        });
    }
}
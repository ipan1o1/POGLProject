package vue;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import modele.Desert;
import modele.Joueur;
import modele.Partie;

public class VueDesert extends JFrame {
    private Partie partie;
    private JPanel grillePanel;
    private JPanel[][] cases;
    private JLabel statusLabel;
    private JLabel joueurLabel;
    private JButton finTourButton;
    private JPanel eauPanel;

    public VueDesert(Partie partie) {
        this.partie = partie;
        this.cases = new JPanel[Desert.TAILLE][Desert.TAILLE];

        setTitle("Le Desert Interdit");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // grid
        grillePanel = new JPanel(new GridLayout(Desert.TAILLE, Desert.TAILLE, 2, 2));
        grillePanel.setBackground(Color.BLACK);
        grillePanel.setPreferredSize(new Dimension(500, 500));

        for (int i = 0; i < Desert.TAILLE; i++) {
            for (int j = 0; j < Desert.TAILLE; j++) {
                JPanel cell = new JPanel(new BorderLayout());
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cases[i][j] = cell;
                grillePanel.add(cell);
            }
        }

        JPanel statusPanel = new JPanel(new GridLayout(3, 1));
        statusLabel = new JLabel("Sable total: 0 | Tempete: 2");
        joueurLabel = new JLabel("Tour: Joueur 1");
        finTourButton = new JButton("Fin de Tour");

        statusPanel.add(statusLabel);
        statusPanel.add(joueurLabel);
        statusPanel.add(finTourButton);

        // water 
        eauPanel = new JPanel();
        eauPanel.setLayout(new BoxLayout(eauPanel, BoxLayout.Y_AXIS));
        eauPanel.setBorder(BorderFactory.createTitledBorder("Eau"));
        eauPanel.setPreferredSize(new Dimension(140, 0));

        add(grillePanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        add(eauPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        mettreAJour();
    }

    public void mettreAJour() {
        Desert desert = partie.getDesert();
        List<Joueur> joueurs = partie.getJoueurs();

        for (int i = 0; i < Desert.TAILLE; i++) {
            for (int j = 0; j < Desert.TAILLE; j++) {
                JPanel cell = cases[i][j];
                cell.removeAll();

                modele.Zone zone = desert.getZone(i, j);
                String type = zone.getType();

             
                switch (type) {
                    case "oeil":      cell.setBackground(Color.WHITE); break;
                    case "oasis":     cell.setBackground(Color.GREEN); break;
                    case "mirage":    cell.setBackground(zone.isExploree() ? new Color(194, 178, 128) : Color.GREEN); break;
                    case "tunnel":    cell.setBackground(new Color(100, 200, 255)); break;
                    case "crash":     cell.setBackground(Color.ORANGE); break;
                    case "decollage": cell.setBackground(new Color(255, 215, 0)); break; // gold
                    case "indice":    cell.setBackground(new Color(200, 160, 255)); break; // purple
                    default:          cell.setBackground(new Color(194, 178, 128));
                }

                // sand
                JLabel sableLabel = new JLabel(String.valueOf(zone.getSable()), SwingConstants.CENTER);
                sableLabel.setFont(new Font("Arial", Font.BOLD, 14));
                if (zone.isBloquee()) {
                    cell.setBackground(Color.DARK_GRAY);
                    sableLabel.setForeground(Color.WHITE);
                }
                cell.add(sableLabel, BorderLayout.CENTER);

                // revealed piece 
                if (zone.hasPiece() && zone.isPieceRevlee()) {
                    JLabel pieceLabel = new JLabel("⚙", SwingConstants.CENTER);
                    pieceLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    pieceLabel.setForeground(new Color(180, 60, 0));
                    cell.add(pieceLabel, BorderLayout.SOUTH);
                }

                // show players
                for (Joueur joueur : joueurs) {
                    if (joueur.getLigne() == i && joueur.getColonne() == j) {
                        JLabel joueurIcon = new JLabel("★", SwingConstants.CENTER);
                        joueurIcon.setForeground(Color.RED);
                        joueurIcon.setFont(new Font("Arial", Font.BOLD, 16));
                        cell.add(joueurIcon, BorderLayout.NORTH);
                    }
                }

                cell.revalidate();
                cell.repaint();
            }
        }

        // update bars
        statusLabel.setText("Sable: " + desert.getTotalSable() + " | Tempete: " + desert.getNiveauTempete());
        joueurLabel.setText("Tour: " + partie.getJoueurActuel().getNom() +
                " | Actions: " + partie.getJoueurActuel().getActionsRestantes());

        // refresh water
        eauPanel.removeAll();
        for (Joueur joueur : joueurs) {
            String gouttes = "💧".repeat(joueur.getEau()) + "🩶".repeat(Joueur.EAU_MAX - joueur.getEau());
            String inv = joueur.getInventaire().isEmpty() ? "" : "<br>⚙ " + String.join(", ", joueur.getInventaire());
            JLabel label = new JLabel("<html><b>" + joueur.getNom() + "</b><br>" + gouttes + inv + "</html>");
            label.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            eauPanel.add(label);
        }
        eauPanel.revalidate();
        eauPanel.repaint();

        if (partie.isPartieTerminee()) {
            String msg = partie.isVictoire() ? "VICTOIRE !" : "DEFAITE !";
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    public JButton getFinTourButton() { return finTourButton; }
    public JPanel[][] getCases() { return cases; }
}
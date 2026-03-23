import controleur.Controleur;
import modele.Partie;
import vue.VueDesert;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Partie partie = new Partie(2);
            VueDesert vue = new VueDesert(partie);
            Controleur controleur = new Controleur(partie, vue);
        });
    }
}
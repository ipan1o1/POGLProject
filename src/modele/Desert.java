package modele;

public class Desert {
    public static final int TAILLE = 5;
    private Zone[][] grille;
    private int[] oeil; // position of storm eye [row, col]
    private int niveauTempete;
    private int totalSable;

    public Desert() {
        grille = new Zone[TAILLE][TAILLE];
        niveauTempete = 2;
        totalSable = 0;
        oeil = new int[]{2, 2}; // center of grid
        initialiserGrille();
        placerSableInitial();
    }

    private void initialiserGrille() {
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                grille[i][j] = new Zone("normale");
            }
        }
        grille[2][2] = new Zone("oeil");
        grille[0][2] = new Zone("crash");
    }

    private void placerSableInitial() {
        int[][] cases = {{0,2},{1,1},{1,3},{2,0},{2,4},{3,1},{3,3},{4,2}};
        for (int[] c : cases) {
            ajouterSable(c[0], c[1]);
        }
    }

    public void ajouterSable(int i, int j) {
        if (grille[i][j].getType().equals("oeil")) return;
        grille[i][j].ajouterSable();
        totalSable++;
    }

    public Zone getZone(int i, int j) { return grille[i][j]; }
    public int[] getOeil() { return oeil; }
    public int getNiveauTempete() { return niveauTempete; }
    public int getTotalSable() { return totalSable; }

    public boolean isPerdu() {
        return totalSable > 43 || niveauTempete >= 7;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                sb.append(grille[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
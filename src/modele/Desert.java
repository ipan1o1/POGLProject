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

    // fixed positions for special zones
    public static final int[][] POSITIONS_OASIS   = {{0,0}, {4,4}};
    public static final int[]   POSITION_MIRAGE   = {4,0};
    public static final int[][] POSITIONS_TUNNELS = {{0,4}, {2,1}, {4,3}};
    public static final int[]   POSITION_CRASH    = {0, 2};
    public static final int[]   POSITION_DECOLLAGE = {4, 2};

    // each row: { indice pos, piece pos, piece name }
    private static final Object[][] INDICES = {
        {new int[]{1, 0}, new int[]{3, 4}, "Hélice"},
        {new int[]{1, 4}, new int[]{3, 0}, "Moteur"},
        {new int[]{3, 1}, new int[]{0, 3}, "Boussole"},
        {new int[]{3, 3}, new int[]{0, 1}, "Gouvernail"},
    };

    private void initialiserGrille() {
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                grille[i][j] = new Zone("normale");
            }
        }
        grille[2][2] = new Zone("oeil");
        for (int[] pos : POSITIONS_OASIS)   grille[pos[0]][pos[1]] = new Zone("oasis");
        grille[POSITION_MIRAGE[0]][POSITION_MIRAGE[1]] = new Zone("mirage");
        for (int[] pos : POSITIONS_TUNNELS) grille[pos[0]][pos[1]] = new Zone("tunnel");
        grille[POSITION_CRASH[0]][POSITION_CRASH[1]]       = new Zone("crash");
        grille[POSITION_DECOLLAGE[0]][POSITION_DECOLLAGE[1]] = new Zone("decollage");
        for (Object[] ind : INDICES) {
            int[] ip = (int[]) ind[0];
            int[] pp = (int[]) ind[1];
            grille[ip[0]][ip[1]] = new Zone("indice");
            grille[pp[0]][pp[1]].setPiece((String) ind[2]);
        }
    }

    private void placerSableInitial() {
        // (0,2) excluded — crash site, players start there
        int[][] cases = {{1,1},{1,3},{2,0},{2,4},{3,1},{3,3},{4,2}};
        for (int[] c : cases) {
            ajouterSable(c[0], c[1]);
        }
    }

    public void revelerPieceDepuis(int i, int j) {
        for (Object[] ind : INDICES) {
            int[] ip = (int[]) ind[0];
            if (ip[0] == i && ip[1] == j) {
                int[] pp = (int[]) ind[1];
                grille[pp[0]][pp[1]].setPieceRevlee(true);
                return;
            }
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

    public void augmenterTempete() {
        niveauTempete++;
    }

    public void deplacerOeil(int oi, int oj, int ni, int nj) {
        grille[oi][oj] = new Zone("normale");
        grille[ni][nj] = new Zone("oeil");
        ajouterSable(oi, oj);
        oeil[0] = ni;
        oeil[1] = nj;
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
package modele;

public class Zone {
    private int sable;        // amount of sand on this tile
    private boolean bloquee;  // blocked if sable >= 2
    private boolean exploree; // whether it's been explored
    private String type;      // "normale", "oeil", "crash", "decollage", "oasis", "mirage", "tunnel", "indice"
    private String piece;     // name of machine part on this tile, null if none
    private boolean pieceRevlee; // whether this part's location has been revealed via a clue

    public Zone(String type) {
        this.type = type;
        this.sable = 0;
        this.exploree = false;
        this.bloquee = false;
        this.piece = null;
        this.pieceRevlee = false;
    }

    public void ajouterSable() {
        this.sable++;
        this.bloquee = (this.sable >= 2);
    }

    public void enleverSable() {
        if (this.sable > 0) {
            this.sable--;
            this.bloquee = (this.sable >= 2);
        }
    }

    public int getSable() { return sable; }
    public boolean isBloquee() { return bloquee; }
    public boolean isExploree() { return exploree; }
    public String getType() { return type; }

    public void explorer() { this.exploree = true; }

    public void setPiece(String nom) { this.piece = nom; }
    public String getPiece() { return piece; }
    public boolean hasPiece() { return piece != null; }
    public boolean isPieceRevlee() { return pieceRevlee; }
    public void setPieceRevlee(boolean b) { this.pieceRevlee = b; }
    public String ramasserPiece() { String p = piece; piece = null; return p; }

    @Override
    public String toString() {
        return "[" + type.charAt(0) + "|s:" + sable + "]";
    }
}
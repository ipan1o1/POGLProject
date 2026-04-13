package modele;

public class Zone {
    private int sable;        
    private boolean bloquee;  
    private boolean exploree; 
    private String type;      
    private String piece;    
    private boolean pieceRevlee;

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
package modele;

public class Zone {
    private int sable;        // amount of sand on this tile
    private boolean bloquee;  // blocked if sable >= 2
    private boolean exploree; // whether it's been explored
    private String type;      // "normale", "oeil", "crash", "decollage", "oasis", "tunnel"

    public Zone(String type) {
        this.type = type;
        this.sable = 0;
        this.exploree = false;
        this.bloquee = false;
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

    @Override
    public String toString() {
        return "[" + type.charAt(0) + "|s:" + sable + "]";
    }
}
public class Cerchio {
    protected double diam, posX, posY;

    public Cerchio(double diam, double posX, double posY) {
        if (diam <= 0) {
            throw new IllegalArgumentException("Diametro non valido");
        }
        if (posX < 0 || posY < 0) {
            throw new IllegalArgumentException("Posizione non valida");
        }
        this.diam = diam;
        this.posX = posX;
        this.posY = posY;
    }

    public double distance(Cerchio c) {
        return Math.sqrt((posX - c.posX) * (posX - c.posX) + (posY - c.posY) * (posY - c.posY));
    }

    // Ritorna vero se c'è sovrapposizione
    public boolean checkSovrapposizione(Cerchio c) {
        // Calcolo distanza tra i due centri
        return distance(c) <= (diam / 2) + (c.getDiametro() / 2);
    }

    // Ritorna vero se il centro è dentro al mio raggio
    public boolean checkEntrata(Cerchio c) {
        return distance(c) < diam / 2;
    }

    public double getDiametro() {
        return diam;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}

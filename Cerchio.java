//classe che gestisce una generica forma a cerchio
public class Cerchio {
    //campi privati ma accessibili alle classi figlie buca e boccia
    protected double diam, posX, posY;

    //costruttore
    public Cerchio(double diam, double posX, double posY) {
        //gestione errori
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

    //calcola distanza tra 2 centri
    public double distance(Cerchio c) {
        return Math.sqrt((posX - c.posX) * (posX - c.posX) + (posY - c.posY) * (posY - c.posY));
    }

    //ritorna vero se c'è sovrapposizione tra 2 cerchi
    public boolean checkSovrapposizione(Cerchio c) {
        return distance(c) <= (diam / 2) + (c.diam / 2);
    }

    //ritorna vero se il centro di un cerchio è dentro al raggio dell'altro cerchio 
    public boolean checkEntrata(Cerchio c) {
        return distance(c) < diam / 2;
    }

    //metodi get per poter leggere e usare il diametro e la posizione del cerchio
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

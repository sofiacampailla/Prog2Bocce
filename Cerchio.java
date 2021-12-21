//classe che gestisce una generica forma a cerchio
//classe che raccoglie tutti i metodi e campi=variabili comuni tra boccia e buca
//costruttore: Cerchio A=new Cerchio(18,2,3); la uso tipo A.distance(B)
public class Cerchio {
    // sono campi privati ma accessibili alle classi figlie buca e boccia
    protected double diam, posX, posY;

    // costruttore che ha stesso nome della classe e non ha tipo di ritorno
    // gli passo ciò che serve per inizializzare l'oggetto cerchio
    // concretizza il concetto astratto
    public Cerchio(double diam, double posX, double posY) {
        // gestione errori, attiro nuova eccezione
        if (diam <= 0) {
            throw new IllegalArgumentException("Diametro non valido");
        }
        if (posX < 0 || posY < 0) {
            throw new IllegalArgumentException("Posizione non valida");
        }
        // this risolve ambiguità tra i campi della classe e i parametri del costruttore
        this.diam = diam;
        this.posX = posX;
        this.posY = posY;
    }

    // calcola distanza tra 2 centri
    // quando verrà richiamata calcola distanza tra il cerchio in questione e il
    // cerchio c
    public double distance(Cerchio c) {
        return Math.sqrt((posX - c.posX) * (posX - c.posX) + (posY - c.posY) * (posY - c.posY));
    }

    // Ritorna vero se c'è sovrapposizione tra 2 cerchi
    public boolean checkSovrapposizione(Cerchio c) {
        // Calcolo distanza tra i due centri
        return distance(c) <= (diam / 2) + (c.diam / 2);
    }

    // Ritorna vero se il centro dell'altro cerchio è dentro al raggio del mio
    // cerchio
    public boolean checkEntrata(Cerchio c) {
        return distance(c) < diam / 2;
    }

    // dato che diam è protected creo metodi get per poterli leggere e usare
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


public class Buca extends Cerchio {

	public Buca(double diam, double posX, double posY) {
		super(diam, posX, posY);
	}

	// Costruttore di copia per creare le copie di quelle in campo
	// Vedi getBuca
	public Buca(Buca b) {
		super(b.diam, b.posX, b.posY);
	}
}

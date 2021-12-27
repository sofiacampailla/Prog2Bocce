//classe che estende Cerchio, crea il concetto buca, separata da boccia
public class Buca extends Cerchio {
	//costruttore
	public Buca(double diam, double posX, double posY) {
		super(diam, posX, posY);
		if(diam>25){
			throw new IllegalArgumentException("Dimensione diametro buca non valida");
		}
	}
}

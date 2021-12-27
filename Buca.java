//classe che estende Cerchio, ovvero aggiunge metodi solo di una buca
//creo concetto buca, separata da boccia
public class Buca extends Cerchio {
	//costruttore che richiama quello della classe Cerchio
	//super poichè riprende i parametri del costruttore di Cerchio
	public Buca(double diam, double posX, double posY) {
		super(diam, posX, posY);
		if(diam>25){
			throw new IllegalArgumentException("Dimensione diametro buca non valida");
		}
	}
}

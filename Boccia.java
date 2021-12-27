// classe che estende la classe madre Cerchio, aggiungendo i metodi relativi solo alle bocce
// creo concetto boccia separata da buca
public class Boccia extends Cerchio {
	// dichiaro i campi della classe che descrivono una Boccia
	private double velX, velY;
	private boolean caduta;

	// costruttore per instanziare una nuova boccia che richiama quello di Cerchio
	// e aggiunge l'inizializzazione della velocità e lo stato di caduta
	// concretizza il concetto astratto
	public Boccia(double diam, double posX, double posY) {
		super(diam, posX, posY);
		if(diam<5 || diam>20){
			throw new IllegalArgumentException("Dimensione diametro boccia non valida");
		}
		velX = 0;
		velY = 0;
		caduta = false;
	}

	//metodo che imposta la velocità della boccia (espressa in coordinate cartesiane)
	//partendo da una velocità espressa in coordinate polari (distanza da origine e intensità)
	public void impostaVelPolare(double intensita, double angoloDirezione) {
		velX = Math.cos(angoloDirezione) * intensita;
		velY = Math.sin(angoloDirezione) * intensita;
	}

	//metodo che aggiorna la posizione della boccia in funzione del tempo trascorso
	//dall'ultimo aggiornamento e della velocità
	public void evoluzionePosizione(double DELTA_T) {
		posX += DELTA_T * velX;
		posY += DELTA_T * velY;
	}

	//metodo booleano per verificare se la boccia è caduta in una buca
	//ritorna true se la boccia è caduta
	public boolean isCaduta() {
		return caduta;
	}

	//metodo booleano per verificare se la boccia è ferma
	//ritorna true se la boccia è ferma
	public boolean isFerma() {
		return velX == 0 && velY == 0;
	}

	//override del metodo di cerchio checkEntrata per aggiornare la caduta
	//versione modificata di un metodo già implementato nella classe madre Cerchio
	public boolean checkEntrata(Buca b) { //passo una buca
		if (!caduta) {       //se la boccia non è già caduta
			caduta = b.checkEntrata(this); //controllo se la boccia è caduta nella buca 
			//b sta per buca, this per boccia
		}
		return caduta;
	}

	//metodo booleano che controlla se la boccia urta il campo
	public boolean urtoCampo(Campo c) {
		//dichiarazione variabili locali per rilevare e distinguere urti con sponde
		boolean urtoDXSX = posX + diam / 2 >= c.getXMax() || posX - diam / 2 <= 0;
		boolean urtoSuGiu = posY + diam / 2 >= c.getYMax() || posY - diam / 2 <= 0;
		// Urto in un angolo, inverto velx con vely
		if (urtoDXSX && urtoSuGiu) {
			double tempVelX = velX;
			velX = -velY;
			velY = -tempVelX;
		} else if (urtoDXSX) { // Urto destra o a sinistra
			velX = -velX;
		} else if (urtoSuGiu) { // Urto alto in basso
			velY = -velY;
		}

		return urtoDXSX || urtoSuGiu;
	}

	//metodo che ritorna vero se la boccia ne urta un'altra, fine scatto simulazione
	public boolean urtoBoccia(Boccia b) {
		if (checkSovrapposizione(b)) {
			b.velX=velX;
			b.velY=velY;
			velX = 0;
			velY = 0;
			return true;
		}
		return false;
	}
	//metodo che aggiorna la velocità della boccia a ogni scatto
	//dimConst parametro in Gioco Decremento
	public void diminuzioneVel(double dimConst) {
		//dichiarazione variabili per calcolare intensita e angoloDirezione della velocità,
		//ripasso da coordinate cartesiane a polari
		double angoloDirezione = Math.atan2(velY, velX);
		double intensita = Math.sqrt((velX * velX) + (velY * velY));
		// Applico diminuzione 
		intensita -= dimConst;
		if(intensita <= 0) { //poichè vettore non può essere negativo
			velX = 0;
			velY = 0;
			return;
		}
		// Ritorno a coordinate cartesiane
		impostaVelPolare(intensita, angoloDirezione);
	}
}
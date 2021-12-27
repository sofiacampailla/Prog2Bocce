//classe che estende la classe madre Cerchio, crea il concetto boccia, separata da buca
public class Boccia extends Cerchio {
	//dichiarazione dei campi
	private double velX, velY;
	private boolean caduta;

	//costruttore
	public Boccia(double diam, double posX, double posY) {
		super(diam, posX, posY);
		if(diam<5 || diam>20){
			throw new IllegalArgumentException("Dimensione diametro boccia non valida");
		}
		velX = 0;
		velY = 0;
		caduta = false;
	}

	//metodo che imposta la velocità della boccia
	public void impostaVelPolare(double intensita, double angoloDirezione) {
		velX = Math.cos(angoloDirezione) * intensita;
		velY = Math.sin(angoloDirezione) * intensita;
	}

	//metodo che aggiorna la posizione della boccia
	public void evoluzionePosizione(double DELTA_T) {
		posX += DELTA_T * velX;
		posY += DELTA_T * velY;
	}

	//metodo che ritorna true se la boccia è caduta
	public boolean isCaduta() {
		return caduta;
	}

	//metodo che ritorna true se la boccia è ferma
	public boolean isFerma() {
		return velX == 0 && velY == 0;
	}

	//override del metodo di cerchio checkEntrata per aggiornare la caduta
	public boolean checkEntrata(Buca b) { 
		if (!caduta) {       
			caduta = b.checkEntrata(this); 
		}
		return caduta;
	}

	//metodo che controlla se la boccia urta il campo
	public boolean urtoCampo(Campo c) {
		//distinzione urti con sponde
		boolean urtoDXSX = posX + diam / 2 >= c.getXMax() || posX - diam / 2 <= 0;
		boolean urtoSuGiu = posY + diam / 2 >= c.getYMax() || posY - diam / 2 <= 0;
		if (urtoDXSX && urtoSuGiu) {
			double tempVelX = velX;
			velX = -velY;
			velY = -tempVelX;
		} else if (urtoDXSX) {
			velX = -velX;
		} else if (urtoSuGiu) {
			velY = -velY;
		}
		return urtoDXSX || urtoSuGiu;
	}

	//metodo che ritorna vero se la boccia ne urta un'altra
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
	public void diminuzioneVel(double dimConst) {
		double angoloDirezione = Math.atan2(velY, velX);
		double intensita = Math.sqrt((velX * velX) + (velY * velY));
		intensita -= dimConst;
		if(intensita <= 0) {
			velX = 0;
			velY = 0;
			return;
		}
		impostaVelPolare(intensita, angoloDirezione);
	}
}
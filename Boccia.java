public class Boccia extends Cerchio {
	// dichiaro i campi della classe che descrivono una Boccia
	private double velX, velY;
	private boolean caduta;

	// costruttore per instanziare una nuova boccia
	public Boccia(double diam, double posX, double posY) {
		super(diam, posX, posY);
		velX = 0;
		velY = 0;
		caduta = false;
	}

	public void impostaVel(double vx, double vy) {
		velX = vx;
		velY = vy;
	}

	public void impostaVelPolare(double intensita, double angoloDirezione) {
		velX = Math.cos(angoloDirezione) * intensita;
		velY = Math.sin(angoloDirezione) * intensita;
	}

	public void evoluzionePosizione(double DELTA_T) {
		posX += DELTA_T * velX;
		posY += DELTA_T * velY;
	}

	public boolean isCaduta() {
		return caduta;
	}

	public boolean isFerma() {
		return velX == 0 && velY == 0;
	}

	// Override del metodo di cerchio per aggiornare la caduta
	public boolean checkEntrata(Cerchio c) {
		if (!caduta) {
			caduta = c.checkEntrata(this);
		}
		return caduta;
	}

	public boolean urtoCampo(Campo c) {
		boolean urtoDXSX = posX + diam / 2 >= c.getXMax() || posX - diam / 2 <= 0;
		boolean urtoSuGiu = posY + diam / 2 >= c.getYMax() || posY - diam / 2 <= 0;
		// Urto in un angolo
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

	public boolean urtoBoccia(Boccia b) {
		if (distance(b) <= diam / 2 + b.getDiametro() / 2) {
			b.impostaVel(velX, velY);
			velX = 0;
			velY = 0;
			return true;
		}
		return false;
	}

	public void diminuzioneVel(double dimConst) {
		// Calcolo intensita e angoloDirezione della velocità
		double angoloDirezione = Math.atan2(velY, velX);
		double intensita = Math.sqrt((velX * velX) + (velY * velY));
		// Applico diminuzione 
		intensita -= dimConst;
		if(intensita <= 0) {
			velX = 0;
			velY = 0;
			return;
		}
		// Ritorno a coordinate cartesiane
		impostaVelPolare(intensita, angoloDirezione);
	}
}
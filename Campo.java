public class Campo {
    // dichiaro campi della classe
    private Buca buche[];
    private double XMax = 0, YMax = 0;

    // costruttore per instanziare un nuovo campo
    public Campo(double diamBuche[], double XMax, double YMax) {
        // Controllo che le buche siano massimo 4
        if (diamBuche.length == 0 || diamBuche.length > 4) {
            throw new IllegalArgumentException("Numero di diametri non valido");
        }
        // Controllo che dimensioni non siano negative
        if(XMax <= 0 || YMax <= 0) {
            throw new IllegalArgumentException("Dimensioni campo non valido");
        }
        this.XMax = XMax;
        this.YMax = YMax;
        buche = new Buca[diamBuche.length];
        // inizializziamo gli arrays
        // inizializziamo la posizione delle buche
        for (int i = 0; i < buche.length; i++) {
            double posX, posY;
            switch (i) {
                case 0:
                    posX = diamBuche[0];
                    posY = diamBuche[0];
                    break;
                case 1:
                    posX = XMax - diamBuche[1];
                    posY = diamBuche[1];
                    break;
                case 2:
                    posX = XMax - diamBuche[2];
                    posY = YMax - diamBuche[2];
                    break;
                case 3:
                    posX = diamBuche[3];
                    posY = YMax - diamBuche[3];
                    break;
                default:
                    posX = 0;
                    posY = 0;
            }
            buche[i] = new Buca(diamBuche[i], posX, posY);
        }
    }

    // Ritorna il numero di buche che serve in Bocce
    public int numeroBuche() {
        return this.buche.length;
    }

    public Buca getBuca(int index) {
        return new Buca(buche[index]);
    }

    public double getXMax() {
        return XMax;
    }

    public double getYMax() {
        return YMax;
    }
}
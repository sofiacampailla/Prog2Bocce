//classe che descrive un campo, dimensioni e buche
public class Campo {
    // dichiaro campi della classe, cioè un array di buche e le dimensioni del campo
    private Buca buche[];
    private double XMax = 0, YMax = 0;

    // costruttore per istanziare un nuovo campo
    public Campo(double diamBuche[], double XMax, double YMax) {
        // Controllo che le buche siano massimo 4
        if (diamBuche.length == 0 || diamBuche.length > 4) {
            throw new IllegalArgumentException("Numero di diametri non valido");
        }
        // Controllo che le dimensioni siano regolari
        if(XMax <= 150 || XMax > 250 || YMax <= 150 || YMax > 250) {
            throw new IllegalArgumentException("Dimensioni campo non valide");
        }
        this.XMax = XMax;
        this.YMax = YMax;
        //nuovo array di buche lungo quanto il numero di diametri passati
        buche = new Buca[diamBuche.length];
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
            //decisa la posizione di questa buca, la costruisco
            buche[i] = new Buca(diamBuche[i], posX, posY);
        }
    }

    // Ritorna il numero di buche che serve in Bocce
    public int numeroBuche() {
        return buche.length;
    }
    // Ritorna la buca i e le dimensioni del campo
    public Buca getBuca(int index) {
        return buche[index];
    }

    public double getXMax() {
        return XMax;
    }

    public double getYMax() {
        return YMax;
    }
}
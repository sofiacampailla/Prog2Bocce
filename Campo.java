//classe che descrive un campo, dimensioni e buche
public class Campo {
    //dichiarazione dei campi
    private Buca buche[];
    private double XMax = 0, YMax = 0;

    //costruttore per istanziare un nuovo campo
    public Campo(double diamBuche[], double XMax, double YMax) {
        //controllo numero buche
        if (diamBuche.length == 0 || diamBuche.length > 4) {
            throw new IllegalArgumentException("Numero di diametri non valido");
        }
        //controllo dimensioni campo
        if(XMax <= 0 || YMax <= 0) {
            throw new IllegalArgumentException("Dimensioni campo non valide");
        }
        this.XMax = XMax;
        this.YMax = YMax;
        //nuovo array di buche
        buche = new Buca[diamBuche.length];
        //inizializziamo la posizione delle buche
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

    //ritorna il numero di buche
    public int numeroBuche() {
        return buche.length;
    }
    //ritorna una buca e le dimensioni del campo
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
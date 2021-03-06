import java.util.*;
import java.lang.Math;
import java.io.*;

//La classe Bocce implementa l'interfaccia Gioco,
//cioè una classe che contiene tutti metodi astratti, cioè non implementati, cioè senza {}
public class Bocce implements Gioco {
  public Campo campo;
  public Boccia bocce[];
  public int Ntiri = 12;
  public Boccia boccino;
  public int indiceBoccino;

  /*
   * Inizializza il gioco leggendo da file. Il file contiene nell'ordine: La
   * parola chiave GIOCO seguita da due interi che rappresentano le dimensioni del
   * campo di gioco La parola chiave BUCHE seguita da un intero N che rappresenta
   * il numero di buche (da 1 a 4) N interi positivi che rappresentano i diametri
   * delle N buche La parola chiave BOCCE seguita da un intero M che rappresenta
   * il numero di bocce (da 1 a 12) M interi positivi che rappresentano i diametri
   * delle M bocce Crea il campo da gioco con le dimensioni indicate e vi colloca
   * le N buche e le M bocce in posizioni valide (ovvero dentro al campo e senza
   * intersezioni fra loro), sceglie come boccino una fra le bocce di dimensione
   * minima. Lancia eccezione se per qualche motivo non riesce a leggere dal file.
   */
  public void inizializzaLeggendo(String nome) throws Exception {
    // Leggiamo tutto quello che riguarda il campo: dimensioni e diametro buche
    // Supposizione che il file abbia sempre lo stesso ordine
    Scanner sc = new Scanner(new File(nome));
    // GIOCO
    if (!(sc.next()).equals("GIOCO")) {
      throw new IllegalArgumentException("Parola GIOCO non presente");
    }
    double campoLenX = sc.nextDouble(); //va alla lettura del pezzo successivo e si aspetta un double
    double campoLenY = sc.nextDouble();
    // BUCHE
    if (!(sc.next()).equals("BUCHE")) {
      throw new IllegalArgumentException("Parola BUCHE non presente");
    }
    int nBuche = sc.nextInt();
    if (nBuche <= 0 || nBuche>4) {
      throw new IllegalArgumentException("Numero di buche non valido");
    }
    //istanzio array per memorizzare i diametri delle buche
    double dimBuche[] = new double[nBuche];
    for (int i = 0; i < nBuche; i++) {
      dimBuche[i] = sc.nextDouble();
    }
    // Creiamo il campo chiamando il costruttore di Campo
    campo = new Campo(dimBuche, campoLenX, campoLenY);
    // BOCCE
    if (!(sc.next()).equals("BOCCE")) {
      throw new IllegalArgumentException("Parola BOCCE non presente");
    }
    int nBocce = sc.nextInt();
    if (nBocce <= 0 || nBocce>12) {
      throw new IllegalArgumentException("Numero di bocce non valido");
    }
    //istanzio array lungo n bocce
    bocce = new Boccia[nBocce];
    // Per ogni nuova boccia
    for (int i = 0; i < nBocce; i++) {
      // Leggiamo il suo diametro
      double diamBoccia = sc.nextDouble();
      // Dobbiamo assegnare randomicamente una posizione
      boolean posizioneNonValida = true;
      //finchè la posizione della boccia non è valida
      while (posizioneNonValida) {
        // Generazione di una nuova posizione, moltiplicando numero random tra 0 e 1 per range valido
        //per posizione campo
        double bocciaPosX = Math.random() * (campoLenX - diamBoccia) + diamBoccia / 2;
        double bocciaPosY = Math.random() * (campoLenY - diamBoccia) + diamBoccia / 2;
        // Creiamo boccia con questa posizione, chiamando il costruttore di boccia, in una variabile locale
        Boccia boccia = new Boccia(diamBoccia, bocciaPosX, bocciaPosY);
        // Assumo che la posizione sia corretta
        posizioneNonValida = false;
        // Confronto se non interseca buche
        for (int k = 0; k < campo.numeroBuche(); k++) {
          if (boccia.checkSovrapposizione(campo.getBuca(k))) { //boccia temporanea confrontata con tutte le buche k
            posizioneNonValida = true;
          }
        }
        // Confronto con le altre bocce
        for (int k = 0; k < i; k++) {
          if (boccia.checkSovrapposizione(bocce[k])) { //boccia temp (boccia) di indice i 
            //è confrontata con bocce già piazzate precedentemente (k)
            posizioneNonValida = true;
          }
        }
        //salviamo comunque la boccia anche se non valida, in caso ricomincia il while
        bocce[i] = boccia;
      }
    }
    sc.close();

    // Definisce l'indice del boccino, supponendo come boccino la prima boccia e cerca boccia più grande per averla comunque più piccola di tutte le buche
    indiceBoccino = 0;
    int indiceBocciaGrande=0;
    //cerco diametro più piccolo e più grande
    for (int i = 1; i < bocce.length; i++) {
      //se il diametro della boccia i-esima è < di quello che attualmente
      //crediamo boccino, aggiorno indice boccino con i
      if (bocce[i].getDiametro() < bocce[indiceBoccino].getDiametro()) {
        indiceBoccino = i;
      }
      if (bocce[i].getDiametro() > bocce[indiceBocciaGrande].getDiametro()) {
        indiceBocciaGrande = i;
      }
    }
    for (int i = 0; i < nBuche; i++) {
      if (dimBuche[i]<bocce[indiceBocciaGrande].getDiametro()){
        throw new IllegalArgumentException("Dimensioni della buca "+(i+1)+" non valide");
      }
    }
  }

  /* Ritorna il numero di buche. */
  public int numeroBuche() {
    return campo.numeroBuche();
  }

  /*
   * Ritorna il numero totale di bocce, incluso il boccino, incluse quelle cadute
   * in buca.
   */
  public int numeroBocce() {
    return bocce.length;
  }

  /* Ritorna il numero di bocce cadute in buca. */
  public int numeroCadute() {
    int numCadute = 0;
    for (int i = 0; i < bocce.length; i++) {
      if (bocce[i].isCaduta()) { //ritorna valore del booleano
        numCadute++;
      }
    }
    return numCadute;
  }

  /*
   * Imprime al boccino una velocita' avente intensita' e direzione date,
   * preparandolo per un nuovo tiro. Ogni volta che questa funzione viene
   * chiamata, il numero di tiri a disposizione del giocatore diminuisce di uno.
   * 
   * @param intensita modulo del vettore velocita' espresso in centimetri al
   * secondo.
   * 
   * @param angoloDirezione angolo formato dal vettore velocita' misurato in
   * radianti partendo dalla direzione positiva dell'asse x.
   */
  //chiamata ogni volta che si lancia il boccino, imprime velocità e angolo ad esso
  public void preparaBoccino(double intensita, double angoloDirezione) {
    bocce[indiceBoccino].impostaVelPolare(intensita, angoloDirezione);
    Ntiri--; // Decremento numero di tiri ogni volta che c'è un tiro del boccino
  }

  /*
   * Avanza il gioco di un intervallo di tempo pari a DELTA_T, spostando la boccia
   * in moto ed eseguendo eventuali urti e cadute in buca. In assenza di urti
   * diminuisce la velocita' della boccia in moto sottraendo DECREMENTO. Nel caso
   * che la boccia in moto urti le sponde del campo, la stessa boccia viene
   * rimbalzata indietro. In caso di urto della boccia in moto contro un'altra
   * boccia, la boccia urtata si mette in moto al posto di quella urtante. Ritorna
   * vero se, dopo, una boccia e' ancora in moto. Ritorna falso se, dopo, tutte le
   * bocce sono ferme.
   */
  //metodo che si occupa della simulazione fisica del gioco, chiama equazioni del moto
  //aggiornando lo stato degli oggetti (velocità, caduta, posizione)
  public boolean evoluzioneDeltaT() {
    for (Boccia boccia : bocce) { // Equivalente a for(int i = 0; i < bocce.length; i++) { bocce[i].evoluz... }
      if(boccia.isCaduta() || boccia.isFerma()) {
        continue; //se la boccia è gia caduta o è ferma, ovvero se si verifica la condizione,
        //non faccio altri calcoli, la ignoro, continua senza eseguire istruzioni rimanenti del for
        //altrimenti controllo se la boccia non è caduta o non è ferma e faccio tutto il resto
      }
      //metodo che aggiorna la posizione della boccia in funzione del delta T
      boccia.evoluzionePosizione(DELTA_T);
      // Controlliamo se una boccia è caduta nella buca
      for (int i = 0; i < campo.numeroBuche(); i++) {
        if (boccia.checkEntrata(campo.getBuca(i))) { //per ogni buca, se la boccia è entrata nella buca i del campo, 
          return true; // scatto di simulazione termina se una boccia è caduta
        } //checkEntrata aggiorna anche lo stato di caduta della boccia
      }
      // Aggiorno velocità se la boccia urta una sponda
      if (boccia.urtoCampo(campo)) {
        return true; // Con urto campo scatto di simulazione si ferma
      }
      // Urto tra due bocce
      for (Boccia altraBoccia : bocce) { //come for(j=0;j<bocce.length;j++) {!bocce[i].isFerma && bocce[i]!=bocce[j] && ...}
        if (!boccia.isFerma() && boccia != altraBoccia && !altraBoccia.isCaduta()) {
          //se diverse a livello di puntatore in memoria e non ferma o caduta
          if (boccia.urtoBoccia(altraBoccia)) {
            return true;
          }
        }
      }
      // Diminuisce velocità della boccia, arrivo qua solo se la boccia non ha urtato e non è caduta
      boccia.diminuzioneVel(DECREMENTO);
    }
    return !bocceFerme();
  }

  /* Ritorna l'indice di boccia corrispondente al boccino. */
  public int indiceBoccino() {
    return indiceBoccino;
  }

  /* Ritorna il numero di tiri di boccino disponibili al giocatore. */
  public int numeroTiri() {
    return Ntiri;
  }

  /*
   * Stabilisce il numero di tiri disponibili, che devono essere almeno uno.
   * Solleva eccezione se l'argomento è < 1.
   * 
   * @param n numero di tiri, deve essere >= 1
   */
  public void cambiaNumeroTiri(int n) throws Exception {
    if (n < 1) {
      throw new IllegalArgumentException("Numero tiri boccino non valido");
    }
    Ntiri = n;
  }

  /*
   * Ritorna la coordinata X della posizione della boccia di indice dato; se la
   * boccia e' gia' caduta in buca, ritorna -1. Gli indici validi sono da 0 a
   * numeroBocce()-1.
   */
  public double bocciaX(int indiceBoccia) {
    if (indiceBoccia < 0 || indiceBoccia >= bocce.length) {
      throw new IllegalArgumentException("Indice boccia invalido");
    }
    if (bocce[indiceBoccia].isCaduta()) {
      return -1;
    } else {
      return bocce[indiceBoccia].getPosX();
    }
  }

  /*
   * Ritorna la coordinata Y della posizione della boccia di indice dato; se la
   * boccia e' gia' caduta in buca, ritorna -1. Gli indici validi sono da 0 a
   * numeroBocce()-1.
   */
  public double bocciaY(int indiceBoccia) {
    if (indiceBoccia < 0 || indiceBoccia >= bocce.length) {
      throw new IllegalArgumentException("Indice boccia invalido");
    }
    if (bocce[indiceBoccia].isCaduta()) {
      return -1;
    } else {
      return bocce[indiceBoccia].getPosY();
    }
  }

  /*
   * Ritorna vero sse la boccia di indice dato e' caduta in buca. Gli indici
   * validi sono da 0 a numeroBocce()-1.
   */
  public boolean caduta(int indiceBoccia) {
    if (indiceBoccia < 0 || indiceBoccia >= bocce.length) {
      throw new IllegalArgumentException("Indice boccia invalido");
    }
    return bocce[indiceBoccia].isCaduta();
  }

  /*
   * Ritorna la coordinata X della posizione della buca di indice dato. Gli indici
   * validi sono da 0 a numeroBuche()-1.
   */
  public double bucaX(int indiceBuca) {
    if (indiceBuca < 0 || indiceBuca >= campo.numeroBuche()) {
      throw new IllegalArgumentException("Indice buca invalido");
    }
    return campo.getBuca(indiceBuca).getPosX();
  }

  /*
   * Ritorna la coordinata Y della posizione della buca di indice dato. Gli indici
   * validi sono da 0 a numeroBuche()-1.
   */
  public double bucaY(int indiceBuca) {
    if (indiceBuca < 0 || indiceBuca >= campo.numeroBuche()) {
      throw new IllegalArgumentException("Indice buca invalido");
    }
    return campo.getBuca(indiceBuca).getPosY();
  }

  /* Ritorna la dimensione del tavolo lungo l'asse X. */
  public double campoX() {
    return campo.getXMax();
  }

  /* Ritorna la dimensione del tavolo lungo l'asse Y. */
  public double campoY() {
    return campo.getYMax();
  }

  /*
   * Ritorna il diametro della boccia di indice dato. Gli indici validi sono da 0
   * a numeroBocce()-1.
   */
  public double diametroBoccia(int indiceBoccia) {
    if (indiceBoccia < 0 || indiceBoccia >= bocce.length) {
      throw new IllegalArgumentException("Indice boccia invalido");
    }
    return bocce[indiceBoccia].getDiametro();
  }

  /*
   * Ritorna il diametro della buca di indice dato. Gli indici validi sono da 0 a
   * numeroBuche()-1.
   */
  public double diametroBuca(int indiceBuca) {
    if (indiceBuca < 0 || indiceBuca >= campo.numeroBuche()) {
      throw new IllegalArgumentException("Indice buca invalido");
    }
    return campo.getBuca(indiceBuca).getDiametro();
  }

  /*
   * Ritorna vero se e solo se tutte le bocce ancora presenti in campo sono ferme.
   */
  public boolean bocceFerme() {
    for (Boccia boccia : bocce) { //come for(i=0;i<bocce.length;i++) {if(!boccia[i].isFerma &&...)}
      if (!boccia.isFerma() && !boccia.isCaduta()) {
        return false;
      }
    }
    return true;
  }

  /*
   * Ritorna vero se e solo se il gioco e' finito, ovvero se il boccino e' gia'
   * caduto in buca oppure se non ci sono piu' tiri a disposizione.
   */
  public boolean giocoFinito() {
    return Ntiri == 0 || bocce[indiceBoccino].isCaduta();
  }

  /*
   * Ritorna il punteggio, pari al numero di bocce cadute in buca, escluso il
   * boccino.
   */
  public int punti() {
    int numCadute = 0;
    for (int i = 0; i < bocce.length; i++) { //come for(Boccia b:bocce) {if(b!=bocce[indiceBoccino] && b.isCaduta()...)}
      if (i != indiceBoccino && bocce[i].isCaduta()) {
        numCadute++;
      }
    }
    return numCadute;
  }
}

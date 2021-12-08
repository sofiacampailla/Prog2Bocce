import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

public class VisualizzaGioco extends JFrame {
  // conterra' un oggetto di una classe che implementa Gioco
  private Gioco gioco;
  // pannello grafico in cui si disegnera' il gioco
  private PannelloGioco p_gioco;

  // Timer che fara' avanzare l'animazione
  private Timer avanzamento;
  // Per il fattore moltiplicativo per l'intervallo del timer:
  // valore iniziale
  static final int INTERVALLO = 10;
  // valori estremi
  static final int INTERV_MIN = 5, INTERV_MAX = 15;
  // valore corrente
  int intervallo = INTERVALLO;

  // valore iniziale e valori estremi per lo zoom
  static final int ZOOM = 2;
  static final int ZOOM_MIN = 1, ZOOM_MAX = 4;

  // Per le due azioni: terminare e tirare il boccino
  private JButton b_esci, b_avvia;

  // comandi per regolare lo zoom e la moviola
  private JSlider s_zoom, s_animaz;
  private JButton b_zoom_reset, b_animaz_reset;

  // comandi per regolare la velocita' del boccino
  private JSlider s_velocita, s_angolo;
  private JButton b_vel_casuale, b_ang_casuale;

  // Per intensita' e angolo della velocita' del boccino:
  // valori iniziali
  static final int VELOCITA = 50;
  static final int ANGOLO = 45;
  // valori estremi
  static final int VELOC_MIN = 5, VELOC_MAX = 50;
  static final int ANG_MIN = 0, ANG_MAX = 360;
  // valori correnti
  private int velocitaBoccino = VELOCITA, angoloBoccino = ANGOLO;

  // per mostrare le varie informazioni
  private JLabel lab_punti, lab_tiri;
  private JLabel lab_velocita, lab_angolo;
  private JLabel lab_zoom, lab_animaz;

  /*
   * Costruisce il visualizzatore caricando il gioco dal file il cui nome e' dato
   * in argomento.
   */
  public VisualizzaGioco(String nome) {
    // EDITARE LA RIGA QUI SOTTO METTENDO IL NOME DELLA VOSTRA CLASSE
    gioco = new Bocce();
    // FINE DELL'UNICA RIGA DA EDITARE
    try {
      gioco.inizializzaLeggendo(nome);
    } catch (Exception exc) {
      trattaEccezione(exc, "Impossibile leggere il gioco dal file " + nome, true);
    }

    p_gioco = new PannelloGioco(gioco);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(p_gioco, BorderLayout.CENTER);
    getContentPane().add(b_esci = new JButton("termina programma"), BorderLayout.SOUTH);

    JPanel destro = new JPanel();
    destro.setLayout(new GridLayout(6, 1));
    destro.add(b_avvia = new JButton("tira boccino"));
    destro.add(creaPannelloTre(lab_tiri = new JLabel("Tiri rimasti: " + gioco.numeroTiri()), new JLabel(),
        lab_punti = new JLabel("Punti: 0  "), "gioco"));
    destro.add(creaPannelloTre(lab_zoom = new JLabel(), s_zoom = creaSlider(ZOOM_MIN, ZOOM_MAX, ZOOM, 1, 1),
        b_zoom_reset = new JButton("default"), "zoom"));
    destro.add(creaPannelloTre(lab_animaz = new JLabel(),
        s_animaz = creaSlider(INTERV_MIN, INTERV_MAX, intervallo = INTERVALLO, 5, 1),
        b_animaz_reset = new JButton("default"), "moviola"));
    destro.add(creaPannelloTre(lab_velocita = new JLabel(),
        s_velocita = creaSlider(VELOC_MIN, VELOC_MAX, velocitaBoccino = VELOCITA, 15, 5),
        b_vel_casuale = new JButton("casuale"), "velocita boccino"));
    destro.add(creaPannelloTre(lab_angolo = new JLabel(),
        s_angolo = creaSlider(ANG_MIN, ANG_MAX, angoloBoccino = ANGOLO, 90, 30), b_ang_casuale = new JButton("casuale"),
        "angolo boccino"));
    getContentPane().add(destro, BorderLayout.EAST);

    associaAzioni();
    creaTimer();
    aggiornaStato();
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Gioco: " + nome);
  }

  // Aggiorna tutte le scritte che vengono mostrate
  private void aggiornaStato() {
    // caso gioco finito
    if (gioco.giocoFinito()) {
      getContentPane().setBackground(Color.red);
      // se e' caduto, nei punti non si conta il boccino
      lab_punti.setText("Punti: " + gioco.punti());
      b_avvia.setEnabled(false);
      s_velocita.setEnabled(false);
      s_angolo.setEnabled(false);
      b_vel_casuale.setEnabled(false);
      b_ang_casuale.setEnabled(false);
      b_animaz_reset.setEnabled(false);
      s_animaz.setEnabled(false);
    }
    // caso gioco in corso ma al momento fermo
    else if (gioco.bocceFerme()) {
      lab_punti.setText("Punti: " + gioco.numeroCadute());
      b_avvia.setEnabled(true);
      s_velocita.setEnabled(true);
      s_angolo.setEnabled(true);
      b_vel_casuale.setEnabled(true);
      b_ang_casuale.setEnabled(true);
      b_animaz_reset.setEnabled(true);
      s_animaz.setEnabled(true);
    }
    // caso gioco al momento in azione
    else {
      b_avvia.setEnabled(false);
      s_velocita.setEnabled(false);
      s_angolo.setEnabled(false);
      b_vel_casuale.setEnabled(false);
      b_ang_casuale.setEnabled(false);
    }
    // in ogni caso
    lab_tiri.setText("Tiri rimasti: " + gioco.numeroTiri());
    lab_velocita.setText("intensita: " + velocitaBoccino + " ");
    lab_angolo.setText("angolo: " + angoloBoccino + " ");
    lab_zoom.setText("pixel/cm: " + p_gioco.zoom + " ");
    lab_animaz.setText("ritardo: " + intervallo + " ");
  }

  // associa i comportamenti ai bottoni e agli slider
  private void associaAzioni() {
    b_esci.addActionListener(new ActionListener() { // termina il programma
      public void actionPerformed(ActionEvent ev) {
        System.exit(0);
      }
    });

    b_avvia.addActionListener(new ActionListener() { // imposta la velocita' del boccino e avvia il timer della
                                                     // simulazione
      public void actionPerformed(ActionEvent ev) {
        gioco.preparaBoccino((double) velocitaBoccino, Math.toRadians((double) angoloBoccino));
        aggiornaStato();
        avanzamento.start();
      }
    });

    b_vel_casuale.addActionListener(new ActionListener() { // sceglie una velocita' casuale
      public void actionPerformed(ActionEvent ev) {
        cambiaIntensita((int) (Math.random() * 40.0 + 5.0));
      }
    });

    s_velocita.addChangeListener(new ChangeListener() { // sceglie la velocita' dello slider
      public void stateChanged(ChangeEvent e) {
        cambiaIntensita(s_velocita.getValue());
      }
    });

    b_ang_casuale.addActionListener(new ActionListener() { // sceglie un angolo casuale
      public void actionPerformed(ActionEvent ev) {
        cambiaAngolo((int) (Math.random() * 360.0));
      }
    });

    s_angolo.addChangeListener(new ChangeListener() { // sceglie l'angolo dello slider
      public void stateChanged(ChangeEvent e) {
        cambiaAngolo(s_angolo.getValue());
      }
    });

    s_zoom.addChangeListener(new ChangeListener() { // imposta lo zoom dello slider
      public void stateChanged(ChangeEvent e) {
        cambiaZoom(s_zoom.getValue());
      }
    });

    b_zoom_reset.addActionListener(new ActionListener() { // reimposta lo zoom iniziale
      public void actionPerformed(ActionEvent ev) {
        cambiaZoom(ZOOM);
        s_zoom.setValue(ZOOM);
      }
    });

    s_animaz.addChangeListener(new ChangeListener() { // imposta il ritardo dello slider
      public void stateChanged(ChangeEvent e) {
        cambiaIntervalloTimer(s_animaz.getValue());
      }
    });

    b_animaz_reset.addActionListener(new ActionListener() { // reimposta il ritardo iniziale
      public void actionPerformed(ActionEvent ev) {
        cambiaIntervalloTimer(INTERVALLO);
        s_animaz.setValue(INTERVALLO);
      }
    });
  }

  // cambia l'intensita' della velocita'
  private void cambiaIntensita(int n) {
    velocitaBoccino = n;
    s_velocita.setValue(velocitaBoccino);
    p_gioco.repaint();
    aggiornaStato();
  }

  // cambia l'angolo della velocita'
  private void cambiaAngolo(int n) {
    angoloBoccino = n;
    s_angolo.setValue(angoloBoccino);
    p_gioco.repaint();
    aggiornaStato();
  }

  // cambia il fattore di ingrandimento del disegno
  private void cambiaZoom(int n) {
    p_gioco.mettiZoom(n, gioco);
    pack();
    p_gioco.repaint();
    aggiornaStato();
  }

  // cambia l'intervallo del timer, regolando la moviola
  private void cambiaIntervalloTimer(int n) {
    intervallo = n;
    avanzamento.setDelay((int) (Gioco.DELTA_T * 50.0 * intervallo));
    if (avanzamento.isRunning())
      avanzamento.restart();
    aggiornaStato();
  }

  // crea uno slider dati i valori minimo, massimo, iniziale
  // e gli intervalli ai quali disegnare i trattini
  JSlider creaSlider(int minimo, int massimo, int iniziale, int intervalloMaggiore, int intervalloMinore) {
    JSlider nuovo = new JSlider(minimo, massimo, iniziale);
    nuovo.setPaintTicks(true);
    nuovo.setPaintLabels(true);
    nuovo.setMajorTickSpacing(intervalloMaggiore);
    nuovo.setMinorTickSpacing(intervalloMinore);
    return nuovo;
  }

  // crea un pannello con tre componenti, rispettivamente a sinistra,
  // al centro e a destra
  JPanel creaPannelloTre(JComponent c1, JComponent c2, JComponent c3, String s) {
    JPanel p = new JPanel(new BorderLayout());
    p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 2), s));
    p.add(c1, BorderLayout.WEST);
    p.add(c2, BorderLayout.CENTER);
    p.add(c3, BorderLayout.EAST);
    return p;
  }

  // crea il timer associando l'azione da eseguire, si tratta
  // di far andare avanti il gioco di un delta di tempo
  void creaTimer() {
    ActionListener avanzaUnPasso = new ActionListener() { // azione da compiere ad ogni scatto del timer
      public void actionPerformed(ActionEvent ev) {
        gioco.evoluzioneDeltaT();
        p_gioco.repaint();
        aggiornaStato();
        if (gioco.bocceFerme()) {
          avanzamento.stop(); // ferma il timer
          if (gioco.giocoFinito()) {
            JOptionPane.showMessageDialog(VisualizzaGioco.this, "GIOCO FINITO!\n" + "PUNTEGGIO: " + gioco.punti());
            System.exit(0);
          }
        }
      }
    };
    avanzamento = new Timer((int) (Gioco.DELTA_T * 50.0 * intervallo), avanzaUnPasso);
  }

  /* Classe interna per disegnare il gioco. */
  protected class PannelloGioco extends JPanel {
    int eps = 10; // larghezza del bordo in pixel
    int zoom; // quanti pixel stanno in un centimetro

    int trasformaX(double x) {
      return eps + (int) (x * zoom);
    }

    int trasformaY(double y) {
      return eps + (int) ((gioco.campoY() - y) * zoom);
    }

    void mettiZoom(int z, Gioco gioco) {
      zoom = z;
      setPreferredSize(new Dimension(2 * eps + (int) (zoom * gioco.campoX()), 2 * eps + (int) (zoom * gioco.campoY())));
    }

    /* Crea il pannello per disegnare il gioco dato. */
    public PannelloGioco(Gioco gioco) {
      mettiZoom(ZOOM, gioco);
    }

    private void disegnaBoccia(Graphics2D g, int i) {
      int ix = trasformaX(gioco.bocciaX(i));
      int iy = trasformaY(gioco.bocciaY(i));
      int id = (int) (zoom * gioco.diametroBoccia(i));
      Shape tondo = new Ellipse2D.Double(ix - id / 2, iy - id / 2, id, id);
      if (i == gioco.indiceBoccino())
        g.setPaint(new GradientPaint(ix, iy, Color.red, ix + id / 2, iy + id / 2, Color.white));
      else
        g.setPaint(new GradientPaint(ix, iy, Color.magenta, ix + id / 2, iy + id / 2, Color.white));
      g.fill(tondo);
      g.setColor(Color.black);
      g.draw(tondo);
      g.drawString(("" + i), ix + id / 2, iy + id / 2);
    }

    private void disegnaBuca(Graphics2D g, int i) {
      int ix = trasformaX(gioco.bucaX(i));
      int iy = trasformaY(gioco.bucaY(i));
      int id = (int) (zoom * gioco.diametroBuca(i));
      g.setColor(Color.black);
      g.fill(new Ellipse2D.Double(ix - id / 2, iy - id / 2, id, id));
      g.setColor(Color.gray);
      for (int w = id - 2; w > 2; w -= 8)
        g.draw(new Ellipse2D.Double(ix - w / 2, iy - w / 2, w, w));
    }

    /* Da JPanel. Funzione che disegna il contenuto di questo pannello. */
    public void paintComponent(Graphics g) {
      // il campo
      g.setColor(Color.gray);
      g.fillRect(0, 0, getWidth(), getHeight());
      g.clearRect(eps, eps, (int) (zoom * gioco.campoX()), (int) (zoom * gioco.campoY()));
      Graphics2D g2 = (Graphics2D) g;
      g.setColor(Color.blue);
      int x0 = trasformaX(0);
      int y0 = trasformaY(0);
      int x1 = trasformaX(gioco.campoX());
      int y1 = trasformaY(gioco.campoY());
      g.drawLine(x0, y0, x0, y1);
      g.drawLine(x1, y0, x1, y1);
      g.drawLine(x0, y0, x1, y0);
      g.drawLine(x0, y1, x1, y1);
      // le bocce
      for (int i = 0; i < gioco.numeroBocce(); i++) {
        if (!gioco.caduta(i))
          disegnaBoccia(g2, i);
      }
      // le buche
      g.setColor(Color.black);
      for (int i = 0; i < gioco.numeroBuche(); i++)
        disegnaBuca(g2, i);
      // segmento che segna la velocita' del boccino
      if (gioco.bocceFerme() && !gioco.giocoFinito() && (velocitaBoccino > 0)) {
        double bx = gioco.bocciaX(gioco.indiceBoccino());
        double by = gioco.bocciaY(gioco.indiceBoccino());
        x0 = trasformaX(bx);
        y0 = trasformaY(by);
        double alpha = Math.toRadians((double) angoloBoccino);
        x1 = trasformaX(bx + (double) velocitaBoccino * Math.cos(alpha));
        y1 = trasformaY(by + (double) velocitaBoccino * Math.sin(alpha));
        g.drawLine(x0, y0, x1, y1);
      }
    }
  } // fine classe interna

  /*
   * Chiede il livello di difficolta' per il gioco (numero di tiri) mediante una
   * finestra apposita.
   */
  public void chiediLivello() {
    try {
      String s = JOptionPane.showInputDialog(this,
          "Livello di difficolta' per il gioco\n" + "(numero di tiri disponibili, deve essere >0)\n"
              + "Scegliere CANCEL conferma il default = " + gioco.numeroTiri() + " tiri");
      if (s == null)
        return; // caso CANCEL
      int k = Integer.parseInt(s.trim());
      gioco.cambiaNumeroTiri(k);
      lab_tiri.setText("Tiri rimasti: " + gioco.numeroTiri());
    } catch (Exception exc) {
      trattaEccezione(exc, "Valore non valido, viene usato il default\n" + gioco.numeroTiri() + " tiri", false);
    }
  }

  /*
   * Se la finestra grafica del visualizzatore e' aperta, mostra il messaggio in
   * una finestra apposita, altrimenti lo mostra sulla finestra testuale da cui e'
   * stato lanciato il programma.
   * 
   * @param e eccezione che rappresenta l'errore avvenuto
   * 
   * @param commento commento da mostrare
   * 
   * @param termina se vero, dopo mostrato il messaggio deve termina il programma,
   * altrimenti no.
   */
  public void trattaEccezione(Exception e, String commento, boolean termina) {
    String msg = "Errore: " + e.getClass().getName() + ": " + e.getMessage() + "\n" + commento + "\n";
    if (termina)
      msg += "\nIl programma termina!";
    if (isVisible()) // errore con finestra grafica gia' aperta
    {
      msg += "Vuoi vedere i dettagli dell'eccezione?";
      int k = JOptionPane.showConfirmDialog(this, msg, "Errore", JOptionPane.YES_NO_OPTION);
      if (k == JOptionPane.YES_OPTION)
        e.printStackTrace();
    } else // errore durante l'inizializzazione
    {
      System.err.println(msg);
      System.err.println("Vuoi vedere i dettagli dell'eccezione? (n=no, altro=si)");
      try {
        BufferedReader aux = new BufferedReader(new InputStreamReader(System.in));
        String s = aux.readLine().trim();
        if (!s.equals("n"))
          e.printStackTrace();
      } catch (java.io.IOException ee) {
        /* fittizio: non si dovrebbe finire mai qui */ }
    }
    if (termina)
      System.exit(1);
  }

  /**
   * Main: crea il visualizzatore caricando il gioco dal file il cui nome e' stato
   * messo sulla command-line.
   */
  public static void main(String[] arg) {
    if (arg.length < 1) {
      System.err.println("Errore: manca il nome del file da cui leggere il gioco");
      return;
    } else if (arg.length > 1) {
      System.err.println("Attenzione: troppi parametri, verra' usato solo il primo");
    }
    System.out.println("Carico il gioco dal file " + arg[0]);
    VisualizzaGioco v = new VisualizzaGioco(arg[0]);
    v.setVisible(true);
    v.chiediLivello();
  }

}
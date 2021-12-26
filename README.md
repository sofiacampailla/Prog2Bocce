# Esercitazione Java gioco Bocce
Come prima cosa si è creata una classe Cerchio che gestisca una generica forma a cerchio. Questa raccoglie tutti i metodi e i campi comuni tra una boccia e una buca, ovvero il diametro, la posizione nel campo, la distanza tra due cerchi e il controllo di una possibile loro sovrapposizione.

Successivamente si sono create le due classi Buca e Boccia che estendono la classe madre Cerchio, contenenti le caratteristiche più specifiche relative ai due oggetti: la classe Buca riprende i parametri del costruttore di Cerchio, creando il concetto di buca, separata da boccia, la classe Boccia contiene invece la velocità e la direzione che assume una boccia in moto, aggiorna la sua posizione ad ogni intervallo di tempo, controlla se è caduta o meno in una buca e tutti i possibili urti, tra due bocce, o con le sponde del campo.

Inoltre si crea la classe Campo, che descrive appunto il campo, le sue dimensioni e le posizioni delle sue buche. 

La classe Bocce invece, implementa l'interfaccia Gioco (che contiene tutti metodi astratti per definizione). Legge il file, individua il boccino, il numero di buche e bocce, imprime le velocità agli oggetti in moto, aggiorna lo stato degli oggetti ad ogni scatto di simulazione, stabilisce il numero di tiri e il punteggio del giocatore, insieme a tutti gli altri metodi che necessitano implementazione da VisualizzaGioco. 


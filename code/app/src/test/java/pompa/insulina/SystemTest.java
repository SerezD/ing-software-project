/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pompa.insulina;

import org.junit.Test;

import static org.junit.Assert.*;

public class SystemTest {


    // Il sensore genera un valore di conducibilità Elettrica ogni 5 secondi
    // se il valore generato è 0 o 100, il sensore smette di generare valori (morte del paziente)
    @Test public void testRequirement1() throws InterruptedException {

        Sistema.test_mode = true;

        Sistema sistema = new Sistema();

        for (int i = 0; i < 5; i++){

            Thread tsensore = sistema.lanciaSensore();
            Thread.sleep(2000);

            //potrebbe aver generato 0 o 100 --> il sensore è morto
            if (sistema.getSensore().getCondElettrica() == 0 || sistema.getSensore().getCondElettrica() == 100){

                //il Thread non c'è più
                assertFalse("Il thread non è più attivo", tsensore.isAlive());
                //Riprovo un altro giro
                continue;
            }

            //Altrimenti il valore generato è cambiato dal default (0)
            assertNotEquals("Generato un valore", 0, sistema.getSensore().getCondElettrica(), 0.0);
            tsensore.interrupt();
            break;
        }

    }

    // se il valore generato è 0 o 100, il sensore smette di generare valori (morte del paziente)
    @Test public void testRequirement2() throws InterruptedException {

        Sistema.test_mode = true;

        //Forzo il sensore a generare 100 per testarne la morte
        Sistema sistema = new Sistema();

        //Con queste condizioni, il valore generato rimane a 100
        sistema.getSensore().setCondElettrica(100);
        sistema.getSensore().setUltimaDose(0);

        Thread tsensore = sistema.lanciaSensore();
        Thread.sleep(1000);
        assertFalse("Il thread non è più attivo", tsensore.isAlive());

    }

     // Il controllore legge la conducibilità elettrica ogni 10 secondi e calcola il livello degli zuccheri
     // Il controllore aggiorna il display sugli zuccheri
    @Test public void testRequirement3() throws InterruptedException {

        Sistema.test_mode = true;
        Sistema sistema = new Sistema();

        //prima lettura del controllore e calcolo degli zuccheri
        sistema.getSensore().setCondElettrica(45.0);

        Thread tcontrollore = sistema.lanciaControllore();
        Thread.sleep(1000);

        assertEquals("calcola bene gli zuccheri", 4.5, sistema.getControllore().getZuccheri(), 0.0);
        assertEquals("aggiorna il display", 4.5, sistema.getDisplay().getZuccheri(), 0.0);

        //seconda lettura dopo 10 secondi
        sistema.getSensore().setCondElettrica(41.0);
        Thread.sleep(10000);

        assertEquals("ogni 10 secondi legge il valore", 4.1, sistema.getControllore().getZuccheri(), 0.0);

        tcontrollore.interrupt();
    }

    /*
     * Un livello critico degli zuccheri è tra 0 e 3 o tra 7 e 10. Questo causerà il lancio dell'emergenza
     * L'emergenza verrà risolta con la visualizzazione del messaggio da parte dell'utente (nuova inizializzione dei valori)
     */
    @Test public void testRequirement4() throws InterruptedException {

        Sistema.test_mode = true;
        Sistema sistema = new Sistema();

        //livello critico
        sistema.getSensore().setCondElettrica(20.0);

        Thread tcontrollore = sistema.lanciaControllore();
        Thread.sleep(3000);

        //emergenza lanciata e risolta
        assertTrue("emergenza risolta", sistema.getSensore().getCondElettrica() > 30 && sistema.getSensore().getCondElettrica() < 70);
        tcontrollore.interrupt();
    }

    // il controllore calcola la dose e se è > 0 procede all'iniezione.
    // l'iniezione è simulata sottraendo la dose corretta dal serbatoio e aggiornando il sensore sulla dose somministrata
    // Nota: Il metodo calcolaDose() è testato in "ControlloreTest" e funziona.
    @Test public void testRequirement5() throws InterruptedException {

        Sistema.test_mode = true;
        Sistema sistema = new Sistema();

        //imposto i parametri iniziali per far generare una dose pari a 0.
        sistema.getSensore().setCondElettrica(36.0);
        Thread tcontrollore = sistema.lanciaControllore();
        Thread.sleep(1000);
        assertEquals("il serbatoio è ancora pieno", 20, sistema.getSerbatoio().getDosi());
        assertEquals("dose precedente del sensore è 0", 0, sistema.getSensore().getUltimaDose());
        tcontrollore.interrupt();

        //Faccio in modo che venga generata una dose pari a 5
        sistema.getSensore().setCondElettrica(80.0);
        sistema.getSensore().setUltimaDose(0);
        sistema.getControllore().setDosePrecedente(0);
        sistema.getControllore().setZuccheri0(0.0);
        sistema.getControllore().setZuccheri1(0.0);
        sistema.getSerbatoio().riempi();

        Thread tcontrollore2 = sistema.lanciaControllore();
        Thread.sleep(1000);
        assertEquals("serbatoio ha 15 dosi", 15, sistema.getSerbatoio().getDosi());
        assertEquals("dose precedente del sensore è 5", 5, sistema.getSensore().getUltimaDose());
        tcontrollore2.interrupt();
    }

    // dopo aver somministrato una dose, il controllore aggiorna il display.
    // se rimangono meno di 5 dosi nel serbatoio, viene stampato il messaggio per riempirlo.
    @Test public void testRequirement6() throws InterruptedException {

        Sistema.test_mode = true;
        Sistema sistema = new Sistema();

        //Faccio in modo che venga generata una dose pari a 5 e che questo provochi lo svuotamento del serbatoio
        sistema.getSerbatoio().sottraiDosi(9);
        assertEquals("Il serbatoio ha ora 11 dosi", 11, sistema.getSerbatoio().getDosi());

        sistema.getSensore().setCondElettrica(80.0);
        sistema.getControllore().setDosePrecedente(0);
        Thread tcontrollore = sistema.lanciaControllore();

        Thread.sleep(2000);
        assertEquals("il display è aggiornato", 6, sistema.getDisplay().getDosiResidue());

        sistema.getSensore().setCondElettrica(80.0);
        sistema.getControllore().setDosePrecedente(0);

        Thread.sleep(10000);
        assertEquals("calcolo una dose di 5", 5, sistema.getControllore().getDosePrecedente());
        assertEquals("Il serbatoio si è riempito dopo l'emergenza", 20, sistema.getSerbatoio().getDosi());

        tcontrollore.interrupt();
    }

}
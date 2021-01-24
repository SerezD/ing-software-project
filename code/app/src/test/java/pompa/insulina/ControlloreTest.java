/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pompa.insulina;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ControlloreTest {

    @Test public void testConstructor() {

        Controllore controllore = new Controllore();
        assertTrue("valori iniziali a 0", controllore.getZuccheri() == 0 && controllore.getDosePrecedente() == 0 && controllore.getZuccheri0() == 0);
    }

    @Test public void testcalcolaZuccheri(){

        Controllore controllore = new Controllore();

        for (int i = 0; i < 100; i++){
            controllore.calcolaZuccheri(Math.random()*100);
            assertTrue("zuccheri calcolati tra 0 e 10", controllore.getZuccheri() >= 0 && controllore.getZuccheri0() <= 10);
        }
    }

    @Test public void testcalcolaDose(){

        Controllore controllore = new Controllore();

        for (int i = 0; i < 1000; i++){

            //genero valori casuali degli attributi
            controllore.setDosePrecedente( (int) (Math.random()*5) );
            assertTrue("dose precedente: " + controllore.getDosePrecedente(), controllore.getDosePrecedente() >= 0 && controllore.getDosePrecedente() <=5  );
            controllore.setZuccheri0(Math.random()*10);
            assertTrue("zuccheri precedenti: " + controllore.getZuccheri0(), controllore.getZuccheri0() >= 0 && controllore.getZuccheri0() <=10  );
            controllore.setZuccheri1(Math.random()*10);
            assertTrue("zuccheri: " + controllore.getZuccheri(), controllore.getZuccheri() >= 0 && controllore.getZuccheri() <=10  );
            int doseCalcolata = controllore.calcolaDose();

            if ( (controllore.getZuccheri0() > controllore.getZuccheri() && controllore.getZuccheri() < 7) || controllore.getZuccheri() <= 5){
                assertEquals("se il tasso è in discesa e non critico o zuccheri1 <= 5 niente dose: " + doseCalcolata, 0, doseCalcolata);
            }

            if (controllore.getZuccheri() > controllore.getZuccheri0() && controllore.getZuccheri() > 5 && controllore.getZuccheri() < 7){
                assertTrue("se il tasso è in salita e il livello non è critico somministro massimo 2 dosi: " + doseCalcolata, doseCalcolata < 3);
            }

            if (controllore.getZuccheri() > 7){
                assertEquals("massima dose: " + doseCalcolata + " con dose precedente: " + controllore.getDosePrecedente(), doseCalcolata, (5 - controllore.getDosePrecedente()));
            }
        }
    }
}

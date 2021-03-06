/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pompa.insulina;

import java.lang.Math;

public class Sensore {

    //attributi
    private double condElettrica;
    private int generazioni;
    private int ultimaDose;

    //costruttore
    public Sensore(){
        condElettrica = (Math.random() * 40) + 30; //cond Elettrica iniziale è casuale tra 30 e 70
        generazioni = 0;
        ultimaDose = 0;
    }

    //metodi
    synchronized public double getCondElettrica(){
        return condElettrica;
    }

    synchronized public void setUltimaDose(int ultimaDose){
        this.ultimaDose = ultimaDose;
        this.generazioni = 0;
    }

    synchronized public void inizializzazione(){
        condElettrica = (Math.random() * 40) + 30;
    }

    synchronized public void generaValore() {

        //nuova generazione
        generazioni += 1;

        //c'è sempre un 2% che il numero sia completamente casuale tra 0 e 100
        if (Math.random() < 0.02 && !(Sistema.test_mode)){
            condElettrica = Math.random()*100;
        }
        else {
            if (ultimaDose > generazioni){
                //scende di un valore tra 0 e 2d/t
                double discesa = Math.random()*( (float) (2* ultimaDose/generazioni) );
                //non può mai andare sotto lo 0
                condElettrica = Math.max(0.0, condElettrica - discesa);
            }
            else{
                //scende con probabilità d/t
                double prob = (float) (ultimaDose/generazioni);
                double lancio = Math.random();
                if (lancio <= prob){
                    //scendo
                    double discesa = Math.random()*( (float) (2* ultimaDose/generazioni) );
                    //non può mai andare sotto lo 0
                    condElettrica = Math.max(0.0, condElettrica - discesa);
                }
                else{
                    //salgo di un valore casuale tra 0 e t/(2d+1), mai sopra 100
                    double salita = Math.random()*( (float) (generazioni/(2*ultimaDose + 1)) );
                    condElettrica = Math.min(100, condElettrica + salita);
                }
            }
        }
    }

    // metodi per il test
    public int getGenerazioni(){return generazioni;}
    public int getUltimaDose(){return ultimaDose;}
    public void setCondElettrica(double test){ condElettrica = test;}
}

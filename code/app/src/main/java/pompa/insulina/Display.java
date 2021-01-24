/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pompa.insulina;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.TreeMap;
import java.time.*;


public class Display{

    //attributi
    private final TreeMap<String, String[]> registrazioni;
    private double zuccheri;
    private int dosiResidue;
    private boolean allarmeSerbatoio;
    private boolean allarmeZuccheri;

    //Attributi grafici
    private JLabel etichettaT;
    private JLabel etichettaZ;
    private JLabel etichettaD;
    private JTable valori;

    //costruttore
    public Display(){

        // Parametri settati a Default
        zuccheri = 0.0;
        dosiResidue = 20;
        registrazioni = new TreeMap<>();
        allarmeSerbatoio = true;
        allarmeZuccheri = true;

        //Inizializzazione Componenti Grafici
        if (!Sistema.test_mode){

            //Finestra Principale
            JFrame f = new JFrame("Sistema avviato: " + LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8));
            f.setSize(new Dimension(500,582));
            f.setResizable(false);
            f.setLocationRelativeTo(null);
            f.setLayout(new BorderLayout());
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //Blocco Nord (etichette)
            JPanel nord = new JPanel();
            nord.setBackground(Color.BLACK);
            nord.setOpaque(true);
            nord.setPreferredSize(new Dimension (500,124));
            nord.setLayout(new BoxLayout(nord,BoxLayout.Y_AXIS));

            //Blocco Centro (tabella)
            JPanel centro = new JPanel();
            centro.setLayout(new BoxLayout(centro,BoxLayout.Y_AXIS));

            //Parametro Tempo
            etichettaT = new JLabel("");
            etichettaT.setHorizontalAlignment(SwingConstants.CENTER);
            etichettaT.setAlignmentX(Component.CENTER_ALIGNMENT);
            etichettaT.setMaximumSize(new Dimension(484, 40));
            etichettaT.setBackground(new Color(235,255,255));
            etichettaT.setOpaque(true);

            //Parametro Zuccheri
            etichettaZ = new JLabel("");
            etichettaZ.setHorizontalAlignment(SwingConstants.CENTER);
            etichettaZ.setAlignmentX(Component.CENTER_ALIGNMENT);
            etichettaZ.setMaximumSize(new Dimension(484, 40));
            etichettaZ.setBackground(new Color(237,255,255));
            etichettaZ.setOpaque(true);

            //Parametro Dosi Residue
            etichettaD = new JLabel("");
            etichettaD.setHorizontalAlignment(SwingConstants.CENTER);
            etichettaD.setAlignmentX(Component.CENTER_ALIGNMENT);
            etichettaD.setMaximumSize(new Dimension(484, 40));
            etichettaD.setBackground(new Color(235,255,255));
            etichettaD.setOpaque(true);

            //Tabella Cronologica Centrale (20 Valori)
            String[] colonne = {"Data", "Ora", "Zuccheri", "Serbatoio", "Operazione"};
            String[][] parametri = {{"","","","",""}, {"","","","",""}, {"","","","",""},
                    {"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},
                    {"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},
                    {"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},
                    {"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""}};

            TableModel model = new DefaultTableModel(parametri,colonne){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

            };

            valori = new JTable(model);

            //Dimensioni Righe e Colonne
            valori.setRowHeight(20);
            valori.getColumnModel().getColumn(0).setPreferredWidth(78);
            valori.getColumnModel().getColumn(1).setPreferredWidth(75);
            valori.getColumnModel().getColumn(2).setPreferredWidth(72);
            valori.getColumnModel().getColumn(3).setPreferredWidth(100);
            valori.getColumnModel().getColumn(4).setPreferredWidth(175);

            //Centramento Valori
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(JLabel.CENTER);
            valori.getColumnModel().getColumn(0).setCellRenderer(renderer);
            valori.getColumnModel().getColumn(1).setCellRenderer(renderer);
            valori.getColumnModel().getColumn(2).setCellRenderer(renderer);
            valori.getColumnModel().getColumn(3).setCellRenderer(renderer);
            valori.getColumnModel().getColumn(4).setCellRenderer(renderer);

            //Header
            JTableHeader header = valori.getTableHeader();
            header.setFont(new Font("Courier New", Font.BOLD, 13));
            header.setReorderingAllowed(false);

            //Assemblaggio finale
            nord.add(Box.createRigidArea(new Dimension(500,1)));
            nord.add(etichettaT);
            nord.add(Box.createRigidArea(new Dimension(500,1)));
            nord.add(etichettaZ);
            nord.add(Box.createRigidArea(new Dimension(500,1)));
            nord.add(etichettaD);
            nord.add(Box.createRigidArea(new Dimension(500,1)));
            centro.add(header);
            centro.add(valori);
            centro.add(Box.createRigidArea(new Dimension(500,1)));

            f.add(nord, BorderLayout.NORTH);
            f.add(centro, BorderLayout.CENTER);
            f.setVisible(true);
        }

    }

    //metodi
    synchronized public void setAllarmeSerbatoio(boolean flag){ allarmeSerbatoio = flag; }

    synchronized public void setAllarmeZuccheri(boolean flag){ allarmeZuccheri = flag; }

    synchronized public boolean getAllarmeSerbatoio(){ return allarmeSerbatoio; }

    synchronized public boolean getAllarmeZuccheri(){ return allarmeZuccheri; }

    synchronized public int getDosiResidue(){
        return dosiResidue;
    }

    synchronized public double getZuccheri(){
        return zuccheri;
    }

    synchronized public void aggiornaZuccheri(double zuccheri){
        this.zuccheri = zuccheri;
    }

    synchronized public void aggiornaDosi(int dosiResidue){
        this.dosiResidue = dosiResidue;
    }

    synchronized public void aggiungiRegistrazione(String registrazione){

        //Creazione della chiave:  data + ora + random
        String chiave =  LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0,8) + String.format("%05d", (int) (Math.random() * 10000));

        //Rimozione valori vecchi (massimo 20 valori)
        if (registrazioni.size() == 20){
            registrazioni.remove(registrazioni.firstKey());
        }

        //Creazione e inserimento valore: zuccheri + dosi + operazione
        String [] tmp  = {String.format("%.2f", zuccheri), String.format("%02d", dosiResidue), registrazione};
        registrazioni.put(chiave, tmp);
    }

    synchronized  public void stampa(){

        if (!Sistema.test_mode){

            //Aggiornamento etichette
            etichettaT.setText("Ultimo Aggiornamento: " + LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0,8));
            etichettaZ.setText("Livello Corrente degli Zuccheri: " + String.format("%.2f", zuccheri));

            //Gestione colori (Rosso in Emergenza)
            if (zuccheri < 3 || zuccheri > 7){
                etichettaZ.setForeground(Color.RED);
            }
            else{
                etichettaZ.setForeground(Color.BLACK);
            }
            etichettaD.setText("Livello Serbatoio di Insulina: " + String.format("%02d", dosiResidue));
            if (dosiResidue < 5){
                etichettaD.setForeground(Color.RED);
            }
            else{
                etichettaD.setForeground(Color.BLACK);
            }

            //Aggiornamento tabella
            Object[] chiavi = registrazioni.descendingKeySet().toArray();

            for (int i = 0; i < 20; i++){
                if (registrazioni.size() > i){
                    valori.setValueAt(chiavi[i].toString().substring(0,10), i,0);
                    valori.setValueAt(chiavi[i].toString().substring(11,19), i,1);
                    valori.setValueAt(registrazioni.get(chiavi[i])[0], i,2);
                    valori.setValueAt(registrazioni.get(chiavi[i])[1], i,3);
                    valori.setValueAt(registrazioni.get(chiavi[i])[2], i,4);
                }
                else{
                    valori.setValueAt(" ", i,0);
                    valori.setValueAt(" ", i,1);
                    valori.setValueAt(" ", i,2);
                    valori.setValueAt(" ", i,3);
                    valori.setValueAt(" ", i,4);
                }
            }
        }
    }

    // metodo per i test
    public TreeMap<String, String[]> getRegistrazioni(){return registrazioni;}
}
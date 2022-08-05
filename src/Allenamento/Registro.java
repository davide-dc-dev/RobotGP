package Allenamento;

import java.io.Serializable;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import Utility.Istruzione;


public class Registro implements Serializable {
  
    
    private DefaultListModel<Istruzione> istruzioniDaEseguire;
    private DefaultListModel<Istruzione> istruzioniEseguite;
    
    public Registro() {
        istruzioniDaEseguire= new DefaultListModel<Istruzione>();
        istruzioniEseguite=new DefaultListModel<Istruzione>();
    }
    
    public DefaultListModel<Istruzione> getIstruzioniDaEseguire() {
        return istruzioniDaEseguire;
    }
    
    public DefaultListModel<Istruzione> getIstruzioniEseguite() {
        return istruzioniEseguite;
    }
    
    
    public boolean remove(Istruzione istruzione) {
        if(istruzione!=null) {
            int i=0;
            while(i<istruzioniDaEseguire.size()) {
                if(istruzioniDaEseguire.get(i).getTipoScheda().equals(istruzione.getTipoScheda())) {
                    istruzioniDaEseguire.remove(i);
                    return true;
                }
                i++;
            }
            return false;
                
        }else
            return false;
    }
    
    /*
    public void stampaRegistro(JTextArea console) {
        
        int i=1;
        console.append("********** REGISTRO UTENTE *******************"+"\n");
        for(Istruzione var: istruzioniDaEseguire) {
            console.append("Istruzione num :"+i+" "+var+"\n");
            i++;
        }
        console.append("********** ISTRUIZIONI ESEGUITE *******************"+"\n");
        for(Istruzione var: istruzioniEseguite) {
            console.append("Istruzione num :"+i+" "+var+"\n");
            i++;
        }
        
        console.append("*************************************************"+"\n");
    }
*/
    
    public int sizeIstruzioni() {
        return istruzioniDaEseguire.size();
    }
    
    
    public boolean modificaOrdine(String s) {
        if(istruzioniDaEseguire!=null && istruzioniDaEseguire.size()>1) {
            if(s=="SpostaAvanti") {
                shiftRight();
                return true;
            }
            else {
                shiftLeft();
                return true;
            }
        }
        return false;
    }
    
    private void shiftRight() {
       int size=istruzioniDaEseguire.size();
       Istruzione last = istruzioniDaEseguire.get(size-1);

        // shift right
        for( int index =size-2; index >= 0 ; index-- )
            istruzioniDaEseguire.set(index+1, istruzioniDaEseguire.get(index));

        // wrap last element into first slot
        istruzioniDaEseguire.set(0, last);
       
    }
    
    private void shiftLeft() {
        int size=istruzioniDaEseguire.size();
        Istruzione start=null;
        for(int i = 0; i < size; i++){
            if(i == 0)
                start = istruzioniDaEseguire.get(i);
            if(i == (size -1))
            {
                istruzioniDaEseguire.set(i, start);
                break;
            }    
            istruzioniDaEseguire.set(i, istruzioniDaEseguire.get(i+1));
        }
                
   }

   
    
}
    





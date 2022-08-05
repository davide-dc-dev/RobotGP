package Partita;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.DefaultListModel;
import Utility.Istruzione;

/**
 *
 * @author John Hundred
 */
public class PoolGlobale {
    
    private final ArrayList<Istruzione> pool;
    private final ArrayList<Istruzione> poolCestino;
    
    private static PoolGlobale singleInstance;
    
    public PoolGlobale() {
        pool=new ArrayList(84);
        poolCestino=new ArrayList<>();
        
        for(int i=1;i<=6;i++)
            pool.add(new Istruzione("U-turn",i*10));
        
        for(int i=1;i<=18;i++)
            pool.add(new Istruzione("Turn Left",50+(i*20)));
        
        for(int i=1;i<=18;i++)
            pool.add(new Istruzione("Turn Right",60+(i*20)));
        
        for(int i=1;i<=6;i++)
            pool.add(new Istruzione("Back-up",420+(i*10)));
        
        for(int i=1;i<=18;i++)
            pool.add(new Istruzione("Move 1",480+(i*10)));
        
        for(int i=1;i<=12;i++)
            pool.add(new Istruzione("Move 2",660+(i*10)));
        
        for(int i=1;i<=6;i++)
            pool.add(new Istruzione("Move 3",780+(i*10)));
        
    }
    
    public static PoolGlobale getInstance() {
        if (PoolGlobale.singleInstance == null) {
           PoolGlobale.singleInstance = new PoolGlobale();
        }
        return PoolGlobale.singleInstance;
    }
    
    public void stampaPool() {
        for(Istruzione istr :pool)
            System.out.println(istr+" "+istr.getPriorita());
    }
    
    public ArrayList<Istruzione> getPool(){
        return pool;
    }
    
    public ArrayList<Istruzione> getPoolCestino(){
        return poolCestino;
    }
    
    
    
    private  synchronized  void removePoolElement(int index) {
        Istruzione istr=pool.remove(index);
        poolCestino.add(istr);
        if(pool.isEmpty()) {
            for(Istruzione istruzione: poolCestino) {
                pool.add(istruzione);
            }
            poolCestino.clear();
        }
          
    }
    
    public PoolGiocatore generaPoolGiocatore(int puntiSalute) {
        return new PoolGiocatore(puntiSalute);
    }
    
    public PoolGiocatore generaPoolGiocatore(int puntiSalute,int dim) {
        return new PoolGiocatore(puntiSalute,dim);
    }
    
    
   
    
    public class PoolGiocatore  {
    
        private DefaultListModel <Istruzione> poolPersonale;

        public PoolGiocatore(int puntiSalute) {
            
            poolPersonale=new DefaultListModel<>();
           
            for(int i=0;i<puntiSalute-1;i++) {
                
                System.out.println("Pool size"+pool.size());
                synchronized (this) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0,pool.size());
                    System.out.println("Sono prima di addPoolElement");
                    addPoolPersonaleElement(randomNum);
                    removePoolElement(randomNum);

                }

            }
        }
        
        public PoolGiocatore(int puntiSalute,int dim) {
            poolPersonale=new DefaultListModel<>();
            int dimPool=((puntiSalute-1)-dim);
            for(int i=0;i<dimPool;i++) {
                
                
                synchronized (this) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0,pool.size());
                    System.out.println("Sono prima di addPoolElement");
                    addPoolPersonaleElement(randomNum);
                    removePoolElement(randomNum);

                }

            }
            
            
            
        }
        
        
        public synchronized void addPoolPersonaleElement(int index) {
            poolPersonale.addElement(pool.get(index));
        
        }
        
        public  DefaultListModel<Istruzione> getPoolPersonale() {
            return poolPersonale;
        }
    
    
    }
    
}

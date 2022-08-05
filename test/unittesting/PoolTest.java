package unittesting;

import Partita.PoolGlobale;
import Partita.PoolGlobale.PoolGiocatore;
import Utility.Istruzione;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marco
 */
public class PoolTest {
    
    @Test
    public void priorityTest1(){
        PoolGlobale poolT = new PoolGlobale();
        ArrayList<Integer> schede = new ArrayList<Integer>();
        for(int i=0; i<84; i++)
            if(poolT.getPool().get(i).getTipoScheda().equals("U-turn"))
                schede.add(poolT.getPool().get(i).getPriorita());
        
        ArrayList<Integer> schedeTest = new ArrayList<Integer>();
        schedeTest.add(10);
        schedeTest.add(20);
        schedeTest.add(30);
        schedeTest.add(40);
        schedeTest.add(50);
        schedeTest.add(60);
        
        assertEquals(schedeTest, schede); 
        
        
    }
    
    @Test
    public void priorityTest2(){
        PoolGlobale poolT = new PoolGlobale();
        ArrayList<Integer> schede = new ArrayList<Integer>();
        for(int i=0; i<84; i++)
            if(poolT.getPool().get(i).getTipoScheda().equals("Back-up"))
                schede.add(poolT.getPool().get(i).getPriorita());
        
        ArrayList<Integer> schedeTest = new ArrayList<Integer>();
        schedeTest.add(10 + 420);
        schedeTest.add(20 + 420);
        schedeTest.add(30 + 420);
        schedeTest.add(40 + 420);
        schedeTest.add(50 + 420);
        schedeTest.add(60 + 420);
        
        assertEquals(schedeTest, schede);       
    }
    
    @Test
    public void removeFromPoolTest1(){
       PoolGlobale poolT = new PoolGlobale();
       PoolGiocatore poolG = poolT.generaPoolGiocatore(10, 1);
       int sizePool = poolT.getPool().size();
       assertEquals(76, sizePool);
    }
    
    @Test
    public void removeFromPoolTest2(){
       PoolGlobale poolT = new PoolGlobale();
       PoolGiocatore poolG = poolT.generaPoolGiocatore(10);
       int sizePool = poolT.getPool().size();
       assertEquals(75, sizePool);
    }
    
    @Test
    public void removeFromPoolTest3(){
       PoolGlobale poolT = new PoolGlobale();
       PoolGiocatore poolG = poolT.generaPoolGiocatore(10);
       int sizePoolCestino = poolT.getPoolCestino().size();
       assertEquals(9, sizePoolCestino);
    }
}

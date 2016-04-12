/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Stats;

import nosteel.Modules.Aiming;
import nosteel.Modules.Data.EnemyData;
import nosteel.Modules.Data.FiredBullet;
import nosteel.Modules.DataList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mabarthe
 */
public class BulletHitRate extends Statistic {
    
    private List<HitRate> statData;
    private static String[] colHeader = {"round", "enemy", "total", "on the way", "hit", "hit rate", "hit wrong", "missed", "missed rate", "Algo Linear Targeting", "Algo Pattern Match"};
    
    public BulletHitRate (DataList s, Aiming t)
    {
        super( s, t, BulletHitRate.class.getName());
        statData = new ArrayList<HitRate>();
    }

    @Override
    public void analyse() {
        
        statData.clear();
        ListIterator it = scans.getEnemyListIterator(DataList.FRONT_OF_LIST);
        while( it.hasNext() )
        {
            EnemyData enemy = (EnemyData) it.next();
            ListIterator fired = enemy.getFiredBulletIterator();

            HitRate h = new HitRate();
            h.name = enemy.getName();
            
            while(fired.hasNext() )
            {
                FiredBullet fb = (FiredBullet) fired.next();
                if( fb.isOnTheWay )
                {
                    h.bulletsOnTheWay++;
                }
                else
                {
                    if(fb.hitTheTarget)
                    {
                        h.bulletsHit++;
                    }
                    else
                    {
                        if( fb.hitAnotherTarget )
                        {
                            h.bulletsHitWrong++;
                        }
                        else
                        {
                            h.bulletsMissed++;
                        }
                    }
                }
                
                if( fb.aimingAlgo == Aiming.INDEX_LinearTargeting )
                    h.algoLinerTargeting++;
                if( fb.aimingAlgo == Aiming.INDEX_PatterMatch )
                    h.algoPatternMatch++;
                
            }
            statData.add(h);
        }        
    }

    @Override
    public void printCsvHeadline() {
//        try {
//            csvFilePrinter.printRecord((Object[]) colHeader);
//            csvFilePrinter.flush();
//        } catch (IOException ex) {
//            Logger.getLogger(BulletHitRate.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void printCsvRow( int round ) {
//        for( HitRate h: statData )
//        {
//            try {
//                String[] row = new String[ colHeader.length ];
//                row[0] = String.format("%d", round);
//                row[1] = String.format("%s", h.name);
//                row[2] = String.format("%.0f", h.getTotalNumberOfBullets());
//                row[3] = String.format("%.0f", h.bulletsOnTheWay);
//                row[4] = String.format("%.0f", h.bulletsHit);
//                row[5] = String.format("%.2f", h.getHitRate()*100);
//                row[6] = String.format("%.0f", h.bulletsHitWrong);
//                row[7] = String.format("%.0f", h.bulletsMissed);
//                row[8] = String.format("%.2f", h.getMisRate()*100);
//                row[9] = String.format("%.2f", h.algoLinerTargeting / (h.algoLinerTargeting+h.algoPatternMatch));
//                row[10] = String.format("%.2f", h.algoPatternMatch / (h.algoLinerTargeting+h.algoPatternMatch));
//    
//                csvFilePrinter.printRecord((Object[]) row);
//                csvFilePrinter.flush();
//                
//            } catch (IOException ex) {
//                Logger.getLogger(BulletHitRate.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

    @Override
    public void print() {

        for( HitRate h: statData )
        {

            System.out.println( String.format("%s total %.1f, hitrate: %.1f%%", h.name, h.getTotalNumberOfBullets(), h.getHitRate()*100));
            System.out.println( h.toString() );
        }
    }

    
    
}

class HitRate
{
    public String name;
    public double bulletsOnTheWay = 0;
    public double bulletsHit = 0;
    public double bulletsHitWrong = 0;
    public double bulletsMissed = 0;
    public double algoLinerTargeting = 0;
    public double algoPatternMatch = 0;
    
    public double getTotalNumberOfBullets()
    {
        return bulletsOnTheWay + bulletsHit + bulletsMissed;
    }
    
    public double getHitRate()
    {
        return bulletsHit / getTotalNumberOfBullets();
    }

    public double getMisRate()
    {
        return bulletsMissed / getTotalNumberOfBullets();
    }
    
    public String toString()
    {
        return String.format("%s way %.0f hit %.0f mis %.0f", name, bulletsOnTheWay, bulletsHit, bulletsMissed);
    }
}
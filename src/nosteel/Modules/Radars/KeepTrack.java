package nosteel.Modules.Radars;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import nosteel.Modules.DataList;
import nosteel.Modules.Data.EnemyData;
import nosteel.Modules.Data.ScanData;
import robocode.util.Utils;

/**
 *
 * @author mabarthe
 */
public class KeepTrack extends RadarAlgo {
    
    public KeepTrack (  DataList scans_ )
    {
         super(scans_);
    }

    @Override
    public double getRadarTurnAngel(long time, double radarHeading) {
        
        double radarTurn;
        EnemyData enemy = scans.getEnemyData(target);
        if( enemy == null )
        {
            radarTurn = Double.POSITIVE_INFINITY;
        }
        else
        {
            ScanData scan = enemy.getLastScan();
            if( Math.abs(scan.time - time) > 1)
            {
                radarTurn = Double.POSITIVE_INFINITY;
            }
            else
            {
                radarTurn = Utils.normalRelativeAngle( scan.absBearing - radarHeading);
            }
        }
            
        return radarTurn;
    }
    

    
            
}

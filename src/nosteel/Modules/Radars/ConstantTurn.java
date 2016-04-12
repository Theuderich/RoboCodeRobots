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
public class ConstantTurn extends RadarAlgo {
    
    public ConstantTurn (  DataList scans_ )
    {
         super(scans_);
    }

    @Override
    public double getRadarTurnAngel(long time, double radarHeading) {
        
        return Double.POSITIVE_INFINITY;
    }
    

    
            
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules;

import nosteel.Modules.Radars.ConstantTurn;
import nosteel.Modules.Radars.KeepTrack;
import nosteel.Modules.Radars.RadarAlgo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mabarthe
 */
public class Radar {
    
    public static int INDEX_ConstantTurn = 0;
    public static int INDEX_KeepTrack = 1;
    
    private List<RadarAlgo> list;

    private int activeIndex;
    
    public Radar (DataList scans_ )
    {
        list = new ArrayList<RadarAlgo>();
        list.add( new ConstantTurn( scans_ ));
        list.add( new KeepTrack( scans_ ));
        activeIndex = INDEX_ConstantTurn;
                
    }
    
    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public double getRadarTurnAngel(long time, double radarHeading)
    {
        return list.get(activeIndex).getRadarTurnAngel(time, radarHeading);
    }
    
    public void setTarget( String name )
    {
        for( RadarAlgo r : list )
            r.setTarget(name);
    }
    
}

package nosteel.Modules.Radars;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import nosteel.Modules.DataList;

/**
 *
 * @author mabarthe
 */
public class RadarAlgo {
    
    protected DataList scans;
    protected String target;
    
    public RadarAlgo( DataList scans_ )
    {
        scans = scans_;
    }
    
    public double getRadarTurnAngel( long time, double radarHeading )
    {
        return 0;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
    
    
}

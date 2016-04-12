/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Move;

import nosteel.Basics.Vector;
import nosteel.Modules.Aiming;
import nosteel.Modules.LinearTracking;
import nosteel.Modules.Data.ScanData;
import nosteel.Modules.DataList;
import java.awt.Graphics2D;
import robocode.util.Utils;

/**
 *
 * @author mabarthe
 */
public class Hunt extends MoveAlgo {
    
    
    public Hunt (DataList s, Aiming t)
    {
        super( s, t);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void process(Vector myPos_, double myHeading_) {
        super.process(myPos_, myHeading_);
        String target = aiming.getTargetName();
        if( target == null )
            return;

        ScanData res = scans.getEnemyData(target).getLastScan();
        
        double dir = res.vDirToHim.getDirection();
        
        this.finalAngle = Utils.normalRelativeAngle(dir - myHeading_);
        this.finalDist = res.vDirToHim.getLength() - 200;
        
    }
    
}

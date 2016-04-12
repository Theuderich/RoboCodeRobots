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
import java.util.Random;
import robocode.util.Utils;

/**
 *
 * @author mabarthe
 */
public class Sidestep extends MoveAlgo {
    
    private static double APPROACH_ANGEL = 0 * 2 * Math.PI / 360;
    private static int MAX_DIST = 50;
    private static int MIN_DIST = 15;
    
    private static int MIN_BORDER_DIST = 50;
    private boolean tooClose;
    
    private boolean clockwise;
    private int distance;
    private Random rand;
    public Sidestep (DataList s, Aiming t)
    {
        super( s, t);
        clockwise = true;
        distance = 0;
        rand = new Random();
        tooClose = false;
    }

    private boolean checkCloseToBorder()
    {
        if( myPos.x < 50 )
            return true;
        
        if( myPos.y < 50 )
            return true;

        if( (this.battleSize.x - myPos.x) < 50)
            return true;
            
        if( (this.battleSize.y - myPos.y) < 50)
            return true;

        return false;
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

        if( checkCloseToBorder() )
        {
            if( tooClose == false )
            {
                distance = 0;
                tooClose = true;
            }
            
        }
        else
        {
            tooClose=false;
        }
        
        if( distance == 0 )
        {
            clockwise = !clockwise;
            distance = rand.nextInt((MAX_DIST - MIN_DIST) + 1) + MIN_DIST;
        }
            
        distance--;
        
                
        ScanData scan = scans.getEnemyData(target).getLastScan();
        
        double dir = scan.vDirToHim.getDirection();
        
        if( clockwise )
        {
            dir += Math.PI/2 - APPROACH_ANGEL;
            this.finalDist = Double.POSITIVE_INFINITY;
        }
        else
        {
            dir += Math.PI/2 + APPROACH_ANGEL ;
            this.finalDist = Double.NEGATIVE_INFINITY;
        }
        
        this.finalAngle = Utils.normalRelativeAngle(dir - myHeading_);
        
//        System.out.println(String.format("distance %d clockwise %b finalAngle %.1f finalDist %.1f", distance, clockwise, finalAngle*360/2/Math.PI, finalDist));
        
    }
    
}

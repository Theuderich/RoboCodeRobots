/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Move;

import nosteel.Basics.Vector;
import nosteel.Modules.Aiming;
import nosteel.Modules.LinearTracking;
import nosteel.Modules.DataList;
import java.awt.Graphics2D;

/**
 *
 * @author mabarthe
 */
public class MoveAlgo {
    
    protected DataList scans;
    protected Aiming aiming;
    protected Vector battleSize;

    protected Vector myPos;
    protected double myHeading;
    
    protected double finalAngle;
    protected double finalDist;
    
    public MoveAlgo (DataList s, Aiming t)
    {
        scans = s;
        aiming = t;
        finalAngle = 0;
        finalDist = 0;
        
    }

    public void setBattlefieldSize( Vector size )
    {
        battleSize = size;
    }
    
    public void intitalize()
    {
        
    }
    
    public void process( Vector myPos_, double myHeading_ )
    {
        myPos = myPos_;
        myHeading = myHeading_;
    }

    public double getFinalRelativeAngle() {
        return finalAngle;
    }

    public double getFinalDist() {
        return finalDist;
    }
        
    public void draw(Graphics2D g)
    {
    }
}

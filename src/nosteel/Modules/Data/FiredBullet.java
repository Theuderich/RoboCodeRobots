/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Data;

import nosteel.Basics.Vector;
import robocode.Bullet;

/**
 *
 * @author mabarthe
 */
public class FiredBullet {
    public Bullet b;
    public double targetDirection;
    public Vector expectedPos;
    public boolean isOnTheWay;
    public boolean hitTheTarget;
    public boolean hitAnotherTarget;
    public int aimingAlgo;
    public String targetName;
    public double probability;
    public double confidence;
    
    public boolean isMissed()
    {
        return (isOnTheWay == false) && (hitTheTarget=false) && (hitAnotherTarget == false); 
    }
    
    public void setHit()
    {
        isOnTheWay = false;
        hitTheTarget = true;
    }
    
    public void setHitWrong()
    {
        isOnTheWay = false;
        hitAnotherTarget = true;
    }

    public void setMissed()
    {
        isOnTheWay = false;
    }
    
    
}

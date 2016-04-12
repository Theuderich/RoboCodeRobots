/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Targeting;

import nosteel.Basics.Vector;
import nosteel.Modules.Data.FiredBullet;
import nosteel.Modules.DataList;
import java.awt.Graphics2D;

/**
 *
 * @author mabarthe
 */
public class Aim {
    
    protected DataList scans;
    protected Vector battleSize;

    private String targetName;
    private Vector myCurPos;
    private double firepower;
    
//    protected double targetDirection;
    protected FiredBullet firedData;
    
    public Aim( DataList scans_ )
    {
        scans = scans_;
        targetName = null;
        myCurPos = null;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return targetName;
    }
    
    public void setBattlefieldSize( Vector size )
    {
        battleSize = size;
    }

    public boolean processAiming(  Vector myCurPos, double firePower, long time )
    {
        return false;
    }
    
    public void printAimingData(long time)
    {
    }
    
    public void drawTargeting(Graphics2D g)
    {
    }

    public Vector getMyCurPos() {
        return myCurPos;
    }

    public void setMyCurPos(Vector myCurPos) {
        this.myCurPos = myCurPos;
    }

    public void setFirepower(double firepower) {
        this.firepower = firepower;
    }

    public double getFirepower() {
        return firepower;
    }
    
    public double getBulletVelocity() {
        return 20 - 3 * firepower;
    }

//    public double getTargetDirection() {
//        return targetDirection;
//    }

    public FiredBullet getFiredData() {
        return firedData;
    }
    
    
    
}

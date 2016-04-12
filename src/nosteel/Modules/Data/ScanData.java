/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Data;

import nosteel.Basics.Draw;
import nosteel.Basics.Vector;
import java.awt.Graphics2D;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.List;
import robocode.Bullet;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;


/**
 *
 * @author mabarthe
 */
public class ScanData {

    /**
     * My Data
     */
    public Vector vMyPos;
    public long   time;
    
    /**
     * Enemy Data
     */
    public double absBearing;
    public double distance;
    public double heading;
    public double velocity;
    public double energy;

    /**
     * His Data Transfered into Vectors 
     */
    public Vector vDirToHim;
    public Vector vHisPos;
    public Vector vHisMovement;
    
    public ScanData ( ScannedRobotEvent event, Vector myPos, double myHeadingRad, long time_ )
    {
        this.vMyPos = myPos;
        this.time = time_;
        
        this.absBearing = Utils.normalAbsoluteAngle(event.getBearingRadians() + myHeadingRad);
        this.distance = event.getDistance();
        this.heading = Utils.normalAbsoluteAngle(event.getHeadingRadians());
        this.velocity = event.getVelocity();
        this.energy = event.getEnergy();
        
        determineHisPosition();
        determineHisMovement();
    }
    
    private void determineHisPosition()
    {
        vDirToHim = new Vector();
        vDirToHim.y = cos( absBearing ) * distance;
        vDirToHim.x = sin( absBearing ) * distance;
        
        vHisPos = vMyPos.add(vDirToHim);
    }
    
    private void determineHisMovement()
    {
        vHisMovement = new Vector();
        vHisMovement.x = sin( heading ) * velocity;
        vHisMovement.y = cos( heading ) * velocity;
    }
    
    public void printScanData()
    {
        System.out.println(String.format("Scan Data at turn: %d", time));
        vMyPos.print("MyPos");
        System.out.println(String.format("absBearing: %.1f distance %.1f", absBearing, distance));
        System.out.println(String.format("heading: %.1f velocity %.1f", heading, velocity));
        System.out.println(String.format("energy: %.1f", energy));
        vDirToHim.print("DirToHim");
        vHisPos.print("HisPos");
        vHisMovement.print("HisMovement");
    }
    
    public void drawScan( Graphics2D g )
    {
        Draw.drawPoint(g, vHisPos, 4);
    }    
    
}

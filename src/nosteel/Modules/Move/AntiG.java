/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Move;

import nosteel.Basics.Draw;
import nosteel.Basics.Vector;
import nosteel.Modules.Aiming;
import nosteel.Modules.LinearTracking;
import nosteel.Modules.Data.EnemyData;
import nosteel.Modules.Data.ScanData;
import nosteel.Modules.DataList;
import java.awt.Graphics2D;
import static java.lang.Math.PI;
import java.util.ListIterator;
import robocode.util.Utils;

/**
 *
 * @author mabarthe
 */
public class AntiG extends MoveAlgo {
    
    static int NUMBER_OF_ENEMIES = 100;
    static Vector[] enemyPoints = new Vector[NUMBER_OF_ENEMIES];
    int countAdmin;    
    int countEnemy;

    private double angle;
    private Vector force;    
 
//    private double finalAngle;
//    private double finalDist;
    
    public AntiG (DataList s, Aiming t)
    {
        super( s, t);
        countAdmin = 0;
        countEnemy = 0;
        angle = 0;
        force = new Vector();
//        finalAngle = 0;
//        finalDist = 0;
    }

    @Override
    public void intitalize() {
        constructDirectWalls();
    }

//    @Override
//    public double getFinalDist() {
//        return finalDist;
//    }
//
//    @Override
//    public double getFinalRelativeAngle() {
//        return finalAngle; 
//    }
//
    @Override
    public void process(Vector myPos_, double myHeading_) {
        super.process(myPos_, myHeading_); 
        updateDirectWalls();
        updateEnemies();
        calcForce(myPos);
        calcAngel(myHeading);
    }
    
    public void constructDirectWalls()
    {
        enemyPoints[countAdmin++] = new Vector();
        enemyPoints[countAdmin++] = new Vector();
        enemyPoints[countAdmin++] = new Vector();
        enemyPoints[countAdmin++] = new Vector();
    }
    
    public void updateDirectWalls()
    {
        enemyPoints[0].x = myPos.x;
        enemyPoints[0].y = 0;
        
        enemyPoints[1].x = 0;
        enemyPoints[1].y = myPos.y;
        
        enemyPoints[2].x = myPos.x;
        enemyPoints[2].y = battleSize.y;
        
        enemyPoints[3].x = battleSize.x;
        enemyPoints[3].y = myPos.y;
    }
  
    private void updateEnemies()
    {
        countEnemy=0;
        ListIterator it = null;
        it = scans.getEnemyListIterator(DataList.FRONT_OF_LIST );
        while (it.hasNext() )
        {
            EnemyData eId = (EnemyData) it.next();
            ScanData scan = eId.getLastScan();
            if( scan == null )
                continue;
            
            enemyPoints[countAdmin+countEnemy++]=new Vector(scan.vHisPos.x,scan.vHisPos.y);
        }
    }
    
    private void calcForce( Vector myPos )
    {
        int i;
        double absBearing;
        double distance;
        Vector t;
        force.clear();
        for(i=0;i<NUMBER_OF_ENEMIES && enemyPoints[i] != null;i++){
            t = myPos.substract(enemyPoints[i]);
            absBearing = t.getDirection();
            distance = t.getLength();
            force.x -= Math.sin(absBearing) / (distance * distance);
            force.y -= Math.cos(absBearing) / (distance * distance);
        }

        angle = Utils.normalAbsoluteAngle(force.getDirection() + PI);
    }
    
    private void calcAngel ( double myHeading )
    {
        if (force.isNull()) {
            // If no force, do nothing
        } else if(Math.abs(angle-myHeading)<Math.PI/2){
            finalAngle = Utils.normalRelativeAngle(angle-myHeading);
            finalDist = Double.POSITIVE_INFINITY;
        } else {
            finalAngle = Utils.normalRelativeAngle(angle+Math.PI-myHeading);
            finalDist = Double.NEGATIVE_INFINITY;
        }
        
    }
    
    public void draw(Graphics2D g)
    {
        g.setColor(java.awt.Color.BLUE);
        for(int i=0;i<NUMBER_OF_ENEMIES && enemyPoints[i] != null;i++)
        {

            Vector t = myPos.substract(enemyPoints[i]);
//            Draw.drawVectorAtPosition(g, enemyPoints[i], t);
//            Draw.drawLineBetweenVectors(g, myPos, enemyPoints[i]);
            Draw.drawPoint(g, enemyPoints[i], 10);
        }
        Draw.drawVectorAtPosition(g, myPos, force);
        
    }
            
    
}

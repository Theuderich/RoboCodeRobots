/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules;

import nosteel.Modules.Data.FiredBullet;
import nosteel.Basics.Vector;
import nosteel.Basics.Draw;
import nosteel.Modules.Data.EnemyData;
import nosteel.Modules.Data.ScanData;
import java.awt.Graphics2D;
import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.util.ListIterator;
import robocode.Bullet;

/**
 *
 * @author mabarthe
 */
public class LinearTracking {
    
    private DataList scans;
    private Vector battleSize;

    private String lockToTargetName;
    
    private ScanData scout;
    private double bulletVelocity;
    private double timeDiff;
    private double alpha;
    private double betta;
    private double gamma;
//    private Vector myCurPos;
    private Vector hisMovementInThisLoop;
    private Vector hisPositionInThisLoop;
    private Vector newEnemyDirection;
    private Vector hisExpectedMovement;
    private Vector hisExpectedFinalPos;
    private Vector bullDist;
    private Vector enemyDist;
    
    private double y1;
    private double y2;
    private double timeToTarget;
    private long expectedHitTime;
    private double targetDirection;
    
    public LinearTracking( DataList scans_ )
    {
        scans = scans_;
        scout = null;
        lockToTargetName = null;
    }

    public void setBattlefieldSize( Vector size )
    {
        battleSize = size;
    }

//    public void attachBulletToScan(Bullet b)
//    {
//        FiredBullet e = new FiredBullet();
//        e.b = b;
//        e.expectedPos = hisExpectedFinalPos;
//        e.startedAt = myCurPos;
//        e.hisPosAtStart = hisPositionInThisLoop;
//        e.hisExpectedMovement = hisExpectedMovement;
//        e.hisMovementTillNow = hisMovementInThisLoop;
//        e.expectedHitTime = expectedHitTime;
//        e.isOnTheWay = true;
//        e.hitTheTarget = false;
//        e.hitAnotherTarget = false;
//        e.alpha = alpha;
//        e.betta = betta;
////        scout.bullets.add(e);
//    }
//    
//    public void setMyCurrentPos( Vector pos )
//    {
//        myCurPos = pos;
//    }
//    
//    public void setTarget( String name )
//    {
//        lockToTargetName = name;
//    }
//    
//    public String getTargetBot()
//    {
//        return lockToTargetName;
//    }
//    
//    public boolean isClockwise ()
//    {
//        double expected = scout.vDirToHim.add(scout.vHisMovement).getDirection();
//        double diff = scout.vDirToHim.getDirection() - expected;
//        boolean clockwise = diff < 0;
//        return clockwise;
//    }
//    
//    private void calcBulletVelocity (double firePower)
//    {
//        bulletVelocity = 20-3*firePower;
//    }
//
//    public void calcNewEnemyDirection( double time )
//    {
//        timeDiff = time - scout.time ;
//        hisMovementInThisLoop = scout.vHisMovement.multiply(timeDiff);
//        hisPositionInThisLoop = scout.vHisPos.add(hisMovementInThisLoop);
//        newEnemyDirection = hisPositionInThisLoop.substract(myCurPos);
//    }
//    
//    public void calcAlpha()
//    {
//        alpha = PI - newEnemyDirection.getAngelBetween(scout.vHisMovement);
//    }
//    
//    public void calcTimeToTarget( )
//    {
////        double tP1 = pow(scout.velocity,2) * timeDiff;
//        double tP2 = scout.velocity * newEnemyDirection.getLength() * cos( alpha );
//        double tP3 = pow(scout.velocity,2) - pow(bulletVelocity,2);
////        double tP = (tP1 - tP2) / tP3;
//        double tP = ( - tP2) / tP3;
//        
////        double tQ1 = pow(scout.velocity,2) * pow(timeDiff,2);
//        double tQ2 = pow(newEnemyDirection.getLength(),2);
////        double tQ3 = 2 * scout.velocity * timeDiff * newEnemyDirection.getLength() * cos( alpha );
//        double tQ4 = pow(scout.velocity,2) - pow(bulletVelocity,2);
////        double tQ = ( tQ1 + tQ2 - tQ3 ) / tQ4;
//        double tQ = ( tQ2 ) / tQ4;
//        
//        y1 = -tP + sqrt(pow(tP,2)-tQ);
//        y2 = -tP - sqrt(pow(tP,2)-tQ);
//        timeToTarget = Math.ceil(y1);
//    }
//    
//    public void calcBetta()
//    {
//        if( Double.isNaN(timeToTarget) )
//        {
//            hisExpectedFinalPos = scout.vHisPos;
//            hisExpectedMovement = new Vector();
//        }
//        else
//        {
//            hisExpectedMovement = scout.vHisMovement.multiply(timeToTarget);
//            hisExpectedFinalPos = hisPositionInThisLoop.add( hisExpectedMovement );
//            
//        }
//        bullDist = hisExpectedFinalPos.substract( myCurPos );
//        enemyDist = hisExpectedFinalPos.substract( hisPositionInThisLoop );
//     
////        betta = acos( ( pow(bulletVelocity*timeToTarget,2) + pow(newEnemyDirection.getLength(),2) - pow((scout.velocity*timeDiff + scout.velocity*timeToTarget),2)) / (2*bulletVelocity*timeToTarget*newEnemyDirection.getLength()));
////        betta = acos( ( pow(bulletVelocity*timeToTarget,2) + pow(newEnemyDirection.getLength(),2) - pow((scout.velocity*timeToTarget),2)) / (2*bulletVelocity*timeToTarget*newEnemyDirection.getLength()));
////        gamma = acos( ( pow(bulletVelocity*timeToTarget,2) + pow(newEnemyDirection.getLength(),2) - pow((scout.velocity*timeToTarget),2)) / (2*bulletVelocity*timeToTarget*newEnemyDirection.getLength()));
//        
//        betta = asin(enemyDist.getLength() / bullDist.getLength() * sin( alpha ));
//        gamma = asin( newEnemyDirection.getLength() / bullDist.getLength() * sin( alpha ));
//    }
//    
//    public void checkBattlefielBorders( )
//    {
//        boolean recalc = false;
//        if( hisExpectedFinalPos.x < 18 )
//        {
//            hisExpectedFinalPos.x = 18;
//            recalc = true;
//        } else if( hisExpectedFinalPos.x > (battleSize.x-18) )
//        {
//            hisExpectedFinalPos.x = battleSize.x-18;
//            recalc = true;
//        }
//        if( hisExpectedFinalPos.y < 18 )
//        {
//            hisExpectedFinalPos.y = 18;
//            recalc = true;
//        } else if( hisExpectedFinalPos.y > (battleSize.y-18) )
//        {
//            hisExpectedFinalPos.y = battleSize.y-18;
//            recalc = true;
//        }
//         
//        if( recalc )
//        {
//            bullDist = hisExpectedFinalPos.substract( myCurPos );
//            enemyDist = hisExpectedFinalPos.substract( hisPositionInThisLoop );
//            betta = asin(enemyDist.getLength() / bullDist.getLength() * sin( alpha ));
//
//            hisExpectedMovement = hisExpectedFinalPos.substract(hisPositionInThisLoop);
//        }
//    }
//    
//    public void checkClockwise()
//    {
//        targetDirection = newEnemyDirection.getDirection();
//        if( scout.vHisMovement.getLength() != 0)
//        {
//            if( isClockwise() )
//                targetDirection += betta;
//            else
//                targetDirection -= betta;
//        }
//    }
//
//    public double processAiming(  Vector myCurPos, double firePower, long time )
//    {
//        if( lockToTargetName == null )
//            return 0;
//        
//        scout = scans.getEnemyData(lockToTargetName ).getLastScan();
//        if( scout == null )
//            return 0;
//        
//        setMyCurrentPos(myCurPos);
//        calcBulletVelocity(firePower);
//        
//        calcNewEnemyDirection( time );
//        calcAlpha();
//        calcTimeToTarget();
//        calcBetta();
//        
//        checkBattlefielBorders();
//        checkClockwise();
//
//        expectedHitTime = time + (long)timeToTarget;
//        return targetDirection;
//    }
//    
//    public void printAimingData(long time)
//    {
//        System.out.println(String.format("EXPECT time: %d timeDiff %.1f Hit: %.1f X: %.1f Y: %.1f", time, timeDiff, time + timeToTarget, hisExpectedFinalPos.x, hisExpectedFinalPos.y));
//        scout.vMyPos.print("- MyPos");
//        myCurPos.print("- myCurPos");
//        scout.vHisPos.print("- HisPos");
//        hisMovementInThisLoop.print("- hisMovementInThisLoop");
//        hisPositionInThisLoop.print("- hisPositionInThisLoop");
//        newEnemyDirection.print("- newEnemyDirection");
//        System.out.println(String.format("- alpha: %.1f", alpha*360/2/PI));
//        System.out.println(String.format("- y1: %.1f y2: %.1f", y1, y2));
//        System.out.println(String.format("- timeToTarget: %.1f", timeToTarget));
//        System.out.println(String.format("- betta: %.1f", betta*360/2/PI));
//        System.out.println(String.format("- gamma: %.1f", gamma*360/2/PI));
//        System.out.println(String.format("- all: %.1f", gamma*360/2/PI + betta*360/2/PI + alpha*360/2/PI));
//
//        
//    }
//    
//    public void drawTracking(Graphics2D g)
//    {
//        g.setColor(java.awt.Color.RED);
//
////        scout = scans.getLastScout( lockToTargetName );
//        if( scout == null )
//            return;
//        
//        Vector hisTemporaryPos = scout.vHisPos.add( scout.vHisMovement.multiply(timeDiff));
//        
//        Draw.drawLineBetweenVectors( g, scout.vMyPos, scout.vHisPos);
//        Draw.drawPoint( g, scout.vMyPos, 6 );
//        Draw.drawPoint( g, scout.vHisPos, 6 );
//        
//        Draw.drawLineBetweenVectors( g, myCurPos, hisTemporaryPos  );
//        Draw.drawLineBetweenVectors(g, hisTemporaryPos, hisExpectedFinalPos );
//        Draw.drawLineBetweenVectors(g, myCurPos, hisExpectedFinalPos );
//        
////        hisExpectedFinalPos.print("hisExpectedFinalPos");
////        System.out.println("alpha " + this.alpha);
////        System.out.println("v " + scout.velocity);
////        System.out.println("timeToTarget " + timeToTarget);
////        hisExpectedFinalPos.print("Expected");
//    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Targeting;

import nosteel.Basics.Draw;
import nosteel.Basics.Vector;
import nosteel.Modules.Aiming;
import nosteel.Modules.Data.FiredBullet;
import nosteel.Modules.DataList;
import nosteel.Modules.Data.ScanData;
import java.awt.Graphics2D;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 *
 * @author mabarthe
 */
public class LinearTargeting extends Aim{

    private ScanData scan;
    
    private double timeDiff;

    private Vector hisMovementInThisLoop;
    private Vector hisPositionInThisLoop;
    private Vector hisDirectionInThisLoop;
    
    private double alpha;
    private double betta;
    private double gamma;
    
    private double timeToTarget;
    
    private Vector hisExpectedMovement;
    private Vector hisExpectedFinalPos;
    private Vector bullDist;
    private Vector enemyDist;
    
    public LinearTargeting(DataList scans_) 
    {
        super(scans_);
    }
    
    public boolean isClockwise ()
    {
        double expected = scan.vDirToHim.add(scan.vHisMovement).getDirection();
        double diff = scan.vDirToHim.getDirection() - expected;
        boolean clockwise = diff < 0;
        return clockwise;
    }

    public void calcCurrentTargetPosition( double time )
    {
        timeDiff = time - scan.time ;
        hisMovementInThisLoop = scan.vHisMovement.multiply(timeDiff);
        hisPositionInThisLoop = scan.vHisPos.add(hisMovementInThisLoop);
    }
    
    public void calcAlpha()
    {
        hisDirectionInThisLoop = hisPositionInThisLoop.substract( this.getMyCurPos() );
        alpha = PI - hisDirectionInThisLoop.getAngelBetween( scan.vHisMovement );
    }
    
    public void calcTimeToTarget( )
    {
        double tP2 = scan.velocity * hisDirectionInThisLoop.getLength() * cos( alpha );
        double tP3 = pow(scan.velocity,2) - pow(this.getBulletVelocity(),2);
        double tP = ( - tP2) / tP3;
        
        double tQ2 = pow(hisDirectionInThisLoop.getLength(),2);
        double tQ4 = pow(scan.velocity,2) - pow(this.getBulletVelocity(),2);
        double tQ = ( tQ2 ) / tQ4;
        
        double y1 = -tP + sqrt(pow(tP,2)-tQ);
        double y2 = -tP - sqrt(pow(tP,2)-tQ);
        timeToTarget = y1;
    }
     
    public void calcBetta()
    {
        if( Double.isNaN(timeToTarget) )
        {
            hisExpectedMovement = new Vector();
            hisExpectedFinalPos = hisPositionInThisLoop;
        }
        else
        {
            hisExpectedMovement = scan.vHisMovement.multiply( timeToTarget );
            hisExpectedFinalPos = hisPositionInThisLoop.add( hisExpectedMovement );
        }
        bullDist = hisExpectedFinalPos.substract( this.getMyCurPos() );
        enemyDist = hisExpectedFinalPos.substract( hisPositionInThisLoop );
     
        betta = asin( enemyDist.getLength() / bullDist.getLength() * sin( alpha ));
        gamma = asin( hisDirectionInThisLoop.getLength() / bullDist.getLength() * sin( alpha ));
    }
     
    public void checkBattlefielBorders( )
    {
        boolean recalc = false;
        if( hisExpectedFinalPos.x < 18 )
        {
            hisExpectedFinalPos.x = 18;
            recalc = true;
        } else if( hisExpectedFinalPos.x > (battleSize.x-18) )
        {
            hisExpectedFinalPos.x = battleSize.x-18;
            recalc = true;
        }
        if( hisExpectedFinalPos.y < 18 )
        {
            hisExpectedFinalPos.y = 18;
            recalc = true;
        } else if( hisExpectedFinalPos.y > (battleSize.y-18) )
        {
            hisExpectedFinalPos.y = battleSize.y-18;
            recalc = true;
        }
         
        if( recalc )
        {
            bullDist = hisExpectedFinalPos.substract( this.getMyCurPos() );
            enemyDist = hisExpectedFinalPos.substract( hisPositionInThisLoop );
            betta = asin(enemyDist.getLength() / bullDist.getLength() * sin( alpha ));
            gamma = asin( hisDirectionInThisLoop.getLength() / bullDist.getLength() * sin( alpha ));

            hisExpectedMovement = hisExpectedFinalPos.substract(hisPositionInThisLoop);
        }
    }
      
    public void checkClockwise()
    {
        firedData = new FiredBullet();
        firedData.targetDirection = hisDirectionInThisLoop.getDirection();
        firedData.aimingAlgo = Aiming.INDEX_LinearTargeting;
        firedData.expectedPos = hisExpectedFinalPos;
        firedData.targetName = this.getTargetName();
        
        if( scan.vHisMovement.getLength() != 0)
        {
            if( isClockwise() )
                firedData.targetDirection += betta;
            else
                firedData.targetDirection -= betta;
        }
    }
    
    @Override
    public void drawTargeting(Graphics2D g) {

        if( scan == null )
            return;
        
//        Draw.drawLineBetweenVectors( g, scan.vMyPos, scan.vHisPos);
        Draw.drawPoint( g, scan.vMyPos, 6 );
        Draw.drawPoint( g, scan.vHisPos, 6 );
        
//        Draw.drawLineBetweenVectors( g, this.getMyCurPos(), hisPositionInThisLoop  );
//        Draw.drawLineBetweenVectors(g, hisPositionInThisLoop, hisExpectedFinalPos );
//        Draw.drawLineBetweenVectors(g, this.getMyCurPos(), hisExpectedFinalPos );
        Draw.drawVectorAtPosition(g, this.getMyCurPos(), hisDirectionInThisLoop);
        Draw.drawVectorAtPosition(g, this.getMyCurPos(), bullDist);
        Draw.drawVectorAtPosition(g, hisPositionInThisLoop, enemyDist);
     }

    @Override
    public void printAimingData(long time) {
        System.out.println(String.format("EXPECT time: %d timeDiff %.1f Hit: %.1f", time, timeDiff, time + timeToTarget));
        this.getMyCurPos().print("- myCurPos");
        scan.vHisPos.print("- HisPos");
        System.out.println(String.format("- this.getBulletVelocity(): %.1f", this.getBulletVelocity()));
        scan.vHisMovement.print("- scan.vHisMovement");
        System.out.println(String.format("- scan.heading: %.1f", scan.heading*360/2/Math.PI));
        System.out.println(String.format("- scan.velocity: %.1f", scan.velocity));
        hisMovementInThisLoop.print("- hisMovementInThisLoop");
        hisPositionInThisLoop.print("- hisPositionInThisLoop");
        hisDirectionInThisLoop.print("- hisDirectionInThisLoop");
        
        System.out.println(String.format("- alpha: %.1f", alpha*360/2/PI));
        System.out.println(String.format("- timeToTarget: %.1f", timeToTarget));
        hisExpectedFinalPos.print("- hisExpectedFinalPos");
        System.out.println(String.format("- bullDist.getLength(): %.1f", bullDist.getLength()));
        System.out.println(String.format("- enemyDist.getLength(): %.1f", enemyDist.getLength()));
        System.out.println(String.format("- hisDirectionInThisLoop.getLength(): %.1f", hisDirectionInThisLoop.getLength()));
        System.out.println(String.format("- hisDirectionInThisLoop.getDirection(): %.1f", hisDirectionInThisLoop.getDirection()*360/2/Math.PI));
        System.out.println(String.format("- scan.vHisMovement.getDirection(): %.1f", scan.vHisMovement.getDirection()*360/2/Math.PI));
        
        System.out.println(String.format("- betta: %.1f", betta*360/2/PI));
        System.out.println(String.format("- gamma: %.1f", gamma*360/2/PI));
        System.out.println(String.format("- all: %.1f", gamma*360/2/PI + betta*360/2/PI + alpha*360/2/PI));
    }

    @Override
    public boolean processAiming(Vector myCurPos, double firePower, long time) {
        
        firedData = null;
        
        if( this.getTargetName() == null )
            return false;
        
        scan = scans.getEnemyData( this.getTargetName() ).getLastScan();
        if( scan == null )
            return false;
        
        this.setMyCurPos(myCurPos);
        this.setFirepower(firePower);
        
        calcCurrentTargetPosition( time );
        calcAlpha();
        calcTimeToTarget();
        calcBetta();
        
        checkBattlefielBorders();
        checkClockwise();

//        expectedHitTime = time + (long)timeToTarget;
        return true;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Targeting;

import nosteel.Basics.Draw;
import nosteel.Basics.Vector;
import nosteel.Modules.Aiming;
import nosteel.Modules.DataList;
import nosteel.Modules.Data.EnemyData;
import nosteel.Modules.Data.FiredBullet;
import nosteel.Modules.Data.ScanData;
import java.awt.Graphics2D;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import robocode.util.Utils;

/**
 *
 * @author mabarthe
 */
public class PatternMatch extends Aim{

    public final int WINDOW_SIZE = 7;
    private Vector start;
    private double startHeading;
    List<Vector> window;
    private boolean isRightTurn;
    
    List<localMinimum> matches;
    private boolean finalSuccess;
    
//    private Vector hitPosition;
    
    public PatternMatch(DataList scans_) {
        super(scans_);
        
        window = new ArrayList<Vector>();
        matches = new ArrayList<localMinimum>();
    }

    @Override
    public void drawTargeting(Graphics2D g) {
        
        if( start == null )
            return;
        
        if( isRightTurn )
            g.setColor(java.awt.Color.RED);
        else
            g.setColor(java.awt.Color.MAGENTA);
        
        for( Vector v : window )
        {
            Draw.drawPoint(g, start.add(v), 8);
        }
        
        if( matches.isEmpty() )
            return;
    
//        System.out.println( String.format("\nPrint Target"));
        
        int count = 0;
        int iPointSize = 15;
        for( localMinimum match : matches )
        {
//        localMinimum match = matches.get(0);
        
            if( !match.succeeded )
                continue;
            
            EnemyData e = this.scans.getEnemyData( this.getTargetName() );
            ListIterator<ScanData> scanIt = e.getScanIterator( match.index );

            if( match.isRightTurn )
                g.setColor(java.awt.Color.RED);
            else
                g.setColor(java.awt.Color.MAGENTA);
                
//            System.out.println( String.format("\nindex %d", match.index));
                
            Vector firstPoint = null;
//            for( int i=0; i<WINDOW_SIZE; i++ )
//            {
                if( scanIt.hasPrevious() )
                {
                    ScanData s = scanIt.previous();
                    Draw.drawPoint(g, s.vHisPos, 8);
                    
                    if( firstPoint == null )
                        firstPoint = s.vHisPos;
                }
//            }
            
            if( match.succeeded )
            {
                Draw.drawPoint(g, match.target, max(2, (int)(iPointSize * match.probability)));
//                System.out.println( String.format("index %d - %f - %f - %f", match.index, match.target.x, match.target.y, match.probability));
                
//                Draw.drawLineBetweenVectors(g, match.target, firstPoint);
//                iPointSize -= 4;
//                if(iPointSize < 4)
//                    iPointSize = 4;
            }
            
//            if( count++ > 5)
//                break;
        }
        
//        if( hitPosition != null )
//            Draw.drawPoint(g, hitPosition, 10);
        
    }

    @Override
    public void printAimingData(long time) {
        super.printAimingData(time); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean processAiming(Vector myCurPos, double firePower, long time) 
    {
        
        finalSuccess = true;
        firedData = null;
//        System.out.println( String.format("\nMatch Pattern"));
        
        this.setMyCurPos(myCurPos);
        this.setFirepower(firePower);
        
        determineWindow();
        determineMatchList();
        
//        System.out.println( String.format("\nMatched List"));
//        for( localMinimum m : matches )
//        {
//            System.out.println(String.format("index %d mse %.3e", m.index, m.mse));
//        }

        predictMovement();     
        chooseFinalTarget();
        
        if( firedData!= null)
        {
            if( firedData.probability < 0.2 )
                finalSuccess = false;

            if( firedData.confidence < 10 )
                finalSuccess = false;
        }
        else
        {
            finalSuccess = false;  
        }
        return finalSuccess;
    }
    
    private void determineWindow()
    {
        EnemyData e = this.scans.getEnemyData( this.getTargetName() );
        if( e == null )
            return;

        ListIterator<ScanData> scanIt = e.getScanIterator( EnemyData.END_OF_LIST );
        
        if( !scanIt.hasPrevious() )
            return;
                
        ScanData s = scanIt.previous();
        start = s.vHisPos;
        startHeading = s.heading;
        Vector behind = null;
        Vector last = null;
        window.clear();
//        window.add( new Vector(0,0) );
        while( scanIt.hasPrevious() && (window.size() <= WINDOW_SIZE) )
        {
            s = scanIt.previous();
            Vector diff = s.vHisPos.substract(start);
            window.add( diff );
            if( behind == null )
                behind = s.vHisPos;
            last = s.vHisPos;
        }
        isRightTurn = this.isRightTurn(start, behind, last);
//        System.out.println( String.format("Is Right Turn %b", isRightTurn));
//        clockwise = isClockwise(start, s.vHisPos );
//        clockwise = false;
    }
    
    private matchResturn matchedMSEAtIndex( int index )
    {
        double mse = 0;

        EnemyData e = this.scans.getEnemyData( this.getTargetName() );
        if( e == null )
            return null;
        
        ListIterator<ScanData> scanIt = e.getScanIterator( index );
        
        if( scanIt == null )
            return null;
        
        if( !scanIt.hasPrevious() )
            return null;
        
        ScanData sIndex = scanIt.previous();
        ScanData s = null;
        Vector behind = null;
        Vector last = null;
        
        double angle = Utils.normalRelativeAngle( sIndex.heading - startHeading);

//        System.out.println(String.format("Angle: %.1f", angle*360/2/Math.PI));
        for( Vector v : window )
        {

            if( !scanIt.hasPrevious() )
                return null;
            
            Vector origPoint = start.add(v);
            
            s = scanIt.previous();
            Vector diff = s.vHisPos.substract(sIndex.vHisPos);
            Vector diffTurned = diff.turnByAngle(angle);
            Vector shiftedPoint = start.add(diffTurned);
            Vector match = origPoint.substract(shiftedPoint);
            mse += Math.pow(match.getLength(), 2);
            if( behind == null )
                behind = s.vHisPos;
            last = s.vHisPos;
        }

        matchResturn ret = new matchResturn();
        ret.mse = Math.sqrt(mse);
//        ret.clockwise = isClockwise(sIndex.vHisPos, s.vHisPos );
        ret.isRightTurn = this.isRightTurn(sIndex.vHisPos, behind, last);
        return ret;
    }
    
    private void determineMatchList()
    {
        EnemyData e = this.scans.getEnemyData( this.getTargetName() );

        if( e == null )
            return;
        
        double mseCur = Double.MAX_VALUE;
        double mseOld = Double.MAX_VALUE;
        boolean detectNegative = false;
        
        matches.clear();
        
//        System.out.println("\nMatch");
        int max = e.getNumOfScans();
        for( int i=WINDOW_SIZE; i<max; i++ )
        {
            matchResturn ret = matchedMSEAtIndex(i);
            if( ret == null )
                continue;
//            if( ret.clockwise != this.clockwise)
//                continue;
            mseCur = ret.mse;
//            System.out.println( String.format("Match %d MSE %.3e is Right %b velo %.0f", i, mseCur, ret.isRightTurn, this.scans.getScan(this.getTargetName(), i).velocity ));
            
            
//            if( mseCur < mseOld )
//                detectNegative = true;
//
//            if( detectNegative == true && (mseCur > mseOld) )
            if( mseCur < 1);
            {
                localMinimum mini = new localMinimum();
                mini.index = i;
                mini.mse = mseCur;
                mini.isRightTurn = ret.isRightTurn;
                matches.add(mini);
                detectNegative = false;
            }
//            mseOld = mseCur;
        }
        matches.sort( null );

    }

    private void predictMovement()
    {
        if( matches.isEmpty() )
            return;
    
//        System.out.println( String.format("\nPredict Movement"));
        int targets = 0;
        for( localMinimum match : matches )
        {
//        localMinimum match = matches.get(0);
            match.succeeded = false;
            match.target = null;
            if( match.mse > 1)
                continue;

            EnemyData e = this.scans.getEnemyData( this.getTargetName() );
            ListIterator<ScanData> scanIt = e.getScanIterator(match.index );

            if( !scanIt.hasNext() )
                return;

            ScanData bestMatchPosition = scanIt.next();
            ScanData lastScan = e.getLastScan();

            double bestMatchAngle = bestMatchPosition.vHisMovement.getDirection();
            double lastScanAngle = lastScan.vHisMovement.getDirection();

            double angleCorr = Utils.normalRelativeAngle( bestMatchAngle-lastScanAngle );

//            System.out.println( String.format("\nindex %d", match.index));
//            System.out.println( "angleCorr " + angleCorr*360/2/Math.PI);
            int count = 0;
            while( scanIt.hasNext() )
            {
                ScanData s = scanIt.next();
                if( s == null)
                    return;
                Vector hisMove = s.vHisPos.substract(bestMatchPosition.vHisPos);
                match.target = lastScan.vHisPos.add(hisMove.turnByAngle(angleCorr));
                
                match.target.x = max(18, match.target.x);
                match.target.x = min(this.battleSize.x - 18, match.target.x);
                match.target.y = max(18, match.target.y);
                match.target.y = min(this.battleSize.y - 18, match.target.y);
                
                Vector bulletDist = match.target.substract(this.getMyCurPos());
                double bulletTime = bulletDist.getLength() / this.getBulletVelocity() ;
                double enemyTime = s.time - bestMatchPosition.time;
//                System.out.println(String.format("bulletTime %.1f enemyTime %.1f diff %.1f", bulletTime, enemyTime, Math.abs( bulletTime - enemyTime)));
        //            System.out.println(String.format("enemyTime %.1f", enemyTime));
//                if( Math.abs( bulletTime - enemyTime) < 1)
                if( bulletTime < enemyTime )
                {
                    match.succeeded = true;
                    break;
                }

            }
//            if( targets++ >= 0)
//                break;
        }
        
    }
    
    public void determineProbability()
    {
        for( localMinimum current : matches )
        {
//            System.out.println(String.format("\ndetermineProbability %d", current.index));
            current.probability = 0;
            double goodMatch = 0;
            double total = 0;
            
            if( !current.succeeded )
                continue;
            
            for( localMinimum compare : matches )
            {
                if( current.target == null )
                    continue;
                if( compare.target == null )
                    continue;
                if( !compare.succeeded )
                    continue;
                
                total++;
                double xError = current.target.x - compare.target.x;                
                double yError = current.target.y - compare.target.y;                
                double meanError = Math.sqrt( Math.pow(xError, 2) + Math.pow(yError, 2));
                if( meanError < 1 )
                {
                    goodMatch++;
                }
                
//                System.out.println(String.format("%f, %f, %f, %f", xError, yError, meanError, goodMatch));
            }
            current.probability = goodMatch / total;
//            System.out.println(String.format("probability %f - %f, %f", current.probability, goodMatch, total));
        }
    }
    
    private void chooseFinalTarget()
    {
        firedData = null;
        if( matches.isEmpty() )
            return;
        determineProbability();

        localMinimum best = null;
        double bestProbability = 0;
        double goodMatches = 0;
        for( localMinimum current : matches )
        {
            if( !current.succeeded )
                continue;
            
            goodMatches++;
            
            if( bestProbability < current.probability )
            {
                best = current;
                bestProbability = current.probability;
            }
        }
        
//        localMinimum mini = matches.get(0);
        if( best == null)
            return;
        if( best.target == null )
           return;
        
//        System.out.println( String.format("\nChoose Target %d with %f%% out of %f", best.index, best.probability*100, goodMatches));

        Vector direction = best.target.substract( this.getMyCurPos() );
        firedData = new FiredBullet();
        firedData.targetDirection = direction.getDirection();
        firedData.aimingAlgo = Aiming.INDEX_PatterMatch;
        firedData.expectedPos = best.target;
        firedData.targetName = this.getTargetName();
        firedData.probability = best.probability;
        firedData.confidence = goodMatches;
        finalSuccess = true;
    }
    
    public boolean isRightTurn ( Vector firstPos, Vector behind, Vector last)
    {
        if( firstPos == null )
            return false;
        if( behind == null )
            return false;
        if( last == null )
            return false;
        
        double angelBehind = behind.substract(firstPos).getDirection();
        double angleLast = last.substract(firstPos).getDirection();
        double diff = Utils.normalRelativeAngle(angleLast - angelBehind);
        boolean isRigth = diff < 0;
        return isRigth;
    }
    
    
}

class localMinimum implements Comparable<localMinimum>
{
    public int index;
    public double mse;
    public Vector target;
    public boolean succeeded;
    public boolean isRightTurn;
    public double probability;
    
    @Override
    public int compareTo(localMinimum o) 
    {
        if( mse < o.mse )
            return -1;
        if( mse > o.mse )
            return 1;
        return 0;
        
//        return (int)((mse*1000) - (o.mse*1000));
    }
}

class matchResturn
{
    public double mse;
    public boolean isRightTurn;
    
}
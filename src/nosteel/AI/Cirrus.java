/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.AI;

import nosteel.Basics.Vector;
import nosteel.Modules.Aiming;
import nosteel.Modules.Movement;
import nosteel.Modules.Data.FiredBullet;
import nosteel.Modules.LinearTracking;
import nosteel.Modules.Data.EnemyData;
import nosteel.Modules.Data.ScanData;
import nosteel.Modules.DataList;
import nosteel.Modules.Radar;
import nosteel.Modules.Statistics;
import nosteel.Modules.Stats.AnalyseMovementLinearity;
import nosteel.Modules.Stats.AnalyseMovementSitting;
import java.awt.Graphics2D;
import java.util.ListIterator;
import robocode.Bullet;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

/**
 *
 * @author mabarthe
 */
public class Cirrus {
    
    private DataList scanList;
    private Radar   radar;
    private Aiming aiming; 
    private Movement move;
    private Statistics stat;

    private double firePower;
    private double firePowerOldTurn;
    private FiredBullet lastAim;
    
    /***************************************************************************
     *  Construction and Initialization
     */
    
    public Cirrus( )
    {
        scanList = new DataList( );
        radar = new Radar(scanList);
        aiming = new Aiming( scanList );
        move = new Movement( scanList, aiming );
        stat = new Statistics( scanList, aiming );
        
        stat.openCSV();
        firePower = 1;
        firePowerOldTurn = 1;
    }
    
    public void setNumberOfEnemies( int enemies )
    {
        scanList.intiEnemyArray(enemies);
    }
    
    public void setBattlefieldSize( Vector size )
    {
        aiming.setBattlefieldSize( size );
        move.setBattlefieldSize( size );
    }
    
    /***************************************************************************
     *  Code decisions of the AI
     */
    
    public void startNewTurn( double myEnergy )
    {
        lockToBotWithLowestEnergy();        
        
        if( scanList.getNrOfEnemiesAlive() <= 1)
        {
            radar.setActiveIndex( Radar.INDEX_KeepTrack );
            
            move.setActiveIndex(Movement.INDEX_SerpentineApproach);
//            move.setActiveIndex(Movement.INDEX_AntiG);
//            move.setActiveIndex(Movement.INDEX_Sidestep);

            aiming.setActiveIndex( Aiming.INDEX_PatterMatch );
//            aiming.setActiveIndex( Aiming.INDEX_LinearTargeting );
            
            EnemyData e = scanList.getEnemyData( aiming.getTargetName());
            if( e != null)
            {
                ScanData s = scanList.getEnemyData( aiming.getTargetName()).getLastScan();
                if( s != null)
                {
                    if( s.velocity == 0)
                        aiming.setActiveIndex( Aiming.INDEX_LinearTargeting );
                }
            }
            
        }
        
//        AnalyseMovementLinearity moveLin = (AnalyseMovementLinearity) stat.getStatsByIndex(Statistics.INDEX_AnalyseMovementLinearity);
//        double linearMovementRatio = moveLin.getLinearityRatio();
//        
//        AnalyseMovementSitting moveSit = (AnalyseMovementSitting) stat.getStatsByIndex(Statistics.INDEX_AnalyseMovementSitting);
//        double sittingRatio = moveSit.getSittingRatio();
//        
//        
//        EnemyId target = tracking.getTargetBot();
//        if( target == null )
//            return;
//
//        ScanResult res = scanList.getLastScout(target.name);
//
//        
//        int alive = scanList.getNumberOfEnemiesAlive();
//        if( alive <= 1 )
//        {
//            if( sittingRatio > 0.5 )
//            {
//                move.setActiveIndex( Movement.INDEX_SerpentineApproach);
//            }
//            else
//            {
//                if( linearMovementRatio > 0.5 )
//                {
//                    move.setActiveIndex( Movement.INDEX_SerpentineApproach);
//                }
//                else
//                {
////                    if( res.energy < (myEnergy/2) )
////                        move.setActiveIndex( Movement.INDEX_Hunt);
////                    else
//                        move.setActiveIndex( Movement.INDEX_SerpentineApproach);
//                }
//            }
//        }
//        else
//        {
//            if( sittingRatio < 0.5 && linearMovementRatio < 0.5 )
//                move.setActiveIndex( Movement.INDEX_Hunt);
//            else
//                move.setActiveIndex( Movement.INDEX_AntiG);
//        }
//        
//        
        firePowerOldTurn = firePower;
        determineFirePower();
//        
    }
    
    /***************************************************************************
     *  Plan Radar Rotation
     */
    
    public double getRadarAngle( long time, double radarHeading )
    {
        return radar.getRadarTurnAngel(time, radarHeading);
    }

    /***************************************************************************
     *  Target Selection
     */
    
    private void lockToBotWithLowestEnergy()
    {
        double minEnemgy = Double.MAX_VALUE;
        ListIterator<EnemyData> dataIt = scanList.getEnemyListIterator(DataList.FRONT_OF_LIST);
        while( dataIt.hasNext() )
        {
            EnemyData e = dataIt.next();
            ScanData s = e.getLastScan();
            
            if( !e.isAlive() )
                continue;
            
            if( s == null )
                continue;
            
            if( minEnemgy > e.getLastScan().energy)
            {
                minEnemgy = e.getLastScan().energy;
                aiming.setTargetName( e.getName() );
            }
        }
        radar.setTarget(aiming.getTargetName());
    }
    
    /***************************************************************************
     *  Decide about the used fire power 
     */
    
    private void determineFirePower()
    {
        String target = aiming.getTargetName();
        if( target == null )
            return;
 
        ScanData res = scanList.getEnemyData(target).getLastScan();
        if( res == null)
            return;
        
        double distance = res.vDirToHim.getLength();
        
        firePower = 0.5;
        
        if( distance < 800 )
            firePower = 1;
            
        if( distance < 500 )
            firePower = 2;
        
        if( distance < 300 )
            firePower = 3;

        if( distance < 200 )
            firePower = 4;
    }

    public double getFirePower() 
    {
        return firePowerOldTurn;
    }

    /***************************************************************************
     *  Calculation of the Movement 
     */
    
    public void processMovement( Vector myPos, double myHeading )
    {
        move.process(myPos, myHeading);
    }
    
    public double getMovementAngle() {
        return move.getFinalRelativeAngle();
    }

    public double getMovementDist() {
        return move.getFinalDist();
    }
    
    /***************************************************************************
     *  Aiming the Target 
     */
    
    public boolean processAiming(  Vector myCurPos, long time )
    {
        return aiming.processAiming( myCurPos, firePower, time );
    }

    public double getTargetDirection() 
    {
        lastAim = aiming.getFiredData();
        return lastAim.targetDirection;
    }
    
    public void registerFiredBullet(Bullet b)
    {
        lastAim.b = b;
        EnemyData e = scanList.getEnemyData( aiming.getTargetName() );
        if( e != null )
        {
            e.addBullet(lastAim);
        }
        else
        {
            System.out.println("What the Fuck! registerFiredBullet ");
        }
    }
    
    public void myBulletHit( BulletHitEvent event )
    {
        FiredBullet fired = scanList.getFiredBullet( event.getBullet() );
        if( fired != null )
        {
            if( fired.targetName.equals(event.getBullet().getVictim()))
            {
                fired.setHit();
            }
            else
            {
                fired.setHitWrong();
            }
        }
    }
    
    public void myBulletMissed( BulletMissedEvent event )
    {
        FiredBullet fired = scanList.getFiredBullet( event.getBullet() );
        if( fired != null )
        {
            fired.setMissed();
        }
    }
    
    public void enemyDied( RobotDeathEvent event )
    {
        scanList.setEnemyKilled(event.getName());
    }
    
    /***************************************************************************
     *  Store the radar scans 
     */
    
    public void attachScan ( ScannedRobotEvent event, Vector myPos, double myHeadingRad, long time )
    {
        scanList.addScan(event, myPos, myHeadingRad, time);
    }
            
    public double getNumberDicoveredOfEnemies()
    {
        return scanList.getNumOfDiscoveredEnemies();
    }
    
    
    /***************************************************************************
     *  Draw modules data 
     */
    
    public void draw( Graphics2D g )
    {
        aiming.drawTargeting( g );
        move.draw( g );
        scanList.draw(g, aiming.getTargetName());
        stat.draw(g);
    }
    
    
    /***************************************************************************
     *  Debug
     */
    
    public void printTotalStatistic()
    {
        stat.analyseIndexed( Statistics.INDEX_BulletHitRate );
        stat.print();
    }
    
    public void logTotalStatistic( int round )
    {
        stat.analyseIndexed( Statistics.INDEX_BulletHitRate );
        stat.printCsvRow( round );
    }
    
}

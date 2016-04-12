package nosteel;




import nosteel.AI.Cirrus;
import nosteel.Basics.Vector;
import java.awt.Graphics2D;
import static java.lang.Math.PI;
import robocode.AdvancedRobot;
import robocode.Bullet;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.RobotDeathEvent;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mabarthe
 * 
 * Rankings so for:
 * 948		nosteel.Welby 0.0.2	29.8	21.18	30.91	0.09	23.65	1105	3484	2015-12-11 02:52:10     --> INDEX_AntiG / INDEX_LinearTargeting 
 * 
 */
public class Welby extends AdvancedRobot {

    private Cirrus ai;
    
    public Welby()
    {
        super();
        ai = new Cirrus( );
    }
    
    @Override
    public void run() {
        
        
        ai.setNumberOfEnemies( this.getOthers() );
        Vector battleField = new Vector( getBattleFieldWidth(), getBattleFieldHeight());
        ai.setBattlefieldSize(battleField);
        
        this.setAdjustGunForRobotTurn(true);
        this.setAdjustRadarForGunTurn(true);
        
//        this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        while( true )
        {
            this.setTurnRadarRightRadians( ai.getRadarAngle(getTime(), getRadarHeadingRadians()) );
            ai.startNewTurn( this.getEnergy());
            ai.processMovement(new Vector( getX(), getY() ), this.getHeadingRadians());
            this.setTurnRightRadians( ai.getMovementAngle() );
            this.setAhead( ai.getMovementDist() );
            doGun();
            execute();
            
        }
        
    }

    long fireTime = 0;
    boolean aimSuccessfull = false;
    void doGun() {
        
        if( ai.getNumberDicoveredOfEnemies() == 0 )
            return;
        
        if (fireTime == getTime() && getGunTurnRemaining() == 0 ) {
            if( getGunHeat() == 0 )
            {
                Bullet b = setFireBullet(ai.getFirePower());
                ai.registerFiredBullet(b);
            }
        }

        Vector myNewPos = new Vector( getX(), getY() );
//        ai.lockTargetLowestEnergy();
        aimSuccessfull = ai.processAiming( myNewPos, getTime() );
//        System.out.println(String.format("Aiming status %b", aimSuccessfull));
        if( aimSuccessfull )
        {
            double target = ai.getTargetDirection();
            this.setTurnGunRight(Utils.normalRelativeAngle(target - getGunHeadingRadians()) *360 / (2*PI));

            fireTime = getTime() + 1;
        }
        else
        {
            fireTime = 0;
        }
    }    
    
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {

//        double absBearing = Utils.normalAbsoluteAngle( event.getBearingRadians()+ getHeadingRadians());
//        setTurnRadarRightRadians( Utils.normalRelativeAngle(absBearing- getRadarHeadingRadians()) );
        ai.attachScan(event, new Vector( getX(), getY() ), getHeadingRadians(), getTime());
    }

    @Override
    public void onPaint(Graphics2D g) {
        ai.draw(g);
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
        ai.myBulletHit( event );
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        ai.myBulletMissed( event );
    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        ai.printTotalStatistic();
        ai.logTotalStatistic( this.getRoundNum() );
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        ai.enemyDied(event);
    }

    
    
    
    
    


    
    
    
}


package DummyRobots;



import java.awt.Color;
import robocode.AdvancedRobot;
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
 */
public class WalkToLine extends AdvancedRobot {

    private double enemyBearing;
    private double myHeading;
    
    public WalkToLine ()
    {
        super();
        enemyBearing = 0;
        myHeading = 0;
    }
    
    
    
    @Override
    public void run() {
         
        this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        
        // Set colors
        setBodyColor(Color.blue);
        setGunColor(Color.blue);
        setRadarColor(Color.black);
        setScanColor(Color.yellow);

        
//        getBattleFieldHeight();
//        getBattleFieldWidth();
//        
//        getX();
//        getY();
        
        
        // Loop forever
        while (true) {
                // Tell the game that when we take move,
                // we'll also want to turn right... a lot.
                setTurnRight(enemyBearing+90);
                execute();
                // Limit our speed to 5
//                setMaxVelocity(5);
                // Start moving (and turning)
                // Repeat.
                ahead(200);
                back(200);
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) 
    {
        enemyBearing = event.getBearing();
        myHeading = getHeading();
    }    

}

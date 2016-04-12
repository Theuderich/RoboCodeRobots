
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
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
public class TestRobot extends AdvancedRobot {

//    private long lastTime;
//    private double enemyHeading;
    private double enemyBearing;
//    
//    private double betta;
//    private double timeToTarget;
   private double betta;
    
    public TestRobot()
    {
        super();
//        enemyHeading = 0;
        enemyBearing = 0;
//        lastTime = 0;
        
//        betta = 0;
//        timeToTarget = 0;
        betta = 0;
    }
    
    @Override
    public void run() {
         
        this.setAdjustGunForRobotTurn(true);
        this.setAdjustRadarForGunTurn(true);
        
        this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        
        while( true )
        {
//            setAhead(2);
//            setTurnRight(enemyBearing);
            doGun();
            execute();
            
        }
        
    }

    long fireTime = 0;
    boolean detected = false;
    boolean aimed = false;
    void doGun() {
        if (fireTime == getTime() && getGunTurnRemaining() == 0 && aimed == true) {
            if( getGunHeat() == 0 )
            {
                System.out.println("Fire gun Heading: " + getGunHeading() + " gunHeat " + getGunHeat());
                setFire(1);
                detected = false;
                aimed = false;
            }
        }

        aimed = false;
        this.setTurnGunRight(Utils.normalRelativeAngleDegrees(enemyBearing + getHeading() - getGunHeading() + betta));
        System.out.println("Aiming gun Heading:" + Utils.normalAbsoluteAngleDegrees(enemyBearing+getHeading()));
        if( detected == true)
            aimed = true;
        detected = false;
        
//        setTurnGunRight(90);
        // Don't need to check whether gun turn will complete in single turn because
        // we check that gun is finished turning before calling setFire(...).
        // This is simpler since the precise angle your gun can move in one tick
        // depends on where your robot is turning.
        fireTime = getTime() + 1;
    }    
    
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
//        this.setTurnGunRight(Utils.normalRelativeAngleDegrees(event.getBearing()+getHeading() - getGunHeading()));
//        enemyHeading = event.getHeading();
        enemyBearing = event.getBearing();
        detected = true;
        
        long thisTime = getTime();
//        System.out.println("timeDiff: " + (thisTime - lastTime));
//        lastTime = thisTime;
        
//        System.out.println("velocity: " + event.getVelocity());
//        System.out.println("heading: " + enemyHeading);
//        System.out.println("distance: " + event.getDistance());
//        System.out.println("heading: " + getHeading());
//        System.out.println("bearing: " + enemyBearing);
//        System.out.println("enemyAngel: " + Utils.normalAbsoluteAngleDegrees(enemyBearing + getHeading()));
//        System.out.println("enemyHeading: " + event.getHeading());
//        setFire(1);
        
        
//        betta = Utils.normalAbsoluteAngleDegrees(enemyBearing + getHeading() + enemyHeading); 
//        
//        System.out.println("betta: " + betta);
//        
//        timeToTarget = sin( betta / 360 * 2*PI) * event.getDistance() / 17;
//        System.out.println("timeToTarget" + timeToTarget);
//        
//        alpha = acos(17*timeToTarget/event.getDistance()) * 360 /(2*PI);
//        System.out.println("alpha" + alpha);
        
        
        Vector v1Pos = new Vector( getX(), getY());
        Vector v1Dir = new Vector();
        
        double  enemyAngel = Utils.normalAbsoluteAngleDegrees(event.getBearing() + getHeading());
//        System.out.println("enemyAngel: " + enemyAngel);
        
        v1Dir.x = sin( enemyAngel / 360 * 2*PI ) * event.getDistance();
        v1Dir.y = cos( enemyAngel / 360 * 2*PI ) * event.getDistance();
        
//        v1Pos.print("V1 Position");
//        v1Dir.print("V1 Direction");

        Vector v2Pos = v1Pos.add(v1Dir);
        Vector v2Dir = new Vector();
        
        double  enemyHeading = Utils.normalAbsoluteAngleDegrees(event.getHeading());
//        System.out.println("enemyHeading: " + enemyHeading);
        
        v2Dir.x = sin( enemyHeading / 360 * 2*PI ) * event.getVelocity();
        v2Dir.y = cos( enemyHeading / 360 * 2*PI ) * event.getVelocity();

//        v2Pos.print("V2 Position");
//        v2Dir.print("V2 Direction");
        
        double alpha = 180 - v1Dir.getAngel(v2Dir);
//        double angelCor = v2Dir.getAngel(v1Dir);

//        System.out.println("Test: " + Utils.normalRelativeAngleDegrees(event.getBearing() - event.getHeading()));
//        System.out.println("Angel 1st: " + angel );

//        if ( (Utils.normalRelativeAngleDegrees(event.getBearing() - event.getHeading())) < - 50 )
//            angelCor = 180 - angel;
//        System.out.println("target: " + Utils.normalAbsoluteAngleDegrees(event.getBearing()+ getHeading()) + " heading " + event.getHeading() + " Angel: " + angel + " AngelCorr: " + angelCor);

//        System.out.println("Angel 2nd: " + angel );
  

        double  d = event.getDistance();
        double  v = event.getVelocity();
        double  b = 17;
        double  t = Double.NaN; 

        System.out.println(" Angel: " + alpha + " dist: " + d + " velo: " + v);
        
        double tP = (v*d*cos(alpha / 360 * 2*PI)) / (pow(b,2) - pow(v,2));
        double tq = -pow(d,2) / (pow(b,2) - pow(v,2));
        double y1 = -tP + sqrt(pow(tP,2)-tq);
        double y2 = -tP - sqrt(pow(tP,2)-tq);
        t = y1+1;
//        t = sqrt( pow( d, 2 ) / ( pow(b,2) + pow(v,2) - 2*b*v*cos(angel / 360 * 2*PI ) ));
//        else
//        {
//            t = sqrt( pow( event.getDistance(), 2 ) / ( pow(17,2) - pow(event.getVelocity(), 2) + 2*17*event.getVelocity()*cos(angel)));
//        }
        
        
        System.out.println("t1: " + y1 + "t2: " + y2 + " bulldist " + b*t + " enemyWay " + v*t );
        
        betta = acos( ( pow(b*t,2) + pow(d,2) - pow(v*t,2)) / (2*b*t*d)) * 360/(2*PI);
        double gamma = 180 - betta - alpha;
        
        System.out.println("gammme: " + gamma + " betta " + betta );
        
        
    }
    
}


class Vector
{
    public double x;
    public double y;
    
    public Vector ()
    {
        this.x = Double.NaN;
        this.y = Double.NaN;
    }
    
    
    public Vector (double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public Vector substract( Vector o )
    {
       Vector v = new Vector();
       v.x = this.x - o.x;
       v.y = this.y - o.y;
       return v;
    }
    
    public Vector add( Vector o )
    {
       Vector v = new Vector();
       v.x = this.x + o.x;
       v.y = this.y + o.y;
       return v;
    }
    
    public double skararProdukt( Vector o )
    {
       return this.x * o.x + this.y * o.y;
    }
    
    public double getLength()
    {
        return sqrt( pow(this.x,2) + pow(this.y,2) );
    }
    
    public double getAngel(Vector o)
    {
        double t1 = this.skararProdukt(o);
        double t2 = abs(this.getLength()) * abs(o.getLength());
        
//        System.out.println("Angel t1: " + t1);
//        System.out.println("Angel t2: " + t2);

        return acos(t1 / t2) * 360 /(2*PI);
    }
    
    public void print( String name )
    {
        System.out.println( String.format("%s --> x: %f y: %f", name, this.x, this.y));
    }
}

class gerade
{
    public Vector p;
    public Vector u;
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Data;

import nosteel.Basics.Vector;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import robocode.Bullet;
import robocode.ScannedRobotEvent;

/**
 *
 * @author mabarthe
 */
public class EnemyData {
    
    public static boolean FRONT_OF_LIST = true;
    public static boolean END_OF_LIST = false;
    
    private String name;
    private boolean detected;
    private boolean alive;
    private List<ScanData> scans;
    
    private List<FiredBullet> fired;
    public EnemyData ()
    {
        name = "";
        detected = false;
        alive = false;
        scans = new ArrayList<ScanData>();
        fired = new ArrayList<FiredBullet>();
    }
    
    /****
     * Administrative Data
     */
    
    public void enemyDetected( String name_ )
    {
        name = name_;
        detected = true;
        alive = true;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public boolean isEnemy( String name_ )
    {
        return this.name.equals(name_);
    }
    
    public boolean isAlive()
    {
        return this.alive;
    }
    
    public boolean isDetected()
    {
        return this.detected;
    }
    
    public void setDead()
    {
        this.alive = false;
    }
    
    /****
     * Scan results
     */
    
    public void addScan( ScannedRobotEvent event, Vector myPos, double myHeadingRad, long time )
    {
        scans.add( new ScanData(event, myPos, myHeadingRad, time));
    }
    
    public ListIterator<ScanData> getScanIterator( boolean pos )
    {
        return scans.listIterator( pos==FRONT_OF_LIST ? 0 : (scans.size()) );
    }
    
    public ListIterator<ScanData> getScanIterator( int index )
    {
        return scans.listIterator( index );
    }
    
    public ScanData getLastScan()
    {
        if( scans.isEmpty() )
            return null;
        return scans.get( scans.size()-1 );
    }
    
    public ScanData getScanByIndex( int index )
    {
        if( scans.isEmpty() )
            return null;
        if( scans.size() <= index )
            return null;
        return scans.get( index );
    }
    
    public int getNumOfScans()
    {
        return scans.size();
    }
    
    public void drawScans( Graphics2D g )
    {
        for( ScanData s : scans )
            s.drawScan(g);
            
    }    
    
    /****
     * Fired bullets
     */
    
    public void addBullet( FiredBullet e )
    {
        e.hitAnotherTarget = false;
        e.hitTheTarget = false;
        e.isOnTheWay = true;
        
        fired.add(e);
    }
    
    public FiredBullet getFiredBullet( Bullet b )
    {
        FiredBullet found = null;
        for( FiredBullet fb : fired )
            found = fb;
        return found;
    }
    
    public ListIterator<FiredBullet> getFiredBulletIterator( )
    {
        return fired.listIterator( );
    }
    
    
}

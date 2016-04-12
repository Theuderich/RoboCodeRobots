/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules;

import nosteel.Basics.Vector;
import nosteel.Modules.Data.EnemyData;
import nosteel.Modules.Data.FiredBullet;
import nosteel.Modules.Data.ScanData;
import java.awt.Color;
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
public class DataList {
    
    public static boolean FRONT_OF_LIST = true;
    public static boolean END_OF_LIST = false;
    
    List<EnemyData> enemies;
    
    public DataList(  )
    {
        enemies = new ArrayList<EnemyData>();
    }
    
    public void intiEnemyArray( int nrOfEnemies )
    {
        for( int i=0; i<nrOfEnemies; i++)
            enemies.add( new EnemyData());
    }
    
    public EnemyData getEnemyData( String name )
    {
        for( EnemyData e : enemies )
            if( e.isEnemy(name))
                return e;
        return null;
    }

    private EnemyData addEnemy( String name )
    {
        for( EnemyData e : enemies )
        {
            if( e.isDetected() == false )
            {
                e.enemyDetected(name);
                return e;
            }
        }
        return null;
    }
    
    public void addScan( ScannedRobotEvent event, Vector myPos, double myHeadingRad, long time )
    {
        EnemyData e = getEnemyData( event.getName() );

        if( e == null )
        {
            e = addEnemy( event.getName() );
        }
        
        if( e != null )
        {
            e.addScan(event, myPos, myHeadingRad, time);
        }
                
    }
    
    public int getNrOfEnemiesTotal()
    {
        return enemies.size();
    }
    
    public int getNrOfEnemiesAlive()
    {
        int alive = 0;
        
        for( EnemyData e : enemies )
            if( e.isAlive() || !e.isDetected() )
                alive++;
        return alive;
    }
    
    public EnemyData getEnemyIndexed( int index )
    {
        return enemies.get(index);
    }
    
    public ListIterator<EnemyData> getEnemyListIterator( boolean pos )
    {
        return enemies.listIterator( pos==FRONT_OF_LIST ? 0 : enemies.size() );
    }
    
    public void setEnemyKilled( String name )
    {
        EnemyData e = getEnemyData(name);
        if( e != null )
            e.setDead();
    }
    
    public int getNumOfDiscoveredEnemies()
    {
        int count = 0;
        
        for( EnemyData e : enemies )
            if( e.isDetected())
                count++;
        return count;
    }
    
    public void draw( Graphics2D g, String name)
    {
        g.setColor(Color.YELLOW);
        EnemyData e = this.getEnemyData(name);
        if( e != null )
        {
            e.drawScans(g);
        }
    }
    
    public EnemyData getMostRecentScan()
    {
        long lastestTime = 0;
        EnemyData mostRecent = null;
        for( EnemyData e : enemies )
        {
            ScanData last = e.getLastScan();
            if( last != null )
            {
                if( lastestTime < last.time )
                {
                    lastestTime = last.time;
                    mostRecent = e;
                }
            }
        }
        return mostRecent;
    }
    
    public FiredBullet getFiredBullet( Bullet b )
    {
        FiredBullet found = null;
        for( EnemyData e : enemies )
        {
            found = e.getFiredBullet(b);
            if( found != null)
                break;
        }
        return found;
    }
    
    public ScanData getScan( String target, int index)
    {
        ScanData s = null;
        EnemyData e = this.getEnemyData(target);
        if( e != null )
        {
            s = e.getScanByIndex(index);
        }
    
        return s;
                        
    }
    
}

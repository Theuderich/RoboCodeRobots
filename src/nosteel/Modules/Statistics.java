/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules;

import nosteel.Basics.Vector;
import nosteel.Modules.Stats.AnalyseMovementLinearity;
import nosteel.Modules.Stats.AnalyseMovementSitting;
import nosteel.Modules.Stats.BulletHitRate;
import nosteel.Modules.Stats.MissedBullets;
import nosteel.Modules.Stats.Statistic;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcobarthel
 */
public class Statistics {
    
    private DataList scans;
    private Aiming aiming;
    private Vector battleSize;

    
    public static int INDEX_AnalyseMovementLinearity = 0;
    public static int INDEX_BulletHitRate = 1;
//    public static int INDEX_MissedBullets = 2;
    public static int INDEX_AnalyseMovementSitting = 2;
    
    List<Statistic> list;
    
    public Statistics( DataList scans_, Aiming aim )
    {
        aiming = aim;
        scans = scans_;
        list = new ArrayList<Statistic>();
        list.add(new AnalyseMovementLinearity(scans_, aim));
        list.add( new BulletHitRate(scans_, aim));
//        list.add( new MissedBullets(scans_, aim));
        list.add( new AnalyseMovementSitting(scans_, aim));
    }

    public void setBattlefieldSize( Vector size )
    {
        battleSize = size;
        for( Statistic e : list )
            e.setBattlefieldSize(size);
    }

    public void analyseAll() {
        for( Statistic e : list )
            e.analyse();
    }
    
    public void analyseIndexed( int id ) {
        Statistic e = list.get(id);
        e.analyse();
    }
    

    public void draw(Graphics2D g)
    {
        g.setColor(java.awt.Color.GREEN);        
        for( Statistic e : list )
            e.draw(g);
   }
    
    public void print() {
        for( Statistic e : list )
            e.print();
    }

    public void openCSV() {
        for( Statistic e : list )
            e.openCSV();
    }

    public void printCsvRow( int round ) {
        for( Statistic e : list )
            e.printCsvRow( round );
    }

    public Statistic getStatsByIndex( int id )
    {
        return list.get(id);
    }
}


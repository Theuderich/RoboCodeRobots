/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Stats;

import nosteel.Basics.Draw;
import nosteel.Basics.Vector;
import nosteel.Modules.Aiming;
import nosteel.Modules.LinearTracking;
import nosteel.Modules.Data.EnemyData;
import nosteel.Modules.Data.FiredBullet;
import nosteel.Modules.Data.ScanData;
import nosteel.Modules.DataList;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import robocode.util.Utils;

/**
 *
 * @author mabarthe
 */
public class MissedBullets extends Statistic {
    
    Object[] array;
//    List<Recalc> list;
//    EnemyId target;
    private static String[] colHeader = {"round", "enemy", "expectedHit X", "expectedHit Y", "realHitPoint X", "realHitPoint Y", "Angel Offset", "distance between points", "expected bullet way", "anpha", "betta", "heading"};
    
    public MissedBullets (DataList s, Aiming t)
    {
        super( s, t, MissedBullets.class.getName());
//        list = new ArrayList<Recalc>();
    }

    @Override
    public void analyse() {
        
        int numElem = this.scans.getNrOfEnemiesTotal();
        array = new Object[numElem];
        
        for( int i=0; i<numElem; i++)
        {
            List<Recalc> list = new ArrayList<Recalc>();
            array[i] = list;
            
            EnemyData e = this.scans.getEnemyIndexed(i);
            calcMissedShoots(e, list);
        }
        
    }

    private void calcMissedShoots(EnemyData target, List<Recalc> list)
    {
        ListIterator<ScanData> dataIt = target.getScanIterator(EnemyData.FRONT_OF_LIST);
//        System.out.println(String.format("\nMissedBullets"));
        while( dataIt.hasNext() )
        {
            ScanData scan = dataIt.next();
//            for( FiredBullet f : s.bullets )
//            {
//                if( f.isOnTheWay==false && 
//                        f.hitTheTarget==false && 
//                        f.hitAnotherTarget==false)
//                {
////                    list.add(f);
//                    
//                    double meetTime = f.expectedHitTime;
//                    Vector point = scans.interpolatePosition(target.name, (long)meetTime);
//                    if( point != null )
//                    {
//                        Vector wayToPoint = f.startedAt.substract(point);
//                        double bulletTimeToPoint = wayToPoint.getLength() / f.b.getVelocity();
//                        double toDrivenToPoint = meetTime - s.time;
//                        double delta = toDrivenToPoint - bulletTimeToPoint;
////                        System.out.println(String.format("meetTime: %.1f bullet: %.1f driven: %.1f deelta: %.1f", meetTime, bulletTimeToPoint, toDrivenToPoint, delta));
//                        int i = 0;
//                        while( Math.abs(delta) > 1)
//                        {
//                            meetTime -= delta / 2;
//                            point = scans.interpolatePosition(target.name, (long)meetTime);
//                            if( point != null )
//                            {
//                                wayToPoint = f.startedAt.substract(point);
//                                bulletTimeToPoint = wayToPoint.getLength() / f.b.getVelocity();
//                                toDrivenToPoint = meetTime - s.time;
//                                delta = toDrivenToPoint - bulletTimeToPoint;
////                                System.out.println(String.format("-meetTime: %.1f bullet: %.1f driven: %.1f deelta: %.1f", meetTime, bulletTimeToPoint, toDrivenToPoint, delta));
//                            }
//                            else
//                            {
////                                System.out.println(String.format("-break"));
//                                break;
//                            }
//                            if( i++ > 10 )
//                                break;
//                        }
//
//                        // fount real tesrget point
//    //                    point;
//    //                    f.expectedPos;
//    //                    f.startedAt;
//                        if(point != null)
//                        {
//                            Vector shoot = f.expectedPos.substract(f.startedAt);
//                            Vector shouldShoot = point.substract(f.startedAt);
//    //                        shoot.print("Had Shot");
//    //                        shouldShoot.print("Shouldhot");
//                            double angelHad = shoot.getDirection()*360/2/Math.PI;
//                            double angelShould = shouldShoot.getDirection()*360/2/Math.PI;  
//
////                            System.out.println(String.format("%s Had %.1f Should %-1f failed %.1f", target.name, angelHad, angelShould, Utils.normalRelativeAngleDegrees(angelShould - angelHad)));
//                            
//                            Recalc re = new Recalc();
//                            re.expectedHit = f.expectedPos;
//                            re.expectedBulletWay = f.expectedPos.substract(f.startedAt);
//                            re.realHitPoint = point;
//                            re.distanceBetween = point.substract(re.expectedHit);
//                            re.angelOffset = Utils.normalRelativeAngle(shouldShoot.getDirection() - shoot.getDirection());
//                            re.bullet = f;
//                            re.name = target.name;
//                            list.add(re);
//                        }
//                    }
//                }
//            }
        }        
    }
    
    @Override
    public void draw(Graphics2D g) {
//        
//        if( array == null )
//            return;
//
//        EnemyId target = this.aiming.getTargetBot();
//        if( target == null )
//            return;
//        
//        int index = 0;
//        List<Recalc> list = (List<Recalc>) array[index];
//                
//        if( list.isEmpty() )
//            return;
//        
//        g.setColor(java.awt.Color.RED);        
//        
//        
////        for( FiredBullet f : list )
////        {
//            Recalc r = list.get( list.size() - 1 );
//            Draw.drawPoint(g, r.bullet.expectedPos, 10);
//            Draw.drawLineBetweenVectors(g, r.bullet.startedAt, r.bullet.expectedPos);
//            Draw.drawPoint(g, r.bullet.hisPosAtStart, 10);
//            Draw.drawVectorAtPosition(g, r.bullet.hisPosAtStart, r.bullet.hisExpectedMovement);
//            Draw.drawVectorAtPosition(g, r.bullet.hisPosAtStart, r.bullet.hisMovementTillNow.multiply(-1));
//            Vector point = scans.interpolatePosition(target.name, r.bullet.expectedHitTime);
//            if( point != null )
//                Draw.drawPoint(g, point, 10);
//            Draw.drawOval(g, r.realHitPoint, 10);
////        }
//        
    }
    
    @Override
    public void printCsvHeadline() {
//        try {
//            csvFilePrinter.printRecord((Object[]) colHeader);
//            csvFilePrinter.flush();
//        } catch (IOException ex) {
//            Logger.getLogger(BulletHitRate.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void printCsvRow( int round ) {
//        if( array == null )
//            return;
//        
//        
//        for( Object o: array )
//        {
//            List<Recalc> list = (List<Recalc>) o;
//            for( Recalc r : list )
//            {
//                try {
//                    String[] row = new String[ colHeader.length ];
//                    row[0] = String.format("%d", round);
//                    row[1] = String.format("%s", r.name);
//                    row[2] = String.format("%.0f", r.expectedHit.x);
//                    row[3] = String.format("%.0f", r.expectedHit.y);
//                    row[4] = String.format("%.0f", r.realHitPoint.x);
//                    row[5] = String.format("%.0f", r.realHitPoint.y);
//                    row[6] = String.format("%.2f", r.angelOffset*360/2/Math.PI);
//                    row[7] = String.format("%.1f", r.distanceBetween.getLength());
//                    row[8] = String.format("%.1f", r.expectedBulletWay.getLength());
//                    row[9] = String.format("%.1f", r.bullet.alpha*360/2/Math.PI);
//                    row[10] = String.format("%.1f", r.bullet.betta*360/2/Math.PI);
//                    row[11] = String.format("%.1f", r.bullet.hisMovementTillNow.getDirection()*360/2/Math.PI);
//                    
//
//                    csvFilePrinter.printRecord((Object[]) row);
//                    csvFilePrinter.flush();
//
//                } catch (IOException ex) {
//                    Logger.getLogger(BulletHitRate.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
    }

    
}



class Recalc
{
    public Vector expectedHit;
    public Vector realHitPoint;
    public Vector expectedBulletWay;
    public Vector distanceBetween;
    public double angelOffset;
    public FiredBullet bullet;
    public String name;
}
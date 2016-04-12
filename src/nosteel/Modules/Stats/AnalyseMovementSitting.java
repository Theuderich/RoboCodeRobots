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
import nosteel.Modules.Data.ScanData;
import nosteel.Modules.DataList;
import java.awt.Graphics2D;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
//import org.apache.commons.math3.fitting.PolynomialCurveFitter;
//import org.apache.commons.math3.fitting.WeightedObservedPoints;
//import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * @author mabarthe
 */
public class AnalyseMovementSitting extends Statistic {
    
    private List<Vector> distanceMovement;

    private final int DISTANCE_THRESHOLD = 3;

    private double SittingRatio;
            
    public AnalyseMovementSitting (DataList s, Aiming t)
    {
        super(s, t, AnalyseMovementSitting.class.getName());
        distanceMovement = new ArrayList<Vector>();
    }

    public double getSittingRatio() {
        return SittingRatio;
    }

    private double calcTotalDistance( List<Vector> window )
    {
        double distance = 0;
        Vector v2 = null;
        for( Vector v : window )
        {
            if( v2 != null)
            {
                distance += v.substract(v2).getLength();
            }
            v2 = v;
        }
        return distance;
    }
    
    private void checkDistanceMovement( String target )
    {
        double shortSitting = 0;
        double longSitting = 0;
        boolean detected = false;
        
        distanceMovement.clear();
        List<Vector> window = new ArrayList<Vector>();
        ListIterator itData = scans.getEnemyData(target).getScanIterator( EnemyData.END_OF_LIST);
        while( itData.hasPrevious() )
        {
            ScanData e = (ScanData) itData.previous();
            Vector v = new Vector(e.vHisPos.x, e.vHisPos.y);

            window.add(v);

            if( distanceMovement.size() == 0 )
                distanceMovement.add(v);


            if( window.size() >= 2 )
            {
                double distance = calcTotalDistance(window);
                if( distance > DISTANCE_THRESHOLD )
                {
                    distanceMovement.add(window.get(0));
                    if( window.size() < 3 )
                        shortSitting++;
                    detected = false;
                    int max = window.size();
                    for( int k=0; k<max; k++ )
                    {
                        window.remove(0);
                        distance = calcTotalDistance(window);
                        if( distance < DISTANCE_THRESHOLD || Double.isNaN(distance) )
                        {
                            break;
                        }
                    }
                }
                else
                {
//                    if( window.size() > 3 && detected == false)
//                    {
                        longSitting++;
//                        detected = true;
//                    }
                }
            }
        }
        distanceMovement.add(window.get(0));
        distanceMovement.add(window.get(window.size()-1));
        
        SittingRatio = longSitting / (longSitting+shortSitting);
//        System.out.println(String.format("sitting %.3e", SittingRatio ));
        
    }
 
    @Override
    public void analyse() {
        String target = aiming.getTargetName();
        if( target == null )
            return;
//        ListIterator it = scans.getEnemyListIterator();
//        while( it.hasNext() )
//        {
//            String enemy = (String) it.next();
//            List<ScanResult> list = scans.getAllScansOfEnemy(target);

            
            if( scans.getEnemyData(target).getNumOfScans() <= 3)
            {
//               break;
                return;
            }
            
            checkDistanceMovement( target );
            
//        }       
    }

    @Override
    public void draw(Graphics2D g) {
//        Vector vOld = null;
//        for( Vector v : distanceMovement )
//        {
//            if( vOld != null )
//            {
//                Draw.drawLineBetweenVectors(g, vOld, v);
//                Draw.drawPoint(g, vOld, 10);
//            }   
//            vOld = v;
//        }
//        if( vOld != null )
//            Draw.drawPoint(g, vOld, 4);
//
    }

    @Override
    public void print() {
    }
        
    
    
}

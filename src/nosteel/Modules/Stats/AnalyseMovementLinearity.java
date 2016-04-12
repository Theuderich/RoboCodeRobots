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
public class AnalyseMovementLinearity extends Statistic {
    
    private List<Vector> linearMovement;

    private final int LINEAR_ERROR = 100;

    private double linearityRatio;
            
    public AnalyseMovementLinearity (DataList s, Aiming t)
    {
        super(s, t, AnalyseMovementLinearity.class.getName());
        linearMovement = new ArrayList<Vector>();
    }

    public double getLinearityRatio() {
        return linearityRatio;
    }

    private void checkLinearMovement( String target )
    {
        double bigWindows = 0;
        double smallWindows = 0;
        boolean detected = false;
        
        linearMovement.clear();
        List<Vector> window = new ArrayList<Vector>();
        ListIterator itData = scans.getEnemyData(target).getScanIterator( EnemyData.END_OF_LIST );
        while( itData.hasPrevious() )
        {
            ScanData e = (ScanData) itData.previous();
            Vector v = new Vector(e.vHisPos.x, e.vHisPos.y);

            window.add(v);
            double error;
            if( linearMovement.size() == 0 )
                linearMovement.add(v);

          


            if( window.size() >= 3 )
            {
                error = calcLinearRegression(window);
                if( error > LINEAR_ERROR )
                {
                    linearMovement.add(window.get(0));
                    if( window.size() < 4 )
                        smallWindows++;
                    detected = false;
                    int max = window.size();
                    for( int k=0; k<max; k++ )
                    {
                        window.remove(0);
                        error = calcLinearRegression(window);
                        if( error < LINEAR_ERROR || Double.isNaN(error) )
                        {
                            break;
                        }
                    }
                }
                else
                {
                    if( window.size() > 4 && detected == false)
                    {
                        bigWindows++;
                        detected = true;
                    }
                }
            }
        }
        linearMovement.add(window.get(0));
        linearMovement.add(window.get(window.size()-1));
        
        linearityRatio = bigWindows / (bigWindows+smallWindows);
//        System.out.println(String.format("linearity %.3e", linearityRatio ));
        
    }
    
    private double calcLinearRegression( List<Vector> data )
    {
        double firstX = data.get(0).x;
        boolean xChanges = false;
//        SimpleRegression regression = new SimpleRegression();
//        for( Vector v : data)
//        {
//            regression.addData( v.x, v.y );
//            if( abs(firstX - v.x) > 1 && xChanges == false )
//            {
//                xChanges = true;
//            }
//        }
//        if( !xChanges )
//        {
//            regression.clear();
//            for( Vector v : data)
//            {
//                regression.addData( v.y, v.x );
//            }
//        }
//        
//        double error = regression.getMeanSquareError();
//        regression.clear();
//        return error;
        return 0;
    }
    
    private double calcPolynominalRegression(int order, List<Vector> data)
    {
//        final WeightedObservedPoints obs = new WeightedObservedPoints();
//        for( Vector v : data)
//        {
//            obs.add( v.x, v.y );
//        }
//        System.out.println("size="+data.size());
//
//        if( data.size() < 3 )
//            return Double.NaN;
//        
//        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(order);
//        final double[] coeff = fitter.fit(obs.toList());        
//
//        double err = 0;
//        for( Vector v : data)
//        {
//            double newY = coeff[3]*v.x*v.x*v.x + coeff[2]*v.x*v.x + coeff[1]*v.x + coeff[0];
//            err += Math.pow(newY-v.y,2);
//        }
//        err = Math.sqrt(err);
//        System.out.println("coef="+Arrays.toString(coeff) + "err " + err);
//        return err;
        return 0;
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
//            List<ScanResult> list = scans.getAllScansOfEnemy(target.name);

            
            if( scans.getEnemyData(target).getNumOfScans() <= 3)
            {
//               break;
                return;
            }
            
            checkLinearMovement( target );
            
//        }       
    }

    @Override
    public void draw(Graphics2D g) {
        Vector vOld = null;
        for( Vector v : linearMovement )
        {
            if( vOld != null )
            {
                Draw.drawLineBetweenVectors(g, vOld, v);
                Draw.drawPoint(g, vOld, 10);
            }   
            vOld = v;
        }
        if( vOld != null )
            Draw.drawPoint(g, vOld, 4);

    }

    @Override
    public void print() {
    }
        
    
    
}

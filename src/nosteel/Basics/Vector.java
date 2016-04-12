/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Basics;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 *
 * @author mabarthe
 */
public class Vector {
    
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
    
    public void clear( )
    {
       x = 0;
       y = 0;
    }
    
    public boolean isNull()
    {
        return (x == 0.0) && (y == 0.0);
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
    
    public Vector multiply( double o )
    {
       Vector v = new Vector();
       v.x = this.x * o;
       v.y = this.y * o;
       return v;
    }
    
    public Vector divide( double o )
    {
       Vector v = new Vector();
       v.x = this.x / o;
       v.y = this.y / o;
       return v;
    }
    
    public double skalarProdukt( Vector o )
    {
       return this.x * o.x + this.y * o.y;
    }
    
    public double getLength()
    {
        return sqrt( pow(this.x,2) + pow(this.y,2) );
    }
    
    public double getAngelBetween(Vector o)
    {
        double skalarProduct = this.skalarProdukt(o);
        double t2 = abs(this.getLength()) * abs(o.getLength());
        return acos(skalarProduct / t2);
    }
    
    public void print( String name )
    {
        System.out.println( String.format("%s --> x: %.1f y: %.1f", name, this.x, this.y));
    }
    
    public double getDirection()
    {
        double  rad = acos( this.y / this.getLength() );
        if( this.x < 0)
            rad = 2*PI-rad;
        return rad;
    }
    
    public Vector turnByAngle( double angle )
    {
       Vector v = new Vector();
       v.x = this.x * Math.cos(angle) - this.y * Math.sin(angle);
       v.y = this.x * Math.sin(angle) + this.y * Math.cos(angle);
       return v;
        
    }
}

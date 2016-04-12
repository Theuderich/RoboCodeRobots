/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Basics;

import java.awt.Graphics2D;

/**
 *
 * @author marcobarthel
 */
public class Draw {
    
    static public void drawVectorAtPosition( Graphics2D g, Vector pos, Vector v)
    {
        g.drawLine((int)pos.x, (int)pos.y, (int)(pos.x+v.x), (int)(pos.y+v.y));
    }
    
    static public void drawLineBetweenVectors(Graphics2D g, Vector a, Vector b)
    {
        g.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);
    }
    
    static public void drawPoint(Graphics2D g, Vector a, int size)
    {
        g.fillRect((int)a.x-size/2, (int)a.y-size/2, size, size);
    }    
    
    static public void drawOval(Graphics2D g, Vector a, int size)
    {
        g.fillOval((int)a.x-size/2, (int)a.y-size/2, size, size);
    }    
    
}

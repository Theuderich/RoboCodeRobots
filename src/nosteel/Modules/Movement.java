/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules;

import nosteel.Basics.Vector;
import nosteel.Modules.Move.AntiG;
import nosteel.Modules.Move.MoveAlgo;
import nosteel.Modules.Move.Hunt;
import nosteel.Modules.Move.SerpentineApproach;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import nosteel.Modules.Move.Sidestep;

/**
 *
 * @author marcobarthel
 */
public class Movement {
    
    private DataList scans;
    private Vector battleSize;

    
    public static int INDEX_AntiG = 0;
    public static int INDEX_Hunt = 1;
    public static int INDEX_SerpentineApproach = 2;
    public static int INDEX_Sidestep = 2;
    
    private int activeIndex;
    List<MoveAlgo> list;
    
    public Movement( DataList scans_, Aiming t )
    {
        scans = scans_;
        list = new ArrayList<MoveAlgo>();
        list.add(new AntiG(scans_, t));
        list.add(new Hunt(scans_, t));
        list.add(new SerpentineApproach(scans_, t));
        list.add(new Sidestep(scans_, t));
        activeIndex = 0;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public void setBattlefieldSize( Vector size )
    {
        battleSize = size;
        for( MoveAlgo e : list )
        {
            e.setBattlefieldSize(size);
            e.intitalize();
        }
    }
  
    public void process( Vector myPos, double myHeading )
    {
        list.get(activeIndex).process(myPos, myHeading);
    }

    public double getFinalRelativeAngle() 
    {
        return list.get(activeIndex).getFinalRelativeAngle();
    }

    public double getFinalDist() 
    {
        return list.get(activeIndex).getFinalDist();
    }
    
    public void draw(Graphics2D g)
    {
        list.get(activeIndex).draw(g);
    }
        
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules;

import nosteel.Basics.Vector;
import nosteel.Modules.Data.FiredBullet;
import nosteel.Modules.Targeting.Aim;
import nosteel.Modules.Targeting.LinearTargeting;
import nosteel.Modules.Targeting.PatternMatch;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mabarthe
 */
public class Aiming {

    public static int INDEX_LinearTargeting = 0;
    public static int INDEX_PatterMatch = 1;
    
    List<Aim> list;
    private int activeIndex;
    
    public Aiming( DataList scans_ ) 
    {
        list = new ArrayList<Aim>();
        list.add( new LinearTargeting( scans_ ));
        list.add(new PatternMatch( scans_ ));
        
        activeIndex = INDEX_LinearTargeting;
    }
    
    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public void setBattlefieldSize( Vector size )
    {
        for( Aim e : list )
        {
            e.setBattlefieldSize(size);
        }
    }

    public void setTargetName(String targetName)
    {
        for( Aim e : list )
        {
            e.setTargetName(targetName);
        }
    }
    
    public String getTargetName()
    {
        return list.get(activeIndex).getTargetName();
    }
    
    public boolean processAiming(  Vector myCurPos, double firePower, long time )
    {
        boolean temp = list.get(activeIndex).processAiming(myCurPos, firePower, time);
//        list.get(activeIndex).printAimingData(time);
        return temp;
        
    }
    
    public FiredBullet getFiredData() 
    {
        return list.get(activeIndex).getFiredData();
    }

    public void drawTargeting(Graphics2D g)
    {
        g.setColor(java.awt.Color.RED);
        list.get(activeIndex).drawTargeting(g);
    }
    
    
}

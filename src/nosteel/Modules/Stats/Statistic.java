/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosteel.Modules.Stats;

import nosteel.Basics.Vector;
import nosteel.Modules.Aiming;
import nosteel.Modules.LinearTracking;
import nosteel.Modules.DataList;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.PieChart.Data;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVPrinter;
import robocode.RobocodeFileWriter;

/**
 *
 * @author mabarthe
 */
public abstract class Statistic {
    
    protected DataList scans;
    protected Aiming aiming;
    protected Vector battleSize;
    private String name;
//    protected RobocodeFileWriter fileWriter;

    protected FileWriter fileWriter = null;
//    protected CSVPrinter csvFilePrinter = null;
    private static final String NEW_LINE_SEPARATOR = "\n";
    
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.GERMAN);
    
    public Statistic (DataList s, Aiming t, String name_)
    {
        scans = s;
        aiming = t;
        name = name_;

//            String path = String.format("./log/%s/%s.csv", format.format(new Date()), name);
//            fileWriter = new RobocodeFileWriter("~/" + name + ".csv");
    }
    
    public void openCSV()
    {
        boolean writeHeader = true;
//        try {
//            String file = name + ".csv";
//            String path = String.format("./log/", format.format(new Date()) );
//            
//            new File(path).mkdirs();
//            File f = new File( path + file);
//            if( f.exists() )
//                writeHeader = false;
            
//            CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
//            fileWriter = new FileWriter(path + file, !writeHeader);
//            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
//            if( writeHeader )
//                printCsvHeadline();
            
//        } catch (IOException ex) {
//            Logger.getLogger(Statistic.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }
    
    public void printCsvHeadline()
    {
        
    }
    public void printCsvRow( int round )
    {
        
    }
    
    public void setBattlefieldSize( Vector size )
    {
        battleSize = size;
    }
    
    public void draw(Graphics2D g)
    {

    }
    
    public void analyse()
    {
        
    }
    
    public void print()
    {
        
    }
}

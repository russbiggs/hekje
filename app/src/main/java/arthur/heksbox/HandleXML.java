package arthur.heksbox;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by artek on 27/02/2015.
 */
public class HandleXML {
    private String ySize ="ysize";
    private String xSize="xsize";
    private String yRes ="yResolution";
    private String xRes="xResolution";
    private String pos ="Position";
    private String walkingmode="Zigzag";
    private String date="Last modified";
    private String created="Date created";
    private String completed="Completed";

    private String idInput=null;

    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    public HandleXML(String id){this.idInput = id;} //maybe here try to find the file...

    public Grid getGrid(){

        Grid outputGrid = new Grid(idInput, Integer.valueOf(xSize), Integer.valueOf(ySize), Double.valueOf(xRes), Double.valueOf(yRes), Integer.valueOf(pos),Boolean.valueOf(walkingmode), Boolean.valueOf(completed));
        return outputGrid;
    }

    public String getGridID(){
        return idInput;
    }

    public String getLastModified(){
        return date;
    }

    public int getxSize(){
        int xSizeInt = Integer.valueOf(xSize);
        return xSizeInt;
    }

    public int getxMatSize(){
        int xMatSizeInt = (int) (Integer.valueOf(xSize)/Double.valueOf(xRes));
        return xMatSizeInt;
    }

    public int getyMatSize(){
        int yMatSizeInt = (int) (Integer.valueOf(ySize)/Double.valueOf(yRes));
        return yMatSizeInt;
    }

public void parseXMLAndStoreIt(XmlPullParser myparser) {
    int event;
    String text = null;
    try {
        event = myparser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = myparser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    break;
                case XmlPullParser.TEXT:
                    text = myparser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (name.equals("SizeX")) {
                        xSize = text;
                    } else if (name.equals("SizeY")) {
                        ySize = text;
                    } else if (name.equals("ResolutionX")) {
                        xRes = text;
                    } else if (name.equals("ResolutionY")) {
                        yRes = text;
                    } else if (name.equals("Position")) {
                        pos = text;
                    } else if (name.equals("Zigzag")) {
                        walkingmode = text;
                    } else if (name.equals("LastModified")) {
                        date = text;
                    }else if (name.equals("Created")) {
                        created = text;
                    }else if (name.equals("Completed")) {
                        completed = text;
                    }else {
                    }
                    break;
            }
            event = myparser.next();
        }
        parsingComplete = false;
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void fetchXML(){
        String state = Environment.getExternalStorageState();
        try {
            boolean mExternalStorageAvailable;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mExternalStorageAvailable = true;
            } else {
                mExternalStorageAvailable = false;
            }
            File xmlfile = null;
            if (mExternalStorageAvailable) {
                File sdDir = Environment.getExternalStorageDirectory();
                xmlfile = new File(sdDir + "/hekje/output/" + idInput + ".xml");
            }
            FileInputStream fileos = null;

            try {
                fileos = new FileInputStream(xmlfile);
            } catch (FileNotFoundException e) {
                Log.e("FileNotFoundException", e.toString());
            }

            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setInput(fileos, null);
            parseXMLAndStoreIt(myparser);
            fileos.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }


    }

    public void grid2Xml(Grid inputGrid) {

            String id=inputGrid.getName();
            int xSi=inputGrid.getXSize();
            int ySi=inputGrid.getYSize();
            Double xRe=inputGrid.getXRes();
            Double yRe=inputGrid.getYRes();
            int pos=inputGrid.getPos();
            boolean walkingmode=inputGrid.getZigzag();
            String date=inputGrid.getDate();
            String created=inputGrid.getDateCreated();
            boolean completed=inputGrid.getCompleted();

            String state = Environment.getExternalStorageState();
            boolean mExternalStorageWriteable;
            boolean mExternalStorageAvailable;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mExternalStorageAvailable = mExternalStorageWriteable = true;
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                mExternalStorageAvailable = true;
                mExternalStorageWriteable = false;
            } else {
                mExternalStorageAvailable = mExternalStorageWriteable = false;
            }
            if (mExternalStorageAvailable == true & mExternalStorageWriteable == true) {
                File sdDir = new File(Environment.getExternalStorageDirectory()+"/hekje/output");
                if(!sdDir.exists() && !sdDir.isDirectory()){sdDir.mkdir();}
                File newxmlfile = new File(sdDir+"/"+id+".xml");
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {
                    Log.e("IOException", "Exception in creating new File(");
                }

                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);

                } catch (FileNotFoundException e) {
                    Log.e("FileNotFoundException", e.toString());
                }
                XmlSerializer serializer = Xml.newSerializer();

                try {
                    serializer.setOutput(fileos, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                    serializer.startTag(null, "Grid");
                    serializer.startTag(null, "GridID");
                    serializer.text(id);
                    serializer.endTag(null, "GridID");
                    serializer.startTag(null, "SizeX");
                    serializer.text(String.valueOf(xSi));
                    serializer.endTag(null, "SizeX");
                    serializer.startTag(null, "SizeY");
                    serializer.text(String.valueOf(ySi));
                    serializer.endTag(null, "SizeY");
                    serializer.startTag(null, "ResolutionX");
                    serializer.text(String.valueOf(xRe));
                    serializer.endTag(null, "ResolutionX");
                    serializer.startTag(null, "ResolutionY");
                    serializer.text(String.valueOf(yRe));
                    serializer.endTag(null, "ResolutionY");
                    serializer.startTag(null, "Position");
                    serializer.text(String.valueOf(pos));
                    serializer.endTag(null, "Position");
                    serializer.startTag(null, "Zigzag");
                    serializer.text(String.valueOf(walkingmode));
                    serializer.endTag(null, "Zigzag");
                    serializer.startTag(null, "Created");
                    serializer.text(created);
                    serializer.endTag(null, "Created");
                    serializer.startTag(null, "LastModified");
                    serializer.text(date);
                    serializer.endTag(null, "LastModified");
                    serializer.startTag(null, "Completed");
                    serializer.text(String.valueOf(completed));
                    serializer.endTag(null, "Completed");
                    serializer.endTag(null, "Grid");

                    serializer.endDocument();
                    serializer.flush();
                    fileos.close();

                } catch (Exception e) {
                    Log.e("Exception", "Exception occurred in writing");
                }


            } else {
            }

        }
}









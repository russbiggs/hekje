package arthur.heksbox;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Grid {
    private String gridID;
    private int xSize;
    private int ySize;
    private Double xRes;
    private Double yRes;
    private int pos;
    private boolean zigzag;
    private boolean completed;
    private String date;
    private String created;



    public Grid(){
        gridID = "Inconnu";
        xSize = 20;
        ySize = 20;
        xRes = 1.0;
        yRes= 1.0;
        zigzag = true;
        pos = 1;
        SimpleDateFormat df = new SimpleDateFormat("EEE dd.MM.yyyy HH:mm");
        date= df.format(Calendar.getInstance().getTime());
        created= df.format(Calendar.getInstance().getTime());
        completed=false;

    }

    public Grid(String pGridID, int pXSize, int pYSize, Double pXRes, Double pYRes, int position, boolean walkingmode, boolean complete)
    {
        gridID = pGridID;
        xSize = pXSize;
        ySize = pYSize;
        xRes = pXRes;
        yRes= pYRes;
        zigzag = walkingmode;
        pos = position;
        SimpleDateFormat df = new SimpleDateFormat("EEE dd.MM.yyyy HH:mm");
        date= df.format(Calendar.getInstance().getTime());
        created= df.format(Calendar.getInstance().getTime());
        completed=complete;

    }

    // Set up the Getters
    public String getName() {
        return gridID;
    }

    public int getPos() {
        return pos;
    }

    public boolean getZigzag() {
        return zigzag;
    }

    public int getXSize() {
        return xSize;
    }

    public String getDate() {return date; }

    public String getDateCreated(){return created; }

    public int getYSize() {
        return ySize;
    }

    public Double getXRes() {
        return xRes;
    }

    public Double getYRes() {
        return yRes;
    }

    public boolean getCompleted() {
        return completed;
    }

    // Set up the Setters

    public void setDate(){
        SimpleDateFormat df = new SimpleDateFormat("EEE dd.MM.yyyy HH:mm");
        date= df.format(Calendar.getInstance().getTime());
    }

    public void setPos(int position){
        pos=position;
}

    public void setCompleted(boolean status){
        completed=true;
    }

}

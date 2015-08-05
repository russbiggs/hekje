package arthur.heksbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;


public class Record extends ActionBarActivity implements View.OnClickListener {
    TextView coordinatesBox = null;
    GridView logView = null;
    String coordinates = null;
    Double[][] logArray;
    EditText resistivityValue = null;
    Double valueF = 0.0;
    Button send = null;
    Button back = null;
    Button skip = null;
    Button next = null;
    String logStr;
    int pos = 0;
    boolean walkingmode;
    Grid currentGrid;
    String currentGridId = null;
    int[] matPos = {0, 0, 0, 0}; //2 first values are the XY in the field. The 2 seconds are the XY in the matrix (that starts at 0 and not 1)
    int end;
    int rows;
    int cols;
    Double min=99999.9;
    Double max = 0.0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentGridId = "Grid Name not recieved";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            currentGridId = extras.getString("gridID");
        }

        currentGrid = loadGrid(currentGridId);

        pos = currentGrid.getPos();
        walkingmode = currentGrid.getZigzag();
        end = definePosMax(currentGrid);
        cols = defineColsRows(currentGrid)[0];
        rows = defineColsRows(currentGrid)[1];


        try {
            logArray = initArray(rows, cols);
        } catch (Exception e) {
            Log.e("Exception", "Exception occurred in initializing the matrix");
        }


        setContentView(R.layout.activity_record);

        send = (Button) findViewById(R.id.send);
        back = (Button) findViewById(R.id.back);
        skip = (Button) findViewById(R.id.skip);
        next = (Button) findViewById(R.id.next);

        send.setOnClickListener(this);
        back.setOnClickListener(this);
        skip.setOnClickListener(this);
        next.setOnClickListener(this);


        matPos = updatePosition(pos, walkingmode);
        coordinates = currentGridId + "    Line " + matPos[0] + "   -   Log " + matPos[1];
        coordinatesBox = (TextView) findViewById(R.id.coordinates);
        coordinatesBox.setText(coordinates);

        resistivityValue = (EditText) findViewById(R.id.inputValue);


        resistivityValue.setText("");

       /*create the array with empty cells*/
        String[] emptyCells = initCells(end);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, emptyCells);
        /*TextView item;
        item= (TextView) findViewById(R.id.txt);
        item.getLayoutParams().height= 10;*/
        logView = (GridView) findViewById(R.id.gridView);
        logView.setNumColumns(cols);
        logView.setAdapter(adapter);


        logStr = log2String(logArray);

    }

    public void onClick(View v) {
        // On récupère l'identifiant de la vue, et en fonction de cet identifiant…
        switch (v.getId()) {

            // Si l'identifiant de la vue est celui du premier bouton
            case R.id.send:
                if (resistivityValue.getText().toString().equals("")) {
                } else {
                    valueF = Double.valueOf(resistivityValue.getText().toString());
                    recordMatrix(matPos[2], matPos[3], valueF);
                    if (logArray[matPos[3]][matPos[2]]!=0.0){
                       scanMinMax();

                        resetLogView();
                    }else{
                        if (valueF<=min & valueF!=0.0){
                            min= valueF;
                        }
                        if (valueF>=max){
                            max=valueF;
                            resetLogView();
                        }
                        setColorLastPoint();
                    }

                    logStr = log2String(logArray);
                    saveArray(currentGridId, logStr);
                    setColorLastPoint();

                        if (pos <= end - 2) {
                            pos = moveLog(pos, 1);

                            updatePosition(pos, walkingmode);
                            updateCursor();

                            currentGrid.setPos(pos);


                            coordinates = currentGridId + "    Line " + matPos[0] + "   -   Log " + matPos[1];
                            coordinatesBox = (TextView) findViewById(R.id.coordinates);
                            coordinatesBox.setText(coordinates);


                            resistivityValue.setText("");


                    } else {
                        pos = moveLog(pos, 1);
                        coordinatesBox.setText(currentGridId + " completed.");
                        currentGrid.setCompleted(true);
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Grid saved. OK to continue, Back to edit last log.").setPositiveButton("OK", dialogClickListener)
                                .setNegativeButton("Back", dialogClickListener).show();
                    }
                    currentGrid.setDate();
                    SaveGridXml(currentGrid);
                }
                break;
            case R.id.back:
                setColorLastPoint();
                if (pos > 0) {
                    pos = moveLog(pos, -1);
                }


                updatePosition(pos, walkingmode);
                updateCursor();
                currentGrid.setPos(pos);

                coordinates = currentGridId + "    Line " + matPos[0] + "   -   Log " + matPos[1];
                coordinatesBox = (TextView) findViewById(R.id.coordinates);
                coordinatesBox.setText(coordinates);



                resistivityValue.setText(logArray[matPos[3]][matPos[2]].toString());

                break;
            case R.id.next:
                setColorLastPoint();
                if (pos < end) {
                    pos = moveLog(pos, 1);
                }


                updatePosition(pos, walkingmode);

                updateCursor();
                currentGrid.setPos(pos);

                coordinates = currentGridId + "    Line " + matPos[0] + "   -   Log " + matPos[1];
                coordinatesBox = (TextView) findViewById(R.id.coordinates);
                coordinatesBox.setText(coordinates);



                resistivityValue.setText(logArray[matPos[3]][matPos[2]].toString());

                break;

            case R.id.skip:
                valueF = 0.0;
                recordMatrix(matPos[2], matPos[3], valueF);

                logStr = log2String(logArray);
                saveArray(currentGridId, logStr);

                if (pos <= end - 2) {
                    pos = moveLog(pos, 1);
                    setColorLastPoint();
                    updatePosition(pos, walkingmode);
                    updateCursor();
                    currentGrid.setPos(pos);


                    coordinates = currentGridId + "    Line " + matPos[0] + "   -   Log " + matPos[1];
                    coordinatesBox = (TextView) findViewById(R.id.coordinates);
                    coordinatesBox.setText(coordinates);


                    resistivityValue.setText("");


                } else {
                    pos = moveLog(pos, 1);
                    coordinatesBox.setText(currentGridId + " completed.");
                    currentGrid.setCompleted(true);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Grid saved. OK to continue, Back to edit last log.").setPositiveButton("OK", dialogClickListener)
                            .setNegativeButton("Back", dialogClickListener).show();
                }
                currentGrid.setDate();
                SaveGridXml(currentGrid);



                resistivityValue.setText("");

                break;
        }
    }

    ;


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public int[] updatePosition(int pos, boolean walkingmode) {
        int xCoordinate = (int) (pos / rows + 1);
        int yCoordinate = (int) (pos % rows + 1);
        int maty;
        int matx = (int) (pos / rows);
        if (walkingmode & xCoordinate % 2 == 0) {
            maty = (int) (rows - (pos % rows) - 1);
        } else {
            maty = (int) (pos % rows);
        }
        matPos[0] = xCoordinate;
        matPos[1] = yCoordinate;
        matPos[2] = matx;
        matPos[3] = maty;
        return matPos;
    }


    public static int moveLog(int pos, int distance) {
        int newpos = pos + distance;
        return newpos;
    }

    public void scanMinMax(){
        int y=0;
        int x=0;
        min=9999.9;
        max=0.0;
        for(y =0; y<rows; y++){
            for (x = 0; x < cols; x++) {
                valueF=logArray[x][y];
                if (valueF<=min & valueF!=0.0){
                    min= valueF;
                }
                if (valueF>=max){
                    max=valueF;
                }
            }
        }
    }

    public void updateCursor() {
        int cursor = (matPos[3] * rows + matPos[2]);
        View cell = (View) logView.getChildAt(cursor);
        cell.setBackgroundColor(Color.RED);
    }

    public void setColorLastPoint() { //to be placed before updating position
        int i;
        int x;

        int lastPoint = ((matPos[3]) * rows + matPos[2]);
        View cell = (View) logView.getChildAt(lastPoint);

        Double coef = (255 / max);
        int maty = (int) (pos % rows);
        int matx = (int) (pos / rows);
        Double value = logArray[maty][matx];
        if ((value != 0.0) & (min != max)) {
            x = (int) (255 - (value * coef));
            cell.setBackgroundColor(Color.argb(255, x, x, x));
        } else {
            cell.setBackgroundColor(Color.BLUE);
        }
    }

    public void resetLogView() {
        int i;
        int x;

        int cursor = (matPos[3]*rows + matPos[2]);
        Double coef= (255/max);

        for (i = 0; i < end; i++) {

            View cell = (View) logView.getChildAt(i);
            if (i == cursor) {
                cell.setBackgroundColor(Color.RED);
            } else {
                int maty = (int) (i % rows);
                int matx = (int) (i / rows);
                Double value = logArray[matx][maty];
                if ((value !=0.0) & (min!=max)){
                    x= (int) (255-(value*coef));
                    cell.setBackgroundColor(Color.argb(255,x,x,x));
                }
                else {
                    cell.setBackgroundColor(Color.BLUE);
                }
            }
        }
    }
    public void createLogView() {
        int i;

        for (i = 0; i < end; i++) {

            View cell = (View) logView.getChildAt(i);
            if (i == 0) {
                cell.setBackgroundColor(Color.RED);
            } else{
                    cell.setBackgroundColor(Color.BLUE);

            }
        }
    }



    public String log2String(Double[][] array){

        String logStr="";

        int x;
        int y;
        String sep="";
        StringBuilder logBuilder = new StringBuilder();
        for(y =0; y<rows; y++){
            for (x = 0; x < cols; x++) {
                if (x<(cols-1)){sep=",";}
                else{sep="\n";}
                logStr = (logBuilder.append(String.valueOf(array[y][x]) + sep)).toString();
            }
        }
        return logStr;
    }

    public static Grid loadGrid(String ID) {
        Grid loadedGrid =new Grid();
        try {
            HandleXML obj;
            obj = new HandleXML(ID);
            obj.fetchXML();
            while (obj.parsingComplete) ;
            loadedGrid = obj.getGrid();
        }catch (Exception e) {
            Log.e("Exeption", "No grid Loaded. Virtual grid instead");
        }

        return loadedGrid;
    }


    public static Double[][] initArray(int rows, int cols){
        int x;
        int y;
        Double[][] array= new Double [rows][cols];

        for(x =0; x<cols; x++){
            for (y = 0; y < rows; y++) {
               array[y][x] = 0.0;
             }
        }
        return array;
    }

    public static String[] initCells(int end){
        int x;
        String[] emptyCells= new String [end];

        for(x =0; x<end; x++){
                emptyCells[x] = "";
        }
        return emptyCells;
    }

    public void saveArray(String ID, String arrayString) {
        HandleCSV obj;
        obj = new HandleCSV(ID);
        obj.array2Csv(ID, arrayString);
    }

    public static Bitmap bitmapFromArray(Double[][] pixels2d){
        int width = pixels2d.length;
        int height = pixels2d[0].length;
        int[] pixels = new int[width * height];
        int pixelsIndex = 0;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                pixels[pixelsIndex] = (int)(pixels2d[i][j]*100);
                pixelsIndex ++;
            }
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    public void SaveGridXml(Grid gridToSave) {
        HandleXML obj;
        String ID = gridToSave.getName();
        obj = new HandleXML(ID);
        obj.grid2Xml(gridToSave);
    }

    public void recordMatrix(int posx, int posy, Double value) {
     logArray[posy][posx]=value;
    }


    public static int definePosMax (Grid currentGrid) {
        int xSize=currentGrid.getXSize();
        int ySize=currentGrid.getYSize();
        double xRes=currentGrid.getXRes();
        double yRes=currentGrid.getYRes();
        return (int) (xSize/xRes*ySize/yRes);
        }

    public static int[] defineColsRows (Grid currentGrid) {
        int xSize=currentGrid.getXSize();
        int ySize=currentGrid.getYSize();
        double xRes=currentGrid.getXRes();
        double yRes=currentGrid.getYRes();
        int[] colsRows = {0,0};
        colsRows[0] = (int) ((int) xSize/xRes);
        colsRows[1] = (int) ((int) ySize/yRes);
        return colsRows;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

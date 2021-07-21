package arthur.heksbox;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/* create manually a repertory on the sdCard /hekje/output/*/

public class CreateGrid extends ActionBarActivity{

    Button validate = null;
    EditText GridID = null;
    EditText xSize = null;
    EditText ySize= null;
    EditText xRes = null;
    EditText yRes = null;
    RadioGroup walking = null;
    RadioGroup heksbox = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grid);
        validate = (Button)findViewById(R.id.validate);

        GridID = (EditText)findViewById(R.id.gridID);
        xSize = (EditText)findViewById(R.id.Xsize);
        ySize = (EditText)findViewById(R.id.Ysize);
        xRes = (EditText)findViewById(R.id.Xresolution);
        yRes = (EditText)findViewById(R.id.Yresolution);
        walking = (RadioGroup)findViewById(R.id.walking);

        validate.setOnClickListener(validateListener);
    }

    protected View.OnClickListener validateListener;

    {
        validateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = GridID.getText().toString();
                String xSi = xSize.getText().toString();
                int xS = Integer.valueOf(xSi);
                String ySi = ySize.getText().toString();
                int yS = Integer.valueOf(ySi);
                String xRe = xRes.getText().toString();
                double xR = Double.parseDouble(xRe);
                String yRe = yRes.toString();
                Double yR = Double.valueOf(xRe);
                boolean walkingmode;
                walkingmode = walking.getCheckedRadioButtonId() == R.id.zigzag;

                if(!id.equals("") & !xSi.equals("") & !ySi.equals("") & !xRe.equals("") & !yRe.equals("") & xR!=0 & yR!=0 & xS!=0 & yS!=0 & xS%xR==0 & yS%yR==0) {

                    Grid newGrid = new Grid(id, xS, yS, xR, yR, 0, walkingmode, false);
                    boolean fileAlreadyExist=fileExist(newGrid);

                    if (fileAlreadyExist){
                        Toast.makeText(CreateGrid.this, "This grid name already exist", Toast.LENGTH_SHORT).show();
                    }else {
                        SaveGridXml(newGrid);
                        Intent intent = new Intent(CreateGrid.this, Record.class);
                        intent.putExtra("gridID", id);
                        startActivity(intent);
                    }
                }
            }
        };
    }

    public void SaveGridXml(Grid grid) {
        HandleXML obj;
        String ID = grid.getName();
        obj = new HandleXML(ID);
        obj.grid2Xml(grid);
    }

    public boolean fileExist(Grid inputGrid) {
        boolean fileAlreadyExist=false;
        String id=inputGrid.getName();

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
            File sdDir = Environment.getExternalStorageDirectory();
            File newxmlfile = new File(sdDir + "/hekje/output/" + id + ".xml");
            if (!newxmlfile.exists()) {
                fileAlreadyExist = false;
            } else {
                fileAlreadyExist = true;
            }
            try {
                newxmlfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return fileAlreadyExist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input__data, menu);
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

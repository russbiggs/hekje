package arthur.heksbox;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;


public class HandleCSV {
    private Double[][]  matrix;

    private String idInput=null;


    public HandleCSV(String id){
        this.idInput = id;
    }

    public Double[] csv2Array(int rows,int end){
        Double[] array = new Double[end];
        File csvfile= null;
        String state = Environment.getExternalStorageState();
        boolean mExternalStorageAvailable;
        try {
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mExternalStorageAvailable = true;
            } else {
                mExternalStorageAvailable = false;
            }

            if (mExternalStorageAvailable) {
                File sdDir = Environment.getExternalStorageDirectory();
               csvfile = new File(sdDir + "/hekje/output/" + idInput + ".csv");
            }//Else ErrorMessage
        } catch ( Exception e) {
            e.printStackTrace();

            BufferedReader bufRdr = null;

            try {
                bufRdr = new BufferedReader(new FileReader(csvfile));
            String line = null;
            int i=0;

            while ((line = bufRdr.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                while (st.hasMoreTokens()) {
                    array[i] = Double.valueOf(st.nextToken());
                }
                i++;
            }
                bufRdr.close();
            }catch (Exception ee) {
                Log.e("IOException", ee.toString());
            }



        }
        return array;
        }


    public void array2Csv(String idInput,String csvContent) {

        FileWriter writer;
        File csvfile = null;

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
            csvfile = new File(sdDir+"/"+idInput+".csv");
            try {
                csvfile.createNewFile();
            } catch (IOException e) {
                Log.e("IOException", "Exception in creating new File(");
            }

            FileOutputStream fileos = null;
            try {
                fileos = new FileOutputStream(csvfile);

            } catch (FileNotFoundException e) {
                Log.e("FileNotFoundException", e.toString());
            }
        }


        try{
            writer= new FileWriter(csvfile);
            writer.write(csvContent);
            writer.flush();
            writer.close();

            }catch (IOException e) {e.printStackTrace();}
    }
}

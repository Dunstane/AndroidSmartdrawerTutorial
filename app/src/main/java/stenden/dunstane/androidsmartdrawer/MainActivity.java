package stenden.dunstane.androidsmartdrawer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.lang.String;

public class MainActivity extends Activity implements OnClickListener{
    private DrawingView drawView;

    private ImageButton saveBtn, newBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = (DrawingView)findViewById(R.id.drawing);
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);
                    //png saving
                    Bitmap viewCache = drawView.getDrawingCache();

                    try {
                        File fPath = Environment.getExternalStorageDirectory();
                        File f = null;
                        f = new File(fPath + "/Pictures", "drawing.png");

                        FileOutputStream outstream = new FileOutputStream(f);
                        viewCache.compress(Bitmap.CompressFormat.PNG, 100, outstream);
                        Toast savedmyToast = Toast.makeText(getApplicationContext(), "Drawing Saved to device", Toast.LENGTH_SHORT);
                        savedmyToast.show();

                    } catch (Exception e) {
                        Toast unsavedmyToast = Toast.makeText(getApplicationContext(), "Drawing not saved to device error message" + e.getMessage(), Toast.LENGTH_SHORT);
                        unsavedmyToast.show();
                    }

//svg saving
                    try {
                        File fPath = Environment.getExternalStorageDirectory();
                        File f = null;
                        f = new File(fPath + "/Pictures", "drawing.txt");

                        FileWriter fw=new FileWriter(f);
                        BufferedWriter writer = new BufferedWriter(fw);
                        if (!f.exists())
                        {
                            try {
                                f.createNewFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            finally {
                                try {
                                    DisplayMetrics metrics = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                    int width = metrics.widthPixels;
                                    int height = metrics.heightPixels;
                                    writer.write("<svg height=\""+height+"\" width=\""+width+"\">");

                                    int counter = 0;

                                    for (ArrayList<PointF> Line : drawView.giveAllLines()) {

                                        writer.write("<polyline points="+"\"" );
                                        for (PointF myPoint : Line)
                                        {

                                            if (counter == 0)
                                            {
                                                //setting up starting point
                                                try {
                                                        writer.write(String.valueOf(myPoint.x));
                                                        writer.write(",");
                                                        writer.write(String.valueOf(myPoint.x));
                                                        writer.write(" ");
                                                        writer.write(String.valueOf(myPoint.y));
                                                        writer.write(",");
                                                        counter++;
                                                } catch (Exception e) {

                                                }
                                            }
                                            else
                                            {

                                                if(counter<Line.size())
                                                {
                                                    writer.write(String.valueOf(myPoint.x));
                                                    writer.write(" ");
                                                    writer.write(String.valueOf(myPoint.y));
                                                    writer.write(",");
                                                    counter++;

                                                }
                                                else
                                                {
                                                    writer.write(String.valueOf(myPoint.x));
                                                    writer.write(" ");
                                                    writer.write(String.valueOf(myPoint.y));
                                                }
                                            }
                                        }
                                        counter=0;
                                        writer.write("\" " +   "style=fill:none;stroke:black;stroke-width:2 />");

                                    }
                                    writer.write(" </svg>");

                                }
                                catch (Exception e)
                                {
                                    Toast myToast = Toast.makeText(getApplicationContext(), "Drawing SVG not exported " + e.getMessage(),Toast.LENGTH_SHORT);
                                    myToast.show();
                                }

                                Toast myToast = Toast.makeText(getApplicationContext(), "Writer SVG generated ",Toast.LENGTH_SHORT);
                                myToast.show();
                                writer.flush();
                            }
                        }
                        else
                        {
                            try {
                                DisplayMetrics metrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                int width = metrics.widthPixels;
                                int height = metrics.heightPixels;
                                writer.write("<svg height=\""+height+"\" width=\""+width+"\">");
                                int counter = 0;
                                for (ArrayList<PointF> Line : drawView.giveAllLines()) {
                                    writer.write("<polyline points= \"");

                                    for (PointF myPoint : Line) {
                                        if (counter == 0) {
                                            //setting up starting point
                                            try {
                                                writer.write(String.valueOf(myPoint.x));
                                                writer.write(",");
                                                writer.write(String.valueOf(myPoint.x));
                                                writer.write(" ");
                                                writer.write(String.valueOf(myPoint.y));
                                                writer.write(",");
                                                counter++;
                                            } catch (Exception e) {

                                            }
                                        }
                                        else
                                        {
                                            if(counter<Line.size())
                                            {
                                                writer.write(String.valueOf(myPoint.x));
                                                writer.write(" ");
                                                writer.write(String.valueOf(myPoint.y));
                                                writer.write(",");
                                                counter++;

                                            }
                                            else
                                            {
                                                writer.write(String.valueOf(myPoint.x));
                                                writer.write(" ");
                                                writer.write(String.valueOf(myPoint.y));
                                            }
                                        }
                                    }
                                    counter=0;
                                    writer.write("\""+   " style=fill:none;stroke:black;stroke-width:2 />");


                                }
                                writer.write(" </svg>");

                            }
                            catch (Exception e)
                            {
                                Toast myToast = Toast.makeText(getApplicationContext(), "Error in SVG" + e.getMessage(),Toast.LENGTH_SHORT);
                            }
                        }
                        Toast myToast = Toast.makeText(getApplicationContext(), "Writer SVG generated ",Toast.LENGTH_SHORT);
                        myToast.show();
                        writer.flush();
                    }
                    catch (Exception e)
                    {
                          Toast myToast = Toast.makeText(getApplicationContext(), "Error in SVG" + e.getMessage(),Toast.LENGTH_SHORT);
                        myToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
    }
}

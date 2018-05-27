package stanford.edu.livetrak2;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import stanford.edu.livetrak2.LiveTrakConstants.Language;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class SessionActivity extends Activity implements LiveTrakConstants {
    private static final String APP_STORAGE_DIR = "LiveTrak/";
    public static final String CONFIG_FILE = "LiveTrak_soil_v1.csv";
    public static final Language LANG = Language.ENGLISH;
    private static String TAG = "SessionActivity";
    private static File appOutputDir = null;
    private FileWriter fw;
    private HandState leftHand = null;
    private RadioButtonGroup location = null;
    private HandState rightHand = null;
    private long timeOffset = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appOutputDir = getOutputDir();
        Intent intent = getIntent();
        this.fw = createNewFile(intent);
        initFileHeader(intent);
        initializeUi(intent);
    }

    private File getOutputDir() {
        File root = Environment.getExternalStorageDirectory();
        Log.e(TAG, "getExternalStoragePublicDirectory(): " + root.getAbsolutePath());
        File outputDir = new File(root, APP_STORAGE_DIR);
        if (!(outputDir.exists() || outputDir.mkdirs())) {
            displayDialogAndExit("Output directory could not be created. App will exit.", "Okay");
        }
        Log.i(TAG, "output dir exists now: " + outputDir.getAbsolutePath());
        return outputDir;
    }

    private void initializeUi(Intent intent) {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        HashMap<String, RadioButtonGroup> buttonGroups = new HashMap();
        LayoutData data = new LayoutData(this, CONFIG_FILE, LANG);
        LinearLayout grid = new LinearLayout(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int buttonHeight = (int) (((double) metrics.heightPixels) / (((double) (data.maxColItemCount + 3)) * 1.0d));
        int numColumns = data.options.size();
        for (int colIndx = 0; colIndx < numColumns; colIndx++) {
            ColumnData colData = (ColumnData) data.options.get(colIndx);
            LinearLayout col = new LinearLayout(this);
            col.setOrientation(LinearLayout.VERTICAL);
            col.setLayoutParams(new LayoutParams(-1, -1, 1.0f));
            TextView text = new TextView(this);
            if (LANG == Language.BANGLA) {
                text.setTypeface(Typeface.createFromAsset(getAssets(), "font/rupali_01-02-2007.ttf"));
            }

            Log.i(TAG, "COL DATA LABEL: " + colData.label);
            text.setText(colData.label);
            text.setTextSize(17.0f);
            col.addView(text);
            Iterator it = colData.options.iterator();
            while (it.hasNext()) {
                OptionData od = (OptionData) it.next();
                RadioButtonX rb = addNewRadioButton(od, buttonGroups);
                rb.getLayoutParams().height = buttonHeight;
                if (!(od == null || od.group == null || od.group.equals("") || !od.group.equalsIgnoreCase("end"))) {
                    rb.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            SessionActivity.this.endSession(v);
                        }
                    });
                }
                col.addView(rb);
            }
            grid.addView(col);
        }
        assignButtonGroupsToHandStates(buttonGroups);
        setContentView(grid);
    }

    private void assignButtonGroupsToHandStates(HashMap<String, RadioButtonGroup> bg) {
        this.location = (RadioButtonGroup) bg.get("LOCATION");
        this.leftHand = new HandState(this, "left", (RadioButtonGroup) bg.get("LEFT HAND OBJECT"), (RadioButtonGroup) bg.get("LEFT HAND FREQUENCY"), (RadioButtonGroup) bg.get("LEFT HAND INTERACTION"));
        this.rightHand = new HandState(this, "right", (RadioButtonGroup) bg.get("RIGHT HAND OBJECT"), (RadioButtonGroup) bg.get("RIGHT HAND FREQUENCY"), (RadioButtonGroup) bg.get("RIGHT HAND INTERACTION"));
    }

    private RadioButtonX addNewRadioButton(OptionData od, HashMap<String, RadioButtonGroup> groups) {
        RadioButtonX rb = new RadioButtonX(this, od);
        rb.setTypeface(Typeface.createFromAsset(getAssets(), "font/rupali_01-02-2007.ttf"));
        rb.setPadding(rb.getPaddingLeft(), rb.getPaddingTop(), rb.getPaddingRight(), 0);
        if (!(od == null || od.group == null || od.group.equalsIgnoreCase("END"))) {
            RadioButtonGroup rbg;
            if (groups.containsKey(od.group)) {
                rbg = (RadioButtonGroup) groups.get(od.group);
            } else {
                rbg = new RadioButtonGroup(this, od.group);
                groups.put(od.group, rbg);
            }
            rbg.addButton(rb);
        }
        return rb;
    }

    private void initFileHeader(Intent intent) {
        Bundle extras = intent.getExtras();
        writeLineToOutput("CODER_ID: " + extras.get(LoginActivity.CODER_ID));
        writeLineToOutput("SUBJECT_ID: " + extras.get(LoginActivity.SUBJECT_ID));
    }

    private FileWriter createNewFile(Intent intent) {
        try {
            Log.i(TAG, "Creating filewriter");
            Log.i(TAG, "filename: " + getFilename(intent));
            Log.i(TAG, "file path: " + appOutputDir.isDirectory() + appOutputDir.exists());
            FileWriter fw = new FileWriter(appOutputDir + getFilename(intent));
            Log.i(TAG, "FileWriter created: " + fw.getClass().toString());
            return fw;
        } catch (IOException e) {
            e.printStackTrace();
            displayDialogAndExit("Output file could not be created. App will exit.", "Okay");
            return null;
        }
    }

    public void displayDialogAndExit(String msg, String buttonText) {
        new Builder(this).setMessage(msg).setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -3:
                        System.exit(0);
                        return;
                    default:
                        return;
                }
            }
        }).show();
    }

    private String getFilename(Intent intent) {
        return new StringBuilder(String.valueOf(new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()))).append("_").append(intent.getStringExtra(LoginActivity.SUBJECT_ID)).append(".csv").toString();
    }

    public void writeLineToOutput(String str) {
        Log.i(TAG, "Writing to output: " + str);
        try {
            this.fw.write(new StringBuilder(String.valueOf(str)).append("\n").toString());
        } catch (IOException e) {
            displayDialogAndExit("Error writing \"" + str + "\" to output file", "Okay");
            e.printStackTrace();
        }
    }

    private void writeOutputHeader(Long observationTime) {
        this.timeOffset = observationTime.longValue();
        writeLineToOutput("DATE/TIME: " + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()));
        writeLineToOutput("");
        writeLineToOutput("Elapsed time (ms), Location, Observation");
    }

    private void recordObservation(Long observationTime, String location, String handObservation) {
        if (this.timeOffset < 0) {
            writeOutputHeader(observationTime);
        }
        writeLineToOutput(new StringBuilder(String.valueOf(observationTime.longValue() - this.timeOffset)).append(",").append(location).append(",").append(handObservation).toString());
    }

    private void endSession(View v) {
        try {
            writeLineToOutput(new StringBuilder(String.valueOf(SystemClock.elapsedRealtime() - this.timeOffset)).append(", END").toString());
            this.fw.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing file");
            e.printStackTrace();
        }
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void recordChange(RadioButtonGroup group) {
        Long observationTime = Long.valueOf(SystemClock.elapsedRealtime());
        if (this.leftHand == null || this.rightHand == null) {
            Log.e(TAG, "null sadness!!!");
            return;
        }
        boolean recordChange = this.leftHand.isChanged();
        if (this.leftHand.isChanged() || group.equals(this.location)) {
            recordObservation(observationTime, this.location.toString(), this.leftHand.getAndUpdateHandStateString());
        }
        if (this.rightHand.isChanged() || group.equals(this.location)) {
            recordObservation(observationTime, this.location.toString(), this.rightHand.getAndUpdateHandStateString());
        }
    }
}

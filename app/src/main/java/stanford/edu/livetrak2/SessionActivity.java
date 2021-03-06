package stanford.edu.livetrak2;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.res.AssetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaCas;
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

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class SessionActivity extends Activity implements LiveTrakConstants {
    private static final String APP_STORAGE_DIR = "LiveTrak/";
    public static final String CONFIG_FILE = "LiveTrak_soil_v1.csv";
    private static String TAG = "SessionActivity";
    private static File appOutputDir = null;
    private FileWriter fw;
    private HashMap<String, RadioButtonGroup> buttonGroups = new HashMap();

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

        LayoutData layoutData = loadData(CONFIG_FILE);

        LinearLayout grid = new LinearLayout(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int buttonHeight = (int) (((double) metrics.heightPixels) / (((double) (layoutData.maxColItemCount + 3)) * 1.0d));
        int numColumns = layoutData.columnDatas.size();
        for (int colIndx = 0; colIndx < numColumns; colIndx++) {
            ColumnData colData = layoutData.columnDatas.get(colIndx);
            LinearLayout col = new LinearLayout(this);
            col.setOrientation(LinearLayout.VERTICAL);
            col.setLayoutParams(new LayoutParams(-1, -1, 1.0f));
            TextView text = new TextView(this);

            Log.i(TAG, "COL DATA LABEL: " + colData.label);
            text.setText(colData.label);
            text.setTextSize(17.0f);
            col.addView(text);
            Iterator<OptionData> it = colData.optionDatas.iterator();
            while (it.hasNext()) {
                OptionData od = it.next();
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
        setContentView(grid);
    }

    private LayoutData loadData(String configFileName) {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        LayoutData layoutData = null;

        try {
            inputStream = assetManager.open(configFileName);
            layoutData = (new LayoutCsvParser()).parse(inputStream);
        } catch (IOException e) {
            String errorMsg = "Error: Could not open config file (" + configFileName + "). App will exit.";
            Log.e(TAG, errorMsg);
            displayDialogAndExit(errorMsg, "Okay");
        }

        return layoutData;
    }

    private RadioButtonX addNewRadioButton(OptionData od, HashMap<String, RadioButtonGroup> groups) {
        RadioButtonX rb = new RadioButtonX(this, od);
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

    public void recordChange(RadioButtonGroup group) {
        Long observationTime = Long.valueOf(SystemClock.elapsedRealtime());

        for(RadioButtonGroup g : buttonGroups.values()) {
            if (!g.isChecked()) {
                //Do not record an observation if a radio button group does not have a selectio
                return;
            }
        }

        if (this.timeOffset < 0) {
            //for the first observation, record everything
            for(RadioButtonGroup g : buttonGroups.values()) {
                recordObservation(observationTime, g.groupName, g.getCheckedRadioButton().toString());
            }
        } else {
            recordObservation(observationTime, group.groupName, group.getCheckedRadioButton().toString());
        }
    }

    private void recordObservation(Long observationTime, String radioButtonGroupName, String selectedOption) {
        if (this.timeOffset < 0) {
            writeOutputHeader(observationTime);
        }
        writeLineToOutput(new StringBuilder(String.valueOf(observationTime.longValue() - this.timeOffset)).append(",").append(radioButtonGroupName).append(",").append(selectedOption).toString());
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
}

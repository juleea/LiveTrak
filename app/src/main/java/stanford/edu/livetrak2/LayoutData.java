package stanford.edu.livetrak2;

import android.content.res.AssetManager;
import android.util.Log;

import stanford.edu.livetrak2.LiveTrakConstants.Language;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LayoutData {
    private static final String NEW_COLUMN = "<COLUMN>";
    private static final String SPACE = "<SPACE>";
    private static String TAG = "LayoutData";
    public int maxColItemCount = 0;
    public ArrayList<ColumnData> columnDatas;

    public LayoutData(ArrayList<ColumnData> columnDatas, int maxColItemCount) {
        this.columnDatas = columnDatas;
        this.maxColItemCount = maxColItemCount;
    }






//    public LayoutData(SessionActivity session, String configFileName, Language language) {
//        AssetManager assetManager = session.getAssets();
//        InputStream inputStream = null;
//
//        IOException e22;
//        try {
//            inputStream = assetManager.open(configFileName);
//        } catch (IOException e) {
//            Log.e(TAG, "Error: Could not open config file (" + configFileName + ") App will exit.");
//            try {
//                for(String asset : assetManager.list("")) {
//                    Log.e(TAG, asset);
//                }
////                Log.e(TAG, "Number of assets: " + );
//            } catch (IOException e2) {
//                e2.printStackTrace();
//            }
//            session.displayDialogAndExit("Error: Could not open config file. App will exit.", "Okay");
//        }
//        BufferedReader configFileReader = new BufferedReader(new InputStreamReader(inputStream));
//        try {
//            ColumnData columnData;
//            String line = configFileReader.readLine().trim();
//            ColumnData columnData2 = null;
//            while (line != null) {
//                line = line.trim();
//                if (line.startsWith(NEW_COLUMN)) {
//                    if (columnData2 != null) {
//                        this.columnDatas.add(columnData2);
//                        this.maxColItemCount = Math.max(this.maxColItemCount, columnData2.optionDatas.size() - 1);
//                    }
//                    columnData = new ColumnData(getColumnLabel(line));
//                } else {
////                    try {
//                        if (line.startsWith(SPACE) || line.isEmpty() || line.charAt(0) == ',') {
//                            columnData2.addOption(null);
//                            columnData = columnData2;
//                        } else {
//                            String[] vals = line.split(",");
//                            if (vals.length < 5) {
//                                Log.e(TAG, "Invalid option string: " + line);
//                                System.exit(0);
//                            }
//                            columnData2.addOption(new OptionData(Color.parseColor(vals[0]), vals[language.getIndex()], vals[3], vals[4]));
//                            columnData = columnData2;
//                        }
////                    } catch (IOException e3) {
////                        e22 = e3;
////                        columnData = columnData2;
////                    }
//                }
//                line = configFileReader.readLine();
//                columnData2 = columnData;
//            }
//            this.columnDatas.add(columnData2);
//            this.maxColItemCount = Math.max(this.maxColItemCount, columnData2.optionDatas.size() - 1);
//            columnData = columnData2;
//            return;
//        } catch (IOException e4) {
//            e22 = e4;
//        }
//        e22.printStackTrace();
//        Log.e(TAG, "Error reading config file");
//        System.exit(0);
//    }
//
//    private String getColumnLabel(String line) {
//        Log.i(TAG, "getColumnLabel line: " + line);
//        String label = line.substring(NEW_COLUMN.length());
//        int commaIndex = label.indexOf(",");
//        if (commaIndex >= 0) {
//            return label.substring(0, commaIndex);
//        }
//        Log.i(TAG, "getColumnLabel label: " + label);
//        return label;
//    }
}
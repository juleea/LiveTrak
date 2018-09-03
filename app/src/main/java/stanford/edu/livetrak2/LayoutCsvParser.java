package stanford.edu.livetrak2;

import android.util.Log;
import android.graphics.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LayoutCsvParser {
    private static final String NEW_COLUMN = "<COLUMN>";
    private static final String SPACE = "<SPACE>";
    private static String TAG = "LayoutData";
    private static final int CSV_COLUMN_COUNT = 4;

    public LayoutData parse(InputStream csvStream) throws IOException, IllegalArgumentException {
        ArrayList<ColumnData> columnDatas = new ArrayList();

        BufferedReader configFileReader = new BufferedReader(new InputStreamReader(csvStream));
        ArrayList<String> csvLines = new ArrayList();

        String line = configFileReader.readLine();
        ColumnData columnData = null;

        while(line != null) {
            line = line.trim();
            csvLines.add(line);
            if (line.startsWith(NEW_COLUMN)) {
                if (columnData != null) {
                    columnDatas.add(columnData);
                }
                columnData = new ColumnData(getColumnLabel(line));
            } else {
                if (line.startsWith(SPACE) || line.isEmpty() || line.charAt(0) == ',') {
                    columnData.addOption(null);
                } else {
                    String[] vals = line.split(",");
                    if(vals.length != CSV_COLUMN_COUNT) {
                        throw new IllegalArgumentException("Invalid CSV line:" + line);
                    }
                    OptionData newOptionData = generateOptionData(vals[0], vals[1], vals[2], vals[3]);
                    columnData.addOption(newOptionData);
                }
            }
            line = configFileReader.readLine();
        }
        columnDatas.add(columnData);

        int maxColItemCount = 0;
        for(ColumnData cd : columnDatas) {
            maxColItemCount = Math.max(maxColItemCount, cd.optionDatas.size() - 1); // TODO: why -1?
        }

        return new LayoutData(columnDatas, maxColItemCount);
    }

    private String getColumnLabel(String line) {
        Log.i(TAG, "getColumnLabel line: " + line);
        String label = line.substring(NEW_COLUMN.length());
        String result = "";
        int commaIndex = label.indexOf(",");
        if (commaIndex >= 0) {
            result = label.substring(0, commaIndex);
        }
        Log.i(TAG, "getColumnLabel label: " + result);

        return result;
    }

    private OptionData generateOptionData(String hexColor, String displayText, String logMsg, String group) {
        try {
            int color = Color.parseColor(hexColor);
            return new OptionData(color, displayText, logMsg, group);
        } catch (IllegalArgumentException e) {
            String errorMsg = new StringBuilder(String.valueOf(hexColor)).append(" is not a valid color").toString();
            Log.e(this.TAG, errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }
}

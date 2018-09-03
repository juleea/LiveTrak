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
}
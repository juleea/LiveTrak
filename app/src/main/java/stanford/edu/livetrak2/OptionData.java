package stanford.edu.livetrak2;

import android.graphics.Color;
import android.util.Log;

public class OptionData {
    String TAG = "OptionsData";
    int color;
    String group;
    String logMsg;
    String text;

    public OptionData(int color, String displayText, String logMsg, String group) {
        Log.i(TAG, String.format("Creating OptionData: color(%s), displayText(%s), logMsg(%s), group(%s)", color, displayText, logMsg, group));
        this.color = color;
        this.text = displayText;
        this.logMsg = logMsg;
        this.group = group;
    }
}

package stanford.edu.livetrak2;

import android.graphics.Color;
import android.util.Log;

public class OptionData {
    String TAG = "OptionsData";
    int color;
    String group;
    String logMsg;
    String text;

    public OptionData(String color, String displayText, String logMsg, String group) {
        this.group = group;
        try {
            this.color = Color.parseColor(color);
        } catch (IllegalArgumentException e) {
            Log.e(this.TAG, new StringBuilder(String.valueOf(color)).append(" is not a valid color").toString());
            System.exit(0);
        }
        this.text = displayText;
        this.logMsg = logMsg;
    }
}

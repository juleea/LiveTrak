package stanford.edu.livetrak2;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.RadioButton;
import android.widget.RadioGroup.LayoutParams;

public class RadioButtonX extends RadioButton {
    private static final float CHECKED_TRANSPARENCY = 0.7f;
    private static final int DEFAULT_TEXT_COLOR = -16777216;
    private static final int DEFAULT_TEXT_PADDING = 5;
    public static final float DEFAULT_TEXT_SIZE = 14.0f;
    private static final float UNCHECKED_TRANSPARENCY = 0.8f;
    private int color;
    private GradientDrawable drawable;
    private RadioButtonGroup group;
    private String logMsg = "";
    private String originalText = "";

    public RadioButtonX(Context context, OptionData data) {
        super(context);
        setAlpha(UNCHECKED_TRANSPARENCY);
        setPadding(5, 0, 5, 0);
        setTextSize(14.0f);
        setTextColor(-16777216);
        setButtonDrawable(new StateListDrawable());
        setLayoutParams(new LayoutParams(-1, 0, 0.0f));
        setGravity(17);
        if (data == null) {
            setAlpha(0.0f);
            setEnabled(false);
            return;
        }
        this.originalText = data.text;
        this.logMsg = data.logMsg;
        setText(data.text);
        this.drawable = new GradientDrawable();
        this.drawable.setShape(0);
        this.drawable.setStroke(2, -16777216);
        this.drawable.setColor(data.color);
        this.color = data.color;
        setBackgroundDrawable(this.drawable);
    }

    public void setChecked(boolean checked) {
        this.drawable = new GradientDrawable();
        this.drawable.setShape(0);
        this.drawable.setStroke(2, -16777216);
        this.drawable.setColor(this.color);
        if (checked) {
            this.drawable.setStroke(8, -16711936);
        }
        setBackgroundDrawable(this.drawable);
        super.setChecked(checked);
    }

    public void toggle() {
        if (!isChecked() && this.group != null) {
            super.toggle();
            this.group.check(this);
        }
    }

    public void setGroup(RadioButtonGroup group) {
        this.group = group;
    }

    public String toString() {
        return this.logMsg;
    }
}

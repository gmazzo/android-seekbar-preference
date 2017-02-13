package gs.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.FloatRange;
import android.support.v7.preference.SeekBarPreference;
import android.util.AttributeSet;

/**
 * A {@link SeekBarPreference} that stores its value in a percentual relation (between 0 and 1) of {@link #getMax()} and {@link #getMin()}
 */
public class PercentSeekBarPreference extends SeekBarPreference {

    public PercentSeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PercentSeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PercentSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentSeekBarPreference(Context context) {
        super(context);
    }

    private int adaptValue(float value) {
        return Math.round(value * getMax());
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? adaptValue(getPersistedFloat(getPercentValue()))
                : (Integer) defaultValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return adaptValue(a.getFloat(index, 0));
    }

    @Override
    protected boolean persistInt(int value) {
        return persistFloat(getPercentValue());
    }

    /**
     * Returns a <code>float</code> value calculated as <code>({@link #getValue()} - {@link #getMin()}) / ({@link #getMax()} - {@link #getMin()})) </code>
     *
     * @return a percentiual value (between 0 and 1)
     */
    @FloatRange(from = 0, to = 1)
    public float getPercentValue() {
        float max = getMax();
        float min = getMin();
        float value = getValue();

        return (value - min) / (max - min);
    }

    /**
     * Sets the percent value between {@link #getMax()} and {@link #getMin()}
     *
     * @param value
     */
    public void setPercentValue(@FloatRange(from = 0, to = 1) float value) {
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException("value must be between 0 and 1");
        }
        setValue(adaptValue(value));
    }

}

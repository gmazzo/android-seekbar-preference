package gs.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.SeekBarPreference;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A {@link SeekBarPreference} that stores its value in a percentual relation (between 0 and 1) of {@link #getMax()}
 */
public class PercentSeekBarPreference extends SeekBarPreference {
    private static final Field FIELD_MAX;
    private static final Field FIELD_VALUE;
    private static final Method METHOD_SET_PROGRESS;

    static {
        try {
            FIELD_MAX = SeekBarPreference.class.getDeclaredField("mMax");
            FIELD_MAX.setAccessible(true);
            FIELD_VALUE = SeekBarPreference.class.getDeclaredField("mProgress");
            FIELD_VALUE.setAccessible(true);
            METHOD_SET_PROGRESS = SeekBarPreference.class.getDeclaredMethod("setProgress", int.class, boolean.class);
            METHOD_SET_PROGRESS.setAccessible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
    @SuppressWarnings("ResourceType")
    protected Object onGetDefaultValue(TypedArray a, int index) {
        setMax(a.getInt(2, 100));
        return adaptValue(a.getFloat(index, 0));
    }

    @Override
    protected boolean persistInt(int value) {
        return persistFloat(getPercentValue());
    }

    /**
     * Returns a <code>float</code> value calculated as <code>{@link #getValue()} / {@link #getMax()}</code>
     *
     * @return a percentiual value (between 0 and 1)
     */
    @FloatRange(from = 0, to = 1)
    public float getPercentValue() {
        float max = getMax();
        float value = getValue();

        return value / max;
    }

    public int getMax() {
        try {
            return FIELD_MAX.getInt(this);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int getValue() {
        try {
            return FIELD_VALUE.getInt(this);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue(int value) {
        try {
            METHOD_SET_PROGRESS.invoke(this, value, true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the percent value between {@link #getMax()}
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

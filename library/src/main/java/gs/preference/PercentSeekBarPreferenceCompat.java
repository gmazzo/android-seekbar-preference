package gs.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.FloatRange;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.preference.SeekBarPreference;
import android.util.AttributeSet;
import android.widget.SeekBar;

import java.lang.reflect.Field;

import gs.preference.seekbar.R;

/**
 * A {@link SeekBarPreference} that stores its value in a percentual relation (between 0 and 1) of {@link #getMax()} and {@link #getMin()}
 */
public class PercentSeekBarPreferenceCompat extends SeekBarPreference {
    private boolean mSmooth;

    public PercentSeekBarPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarPreference, defStyleAttr, defStyleRes);
        try {
            mSmooth = a.getBoolean(R.styleable.SeekBarPreference_smooth, true);

        } finally {
            a.recycle();
        }
    }

    public PercentSeekBarPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    public PercentSeekBarPreferenceCompat(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, R.attr.seekBarPreferenceStyle, 0);
    }

    public PercentSeekBarPreferenceCompat(Context context) {
        super(context);

        init(context, null, R.attr.seekBarPreferenceStyle, 0);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder view) {
        super.onBindViewHolder(view);

        if (mSmooth) {
            try {
                Field field = SeekBar.class.getDeclaredField("mOnSeekBarChangeListener");
                field.setAccessible(true);

                SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekbar);
                SeekBar.OnSeekBarChangeListener listener = (SeekBar.OnSeekBarChangeListener) field.get(seekBar);
                seekBar.setOnSeekBarChangeListener(new SmoothOnSeekBarChangeWrapper(listener));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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
        setMax(a.getInt(android.support.v7.preference.R.styleable.SeekBarPreference_android_max, 100));
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
        float min = getMin();
        return (getValue() - min) / (getMax() - min);
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

class SmoothOnSeekBarChangeWrapper implements SeekBar.OnSeekBarChangeListener {
    private final SeekBar.OnSeekBarChangeListener wrapped;

    SmoothOnSeekBarChangeWrapper(SeekBar.OnSeekBarChangeListener wrapped) {
        this.wrapped = wrapped instanceof SmoothOnSeekBarChangeWrapper ?
                ((SmoothOnSeekBarChangeWrapper) wrapped).wrapped : wrapped;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        wrapped.onProgressChanged(seekBar, progress, fromUser);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Avoids the flag that keeps the value from updating
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        wrapped.onStopTrackingTouch(seekBar);
    }

}

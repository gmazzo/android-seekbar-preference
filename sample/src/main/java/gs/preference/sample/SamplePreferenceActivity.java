package gs.preference.sample;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class SamplePreferenceActivity extends PreferenceActivity implements android.preference.Preference.OnPreferenceClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

        addPreferencesFromResource(R.xml.preferences);

        findPreference(getString(R.string.preference_switch_key)).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        startActivity(new Intent(this, SampleFragmentActivity.class));
        finish();
        return true;
    }

}

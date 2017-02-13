package gs.preference.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

public class SampleFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sample);
    }

    public static class SampleFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            PreferenceManager.setDefaultValues(getContext(), R.xml.preferences_compat, true);

            addPreferencesFromResource(R.xml.preferences_compat);

            findPreference(getString(R.string.preference_switch_key)).setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            startActivity(new Intent(getContext(), SamplePreferenceActivity.class));
            getActivity().finish();
            return true;
        }

    }

}

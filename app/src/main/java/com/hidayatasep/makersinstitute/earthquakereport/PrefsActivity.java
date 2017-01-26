package com.hidayatasep.makersinstitute.earthquakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by fachrifebrian on 1/26/17.
 */

public class PrefsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        Preference minMagnitude = findPreference(getString(R.string.setting_min_magnitude_key));
        bindPreferenceSummaryToValue(minMagnitude);

    }

    //helper method to read the current value of the preference stored in the SharedPreferences on the device,
    // and display that in the preference summary (Setting Screen)
    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String preferenceString = sharedPreferences.getString(preference.getKey(), "");
        onPreferenceChange(preference, preferenceString);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String preferenceValue = value.toString();
        preference.setSummary(preferenceValue);
        return true;
    }
}

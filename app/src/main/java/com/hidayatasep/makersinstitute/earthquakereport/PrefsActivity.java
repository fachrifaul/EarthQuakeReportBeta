package com.hidayatasep.makersinstitute.earthquakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.Preference;
import android.preference.PreferenceManager;

/**
 * Created by hidayatasep43 on 1/26/2017.
 */

public class PrefsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set content to file prefs.xml in folder xml
        addPreferencesFromResource(R.xml.prefs);

        //Given the key of a preference, we can use PreferenceFragment's findPreference() method to get the Preference object
        Preference minMagnitude = findPreference(getString(R.string.setting_min_magnitude_key));
        bindPreferenceSummaryToValue(minMagnitude);
    }

    //helper method to read the current value of the preference stored in the SharedPreferences on the device,
    // and display that in the preference summary (Setting Screen)
    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        String preferenceString = sharedPreferences.getString(preference.getKey(), "");
        onPreferenceChange(preference,preferenceString);
    }

    //OnPreferenceChangeListener interface to get notified when a preference changes
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String preferenceValue = value.toString();
        preference.setSummary(preferenceValue);
        return true;
    }
}

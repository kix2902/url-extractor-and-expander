package com.redinput.share2browser;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity implements OnPreferenceChangeListener {

	private ListPreference prefTheme;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Utils.applySharedTheme(this);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs_screen);

		prefTheme = (ListPreference) findPreference("theme");
		prefTheme.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference.equals(prefTheme)) {
			Intent iRestart = new Intent(this, this.getClass());
			startActivity(iRestart);
			finish();

			return true;

		} else {
			return false;
		}
	}

}

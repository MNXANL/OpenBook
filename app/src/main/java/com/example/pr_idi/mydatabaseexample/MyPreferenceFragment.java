package com.example.pr_idi.mydatabaseexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class MyPreferenceFragment extends PreferenceFragment {
    int cercaTitol = 1;
    String ordre = "titol";
    boolean defBooks ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferences();
        addPreferencesFromResource(R.xml.settings);
        savePreferences();
    }
    private void getPreferences() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        cercaTitol = SP.getInt(String.valueOf(R.string.settingSearch), cercaTitol);
        ordre = SP.getString(String.valueOf(R.string.settingOrder), ordre);
        defBooks = SP.getBoolean(String.valueOf(R.string.DefBooks), true);
    }

    private void savePreferences() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editPref = SP.edit();
        editPref.putInt(getString(R.string.settingSearch), cercaTitol);
        editPref.putString(getString(R.string.settingOrder), ordre);
        editPref.putBoolean(getString(R.string.DefBooks), defBooks);
        editPref.apply();
    }
}

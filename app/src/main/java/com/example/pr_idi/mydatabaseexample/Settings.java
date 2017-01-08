package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }
    @Override
    public void onBuildHeaders(List<Header> target)    {
        loadHeadersFromResource(R.xml.headers_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName)    {
        return MyPreferenceFragment.class.getName().equals(fragmentName);
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }

}

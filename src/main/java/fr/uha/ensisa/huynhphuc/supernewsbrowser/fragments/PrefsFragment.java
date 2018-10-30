package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import java.text.ParseException;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Settings;

public class PrefsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private PrefsFragmentListener mListener;
    private SharedPreferences sharedPreferences;

    public static Fragment newInstance() {
        PrefsFragment prefsFragment = new PrefsFragment();
        return prefsFragment;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("language_list") || key.equals("pageSize_list") || key.equals("sortBy_list")) {
            Preference listPref = findPreference(key);
            String choice = sharedPreferences.getString(key, "");
            listPref.setSummary(choice);
            Log.d("PrefsFragment", key + " = " + choice);
        }

        if (key.equals("pref_select_from_date")) {
            Preference fromPref = findPreference(key);
            String from = mListener.getSettings().getFrom();
            fromPref.setSummary(from);
        }

        if (key.equals("pref_select_to_date")) {
            Preference toPref = findPreference(key);
            String to = mListener.getSettings().getFrom();
            toPref.setSummary(to);
        }

    }

    public interface PrefsFragmentListener {
        void requestDatePickerDialog(String ID) throws ParseException;

        Settings getSettings();

        void setDefaultSettings();
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        //Load preferences xml file
        setPreferencesFromResource(R.xml.preferences, s);

        Preference from_date_preference = (Preference) findPreference("pref_select_from_date");
        Preference to_date_preference = (Preference) findPreference("pref_select_to_date");

        from_date_preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    mListener.requestDatePickerDialog("from");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        to_date_preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    mListener.requestDatePickerDialog("to");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PrefsFragment.PrefsFragmentListener) {
            mListener = (PrefsFragment.PrefsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PrefsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}

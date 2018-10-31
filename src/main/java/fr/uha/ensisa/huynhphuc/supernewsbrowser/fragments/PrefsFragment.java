package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.DatePicker;

import java.text.ParseException;
import java.util.Calendar;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Settings;

public class PrefsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        DatePickerDialog.OnDateSetListener {

    private PrefsFragmentListener mListener;
    private SharedPreferences sharedPreferences;
    private String from;
    private String to;


    public interface PrefsFragmentListener {
        Settings getSettings();

        void setDefaultSettings();
    }

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
            //set summary
        }

        if (key.equals("pref_select_to_date")) {
            Preference toPref = findPreference(key);
            //set summary
        }

    }

    private void showDateDialog(String ID) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (ID.equals("from")) {
            this.from = year + "-" + (month + 1) + "-" + day;
        } else {
            this.to = year + "-" + (month + 1) + "-" + day;
        }

        new DatePickerDialog(this.getContext(), this, year, month, day).show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

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

        Preference fromPref = (Preference) findPreference("pref_select_from_date");
        Preference toPref = (Preference) findPreference("pref_select_to_date");

        fromPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDateDialog("from");
                return false;
            }
        });

        toPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDateDialog("to");
                return false;
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

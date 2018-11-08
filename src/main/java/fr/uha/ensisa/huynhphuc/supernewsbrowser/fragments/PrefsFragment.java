package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Settings;

public class PrefsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private PrefsFragmentListener mListener;
    private SharedPreferences sharedPreferences;
    private DatePickerFragment datepicker_from;
    private DatePickerFragment datepicker_to;

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

        Preference listPref = findPreference(key);
        Preference datePref = findPreference("pref_date_choice");
        Preference sortBy = findPreference("sortBy_list");

        if (key.equals("language_list") || key.equals("pageSize_list")) {
            String choice = sharedPreferences.getString(key, "");
            listPref.setSummary(choice);
            Log.d("PrefsFragment", key + " = " + choice);
        }

        if(key.equals("sortBy_list")){
            String sort = sharedPreferences.getString("sortBy_list","");
            sortBy.setSummary(sort);
            if(sort.equals("Date")){
               datePref.setVisible(true);
            } else {
                datePref.setVisible(false);
            }
        }
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initSummaries();
    }

    private void initSummaries() {

        Settings settings = mListener.getSettings();

        Preference languagePref = (Preference) findPreference("language_list");
        Preference pageSizePref = (Preference) findPreference("pageSize_list");
        Preference sortByPref = (Preference) findPreference("sortBy_list");
        Preference fromPref = (Preference) findPreference("pref_select_from_date");
        Preference toPref = (Preference) findPreference("pref_select_to_date");

        languagePref.setSummary(sharedPreferences.getString("language_list", settings.getLanguage()));
        pageSizePref.setSummary(sharedPreferences.getString("pageSize_list", settings.getPageSize()));
        sortByPref.setSummary(sharedPreferences.getString("sortBy_list", settings.getSortBy()));
        fromPref.setSummary(settings.getFrom());
        toPref.setSummary(settings.getTo());

        Preference datePref = findPreference("pref_date_choice");
        String sort = sharedPreferences.getString("sortBy_list","Date");

        if(sort.equals("Date")){
            datePref.setVisible(true);
        } else {
            datePref.setVisible(false);
        }


    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        //Load preferences xml file
        setPreferencesFromResource(R.xml.preferences, s);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        final Settings settings = mListener.getSettings();

        Preference fromPref = (Preference) findPreference("pref_select_from_date");
        Preference toPref = (Preference) findPreference("pref_select_to_date");

        try {
            datepicker_from = new DatePickerFragment("from",
                    sharedPreferences.getString("from", settings.getFrom()));

            datepicker_to = new DatePickerFragment("to",
                    sharedPreferences.getString("to", settings.getTo()));


        } catch (ParseException e) {
            e.printStackTrace();
        }


        fromPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    datepicker_from = new DatePickerFragment("from",
                            sharedPreferences.getString("from", settings.getFrom()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datepicker_from.show(getFragmentManager(), "datepicker");
                return false;
            }
        });

        toPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    datepicker_to = new DatePickerFragment("to",
                            sharedPreferences.getString("to", settings.getTo()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datepicker_to.show(getFragmentManager(), "datepicker");
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

package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;

public class PrefsFragment extends PreferenceFragmentCompat {

    private PrefsFragmentListener mListener;

    public static Fragment newInstance() {
        PrefsFragment prefsFragment = new PrefsFragment();
        return prefsFragment;
    }

    public interface PrefsFragmentListener{

    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        setPreferencesFromResource(R.xml.preferences, s);

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

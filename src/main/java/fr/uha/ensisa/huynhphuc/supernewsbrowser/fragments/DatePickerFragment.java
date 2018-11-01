package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Settings;

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private String date;
    private String id;
    private int year;
    private int month;
    private int day;

    private DatePickerFragmentListener mListener;

    public interface DatePickerFragmentListener {
        Settings getSettings();

        void update(String ID, String value);
    }

    public DatePickerFragment(String id, String date) throws ParseException {
        this.id = id;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = sdf.parse(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertedDate);

        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.date = year + "-" + (month + 1) + "-" + day;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar current = Calendar.getInstance();
        int current_year = current.get(Calendar.YEAR);
        int current_day = current.get(Calendar.DAY_OF_MONTH);
        int current_month = current.get(Calendar.MONTH);
        //Current date
        String to = current_year + "-" + (current_month + 1) + "-" + current_day;

        Calendar before = current;
        before.add(Calendar.MONTH, -1); // 1 month before today
        int before_year = before.get(Calendar.YEAR);
        int before_day = before.get(Calendar.DAY_OF_MONTH);
        int before_month = before.get(Calendar.MONTH);

        String from = before_year + "-" + (before_month + 1) + "-" + before_day;

        //Set min date because the free version of NewsApi allows only a gap of one month between the two dates
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, this.year, this.month, this.day);
        dialog.getDatePicker().setMinDate(before.getTimeInMillis());

        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.date = year + "-" + (month + 1) + "-" + day;

        if (this.id == "from") {
            mListener.update("from", this.getDate());
            ((PrefsFragment) this.getFragmentManager()
                    .findFragmentById(R.id.fragment_container))
                    .findPreference("pref_select_from_date")
                    .setSummary(this.date);
        } else {
            mListener.update("to", this.getDate());
            ((PrefsFragment) this.getFragmentManager()
                    .findFragmentById(R.id.fragment_container))
                    .findPreference("pref_select_to_date")
                    .setSummary(this.date);
        }
    }

    public String getDate() {
        return date;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DatePickerFragmentListener) {
            mListener = (DatePickerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DatePickerFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

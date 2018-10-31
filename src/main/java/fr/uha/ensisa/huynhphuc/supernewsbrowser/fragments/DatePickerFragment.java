package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment {

    private String date;
    private String id; // from or to
    private int year;
    private int month;
    private int day;

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
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), null, this.year, this.month, this.day);
        dialog.getDatePicker().setMinDate(before.getTimeInMillis());

        return dialog;
    }


    /*public void onDateSet(DatePicker view, int year, int month, int day) {
        this.date = year + "-" + (month + 1) + "-" + day;

        if (this.id.equals("from")) {
            mListener.updateFrom(this.getDate());
            ((TextView) ((SettingsFragment) this.getFragmentManager()
                    .findFragmentById(R.id.fragment_container))
                    .getView()
                    .findViewById(R.id.from_date_choosen))
                    .setText(this.getDate());
        } else {
            mListener.updateTo(this.getDate());
            ((TextView) ((SettingsFragment) this.getFragmentManager()
                    .findFragmentById(R.id.fragment_container))
                    .getView()
                    .findViewById(R.id.to_date_choosen))
                    .setText(this.getDate());
        }
    }*/

    public String getDate() {
        return date;
    }

    /*@Override
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
    }*/
}

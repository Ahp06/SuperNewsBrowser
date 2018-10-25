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

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private String date;
    private String id; // from or to
    private int year;
    private int month;
    private int day;
    private String from;
    private String to;

    private DatePickerFragmentListener mListener;

    public interface DatePickerFragmentListener {
        void requestFromChange(String from);
        void requestToChange(String to);
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
        this.date = year + "-" + (month+1) + "-" + day;
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

    /**
     * Set the date data member of this class according to news API date format.
     * The month value is incremented because in the Android SDK, months are indexed starting at 0.
     * @param view
     * @param year
     * @param month
     * @param day
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.date = year + "-" + (month+1) + "-" + day ;

        if(this.id.equals("from")){
            //from_date_choosen.setText("" + this.getDate());
            from = "" + this.getDate();
            mListener.requestFromChange(from);
        } else {
            //to_date_choosen.setText("" + this.getDate());
            to = "" + this.getDate();
            mListener.requestToChange(to);
        }

    }

    public String getDate() {
        return date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.HomeFragmentListener) {
            mListener = (DatePickerFragment.DatePickerFragmentListener) context;
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

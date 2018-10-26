package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Settings;

public class SettingsFragment extends Fragment {

    private SettingsFragmentListener mListener;

    public interface SettingsFragmentListener {
        Settings getSettings();
        void updateSettings(Settings settings);
        void requestHome();
        void requestDatePickerDialog(String ID) throws ParseException;
    }

    public static Fragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public class ViewHolder {
        public final View mView;
        public final Spinner language_spinner;
        public final Spinner pageSize_spinner;
        public final Spinner sortBy_spinner;
        public final TextView from_date_choosen;
        public final TextView to_date_choosen;
        public final Button valid_button;
        public final Button date_picker_from;
        public final Button date_picker_to;

        public ViewHolder(View view) {
            language_spinner = (Spinner) view.findViewById(R.id.language_spinner);
            pageSize_spinner = (Spinner) view.findViewById(R.id.pageSize_spinner);
            sortBy_spinner = (Spinner) view.findViewById(R.id.sortBy_spinner);
            from_date_choosen = (TextView) view.findViewById(R.id.from_date_choosen);
            to_date_choosen = (TextView) view.findViewById(R.id.to_date_choosen);
            date_picker_from = (Button) view.findViewById(R.id.date_picker_from);
            date_picker_to = (Button) view.findViewById(R.id.date_picker_to);
            valid_button = (Button) view.findViewById(R.id.valid_button);
            mView = view;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        final Settings settings = mListener.getSettings();
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.from_date_choosen.setText(settings.getFrom());
        viewHolder.to_date_choosen.setText(settings.getTo());

        ArrayAdapter<CharSequence> language_adapter = ArrayAdapter.createFromResource(container.getContext(),
                R.array.language_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> pageSize_adapter = ArrayAdapter.createFromResource(container.getContext(),
                R.array.pageSize_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> sortBy_adapter = ArrayAdapter.createFromResource(container.getContext(),
                R.array.sortBy_array, android.R.layout.simple_spinner_item);


        language_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSize_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBy_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        viewHolder.language_spinner.setAdapter(language_adapter);
        viewHolder.pageSize_spinner.setAdapter(pageSize_adapter);
        viewHolder.sortBy_spinner.setAdapter(sortBy_adapter);

        viewHolder.language_spinner.setSelection(getIndex(viewHolder.language_spinner, settings.getLanguage()));
        viewHolder.pageSize_spinner.setSelection(getIndex(viewHolder.pageSize_spinner, settings.getPageSize()));
        viewHolder.sortBy_spinner.setSelection(getIndex(viewHolder.sortBy_spinner, settings.getSortBy()));

        viewHolder.date_picker_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mListener.requestDatePickerDialog("from");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.date_picker_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mListener.requestDatePickerDialog("to");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.valid_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = viewHolder.language_spinner.getSelectedItem().toString();
                String pageSize = viewHolder.pageSize_spinner.getSelectedItem().toString();
                String sortBy = viewHolder.sortBy_spinner.getSelectedItem().toString();

                String from = settings.getFrom();
                String to = settings.getTo();

                settings.setLanguage(language);
                settings.setPageSize(pageSize);
                settings.setSortBy(sortBy);
                if (!from.equals("")) settings.setFrom(from);
                if (!to.equals("")) settings.setTo(to);

                try {
                    if (greater(from, to) && oneMonthTimeSlot(from, to)) {
                        mListener.updateSettings(settings);
                        mListener.requestHome();
                    } else {
                        Toast.makeText(container.getContext(), R.string.incorrect_date_choice, Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


        return view;
    }

    public boolean greater(String from, String to) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date from_date = sdf.parse(from);
        Date to_date = sdf.parse(to);

        int greater = to_date.compareTo(from_date);
        // ok = 1 -> to_date > from_date
        // ok = 0 -> to_date = from_date
        // ok = -1 -> to_date < from_date

        return greater == 1;
    }

    public boolean oneMonthTimeSlot(String from, String to) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date from_date = sdf.parse(from);
        Date to_date = sdf.parse(to);

        long diffInMillies = Math.abs(to_date.getTime() - from_date.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        return diff <= 31;

    }

    /**
     * Return the specific index of an element in a spinner
     *
     * @param spinner
     * @param myString
     * @return
     */
    public int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsFragment.SettingsFragmentListener) {
            mListener = (SettingsFragment.SettingsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SettingsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}

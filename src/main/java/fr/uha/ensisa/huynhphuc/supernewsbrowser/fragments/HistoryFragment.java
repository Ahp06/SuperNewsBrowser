package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;

public class HistoryFragment extends Fragment {

    private HistoryFragmentListener mListener;
    private ListView lv_history;
    private ArrayAdapter<String> adapter;
    private View view;

    public interface HistoryFragmentListener {
        ArrayList<String> getHistory();
        void clearHistory();
    }

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_history, container, false);

        ArrayList<String> history = mListener.getHistory();

        this.lv_history = (ListView) view.findViewById(R.id.history);

        if (!history.isEmpty()) {
            this.adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, history);
            lv_history.setAdapter(adapter);
        } else {
            TextView emptyText = (TextView) view.findViewById(R.id.empty_history);
            lv_history.setEmptyView(emptyText);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistoryFragmentListener) {
            mListener = (HistoryFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HistoryFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_history) {
            mListener.clearHistory();
            TextView emptyText = (TextView) view.findViewById(R.id.empty_history);
            lv_history.setEmptyView(emptyText);
        }

        return super.onOptionsItemSelected(item);
    }

}

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
import java.util.List;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.History;

public class HistoryFragment extends Fragment {

    private HistoryFragmentListener mListener;
    private ListView lv_history;
    private TextView emptyText;
    private ArrayAdapter<String> adapter;
    private View view;

    public interface HistoryFragmentListener {
        List<History> getHistory();
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
        setHasOptionsMenu(true);

        List<History> history = mListener.getHistory();
        List<String> queries = new ArrayList<String>();
        for(int i = 0; i < history.size() ; i ++){
            queries.add(history.get(i).getQuery());
        }

        this.lv_history = (ListView) view.findViewById(R.id.history);
        this.emptyText = (TextView) view.findViewById(R.id.empty_history);

        if (!queries.isEmpty()) {
            this.adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, queries);
            lv_history.setAdapter(adapter);
            lv_history.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        } else {
            lv_history.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
            //lv_history.setEmptyView(emptyText);
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
            lv_history.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
            mListener.clearHistory();
        }

        return super.onOptionsItemSelected(item);
    }

}

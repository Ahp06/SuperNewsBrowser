package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Article;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Settings;


public class HomeFragment extends Fragment {

    private View root;
    private EditText query;
    private HomeFragmentListener mListener;
    private boolean backPressed = false;

    public class ArticleHttpRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpsURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();

                if (result != null) {
                    String response = read(urlConnection.getInputStream());
                    this.parseJSON(response);
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(String msg) {
            final EditText editText = (EditText) root.findViewById(R.id.query);
            mListener.requestArticleList();
            editText.getText().clear();
        }

        /***
         * Read the URL stream and return all articles into a String (in JSON format)
         * @param stream
         * @return
         * @throws IOException
         */
        public String read(InputStream stream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            String line;
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            // Close stream
            if (null != stream) {
                stream.close();
            }
            return result;
        }

        /***
         * Parse the string result in JSON format and create a list of article object
         * @param result
         */
        public void parseJSON(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONArray articles = response.optJSONArray("articles");

                ArrayList<Article> articlesList = new ArrayList<Article>();
                for (int i = 0; i < articles.length(); i++) {
                    Article article = new Article(articles.optJSONObject(i));
                    articlesList.add(article);
                }

                mListener.setArticleList(articlesList);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface HomeFragmentListener {
        Settings getSettings();

        void requestSettings();

        void setArticleList(ArrayList<Article> articles);

        void requestArticleList();

        void requestSavedList();

        void requestHistory();

        void addIntoHistory(String query);
    }

    public HomeFragment() {
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    /***
     *
     * Execute the query with all settings by launching an ArticleHttpRequest AsyncTask
     */
    public void executeQueryWithSettings() {
        if (!this.query.getText().toString().equals("")) {
            String query = this.query.getText().toString();
            Log.d("query", "=" + query);
            mListener.addIntoHistory(query);
            new ArticleHttpRequest().execute(mListener.getSettings().applySettings(query));
        } else {
            Toast.makeText(this.getContext(), R.string.empty_query, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        /*root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (backPressed) {
                        getFragmentManager().popBackStack();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(v.getContext(), R.string.exit_application, Toast.LENGTH_SHORT).show();
                        backPressed = true;
                    }
                }
                return true;
            }
        });*/

        //execute the search if the user presses confirm with the keyboard
        this.query = root.findViewById(R.id.query);
        query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    executeQueryWithSettings();
                    handled = true;
                }
                return handled;
            }
        });

        //Or if he clicks on the validate button
        Button btn = (Button) root.findViewById(R.id.queryButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeQueryWithSettings();
            }
        });


        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.saved_item) {
            mListener.requestSavedList();
        }

        if (item.getItemId() == R.id.settings_item) {
            mListener.requestSettings();
        }

        if (item.getItemId() == R.id.history_item) {
            mListener.requestHistory();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentListener) {
            mListener = (HomeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HomeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}

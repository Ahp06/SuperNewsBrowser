package fr.uha.ensisa.huynhphuc.supernewsbrowser;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.ArticleListFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.CommentFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.DatePickerFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.HistoryFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.HomeFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.PrefsFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.SavedListFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Article;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.ArticleDao;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.DaoSession;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.History;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.HistoryDao;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Settings;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.HomeFragmentListener,
        ArticleListFragment.ArticleListFragmentListener,
        SavedListFragment.SavedFragmentListener,
        CommentFragment.CommentFragmentListener,
        HistoryFragment.HistoryFragmentListener,
        PrefsFragment.PrefsFragmentListener,
        DatePickerFragment.DatePickerFragmentListener {

    //Temporary data
    private ArrayList<Article> articleList;
    private ArrayList<Article> toDelete;

    //DAOs
    private ArticleDao savedArticleDao;
    private HistoryDao historyDao;

    //Settings
    private SharedPreferences sharedPreferences;
    private String from;
    private String to;

    //Constants
    public static final int COMMENT_FRAGMENT = 0;
    public static final int SAVED_FRAGMENT = 1;
    public static final int LIST_FRAGMENT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get all DAOs 
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        savedArticleDao = daoSession.getArticleDao();
        historyDao = daoSession.getHistoryDao();

        if (this.articleList == null) this.articleList = new ArrayList<Article>();
        if (this.toDelete == null) this.toDelete = new ArrayList<Article>();

        //Loading the first fragment
        this.replaceFragment(HomeFragment.newInstance());
    }

    /**
     * Replace the fragment in the fragment container of the MainActivity
     *
     * @param someFragment
     */
    public void replaceFragment(Fragment someFragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, someFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Return true if all fields of the two articles are the same
     *
     * @param a1
     * @param a2
     * @return
     */
    public boolean compareArticles(Article a1, Article a2) {
        boolean condition =
                a1.getTitle().equals(a2.getTitle())
                        && a1.getAuthor().equals(a2.getAuthor())
                        && a1.getDescription().equals(a2.getDescription())
                        && a1.getUrl().equals(a2.getUrl())
                        && a1.getUrlToImage().equals(a2.getUrlToImage());

        return condition;
    }

    /**
     * Return the copy of the article saved in the saved list
     *
     * @param article
     * @return
     */
    public Article getCopySavedOf(Article article) {
        List<Article> queryCopy = savedArticleDao
                .queryBuilder()
                .where(ArticleDao.Properties.Title.eq(article.getTitle()),
                        ArticleDao.Properties.UrlToImage.eq(article.getUrlToImage()),
                        ArticleDao.Properties.PublishedAt.eq(article.getPublishedAt()),
                        ArticleDao.Properties.Url.eq(article.getUrl()),
                        ArticleDao.Properties.Author.eq(article.getAuthor()))
                .list();

        if (queryCopy.size() == 1) return queryCopy.get(0);

        return null;
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) {
            manager.popBackStack();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exit_title_message)
                    .setMessage(R.string.exit_message)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                            moveTaskToBack(true);
                        }
                    }).create().show();
        }
    }

    //Implementation of all fragment listener interfaces

    /**
     * Return the article list resulting of the user query
     *
     * @return
     */
    @Override
    public List<Article> getArticleList() {
        return articleList;
    }

    /**
     * Save an article in the DB
     *
     * @param article
     */
    @Override
    public void requestSaveArticle(Article article) {
        this.savedArticleDao.insert(article);
    }

    /**
     * Cancel the save of an article in the fragment article list,
     * delete the saved article in the DB
     *
     * @param article
     */
    @Override
    public void requestCancelSave(Article article) {
        Long ID = this.getCopySavedOf(article).getId();
        this.savedArticleDao.deleteByKey(ID);
    }

    /**
     * Switch to CommentFragment
     *
     * @param article
     */
    @Override
    public void requestComment(Article article) {
        CommentFragment commentFragment = CommentFragment.newInstance();
        Bundle args = new Bundle();

        args.putParcelable("article", getCopySavedOf(article));
        commentFragment.setArguments(args);
        this.replaceFragment(commentFragment);
    }

    /**
     * Delete all articles saved in DB who are in the " To Delete " list
     */
    @Override
    public void deleteArticlesToDelete() {
        for (Article article : toDelete) {
            this.savedArticleDao
                    .queryBuilder()
                    .where(ArticleDao.Properties.Id.eq(article.getId()))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            Log.d("DaoExample", "Deleted an article, ID: " + article.getId());
        }
        this.toDelete.clear();
    }

    @Override
    public void clearSaved() {
        Log.d("saved list", "=" + this.savedArticleDao.loadAll());
        this.toDelete.addAll(this.savedArticleDao.loadAll());
    }

    /**
     * Return the settings of the application
     *
     * @return
     */
    @Override
    public Settings getSettings() {

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Settings settings = new Settings();

        //this.getSettings().setTopNewsMode(sharedPreferences.getBoolean("mode",false));
        String language = sharedPreferences.getString("language_list", settings.getLanguage());
        String pageSize = sharedPreferences.getString("pageSize_list", settings.getPageSize());
        String sortBy = sharedPreferences.getString("sortBy_list", settings.getSortBy());

        settings.setLanguage(language);
        settings.setPageSize(pageSize);
        settings.setSortBy(sortBy);
        if (from != null) settings.setFrom(this.from);
        if (to != null) settings.setTo(this.to);

        return settings;
    }

    @Override
    public void update(String ID, String value) {
        if (ID == "from") {
            this.from = value;
        } else {
            this.to = value;
        }
        Log.d("DatePicker", "ID = " + ID + ", value = " + value);
    }

    @Override
    public void setDefaultSettings() {
        Settings default_settings = new Settings();
        Settings settings = this.getSettings();

        settings.setTo(default_settings.getTo());
        settings.setFrom(default_settings.getFrom());
        settings.setLanguage(default_settings.getLanguage());
        settings.setPageSize(default_settings.getPageSize());
        settings.setSortBy(default_settings.getSortBy());
    }

    /**
     * Switch to SettingsFragment
     */
    @Override
    public void requestSettings() {
        this.replaceFragment(PrefsFragment.newInstance());
    }

    /**
     * Set the article list resulting of the user query
     *
     * @param articles
     */
    @Override
    public void setArticleList(ArrayList<Article> articles) {
        this.articleList = articles;
    }

    /**
     * Switch to ArticleListFragment
     */
    @Override
    public void requestArticleList() {
        this.replaceFragment(ArticleListFragment.newInstance());
    }

    /**
     * Switch to SavedListFragment
     */
    @Override
    public void requestSavedList() {
        this.replaceFragment(SavedListFragment.newInstance());
    }

    /**
     * Switch to HistoryFragment
     */
    @Override
    public void requestHistory() {
        this.replaceFragment(HistoryFragment.newInstance());
    }

    /**
     * Put a query into the History table into DB
     *
     * @param query
     */
    @Override
    public void addIntoHistory(String query) {
        this.historyDao.insert(new History(query));
    }

    /**
     * If saved list isn't empty return all the saved articles list,
     * or an empty arraylist if empty
     *
     * @return
     */
    @Override
    public List<Article> getSavedList() {
        return this.savedArticleDao.count() == 0 ? new ArrayList<Article>() : this.savedArticleDao.loadAll();
    }

    /**
     * Return true if the article is saved in the DB and not into the "ToDelete" list,
     * else it will return false
     *
     * @param article
     * @param fragment
     * @return
     */
    @Override
    public boolean isSaved(Article article, int fragment) {
        boolean isSaved = false;
        List<Article> articles = this.getSavedList();

        for (int i = 0; i < articles.size(); i++) {
            if (compareArticles(article, articles.get(i))) {
                isSaved = true;
            }
        }

        boolean inToDeleteList = false;
        if (fragment == MainActivity.SAVED_FRAGMENT) {
            for (Article a : toDelete) {
                if (compareArticles(article, a)) {
                    inToDeleteList = true;
                }
            }
        }

        //The article is in the saved list and isn't into toDelete list
        return fragment == MainActivity.SAVED_FRAGMENT ? (isSaved && !inToDeleteList) : isSaved;
    }

    /**
     * Add an article in the toDelete list
     *
     * @param article
     */
    @Override
    public void addToDelete(Article article) {
        this.toDelete.add(article);
    }

    /**
     * Remove an article from the toDelete list
     *
     * @param article
     */
    @Override
    public void removeToDelete(Article article) {
        this.toDelete.remove(article);
    }

    /**
     * Open the website corresponding to the URL field of the article
     *
     * @param article
     */
    @Override
    public void requestWebsite(Article article) {
        String url = article.getUrl();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        Toast.makeText(getApplicationContext(), R.string.loading_text, Toast.LENGTH_LONG).show();
    }

    /**
     * Return the saved copy instance in DB of an article
     *
     * @param article
     * @return
     */
    @Override
    public Article getCopyInSaved(Article article) {
        return this.getCopySavedOf(article);
    }

    /**
     * If history isn't empty return the history list,
     * else it will return an empty arraylist
     *
     * @return
     */
    @Override
    public List<History> getHistory() {
        return historyDao.count() == 0 ? new ArrayList<History>() : historyDao.loadAll();
    }

    /**
     * Remove all queries into history DB
     */
    @Override
    public void clearHistory() {
        this.historyDao.deleteAll();
    }
}

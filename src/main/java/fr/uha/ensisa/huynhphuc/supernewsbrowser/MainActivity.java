package fr.uha.ensisa.huynhphuc.supernewsbrowser;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.SavedListFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.SettingsFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Article;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.ArticleDao;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.DaoSession;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Settings;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.SettingsDao;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.HomeFragmentListener,
        ArticleListFragment.ArticleListFragmentListener,
        SavedListFragment.SavedFragmentListener,
        SettingsFragment.SettingsFragmentListener,
        DatePickerFragment.DatePickerFragmentListener,
        CommentFragment.CommentFragmentListener,
        HistoryFragment.HistoryFragmentListener {

    private ArrayList<Article> articleList;
    private ArrayList<Article> toDelete;
    private ArrayList<String> history;
    private Settings settings;

    private ArticleDao savedArticleDao;
    private SettingsDao settingsDAO;

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
        settingsDAO = daoSession.getSettingsDao();

        savedArticleDao.deleteAll();
        settingsDAO.deleteAll();


        if (this.articleList == null) this.articleList = new ArrayList<Article>();
        if (this.settings == null) this.settings = new Settings();
        if (this.toDelete == null) this.toDelete = new ArrayList<Article>();
        if (this.history == null) this.history = new ArrayList<String>();

        this.replaceFragment(HomeFragment.newInstance());
    }

    public void replaceFragment(Fragment someFragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, someFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public boolean compareArticles(Article a1, Article a2) {
        boolean condition =
                a1.getTitle().equals(a2.getTitle())
                        && a1.getAuthor().equals(a2.getAuthor())
                        && a1.getDescription().equals(a2.getDescription())
                        && a1.getUrl().equals(a2.getUrl())
                        && a1.getUrlToImage().equals(a2.getUrlToImage());

        return condition;
    }

    public Article getArticleSaved(Article article) {
        Article savedArticle = savedArticleDao
                .queryBuilder()
                .where(ArticleDao.Properties.Title.eq(article.getTitle()),
                        ArticleDao.Properties.Id.eq(article.getId()),
                        ArticleDao.Properties.Id.eq(article.getId()),
                        ArticleDao.Properties.Id.eq(article.getId()),
                        ArticleDao.Properties.Id.eq(article.getId()))
                .list().get(0);

        return savedArticle;
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1 ) {
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

    @Override
    public List<Article> getArticleList() {
        return articleList;
    }

    @Override
    public void requestSaveArticle(Article article) {
        this.savedArticleDao.insert(article);
        Log.d("DaoExample", "Inserted new article, ID: " + article.getId());
    }

    @Override
    public void requestCancelSave(Article article) {
        Long ID = this.getArticleSaved(article).getId();
        this.savedArticleDao.deleteByKey(ID);
        Log.d("DaoExample", "Deleted an article, ID: " + article.getId());
    }

    @Override
    public void requestComment(Article article) {
        CommentFragment commentFragment = CommentFragment.newInstance();
        Bundle args = new Bundle();

        args.putParcelable("article", getArticleSaved(article));
        commentFragment.setArguments(args);
        this.replaceFragment(commentFragment);
    }

    @Override
    public void deleteArticlesToDelete() {
        for(Article article : toDelete){
            this.savedArticleDao
                    .queryBuilder()
                    .where(ArticleDao.Properties.Id.eq(article.getId()))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            Log.d("DaoExample", "Deleted an article, ID: " + article.getId());
        }
        this.toDelete.clear();
    }

    @Override
    public Settings getSettings() {
        return this.settings;
    }

    @Override
    public void updateSettings(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void requestHome() {
        this.replaceFragment(HomeFragment.newInstance());
    }

    @Override
    public void requestDatePickerDialog(String ID) throws ParseException {
        DatePickerFragment datePickerFragment;
        if (ID == "from") {
            datePickerFragment = new DatePickerFragment(ID, this.settings.getFrom());
        } else {
            datePickerFragment = new DatePickerFragment(ID, this.settings.getTo());
        }

        datePickerFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void requestSettings() {
        this.replaceFragment(SettingsFragment.newInstance());
    }

    @Override
    public void setArticleList(ArrayList<Article> articles) {
        this.articleList = articles;
    }

    @Override
    public void requestArticleList() {
        this.replaceFragment(ArticleListFragment.newInstance());
    }

    @Override
    public void requestSavedList() {
        this.replaceFragment(SavedListFragment.newInstance());
    }

    @Override
    public void requestHistory() {
        this.replaceFragment(HistoryFragment.newInstance());
    }

    @Override
    public void addIntoHistory(String query) {
        this.history.add(query);
    }

    @Override
    public List<Article> getSavedList() {
        return this.savedArticleDao.count() == 0 ? new ArrayList<Article>() : this.savedArticleDao.loadAll();
    }

    @Override
    public void updateFrom(String from) {
        this.settings.setFrom(from);
    }

    @Override
    public void updateTo(String to) {
        this.settings.setTo(to);
    }

    @Override
    public boolean isSaved(Article article, int fragment) {
        boolean isSaved = false;
        List<Article> articles = this.getSavedList();

        for (int i = 0 ; i < articles.size() ; i ++) {
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

    @Override
    public void addToDelete(Article article) {
        this.toDelete.add(article);
    }

    @Override
    public void removeToDelete(Article article) {
        this.toDelete.remove(article);
    }

    @Override
    public void requestWebsite(Article article) {
        String url = article.getUrl();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        Toast.makeText(getApplicationContext(), R.string.loading_text, Toast.LENGTH_LONG).show();
    }

    @Override
    public ArrayList<String> getHistory() {
        return this.history;
    }

    @Override
    public void clearHistory() {
        this.history.clear();
    }
}

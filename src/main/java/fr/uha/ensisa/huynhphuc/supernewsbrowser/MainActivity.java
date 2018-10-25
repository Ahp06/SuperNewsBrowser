package fr.uha.ensisa.huynhphuc.supernewsbrowser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.ArticleListFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.HomeFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.SavedListFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Article;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Settings;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.HomeFragmentListener, ArticleListFragment.ArticleListFragmentListener, SavedListFragment.SavedFragmentListener {

    private EditText query;
    private ArrayList<Article> articleList;
    private ArrayList<Article> savedArticles;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.articleList = new ArrayList<Article>();
        this.savedArticles = new ArrayList<Article>();
        this.settings = new Settings();

        this.replaceFragment(HomeFragment.newInstance());
    }

    public void replaceFragment(Fragment someFragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, someFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //Implementation of all fragment listener interfaces

    @Override
    public List<Article> getArticleList() {
        return articleList;
    }

    @Override
    public void saveArticle(Article article) {
        this.savedArticles.add(article);
    }

    @Override
    public Settings getSettings() {
        return this.settings;
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
    public ArrayList<Article> getSavedList() {
        return this.savedArticles;
    }
}

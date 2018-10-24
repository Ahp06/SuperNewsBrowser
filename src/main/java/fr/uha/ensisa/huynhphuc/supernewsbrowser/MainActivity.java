package fr.uha.ensisa.huynhphuc.supernewsbrowser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.ArticleListFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.HomeFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Article;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.HomeFragmentListener, ArticleListFragment.ArticleListFragmentListener{

    private EditText query;
    private ArrayList<Article> articleList;
    private ArrayList<Article> savedArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.articleList = new ArrayList<Article>();
        this.savedArticles = new ArrayList<Article>();

    }

    @Override
    public List<Article> getArticleList() {
        return articleList;
    }

    @Override
    public void saveArticle(Article article) {
        this.savedArticles.add(article);
    }

    public void replaceFragment(Fragment someFragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, someFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void setArticleList(ArrayList<Article> articles) {
        this.articleList = articles;
    }

    @Override
    public void requestArticleList() {
        this.replaceFragment(ArticleListFragment.newInstance(articleList));
    }

    @Override
    public void requestSavedList() {

    }

    @Override
    public void requestHistory() {

    }

    @Override
    public void requestSettings() {

    }

    @Override
    public void requestTopNews() {

    }
}

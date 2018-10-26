package fr.uha.ensisa.huynhphuc.supernewsbrowser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.w3c.dom.Comment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.ArticleListFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.CommentFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.DatePickerFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.HomeFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.SavedListFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments.SettingsFragment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Article;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.ArticleComment;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Settings;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.HomeFragmentListener,
        ArticleListFragment.ArticleListFragmentListener,
        SavedListFragment.SavedFragmentListener,
        SettingsFragment.SettingsFragmentListener,
        DatePickerFragment.DatePickerFragmentListener,
        CommentFragment.CommentFragmentListener {

    private ArrayList<Article> articleList;
    private ArrayList<Article> savedArticles;
    private ArrayList<Article> toDelete;
    private ArrayList<ArticleComment> comments;
    private Settings settings;

    public static final int COMMENT_FRAGMENT = 0;
    public static final int SAVED_FRAGMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (this.articleList == null) this.articleList = new ArrayList<Article>();
        if (this.savedArticles == null) this.savedArticles = new ArrayList<Article>();
        if (this.comments == null) this.comments = new ArrayList<ArticleComment>();
        if (this.settings == null) this.settings = new Settings();
        if(this.toDelete == null) this.toDelete = new ArrayList<Article>();

        this.replaceFragment(HomeFragment.newInstance());
    }

    public void replaceFragment(Fragment someFragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, someFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static boolean compare(Article a1, Article a2) {
        boolean condition =
                a1.getTitle().equals(a2.getTitle())
                        && a1.getAuthor().equals(a2.getAuthor())
                        && a1.getDescription().equals(a2.getDescription())
                        && a1.getUrl().equals(a2.getUrl())
                        && a1.getUrlToImage().equals(a2.getUrlToImage());

        return condition;
    }

    public static int getIndex(Article article, ArrayList<Article> list) {
        int index = 0;
        for (Article art : list) {
            if (compare(art, article)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    //Implementation of all fragment listener interfaces

    @Override
    public List<Article> getArticleList() {
        return articleList;
    }

    @Override
    public void requestSaveArticle(Article article) {
        this.savedArticles.add(article);
    }

    @Override
    public void deleteComment(ArticleComment comment) {
        this.comments.remove(comment);
    }

    @Override
    public void requestComment(Article article) {
        CommentFragment commentFragment = CommentFragment.newInstance();
        Bundle args = new Bundle();
        args.putParcelable("article", article);
        commentFragment.setArguments(args);
        this.replaceFragment(commentFragment);
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
    public ArrayList<Article> getSavedList() {
        return this.savedArticles;
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
    public ArticleComment getCommentOf(Article article) {
        for (ArticleComment comment : comments) {
            if (compare(article, comment.getArticle())) {
                return comment;
            }
        }
        return null;
    }

    @Override
    public void addComment(ArticleComment articleComment) {
        this.comments.add(articleComment);
    }

    @Override
    public boolean isSaved(Article article, int fragment) {
        boolean isSaved = false;
        for (Article a : savedArticles) {
            if (compare(article, a)) {
                isSaved = true;
            }
        }

        boolean inToDeleteList = false;
        if (fragment == MainActivity.COMMENT_FRAGMENT) {
            for (Article a : toDelete) {
                if (compare(article, a)) {
                    inToDeleteList = true;
                }
            }
        }

        //The article is in the saved list and isn't into toDelete list
        return fragment == MainActivity.SAVED_FRAGMENT ? (isSaved && !inToDeleteList) : isSaved;
    }
}

package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Article;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.utils.ArticleImageDownload;

public class ArticleListFragment extends Fragment {

    private ArticleListFragmentListener mListener;

    public interface ArticleListFragmentListener {
        List<Article> getArticleList();
        void requestComment();
        void requestSaveArticle(Article article);
    }

    public ArticleListFragment() {
    }

    public static ArticleListFragment newInstance() {
        ArticleListFragment fragment = new ArticleListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ArticleListAdapter());
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ArticleListFragmentListener) {
            mListener = (ArticleListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ArticleListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public final View mView;
            public final TextView contentView;
            public final ImageView imageView;
            public final Button save_button;
            public final Button comment_button;

            public ViewHolder(View view) {
                super(view);
                contentView = (TextView) view.findViewById(R.id.articleContent);
                imageView = (ImageView) view.findViewById(R.id.imageView);
                save_button = (Button) view.findViewById(R.id.save_button);
                comment_button = (Button) view.findViewById(R.id.comment_button);
                mView = view;
            }

            @Override
            public void onClick(View v) {
                //GO to the website
            }
        }

        public ArticleListAdapter() {
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_article, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            final Article article = mListener.getArticleList().get(i);

            //Image downloading
            ArticleImageDownload downloader = new ArticleImageDownload(viewHolder.imageView);
            downloader.download(article.getUrlToImage());


            //Date parsing
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = null;
            try {
                date = df.parse(article.getPublishedAt().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateFormatted = outputFormatter.format(date);

            //Set content into article item
            String author;
            if (article.getAuthor() != "null") {
                author = article.getAuthor();
            } else {
                author = getContext().getString(R.string.unknow_author);
            }

            String content =
                    "<h2>" + article.getTitle() + "</h2>" +
                            "<p> Auteur : " + author + "</p>" +
                            "</br>" +
                            "<p>" + article.getDescription() + "</p>" +
                            "</br>" +
                            "<p> écrit le : " + dateFormatted + "</p>";

            viewHolder.contentView.setText(Html.fromHtml(content));

            viewHolder.save_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.requestSaveArticle(article);
                }
            });

            viewHolder.comment_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   mListener.requestComment();
                }
            });

        }

        @Override
        public int getItemCount() {
            return mListener.getArticleList().size();
        }
    }


}

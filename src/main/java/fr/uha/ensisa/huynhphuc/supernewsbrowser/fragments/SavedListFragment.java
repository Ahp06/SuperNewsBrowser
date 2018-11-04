package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.MainActivity;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Article;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.utils.ArticleImageDownload;


public class SavedListFragment extends Fragment {

    private SavedFragmentListener mListener;

    public interface SavedFragmentListener {
        List<Article> getSavedList();

        boolean isSaved(Article article, int fragment);

        void addToDelete(Article article);

        void removeToDelete(Article article);

        void requestComment(Article article);

        void requestWebsite(Article article);

        void deleteArticlesToDelete();

        void clearSaved();
    }


    public SavedListFragment() {
    }

    public static SavedListFragment newInstance() {
        SavedListFragment fragment = new SavedListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.saved_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_saved) {
            mListener.clearSaved();
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        this.setHasOptionsMenu(true);
        if (!mListener.getSavedList().isEmpty()) {
            view = inflater.inflate(R.layout.fragment_article_list, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_empty_saved, container, false);
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            DividerItemDecoration itemDecor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(itemDecor);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new SavedListAdapter());
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SavedFragmentListener) {
            mListener = (SavedFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SavedFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.deleteArticlesToDelete();
        mListener = null;
    }


    private class SavedListAdapter extends RecyclerView.Adapter<SavedListAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView contentViewSaved;
            public final ImageView imageViewSaved;
            public final Button delete_button;
            public final Button comment_button_saved;

            public ViewHolder(View view) {
                super(view);
                contentViewSaved = (TextView) view.findViewById(R.id.articleContent_saved);
                imageViewSaved = (ImageView) view.findViewById(R.id.imageView_saved);
                delete_button = (Button) view.findViewById(R.id.delete_button);
                comment_button_saved = (Button) view.findViewById(R.id.saved_comment_button);
                mView = view;
            }
        }

        public SavedListAdapter() {
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_saved, viewGroup, false);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

            final Article article = mListener.getSavedList().get(i);

            //Image downloading
            ArticleImageDownload downloader = new ArticleImageDownload(viewHolder.imageViewSaved);
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

            viewHolder.contentViewSaved.setText(Html.fromHtml(content));

            viewHolder.delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener.isSaved(article, MainActivity.SAVED_FRAGMENT)) {
                        viewHolder.delete_button.setText(R.string.cancel_delete_text);
                        mListener.addToDelete(article);
                    } else {
                        viewHolder.delete_button.setText(R.string.delete_text);
                        mListener.removeToDelete(article);
                    }
                }
            });

            viewHolder.comment_button_saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.requestComment(article);
                }
            });

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.requestWebsite(article);
                }
            });

            if (article.getComment() != "" && article.getComment() != null) {
                viewHolder.comment_button_saved.setText(R.string.see_comment);
            } else {
                viewHolder.comment_button_saved.setText(R.string.comment_text);
            }

        }

        @Override
        public int getItemCount() {
            return mListener.getSavedList().size();
        }


    }

}

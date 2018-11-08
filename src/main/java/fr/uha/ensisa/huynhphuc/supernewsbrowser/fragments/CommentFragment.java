package fr.uha.ensisa.huynhphuc.supernewsbrowser.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.MainActivity;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.Article;

public class CommentFragment extends Fragment {

    private Article article;
    private CommentFragmentListener mListener;

    public interface CommentFragmentListener {
        boolean isSaved(Article article, int fragment);
        void requestSaveArticle(Article article);
        void requestCancelSave(Article article);
        void updateSavedDao(Article article);
    }

    public CommentFragment() {
    }


    public static CommentFragment newInstance() {
        CommentFragment fragment = new CommentFragment();
        return fragment;
    }

    public class ViewHolder {

        public final View mView;
        public final TextView comment_text;
        public final Button cancel_button;
        public final Button valid_button;
        public final Button delete_comment;

        public ViewHolder(View v) {
            comment_text = (TextView) v.findViewById(R.id.articleComment);
            cancel_button = (Button) v.findViewById(R.id.cancel_comment);
            valid_button = (Button) v.findViewById(R.id.valid_comment);
            delete_comment = (Button) v.findViewById(R.id.delete_comment);
            mView = v;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        Bundle args = getArguments();
        if (args != null) {
            this.article = args.getParcelable("article");
        }

        String comment = article.getComment();
        if (comment != null) {
            viewHolder.comment_text.setText(comment);
        } else {
            viewHolder.comment_text.setText("");
        }

        viewHolder.valid_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewHolder.comment_text.getText().equals("")) {
                    String content = viewHolder.comment_text.getText().toString();
                    article.setComment(content);
                    if (!mListener.isSaved(article, MainActivity.COMMENT_FRAGMENT)) {
                        mListener.requestSaveArticle(article);
                    }
                } else {
                    mListener.requestCancelSave(article);
                }
                mListener.updateSavedDao(article);
                getFragmentManager().popBackStack();
            }
        });

        viewHolder.cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        viewHolder.delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article.setComment(""); // OU null ?
                mListener.updateSavedDao(article);
                getFragmentManager().popBackStack();
                Toast.makeText(v.getContext(), R.string.delete_comment_text, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommentFragmentListener) {
            mListener = (CommentFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CommentFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

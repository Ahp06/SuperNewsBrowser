package fr.uha.ensisa.huynhphuc.supernewsbrowser.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.R;

public class ArticleImageDownload extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    private String url;

    static class LoadingDrawable extends ColorDrawable {
        private final WeakReference<ArticleImageDownload> articleImageDownloadWeakReference;

        public LoadingDrawable(ArticleImageDownload articleImageDownload) {
            super(R.drawable.ic_library_books_black_24dp);
            articleImageDownloadWeakReference =
                    new WeakReference<ArticleImageDownload>(articleImageDownload);
        }

        public ArticleImageDownload getArticleImageDownload() {
            return articleImageDownloadWeakReference.get();
        }
    }

    public ArticleImageDownload(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        this.url = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            ArticleImageDownload articleImageDownload = getAsyncTask(imageView);
            //If this is equal to the downloader reference of this image View
            if (this == articleImageDownload) {
                imageView.setImageBitmap(result);
            }
        }
    }

    public String getUrl() {
        return this.url;
    }

    public boolean cancelDownload(String url, ImageView imageView) {
        ArticleImageDownload articleImageDownload = this.getAsyncTask(imageView);

        if (articleImageDownload != null) {
            String bitmapUrl = articleImageDownload.getUrl();
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                articleImageDownload.cancel(true);
            } else {
                // URL is already being downloaded
                return false;
            }
        }
        return true;
    }

    public ArticleImageDownload getAsyncTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof ArticleImageDownload.LoadingDrawable) {
                ArticleImageDownload.LoadingDrawable downloadedDrawable = (ArticleImageDownload.LoadingDrawable) drawable;
                return downloadedDrawable.getArticleImageDownload();
            }
        }
        return null;
    }

    public void download(String url) {
        if (cancelDownload(url, imageView)) {
            ArticleImageDownload task = new ArticleImageDownload(imageView);
            LoadingDrawable downloadedDrawable = new LoadingDrawable(task);
            imageView.setImageDrawable(downloadedDrawable);
            task.execute(url);
        }
    }
}
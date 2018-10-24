package fr.uha.ensisa.huynhphuc.supernewsbrowser.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Article implements Parcelable {

    private String title;
    private String author;
    private String urlToImage;
    private String description;
    private String publishedAt;
    private String url;

    public Article(String title, String author, String urlToImage, String description, String publishedAt, String url) {
        this.title = title;
        this.author = author;
        this.urlToImage = urlToImage;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
    }

    /***
     * Constructor by reading JSON object fields
     * @param articleJSON
     */
    public Article(JSONObject articleJSON) {
        this.title = articleJSON.optString("title");
        this.author = articleJSON.optString("author");
        this.urlToImage = articleJSON.optString("urlToImage");
        this.description = articleJSON.optString("description");
        this.publishedAt = articleJSON.optString("publishedAt");
        this.url = articleJSON.optString("url");
    }

    protected Article(Parcel in) {
        title = in.readString();
        author = in.readString();
        urlToImage = in.readString();
        description = in.readString();
        publishedAt = in.readString();
        url = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);

        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Article{" +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", description='" + description + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(urlToImage);
        dest.writeString(description);
        dest.writeString(publishedAt);
        dest.writeString(url);
    }
}
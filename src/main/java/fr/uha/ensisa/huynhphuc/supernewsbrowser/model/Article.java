package fr.uha.ensisa.huynhphuc.supernewsbrowser.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.json.JSONObject;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Article implements Parcelable {

    @Id
    private Long id;

    @Property(nameInDb = "TITLE")
    private String title;

    @Property(nameInDb = "AUTHOR")
    private String author;

    @Property(nameInDb = "URLTOIMAGE")
    private String urlToImage;

    @Property(nameInDb = "DESCRIPTION")
    private String description;

    @Property(nameInDb = "PUBLISHEDAT")
    private String publishedAt;

    @Property(nameInDb = "URL")
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

    @Keep
    public Article(Long id, String title, String author, String urlToImage, String description, String publishedAt,
            String url, boolean isSaved) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.urlToImage = urlToImage;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
    }

    @Keep
    public Article() {
    }

    @Generated(hash = 1496608951)
    public Article(Long id, String title, String author, String urlToImage, String description, String publishedAt,
            String url) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.urlToImage = urlToImage;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
package fr.uha.ensisa.huynhphuc.supernewsbrowser.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;

import java.util.Calendar;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.BuildConfig;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class Settings implements Parcelable {

    private final static String BASE_URL = "https://newsapi.org/v2/everything?";
    private final static String API_KEY = BuildConfig.ApiKey;
    private String queryWithSettings;

    @Id
    private Long id;

    @Property(nameInDb = "LANGUAGE")
    @Index(unique = true)
    private String language;

    @Property(nameInDb = "PAGESIZE")
    @Index(unique = true)
    private String pageSize;

    @Property(nameInDb = "SORTBY")
    @Index(unique = true)
    private String sortBy;

    @Property(nameInDb = "FROM")
    @Index(unique = true)
    private String from;

    @Property(nameInDb = "TO")
    @Index(unique = true)
    private String to;


    public Settings(String language, String pageSize, String sortBy, String from, String to) {
        this.language = language;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.from = from;
        this.to = to;
    }

    //default settings
    public Settings(){
        Calendar current = Calendar.getInstance();
        int current_year = current.get(Calendar.YEAR);
        int current_day = current.get(Calendar.DAY_OF_MONTH);
        int current_month = current.get(Calendar.MONTH);
        //Current date
        String to = current_year + "-" + (current_month + 1) + "-" + current_day;

        Calendar before = current;
        before.add(Calendar.DAY_OF_WEEK, -7); // 7 days before today
        int before_year = before.get(Calendar.YEAR);
        int before_day = before.get(Calendar.DAY_OF_MONTH);
        int before_month = before.get(Calendar.MONTH);

        String from = before_year + "-" + (before_month + 1) + "-" + before_day;

        this.language = "fr";
        this.pageSize = "20";
        this.sortBy = "Date";
        this.from = from;
        this.to = to;
    }


    protected Settings(Parcel in) {
        queryWithSettings = in.readString();
        language = in.readString();
        pageSize = in.readString();
        sortBy = in.readString();
        from = in.readString();
        to = in.readString();
    }

    @Generated(hash = 1598426357)
    public Settings(String queryWithSettings, Long id, String language, String pageSize,
            String sortBy, String from, String to) {
        this.queryWithSettings = queryWithSettings;
        this.id = id;
        this.language = language;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.from = from;
        this.to = to;
    }

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel in) {
            return new Settings(in);
        }

        @Override
        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    public String applySettings(String query){

        StringBuilder queryWithSettings = new StringBuilder();

        queryWithSettings.append(BASE_URL);

        queryWithSettings.append("language=" + language);
        queryWithSettings.append("&");

        queryWithSettings.append("pageSize=" + pageSize);
        queryWithSettings.append("&");

        String sortMethod = "publishedAt";
        if(sortBy.equals("Popularité")) sortMethod ="popularity";
        if(sortBy.equals("Pertinence")) sortMethod ="relevancy";
        if(sortBy.equals("Date")) sortMethod ="publishedAt";

        queryWithSettings.append("sortBy=" + sortMethod);
        queryWithSettings.append("&");

        if(from != ""){
            queryWithSettings.append("from=" + from);
            queryWithSettings.append("&");
        }
        if(to != ""){
            queryWithSettings.append("to=" + to);
            queryWithSettings.append("&");
        }

        queryWithSettings.append("q=" + query);
        queryWithSettings.append("&");
        queryWithSettings.append("apiKey=" + API_KEY);

        return queryWithSettings.toString();
    }

    @Override
    public String toString() {
        return "Settings{" +
                "language='" + language + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(queryWithSettings);
        dest.writeString(language);
        dest.writeString(pageSize);
        dest.writeString(sortBy);
        dest.writeString(from);
        dest.writeString(to);
    }

    public String getQueryWithSettings() {
        return this.queryWithSettings;
    }

    public void setQueryWithSettings(String queryWithSettings) {
        this.queryWithSettings = queryWithSettings;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

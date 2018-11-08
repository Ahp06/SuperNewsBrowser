package fr.uha.ensisa.huynhphuc.supernewsbrowser.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

@Entity
public class History {

    @Id
    private Long id;

    @Property(nameInDb = "QUERY")
    private String query;

    @Property(nameInDb = "DATE")
    private String date;

    public History(String query, String date){
        this.query = query;
        this.date = date;
    }

    @Generated(hash = 1173401740)
    public History(Long id, String query, String date) {
        this.id = id;
        this.query = query;
        this.date = date;
    }

    @Generated(hash = 869423138)
    public History() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}

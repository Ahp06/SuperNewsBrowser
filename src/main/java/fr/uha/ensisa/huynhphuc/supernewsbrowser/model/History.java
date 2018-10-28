package fr.uha.ensisa.huynhphuc.supernewsbrowser.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class History {

    @Id
    private Long id;

    @Property(nameInDb = "QUERY")
    private String query;

    public History(String query){
        this.query = query;
    }

    @Generated(hash = 2104745400)
    public History(Long id, String query) {
        this.id = id;
        this.query = query;
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

}

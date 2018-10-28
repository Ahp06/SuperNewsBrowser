package fr.uha.ensisa.huynhphuc.supernewsbrowser.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class ArticleComment {

    @Id
    private Long id;

    @Property(nameInDb = "COMMENT")
    private String comment;

    private Long articleID;

    @ToOne(joinProperty = "articleID")
    private Article article;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1606932241)
    private transient ArticleCommentDao myDao;

    @Generated(hash = 341632769)
    private transient Long article__resolvedKey;

    public ArticleComment(Article article, String comment) {
        this.article = article;
        this.comment = comment;
    }

    @Generated(hash = 998364151)
    public ArticleComment(Long id, String comment, Long articleID) {
        this.id = id;
        this.comment = comment;
        this.articleID = articleID;
    }

    @Generated(hash = 162153645)
    public ArticleComment() {
    }

    /*public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }*/

    public long getArticleID() {
        return articleID;
    }

    public void setArticleID(long articleID) {
        this.articleID = articleID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ArticleComment{" +
                "article=" + article +
                ", comment='" + comment + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setArticleID(Long articleID) {
        this.articleID = articleID;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1859043465)
    public Article getArticle() {
        Long __key = this.articleID;
        if (article__resolvedKey == null || !article__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ArticleDao targetDao = daoSession.getArticleDao();
            Article articleNew = targetDao.load(__key);
            synchronized (this) {
                article = articleNew;
                article__resolvedKey = __key;
            }
        }
        return article;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1301069091)
    public void setArticle(Article article) {
        synchronized (this) {
            this.article = article;
            articleID = article == null ? null : article.getId();
            article__resolvedKey = articleID;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1076226408)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getArticleCommentDao() : null;
    }

}

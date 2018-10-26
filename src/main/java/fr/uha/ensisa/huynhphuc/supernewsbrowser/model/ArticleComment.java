package fr.uha.ensisa.huynhphuc.supernewsbrowser.model;

public class ArticleComment {

    private String comment;
    private Article article;

    public ArticleComment(Article article, String comment) {
        this.article = article;
        this.comment = comment;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
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

}

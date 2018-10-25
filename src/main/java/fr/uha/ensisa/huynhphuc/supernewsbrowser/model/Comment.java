package fr.uha.ensisa.huynhphuc.supernewsbrowser.model;

public class Comment {

    private String comment;
    private Article article;

    public Comment(Article article, String comment) {
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
        return "Comment{" +
                "article=" + article +
                ", comment='" + comment + '\'' +
                '}';
    }

}

package sg.edu.nus.iss.day28workshop.models;

import java.io.Serializable;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

public class Review implements Serializable {
    
    private Integer gid;
    private String name;
    private Integer rating;
    private String user;
    private String comment;
    private String reviewId;

    public Integer getGid() {
        return gid;
    }
    public void setGid(Integer gid) {
        this.gid = gid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getReviewId() {
        return reviewId;
    }
    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public static Review create(Document document) {
        Review review = new Review();
        review.setGid(document.getInteger("gid"));

        List<Object> gameName = document.get("name", List.class);
        review.setName((String) gameName.get(0));

        review.setRating(document.getInteger("rating"));
        review.setUser(document.getString("user"));
        review.setComment(document.getString("c_text"));
        review.setReviewId(document.getString("c_id"));
        return review;
    }

    public JsonObjectBuilder toJsonObjectBuilder() {
        return Json.createObjectBuilder()
                    .add("gid", this.getGid())
                    .add("name", this.getName())
                    .add("rating", this.getRating())
                    .add("user", this.getUser())
                    .add("comment", this.getComment())
                    .add("review_id", this.getReviewId());
    }
}

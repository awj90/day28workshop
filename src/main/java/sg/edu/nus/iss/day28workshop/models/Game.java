package sg.edu.nus.iss.day28workshop.models;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Game implements Serializable {
    private String _id;
    private Integer gid;
    private String name;
    private Integer year;
    private Integer ranking;
    private Integer usersRated;
    private String url;
    private String image;
    private long timestamp;
    private String[] reviews;

    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
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
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getRanking() {
        return ranking;
    }
    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
    public Integer getUsersRated() {
        return usersRated;
    }
    public void setUsersRated(Integer usersRated) {
        this.usersRated = usersRated;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String[] getReviews() {
        return reviews;
    }
    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }

    public static Game create(Document document) {

        List<Object> r = document.get("reviews", List.class);
        List<String> rStr = new LinkedList<>();

        for (Object o: r) {
            String fullStr = "/review/" + (String) o;
            rStr.add(fullStr);
        }

        Game game = new Game();
        game.set_id(document.getObjectId("_id").toString());
        game.setGid(document.getInteger("gid"));
        game.setName(document.getString("name"));
        game.setYear(document.getInteger("year"));
        game.setRanking(document.getInteger("ranking"));
        game.setUsersRated(document.getInteger("users_rated"));
        game.setUrl(document.getString("url"));
        game.setImage(document.getString("image"));
        game.setTimestamp(document.getLong("timestamp"));
        game.setReviews(rStr.toArray(new String[rStr.size()]));
        return game;
    }

    public JsonObject toJsonObject() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();  
        for (String s: this.getReviews()) {
            jsonArrayBuilder.add(s);
        }      
        JsonArray jsonArray = jsonArrayBuilder.build();
        System.out.println(getTimestamp());
        return Json.createObjectBuilder()
                    .add("_id", this.get_id())
                    .add("gid", this.getGid())
                    .add("name", this.getName())
                    .add("year", this.getYear())
                    .add("ranking", this.getRanking())
                    .add("users_rated", this.getUsersRated())
                    .add("url", this.getUrl())
                    .add("image", this.getImage())
                    .add("timestamp", new Date(this.getTimestamp()).toString())
                    .add("reviews", jsonArray.toString())
                    .build();
    }
}

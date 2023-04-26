package sg.edu.nus.iss.day28workshop.models;

import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

public class UserReviews {
    private String rating;
    private Review[] games;
    private long timestamp;

    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public Review[] getGames() {
        return games;
    }
    public void setGames(Review[] games) {
        this.games = games;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public JsonObjectBuilder toJsonObjectBuilder() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Review r : games) {
            jsonArrayBuilder.add(r.toJsonObjectBuilder());
        }
        JsonArray jsonArray = jsonArrayBuilder.build();
        return Json.createObjectBuilder()
                    .add("rating", this.getRating())
                    .add("games", jsonArray.toString())
                    .add("timestamp", new Date(this.getTimestamp()).toString());
    }
}

package sg.edu.nus.iss.day28workshop.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import sg.edu.nus.iss.day28workshop.models.Game;
import sg.edu.nus.iss.day28workshop.models.Review;
import sg.edu.nus.iss.day28workshop.models.UserReviews;
import sg.edu.nus.iss.day28workshop.service.AppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class AppRestController {
    
    @Autowired
    private AppService appService;

    @GetMapping(path="/game/{gid}/reviews", produces="application/json")
    public ResponseEntity<String> getGameWithReviews(@PathVariable String gid) {
        Optional<Game> result = appService.getGameWithReviews(gid);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Json.createObjectBuilder()
                                            .add("error", "Game ID %s not found".formatted(gid))
                                            .build()
                                            .toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(result.get()
                                            .toJsonObject()
                                            .toString());
    }
    
    @GetMapping(path="/games/{flag}", produces="application/json")
    public ResponseEntity<String> getRatedGamesOfUser(@PathVariable String flag, @RequestParam(required=true) String user, @RequestParam(required=false, defaultValue="500") Integer limit) {
        Optional<List<Review>> results = appService.getRatedGamesOfUser(user, limit, flag);
        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Json.createObjectBuilder()
                                            .add("error", "Your query returned an empty set")
                                            .build()
                                            .toString());
        }

        UserReviews userReviews = new UserReviews();
        userReviews.setRating(flag);
        List<Review> games = results.get();
        userReviews.setGames(games.toArray(new Review[games.size()]));
        userReviews.setTimestamp(new Date().getTime());
        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(userReviews.toJsonObjectBuilder()
                                                .build()
                                                .toString());
    }
}

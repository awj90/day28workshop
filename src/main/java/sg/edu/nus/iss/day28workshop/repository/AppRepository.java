package sg.edu.nus.iss.day28workshop.repository;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation.AddFieldsOperationBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day28workshop.models.Game;
import sg.edu.nus.iss.day28workshop.models.Review;

@Repository
public class AppRepository {
    
    private static final String GAME_COLLECTION_NAME = "game";
    private static final String REVIEWS_COLLECTION_NAME = "reviews";

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<Game> getGameWithReviews(Integer gid) {
        
        MatchOperation matchOperation = Aggregation.match(Criteria.where("gid").is(gid));

        LookupOperation lookupOperation = Aggregation.lookup(REVIEWS_COLLECTION_NAME, "gid", "gid", "game_id_reviews");

        ProjectionOperation projectionOperation = Aggregation.project("_id", "gid", "name", "year", "ranking", "users_rated", "url", "image").and("game_id_reviews.c_id").as("reviews");

        // Manage dates separately
        AddFieldsOperationBuilder addFieldsOperationBuilder = Aggregation.addFields();
        addFieldsOperationBuilder.addFieldWithValue("timestamp", new Date().getTime());
        AddFieldsOperation addFieldsOperation = addFieldsOperationBuilder.build();

        Aggregation pipeline = Aggregation.newAggregation(matchOperation, lookupOperation, projectionOperation, addFieldsOperation);

        AggregationResults<Document> result = mongoTemplate.aggregate(pipeline, GAME_COLLECTION_NAME, Document.class);

        if (!result.iterator().hasNext()) {
            return Optional.empty(); // game id does not exist
        }
        Document doc = result.iterator().next();
        return Optional.of(Game.create(doc));
    }

    public Optional<List<Review>> getRatedGamesOfUser(String user, Integer limit, String flag) {

        Criteria criteria = new Criteria();

        if (flag.equals("highest")) {
            criteria.andOperator(
                Criteria.where("user").is(user),
                Criteria.where("rating").gt(5)
                );
            } else if (flag.equals("lowest")) {
                criteria.andOperator(
                Criteria.where("user").is(user),
                Criteria.where("rating").lte(5)
                );
            }

        MatchOperation matchOperation = Aggregation.match(criteria);

        LookupOperation lookupOperation = Aggregation.lookup(GAME_COLLECTION_NAME, "gid", "gid", "game_of_review");

        ProjectionOperation projectionOperation = Aggregation.project("gid", "rating", "user", "c_text", "c_id").and("game_of_review.name").as("name");

        LimitOperation limitOperation = Aggregation.limit(limit);

        Aggregation pipeline = Aggregation.newAggregation(matchOperation, lookupOperation, projectionOperation, limitOperation);

        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, REVIEWS_COLLECTION_NAME, Document.class);

        Iterator<Document> iterator = results.iterator();

        if (!iterator.hasNext()) {
            return Optional.empty(); // user id does not exist or user did not have ratings for games that were too high or too low
        }

        List<Review> r = new LinkedList<>();
        while (iterator.hasNext()) {
            Document doc = iterator.next();
            r.add(Review.create(doc));
        }
        return Optional.of(r);
    }
}

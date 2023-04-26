## GET /game/<game_id>/reviews (Accept: application/json)

Get information on a particular game including an array of the game's reviews

MongoDB aggregation pipeline:
- Match game_id (game collection)
- Look up reviews collection for reviews with foreign key = game_id
- project game_id's fields together with an array of reviews associated with the game_id

db.game.aggregate([
    {
        $match: { gid : 1 }
    },
    {
        $lookup: {
            from: 'reviews',
            foreignField: 'gid',
            localField: 'gid',
            as: 'game_id_reviews',
        }
    },
    {
        $project: {
            _id: 1,
            gid: 1,
            name: 1,
            year: 1,
            ranking: 1,
            users_rated: 1,
            url: 1,
            image: 1,
            reviews: "$game_id_reviews.c_id",
            timestamp: "$$NOW",
        }
    }
]);

## GET /games/highest?user=<username>&limit=<limit> (Accept: application/json)
Get list of games that are rated greater than 5 out of 10 by a particular user

## GET /games/lowest?user=<username>&limit=<limit> (Accept: application/json)
Get list of games that are rated smaller than or equals to 5 out of 10 by a particular user

MongoDB aggregation pipeline:
- Match user and rating (reviews collection)
- Look up game collection for games with same game_id as filtered reviews
- project desired fields of the reviews and associated games

db.reviews.aggregate([
    {
        $match: {"$and": [{"user": "densha"}, {"rating": {"$gt": 5}}]}
    },
    {
        $lookup: {
            from: 'game',
            foreignField: 'gid',
            localField: 'gid',
            as: 'game_of_review',
        }
    },
    {
        $project: {
            gid: 1,
            rating: 1,
            user: 1,
            c_text: 1,
            c_id: 1 ,
            name: "$game_of_review.name",
        }
    }, 
    { 
        "$limit": 500
    },
]);

package sg.edu.nus.iss.day28workshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.day28workshop.models.Game;
import sg.edu.nus.iss.day28workshop.models.Review;
import sg.edu.nus.iss.day28workshop.repository.AppRepository;

@Service
public class AppService {
    
    @Autowired
    private AppRepository appRepository;

    public Optional<Game> getGameWithReviews(String gid) {
        return appRepository.getGameWithReviews(Integer.parseInt(gid));
    }

    public Optional<List<Review>> getRatedGamesOfUser(String user, Integer limit, String flag) {
        return appRepository.getRatedGamesOfUser(user, limit, flag);
    }
}

package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("playerScoreRepository")
public interface PlayerScoreRepository extends JpaRepository<PlayerScore, Long> {
    Player findPlayerScoreByUserName(String userName);
}
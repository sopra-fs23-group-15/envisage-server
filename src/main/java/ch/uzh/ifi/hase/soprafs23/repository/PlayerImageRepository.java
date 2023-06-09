package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerImage;
import ch.uzh.ifi.hase.soprafs23.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("playerImageRepository")
public interface PlayerImageRepository extends JpaRepository<PlayerImage, Long> {

    List<PlayerImage> findAllByRound(Round round);

    PlayerImage findByPlayerAndRound(Player player, Round round);

    PlayerImage findById(long id);
}

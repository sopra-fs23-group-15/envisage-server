package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.PlayerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("playerImageRepository")
public interface PlayerImageRepository extends JpaRepository<PlayerImage, Player> {

    PlayerImage findByPlayer(Player player);
}

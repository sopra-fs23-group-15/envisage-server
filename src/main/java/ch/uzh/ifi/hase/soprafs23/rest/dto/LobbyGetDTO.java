package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class LobbyGetDTO {
    private Long pin;

    private List<PlayerGetDTO> players;

    public List<PlayerGetDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerGetDTO> players) {
        this.players = players;
    }

    public Long getPin() {
        return pin;
    }

    public void setPin(Long pin) {
        this.pin = pin;
    }

}

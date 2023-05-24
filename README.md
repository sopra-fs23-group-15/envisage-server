# Envisage

![envisageAsBanner](src/main/resources/docs/logoAsBanner.png)

## Introduction
In ENVISAGE, you and your friends can compete against each other over multiple rounds to create the best AI generated masterpieces. At the beginning of the first round, you are given a picture from the category you choose and an image style as a requirement. Your task is to generate an image as close as possible to the requirements. At the end of each round, you get to vote on your favorite picture (not your own!) and the best picture is given as the prompt for the next round with a different style requirement. The process continues till the final round is completed and a winner is declared based on the accumulated votes.

## Technologies
Programming Language: Java

* Spring Boot
* JPA
* Communication with the client
    - REST
    - Stomp (websockets)
* External APIs
    - [DALL-E API](https://platform.openai.com/docs/api-reference/introduction)
    - [Met Collection API](https://metmuseum.github.io/)
* Testing
    - JUnit 
    - Mockito
* Hosted on Google App Engine


## High-level components
### [Lobby](https://github.com/sopra-fs23-group-15/envisage-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Lobby.java)
The lobby keeps track of the [players](https://github.com/sopra-fs23-group-15/envisage-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Player.java) which can join a lobby with a lobby pin. Furthermore, the lobby knows how long one round is and how many rounds are going to be played. The lobby has a one-to-one relation with a game entity.

### [Game](https://github.com/sopra-fs23-group-15/envisage-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Game.java)
The game has a one-to-many relation with the [round entity](https://github.com/sopra-fs23-group-15/envisage-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Round.java). The game has as well a one-to-many relation with the [playerscore entity](https://github.com/sopra-fs23-group-15/envisage-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/PlayerScore.java).
When the players decide to restart a game, the old game will be deleted and a new game entity will be created and linked to the lobby.

### [ChallengeService](https://github.com/sopra-fs23-group-15/envisage-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/ChallengeService.java)
The ChallengeService is responsible for creating a [challenge](https://github.com/sopra-fs23-group-15/envisage-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Challenge.java). A challenge consists of a category which gets passed from the client to the server. The prompt image for the first round is then chosen according to the category. For the subsequent rounds the prompt image is the winning image of the previous round. Furthermore, the challenge has a style requirement. Style requirements are artists or art styles.



## Launch & Deployment
### Prerequisites
Clone the server repository:

```bash
git clone git@github.com:sopra-fs23-group-15/envisage-server.git
```

### Build
Run the following command to build the application:
```bash
./gradlew build
```

### Run
Run the application with the following command:
```bash
./gradlew bootRun
```
By visiting [localhost:8080](http://localhost:8080) in your browser, you can verify that the server is running.

### Test
Run the tests with:
```bash
./gradlew test
```

### Deployment
The application is hosted on Google App Engine. A push to the main will automatically lead to the deployment to the Google App Engine.


## Roadmap
* Play with anyone in the world by introducing waiting lobbies
* Ability to download your images
* Accounts to save player stats and their generated images
* Disappearing Mode (images disappear after a while and are not displayed for the entire round duration)


## Authors and acknowledgment
* Marion Andermatt - [marion-an](https://github.com/marion-an)
* Moritz Mohn - [moritzmohn](https://github.com/moritzmohn)
* Nikita Amitabh - [nikita-uzh](https://github.com/nikita-uzh)
* Shantam Raj - [armsp](https://github.com/armsp)
* Xue Wang - [xueswang](https://github.com/xueswang)

We would like to thank our mentor Valentin Hollenstein - [v4lentin1879](https://github.com/v4lentin1879) for supporting us throughout the project.


## License
This project is licensed under [Apache-2.0 license](https://github.com/sopra-fs23-group-15/envisage-client/blob/main/LICENSE)

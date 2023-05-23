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

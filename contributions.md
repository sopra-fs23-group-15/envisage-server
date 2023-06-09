## Marion
**Sprint1 Week1**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/23 (create lobby with corresponding POST request and get back Lobby)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/24 (username of player is updated in database)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/62 (implement POST request to add players to lobby and create player)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/63 (check if username is unique when joining a lobby and throw correct HTTP error)

**Sprint1 Week2**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/71 (websocket connection of server and client)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/38 (implement method to get the first image prompt from the met museum API)

**Sprint1 Week3**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/131 (subscribe to websocket to get notified when player joined the lobby and when a game starts all players get navigated to next page)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/32 (show same requirement to each player in the same lobby)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/38 (show same requirement to each player in the same lobby)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/31 (show same image to each player in the same image)

**Sprint1 Week4**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/33 (send generated images to the frontend for voting (generates a list with images))
* https://github.com/sopra-fs23-group-15/envisage-server/issues/93 (update player images array in the game with the player name and respective generated image received after calling the dalle api)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/77 (create challenge entity that has StyleRequirement Entity and ImagePrompt Entity (Xue and I came up with imagePromptIdeas=StyleRequirements) and implement logic for challenge Entity)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/37 (if there are multiple winning images then one of the images is chosen randomly (not really randomly because that would lead to a conflict with the challengeEntity))
* https://github.com/sopra-fs23-group-15/envisage-server/issues/43 (with finishing the challenge entity this is done as well, because a met Museum image is chosen again)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/50 (is completed as well with the server task above)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/37 (players get a new style requirement each round with websocket)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/36 (websocket endpoint "/lobbies/{lobbyId}/challengeForRounds/{roundId}" with a new style requirement prompt)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/35 (websocket endpoint "/lobbies/{lobbyId}/challengeForRounds/{roundId}" with the most voted on image from the previous round)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/30 (submit prompt to dall e API)

**Sprint2 Week1 (Week5)**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/82 (prompt is limited to 400 characters)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/102 (fix "submit" button issues, that either auto-submission or submission by click is triggered but not both)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/93 (implemented more suitable solution for createPlayerImage)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/111 (Beta testing of group 16 - HanZikon)

**Sprint2 Week2 (Week6)**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/111 (fix issue that end view will be displayed after configured number of rounds)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/54 (implement a GET request to get all winning images)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/55 (send images with round information metadata so that frontend can show from first to last)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/129 (Fix issue why subsequent rounds can't be started anymore)

**Sprint2 Week3 (Week7)**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/57 (send chosen category to the server and get the corresponding image for all players in the first round)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/56 (show categories to the lobby creator for the image displayed in the first round)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/58 (chosen category is passed as a parameter to the server)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/59 (upon receiving the category the server sends an image from that category to the UI for the first round)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/145 (ensure that websockets work after reloading page)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/144 (create logo and change the favicon to the logo)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/126 (fix bug why dall e didn't generate an image for each prompt)

**Sprint2 Week4 (Week8)**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/130 (tests for the remaining REST endpoints and test coverage > 75%)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/129 (fix restart method)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/131 (create REST endpoint and methods when someone leaves lobby when the game has ended)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/104 (README Frontend)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/112 (README Backend)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/114 (Presentation slides)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/113 (Report for Milestone 4)



## Moritz
**Sprint1 Week1**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/5 (if the username is already taken, prompt user to provide a new one)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/6 (if game pin is wrong provide an error message)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/7 (go to the lobby page if username and game pin are provided)

**Sprint1 Week2**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/19
* https://github.com/sopra-fs23-group-15/envisage-client/issues/20

**Sprint1 Week3**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/40
* https://github.com/sopra-fs23-group-15/envisage-client/issues/41

**Sprint1 Week4**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/39
* https://github.com/sopra-fs23-group-15/envisage-client/issues/51

**Sprint2 Week1**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/42
* https://github.com/sopra-fs23-group-15/envisage-client/issues/43
* https://github.com/sopra-fs23-group-15/envisage-client/issues/45
* https://github.com/sopra-fs23-group-15/envisage-client/issues/88
* https://github.com/sopra-fs23-group-15/envisage-server/issues/111

**Sprint2 Week2**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/44
* https://github.com/sopra-fs23-group-15/envisage-client/issues/107
* https://github.com/sopra-fs23-group-15/envisage-client/issues/114

**Sprint2 Week3**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/49
* https://github.com/sopra-fs23-group-15/envisage-client/issues/124

**Sprint2 Week4**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/152
* https://github.com/sopra-fs23-group-15/envisage-server/issues/113

## Nikita
**Sprint1 Week1**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/25
* https://github.com/sopra-fs23-group-15/envisage-server/issues/26
* https://github.com/sopra-fs23-group-15/envisage-server/issues/62

**Sprint1 Week2**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/64
* https://github.com/sopra-fs23-group-15/envisage-server/issues/65

**Sprint1 Week3**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/44
* https://github.com/sopra-fs23-group-15/envisage-server/issues/46

**Sprint1 Week4**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/45
* https://github.com/sopra-fs23-group-15/envisage-server/issues/47
* M3 Report

**Sprint2 Week1**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/78
* https://github.com/sopra-fs23-group-15/envisage-server/issues/53
* https://github.com/sopra-fs23-group-15/envisage-server/issues/111

**Sprint2 Week2**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/42
* https://github.com/sopra-fs23-group-15/envisage-server/issues/41

**Sprint2 Week3**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/125
* https://github.com/sopra-fs23-group-15/envisage-client/issues/132
* https://github.com/sopra-fs23-group-15/envisage-client/issues/155
* 
**Sprint2 Week4**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/153
* https://github.com/sopra-fs23-group-15/envisage-client/issues/154
* https://github.com/sopra-fs23-group-15/envisage-client/issues/156
* https://github.com/sopra-fs23-group-15/envisage-client/issues/112
* https://github.com/sopra-fs23-group-15/envisage-client/issues/113
* https://github.com/sopra-fs23-group-15/envisage-client/issues/114

## Shantam
**Sprint1 Week1**
* [#27](https://github.com/sopra-fs23-group-15/envisage-server/issues/27)
* [#28](https://github.com/sopra-fs23-group-15/envisage-server/issues/28)

**Sprint1 Week2**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/65
* https://github.com/sopra-fs23-group-15/envisage-client/issues/33
* https://github.com/sopra-fs23-group-15/envisage-client/issues/34
* https://github.com/sopra-fs23-group-15/envisage-server/issues/77
* https://github.com/sopra-fs23-group-15/envisage-server/issues/76
* https://github.com/sopra-fs23-group-15/envisage-server/issues/32
* https://github.com/sopra-fs23-group-15/envisage-server/issues/31
* https://github.com/sopra-fs23-group-15/envisage-client/issues/13

**Sprint1 Week3**
* Fixed Server Deployment issue on GCP App Engine
* https://github.com/sopra-fs23-group-15/envisage-client/issues/66

**Sprint1 Week4**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/83
* https://github.com/sopra-fs23-group-15/envisage-client/issues/79
* https://github.com/sopra-fs23-group-15/envisage-server/issues/29
* PPT Slides and Presentation with Marion
* https://github.com/sopra-fs23-group-15/envisage-client/issues/26

**Sprint2 Week1**
* https://github.com/sopra-fs23-group-15/envisage-server/issues/111
* https://github.com/sopra-fs23-group-15/envisage-client/issues/55
* https://github.com/sopra-fs23-group-15/envisage-client/issues/54

**Sprint2 Week2**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/106
* https://github.com/sopra-fs23-group-15/envisage-client/issues/108
* https://github.com/sopra-fs23-group-15/envisage-client/issues/109

**Sprint2 Week3**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/105
* https://github.com/sopra-fs23-group-15/envisage-client/issues/124
* https://github.com/sopra-fs23-group-15/envisage-client/issues/133

**Sprint2 Week4**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/104
* https://github.com/sopra-fs23-group-15/envisage-server/issues/114
* https://github.com/sopra-fs23-group-15/envisage-server/issues/113
* https://github.com/sopra-fs23-group-15/envisage-client/issues/168


## Xue
**Sprint1 Week1**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/1
* https://github.com/sopra-fs23-group-15/envisage-client/issues/2
* https://github.com/sopra-fs23-group-15/envisage-client/issues/3

**Sprint1 Week2**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/61
* https://github.com/sopra-fs23-group-15/envisage-client/issues/64
* https://github.com/sopra-fs23-group-15/envisage-client/issues/21 (a big commit to issue#64 also closes this issue)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/22 (a big commit to issue#64 also closes this issue)

**Sprint1 Week3**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/23
* https://github.com/sopra-fs23-group-15/envisage-client/issues/27
* https://github.com/sopra-fs23-group-15/envisage-client/issues/28
* https://github.com/sopra-fs23-group-15/envisage-client/issues/29
* https://github.com/sopra-fs23-group-15/envisage-client/issues/30

**Sprint1 Week4**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/25
* https://github.com/sopra-fs23-group-15/envisage-client/issues/36
* https://github.com/sopra-fs23-group-15/envisage-client/issues/78

**Sprint2 Week1(Week5)**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/35
* https://github.com/sopra-fs23-group-15/envisage-client/issues/101
* https://github.com/sopra-fs23-group-15/envisage-server/issues/111

**Sprint2 Week2(Week6)**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/39 (fixed issues of the final page leaderboard to make it dynamic)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/40 (fixed issues of the final page leaderboard to make it dynamic)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/41 (made the final winner only appear after all votes get collected)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/78
* https://github.com/sopra-fs23-group-15/envisage-client/issues/103

**Sprint2 Week3(Week7)**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/48 (styled the exhibition page and made it responsive, all images moving with repect to the screen size at the correct aspect ratio)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/158 (divided the initial issue #122 into subissues #158, #159, #161, #162, since there are too many pages to restyle, also restyled lobby configuration page according to the mockup design)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/159
* https://github.com/sopra-fs23-group-15/envisage-client/issues/161

**Sprint2 Week4(Week8)**
* https://github.com/sopra-fs23-group-15/envisage-client/issues/162
* https://github.com/sopra-fs23-group-15/envisage-client/issues/163 (removing the unnecessary 5-second waiting spinners; modifying notifications to let players wait before all votes are collected; fixing tie situations to display multiple winners)
* https://github.com/sopra-fs23-group-15/envisage-client/issues/122 (final round of styling touches)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/114 (Presentation slides)
* https://github.com/sopra-fs23-group-15/envisage-server/issues/113 (Report for Milestone 4)

1) Run gradle project:
   - gradlew build
   - cd build/libs/
   - java -jar drunkencard-0.0.1-SNAPSHOT.jar
2) Import SoCash_Drunken_kkolluri.postman_collection.json into postman
3) Run the follwing API in the monetioned order:
   - Creat Game //creates a new game, returns ID and the player1 joins the game
   - join Game Player2 // player2 joins the game
   - join Game Player3 // player3 joins the game
   - join Game Player4 // player4 joins the game
   - deal //cards are dealt
   - decide //decides the winner

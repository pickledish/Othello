Othello Group Project

Brandon Som and Jeff

Goal is to make a working version of Othello that has an AI that can beat you and various good things! 

Side goal is to learn how the hell Github works, is good fun

** Make sure you guys pull the lastest versions before working on the code!

Current Goals!

Compatibility for Game
Time Limits

************** PROJECT WRITE UP *******************

Mellow Othello Portobellos
Names: Jeff Hrebenach, Brandon Willett, (Som) Euakarn Liengtiraphan

|||Justification of your design decisions, including your choice of programming language

The main strategy we used for this program is the Regional Priority strategy. In this strategy we prioritize certain places for its strategic location. Corners are the most strategic position on the board and thus the most prized. They are strategic because they cannot be overtaken from any direction, making them a permanent territory marker for your color on the board. The next most valuable are the borders, excluding the squares directly next to the corners. If you occupy a square immediately surrounding the corners, you will be unable to attain a corner. Thus, it is in your best interest to avoiding placing a piece in that space for a long as possible. However, the rest of the border is a good second strategic position. If you side along the borders, there is one less direction that the opponent can use to overtake your piece. The square directly inside the first layer is also undesirable because it provides access to both the corners and the borders. 


+1000 |  -10 |  +10 |  +10 |  +10 |  +10 |  -10 |  +1000
--------------------------------------------------------
-10   |  -15 |  -15 |  -15 |  -15 |  -15 |  -15 |  -10
--------------------------------------------------------
+10   |  -15 |   +2 |    0 |   +1 |   +2 |  -15 |  +10
--------------------------------------------------------
+10   |  -15 |   0  |      |      |   +1 |  -15 |  +10
--------------------------------------------------------
+10   |  -15 |   +1 |      |      |    0 |  -15 |  +10
--------------------------------------------------------
+10   |  -15 |   +2 |   +1 |   0  |   +2 |  -15 |  +10
--------------------------------------------------------
-10   |  -15 |  -15 |  -15 | -15  | -15  | -15  | -10
--------------------------------------------------------
+1000 |  -10 |  +10 |  +10 | +10  | +10  | -10  | +1000

 
We choose to use Java because it is the language that all of us are most comfortable with. The flexibility of the language also helped us to program the complicated methods needed for this game with much more ease. 

|||Any special features of your program 



|||How you tested your program


 
|||Citations to any resources you used in developing your program, such as articles about Othello strategy. 



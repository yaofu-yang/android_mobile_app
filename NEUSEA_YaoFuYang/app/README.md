Yao-Fu Yang
10/16/2020
Tic-Tac-Toe-Activity README

Changes to Tic-Tac-Toe
Adopting Code into Main Activity:
 1) Renamed activity name and added to current project.
	**Reason**: To incorporate game as part of our current project.

 2) Converted code from Java to Kotlin (using Android Studio converter) and made small
modifications to make code work.
	**Reason**: Since I have been mostly programming in Kotlin, it made sense
to convert the language first.

 3) Externalized strings and colors into resource files
	**Reason**: To make later modifications easier (especially default values)

 4) Moved the tic-tac-toe game-board into a fragment.
	**Reason**: Original intent was to retain data inside the game board/grid, so that on
orientation changes the fragment data would still be retained. However, I was unsuccessful so far.

 5) Converted the player 1 score, player 2 score, and player status components into ViewModels
	**Reason**: For retaining score and data on changes such as screen orientation change.

 6) Resized the boxes and text for more consistent display
	**Reason**: Both the game board boxes and inner text were oversized, leading to off-screen
runoffs and hiding other buttons.

 7) Created landscape layout
	**Reason**: For better placement of buttons and information on a landscape orientation.
This also resolved the issue with some button running offscreen.

 8) Created GameController class
	**Reason**: For moving some of the game logic out of the tic-tac-toe activity.
However, since the GameController class was a static class, this quickly became difficult so I
was only able to extract some of the game logic out.
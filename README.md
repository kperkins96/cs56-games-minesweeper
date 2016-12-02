cs56-games-minesweeper
======================


Migrated from: https://foo.cs.ucsb.edu/56mantis/view.php?id=0000900 by http://github.com/pconrad

Code prior to W14: https://foo.cs.ucsb.edu/cs56/issues/0000900/

project history
===============
```
M16 hwangaustin | ije896 | saisrimat | W16 Athielk 4pm | athielk | W14 | calebnelson | andrewberls 4pm | jgee67, davidacevedo | (pconrad) Minesweeper game
```


==============================================================================
High-level description
==============================================================================

This is a program that runs the game minesweeper. Currently, the game itself is able to be played. The game can be played via GUI or through text, depending on the user's preference. There is a start menu, with six buttons: new game (with three separate difficulties), help, load, and quit. Selecting new game will start a new game. Selecting help will bring the user to a page with instructions of how to play. Quit exits the program. During a game, the user can choose reset game, exit minesweeper, or main menu the in-game toolbar at the top of the game. Once the game is over, the user gets a message indicating whetheer he/she won or lost. The user can then choose an option from the toolbar to continue with their game.

==============================================================================
Developer Notes/Documentation
==============================================================================

The end game message is currently a JLabel located in StartMenu.java. The variable name for this label is status. To change the text in this label, there is a method in StartMenu.java called setLabel(string s).

StartMenu is a class that creates a menu that the user can see and pick an option. It also initializes the game on call.

Grid is a class that is the foundation for minesweeper, applies mine locations, checks if something is open, makes flags functional, etc.

HelpScreen.java is a panel that displays help messages and a button to return to the start menu. It is displayed only after the user clicks "Help" from either the start menu or the pause menu.

MineComponent is a class that sets up the minesweeper Gui, ie. sets up the grid with numbered buttons.

==============================================================================
How to Run
=============================================================================

To run the GuiGame, use "ant mine".
To run the TextGame, use "ant textmine".

==============================================================================
W16 Final Remarks
=============================================================================
The coding style was not what I was accustom to so when I was reading through the code, I tabbed and rearranged stuff just so I could read it, however I didn't do that for all the code so beware there are atleast 2 styles here.   

==============================================================================
M16 Final Remarks
=============================================================================
Depending on the issue being addressed, it was helpful to identify which classes needed changes (adding/removing features). There are a handful of files (some that deal with GUI, another that sets up and modifies the grid/map, etc.) so reading through the code multiple times line by line while running the program will help you to understand what each part of the code is doing and how it needs to be changed to accomodate the issue. Possible features to be added along with potential bugs are listed in the "Issues" of the master repo, these include but are not limited to (#30)adding a highscore feature and (#34)exposing all mines on the display when the player loses.

==============================================================================
F16 Final Remarks
=============================================================================
At first there is a lot going on and hard to understand, Recommend looking at Grid.java and MineComponent.java first. Would be a good idea to refactor code and split up into more than three classes. 

Know that:
1. Grid is the 2D array that holds ONLY mines.
2. Map is the 2D array that holds what the user puts down (whether an entry has been "opened" aka clicked on by the user, whether an entry is a flag, etc...)
3. Thus grid and Map interactions make up the game.

The way that the code is written is kind of messy because the naming of the variables is inconsistent from file to file. The functions are scattered. So refactoring would be good. Also remove the commented out code if it doesn't help you. 

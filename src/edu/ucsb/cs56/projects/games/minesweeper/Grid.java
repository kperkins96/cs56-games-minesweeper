package edu.ucsb.cs56.projects.games.minesweeper;

import java.io.Serializable;

/** The Grid class is the foundation for minesweeper, applies mine locations, checks if something is open,
	makes flags functional, etc.
	@author Caleb Nelson
    @author David Acevedo
    @version 2015/03/04 for lab07, cs56, W15
    
 
    @author Isaiah Egan 
    @author Austin Hwang
    @author Sai Srimat
    @version July 2016 for Legacy Code, cs56, M16

*/
public class Grid implements Serializable{
    //
    String saveTime = new String("0");
    //
    final int EASY_SIZE = 10;
    final int MED_SIZE = 15;
    final int HARD_SIZE = 20;

    // instance variables
    private int size;
    private char[][] grid;
    private char[][] map;
    private boolean isGUI;
    /**
     * Default constructor for objects of class GUIGrid
     */
    public Grid(boolean isGUI){
		this.isGUI=isGUI;
		size = EASY_SIZE;
		grid = new char[size][size];
		map = new char[size][size];
		setZero();
		for(int i = 0; i < size; i++){
			blankToMine();
		}
		insertNums();
		mapMaker(map);
    }

    public void setGrid(char[][] input){ //method that creates a known grid for testing various Grid methods
        grid = input;
        size = 4;
    }

    
    public Grid(boolean isGUI, int difficulty) {
	this.isGUI=isGUI;
	switch (difficulty){
        case -1: //for known grid testing
            size = 4;
            break;
        case 0:
			size = EASY_SIZE;
			break;
		case 1:
			size = MED_SIZE;
			break;
		case 2:
			size = HARD_SIZE;
			break;
		default:
			throw new IllegalArgumentException("Difficulty needs to be an integer between 0 and 2 inclusive.");
	}
	grid = new char[size][size];
	map = new char[size][size];
	setZero();
	switch (difficulty){
        case -1:
            break;
        case 0:
			for(int i = 0; i < size; i++){
	    		blankToMine();
			}
			break;
		case 1:
			for(int i = 0; i < 2*size; i++){
	    		blankToMine();
			}
			break;
		case 2:
			for(int i = 0; i < 3*size; i++){
	    		blankToMine();
			}
			break;
		default:
			throw new IllegalArgumentException("Difficulty needs to be an integer between 0 and 2 inclusive.");
	}
	insertNums();
	mapMaker(map);
    }
	
	/**
	 *	Getter for isGUI
	 */
	public boolean getIsGUI(){
		return isGUI;
	}

	/**
	 *	Getter for size
	 */
	public int getSize(){
		return size;
	}

    /**
     * Sets all cells in the grid to zero.
     */

    public void setZero(){ 
	for(int i = 0;i < size;i++){
	    for(int j = 0; j < size;j++){
		grid[i][j] = '0';
	    }
	}
	return;
    }

    /**
     * Finds a random Empty cell and makes it a Mine
     */

    public void blankToMine(){ 
	int spotX = (int) (size*size * Math.random());
	int a = spotX/size;
	int b = spotX%size;
	if(grid[a][b] != 'X'){
	    grid[a][b] = 'X';
		if(isGUI == true)
			System.out.println(a*size+b);}
	else
	    blankToMine(); 
	return;
    }

    /**
     * Finds all Mines in the grid and increases the number of its surrounding integer Cells by 1
     */

    public void insertNums(){ 
	for(int i = 0;i < size;i++){
	    for(int j = 0; j < size;j++){
		if(isMine(i*size+j)){
		    for(int k = i-1; k <= i+1; k++){
			for(int l = j-1; l <= j+1; l++){
			    if(k >= 0 && k <= size-1 && l >= 0 && l <= size-1 && !(isMine(k*size+l))){
				grid[k][l]++;}
			}
		    }
		}
	    }
	}
	return;
    }

    /**
     * Creates a map filled with question marks
     */

    public void mapMaker(char map[][]){ 
	for(int i = 0;i < size;i++){
	    for(int j = 0; j < size;j++){
		map[i][j] = '?';
	    }
	}
	return;
    }

    /**
     * Prints out the map in a table
     */

    public String toString() {
	final String borders = " ---------------------";
	final String line = "|";
	
	String game = "";

	game += "\n  ";
	for(int i = 0;i < size; i++){
		game += i;
		game += " ";
	}
	game += "\n";
	game += borders;
	game += "\n";
	for(int i = 0;i < size;i++){
	    game += i + line;
	    for(int j = 0; j < size;j++){
		game += map[i][j];
		game += line;
	    }
	    game += "\n";
	}
	game += borders;

	return game;  
    }

    /**
     * Checks a cell to see if it has been opened
     */

    public boolean isOpen(int i) throws IllegalArgumentException {
	if( i >= 0 && i <= (size*size)-1){
	    if(map[i/size][i%size]  != '?' && map[i/size][i%size] != 'F')
		return true;
	    else
		return false;
    }
	else
	    throw new IllegalArgumentException("I don't know where this exists :(");
    }

    /**
     * Checks a cell to see if there is a grid underneath 
     */

    public boolean isMine(int i) throws IllegalArgumentException {
	if( i >= 0 && i <= (size*size)-1){
	    if(grid[i/size][i%size]  == 'X')
		return true;
	    else
		return false;
    }
	else
	    throw new IllegalArgumentException("I don't know where this exists :(");
    }

    /**
     * Check to see if a user placed a flag on that cell
     */

    public boolean isFlag(int i) throws IllegalArgumentException{
	if( i >= 0 && i <= (size*size)-1){
	    if(map[i/size][i%size]  == 'F')
		return true;
	    else
		return false;
    }
	else
	    throw new IllegalArgumentException("I don't know where this exists :(");
    }

    /**
     * Opens the cell and returns what will be placed there
     */

    public char searchBox(int box){ 
    	char spot='e';
		if(box >= 0 && box < (size*size)){
			// set variable to an object in the grid
	    	spot = grid[box/size][box%size];

	    	if(isFlag(box)){
				System.out.println("You cannot search a flaged box!");
	    	}
	    	else if(isOpen(box)){
				System.out.println("You cannot search an opened box!");
	    	}
			else{
				if(spot == '0') {
					findAllZeros(box / size, box % size);
				}
				else {
					map[box / size][box % size] = spot;
				}
			}
		}
	return spot;
    }

     /**
     * Places a flag on the cell
     */
 
    public void flagBox(int box){ 
	if(isFlag(box)){
	    System.out.println("This box is already flagged!");
	    return;
	}
	else if(isOpen(box)){
        System.out.println("You cannot put a flag on an opened box!");
	    return;
	}
	else{
		// TODO: places 'F' only after a left click on a nonflag occurs?
        map[box/size][box%size] = 'F';
	    return;
	}
    }

    /**
     * Removes a flag on a cell that has one
     */

    public void deflagBox(int box){
	if(!(isFlag(box))){
	    System.out.println("That box does not have a flag on it!");
	    return;
	}
	else{
	    map[box/size][box%size] = '?';
	}
	return;
    }

    /**
     * Looks for surrounding numbers near the cell and opens them, repeats when find another zero
     */

    public void findAllZeros(int row, int col){ //TODO: throw exception
		for(int i = row-1; i <= row+1; i++){
			for(int j = col-1; j <= col+1; j++){
				if(i >= 0 && i <= size-1 && j >= 0 && j <= size-1 && !(isMine(i*size+j)) && !(isOpen(i*size+j))){
					map[i][j] = grid[i][j];
					if(grid[i][j] == '0')
					findAllZeros(i,j);
				}

			}
		}
    }

    /**
     * Updates the state of the game
     */

    public int gameStatus(int stat){ 
    	if(stat == 0){ // runs only if player hasn't hit a mine
    		int correctBoxes = 0;
    		for(int i = 0; i < size;i++){
    			for(int j = 0; j < size; j++){
    				if(( isMine(i*size+j) && isFlag(i*size+j))||(grid[i][j] == map[i][j]))
    					correctBoxes++; //the map has the correct move for that cell
    				if (map[i][j]=='X'){
    					stat=-1;
    					break;
    				}
    			}
    		}
    		if(stat==0){
    			if(correctBoxes == size*size) //all correct moves have been made
    				stat=1;
    		}
    	}
	return stat;
    }

	/**
	 * displays the value of the grid (grid contains all the mines only) on the GUI
	 */


	/*
	public char[][] displayMines(int size) {
		for (int i = size - 1; i <= size + 1; i++) {
			for (int j = size - 1; j <= size + 1; j++) {
				if (isMine(map[i][j]))
					map[i][j] = grid[i][j];
			}
		}
		return map;
	}
	*/


	/**
     * Finds the current condition of a cell
     */

     public char getCell(int cell){
	 return map[cell/size][cell%size];   
     }
    char[][] getG(){
        return grid;
    }
    char[][] getM(){
        return map;
    }

}

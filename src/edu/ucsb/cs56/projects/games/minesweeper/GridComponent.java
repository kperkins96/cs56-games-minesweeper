package edu.ucsb.cs56.projects.games.minesweeper;

import java.io.Serializable;

/**
 * Created by ryanwiener on 11/4/17.
 */

public class GridComponent implements Serializable {

    private boolean isOpen;
    private boolean isFlagged;
    private char symbol;

    public GridComponent() {
        isOpen = false;
        isFlagged = false;
        symbol = '0';
    }

    public void iterate() {
        if (symbol != 'X') {
            symbol++;
        }
    }

    public boolean makeMine() {
        if (symbol == 'X') {
            return false;
        }
        symbol = 'X';
        return true;
    }

    public void open() {
        if (!isFlagged) {
            isOpen = true;
        }
    }

    public void setFlagged(boolean val) {
        if (!isOpen) {
            isFlagged = val;
        }
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public boolean getIsFlagged() {
        return isFlagged;
    }

    public boolean getIsMine() {
        return symbol == 'X';
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        if (isOpen) {
            return Character.toString(symbol);
        } else if (isFlagged) {
            return "F";
        } else {
            return "?";
        }
    }
}

package com.javarush.games.minesweeper;
import com.javarush.engine.cell.*;

public class GameObject {

    public int x;
    public int y;
    public boolean isMine;
    public int countMineNeighbors;
    public boolean isOpen;
    public boolean isFlag;

    public GameObject(int _x, int _y, boolean _isMine) {
        x = _x;
        y = _y;
        isMine = _isMine;
    }
}

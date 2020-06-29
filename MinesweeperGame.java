package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField = 0;
    private static final String MINE = "\uE022";
    private static final String FLAG = 	"\u2623";
    private int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles = SIDE*SIDE;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(100) < 15;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[x][y] = new GameObject(y, x, isMine);
                setCellColor(x, y, Color.PINK);
                setCellValue(x, y, "");
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
        score = 0;
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void countMineNeighbors() {
        List<GameObject> _Neighbors;
        for (int i = 0; i < SIDE; ++i){
            for (int j = 0; j < SIDE; ++j) {
                if (!gameField[i][j].isMine) {
                    gameField[i][j].countMineNeighbors = 0;
                    _Neighbors = getNeighbors(gameField[i][j]);
                    for (GameObject _neighbor: _Neighbors) {
                        if (_neighbor.isMine) {
                            ++gameField[i][j].countMineNeighbors;
                        }
                    }
                }
            }
        }
    }

    private void openTile(int x, int y) {
        //Color _custom = new Color(255, 153, 153);
        if (!isGameStopped && !gameField[y][x].isOpen && !gameField[y][x].isFlag) {
            gameField[y][x].isOpen = true;
            --countClosedTiles;
            if (gameField[y][x].isMine) {
                setCellValueEx(x, y, Color.RED, MINE);
                gameOver();
            } else {
                if (gameField[y][x].countMineNeighbors == 0) {
                    setCellValue(x, y, "");
                    for (GameObject obj : getNeighbors(gameField[y][x])) {
                        if (!obj.isOpen) {
                            openTile(obj.x, obj.y);
                        }
                    }
                } else {
                    setCellNumber(x, y, gameField[y][x].countMineNeighbors);
                }
                //setCellColor(x, y, _custom);
                setCellColor(x, y, Color.GRAY);
                score += 5;
                setScore(score);
                if (countMinesOnField == countClosedTiles) {
                    win();
                }
            }
        }
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped) {
            restart();
        }
        else {
                openTile(x, y);
            }
    }

    private void markTile(int x, int y) {
        if (!isGameStopped) {
            if (!gameField[y][x].isOpen) {
                if (gameField[y][x].isFlag) {
                    gameField[y][x].isFlag = false;
                    setCellValue(x, y, "");
                    setCellColor(x, y, Color.PINK);
                    ++countFlags;
                } else {
                    if (countFlags != 0) {
                        gameField[y][x].isFlag = true;
                        setCellValue(x, y, FLAG);
                        setCellColor(x, y, Color.GRAY);
                        --countFlags;
                    }
                }
            }
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.PINK, "Y0U L0SE. I L0VE Y0U ANYWAY", Color.RED, 25);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.PINK, "YOU'VE PASSED. I L0VE Y0U", Color.RED, 25);
    }

    private void restart() {
        isGameStopped = false;
        countClosedTiles = SIDE*SIDE;
        score = 0;
        countMinesOnField = 0;
        setScore(score);
        createGame();
    }
}





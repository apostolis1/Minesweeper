import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    int gridSize, numberOfMoves, numberOfMines;
    boolean hasSuperMine;
    TileInternal [][] grid;

    public Game(int gridSize, int numberOfMines, boolean hasSuperMine) {
        this.gridSize = gridSize;
        this.numberOfMoves = 0;
        this.numberOfMines = numberOfMines;
        this.hasSuperMine = hasSuperMine;
        this.grid = new TileInternal[this.gridSize][this.gridSize];

        ArrayList<Integer> allIndices = new ArrayList<>();
        for (int i =0 ; i< this.gridSize; i++) {
            for (int j=0; j<this.gridSize; j++) {
                allIndices.add(i*this.gridSize + j);
            }
        }

        Collections.shuffle(allIndices);
        System.out.println(allIndices.toString());
        ArrayList<Integer> mineIndices = new ArrayList<>();

        // Pick the first numberOfMines indices and mark them as mines
        // Mark the first one as a supermine if the game contains one
        // Due to shuffling, the indices are random
        for (int i=0; i<numberOfMines; ++i)
            mineIndices.add(allIndices.get(i));
        int superMinePosition = -1 ;
        if (this.hasSuperMine) {
            superMinePosition = mineIndices.get(0);
        }
        for (int i =0; i< this.gridSize; ++i) {
            for (int j = 0; j < this.gridSize; ++j) {
                boolean hasMine = mineIndices.contains(i*this.gridSize + j);
                boolean isSuperMine = (superMinePosition == i*this.gridSize + j);
                TileInternal tile = new TileInternal(i, j, hasMine, isSuperMine);
                grid[i][j] = tile;
            }
        }
    }

    void tileLeftClicked(int x, int y) {

    }

    void tileRightClicked(int x, int y) {
        grid[x][y].rightClicked();
    }

    boolean isTileMine(int x, int y) {
        return this.grid[x][y].getMine();
    }

    boolean isTileSuperMine(int x, int y) {
        return this.grid[x][y].getSuperMine();
    }

    public ArrayList<TileInternal> getNeighborsByCoordinates(int x, int y) {
        ArrayList<TileInternal> result = new ArrayList<>();
        TileInternal temp;
        // Get elements of above row
        temp = getTileByCoordinates(x-1,y-1);
        if (temp != null)
            result.add(temp);
        temp = getTileByCoordinates(x-1,y);
        if (temp != null)
            result.add(temp);
        temp = getTileByCoordinates(x-1,y+1);
        if (temp != null)
            result.add(temp);
        // Get elements of same row
        temp = getTileByCoordinates(x,y-1);
        if (temp != null)
            result.add(temp);
        temp = getTileByCoordinates(x,y+1);
        if (temp != null)
            result.add(temp);
        // Get elements of next row
        temp = getTileByCoordinates(x+1,y-1);
        if (temp != null)
            result.add(temp);
        temp = getTileByCoordinates(x+1,y);
        if (temp != null)
            result.add(temp);
        temp = getTileByCoordinates(x+1,y+1);
        if (temp != null)
            result.add(temp);
        return result;
    }

    public TileInternal getTileByCoordinates(int x, int y) {
        if (x < 0 | x >= this.gridSize | y < 0 | y>= this.gridSize)
            return null;
        return this.grid[x][y];
    }
}

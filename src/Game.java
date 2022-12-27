import reader.Description;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    int gridSize, numberOfMoves, numberOfMines;
    boolean hasSuperMine;
    final String minesTextLocation = "/home/apostolis/Apostolis/Shmmy/multimedia/MinesweeperJava/medialab/mines.txt";
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
        // Write mine locations to file
        writeMineLocations(mineIndices, superMinePosition);
        for (int i =0; i< this.gridSize; ++i) {
            for (int j = 0; j < this.gridSize; ++j) {
                boolean hasMine = mineIndices.contains(i*this.gridSize + j);
                boolean isSuperMine = (superMinePosition == i*this.gridSize + j);
                TileInternal tile = new TileInternal(i, j, hasMine, isSuperMine);
                grid[i][j] = tile;
            }
        }
        for (int i =0; i< this.gridSize; ++i) {
            for (int j = 0; j < this.gridSize; ++j) {
                ArrayList<TileInternal> neighbors = getNeighborsByCoordinates(i, j);
                grid[i][j].neighborMines = this.countMines(neighbors);
            }
        }
    }

    public Game(Description description) {
        this(description.getGridSize(), description.getNumberOfMines(), (description.getSuperMine() == 1));
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

    public void revealTileRecursive(int x, int y) {
        TileInternal t = getTileByCoordinates(x, y);
        if (t.getMine()) {
            // the user lost
//            this.game.gameLoss();
            return ;
        }
        ArrayList<TileInternal> neighbors = getNeighborsByCoordinates(x, y);
        t.setRevealed(true);
        if (t.neighborMines == 0) {
            for (TileInternal neighbor : neighbors) {
                // If the user has incorrectly set a flag, we don't want to give him the information that
                // the tile can be revealed
                if (!neighbor.getRevealed() && !neighbor.getFlagSet() && !neighbor.getMine())
                    this.revealTileRecursive(neighbor.x, neighbor.y);
            }
        }
    }

    public int countMines(ArrayList<TileInternal> neighbors) {
        int minesInNeighbors = 0;
        for (TileInternal t : neighbors) {
            if (t.getMine())
                minesInNeighbors++;
        }
        return minesInNeighbors;
    }

    public void gameLoss() {
        //Dump stats
        System.out.println("Internal game loss");
    }

    public void gameWin() {
        //Dump stats
        System.out.println("Internal game win");
    }

    public boolean isGameWon() {
//        Returns true if the game is already won by the player
        // Meaning all non mine tiles are revealed

        for (int i =0; i< this.gridSize; ++i) {
            for (int j = 0; j < this.gridSize; ++j) {
                TileInternal t = this.grid[i][j];
                System.out.print(t);
                System.out.print(t.getRevealed());
                System.out.println(t.getMine());
                if (!t.getRevealed() && !t.getMine())
                    return false;
            }
        }
        return true;
    }

    public void writeMineLocations(ArrayList<Integer> mineIndices, int superMinePosition) {
        try {
            FileWriter myWriter = new FileWriter(minesTextLocation);
            for (int mineIndex : mineIndices) {
                System.out.printf("Writing to file %d%n", mineIndex);
                int row = mineIndex / this.gridSize;
                int col = mineIndex % this.gridSize;
                int isSuperMine = (mineIndex == superMinePosition) ? 1 : 0;
                myWriter.write(String.format("%d,%d,%d%n", row, col, isSuperMine));
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public int getNumberOfFlagsSet() {
        int result = 0;
        for (int i =0; i< this.gridSize; ++i) {
            for (int j = 0; j < this.gridSize; ++j) {
                TileInternal t = this.grid[i][j];
                if (t.getFlagSet())
                        result++;
            }
        }
        return result;
    }
}

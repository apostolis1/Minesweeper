import java.util.ArrayList;
import java.util.Collections;

public class Game {
    int gridSize, numberOfMoves, numberOfMines;
    boolean hasSuperMine;
    int [][] grid;

    public Game(int gridSize, int numberOfMines, boolean hasSuperMine) {
        this.gridSize = gridSize;
        this.numberOfMoves = 0;
        this.numberOfMines = numberOfMines;
        this.hasSuperMine = hasSuperMine;

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

    }

    void tileClicked(int x, int y) {

    }

    boolean isTileMine() {

    }

    boolean isTileSuperMine() {

    }

    void
}

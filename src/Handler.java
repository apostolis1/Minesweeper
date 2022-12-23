import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class Handler implements EventHandler<MouseEvent>{
//
    public Game game;
    public Handler(Game g) {
        this.game = g;
    }

    @Override
    public void handle(MouseEvent event) {
        if (!(event.getTarget() instanceof Tile))
            return;

        Tile tileClicked = (Tile) event.getTarget();

        System.out.println(tileClicked.toString());
        if (tileClicked.isRevealed)
            return;
        if (event.getButton() == MouseButton.SECONDARY) {
            System.out.println("Right click");
            tileClicked.rightClicked();
        }
        else {
            System.out.println("Left Click");
            System.out.println(tileClicked.x);
            System.out.println(tileClicked.y);
            revealTileRecursive(tileClicked);
//            After all tiles are revealed recursively, check if the game is won
            if (this.game.isGameWon())
                this.game.gameWin();
        }
    }

    public void revealTileRecursive(Tile t) {
        if (t.isMine) {
        // the user lost
            this.game.gameLoss();
            return ;
        }
        ArrayList<Tile> neighbors = this.game.getNeighborsByCoordinates(t.x, t.y);
        Integer minesInNeighbors = this.countMines(neighbors);
        t.reveal(minesInNeighbors);
        if (minesInNeighbors == 0) {
            for (Tile neighbor : neighbors) {
                // If the user has incorrectly set a flag, we don't want to give him the information that
                // the tile can be revealed
                if (!neighbor.isRevealed && !neighbor.flagSet && !neighbor.isMine)
                    this.revealTileRecursive(neighbor);
            }
        }
    }
    public Integer countMines(ArrayList<Tile> tiles) {
        Integer minesInNeighbors = 0;
        for (Tile t : tiles) {
            if (t.isMine)
                minesInNeighbors++;
        }
        return minesInNeighbors;
    }

}

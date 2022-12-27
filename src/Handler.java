import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class Handler implements EventHandler<MouseEvent>{
//
    public Game game;
    public GameGui gameGui;
    public Handler(Game g, GameGui gameGui) {
        this.game = g;
        this.gameGui = gameGui;
    }

    @Override
    public void handle(MouseEvent event) {
        if (!(event.getTarget() instanceof Tile))
            return;

        Tile tileClicked = (Tile) event.getTarget();

        System.out.println(tileClicked.toString());
        // Avoid any updates if the tile is already revealed
        if (this.game.grid[tileClicked.x][tileClicked.y].getRevealed())
            return;
        if (event.getButton() == MouseButton.SECONDARY) {
            // Right click
            System.out.println("Right click");
            this.game.tileRightClicked(tileClicked.x, tileClicked.y);
            this.gameGui.updateGridFromGame();
        }
        else {
            // Left click
            System.out.println("Left Click");
            System.out.println(tileClicked.x);
            System.out.println(tileClicked.y);
//            Check if the user clicked on a mine
            if (game.isTileMine(tileClicked.x, tileClicked.y)) {
                game.gameLoss();
                gameGui.gameLoss();
                return ;
            }
            this.game.revealTileRecursive(tileClicked.x, tileClicked.y);
            this.gameGui.updateGridFromGame();
//            revealTileRecursive(tileClicked);
//            After all tiles are revealed recursively, check if the game is won
            if (this.game.isGameWon()) {
                this.game.gameWin();
                this.gameGui.gameWin();
            }
        }
    }
}

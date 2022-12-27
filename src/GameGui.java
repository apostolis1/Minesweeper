import exception.InvalidDescriptionException;
import exception.InvalidValueException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import reader.Description;
import reader.Reader;

public class GameGui extends Application{
    GridPane grid, informationPane;
    BorderPane mainPane;
    Game internalGame;
    Description gameDescription;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Minesweeper");
        VBox applicationVBox = new VBox();
        getGameDescription();
        this.mainPane = new BorderPane();
        this.informationPane = getInformationPane();
        startNewGame();
        MenuBar mBar = getMenuBar();
        applicationVBox.getChildren().addAll(mBar, informationPane, this.mainPane);
        Scene scene = new Scene(applicationVBox, 9*50, 600);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane getInformationPane() {
        GridPane infoPane = new GridPane();

        Label label1 = new Label("Mines in Game");
        infoPane.add(label1, 0,0);
        Label label2 = new Label("0");
        infoPane.add(label2, 1,0);

        Label labelTime = new Label("Time Remaining (secs)");
        infoPane.add(labelTime, 0,1);
        Label timeValue = new Label("0");
        infoPane.add(timeValue, 1, 1);

        Label flagsLabel = new Label("Flags placed");
        infoPane.add(flagsLabel, 0,2);
        Label flagsValue = new Label("0");
        infoPane.add(flagsValue, 1,2);
        infoPane.setStyle("-fx-background-color: " + "green" + ";");
        return infoPane;
    }

    private MenuBar getMenuBar() {
        MenuBar mb = new MenuBar();
        Menu applicationMenu = new Menu("Application");
        // create menu items
        MenuItem m1 = new MenuItem("Create");
        m1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startNewGame();
            }
        });
        MenuItem m2 = new MenuItem("Load");
        m2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getGameDescription();
            }
        });
        MenuItem m3 = new MenuItem("Start");

        // add menu items to menu
        applicationMenu.getItems().add(m1);
        applicationMenu.getItems().add(m2);
        applicationMenu.getItems().add(m3);

        Menu detailsMenu = new Menu("Details");
        MenuItem b1 = new MenuItem("Rounds");
        MenuItem b2 = new MenuItem("Solution");
        detailsMenu.getItems().add(b1);
        detailsMenu.getItems().add(b2);

        mb.getMenus().add(applicationMenu);
        mb.getMenus().add(detailsMenu);
        return mb;
    }

    public Tile getTileByCoordinates(int x, int y) {
        for (Node tile : this.grid.getChildren()) {
            Tile t = (Tile) tile;
            if (t.x == x && t.y == y)
                return t;
        }
        return null;
    }


    public void gameLoss() {
        //Creating a dialog
        Dialog<String> dialog = new Dialog<String>();
        //Setting the title
        dialog.setTitle("You Lose!");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        //Setting the content of the dialog
        dialog.setContentText("You lost the game, try again!");
        //Adding buttons to the dialog pane
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
        startNewGame();
    }

    public void gameWin() {
        //Creating a dialog
        Dialog<String> dialog = new Dialog<String>();
        //Setting the title
        dialog.setTitle("You Win!");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        //Setting the content of the dialog
        dialog.setContentText("You win the game, play again!");
        //Adding buttons to the dialog pane
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
        startNewGame();
    }


    private void getGameDescription() {
        Reader reader = new Reader();
        Description description = null;
        try {
            description = reader.getFileContents("level_1_example.txt");
        } catch (InvalidDescriptionException | InvalidValueException e) {
            System.out.println("Popup will be created");
        }
        this.gameDescription = description;
    }

    private GridPane initGridFromGame() {
        // The first call that simply creates the grid pane according to the internal game object
        GridPane grid = new GridPane();
        grid.addEventFilter(MouseEvent.MOUSE_CLICKED, new Handler(this.internalGame, this));
        for (int i =0; i< this.internalGame.gridSize; ++i) {
            for (int j = 0; j < this.internalGame.gridSize; ++j) {
                Tile btn = new Tile("", i,j);

                grid.getChildren().add(btn);
                GridPane.setRowIndex(btn, i);
                GridPane.setColumnIndex(btn, j);
                System.out.println(GridPane.getRowIndex(btn));

            }
        }
        return grid;
    }

    public void startNewGame() {
//        Check description to see if it was loaded correctly
        if (this.gameDescription == null) {
            System.out.println("Please load a valid description and try again");
            return ;
        }
//        Create a new game for the given description
//        this.internalGame = new Game(10, 10, true);
        this.internalGame = new Game(gameDescription);
        grid = this.initGridFromGame();
        mainPane.setCenter(grid);
    }

    public void updateGridFromGame() {
        for (int i =0; i< this.internalGame.gridSize; ++i) {
            for (int j = 0; j < this.internalGame.gridSize; ++j) {
                Tile t = getTileByCoordinates(i, j);
                TileInternal tInternal = this.internalGame.getTileByCoordinates(i, j);
                t.updateTileFromInternal(tInternal);
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }


}

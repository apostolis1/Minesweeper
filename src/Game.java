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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import reader.Description;
import reader.Reader;

import java.util.ArrayList;
import java.util.Collections;

public class Game  extends Application{

    Integer size;
    GridPane grid, informationPane;
    BorderPane mainPane;
    Description gameDescription;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Minesweeper");
        GridPane applicationGrid = new GridPane();
        VBox applicationVBox = new VBox();
        getGameDescription();
        this.mainPane = new BorderPane();
        this.informationPane = getInformationPane();
        this.grid = getGridPane();
        MenuBar mBar = getMenuBar();
        this.mainPane.setCenter(this.grid);
//        applicationGrid.add(mBar, 0,0);
//        applicationGrid.add(informationPane, 0, 1);
//        applicationGrid.add(this.mainPane, 0, 2);
        applicationVBox.getChildren().addAll(mBar, informationPane, this.mainPane);
        for (int i=0; i< size; ++i) {
            ColumnConstraints tempConstraints = new ColumnConstraints();
            tempConstraints.setPercentWidth(100/ (float)size);
            this.grid.getColumnConstraints().add(tempConstraints);
        }

//        Scene scene = new Scene(applicationGrid, 600, 275);
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
        // create menuitems
        MenuItem m1 = new MenuItem("Create");
        m1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createNewGrid();
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

    private void createNewGrid() {
        grid = getGridPane();
        mainPane.setCenter(grid);
    }

    private GridPane getGridPane() {
        // Returns a new GridPane object that contains the visual information of the MineSweeper
        GridPane grid = new GridPane();
        grid.addEventFilter(MouseEvent.MOUSE_CLICKED, new Handler(this));
        this.size = 10;
        int numberOfMines = 10;
        ArrayList<Integer> allIndices = new ArrayList<>();
        for (int i =0 ; i< size; i++) {
            for (int j=0; j<size; j++) {
                allIndices.add(i*size + j);
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
        // Write mine locations in file mines.txt

        System.out.println(mineIndices.toString());
        for (int i =0; i< size; ++i) {
            for (int j = 0; j < size; ++j) {
                Tile btn = new Tile("", i,j);
                if (mineIndices.contains(i*this.size + j))
                    btn.setMine();
//                grid.add(btn, i, j);
                grid.getChildren().add(btn);
                GridPane.setRowIndex(btn, i);
                GridPane.setColumnIndex(btn, j);
                System.out.println(GridPane.getRowIndex(btn));

            }
        }
        return grid;
    }

    public Tile getTileByCoordinates(int x, int y) {
        for (Node tile : this.grid.getChildren()) {
            Tile t = (Tile) tile;
            if (t.x == x && t.y == y)
                return t;
        }
        return null;
    }

    public ArrayList<Tile> getNeighborsByCoordinates(int x, int y) {
        ArrayList<Tile> result = new ArrayList<>();
        Tile temp;
        // Get elemtns of above row
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
        createNewGrid();
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
        createNewGrid();
    }

    public boolean isGameWon() {
//        Returns true if the game is already won by the player
        // Meaning all non mine tiles are revealed
        for (Node tile : this.grid.getChildren()) {
            Tile t = (Tile) tile;
            System.out.print(t);
            System.out.print(t.isRevealed);
            System.out.println(t.isMine);
            if (!t.isRevealed && !t.isMine)
                return false;

        }
        return true;
    }

    private void getGameDescription() {
        Reader reader = new Reader();
        Description description = null;
        try {
            description = reader.getFileContents("invalid_range_example.txt");
        } catch (InvalidDescriptionException | InvalidValueException e) {
            System.out.println("Popup will be created");
        }
        this.gameDescription = description;
        return;
    }

    private void writeMines() {
        
    }
    public static void main(String[] args) {
        launch(args);
    }


}

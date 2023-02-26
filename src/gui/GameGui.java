package gui;

import exception.InvalidDescriptionException;
import exception.InvalidValueException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import reader.Description;
import reader.Reader;
import internal.Game;
import internal.TileInternal;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class GameGui extends Application{
    GridPane grid, informationPane;
    BorderPane mainPane;
    Label labelMines, labelFlags, labelTimeRemaining;
    Game internalGame;
    Timer secondsTimer;
    Description gameDescription;
    private final Integer INFO_PANE_HEIGHT = 75;
    private final Integer WIDTH = 4*144, HEIGHT = INFO_PANE_HEIGHT +WIDTH + 50;

    StatsManager statsManager;
    final String medialabLocation = "/home/apostolis/Apostolis/Shmmy/multimedia/MinesweeperJava/medialab/";
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Minesweeper");
        VBox applicationVBox = new VBox();
//        getGameDescription();
        this.statsManager = new StatsManager();
        this.mainPane = new BorderPane();
        this.informationPane = getInformationPane();
        startNewGame();
        MenuBar mBar = getMenuBar();
        this.informationPane.setMinHeight(INFO_PANE_HEIGHT);
        applicationVBox.getChildren().addAll(mBar, informationPane, this.mainPane);
        Scene scene = new Scene(applicationVBox, WIDTH, HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane getInformationPane() {
        GridPane infoPane = new GridPane();

        Label label1 = new Label("Mines in internal.Game");
        infoPane.add(label1, 0,0);
        labelMines = new Label("0");
        infoPane.add(labelMines, 1,0);

        Label labelTime = new Label("Time Remaining (secs)");
        infoPane.add(labelTime, 0,1);
        labelTimeRemaining = new Label("0");
        infoPane.add(labelTimeRemaining, 1, 1);

        Label flagsLabel = new Label("Flags placed");
        infoPane.add(flagsLabel, 0,2);
        labelFlags = new Label("0");
        infoPane.add(labelFlags, 1,2);
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
//                startNewGame();
                createDescriptionDialog();
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
        m3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startNewGame();
            }
        });
        // add menu items to menu
        applicationMenu.getItems().add(m1);
        applicationMenu.getItems().add(m2);
        applicationMenu.getItems().add(m3);

        Menu detailsMenu = new Menu("Details");
        MenuItem b1 = new MenuItem("Rounds");
        MenuItem b2 = new MenuItem("Solution");

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showDetailsPopup();
            }
        });
        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Return if the game has not yet started
                if (internalGame == null)
                    return ;
                if (secondsTimer != null)
                    secondsTimer.cancel();
                internalGame.revealSolution();
                revealSolution();
            }

        });

        detailsMenu.getItems().add(b1);
        detailsMenu.getItems().add(b2);

        mb.getMenus().add(applicationMenu);
        mb.getMenus().add(detailsMenu);
        return mb;
    }

    private void createDescriptionDialog() {
        Dialog<Description> dialog = new Dialog<>();
        dialog.setTitle("Create Scenario");
        dialog.setHeaderText("This is a custom dialog. Enter info and \n" +
                "press Okay (or click title bar 'X' for cancel).");
        dialog.setResizable(true);

        Label label1 = new Label("SCENARIO-ID (filename will be for example SCENARIO-1.txt): ");
        Label label2 = new Label("Level (1 or 2): ");
        Label label3 = new Label("Number of mines: ");
        Label label4 = new Label("Supermine in game (0 or 1): ");
        Label label5 = new Label("Time in seconds: ");

        TextField textScenarioId = new TextField();
        TextField textLevel = new TextField();
        TextField textNumberOfMines = new TextField();
        TextField textSupermine = new TextField();
        TextField textTime = new TextField();

        textScenarioId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textScenarioId.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        textLevel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textLevel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        textNumberOfMines.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textNumberOfMines.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        textSupermine.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textSupermine.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        textTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textTime.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(textScenarioId, 2, 1);

        grid.add(label2, 1, 2);
        grid.add(textLevel, 2, 2);

        grid.add(label3, 1, 3);
        grid.add(textNumberOfMines, 2, 3);

        grid.add(label4, 1, 4);
        grid.add(textSupermine, 2, 4);

        grid.add(label5, 1, 5);
        grid.add(textTime, 2, 5);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, Description>() {
            @Override
            public Description call(ButtonType b) {

                if (b == buttonTypeOk) {

                    try {
                        return new Description(textLevel.getText(), textNumberOfMines.getText(), textTime.getText(), textSupermine.getText());
                    } catch (InvalidValueException e) {
                        // We could handle it here, but the requirements are that the error is handled when the
                        // description is loaded, so we will just ignore the exception here and handle it when
                        // loading the txt file
                        System.out.println("Bad Description Created");
                    }
                }

                return null;
            }
        });

        Optional<Description> result = dialog.showAndWait();
        // No matter what happened, we want to write the values in the txt file as explained above
        try {
            String scenarioLocation = medialabLocation + "SCENARIO-" + textScenarioId.getText() + ".txt";
            FileWriter myWriter = new FileWriter(scenarioLocation);
            myWriter.write(String.format("%s%n%s%n%s%n%s", textLevel.getText(), textNumberOfMines.getText(), textTime.getText(), textSupermine.getText()));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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
        // Stop the timer
        if (secondsTimer != null)
            secondsTimer.cancel();

        statsManager.addStats(internalGame.getStatsFromGame(false));
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
        // Stop the timer
        if (secondsTimer != null)
            secondsTimer.cancel();
        statsManager.addStats(internalGame.getStatsFromGame(true));
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

    private void showDetailsPopup() {
        LinkedList<Stats> mostRecentStats = statsManager.getMostRecentStats();
        for (Stats s:mostRecentStats)
            System.out.println(s);

        Dialog<Object> dialog = new Dialog<>();
        dialog.setTitle("Details");
        dialog.setResizable(false);

        TableView<Stats> table = new TableView<Stats>();

        TableColumn<Stats, Integer> minesCol = new TableColumn<>("Number of mines");
        minesCol.setCellValueFactory(
                new PropertyValueFactory<Stats, Integer>("mines"));
        minesCol.setMinWidth(150);

        TableColumn<Stats, Integer> triesCol = new TableColumn<>("Number of tries");
        triesCol.setCellValueFactory(
                new PropertyValueFactory<Stats, Integer>("tries"));
        triesCol.setMinWidth(150);

        TableColumn<Stats, Integer> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(
                new PropertyValueFactory<Stats, Integer>("time"));
        timeCol.setMinWidth(100);

        TableColumn<Stats, String> winnerCol = new TableColumn<>("Winner");
        winnerCol.setCellValueFactory(
                new PropertyValueFactory<Stats, String>("playerWonString"));
        winnerCol.setMinWidth(150);
        table.getColumns().addAll(minesCol, triesCol, timeCol, winnerCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        mostRecentStats.forEach(s -> table.getItems().add(s));
        dialog.getDialogPane().setContent(table);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.showAndWait();
    }

    private void getGameDescription() {
        // Create a text input dialog to get the SCENARIO-ID
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Load Game Description");
        td.setHeaderText("Provide the ID of the scenario you want to play. Will search for the file named SCENARIO-{ID} in the predefined medialab folder");
        td.getDialogPane().setMaxWidth(500);
        td.showAndWait();
        String scenarioId = td.getEditor().getText();
        System.out.printf("User provided %s ID%n", scenarioId);
        Reader reader = new Reader();
        Description description = null;
        try {
            String filename = String.format("SCENARIO-%s.txt", scenarioId);
            description = reader.getFileContents(filename);
        } catch (InvalidDescriptionException | InvalidValueException e) {
            System.out.println("Popup will be created");
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Error");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            //Setting the content of the dialog
            String ContentText = String.format("There is an error with the scenario ID (%s) you tried to load!", scenarioId);
            dialog.setContentText(ContentText);
            //Adding buttons to the dialog pane
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.getDialogPane().setMinSize(100,100);
            dialog.showAndWait();
        }
        this.gameDescription = description;
    }

    private GridPane initGridFromGame() {
        // The first call that simply creates the grid pane according to the internal game object
        GridPane grid = new GridPane();
        grid.addEventFilter(MouseEvent.MOUSE_CLICKED, new Handler(this.internalGame, this));

        // Calculate size of tile, based on how many we want to fit
        int tileSize = WIDTH / this.internalGame.getGridSize();

        for (int i =0; i< this.internalGame.getGridSize(); ++i) {
            for (int j = 0; j < this.internalGame.getGridSize(); ++j) {
                Tile btn = new Tile("", i,j, tileSize);

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
//        this.internalGame = new internal.Game(10, 10, true);
        this.internalGame = new Game(gameDescription);
        if (secondsTimer != null)
            secondsTimer.cancel();
        secondsTimer = new Timer();
        secondsTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Check if timer is zero, then the user loses
                    if (internalGame.getSecondsRemaining() == 0) {
                        internalGame.gameLoss();
                        gameLoss();
                        return ;
                    }
                    internalGame.decreaseSeconds();
                    updateInfoPanelFromGame();
                });
            }
        }, 1000, 1000);
        grid = this.initGridFromGame();
        mainPane.setCenter(grid);
        this.updateInfoPanelFromGame();
    }

    public void updateGridFromGame() {
        for (int i =0; i< this.internalGame.getGridSize(); ++i) {
            for (int j = 0; j < this.internalGame.getGridSize(); ++j) {
                Tile t = getTileByCoordinates(i, j);
                TileInternal tInternal = this.internalGame.getTileByCoordinates(i, j);
                t.updateTileFromInternal(tInternal);
            }
        }
    }

    @Override
    public void stop() {
        // Override the method to close our timer if the timer exists
        if (secondsTimer != null)
            secondsTimer.cancel();
    }
    public static void main(String[] args) {
        launch(args);
    }


    public void updateInfoPanelFromGame() {
        // Update the label for flags
        int flagsSet = this.internalGame.getNumberOfFlagsSet();
        this.labelFlags.setText(Integer.toString(flagsSet));
        // Update number of mines in current game
        this.labelMines.setText(Integer.toString(internalGame.getNumberOfMines()));
        // Update time remaining
        this.labelTimeRemaining.setText((Integer.toString(internalGame.getSecondsRemaining())));
    }

    private void revealSolution() {
        this.updateGridFromGame();
        this.gameLoss();
    }
}

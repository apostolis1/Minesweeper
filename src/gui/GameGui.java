package gui;

import exception.InvalidDescriptionException;
import exception.InvalidValueException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import reader.Description;
import reader.Reader;
import internal.Game;
import internal.TileInternal;
import sun.security.krb5.internal.crypto.Des;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class GameGui extends Application{
    GridPane grid, informationPane;
    BorderPane mainPane;
    Label labelMines, labelFlags, labelTimeRemaining;
    Game internalGame;
    Description gameDescription;
    final String medialabLocation = "/home/apostolis/Apostolis/Shmmy/multimedia/MinesweeperJava/medialab/";
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Minesweeper");
        VBox applicationVBox = new VBox();
//        getGameDescription();
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

        TextField text1 = new TextField();
        TextField text2 = new TextField();
        TextField text3 = new TextField();
        TextField text4 = new TextField();
        TextField text5 = new TextField();

        text1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    text1.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        text2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    text2.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        text3.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    text3.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        text4.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    text4.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        text5.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    text5.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);

        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);

        grid.add(label3, 1, 3);
        grid.add(text3, 2, 3);

        grid.add(label4, 1, 4);
        grid.add(text4, 2, 4);

        grid.add(label5, 1, 5);
        grid.add(text5, 2, 5);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, Description>() {
            @Override
            public Description call(ButtonType b) {

                if (b == buttonTypeOk) {

                    try {
                        return new Description(text2.getText(), text3.getText(), text5.getText(), text4.getText());
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
            String scenarioLocation = medialabLocation + "SCENARIO-" + text1.getText() + ".txt";
            FileWriter myWriter = new FileWriter(scenarioLocation);
            myWriter.write(String.format("%s%n%s%n%s%n%s", text2.getText(), text3.getText(), text4.getText(), text5.getText()));
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
        // Create a text input dialog to get the SCENARIO-ID
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Provide the ID of the scenario you want to play. Will search for the file named SCENARIO-{ID} in the predefined medialab folder");
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
            dialog.setContentText("There is an error with the scenario ID you tried to load!");
            //Adding buttons to the dialog pane
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        }
        this.gameDescription = description;
    }

    private GridPane initGridFromGame() {
        // The first call that simply creates the grid pane according to the internal game object
        GridPane grid = new GridPane();
        grid.addEventFilter(MouseEvent.MOUSE_CLICKED, new Handler(this.internalGame, this));
        for (int i =0; i< this.internalGame.getGridSize(); ++i) {
            for (int j = 0; j < this.internalGame.getGridSize(); ++j) {
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
//        this.internalGame = new internal.Game(10, 10, true);
        this.internalGame = new Game(gameDescription);
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
    public static void main(String[] args) {
        launch(args);
    }


    public void updateInfoPanelFromGame() {
        // Update the label for flags
        int flagsSet = this.internalGame.getNumberOfFlagsSet();
        this.labelFlags.setText(Integer.toString(flagsSet));
        // Update number of mines in current game
        this.labelMines.setText(Integer.toString(internalGame.getNumberOfMines()));
    }
}

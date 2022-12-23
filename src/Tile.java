import javafx.scene.control.Button;

public class Tile extends Button {

    private Integer state;
    public int x,y;
    public Boolean isMine, isSuperMine, flagSet, isRevealed;
    private final String flagUrl = "https://icons.iconarchive.com/icons/flaticonmaker/flat-style/256/flag-icon.png";
    private final String mineUrl = "https://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/256/Actions-edit-bomb-icon.png";
    public Tile(String text, int x, int y) {
        super(text);
        this.flagSet = false;
        this.isRevealed = false;
        this.state = 1;
        this.x = x;
        this.y = y;
        this.isMine = false; // By default it is not a mine
        this.isSuperMine = false;
        this.setMinSize(50,50);
//        this.changeStyle();
//        this.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                onPressed();
//            }
//        });
//        this.setOnMouseClicked(h);
    }
    public void changeStyle () {
        this.setStyle(
            "-fx-background-image: url('http://icons.iconarchive.com/icons/aha-soft/desktop-buffet/128/Piece-of-cake-icon.png');"
            + "-fx-background-size: 20px 20px;"
            + "-fx-background-position: center;"
            + "-fx-background-repeat: no-repeat;"
        );
    }

    public void setBackgroundReveal(){
        String color = "pink";
        this.setStyle(
            "-fx-background-color: " + color + ";"
        );
    }
    public void setIcon(String url) {
        this.setStyle(
            "-fx-background-image: url('"
            + url
            + "');"
            + "-fx-background-size: 20px 20px;"
            + "-fx-background-position: center;"
            + "-fx-background-repeat: no-repeat;"
        );
    }


    public void setMine() {
        this.isMine = true;
    }

    public void reveal(Integer minesInNeighbors) {
        if (this.isRevealed)
            return;
        this.setBackgroundReveal();
        this.isRevealed = true;
        if (this.isMine) {
            this.setIcon(mineUrl);
            System.out.println("You lose");
        }
        else {
            System.out.println("Not a mine");
            // Don't set the label to 0 for better visual
            if (minesInNeighbors != 0)
                this.setText(minesInNeighbors.toString());
        }
    }

    public void rightClicked() {
        if (!this.flagSet) {
            this.setIcon(flagUrl);
            this.flagSet = true;
        }
        else {
            this.setStyle(null);
            this.flagSet = false;
        }
    }
}

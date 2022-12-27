package gui;

import internal.TileInternal;
import javafx.scene.control.Button;

public class Tile extends Button {

    public int x,y;
    private final String flagUrl = "https://icons.iconarchive.com/icons/flaticonmaker/flat-style/256/flag-icon.png";
    private final String mineUrl = "https://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/256/Actions-edit-bomb-icon.png";
    public Tile(String text, int x, int y) {
        super(text);
        this.x = x;
        this.y = y;
        this.setMinSize(50,50);
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


    public void updateTileFromInternal(TileInternal tInternal) {
        if (tInternal.getRevealed()) {
            setBackgroundReveal();
            // Revealed mines should have their icon set to mineUrl
            // Mines can be revealed without the game ending in case of a supermine
            if (tInternal.getMine())
                this.setIcon(mineUrl);
            else if (tInternal.neighborMines != 0) {
                String numberToDisplay = Integer.toString(tInternal.neighborMines);
                setText(numberToDisplay);
            }

            return;
        }
        if (tInternal.getFlagSet()) {
            this.setIcon(flagUrl);
        }
        else { // remove the flag icon
            this.setStyle(null);
        }
    }
}

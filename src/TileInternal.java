public class TileInternal {
    // This is the internal representation for the Tiles
    // It is used to separate the backend logic from the frontend

    public int x,y, neighborMines;
    private Boolean isMine, isSuperMine, flagSet, isRevealed;

    public TileInternal(int x, int y, Boolean isMine, Boolean isSuperMine) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
        this.isSuperMine = isSuperMine;
        this.flagSet = false;
        this.isRevealed = false;
    }

    public Boolean getRevealed() {
        return isRevealed;
    }

    public void setRevealed(Boolean revealed) {
        isRevealed = revealed;
    }

    public Boolean getMine() {
        return isMine;
    }

    public void setMine(Boolean mine) {
        isMine = mine;
    }

    public Boolean getFlagSet() {
        return flagSet;
    }

    public void setFlagSet(Boolean flagSet) {
        this.flagSet = flagSet;
    }

    public Boolean getSuperMine() {
        return isSuperMine;
    }

    public void rightClicked() {
        this.flagSet = !this.flagSet;
    }
}

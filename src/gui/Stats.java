package gui;

public class Stats {
    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isPlayerWon() {
        return playerWon;
    }

    public void setPlayerWon(boolean playerWon) {
        this.playerWon = playerWon;
    }

    int mines, tries, time;

    public String getPlayerWonString() {
        return playerWonString;
    }

    private String playerWonString;
    boolean playerWon;

    public Stats(int mines, int tries, int time, boolean playerWon) {
        this.mines = mines;
        this.tries = tries;
        this.time = time;
        this.playerWon = playerWon;
        this.playerWonString = getPlayerWon();

    }

    private String getPlayerWon() {
        if (playerWon)
            return "Player";
        return "CPU";
    }

    @Override
    public String toString() {
        return String.format("%d %d %d %s", mines, tries, time, getPlayerWon());
    }
}

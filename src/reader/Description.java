package reader;

import exception.InvalidValueException;

public class Description {
//    This could also be an Interface with 2 different Classes that implement it, but for
//    such a small Class it is not worth it, the logic is quite simple and can be included in one Class
    private final Integer level, size, numberOfMines, time, superMine;

    public Description(String level, String numberOfMines, String time, String superMine) throws InvalidValueException {
        try {
            this.level = Integer.parseInt(level);
            this.numberOfMines = Integer.parseInt(numberOfMines);
            this.time = Integer.parseInt(time);
            this.superMine = Integer.parseInt(superMine);
        }
        catch (NumberFormatException e) {
            throw new InvalidValueException();
        }
        if (this.level == 1)
            this.size = 9;
        else if (this.level == 2)
            this.size = 16;
        else throw new InvalidValueException();
    }

    public void checkParameters() throws InvalidValueException {
        if (this.level == 1 && !(this.time >= 120 && this.time <= 180 && this.numberOfMines >= 9 && this.numberOfMines <= 11 && this.superMine == 0))
            throw new InvalidValueException();
        if (this.level == 2 && !(this.time >= 240 && this.time <= 360 && this.numberOfMines >= 35 && this.numberOfMines <= 45 && (this.superMine == 0 || this.superMine == 1)))
            throw new InvalidValueException();
    }
}

package reader;

import exception.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
//    A class to read contents from files and return a Description
    String DescriptionFolderPath = "/home/apostolis/Apostolis/Shmmy/multimedia/Minesweeper/medialab/";

    public Reader() {
        System.out.print(DescriptionFolderPath);
    }

    public Description getFileContents(String fileName) throws InvalidDescriptionException{
        String fullPath = DescriptionFolderPath + fileName;
        BufferedReader buffer;
        try {
            buffer = new BufferedReader(new FileReader(fullPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String level, numberOfMines, time, superMine;
        try {
            level = buffer.readLine();
            System.out.print(level);
            if (level == null)
                throw new RuntimeException();

            numberOfMines = buffer.readLine();
            System.out.print(numberOfMines);
            if (numberOfMines == null)
                throw new RuntimeException();

            time = buffer.readLine();
            System.out.print(time);
            if (time == null)
                throw new RuntimeException();

            superMine = buffer.readLine();
            System.out.print(superMine);
            if (superMine == null)
                throw new RuntimeException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Description description;
        try {
            description = new Description(level, numberOfMines, time, superMine);
            description.checkParameters();
        }
         catch (InvalidValueException e) {
            System.out.println("Invalid Value Exception Caught");
            throw new RuntimeException(e);
        }
        return description;
    }
}

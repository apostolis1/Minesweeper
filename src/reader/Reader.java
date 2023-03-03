package reader;

import exception.*;
import config.ConfigHandler;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
//    A class to read content from files and return a Description
    String DescriptionFolderPath;

    public Reader() {
        ConfigHandler ch = new ConfigHandler();
        DescriptionFolderPath = ch.getMedialabFolderPath();
        System.out.print(DescriptionFolderPath);
    }

    public Description getFileContents(String fileName) throws InvalidDescriptionException, InvalidValueException {
        String fullPath = DescriptionFolderPath + fileName;
        BufferedReader buffer;
        try {
            buffer = new BufferedReader(new FileReader(fullPath));
        } catch (FileNotFoundException e) {
            throw new InvalidValueException();
        }

        String level, numberOfMines, time, superMine;
        try {
            level = buffer.readLine();
            System.out.print(level);
            if (level == null)
                throw new InvalidValueException();

            numberOfMines = buffer.readLine();
            System.out.print(numberOfMines);
            if (numberOfMines == null)
                throw new InvalidValueException();

            time = buffer.readLine();
            System.out.print(time);
            if (time == null)
                throw new InvalidValueException();

            superMine = buffer.readLine();
            System.out.print(superMine);
            if (superMine == null)
                throw new InvalidValueException();
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
            throw new InvalidValueException();
        }
        return description;
    }
}

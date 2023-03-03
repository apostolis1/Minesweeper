package config;

import java.io.*;
import java.util.Properties;

public class ConfigHandler {

    String configFilePath = "src/config.properties";
    public String getMedialabFolderPath() {
        try {

            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);
            System.out.println(prop.getProperty("MEDIALAB_PATH"));
            return prop.getProperty("MEDIALAB_PATH");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return "" ;
    }
}


package com.prajwal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author prajwal.kulkarni on 06/05/21
 */
public class FileUtil {

    public static String readFileAsString(File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];
            StringBuilder stringBuilder = new StringBuilder();

            while ((read = inputStream.read(bytes)) != -1) {
                stringBuilder.append(new String(bytes));
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

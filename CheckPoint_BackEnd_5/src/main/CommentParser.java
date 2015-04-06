package main;

import java.io.*;

public class CommentParser {
    public static void main(String args[]) {
        parseComments(System.getProperty("user.dir") + "\\res\\src.java");
    }

    public static int parseComments(String path) {
        FileInputStream is;
        FileOutputStream os;
        try {
            is = new FileInputStream(path);
            os = new FileOutputStream(path.substring(0, path.length() - 5) + "_cleaned.java");
        } catch (FileNotFoundException e) {
            return -1;
        }
        try {
            if (is.available() == 0)
                return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        char ch = 0;
        boolean flag = false;
        String newLine;
        while (ch != 65535) {
            newLine = "";
            while (ch != '\n') {
                newLine += ch;
                try {
                    ch = (char) is.read();
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
            if (newLine.matches("//.*\r*\n*"))
                newLine = newLine.replaceAll("//.*\r*\n*", "");
            else
                newLine = newLine.replaceAll("//.*\r*\n*", "") + "\n";
            if (flag) {
                if (newLine.matches(".*\\*/.*\r*\n*")) {
                    flag = false;
                    newLine = newLine.replaceAll(".*\\*/\n*", "");
                } else
                    newLine = "";
            } else if (newLine.matches(".*/\\*.*\r*\n*")) {
                newLine = newLine.replaceAll("/\\*.*\r*\n*", "");
                flag = true;
            }
            try {
                os.write((newLine).getBytes());
                ch = (char) is.read();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        try {
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

}

package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class WordCounter {

    public static void main(String args[]) {
        calculateFrequency(System.getProperty("user.dir") + "\\res\\shakespeare.txt", 20);
    }

    public static void calculateFrequency(String path, int ThreadNum) {
        MyThread t[] = new MyThread[ThreadNum];
        FileHandler raw = new FileHandler(path);
        for (int i = 0; i < ThreadNum; i++)
            t[i] = new MyThread(raw);
        try {
            for (int i = 0; i < ThreadNum; i++)
                t[i].thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        for (String key : FileHandler.count.keySet()) {
            FileHandler.writeBytes(String.format(
                    "%s %f\n", key,
                    FileHandler.count.get(key).doubleValue() / FileHandler.totalCount * 100
            ).getBytes());
        }
        FileHandler.closeFiles();
    }

}

class MyThread implements Runnable {
    Thread thread;
    FileHandler fileHandler;

    public MyThread(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        String sb = FileHandler.nextLine();
        while (sb != null) {
            String temp[] = sb.split("[^a-zA-Z-']+");
            for (String s : temp) {
                if (s.equals("") || s.equals("\'") || s.equals("-"))
                    continue;
                Integer i = FileHandler.getCount(s);
                if (i != null)
                    FileHandler.setCount(s, i + 1);
                else
                    FileHandler.setCount(s, 1);
                FileHandler.totalCount++;
            }
            sb = FileHandler.nextLine();
        }
    }

}

class FileHandler {
    public static HashMap<String, Integer> count;
    public static long totalCount;
    private static FileInputStream inputStream;
    private static FileOutputStream outputStream;

    public FileHandler(String path) {
        try {
            inputStream = new FileInputStream(path);
            outputStream = new FileOutputStream(System.getProperty("user.dir") + "\\res\\result.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        totalCount = 0;
        count = new HashMap<String, Integer>();
    }

    private static char nextChar() {
        int i = 0;
        try {
            i = inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (char) i;
    }

    synchronized static public String nextLine() {
        String sb = "";
        char ch = nextChar();
        if (ch == 65535)
            return null;
        while (ch != '\n') {
            sb += ch;
            ch = nextChar();
        }
        return sb;
    }

    public static void writeBytes(byte arr[]) {
        try {
            outputStream.write(arr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public static Integer getCount(String s) {
        return count.get(s);
    }

    synchronized public static void setCount(String s, int count) {
        FileHandler.count.put(s, count);
    }

    public static void closeFiles() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

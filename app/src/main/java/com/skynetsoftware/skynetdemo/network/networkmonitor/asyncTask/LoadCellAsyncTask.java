package com.skynetsoftware.skynetdemo.network.networkmonitor.asyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.skynetsoftware.skynetdemo.network.networkmonitor.activity.MainActivity;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.Cell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;


public class LoadCellAsyncTask extends AsyncTask<Void, Void, Void> {
    Activity contextCha;
    private String file;

    public LoadCellAsyncTask(Activity ctx) {
        this.contextCha = ctx;
    }

    protected Void doInBackground(Void... params) {
        this.file = "sdcard/NetworkMonitorLOGs/cellfile/cellfile.txt";
        readCellFile();
        return null;
    }

    private void readCellFile() {
        Exception e;
        try {
            Reader fileReader = new FileReader(new File(this.file));
            BufferedReader bufReader = new BufferedReader(fileReader);
            String readLine = bufReader.readLine();
            while (true) {
                readLine = bufReader.readLine();
                Reader reader;
                if (readLine != null) {
                    String[] elements = readLine.split("\t");
                    String tilt = "";
                    String node = "";
                    if (elements.length > 7) {
                        node = elements[7];
                    }
                    if (elements.length > 8) {
                        tilt = elements[8];
                    }
                    Cell cell = new Cell(elements[0], elements[1].trim(), elements[2].trim(), elements[3], elements[4], elements[5], elements[6], node, tilt);
                    String key = cell.getTech() + "_" + cell.getLac() + "_" + cell.getCellid();
                    if (cell.getTech().trim().equals("4G")) {
                        key = cell.getTech() + "_" + cell.getNode() + "_" + cell.getCellid();
                    }
                    if (cell.getTech().trim().equals("2G") && !MainActivity.cells2G.containsKey(key)) {
                        try {
                            MainActivity.cells2G.put(key, cell);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (cell.getTech().trim().equals("3G") && !MainActivity.cells3G.containsKey(key)) {
                        try {
                            MainActivity.cells3G.put(key, cell);
                        } catch (Exception e22) {
                            e22.printStackTrace();
                        }
                    }
                    if (cell.getTech().trim().equals("4G") && !MainActivity.cells4G.containsKey(key)) {
                        try {
                            MainActivity.cells4G.put(key, cell);
                        } catch (Exception e222) {
                            try {
                                e222.printStackTrace();
                            } catch (Exception e3) {
                                e222 = e3;
                                reader = fileReader;
                            }
                        }
                    }
                } else {
                    reader = fileReader;
                    return;
                }
            }
        } catch (Exception e4) {

            e4.printStackTrace();
        }
    }
}

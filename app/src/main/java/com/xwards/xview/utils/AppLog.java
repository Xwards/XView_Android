package com.xwards.xview.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nithinjith on 12/5/2017.
 */

public class AppLog {

    public static void writeToFile(String txt) {

            /*try {
                File sd = Environment.getExternalStorageDirectory();

                File logfile = new File(sd, "XView.txt");

                if (!logfile.exists()) {
                    logfile.createNewFile();
                }

                if (sd.canWrite()) {
                    FileWriter fw = new FileWriter(sd + "/XView.txt", true); // the
                    // true
                    // will
                    // append
                    // the
                    // new
                    // data
                    fw.write(txt + " TIME = " + getCurrentDate() + " TimeMillis=" + System.currentTimeMillis() + "\n");// appends the string to the file
                    fw.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }*/
    }

    private static String getCurrentDate() {
        Date date = new Date();
        String strDateFormat = "yyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat, Locale.getDefault());

        return sdf.format(date);
    }
}

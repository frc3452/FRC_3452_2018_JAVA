package frc.robot.util;

import java.io.File;
import java.io.IOException;

public class GZFileMaker {

    private static String[] illegalCharacters = { "*", "?", "<", ">", "|", ":" };

    private static String placeForInvalidFilesToGo = "/home/lvuser/InvalidFileName/";

    public static enum ValidFileExtensions {
        CSV(".csv"), HTML(".html");

        private String val;

        private ValidFileExtensions(String val) {
            this.val = val;
        }
    }

    public static File getFile(String name, String folder, ValidFileExtensions fileExtension, boolean usb,
            boolean write) throws Exception {

        File f;

        String path = getFileLocation(name, folder, fileExtension, usb, true);
        if (write) {
            f = new File(path);

            if (!f.getParentFile().exists())
                f.getParentFile().mkdirs();

            try {
                if (!f.exists())
                    f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            f = new File(path);
        }

        if (!f.exists() && !write) {
            throw new IOException("ERROR File cannot be found at path {" + path + "}");
        }

        return f;
    }

    public static File getFile(String name, String folder, ValidFileExtensions fileExtension, boolean write)
            throws Exception {
        return getFile(name, folder, fileExtension, false, write);
    }

    public static File getFile(String name, String folder, boolean usb, boolean write) throws Exception {
        return getFile(name, folder, ValidFileExtensions.CSV, usb, write);
    }

    public static File getFile(String name, String folder, boolean write) throws Exception {
        return getFile(name, folder, false, write);
    }

    public static String getFileLocation(String name, String folder, ValidFileExtensions fileExtension, boolean usb,
            boolean withFile) {
        String retval = ((usb) ? "/u/" : "/home/lvuser/") + folder;
        // "/media/sda1/"

        if (withFile)
            retval += ((folder == "" ? "" : "/") + name + fileExtension.val);

        if (pathValid(retval))
            return retval;
        else {
            String newRetval = placeForInvalidFilesToGo + GZUtil.dateTime(true) + fileExtension.val;
            System.out.println("Invalid file location: " + retval + "\nFile will be written at: " + newRetval);
            return newRetval;
        }
    }

    public static boolean pathValid(String path) {
        boolean containsBadValue = false;

        for (String s : illegalCharacters)
            containsBadValue |= stringContainsChar(path, s);

        // Method should returning if valid, not if contains bad characters
        return !containsBadValue;
    }

    private static boolean stringContainsChar(String name, String character) {
        return name.contains(character);
    }

}
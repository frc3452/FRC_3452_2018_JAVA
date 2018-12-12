package frc.robot.util;

import java.io.File;
import java.io.IOException;

public class GZFileMaker {

    private static final String[] illegalCharacters = { "/", "\"", "*", "?", "\"", "<", ">", "|", ":" };

    private static final String placeForInvalidFilesToGo = "/home/lvuser/InvalidFileName/";

    public static enum ValidFileExtensions {
        CSV(".csv"), HTML(".html");

        private String val;

        private ValidFileExtensions(final String val) {
            this.val = val;
        }
    }

    public static File getFile(final String name, final String folder, final ValidFileExtensions fileExtension,
            final boolean usb, boolean write) throws Exception {

        File f;

        String path = getFileLocation(name, folder, fileExtension, usb, true);
        if (write) {
            f = new File(getFileLocation(name, folder, fileExtension, usb, false));
            f.mkdirs();
            f = new File(path);

            try {
                if (!f.exists())
                    f.createNewFile();
            } catch (SecurityException e) {
                throw new Exception("Read access to file at path {" + path + "} denied!");
            }

        } else {
            f = new File(path);
        }

        if (!f.exists() && !write) {
            throw new IOException("ERROR File cannot be found at path {" + path + "}");
        }

        return f;
    }

    public static File getFile(final String name, final String folder, final ValidFileExtensions fileExtension,
            boolean write) throws Exception {
        return getFile(name, folder, fileExtension, false, write);
    }

    public static File getFile(final String name, final String folder, final boolean usb, boolean write)
            throws Exception {
        return getFile(name, folder, ValidFileExtensions.CSV, usb, write);
    }

    public static File getFile(final String name, final String folder, boolean write) throws Exception {
        return getFile(name, folder, false, write);
    }

    public static String getFileLocation(final String name, final String folder,
            final ValidFileExtensions fileExtension, final boolean usb, boolean withFile) {
        String retval = ((usb) ? "/u/" : "/home/lvuser/") + folder;

        if (withFile)
            retval += ("/" + name + fileExtension.val);

        if (pathValid(retval))
            return retval;
        else {
            String newRetval = placeForInvalidFilesToGo + GZUtil.dateTime(true) + fileExtension.val;
            System.out.println("Invalid file location: " + retval + "\nFile will be written at: " + newRetval);
            return newRetval;
        }
    }

    public static boolean pathValid(final String path) {
        boolean containsBadValue = false;

        for (String s : illegalCharacters)
            containsBadValue |= stringContainsChar(path, s);

        // Method should returning if valid, not if contains bad characters
        return !containsBadValue;
    }

    private static boolean stringContainsChar(final String name, final String character) {
        return name.contains(character);
    }

}
package frc.robot.util;

import java.io.File;
import java.io.IOException;

public class GZFileMaker {

    private static final String[] illegalCharacters = { "/", "\"", "*", "?", "\"", "<", ">", "|", ":" };

    private static final String placeForInvalidFilesToGo = "/home/lvuser/InvalidFileName/";

    private static enum ValidFileExtensions {
        CSV(".csv"), HTML(".html");

        private String val;

        private ValidFileExtensions(final String val) {
            this.val = val;
        }
    }

    public static File getFile(final String name, final String folder, final ValidFileExtensions fileExtension,
            final boolean usb, boolean write) throws IOException {

        File f;

        if (write) {
            f = new File(getFileLocation(name, folder, fileExtension, usb, false));
            f.mkdirs();
        }
        f = new File(getFileLocation(name, folder, fileExtension, usb, true));

        if (!f.exists() && !write) {
            System.out
                    .println("ERROR File cannot be found: " + getFileLocation(name, folder, fileExtension, usb, true));
            throw new IOException();
        }

        return f;
    }

    public static File getFile(final String name, final String folder, final ValidFileExtensions fileExtension,
            boolean write) throws IOException {
        return getFile(name, folder, fileExtension, false, write);
    }

    public static File getFile(final String name, final String folder, final boolean usb, boolean write)
            throws IOException {
        return getFile(name, folder, ValidFileExtensions.CSV, usb, write);
    }

    public static File getFile(final String name, final String folder, boolean write) throws IOException {
        return getFile(name, folder, false, write);
    }

    public static String getFileLocation(final String name, final String folder,
            final ValidFileExtensions fileExtension, final boolean usb, boolean withFile) {
        String retval = ((usb) ? "/u/" : "/home/lvuser/") + folder + (withFile ? "/" + name + fileExtension.val : "");

        if (pathValid(name, folder))
            return retval;
        else {
            String newRetval = placeForInvalidFilesToGo + GZUtil.dateTime(true) + fileExtension.val;
            System.out.println("Invalid file location: " + retval + "\nFile will be written at: " + newRetval);
            return newRetval;
        }
    }

    public static boolean pathValid(final String name, final String folder) {
        boolean containsBadValue = false;

        for (String s : illegalCharacters)
            containsBadValue |= pathOrFolderContain(name, folder, s);

        // Method should returning if valid, not if contains bad characters
        return !containsBadValue;
    }

    private static boolean pathOrFolderContain(final String name, final String folder, final String character) {
        return name.contains(character) || folder.contains(character);
    }

}
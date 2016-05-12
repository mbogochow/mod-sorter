package me.mbogo.modsorter.util;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static void openInEditor(File file) throws IOException {
        // http://stackoverflow.com/questions/6273221/open-a-text-file-in-the-default-text-editor-via-java#answer-6273238
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            String cmd = "rundll32 url.dll,FileProtocolHandler "
                    + file.getCanonicalPath();
            Runtime.getRuntime().exec(cmd);
        } else {
            Desktop.getDesktop().edit(file);
        }
    }

    private FileUtil() {
    }
}

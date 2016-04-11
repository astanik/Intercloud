package de.tu_berlin.cit.intercloud.webapp.profiling;

import de.tu_berlin.cit.intercloud.client.profiling.ProfilingItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class ProfilingUtil {
    private static final String COL_SEP = ";";

    public static void writeToCsv(List<ProfilingItem> items, String id, String filename) throws FileNotFoundException {
        File file = new File("target/" + filename + ".csv");
        boolean fileExists = file.exists();
        PrintWriter writer = new PrintWriter(new FileOutputStream(file, true));
        try {
            StringBuilder s = new StringBuilder();
            if (!fileExists) {
                s.append("id").append(COL_SEP)
                        .append("configure").append(COL_SEP)
                        .append("beforeRender").append(COL_SEP)
                        .append("render").append(COL_SEP)
                        .append("getRequestModel").append(COL_SEP)
                        .append("getRepresentationString").append(COL_SEP)
                        .append("getResponseModel");
                writer.println(s);
            }
            for (ProfilingItem item : items) {
                s.delete(0, s.length());
                s.append(id).append(COL_SEP)
                        .append(item.getDuration()).append(COL_SEP)
                        .append(item.get("configure")).append(COL_SEP)
                        .append(item.get("beforeRender")).append(COL_SEP)
                        .append(item.get("render")).append(COL_SEP)
                        .append(item.get("getRequestModel")).append(COL_SEP)
                        .append(item.get("getRepresentationString")).append(COL_SEP)
                        .append(item.get("getResponseModel"));
                writer.println(s);
            }
        } finally {
            writer.close();
        }
    }
}

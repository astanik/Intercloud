package de.tu_berlin.cit.intercloud.client.profiling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncFileListener implements IProfilingListener {
    private static final Logger logger = LoggerFactory.getLogger(AsyncFileListener.class);
    private static final String CSV_SEPERATOR = ";";
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private String fileName;

    public AsyncFileListener(String fileName) {
        this.fileName = fileName;
        if (!new File(fileName).exists()) {
            writeHeaderToCsv();
        }
    }

    @Override
    public void onStart(ProfilingItem item) {

    }

    @Override
    public void onStop(ProfilingItem item) {
        EXECUTOR_SERVICE.execute(() -> {
            try {
                writeToCsv(item);
            } catch (Exception e) {
                logger.error("Could not write profiling item to csv.", e);
            }
        });
    }

    private void writeToCsv(ProfilingItem item) throws FileNotFoundException {
        if (null != fileName) {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(fileName, true));
            try {
                printWriter.println(item.getContext() + CSV_SEPERATOR
                        + item.getDuration() + CSV_SEPERATOR
                        + item.get("configure") + CSV_SEPERATOR
                        + item.get("beforeRender") + CSV_SEPERATOR
                        + item.get("render") + CSV_SEPERATOR
                        + item.get("transform") + CSV_SEPERATOR
                );
            } finally {
                printWriter.close();
            }
        }
    }

    private void writeHeaderToCsv() {
        if (null != fileName) {
            try {
                PrintWriter printWriter = new PrintWriter(fileName);
                try {
                    printWriter.println("requestName" + CSV_SEPERATOR
                            + "requestDuration" + CSV_SEPERATOR
                            + "onConfigure" + CSV_SEPERATOR
                            + "onBeforeRender" + CSV_SEPERATOR
                            + "onRender" + CSV_SEPERATOR
                            + "transform" + CSV_SEPERATOR

                    );
                } finally {
                    printWriter.close();
                }
            } catch (Exception e) {
                logger.error("Could not write header data to csv.", e);
            }
        }
    }
}

package de.tu_berlin.cit.intercloud.client.profiling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfilingService {
    private static final ProfilingService INSTANCE = new ProfilingService();
    private static final String CSV_SEPERATOR = ";";
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private static final ThreadLocal<ProfilingItem> PROFILING_THREAD = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(ProfilingService.class);

    public static ProfilingService getInstance() {
        return INSTANCE;
    }

    private ProfilingService() {
    }

    private boolean enabled = false;
    private String fileName = null;

    public ProfilingItem newProfilingItem() {
        if (enabled) {
            ProfilingItem item = new ProfilingItem();
            PROFILING_THREAD.set(item);
            return item;
        } else {
            return null;
        }
    }

    public ProfilingItem getProfilingItem() {
        if (enabled) {
            return PROFILING_THREAD.get();
        } else {
            return null;
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        if (!new File(fileName).exists()) {
            writeHeaderToCsv();
        }
    }

    public Object invokeAndProfile(IProfilingInterceptor interceptor) {
        if (enabled) {
            long time = System.currentTimeMillis();
            try {
                return interceptor.invoke();
            } finally {
                interceptor.profile(PROFILING_THREAD.get(), System.currentTimeMillis() - time);
            }
        } else {
            return interceptor.invoke();
        }
    }

    public void writeToCsv() {
        ProfilingItem item = PROFILING_THREAD.get();
        EXECUTOR_SERVICE.execute(() -> {
            try {
                writeToCsv(item);
            } catch (Exception e) {
                logger.error("Could not write profiling item to csv.", e);
            }
        });
    }

    private void writeToCsv(ProfilingItem item) throws FileNotFoundException {
        if (enabled && null != fileName) {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(fileName, true));
            try {
                printWriter.println(item.getRequestName() + CSV_SEPERATOR
                        + item.getRequestDuration() + CSV_SEPERATOR
                        + item.getOnConfigure() + CSV_SEPERATOR
                        + item.getOnBeforeRender() + CSV_SEPERATOR
                        + item.getOnRender() + CSV_SEPERATOR
                        + item.getTransform() + CSV_SEPERATOR
                );
            } finally {
                printWriter.close();
            }
        }
    }

    private void writeHeaderToCsv() {
        if (enabled && null != fileName) {
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

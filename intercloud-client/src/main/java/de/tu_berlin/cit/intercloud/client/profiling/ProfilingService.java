package de.tu_berlin.cit.intercloud.client.profiling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ProfilingService implements IProfilingService {
    private static final Logger logger = LoggerFactory.getLogger(ProfilingService.class);

    private static final ProfilingService INSTANCE = new ProfilingService();
    private static final String CSV_SEPERATOR = ";";

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private static final ThreadLocal<ProfilingItem> PROFILING_THREAD = new ThreadLocal<ProfilingItem>() {
        @Override
        protected ProfilingItem initialValue() {
            return null;
        }
    };

    public static ProfilingService getInstance() {
        return INSTANCE;
    }

    private ProfilingService() {
    }

    private Pattern filter = null;
    private String fileName = null;

    @Override
    public ProfilingItem start(String context) {
        ProfilingItem item = null;
        if (null != filter && filter.matcher(context).find()) {
            item = new ProfilingItem();
        }
        PROFILING_THREAD.set(item);
        return item;
    }

    public ProfilingItem getProfilingItem() {
        return PROFILING_THREAD.get();
    }

    @Override
    public void setFilter(String regex) {
        this.filter = Pattern.compile(regex);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        if (!new File(fileName).exists()) {
            writeHeaderToCsv();
        }
    }

    @Override
    public Object invokeAndProfile(IProfilingInterceptor interceptor) {
        ProfilingItem item = PROFILING_THREAD.get();
        if (null != item) {
            long time = System.currentTimeMillis();
            try {
                return interceptor.invoke();
            } finally {
                interceptor.profile(item, System.currentTimeMillis() - time);
            }
        } else {
            return interceptor.invoke();
        }
    }

    public void stop() {
        ProfilingItem item = PROFILING_THREAD.get();
        if (null != item) {
            PROFILING_THREAD.set(null);
            EXECUTOR_SERVICE.execute(() -> {
                try {
                    writeToCsv(item);
                } catch (Exception e) {
                    logger.error("Could not write profiling item to csv.", e);
                }
            });
        }
    }

    private void writeToCsv(ProfilingItem item) throws FileNotFoundException {
        if (null != fileName) {
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

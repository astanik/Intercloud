package de.tu_berlin.cit.intercloud.client.profiling.impl;

import de.tu_berlin.cit.intercloud.client.profiling.api.IProfilingCommand;
import de.tu_berlin.cit.intercloud.client.profiling.api.IProfilingListener;
import de.tu_berlin.cit.intercloud.client.profiling.api.IProfilingService;
import de.tu_berlin.cit.intercloud.client.profiling.api.ProfilingItem;

import java.util.regex.Pattern;

public class ProfilingService implements IProfilingService {
    private static final ProfilingService INSTANCE = new ProfilingService();
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
    private IProfilingListener listener = new LoggerListener();

    @Override
    public ProfilingItem start(String context) {
        ProfilingItem item = null;
        if (null != filter && filter.matcher(context).find()) {
            item = new ProfilingItem(context);
            item.setDuration(System.currentTimeMillis());
            if (null != listener) {
                listener.onStart(item);
            }
        }
        PROFILING_THREAD.set(item);
        return item;
    }

    @Override
    public <T> T execute(IProfilingCommand<T> command) {
        ProfilingItem item = PROFILING_THREAD.get();
        if (null != item) {
            long time = System.currentTimeMillis();
            try {
                return command.execute();
            } finally {
                item.add(command.getIdentifier(), System.currentTimeMillis() - time);
            }
        } else {
            return command.execute();
        }
    }

    @Override
    public ProfilingItem stop() {
        ProfilingItem item = PROFILING_THREAD.get();
        if (null != item) {
            item.setDuration(System.currentTimeMillis() - item.getDuration());
            PROFILING_THREAD.set(null);
            if (null != listener) {
                listener.onStop(item);
            }
        }
        return item;
    }

    @Override
    public void setListener(IProfilingListener listener) {
        this.listener = listener;
    }

    @Override
    public void setFilter(String regex) {
        if (null != regex) {
            this.filter = Pattern.compile(regex);
        } else {
            this.filter = null;
        }
    }
}

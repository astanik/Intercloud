package de.tu_berlin.cit.intercloud.client.profiling;

public class ProfilingItem {

    String context;
    long duration;
    long onConfigure;
    long onBeforeRender;
    long onRender;
    long transform;

    public ProfilingItem(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getOnConfigure() {
        return onConfigure;
    }

    public void setOnConfigure(long onConfigure) {
        this.onConfigure = onConfigure;
    }

    public long getOnBeforeRender() {
        return onBeforeRender;
    }

    public void setOnBeforeRender(long onBeforeRender) {
        this.onBeforeRender = onBeforeRender;
    }

    public long getOnRender() {
        return onRender;
    }

    public void setOnRender(long onRender) {
        this.onRender = onRender;
    }

    public long getTransform() {
        return transform;
    }

    public void setTransform(long transform) {
        this.transform = transform;
    }

    @Override
    public String toString() {
        return "ProfilingItem{" +
                "context='" + context + '\'' +
                ", duration=" + duration +
                ", onConfigure=" + onConfigure +
                ", onBeforeRender=" + onBeforeRender +
                ", onRender=" + onRender +
                ", transform=" + transform +
                '}';
    }
}

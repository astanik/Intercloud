package de.tu_berlin.cit.intercloud.client.profiling;

public class ProfilingItem {

    String requestName;
    long requestDuration;
    long onConfigure;
    long onBeforeRender;
    long onRender;
    long transform;

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public long getRequestDuration() {
        return requestDuration;
    }

    public void setRequestDuration(long requestDuration) {
        this.requestDuration = requestDuration;
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
}

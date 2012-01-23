package org.jenkinsci.plugins.buildcontextcapture;

/**
 * @author Gregory Boissinot
 */
public class BuildContextException extends Exception {

    public BuildContextException() {
    }

    public BuildContextException(String s) {
        super(s);
    }

    public BuildContextException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BuildContextException(Throwable throwable) {
        super(throwable);
    }
}

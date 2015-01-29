package com.tcmj.common.lang;

import java.io.PrintWriter;
import java.io.StringWriter;

/** Adapter for checked exceptions. */
class ExceptionAdapter extends RuntimeException {

    private static final long serialVersionUID = 3628087340457915101L;
    private final String stackTrace;
    public final Exception originalException;

    /** {@inheritDoc} */
    public ExceptionAdapter(Exception e) {
        super(e.toString());
        originalException = e;
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        stackTrace = writer.toString();
    }

    public ExceptionAdapter(String message, Exception e) {
        super(new StringBuilder(message == null ? "" : message).append(" : ").append(e.getMessage()).toString());
        originalException = e;
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        stackTrace = sw.toString();
    }

    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override
    public void printStackTrace(java.io.PrintStream printStream) {
        synchronized (printStream) {
            printStream.print(getClass().getName() + ": ");
            printStream.print(stackTrace);
        }
    }

    @Override
    public void printStackTrace(java.io.PrintWriter printWriter) {
        synchronized (printWriter) {
            printWriter.print(getClass().getName() + ": ");
            printWriter.print(stackTrace);
        }
    }

    public void rethrow() throws Exception {
        throw originalException;
    }

    public static RuntimeException toRuntimeException(Exception e) {
        if (RuntimeException.class.isInstance(e)) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }
}

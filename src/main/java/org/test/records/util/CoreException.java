package org.test.records.util;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;

public class CoreException extends Exception implements MessageSourceResolvable {

    private static final long serialVersionUID = 8128574366773363023L;

    private final DefaultMessageSourceResolvable defaultMessageSourceResolvable;

    private boolean isAlreadyLogged;

    public boolean isAlreadyLogged() {
        return this.isAlreadyLogged;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        this.isAlreadyLogged = true;
        StackTraceElement[] arrayOfStackTraceElement = super.getStackTrace();
        return arrayOfStackTraceElement;
    }

    public CoreException(String code) {
        super(createMessage(code, null));
        this.defaultMessageSourceResolvable = new DefaultMessageSourceResolvable(code);
    }

    public CoreException(String code, Object[] arguments) {
        super(createMessage(code, null));
        this.defaultMessageSourceResolvable = new DefaultMessageSourceResolvable(new String[] { code }, arguments);
    }

    public CoreException(String code, Throwable cause) {
        super(createMessage(code, null), cause);
        this.defaultMessageSourceResolvable = new DefaultMessageSourceResolvable(new String[] { code }, null, cause.getMessage());
    }

    public CoreException(String code, Object[] arguments, Throwable cause) {
        super(createMessage(code, null), cause);
        this.defaultMessageSourceResolvable = new DefaultMessageSourceResolvable(new String[] { code }, arguments, cause.getMessage());
    }

    public CoreException(String code, String defaultMessage) {
        super(createMessage(code, defaultMessage));
        this.defaultMessageSourceResolvable = new DefaultMessageSourceResolvable(new String[] { code }, null, defaultMessage);
    }

    public CoreException(String code, Object[] arguments, String defaultMessage) {
        super(createMessage(code, defaultMessage));
        this.defaultMessageSourceResolvable = new DefaultMessageSourceResolvable(new String[] { code }, arguments, defaultMessage);
    }

    public CoreException(String code, String defaultMessage, Throwable cause) {
        super(createMessage(code, defaultMessage), cause);
        this.defaultMessageSourceResolvable = new DefaultMessageSourceResolvable(new String[] { code }, new Object[] { cause.getClass().getName() }, defaultMessage);
    }

    public CoreException(String code, Object[] arguments, String defaultMessage, Throwable cause) {
        super(createMessage(code, defaultMessage), cause);
        this.defaultMessageSourceResolvable = new DefaultMessageSourceResolvable(new String[] { code }, arguments, defaultMessage);
    }


    public String getCode() {
        return this.defaultMessageSourceResolvable.getCode();
    }

    @Override
    public String[] getCodes() {
        return this.defaultMessageSourceResolvable.getCodes();
    }

    @Override
    public Object[] getArguments() {
        return this.defaultMessageSourceResolvable.getArguments();
    }

    @Override
    public String getDefaultMessage() {
        return this.defaultMessageSourceResolvable.getDefaultMessage();
    }

    static String createMessage(String code, String defaultMessage) {
        if (code == null){
            return defaultMessage;
        }

        if (defaultMessage == null) {
            return "[" + code + "]";
        }
        return "[" + code + "] " + defaultMessage;
    }
}

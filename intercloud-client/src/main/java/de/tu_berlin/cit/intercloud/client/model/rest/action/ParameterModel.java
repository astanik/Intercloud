package de.tu_berlin.cit.intercloud.client.model.rest.action;

import java.io.Serializable;

public class ParameterModel implements Serializable {
    private static final long serialVersionUID = -6182746543572339674L;
    private static final String WRONG_TYPE_MSG = "Cannot set %s Parameter for type %s";

    public enum Type {
        STRING,
        INTEGER,
        DOUBLE,
        BOOLEAN,
        LINK
    }

    private final String name;
    private final Type type;
    private final String documentation;
    private Object value;

    public ParameterModel(String name, String type, String documentation) {
        this(name, Type.valueOf(type), documentation);
    }

    public ParameterModel(String name, Type type, String documentation) {
        this.name = name;
        this.type = type;
        this.documentation = documentation;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getDocumentation() {
        return documentation;
    }

    /*
        STRING
     */

    public boolean isString() {
        return Type.STRING.equals(this.type);
    }

    public void setString(String s) {
        if (isString()) {
            this.value = s;
        } else {
            throw new IllegalArgumentException(String.format(WRONG_TYPE_MSG, "String", this.type));
        }
    }

    public String getString() {
        return isString() ? (String) value : null;
    }

    /*
        INTEGER
     */

    public boolean isInteger() {
        return Type.INTEGER.equals(this.type);
    }

    public void setInteger(Integer i) {
        if (isInteger()) {
            this.value = i;
        } else {
            throw new IllegalArgumentException(String.format(WRONG_TYPE_MSG, "Integer", this.type));
        }
    }

    public Integer getInteger() {
        return isInteger() ? (Integer) this.value : null;
    }

    /*
        DOUBLE
     */

    public boolean isDouble() {
        return Type.DOUBLE.equals(this.type);
    }

    public void setDouble(Double d) {
        if (isDouble()) {
            this.value = d;
        } else {
            throw new IllegalArgumentException(String.format(WRONG_TYPE_MSG, "Double", this.type));
        }
    }

    /*
        BOOLEAN
     */

    public boolean isBoolean() {
        return Type.BOOLEAN.equals(this.type);
    }

    public void setBoolean(Boolean b) {
        if (isBoolean()) {
            this.value = b;
        } else {
            throw new IllegalArgumentException(String.format(WRONG_TYPE_MSG, "Boolean", this.type));
        }
    }

    public Boolean getBoolean() {
        return isBoolean() ? (Boolean) value : null;
    }

    /*
        LINK
     */

    public boolean isLink() {
        return Type.LINK.equals(this.type);
    }

    public void setLink(String l) {
        if (isLink()) {
            value = l;
        } else {
            throw new IllegalArgumentException(String.format(WRONG_TYPE_MSG, "Link", this.type));
        }
    }

    public String getLink() {
        return isLink() ? (String) value : null;
    }
}

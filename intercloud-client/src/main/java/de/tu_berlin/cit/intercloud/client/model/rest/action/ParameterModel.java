package de.tu_berlin.cit.intercloud.client.model.rest.action;

import java.io.Serializable;

public class ParameterModel implements Serializable {
    private static final long serialVersionUID = -6182746543572339674L;
    private static final String WRONG_TYPE_MSG = "Cannot set %s Parameter for type %s.";

    public enum Type {
        STRING,
        INTEGER,
        DOUBLE,
        BOOLEAN,
        LINK
    }

    private final String name;
    private final Type type;
    private Object value;
    private final boolean required;
    private final String documentation;

    public ParameterModel(String name, String type, boolean required, String documentation) {
        this(name, Type.valueOf(type), required, documentation);
    }

    public ParameterModel(String name, Type type, boolean required, String documentation) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.documentation = documentation;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDocumentation() {
        return documentation;
    }

    @Override
    public String toString() {
        return "ParameterModel{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}';
    }

    public boolean hasValue() {
        return null != this.value;
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

    public Double getDouble() {
        return isDouble() ? (Double) value : null;
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

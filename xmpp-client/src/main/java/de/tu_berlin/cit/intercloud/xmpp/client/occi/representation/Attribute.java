package de.tu_berlin.cit.intercloud.xmpp.client.occi.representation;

import java.io.Serializable;

public class Attribute implements Serializable {
    private static final long serialVersionUID = -4070567697021876585L;

    public enum Type {
        STRING,
        ENUM,
        INTEGER,
        DOUBLE,
        FLOAT,
        BOOLEAN,
        URI,
        SIGNATURE,
        KEY,
        DATETIME,
        DURATION
    }
    
    private final String name;
    private final boolean required;
    private final Type type;
    private final String description;
    private Object value;

    public Attribute(String name, boolean required, String type, String description) {
        this.name = name;
        this.required = required;
        this.type = Type.valueOf(type.toString());
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", required=" + required +
                ", type=" + type +
                ", value=" + value +
                '}';
    }

    /*
        STRING
     */

    public boolean isString() {
        return Type.STRING.equals(type);
    }

    public void setString(String string) {
        if (isString()) {
            value = string;
        } else {
            throw new IllegalArgumentException("Cannot set String argument for type " + type);
        }
    }

    public String getString() {
        return isString() ? (String) value : null;
    }

    /*
        ENUM
     */

    public boolean isEnum() {
        return Type.ENUM.equals(type);
    }

    public void setEnum(String enumeration) {
        if (isEnum()) {
            value = enumeration;
        } else {
            throw new IllegalArgumentException("Cannot set Enum argument for type " + type);
        }
    }

    public String getEnum() {
        return isEnum() ? (String) value : null;
    }

    /*
        INTEGER
     */

    public boolean isInteger() {
        return Type.INTEGER.equals(type);
    }

    public void setInteger(Integer integer) {
        if (isInteger()) {
            value = integer;
        } else {
            throw new IllegalArgumentException("Cannot set Integer argument for type " + type);
        }
    }

    public Integer getInteger() {
        return isInteger() ? (Integer) value : null;
    }

    /*
        DOUBLE
     */

    public boolean isDouble() {
        return Type.DOUBLE.equals(type);
    }

    public void setDouble(Double d) {
        if (isDouble()) {
            value = d;
        } else {
            throw new IllegalArgumentException("Cannot set Double argument for type " + type);
        }
    }

    public Double getDouble() {
        return isDouble() ? (Double) value : null;
    }

}

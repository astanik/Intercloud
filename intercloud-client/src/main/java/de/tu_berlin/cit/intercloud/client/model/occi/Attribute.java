package de.tu_berlin.cit.intercloud.client.model.occi;

import java.io.Serializable;
import java.util.Date;

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
        return Type.STRING.equals(this.type);
    }

    public void setString(String string) {
        if (isString()) {
            this.value = string;
        } else {
            throw new IllegalArgumentException("Cannot set String argument for type " + this.type);
        }
    }

    public String getString() {
        return isString() ? (String) this.value : null;
    }

    /*
        ENUM
     */

    public boolean isEnum() {
        return Type.ENUM.equals(this.type);
    }

    public void setEnum(String enumeration) {
        if (isEnum()) {
            this.value = enumeration;
        } else {
            throw new IllegalArgumentException("Cannot set Enum argument for type " + this.type);
        }
    }

    public String getEnum() {
        return isEnum() ? (String) this.value : null;
    }

    /*
        INTEGER
     */

    public boolean isInteger() {
        return Type.INTEGER.equals(this.type);
    }

    public void setInteger(Integer integer) {
        if (isInteger()) {
            this.value = integer;
        } else {
            throw new IllegalArgumentException("Cannot set Integer argument for type " + this.type);
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
            throw new IllegalArgumentException("Cannot set Double argument for type " + this.type);
        }
    }

    public Double getDouble() {
        return isDouble() ? (Double) this.value : null;
    }

    /*
        FLOAT
     */

    public boolean isFloat() {
        return Type.FLOAT.equals(this.type);
    }

    public void setFloat(Float f) {
        if (isFloat()) {
            this.value = f;
        } else {
            throw new IllegalArgumentException("Cannot set Float argument for type " + this.type);
        }
    }

    public Float getFloat() {
        return isFloat() ? (Float) value : null;
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
            throw new IllegalArgumentException("Cannot set Boolean argument for type " + this.type);
        }
    }

    public Boolean getBoolean() {
        return isBoolean() ? (Boolean) value : null;
    }

    /*
        DATETIME
     */

    public boolean isDatetime() {
        return Type.DATETIME.equals(this.type);
    }

    public void setDatetime(Date datetime) {
        if (isDatetime()) {
            this.value = datetime;
        } else {
            throw new IllegalArgumentException("Cannot set Datetime argument for type " + this.type);
        }
    }

    public Date getDatetime() {
        return isDatetime() ? (Date) this.value : null;
    }

    /*
        URI
     */

    public boolean isUri() {
        return Type.URI.equals(this.type);
    }

    public void setUri(String uri) {
        if (isUri()) {
            this.value = uri;
        } else {
            throw new IllegalArgumentException("Cannot set Uri argument for type " + this.type);
        }
    }

    public String getUri() {
        return isUri() ? (String) value : null;
    }
}

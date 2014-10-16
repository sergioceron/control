package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Usuario on 04/10/2014.
 */
@Entity
public class Configuration {
    private String name;
    private String value;
    private String description;

    @Id
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

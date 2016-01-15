package org.thanatos.flowgeek.bean;

import org.simpleframework.xml.Element;

import java.io.Serializable;

/**
 * Created by thanatos on 15/12/21.
 */
public class Entity implements Serializable{

    //field
    @Element(name = "id", required = false)
    private Long id;

    //get and set
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

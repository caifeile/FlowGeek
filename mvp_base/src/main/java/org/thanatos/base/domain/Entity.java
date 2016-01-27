package org.thanatos.base.domain;

import java.io.Serializable;

/**
 * Created by thanatos on 15/12/21.
 */
public abstract class Entity implements Serializable{

    //field
    private Long id;

    //get and set
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

package org.cassandrademo.example.pojo;

import me.prettyprint.hom.annotations.Column;
import me.prettyprint.hom.annotations.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: legezadm
 * @since: 21.07.13
 */
@Entity
@Table(name = "State")
public class State {

    @Id
    private String id;

    @Column(name = "stateName")
    private String stateName;

    @Column(name="zipCode")
    private String zipCode;

    /*
        needed for EntityManager
     */
    public State() {
    }

    public State(String id, String stateName, String zipCode) {
        this.id = id;
        this.stateName = stateName;
        this.zipCode = zipCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}

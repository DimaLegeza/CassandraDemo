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
@Table (name = "UserState")
public class UserLivesInState {

    @Id
    private String id;

    @Column(name = "user")
    private String userId;

    @Column(name = "state")
    private String stateId;

    public UserLivesInState(String id, String userId, String stateId) {
        this.id = id;
        this.userId = userId;
        this.stateId = stateId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
}

package org.cassandrademo.example.pojo;

/**
 * @author: legezadm
 * @since: 21.07.13
 */
public enum UserInStateEnum {

    USER_ID("userId"), STATE_ID("stateId");

    private String value;

    UserInStateEnum(String stateId) {
        this.value = stateId;
    }

    public String getValue() {
        return value;
    }
}

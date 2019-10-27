package com.activeMQ.productor.domain;
/**
 * @Author xiongjinchen
 * @Date 2019/10/1 8:46
 * @Version 1.0
 */

import java.io.Serializable;
import java.util.Date;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/1 8:46
 * activeMQ传送JavaBean，JavaBean需要序列化
 */
public class User implements Serializable {
    private static final long serialVersionUID = 8687616031325769901L;
    private Integer id;
    private String username;
    private Date date;

    public User() {
    }

    public User(Integer id, String username, Date date) {
        this.id = id;
        this.username = username;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", date=" + date +
                '}';
    }
}

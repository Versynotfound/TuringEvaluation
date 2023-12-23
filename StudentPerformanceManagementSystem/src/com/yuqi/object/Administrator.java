package com.yuqi.object;

import java.time.LocalDateTime;

/**
 * 管理员类
 * 管理员ID、管理员名称、密码、是否被锁定、管理员账号创建时间
 *
 * @author yuqi
 * @version 1.0
 * date 2023/12/16
 */
public class Administrator {
    private String administratorId;
    private String name;
    private String password;
    private String locked = "false";
    private String createTime;

    @Override
    public String toString() {
        return "Administrator{" +
                "administratorId='" + administratorId + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", locked='" + locked + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public Administrator() {
    }

    public Administrator(String administratorId, String name, String password, String createTime) {
        this.administratorId = administratorId;
        this.name = name;
        this.password = password;
        this.createTime = createTime;
    }

    public String getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(String administratorId) {
        this.administratorId = administratorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }
}
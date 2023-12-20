package com.yuqi;

import java.time.LocalDate;

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
    private boolean locked = false;
    private LocalDate createTime;

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

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked() {
        this.locked = true;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }
}
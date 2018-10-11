package xyz.yuzh.spring.boot.blog.blogprototype.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author yu.zh [yuzh233@gmail.com] 2018/09/24 16:43
 */
@Entity // 实体
public class User implements Serializable {
    /**
     * 用户的唯一标识
     */
    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增策略
    private long id;

//    @Column(nullable = false) // 映射为字段，值不能为空
    private String name;

//    @Column(nullable = false) // 映射为字段，值不能为空
    private String email;

//    @Column(nullable = false) // 映射为字段，值不能为空
    private int age;

    protected User() { // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
    }

    public User(long id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

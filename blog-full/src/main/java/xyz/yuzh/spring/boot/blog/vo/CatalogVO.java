package xyz.yuzh.spring.boot.blog.vo;

import xyz.yuzh.spring.boot.blog.domain.Catalog;

import java.io.Serializable;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/26
 */
public class CatalogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private Catalog catalog;

    public CatalogVO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

}
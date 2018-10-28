package xyz.yuzh.spring.boot.blog.service;

import xyz.yuzh.spring.boot.blog.domain.Catalog;
import xyz.yuzh.spring.boot.blog.domain.User;

import java.util.List;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/26
 */
public interface CatalogService {

    /**
     * 保存 Catalog
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除 Catalog
     */
    void removeCatalog(Long id);

    /**
     * 根据 id 获取 Catalog
     */
    Catalog getCatalogById(Long id);

    /**
     * 获取 Catalog 列表
     */
    List<Catalog> listCatalogs(User user);
}

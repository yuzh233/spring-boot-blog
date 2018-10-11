package xyz.yuzh.learn.spring.boot.blog.elasticsearchinaction.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = "blog", type = "blog") // 标注为文档实体类
public class EsBlog implements Serializable {

    @Id
    private String id;
    private String title;
    private String summary; // 关键字
    private String content;

    // 遵循 JPA 规范
    protected EsBlog() {
    }

    public EsBlog(String id, String title, String summary, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "EsBlog{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public EsBlog(String title, String summary, String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

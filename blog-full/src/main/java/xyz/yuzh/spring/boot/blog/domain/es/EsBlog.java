package xyz.yuzh.spring.boot.blog.domain.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import xyz.yuzh.spring.boot.blog.domain.Blog;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 全文检索文档类
 *
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/27
 */
@Document(indexName = "blog", type = "blog") // 标识是一个 Es 的文档类
@XmlRootElement // MediaType 转为 XML
public class EsBlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id; // es 自己的标识 id

    // @Field(index = FieldIndex.not_analyzed)
    private Long blogId; // Blog 实体的 id

    private String title;

    private String summary;

    private String content;

    // @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private String username;

    // @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private String avatar;

    // @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Timestamp createTime;

    // @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Integer readSize = 0;

    // @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Integer commentSize = 0;

    // @Field(index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Integer voteSize = 0;

    private String tags;

    // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
    protected EsBlog() {
    }

    public EsBlog(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public EsBlog(Long blogId, String title, String summary, String content, String username, String avatar, Timestamp createTime,
                  Integer readSize, Integer commentSize, Integer voteSize, String tags) {
        this.blogId = blogId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.username = username;
        this.avatar = avatar;
        this.createTime = createTime;
        this.readSize = readSize;
        this.commentSize = commentSize;
        this.voteSize = voteSize;
        this.tags = tags;
    }

    public EsBlog(Blog blog) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
    }

    public void update(Blog blog) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Integer getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(Integer voteSize) {
        this.voteSize = voteSize;
    }

    public String getTags() {
        return tags;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "EsBlog{" +
                "id='" + id + '\'' +
                ", blogId=" + blogId +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", readSize=" + readSize +
                ", commentSize=" + commentSize +
                ", voteSize=" + voteSize +
                ", tags='" + tags + '\'' +
                '}';
    }
}


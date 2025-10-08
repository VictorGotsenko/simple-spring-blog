package spring.blog.dto;

public class TagDTO {

    private String name;

    private Long postId; // ссылка на пост, где расположен тег

    /**
     *
     * @return postId
     */
    public Long getPostId() {
        return postId;
    }

    /**
     *
     * @param postId postId
     */
    public void setPostId(Long postId) {
        this.postId = postId;
    }

    /**
     *
     * @return name String
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }
}

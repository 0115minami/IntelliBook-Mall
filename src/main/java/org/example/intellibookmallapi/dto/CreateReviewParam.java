package org.example.intellibookmallapi.dto;

/**
 * 创建评价参数
 */
public class CreateReviewParam {
    
    private Long bookId;
    private Integer rating;  // 1-5星
    private String content;
    
    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}

package main;

public class CommentBean {
    private String JobTitle;
    private String Question;
    private String Comment;
    private String Category;
    private String CommentTheme;
    
    public CommentBean(String jobTitle, String question, String comment, String category, String commentTheme) {
        JobTitle = jobTitle;
        Question = question;
        Comment = comment;
        Category = category;
        CommentTheme = commentTheme;
    }
    
    public CommentBean() {
    }
    
    public String getJobTitle() {
        return JobTitle;
    }
    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }
    public String getQuestion() {
        return Question;
    }
    public void setQuestion(String question) {
        Question = question;
    }
    public String getComment() {
        return Comment;
    }
    public void setComment(String comment) {
        Comment = comment;
    }
    public String getCategory() {
        return Category;
    }
    public void setCategory(String category) {
        Category = category;
    }
    public String getCommentTheme() {
        return CommentTheme;
    }
    public void setCommentTheme(String commentTheme) {
        CommentTheme = commentTheme;
    }
    
    
}

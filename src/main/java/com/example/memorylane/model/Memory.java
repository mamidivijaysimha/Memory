package com.example.memorylane.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "memories")
public class Memory {

    @Id
    private String id;

    private String imageUrl;
    private String description;
    private int likes;
    private List<Map<String, String>> comments = new ArrayList<>();
    private String postedBy;
    private List<String> likedBy = new ArrayList<>();

    public Memory() {}

    public Memory(String imageUrl, String description, String postedBy) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.postedBy = postedBy;
        this.likes = 0;
        this.comments = new ArrayList<>();
        this.likedBy = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public List<Map<String, String>> getComments() { return comments; }
    public void setComments(List<Map<String, String>> comments) { this.comments = comments; }

    public String getPostedBy() { return postedBy; }
    public void setPostedBy(String postedBy) { this.postedBy = postedBy; }

    public List<String> getLikedBy() { return likedBy; }
    public void setLikedBy(List<String> likedBy) { this.likedBy = likedBy; }
}

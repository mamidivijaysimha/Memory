package com.example.memorylane.controller;

import com.example.memorylane.model.Memory;
import com.example.memorylane.repository.MemoryRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/api/memories")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MemoryController {

    private final MemoryRepository memoryRepo;

    public MemoryController(MemoryRepository memoryRepo) {
        this.memoryRepo = memoryRepo;
    }

    @GetMapping
    public List<Memory> getAll() {
        return memoryRepo.findAll();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addMemory(
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("postedBy") String postedBy
    ) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            File path = new File(uploadDir + fileName);
            imageFile.transferTo(path);

            String imageUrl = "http://localhost:8081/uploads/" + fileName;

            Memory memory = new Memory(imageUrl, description, postedBy);
            memory.setComments(new ArrayList<>());
            memory.setLikedBy(new ArrayList<>());

            memoryRepo.save(memory);
            return ResponseEntity.ok(memory);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed");
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> like(@PathVariable String id, @RequestParam("username") String username) {
        Optional<Memory> optional = memoryRepo.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Memory memory = optional.get();

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }

        if (memory.getPostedBy() != null && memory.getPostedBy().equals(username)) {
            return ResponseEntity.badRequest().body("You cannot like your own memory.");
        }

        List<String> likedBy = memory.getLikedBy();
        if (likedBy.contains(username)) {
            // Unlike
            likedBy.remove(username);
            memory.setLikes(memory.getLikes() - 1);
        } else {
            // Like
            likedBy.add(username);
            memory.setLikes(memory.getLikes() + 1);
        }

        memory.setLikedBy(likedBy);
        memoryRepo.save(memory);
        return ResponseEntity.ok(memory);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> comment(@PathVariable String id, @RequestBody Map<String, String> body) {
        String commentText = body.get("comment");
        String username = body.get("username");

        if (commentText == null || commentText.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Comment cannot be empty");
        }

        Optional<Memory> optional = memoryRepo.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Memory memory = optional.get();

        if (memory.getPostedBy() != null && memory.getPostedBy().equals(username)) {
            return ResponseEntity.badRequest().body("You cannot comment on your own memory.");
        }

        Map<String, String> newComment = new HashMap<>();
        newComment.put("text", commentText);
        newComment.put("username", username);

        memory.getComments().add(newComment);
        memoryRepo.save(memory);

        return ResponseEntity.ok(memory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMemory(@PathVariable String id) {
        Optional<Memory> optional = memoryRepo.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        memoryRepo.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDescription(@PathVariable String id, @RequestBody Map<String, String> body) {
        Optional<Memory> optional = memoryRepo.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        String newDescription = body.get("description");
        if (newDescription == null || newDescription.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Description cannot be empty.");
        }

        Memory memory = optional.get();
        memory.setDescription(newDescription.trim());
        memoryRepo.save(memory);

        return ResponseEntity.ok(memory);
    }



}

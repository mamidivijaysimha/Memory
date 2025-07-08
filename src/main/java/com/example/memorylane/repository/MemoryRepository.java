package com.example.memorylane.repository;

import com.example.memorylane.model.Memory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemoryRepository extends MongoRepository<Memory, String> {
}

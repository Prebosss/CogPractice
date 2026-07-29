package com.example.bankapi.repos;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.bankapi.models.Account;

public interface AccountRepo extends MongoRepository<Account, String> {
    List<Account> findByUserId(String userId);
}
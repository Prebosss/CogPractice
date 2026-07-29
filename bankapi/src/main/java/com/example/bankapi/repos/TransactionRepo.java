package com.example.bankapi.repos;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.bankapi.models.Transaction;

public interface TransactionRepo extends MongoRepository<Transaction, String> {
    List<Transaction> findByAccountId(String accountId);
}

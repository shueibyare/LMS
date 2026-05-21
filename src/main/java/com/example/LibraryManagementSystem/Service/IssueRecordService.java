package com.example.LibraryManagementSystem.Service;

import com.example.LibraryManagementSystem.Entity.Book;
import com.example.LibraryManagementSystem.Entity.IssueRecord;
import com.example.LibraryManagementSystem.Entity.User;
import com.example.LibraryManagementSystem.Repository.BookRepository;
import com.example.LibraryManagementSystem.Repository.IssueRecordRepository;
import com.example.LibraryManagementSystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IssueRecordService {

    @Autowired
    private IssueRecordRepository issueRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public IssueRecord issueTheBook(Long bookId){

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if(book.getQuantity()<=0 || !book.getIsAvailable()) {
            throw new RuntimeException("Book is not available");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        IssueRecord issueRecord = new IssueRecord();
        issueRecord.setIssueDate(LocalDate.now());
        issueRecord.setDueDate(LocalDate.now().plusDays(14));
        issueRecord.setIsReturned(false);
        issueRecord.setUser(user);
        issueRecord.setBook(book);

        book.setQuantity(book.getQuantity()-1);
        if(book.getQuantity()==0) {
            book.setIsAvailable(false);
        }

        bookRepository.save(book);
        return issueRecordRepository.save(issueRecord);
    }

    public IssueRecord returnTheBook(Long issueRecordId){
        IssueRecord issueRecord = issueRecordRepository.findById(issueRecordId)
                .orElseThrow(()-> new RuntimeException("Issue Record not found"));

        if(issueRecord.getIsReturned()) {
            throw  new RuntimeException("Book is already retrned");
        }
        Book book = issueRecord.getBook();
        book.setQuantity(book.getQuantity()+1);
        book.setIsAvailable(true);
        bookRepository.save(book);

        issueRecord.setReturnDate(LocalDate.now());
        issueRecord.setIsReturned(true);

        return issueRecordRepository.save(issueRecord);

    }


}

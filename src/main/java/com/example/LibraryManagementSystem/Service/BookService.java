package com.example.LibraryManagementSystem.Service;

import com.example.LibraryManagementSystem.DTO.BookDTO;
import com.example.LibraryManagementSystem.Entity.Book;
import com.example.LibraryManagementSystem.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Book not found"));
        return book;
    }

    public Book addBook(BookDTO bookDTO){
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setIsAvailable(bookDTO.getIsAvailable());
        book.setQuantity(bookDTO.getQuantity());

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, BookDTO bookDTO){
        Book oldBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        oldBook.setTitle(bookDTO.getTitle());
        oldBook.setAuthor(bookDTO.getAuthor());
        oldBook.setIsbn(bookDTO.getIsbn());
        oldBook.setIsAvailable(bookDTO.getIsAvailable());
        oldBook.setQuantity(bookDTO.getQuantity());

        return bookRepository.save(oldBook);
    }

    public void deleteBookById(Long id){
        bookRepository.deleteById(id);
    }

}

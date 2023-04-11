package com.sofka.book.repository;


import com.sofka.book.model.Book;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BookRepository extends ReactiveCrudRepository<Book,String> {
}

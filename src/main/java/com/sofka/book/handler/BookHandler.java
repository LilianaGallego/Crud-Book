package com.sofka.book.handler;

import com.sofka.book.model.Book;
import com.sofka.book.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class BookHandler {
    private final BookRepository bookRepository;


    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public BookHandler(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Mono<ServerResponse> createBook(ServerRequest serverRequest){
        Mono<Book> bookMono = serverRequest.bodyToMono((Book.class));
        Mono response = bookMono.flatMap(book->
                ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(bookRepository.save(book), Book.class));

        return response;
    }

    public Mono<ServerResponse> getAllBooks(ServerRequest serverRequest){
        Mono response = ServerResponse.ok()
                .contentType((MediaType.APPLICATION_JSON))
                .body(bookRepository.findAll(), Book.class);
        return response;
    }

    public Mono<ServerResponse> getOneBook(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        Mono <Book> itemMono = bookRepository.findById(id);
        Mono response = itemMono.flatMap(item->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(item)
                        .switchIfEmpty(notFound));
        return response;

    }

    public Mono<ServerResponse> deleteBook(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        Mono <Void> deleteBook = bookRepository.deleteById(id);
        Mono response = ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deleteBook,Void.class);
        return response;

    }

    public Mono<ServerResponse> updateBook(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");

        Mono<Book> updatedBook = serverRequest.bodyToMono(Book.class).log("mono: ")
                .flatMap(book -> bookRepository.findById(id).log()
                        .flatMap(oldBook -> {
                            oldBook.setTitle(book.getTitle());
                            oldBook.setAuthor(book.getAuthor());
                            return bookRepository.save(oldBook).log();
                        }));

        Mono response = updatedBook.flatMap(book -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(book)))
                .switchIfEmpty(notFound);
        return response;

    }
}

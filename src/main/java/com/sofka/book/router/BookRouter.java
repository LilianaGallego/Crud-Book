package com.sofka.book.router;

import com.sofka.book.handler.BookHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class BookRouter {

    public RouterFunction<ServerResponse> routerFunction(BookHandler bookHandler){
        return RouterFunctions.route()
                .GET("/router/books",bookHandler::getAllBooks)
                .GET("/router/books/{id}",bookHandler::getOneBook)
                .POST("/router/books/",bookHandler::createBook)
                .PUT("/router/updateBook/{id}",bookHandler::updateBook)
                .DELETE("/router/deleteBook/{id}",bookHandler::deleteBook)
                .build();


    }
}

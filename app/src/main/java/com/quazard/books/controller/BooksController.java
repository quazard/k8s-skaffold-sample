package com.quazard.books.controller;

import com.quazard.books.model.Book;
import com.quazard.books.util.BookStorageSim;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BooksController {

    private BookStorageSim storageSim;

    public BooksController(
        final BookStorageSim storageSim
    ) {
        this.storageSim = storageSim;
    }


    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Flux<Book> getAll() {
        return storageSim.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Book>> findById(
        @PathVariable(name = "id") UUID id
    ) {
        return storageSim.findById(id)
            .map(ResponseEntity::ok)
            .onErrorResume(throwable -> Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Book> create(
        @RequestBody Book book
    ) {
        return storageSim.save(book);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Book>> update(
        @PathVariable(name = "id") UUID id,
        @RequestBody Book book
    ) {
        return storageSim.save(id, book)
            .map(ResponseEntity::ok)
            .onErrorResume(throwable -> Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> delete(
        @PathVariable(name = "id") UUID id
    ) {
        return storageSim.delete(id);
    }

}

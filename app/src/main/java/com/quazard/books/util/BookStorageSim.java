package com.quazard.books.util;

import com.quazard.books.model.Book;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class BookStorageSim {

    private final Map<UUID, Book> storage;

    public BookStorageSim() {
        this.storage = new HashMap<>();
    }

    public Flux<Book> getAll() {
        return Flux.fromIterable(storage.values());
    }

    public Mono<Book> findById(final UUID id) {
        return Mono.create(
            sink -> {
                if (!storage.containsKey(id)) {
                    sink.error(
                        new RuntimeException("Record not found with id: " + id.toString())
                    );
                }

                sink.success(storage.get(id));
            }
        );
    }

    public Mono<Book> save(final Book book) {
        return save(null, book);
    }

    public Mono<Book> save(final UUID id, final Book book) {
        return Mono.create(
            sink -> {
                if (id == null) {
                    storage.put(book.getId(), book);
                    sink.success(book);
                }

                if (!storage.containsKey(id)) {
                    sink.error(
                        new RuntimeException("Record not found with id: " + id.toString())
                    );
                }

                book.setId(id);
                storage.put(id, book);
                sink.success(book);
            }
        );
    }

    public Mono<Void> delete(final UUID id) {
        return Mono.just(id)
            .map(storage::remove)
            .then(Mono.empty());
    }

}

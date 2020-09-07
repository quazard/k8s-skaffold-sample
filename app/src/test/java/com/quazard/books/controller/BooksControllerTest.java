package com.quazard.books.controller;

import com.quazard.books.model.Book;
import com.quazard.books.util.BookStorageSim;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BooksControllerTest {

    @Mock
    private BookStorageSim storageSimMock;

    private BooksController booksController;

    @BeforeEach
    void setUp() {
        this.booksController = new BooksController(storageSimMock);
    }


    @Test
    @DisplayName("Return empty list when no data available")
    void whenNoData_onGetAll_thenOk() {
        when(storageSimMock.getAll()).thenReturn(Flux.empty());

        StepVerifier.create(
            booksController.getAll()
        )
            .expectNextCount(0)
            .verifyComplete();
    }

    @Test
    @DisplayName("Return a list with resources when data is available")
    void whenData_onGetAll_thenSuccess() {
        List<Book> books = new ArrayList<>();
        Book bookMock = mock(Book.class);

        books.add(bookMock);

        when(storageSimMock.getAll()).thenReturn(Flux.fromIterable(books));

        StepVerifier.create(
            booksController.getAll()
        )
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    @DisplayName("Return an error when resource for given id is not found")
    void whenNoData_onFindById_thenError() {
        when(storageSimMock.findById(any(UUID.class))).thenReturn(Mono.error(new RuntimeException("dummy error")));

        StepVerifier.create(
            booksController.findById(UUID.randomUUID())
        )
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    @DisplayName("Return found resource by given id")
    void whenData_onFindById_thenSuccess() {
        Book bookMock = mock(Book.class);

        when(storageSimMock.findById(any(UUID.class))).thenReturn(Mono.just(bookMock));

        StepVerifier.create(
            booksController.findById(UUID.randomUUID())
        )
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    @DisplayName("Return stored resource")
    void withValidData_onCreate_thenSuccess() {
        Book bookMock = mock(Book.class);

        when(storageSimMock.save(any(Book.class))).thenReturn(Mono.just(bookMock));

        StepVerifier.create(
            booksController.create(bookMock)
        )
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    @DisplayName("Return an error when resource for given id is not found")
    void whenNoData_onUpdate_thenError() {
        Book bookMock = mock(Book.class);

        when(storageSimMock.save(any(UUID.class), any(Book.class)))
            .thenReturn(Mono.error(new RuntimeException("dummy error")));

        StepVerifier.create(
            booksController.update(UUID.randomUUID(), bookMock)
        )
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    @DisplayName("Return updated resource by given id")
    void withValidData_onUpdate_thenSuccess() {
        Book bookMock = mock(Book.class);

        when(storageSimMock.save(any(UUID.class), any(Book.class)))
            .thenReturn(Mono.just(bookMock));

        StepVerifier.create(
            booksController.update(UUID.randomUUID(), bookMock)
        )
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    @DisplayName("Return empty regardless of deleted resource")
    void onDelete_thenSuccess() {
        when(storageSimMock.delete(any(UUID.class))).thenReturn(Mono.empty());

        StepVerifier.create(
            booksController.delete(UUID.randomUUID())
        )
            .expectNextCount(0)
            .verifyComplete();
    }

}

package com.botscrew.repository;

import com.botscrew.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {


    List<Book> findByNameOrderByAuthorAsc(String name);

    List<Book> findAllByOrderByNameAsc();
}

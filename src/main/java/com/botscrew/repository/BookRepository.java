package com.botscrew.repository;

import com.botscrew.model.Book;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findByNameOrderByAuthorAsc(String name);

    List<Book> findAllByOrderByNameAsc();
}

package com.botscrew.repository;

import com.botscrew.model.Book;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface BookRepository extends CrudRepository<Book, Long> {
}

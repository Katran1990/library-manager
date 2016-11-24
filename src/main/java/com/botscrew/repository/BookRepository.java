package com.botscrew.repository;

import com.botscrew.model.Book;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface BookRepository extends CrudRepository<Book, Long> {

    String COMMAND_LIST_MESSAGE ="Add a book to library - add [author's name]/[book title]\n" +
            "Edit/delete a book - edit book/delete [book title]\n" +
            "Get all books from the library - all books";
    String NEW_COMMAND_MESSAGE = "Please, enter your command: ";
    String WRONG_COMMAND_MESSAGE = "Wrong format of command. Please, enter one of these commands";

    Book findOneByName(String name);

    List<Book> findByNameOrderByAuthor(String name);

    List<Book> findAllOrderByNameASC();
}

package com.botscrew.service;

import com.botscrew.model.Book;
import com.botscrew.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static com.botscrew.repository.BookRepository.*;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public void start() {
        System.out.println("\nWelcome to library manager!\n" +
                "Below is a list of commands for library managing:\n" +
                COMMAND_LIST_MESSAGE);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            String author;
            String name;
            while (true) {

                input = reader.readLine();

                if (input.equals("exit")) {
                    System.out.println("Closing an application...");
                    System.exit(0);
                }
                if (input.isEmpty()) {
                    System.out.println("Please, enter one of these commands:\n" + COMMAND_LIST_MESSAGE);
                    continue;
                }


                if (input.startsWith("add ")) {
                    String command = input.replaceFirst("add ", "");
                    if (!command.matches("^(?:[^/]+)/(?:[^/]+)$")) {
                        System.out.println(WRONG_COMMAND_MESSAGE + "\n" + COMMAND_LIST_MESSAGE);
                        continue;
                    }
                    author = command.substring(0, command.indexOf("/"));
                    name = command.substring(command.indexOf("/") + 1, command.length());
                    Book book = repository.save(new Book(author, name));
                    System.out.println("Book \"" + book.getAuthor() + " : " + book.getName() + "\" was added\n" + NEW_COMMAND_MESSAGE);
                }


                if (input.startsWith("remove ")) {

                    String command = input.replaceFirst("remove ", "");

                    if (command.isEmpty()) {
                        System.out.println(WRONG_COMMAND_MESSAGE + "\n" + COMMAND_LIST_MESSAGE);
                        continue;
                    }

                    name = command.substring(0, command.length());
                    Book book = repository.findOneByName(name);

                    if (book == null) {
                        System.out.println("Book with this name is absent in our library.\n" +
                                "Please, enter one of these commands\n" + COMMAND_LIST_MESSAGE);
                        continue;
                    }

                    repository.delete(book.getId());
                    System.out.println("Book \"" + book.getAuthor() + " : " + book.getName() + "\" was removed\n" + NEW_COMMAND_MESSAGE);
                }


                if (input.startsWith("edit book ")) {

                    String command = input.replaceFirst("edit book ", "");

                    if (command.isEmpty()) {
                        System.out.println(WRONG_COMMAND_MESSAGE + "\n" + COMMAND_LIST_MESSAGE);
                        continue;
                    }

                    name = command.substring(0, command.length());
                    List<Book> books = repository.findByNameOrderByAuthor(name);

                    if (books.size() > 1) {
                        System.out.println("We have few books with such name please choose one by typing a number of book:");

                        for (int i = 0; i < books.size(); i++) {
                            System.out.println(i + 1 + ". " + books.get(i).getAuthor() + " : " + books.get(i).getName());
                        }

                        input = reader.readLine();

                        if (input.isEmpty() || Integer.parseInt(input) < 0 || Integer.parseInt(input) > books.size()) {
                            System.out.println("Wrong number\n" + NEW_COMMAND_MESSAGE);
                            continue;
                        }
                    } else {
                        Book book = books.get(0);

                        if (book == null) {
                            System.out.println("Book with this name is absent in our library.\n" +
                                    "Please, enter one of these commands\n" + COMMAND_LIST_MESSAGE);
                            continue;
                        }

                        System.out.println("Book \"" + book.getAuthor() + " : " + book.getName() + "\" was found\n" +
                                "Enter a new name for the book, please: ");

                        if ((name = reader.readLine()).isEmpty()) {
                            System.out.println("Wrong name\n" + NEW_COMMAND_MESSAGE);
                            continue;
                        }
                        book.setName(name);
                        book = repository.save(book);
                        System.out.println("Book \"" + book.getAuthor() + " : " + book.getName() + "\" was saved\n" + NEW_COMMAND_MESSAGE);
                    }
                }


                if (input.startsWith("all books ")) {
                    System.out.println("Our books:");
                    for (Book book : repository.findAllOrderByNameASC()) {
                        System.out.println(book.getAuthor() + " : " + book.getName());
                    }
                }
                System.out.println(NEW_COMMAND_MESSAGE);
            }

        } catch (Exception e) {
            System.out.println("Error has been occurred");
            System.err.println(e.getCause() + " : " + e.getMessage());
            System.exit(1);
        }

    }

}

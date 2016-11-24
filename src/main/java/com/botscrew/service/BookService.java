package com.botscrew.service;

import com.botscrew.model.Book;
import com.botscrew.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class BookService {
    public static final String COMMAND_LIST_MESSAGE = "* Add a book to library - add [author's name]/[book title]\n" +
            "* Edit/delete a book - edit book/delete [book title]\n" +
            "* Get all books from the library - all books";
    public static final String NEW_COMMAND_MESSAGE = "Enter your command: ";
    public static final String WRONG_COMMAND_MESSAGE = "Wrong format of command! Please, enter one of these commands: ";

    @Autowired
    private BookRepository repository;

    public void start() {
        System.out.println("\nWelcome to library manager!\n" +
                "Below is a list of commands for library managing:\n" +
                COMMAND_LIST_MESSAGE);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String consoleInput;

            while (true) {
                System.out.print(NEW_COMMAND_MESSAGE);
                consoleInput = reader.readLine();

                if (consoleInput.equals("exit")) {
                    System.out.println("Closing an application...");
                    System.exit(0);
                }

                if (consoleInput.isEmpty()) {
                    System.out.println("Please, enter one of these commands:\n" + COMMAND_LIST_MESSAGE);
                    continue;
                }

                if (consoleInput.matches("^add (?:[^/]+)/(?:[^/]+)$")) {
                    Book newBook = addingNewBook(consoleInput);
                    System.out.println("Book \"" + newBook.getAuthor() + " : " + newBook.getName() + "\" was added");
                } else if (consoleInput.matches("^remove (?:.*)")) {
                    Book bookFound;
                    if ((bookFound = removeBook(reader, consoleInput)) == null) {
                        continue;
                    }
                    System.out.println("Book \"" + bookFound.getAuthor() + " : " + bookFound.getName() + "\" was removed");
                } else if (consoleInput.matches("^edit book (?:.*)")) {
                    Book bookFound;
                    if ((bookFound = editBook(reader, consoleInput)) == null) {
                        continue;
                    }
                    System.out.println("Book \"" + bookFound.getAuthor() + " : " + bookFound.getName() + "\" was saved");
                } else if (consoleInput.matches("^all books$")) {
                    showAllBooks();
                } else {
                    System.out.println(WRONG_COMMAND_MESSAGE + "\n" + COMMAND_LIST_MESSAGE);
                }
            }
        } catch (IOException e) {
            System.out.println("Error has been occurred");
            System.err.println(e.getCause() + " : " + e.getMessage());
            System.exit(1);
        }
    }

    private Book addingNewBook(String consoleInput) {
        String bookInfo = consoleInput.replaceFirst("add ", "");
        String author = bookInfo.substring(0, bookInfo.indexOf("/"));
        String bookName = bookInfo.substring(bookInfo.indexOf("/") + 1, bookInfo.length());
        return repository.save(new Book(author, bookName));
    }

    private Book removeBook(BufferedReader reader, String consoleInput) throws IOException {
        String bookInfo = consoleInput.replaceFirst("remove ", "");

        if (bookInfo.isEmpty()) {
            System.out.println(WRONG_COMMAND_MESSAGE + "\n" + COMMAND_LIST_MESSAGE);
            return null;
        }

        String bookName = bookInfo.substring(0, bookInfo.length());
        List<Book> booksFound = repository.findByNameOrderByAuthorAsc(bookName);

        if (booksFound.isEmpty()) {
            System.out.println("Books with this name are absent in our library.");
            return null;
        }

        Book bookFound;
        if ((bookFound = getBookFromLibrary(booksFound, reader)) == null) {
            return null;
        }

        repository.delete(bookFound.getId());
        return bookFound;
    }

    private Book editBook(BufferedReader reader, String consoleInput) throws IOException {
        String bookInfo = consoleInput.replaceFirst("edit book ", "");

        if (bookInfo.isEmpty()) {
            System.out.println(WRONG_COMMAND_MESSAGE + "\n" + COMMAND_LIST_MESSAGE);
            return null;
        }

        String bookName = bookInfo.substring(0, bookInfo.length());
        List<Book> booksFound = repository.findByNameOrderByAuthorAsc(bookName);

        if (booksFound.isEmpty()) {
            System.out.println("Books with this name are absent in our library.");
            return null;
        }

        Book bookFound;
        if ((bookFound = getBookFromLibrary(booksFound, reader)) == null) {
            return null;
        }

        System.out.println("Enter a new name for the book, please:");

        if ((bookName = reader.readLine()).isEmpty()) {
            System.out.println("Wrong name");
            return null;
        }
        bookFound.setName(bookName);
        return repository.save(bookFound);
    }

    private void showAllBooks() {
        List<Book> books = repository.findAllByOrderByNameAsc();
        if (books.isEmpty()) {
            System.out.println("The library is empty");
            return;
        }
        System.out.println("Our books:");
        for (Book book : books) {
            System.out.println("* " + book.getAuthor() + " : " + book.getName());
        }
    }


    private Book getBookFromLibrary(List<Book> booksFound, BufferedReader reader) throws IOException {
        if (booksFound.size() > 1) {
            System.out.println("We have few books with such name please choose one by typing a number of book:");

            for (int i = 0; i < booksFound.size(); i++) {
                System.out.println(i + 1 + ". " + booksFound.get(i).getAuthor() + " : " + booksFound.get(i).getName());
            }

            int number;
            try {
                number = Integer.parseInt(reader.readLine());
                return booksFound.get(number - 1);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Wrong number");
                return null;
            }
        } else {
            return booksFound.get(0);
        }
    }


}

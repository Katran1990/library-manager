package com.botscrew.service;

import com.botscrew.model.Book;
import com.botscrew.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
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

                //>>Ready
                if (consoleInput.matches("^add (?:[^/]+)/(?:[^/]+)$")) {

                    String bookInfo = consoleInput.replaceFirst("add ", "");
                    String author = bookInfo.substring(0, bookInfo.indexOf("/"));
                    String bookName = bookInfo.substring(bookInfo.indexOf("/") + 1, bookInfo.length());
                    Book newBook = repository.save(new Book(author, bookName));
                    System.out.println("Book \"" + newBook.getAuthor() + " : " + newBook.getName() + "\" was added");

                } else
                if (consoleInput.matches("^remove (?:.*)")) {

                    String bookInfo = consoleInput.replaceFirst("remove ", "");

                    if (bookInfo.isEmpty()) {
                        System.out.println(WRONG_COMMAND_MESSAGE + "\n" + COMMAND_LIST_MESSAGE);
                        continue;
                    }

                    String bookName = bookInfo.substring(0, bookInfo.length());
                    List<Book> booksFound = repository.findByNameOrderByAuthorAsc(bookName);

                    if (booksFound.isEmpty()){
                        System.out.println("Books with this name are absent in our library.");
                        continue;
                    }

                    Book bookFound;
                    if ((bookFound = getBookFromLibrary(booksFound, reader))==null){
                        continue;
                    }

                    repository.delete(bookFound.getId());
                    System.out.println("Book \"" + bookFound.getAuthor() + " : " + bookFound.getName() + "\" was removed");

                }else
                if (consoleInput.matches("^edit book (?:.*)")) {

                    String bookInfo = consoleInput.replaceFirst("edit book ", "");

                    if (bookInfo.isEmpty()) {
                        System.out.println(WRONG_COMMAND_MESSAGE + "\n" + COMMAND_LIST_MESSAGE);
                        continue;
                    }

                    String bookName = bookInfo.substring(0, bookInfo.length());
                    List<Book> booksFound = repository.findByNameOrderByAuthorAsc(bookName);

                    if (booksFound.isEmpty()){
                        System.out.println("Books with this name are absent in our library.");
                        continue;
                    }

                    Book bookFound;
                    if ((bookFound = getBookFromLibrary(booksFound, reader))==null){
                        continue;
                    }

                    System.out.println("Enter a new name for the book, please:");

                    if ((bookName = reader.readLine()).isEmpty()) {
                        System.out.println("Wrong name");
                        continue;
                    }
                    bookFound.setName(bookName);
                    bookFound = repository.save(bookFound);
                    System.out.println("Book \"" + bookFound.getAuthor() + " : " + bookFound.getName() + "\" was saved");

                }else
                if (consoleInput.matches("^all books$")) {

                    List<Book> books = repository.findAllByOrderByNameAsc();
                    if (books.isEmpty()) {
                        System.out.println("The library is empty");
                        continue;
                    }
                    System.out.println("Our books:");
                    for (Book book : books) {
                        System.out.println("* "+book.getAuthor() + " : " + book.getName());
                    }

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

    private Book getBookFromLibrary(List<Book> booksFound, BufferedReader reader) throws IOException {
        if (booksFound.size()>1) {
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

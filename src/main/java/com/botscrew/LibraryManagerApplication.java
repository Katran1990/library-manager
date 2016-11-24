package com.botscrew;

import com.botscrew.service.BookService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryManagerApplication{

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagerApplication.class, args).getBean(BookService.class).start();
    }

}

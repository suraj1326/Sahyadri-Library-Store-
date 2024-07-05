package com.bookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.bookStore.entity.Book;
import com.bookStore.entity.MyBookList;
import com.bookStore.service.BookService;
import com.bookStore.service.MyBookListService;

import java.util.*;

@Controller
public class BookController {
	
	@Autowired
	private BookService service;
	
	@Autowired
	private MyBookListService myBookService;
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/book_register")
	public String bookRegister() {
		return "bookRegister";
	}
	
	@GetMapping("/available_books")
	public ModelAndView getAllBook() {
		List<Book>list=service.getAllBook();
//		ModelAndView m=new ModelAndView();
//		m.setViewName("bookList");
//		m.addObject("book",list);
		return new ModelAndView("bookList","book",list);
	}
	
	

	@PostMapping("/save")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        service.save(book);
        return ResponseEntity.ok().body("Book added successfully");
    }

	@PostMapping("/save/bookform")
    public String addBookForm(@ModelAttribute Book book) {
        service.save(book);
        return "redirect:/available_books";
    }

	@GetMapping("/book/{id}")
public ResponseEntity<Book> getBookById(@PathVariable("id") int id) {
    // Retrieve the book from the service layer based on the provided ID
    Book book = service.getBookById(id);

    // Check if the book exists
    if (book != null) {
        // Return a ResponseEntity with the book and HTTP status 200 OK
        return ResponseEntity.ok().body(book);
    } else {
        // If the book does not exist, return HTTP status 404 Not Found
        return ResponseEntity.notFound().build();
    }
}
@GetMapping("/books")
public ResponseEntity<List<Book>> getAllBooks() {
    // Retrieve the list of all books from the service layer
    List<Book> books = service.getAllBook();

    // Check if any books are found
    if (!books.isEmpty()) {
        // Return a ResponseEntity with the list of books and HTTP status 200 OK
        return ResponseEntity.ok().body(books);
    } else {
        // If no books are found, return HTTP status 404 Not Found
        return ResponseEntity.notFound().build();
    }
}

	@GetMapping("/my_books")
	public String getMyBooks(Model model)
	{
		List<MyBookList>list=myBookService.getAllMyBooks();
		model.addAttribute("book",list);
		return "myBooks";
	}
	@PutMapping("/update/{id}")
    public ResponseEntity<String> updateBook(@PathVariable("id") int id, @RequestBody Book book) {
        Book existingBook = service.getBookById(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }
        
        existingBook.setName(book.getName());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        
        service.save(existingBook);
        
        return ResponseEntity.ok().body("Book updated successfully");
    }
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletebook(@PathVariable("id") int id) {
        Book existingBook = service.getBookById(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }
        
        service.deleteById(id);
        
        return ResponseEntity.ok().body("Book deleted successfully");
    }
	@RequestMapping("/mylist/{id}")
	public String getMyList(@PathVariable("id") int id) {
		Book b=service.getBookById(id);
		MyBookList mb=new MyBookList(b.getId(),b.getName(),b.getAuthor(),b.getPrice());
		myBookService.saveMyBooks(mb);
		return "redirect:/my_books";
	}
	
	@RequestMapping("/editBook/{id}")
	public String editBook(@PathVariable("id") int id,Model model) {
		Book b=service.getBookById(id);
		model.addAttribute("book",b);
		return "bookEdit";
	}
	@RequestMapping("/deleteBook/{id}")
	public String deleteBook(@PathVariable("id")int id) {
		service.deleteById(id);
		return "redirect:/available_books";
	}
	
}

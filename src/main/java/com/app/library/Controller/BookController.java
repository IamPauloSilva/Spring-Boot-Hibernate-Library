package com.app.library.Controller;

import com.app.library.Model.Book;
import com.app.library.Service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/listbooks")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "listbooks";
    }

    @GetMapping("/addbook")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "addbook";
    }

    @PostMapping("/addbook")
    public String addBook(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/listbooks";
    }

    @GetMapping("/editbook/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "editbook";
    }

    @PostMapping("/editbook")
    public String editBook(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/listbooks";
    }

    @GetMapping("/deletebook/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/listbooks";
    }
}
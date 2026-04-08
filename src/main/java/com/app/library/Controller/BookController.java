package com.app.library.Controller;

import com.app.library.Model.Book;
import com.app.library.Service.BookService;
import com.app.library.Service.CoverStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping
public class BookController {

    private final BookService bookService;
    private final CoverStorageService coverStorageService;

    public BookController(BookService bookService, CoverStorageService coverStorageService) {
        this.bookService = bookService;
        this.coverStorageService = coverStorageService;
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
    public String addBook(@ModelAttribute Book book,
                          @RequestParam("coverFile") MultipartFile coverFile,
                          Model model) {
        try {
            book.setCoverImagePath(coverStorageService.storeCover(coverFile, null));
            bookService.save(book);
            return "redirect:/listbooks";
        } catch (IllegalArgumentException | IOException ex) {
            model.addAttribute("book", book);
            model.addAttribute("uploadError", ex.getMessage());
            return "addbook";
        }
    }

    @GetMapping("/editbook/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "editbook";
    }

    @PostMapping("/editbook")
    public String editBook(@ModelAttribute Book book,
                           @RequestParam("coverFile") MultipartFile coverFile,
                           Model model) {
        Book existingBook = bookService.findById(book.getId());

        if (existingBook == null) {
            model.addAttribute("book", book);
            model.addAttribute("uploadError", "Livro não encontrado.");
            return "editbook";
        }

        try {
            String coverPath = coverStorageService.storeCover(coverFile, existingBook.getCoverImagePath());
            book.setCoverImagePath(coverPath);
            bookService.save(book);
            return "redirect:/listbooks";
        } catch (IllegalArgumentException | IOException ex) {
            book.setCoverImagePath(existingBook.getCoverImagePath());
            model.addAttribute("book", book);
            model.addAttribute("uploadError", ex.getMessage());
            return "editbook";
        }
    }

    @GetMapping("/deletebook/{id}")
    public String deleteBook(@PathVariable Long id) {
        Book existingBook = bookService.findById(id);
        if (existingBook != null) {
            try {
                coverStorageService.deleteByPublicPath(existingBook.getCoverImagePath());
            } catch (IOException ignored) {
                // If file deletion fails, keep DB delete behavior unchanged.
            }
        }
        bookService.deleteById(id);
        return "redirect:/listbooks";
    }
}
package Ejercicio01_firebase;

import java.util.ArrayList;
import java.util.List;

public class Author {

    private final String name;
    private final String id;
    private List<Book> books = new ArrayList<>();


    public Author(String id,String name){

        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    @Override
    public String toString(){
        return String.format("Document %s - El autor %s posee los siguientes libros: %s",
                id, name, String.join( ", ", books.stream().map(Book::toString).toList()));
    }
}

package Ejercicio01_firebase;

import java.time.Year;
import java.util.List;

public class Frontend {
    private final Backend b = new Backend();

    public static void main(String[] args) {
        new Frontend().show();
    }

    private void show() {
        List<Book> books = List.of(
                new Book("Don Quixote de la Mancha", Year.of(1605)),
                new Book("La gitanilla", Year.of(1613)));
        Author author = new Author("12346", "Miguel de Cervantes");
        author.setBooks(books);
        //b.createAuthor(author.getId(), author.getName(), author.getBooks());
        //b.getAuthors();
        b.deleteAuthor(author.getId());
    }
}

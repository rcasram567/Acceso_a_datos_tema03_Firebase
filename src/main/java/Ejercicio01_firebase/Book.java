package Ejercicio01_firebase;

import java.time.Year;

public class Book {

    String title;
    Year year;
    public Book(String title, Year year){
        this.title = title;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public Year getYear() {
        return year;
    }

    @Override
    public String toString(){
        return String.format("Libro titulado %s del año %s",title,year.toString());
    }
}

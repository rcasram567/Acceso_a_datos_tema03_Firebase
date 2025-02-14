package Ejercicio01_firebase;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Backend {

    private final Firestore db;

    public Backend(){
        initializeFirebase();
        db = FirestoreClient.getFirestore();
    }

    public static void initializeFirebase()  {

        Path serviceAccountPath = Paths.get("src","main","resources", "serviceAccountKey.json");
        try (FileInputStream serviceAccount = new FileInputStream(serviceAccountPath.toFile());){
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    void createAuthor(String authorId, String name, List<Book> books)  {

        DocumentReference authorRef = db.collection("authors").document(authorId);
        CollectionReference booksSubcollect  = authorRef.collection("books");
        Map<String, Object> docData = new HashMap<>();
        ApiFuture<WriteResult> insert;
        docData.put("name", name);
        insert = authorRef.set(docData);
        try {
            insert.get();
            for (Book book : books) {
                Map<String, Object> bookData = new HashMap<>();
                bookData.put("title", book.getTitle());
                bookData.put("year", book.getYear().toString());
                booksSubcollect.add(bookData).get();
            }

        } catch (InterruptedException e) {
            System.err.println("InterruptedException");
            showError(e);
        } catch (ExecutionException e) {
            System.err.println("ExecutionException");
            showError(e);
        }
    }
    void getAuthors(){
        ApiFuture<QuerySnapshot> futureAuthor = db.collection("authors").get();
        List<QueryDocumentSnapshot> documentsAuthor = null;

        try {
            documentsAuthor = futureAuthor.get().getDocuments();
            for (DocumentSnapshot document : documentsAuthor) {
                Map<String, Object> authorData = document.getData();
                Author author = new Author(document.getId(),authorData.get("name").toString());


                DocumentReference docRef = db.collection("authors").document(document.getId());
                CollectionReference subcollectRef = docRef.collection("books");
                ApiFuture<QuerySnapshot> futureSubcollect = subcollectRef.get();
                List<QueryDocumentSnapshot> booksDocuments = futureSubcollect.get().getDocuments();
                for (DocumentSnapshot bookDocument : booksDocuments) {
                    Map<String, Object> bookData = bookDocument.getData();
                    assert bookData != null;
                    author.addBook(new Book(bookData.get("title").toString(), Year.of(Integer.parseInt(bookData.get("year").toString()))));
                }

                System.out.println(author);

            }
        } catch (InterruptedException e) {
            System.err.println("InterruptedException");
            showError(e);
        } catch (ExecutionException e) {
            System.err.println("ExecutionException");
            showError(e);
        }

    }

    void deleteAuthor(String authorId){
        DocumentReference docRef = db.collection("authors").document(authorId);

        ApiFuture<WriteResult> future = docRef.delete();

        try {
            future.get();
            System.out.println("Documento eliminado con éxito.");
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error al eliminar el documento: " + e.getMessage());
        }
    }

    private void showError(Throwable e) {
        System.out.println(e.toString());
    }
}

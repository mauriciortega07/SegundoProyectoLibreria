package com.ebac.segundoProyectoJava.service.MenuSelectorCaseMethods;

import com.ebac.segundoProyectoJava.dao.CrudOperations;
import com.ebac.segundoProyectoJava.exceptions.ExcepcionesPersonalizadas;
import com.ebac.segundoProyectoJava.dao.Library;
import com.ebac.segundoProyectoJava.model.Author;
import com.ebac.segundoProyectoJava.model.Book;
import com.ebac.segundoProyectoJava.service.VerificateStringMethods;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.ebac.segundoProyectoJava.service.MenuSelector.scanner;

public class BooksMethods {

    private static CrudOperations<Book> bookCrudOperations;
    private static String respuestaUsuario;

    //METODO QUE REALIZA TODO EL PROCESO DE NUEVO REGISTRO
    public static void addBook(EntityManager entityManager) {
        System.out.println("..... Agregar un libro a la biblioteca ....");

        bookCrudOperations = new Library<>(Book.class, entityManager);

        //ESTABLECE EL NOMBRE DEL LIBRO
        String bookName;
        System.out.println("Ingresa el nombre del libro: ");
        bookName = scanner.nextLine();

        //ESTABLECE EL NOMBRE COMPLETO DEL AUTOR
        String authorName;
        do {
            System.out.println("Ingresa el nombre completo del autor: ");
            authorName = scanner.nextLine().toLowerCase().trim();

            if (VerificateStringMethods.isNumber(authorName)) {
                System.out.println("Error debes ingresar un nombre valido");
            }

        } while (VerificateStringMethods.isNumber(authorName));

        //ESTABLECE EL AÑO DE LANZAMIENTO
        String releaseYearTxt;
        do {
            System.out.println("Ingresa el año de lanzamiento del libro: ");
            releaseYearTxt = scanner.nextLine();

            if (!releaseYearTxt.matches("\\d{4}")) {
                System.out.println("Deben ser cuatro valores numericos");
            }
        } while (!releaseYearTxt.matches("\\d{4}"));

        int releaseYear = Integer.parseInt(releaseYearTxt);

        //ESTABLECE EL CODIGO ISBN
        String isbnCode;
        do {
            System.out.println("Ingresa el codigo ISBN del libro: ");
            isbnCode = scanner.nextLine();

            if (!isbnCode.matches("\\d{4}")) {
                System.out.println("Deben ser cuatro valores numericos");
            }
        } while (!isbnCode.matches("\\d{4}"));


        //SE CREA EL OBJETO BOOK A GUARDAR EN LA BD EN LA TABLA BOOK
        Book newBook = new Book();

        //ESTABLECE LOS VALORES CAPTURADOS
        newBook.setTitleBook(bookName);
        newBook.setReleaseYear(releaseYear);
        newBook.setIsbnCode(isbnCode);

        /*SE BUSCA SI YA UN AUTOR CREADO, SI LO HAY, SE TOMA SU NOMBRE
         * Y SI NO SE CREA UNO NUEVO*/
        CrudOperations<Author> authorCrudOperations = new Library<>(Author.class, entityManager);
        Optional<List<Author>> authorsList = authorCrudOperations.getAll();

        Author authorFound = null;

        if (authorsList.isPresent()) {
            for (Author authorOnList : authorsList.get()) {
                if (authorOnList.getCompleteName().equalsIgnoreCase(authorName)) {
                    authorFound = authorOnList;
                    break;
                }
            }
        }

        if (authorFound == null) {
            System.out.println("Autor no registrado, ¿Deseas crearlo? (si/no)");
            String respuestaUsuario = scanner.nextLine();

            while (!respuestaUsuario.equalsIgnoreCase("si") && !respuestaUsuario.equalsIgnoreCase("no")){
                System.out.println("Ingresa si o no para continuar : ");
                respuestaUsuario = scanner.nextLine();
            }
            //scanner.nextLine();
            switch (respuestaUsuario) {
                case "si":
                    Author newAuthor = new Author();

                    System.out.println("Nombre de autor Capturado! ");
                    newAuthor.setCompleteName(authorName);

                    System.out.println("Ingresa su biografia: ");
                    String biography = scanner.nextLine();
                    newAuthor.setBiography(biography);

                    newAuthor.addBookPublished(newBook);
                    newBook.setAuthorBook(newAuthor);

                    try {
                        authorCrudOperations.save(newAuthor);
                        newAuthor.addBookPublished(newBook);
                        System.out.println("AUTOR GUARDADO CON EXITO");
                        System.out.println("LIBRO GUARDADO CON EXITO");
                        scanner.nextLine();

                    } catch (Exception e) {
                        throw new ExcepcionesPersonalizadas("ERROR AL AGREGAR EL AUTOR DESDE EL REGISTRO DE LIBROS", e);
                    }
                    scanner.nextLine();
                    break;
                case "no":
                    System.out.println("OPERACION CANCELADA");
                    break;
                default:
                    System.out.println("Ingresa un respuesta correcta");
            }
            return;
        } else {
            newBook.setAuthorBook(authorFound);
        }

        try {
            bookCrudOperations.save(newBook);

            System.out.println("LIBRO GUARDADO CON EXITO");
            scanner.nextLine();
        } catch (Exception e) {
            throw new ExcepcionesPersonalizadas("ERROR AL GUARDADR EL REGISTRO", e);
        }

        scanner.nextLine();
    }

    //METODO QUE REALIZA TODO EL PROCESO PARA OBTENER LA LISTA DE REGISTROS DESDE LA BD
    public static void getListBooks(EntityManager entityManager) {
        System.out.println("..... Consultar lista de libros .....");

        bookCrudOperations = new Library<>(Book.class, entityManager);

        Optional<List<Book>> allRegisters = bookCrudOperations.getAll();
        if (allRegisters.isPresent()) {
            List<Book> listBooks = allRegisters.get();
            if (listBooks.isEmpty()) {
                System.out.println("NO HAY LIBROS GUARDADOS");
            } else {
                System.out.println("..... Lista de libros existentes .....");
                for (Book book : listBooks) {
                    System.out.println("- " + book);
                }
            }
        } else {
            System.out.println("NO SE PUDO OBTENER LA LISTA DE LIBROS");
        }
        scanner.nextLine();
    }

    //METODO QUE REALIZA TODO EL PROCESO PARA EDITAR UN ELEMENTO DE LOS REGISTROS
    public static void updateBookByISBN(EntityManager entityManager) {
        System.out.println("..... Actualizar un elemento de la lista de libros .....");

        bookCrudOperations = new Library<>(Book.class, entityManager);
        Optional<List<Book>> bookList = bookCrudOperations.getAll();

        if (bookList.isEmpty() || bookList.get().isEmpty()){
            System.out.println("No hay libros registrados");
            scanner.nextLine();
            return;
        }

        for(Book book : bookList.get()){
            System.out.println(book);
        }
        System.out.println("Ingresa el ISBN del libro a modificar: ");
        String isbn = scanner.nextLine();

        Library<Book> bookCrudOperations = new Library<>(Book.class, entityManager);
        Optional<Book> bookFound = bookCrudOperations.getByISBN(isbn);

        bookFound.ifPresentOrElse(bookUpdate -> {
            System.out.println("Libro encontrado = " + bookUpdate);

            //EDITA EL NOMBRE DEL LIBRO
            System.out.println("¿Deseas editar el nombre del libro (si/no)?: ");
            System.out.println("Titulo Actual: " + bookUpdate.getTitleBook());
            respuestaUsuario = scanner.nextLine().trim().toLowerCase();

            while (!respuestaUsuario.equals("si") && !respuestaUsuario.equals("no")) {
                System.out.println("Respuesta invalida Ingresa 'si' o 'no': ");
                respuestaUsuario = scanner.nextLine().trim().toLowerCase();
            }

            switch (respuestaUsuario) {
                case "si":
                    try {
                        System.out.println("Ingresa el nuevo titulo: ");
                        String newBookTitle = scanner.nextLine().trim().toLowerCase();

                        bookUpdate.setTitleBook(newBookTitle);
                        bookCrudOperations.update(bookUpdate);
                        System.out.println("--- CAMPO ACTUALIZADO ---");
                    } catch (Exception e) {
                        throw new ExcepcionesPersonalizadas("Error al editar el campo", e);
                    }
                    break;

                case "no":
                    System.out.println("---SIN CAMBIOS PARA ESTE CAMPO---");
                    break;
                default:
                    System.out.println("INGRESA UN OPCION VALIDA 'si' O 'no'");
            }

            //EDITA EL AUTOR
            Author autorActual = bookUpdate.getAuthorBook();
            System.out.println("¿Deseas editar el nombre del autor (si/no)?: ");
            System.out.println("Autor actual: " + autorActual.getCompleteName());
            respuestaUsuario = scanner.nextLine().trim().toLowerCase();;

            while (!respuestaUsuario.equals("si") && !respuestaUsuario.equals("no")) {
                System.out.println("Respuesta invalida ingresa 'si' o 'no'");
                respuestaUsuario = scanner.nextLine().trim().toLowerCase();
            }

            switch (respuestaUsuario) {
                case "si":
                    try {
                        System.out.println("Ingresa el nuevo nombre: ");
                        String newAuthorBookName = scanner.nextLine();
                        autorActual.setCompleteName(newAuthorBookName);

                        Library<Author> crudAuthor = new Library<>(Author.class, entityManager);
                        crudAuthor.update(autorActual);

                        System.out.println("--- CAMPO ACTUALIZADO ---");
                    } catch (Exception e) {
                        throw new ExcepcionesPersonalizadas("Error al editar el campo", e);
                    }
                    break;
                case "no":
                    System.out.println("---SIN CAMBIOS PARA ESTE CAMPO---");
                    break;
                default:
                    System.out.println("INGRESA UN OPCION VALIDA 'si' O 'no'");
            }


            //EDITA EL AÑO DE LANZAMIENTO
            System.out.println("¿Deseas actualizar el año de lanzamiento ('si'/'no')?");
            System.out.println("año Actual: " + bookUpdate.getReleaseYear());
            respuestaUsuario = scanner.nextLine().trim().toLowerCase();;

            while (!respuestaUsuario.equals("si") && !respuestaUsuario.equals("no")) {
                System.out.println("Respuesta invalida ingresa 'si' o 'no'");
                respuestaUsuario = scanner.nextLine().trim().toLowerCase();
            }

            switch (respuestaUsuario) {
                case "si":
                    try {
                        System.out.println("Ingresa el nuevo año: ");
                        int newYearBook = scanner.nextInt();
                        scanner.nextLine();

                        bookUpdate.setReleaseYear(newYearBook);
                        bookCrudOperations.update(bookUpdate);
                        System.out.println("--- CAMPO ACTUALIZADO ---");
                    } catch (Exception e) {
                        throw new ExcepcionesPersonalizadas("Erro al editar el campo : ", e);
                    }
                    break;
                case "no":
                    System.out.println("---SIN CAMBIOS PARA ESTE CAMPO---");
                    break;
                default:
                    System.out.println("INGRESA UN OPCION VALIDA 'si' O 'no'");
            }

            //EDITA EL CODIGO ISBN
            System.out.println("Deseas cambiar el codigo ISBN del libro ('si' / 'no')?");
            System.out.println("ISBN Actual: " + bookUpdate.getIsbnCode());
            respuestaUsuario = scanner.nextLine().trim().toLowerCase();;

            while (!respuestaUsuario.equals("si") && !(respuestaUsuario.equals("no"))) {
                System.out.println("Respuesta invalida ingresa 'si' o 'no'");
                respuestaUsuario = scanner.nextLine().trim().toLowerCase();
            }


            switch (respuestaUsuario) {
                case "si":
                    try {
                        System.out.println("Ingresa el nuevo codigo ISBN: ");
                        String newIsbn = scanner.nextLine();

                        bookUpdate.setIsbnCode(newIsbn);
                        bookCrudOperations.update(bookUpdate);
                        System.out.println("--- CAMPO ACTUALIZADO ---");

                    } catch (Exception e) {
                        throw new ExcepcionesPersonalizadas("Erro al actualizar el campo : ", e);
                    }
                    break;
                case "no":
                    System.out.println("---SIN CAMBIOS PARA ESTE CAMPO---");
                    break;
                default:
                    System.out.println("INGRESA UN OPCION VALIDA 'si' O 'no'");
            }

            System.out.println("---OPERACION FINALIZADA---");
            scanner.nextLine();
        }, () -> {
            System.out.println("No se encontraron resultados con este ISBN");
            scanner.nextLine();
        });


    }

    //METODO QUE RELAIZA TODO EL PROCESO DE ELIMINCION DE UN REGISTRO
    public static void deleteBookByISBN(EntityManager entityManager){
        System.out.println("..... Eliminar Libros .....");

        bookCrudOperations = new Library<>(Book.class, entityManager);
        Optional<List<Book>> bookList = bookCrudOperations.getAll();

        if (bookList.isEmpty() || bookList.get().isEmpty()){
            System.out.println("No hay libros registrados");
            scanner.nextLine();
            return;
        }

        for(Book book : bookList.get()){
            System.out.println(book);
        }
        System.out.println("..... Ingresa el codigo ISBN del libro a eliminar .....");
        String isbnToDelete = scanner.nextLine();

        Library<Book> bookLibrary = new Library<>(Book.class, entityManager);
        Optional<Book> bookFound = bookLibrary.getByISBN(isbnToDelete);

        bookFound.ifPresentOrElse(bookPresent -> {
            try {
                bookCrudOperations.delete(bookPresent);
                System.out.println("---OPERACION FINALIZADA---");
                scanner.nextLine();
            } catch (Exception e) {
                throw new ExcepcionesPersonalizadas("Error al ejecutar el Delete : ", e);
            }
        }, () -> {
            System.out.println("No se encontro el libro a eliminar : " + isbnToDelete);
            scanner.nextLine();
        });
    }
}

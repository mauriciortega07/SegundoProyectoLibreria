package com.ebac.segundoProyectoJava.service.MenuSelectorCaseMethods;

import com.ebac.segundoProyectoJava.dao.CrudOperations;
import com.ebac.segundoProyectoJava.exceptions.ExcepcionesPersonalizadas;
import com.ebac.segundoProyectoJava.dao.Library;
import com.ebac.segundoProyectoJava.model.Author;
import com.ebac.segundoProyectoJava.service.VerificateStringMethods;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.ebac.segundoProyectoJava.service.MenuSelector.scanner;

public class AuthorMethods {

    private static CrudOperations<Author> authorCrudOperations;
    private static String respuestaUsuario;

    //METODO QUE REALIZA TODO EL PROCESO DE NUEVO REGISTRO
    public static void addAuthor(EntityManager entityManager) {
        System.out.println("..... Agregar un autor a la biblioteca ....");
        authorCrudOperations = new Library<>(Author.class, entityManager);
        Author newAuthor = new Author();

        System.out.println("Ingresa el nombre del autor: ");
        String authorName = scanner.nextLine().trim().toLowerCase();

        while (VerificateStringMethods.isNumber(authorName)) {
            System.out.println("Ingresa un nombre correcto : ");
            authorName = scanner.nextLine().trim().toLowerCase();
        }

        Optional<List<Author>> authorList = authorCrudOperations.getAll();
        Author authorFound = null;

        if (authorList.isPresent()) {
            for (Author authorOnList : authorList.get()) {
                if (authorOnList.getCompleteName().equalsIgnoreCase(authorName)) {
                    authorFound = authorOnList;
                    System.out.println(authorOnList);
                    System.out.println("Autor existente");
                    scanner.nextLine();
                    break;
                }


            }
        }

        if (authorFound == null) {
            System.out.println("Ingresa su biografia: ");
            String authorBiography = scanner.nextLine();

            try {
                newAuthor.setCompleteName(authorName);
                newAuthor.setBiography(authorBiography);

                authorCrudOperations.save(newAuthor);
                System.out.println("----- REGISTRO EXITOSO -----");
            } catch (Exception e) {
                throw new ExcepcionesPersonalizadas("Error al ejecutar el Save : ", e);
            }
        }


    }

    //METODO QUE REALIZA TODO EL PROCESO PARA OBTENER LA LISTA DE REGISTROS DESDE LA BD
    public static void getListAuthors(EntityManager entityManager) {
        System.out.println("..... Consultar lista de Autores .....");
        authorCrudOperations = new Library<>(Author.class, entityManager);

        Optional<List<Author>> authorList = authorCrudOperations.getAll();

        authorList.ifPresentOrElse(authors -> {
            for (Author author : authors) {
                System.out.println(author);
            }
            scanner.nextLine();
        }, () -> System.out.println("NO SE ENCONTRARON AUTORES REGISTRADOS"));

    }

    //METODO QUE REALIZA TODO EL PROCESO PARA EDITAR UN ELEMENTO DE LOS REGISTROS
    public static void updateAuthorByName(EntityManager entityManager) {
        System.out.println("..... Actualizar un elemento de la lista de autores .....");

        authorCrudOperations = new Library<>(Author.class, entityManager);
        Optional<List<Author>> authorList1 = authorCrudOperations.getAll();

        if (authorList1.isEmpty() || authorList1.get().isEmpty()) {
            System.out.println("No hay autores registrados");
            scanner.nextLine();
            return;
        }

        for (Author author : authorList1.get()) {
            System.out.println(author);
        }

        System.out.println("Ingresa el nombre del autor a editar: ");
        String authorForSearch = scanner.nextLine().trim().toLowerCase();

        while (VerificateStringMethods.isNumber(authorForSearch)) {
            System.out.println("Ingresa un nombre correcto : ");
            authorForSearch = scanner.nextLine().trim().toLowerCase();
        }

        final String finalAuhtorForSearch = authorForSearch;

        authorCrudOperations = new Library<>(Author.class, entityManager);
        Optional<List<Author>> authorList = authorCrudOperations.getAll();

        authorList.ifPresentOrElse(authors -> {
            //DETERMINA SI HAY O NO
            boolean found = false;

            //RECORRE CADA ELEMENTO BUSCANDO COINCIDENCIA CON EL NOMBRE PROPORCIONADO
            for (Author author : authors) {

                if (author.getCompleteName().equalsIgnoreCase(finalAuhtorForSearch)) {
                    found = true;
                    System.out.println("Registro encontrado: " + author);

                    //EDITA EL NOMBRE DEL AUTOR
                    System.out.println("¿Deseas editar el nombre del autor?");
                    System.out.println("nombre actual: " + author.getCompleteName());

                    String respuestaUsuario = scanner.nextLine().trim().toLowerCase();

                    //VERIFICA QUE LA CADENA NO SEA UN NUMERO
                    while (!respuestaUsuario.equalsIgnoreCase("si") && !respuestaUsuario.equalsIgnoreCase("no")) {
                        System.out.println("Ingresa si o no para continuar: ");
                        respuestaUsuario = scanner.nextLine().trim().toLowerCase();
                    }

                    //EVALUA SI EDITAR O NO EL NOMBRE DEL AUTOR
                    switch (respuestaUsuario) {
                        case "si":
                            try {
                                System.out.println("Ingresa el nuevo nombre completo: ");
                                String newAuthorName = scanner.nextLine().trim().toLowerCase();

                                while (VerificateStringMethods.isNumber(respuestaUsuario)) {
                                    System.out.println("Ingresa un nombre correcto: ");
                                    respuestaUsuario = scanner.nextLine().trim().toLowerCase();
                                }

                                author.setCompleteName(newAuthorName);
                                authorCrudOperations.update(author);
                                System.out.println("--- CAMPO ACTUALIZADO ---");
                            } catch (Exception e) {
                                throw new ExcepcionesPersonalizadas("Error al editar el campo", e);
                            }

                            break;
                        case "no":
                            System.out.println("SIN CAMBIOS PARA ESTE CAMPO");
                            break;
                        default:
                            System.out.println("INGRESA UN OPCION VALIDA 'si' O 'no'");
                    }

                    //EDITA LA BIOGRAFIA DEL AUTOR
                    System.out.println("¿Deseas editar la biografia del autor?");
                    System.out.println("biografia actual: " + author.getBiography());
                    respuestaUsuario = scanner.nextLine().trim().toLowerCase();

                    while (!respuestaUsuario.equalsIgnoreCase("si") && !respuestaUsuario.equalsIgnoreCase("no")) {

                        System.out.println("Ingresa si o no para continuar: ");
                        respuestaUsuario = scanner.nextLine().trim().toLowerCase();
                    }

                    switch (respuestaUsuario) {
                        case "si":
                            try {
                                System.out.println("Escribe la nueva biografia: ");
                                String newAuthorBiography = scanner.nextLine().trim().toLowerCase();

                                author.setBiography(newAuthorBiography);
                                authorCrudOperations.update(author);
                                System.out.println("--- CAMPO ACTUALIZADO ---");
                            } catch (Exception e) {
                                throw new ExcepcionesPersonalizadas("Error al editar el campo", e);
                            }
                            System.out.println("--- AUTOR ACTUALIZADO ---");
                            scanner.nextLine();
                            break;
                        case "no":
                            System.out.println("SIN CAMBIOS PARA ESTE CAMPO");
                            System.out.println("FIN DE LA OPERACION");
                            scanner.nextLine();
                            break;
                        default:
                            System.out.println("INGRESA UN OPCION VALIDA 'si' O 'no'");
                    }
                }
            }

            if (!found) {
                System.out.print("AUTOR NO ENCONTRADO");
                scanner.nextLine();
            }


        }, () -> System.out.println("SIN AUTORES ENCONTRADOS"));

    }

    //METODO QUE RELAIZA TODO EL PROCESO DE ELIMINCION DE UN REGISTRO
    public static void deleteAuthor(EntityManager entityManager) {
        System.out.println("..... Eliminar Autor .....");

        authorCrudOperations = new Library<>(Author.class, entityManager);
        Optional<List<Author>> authorList = authorCrudOperations.getAll();

        if (authorList.isEmpty() || authorList.get().isEmpty()) {
            System.out.println("No hay autores registrados");
            scanner.nextLine();
            return;
        }

        for (Author author : authorList.get()) {
            System.out.println(author);
        }

        System.out.println("Ingresa el id del autor a eliminar: ");
        int idDelete;

        try {
            idDelete = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("ID no valido");
            return;
        }

        Optional<Author> authorFound = authorCrudOperations.getById(idDelete);


        authorFound.ifPresentOrElse(author -> {
            if (!author.getBooksPublished().isEmpty()) {
                System.out.println("No se puede eliminar este autor porque tiene libros asociados");
                scanner.nextLine();
                return;
            }

            try {
                authorCrudOperations.delete(author);
                System.out.println("Autor Eliminado con Exito");
                scanner.nextLine();
            } catch (Exception e) {
                throw new ExcepcionesPersonalizadas("Error al ejecutar el Delete : ", e);
            }
        }, () -> System.out.println("NO HAY NINGUN REGISTRO CON ESTE ID" + idDelete));


    }
}

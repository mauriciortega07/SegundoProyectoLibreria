package com.ebac.segundoProyectoJava.service;

import com.ebac.segundoProyectoJava.dao.MySqlConnection;
import com.ebac.segundoProyectoJava.service.MenuSelectorCaseMethods.AuthorMethods;
import com.ebac.segundoProyectoJava.service.MenuSelectorCaseMethods.BooksMethods;
import com.ebac.segundoProyectoJava.service.MenuSelectorCaseMethods.UserMethods;
import jakarta.persistence.EntityManager;

import java.util.Scanner;

public class MenuSelector {
    public static Scanner scanner = new Scanner(System.in);
    private static EntityManager entityManager = MySqlConnection.getConnection();

    public static int menu(int opcion) {
        opcion = 0;
        do {
            menuOptions();

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Ingresa una opcion valida");
                scanner.nextLine();
                continue;
            }

        } while (opcion < 1 || opcion > 13);

        return opcion;
    }

    public static void menuOperations(int opcion) {

        switch (opcion) {
            case 1:
                BooksMethods.addBook(entityManager);
                break;
            case 2:
                BooksMethods.getListBooks(entityManager);
                break;
            case 3:
                BooksMethods.updateBookByISBN(entityManager);
                break;
            case 4:
                BooksMethods.deleteBookByISBN(entityManager);
                break;
            case 5:
                AuthorMethods.addAuthor(entityManager);
                break;
            case 6:
                AuthorMethods.getListAuthors(entityManager);
                break;
            case 7:
                AuthorMethods.updateAuthorByName(entityManager);
                break;
            case 8:
                AuthorMethods.deleteAuthor(entityManager);
                break;
            case 9:
                UserMethods.addUser(entityManager);
                break;
            case 10:
                UserMethods.getListUsers(entityManager);
                break;
            case 11:
                UserMethods.updateUsery(entityManager);
                break;
            case 12:
                UserMethods.deleteUser(entityManager);
                break;
            default:
                System.out.println("Ingresa una opcion correcta");

        }

    }

    public static void menuOptions() {
        System.out.println("\n************ BIBLIOTECA EBAC ************");
        System.out.println("--- Selecciona una operacion del menu ---");
        System.out.println("\n---------------- Biblioteca -------------------");
        System.out.println("(1)..... Agregar un libro a la biblioteca");
        System.out.println("(2)..... Consultar lista de libros");
        System.out.println("(3)..... Actualizar un elemento de la lista de libros");
        System.out.println("(4)..... Eliminar un elemento de la lista de libros");
        System.out.println("\n----------------Autores de la Biblioteca -------------------");
        System.out.println("(5)..... Agregar un autor a la biblioteca");
        System.out.println("(6)..... Consultar lista de autores");
        System.out.println("(7)..... Actualizar un autor");
        System.out.println("(8)..... Eliminar un autor");
        System.out.println("\n---------------- Usuarios de la Biblioteca -------------------");
        System.out.println("(9)..... Agregar un usuario a la biblioteca");
        System.out.println("(10)..... Lista de usuarios en la biblioteca");
        System.out.println("(11)..... Actualizar un usuario");
        System.out.println("(12)..... Eliminar un usuario");
        System.out.println("\n(13)..... SALIR DEL PROGRAMA");
    }
}

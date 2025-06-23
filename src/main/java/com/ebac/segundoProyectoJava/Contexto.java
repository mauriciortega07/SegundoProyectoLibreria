package com.ebac.segundoProyectoJava;

import com.ebac.segundoProyectoJava.service.MenuSelector;

public class Contexto {
    public static void main(String[] args) {

        int opcion = 0;

        do {
            opcion = MenuSelector.menu(opcion);
            MenuSelector.menuOperations(opcion);
        } while (opcion != 13);
            System.out.println("Fin del programa");
    }
}

package com.ebac.segundoProyectoJava.exceptions;

public class ExcepcionesPersonalizadas extends RuntimeException {
    public ExcepcionesPersonalizadas(String mensaje){
        super(mensaje);
    }

    public ExcepcionesPersonalizadas(String mensaje, Throwable e){
        super(mensaje);
    }
}

package com.ebac.segundoProyectoJava.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificateStringMethods {
    public static boolean isNumber(String value){
        boolean resultado;

        try {
        Integer.parseInt(value);
        resultado = true;
        } catch (Exception e) {
            resultado = false;
        }

        return resultado;
    }

    public static boolean isValidEmail(String email){
        String emailFormato = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailFormato);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        String passFormato = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(passFormato);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
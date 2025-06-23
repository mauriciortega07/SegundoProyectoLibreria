package com.ebac.segundoProyectoJava.service.MenuSelectorCaseMethods;

import com.ebac.segundoProyectoJava.dao.CrudOperations;
import com.ebac.segundoProyectoJava.dao.Library;
import com.ebac.segundoProyectoJava.exceptions.ExcepcionesPersonalizadas;
import com.ebac.segundoProyectoJava.model.User;
import com.ebac.segundoProyectoJava.service.VerificateStringMethods;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.ebac.segundoProyectoJava.service.MenuSelector.scanner;

public class UserMethods {

    private static CrudOperations<User> userCrudOperations;
    private static String respuestaUsuario;

    //METODO QUE REALIZA TODO EL PROCESO DE NUEVO REGISTRO
    public static void addUser(EntityManager entityManager) {
        userCrudOperations = new Library<>(User.class, entityManager);
        //En Caso de fallar en alguna fase se toma la excepcion
        try {

            //pide le nombre completo
            System.out.println("..... Registro de Usuario .....");
            System.out.println("Ingresa tu nombre completo: ");
            String userName = scanner.nextLine().trim().toLowerCase();

            //verifica que el nombre no sea numeros
            while (VerificateStringMethods.isNumber(userName)) {
                System.out.println("Ingresa tu nombre correctamente: ");
                userName = scanner.nextLine().trim().toLowerCase();
            }

            //este objeto determinara si el resgistro ya existe
            User userFound = null;

            //Obtenemos la lista de todos los registros
            Optional<List<User>> allUsers = userCrudOperations.getAll();

            //Se hace una validacion para confirmar si hay o no un usuario con el mismo nombre ya registrado
            if (allUsers.isPresent()) {
                for (User user : allUsers.get()) {
                    if (user.getUsername().equalsIgnoreCase(userName)) {
                        System.out.println("Este usuario ya esta registrado");
                        userFound = user;
                        scanner.nextLine();
                        break;
                    }
                }
            }

            //Si no hay usuarios registrados se procedera a pedir los demas requisitos para guardar al usuario nuevo
            if (userFound == null) {

                //Pedimos el correo
                System.out.println("Ingresa tu correo: ");
                String userMail = scanner.nextLine().trim().toLowerCase();

                //verifica que el correo ingresado tenga un formato de correo valido
                while (!VerificateStringMethods.isValidEmail(userMail)) {
                    System.out.println("Ingresa una direccion de correo valida: ");
                    userMail = scanner.nextLine().trim().toLowerCase();
                }


                //Pedimos la contraseña
                System.out.println("Ingresa tu contraseña: ");
                System.out.println("Debe tener al menos una letra mayuscula y minuscula\nAl menos un numero y 8 caracteres");
                String userPassword = scanner.nextLine();

                //Verifica que la contraseña cumpla con los requerimientos de creacion
                while (!VerificateStringMethods.isValidPassword(userPassword)) {
                    System.out.println("Ingresa una contraseña valida");
                    System.out.println("Debe tener al menos una letra mayuscula y minuscula\nAl menos un numero y 8 caracteres");
                    userPassword = scanner.nextLine();
                }

                //Se setean los datos en el objeto newUSer
                try {
                    User newUser = new User();
                    newUser.setUsername(userName);
                    newUser.setEmail(userMail);
                    newUser.setPassword(userPassword);

                    //Guarda en la BD el Registro
                    userCrudOperations.save(newUser);
                    System.out.println("--- REGISTRO EXITOSO ---");
                    scanner.nextLine();
                } catch (Exception e) {
                    throw new ExcepcionesPersonalizadas("Error no se pudo ejecutar el Save : ", e);
                }
            }

        } catch (Exception e) {
            throw new ExcepcionesPersonalizadas("Hubo un error al registrar el usuario : ", e);
        }
    }

    //METODO QUE REALIZA TODO EL PROCESO PARA OBTENER LA LISTA DE REGISTROS DESDE LA BD
    public static void getListUsers(EntityManager entityManager){
        System.out.println("..... Lista de Usuarios Registrados .....");
        userCrudOperations = new Library<>(User.class, entityManager);
        Optional<List<User>> userList = userCrudOperations.getAll();

        userList.ifPresentOrElse(users -> {
            if(users.isEmpty()){
                System.out.println("No hay registros");
            }

            for (User user : users){
                System.out.println(user);
            }
        }, () -> {
            System.out.println("NO SE ENCONTRARON USUARIOS REGISTRADOS");
            scanner.nextLine();
        });
        scanner.nextLine();
    }

    //METODO QUE REALIZA TODO EL PROCESO PARA EDITAR UN ELEMENTO DE LOS REGISTROS
    public static void updateUsery(EntityManager entityManager){
        System.out.println("..... Actualizar un elemento de la lista de autores .....");

        //OBTENEMOS LA LISTA DE TODOS LOS REGISTROS
        userCrudOperations = new Library<>(User.class, entityManager);
        Optional<List<User>> userList = userCrudOperations.getAll();

        //MUESTRA LA LISTA DE USUARIOS ENCONTRADOS
        System.out.println("-------USUARIOS REGISTRADOS-------");
        userList.ifPresentOrElse(users -> {
            for (User user : users){
                System.out.println(user);
            }
        }, () -> System.out.println("No se encontraron registros"));
        //getListUsers(entityManager);

        //PIDE EL NOMBRE DEL USUARIO A EDITAR
        System.out.println("Ingresa tu nombre de usuario: ");
        String userName = scanner.nextLine().trim().toLowerCase();

        //VERIFICA QUE SEA UN NOMBRE CORRECTO
        while (VerificateStringMethods.isNumber(userName)){
            System.out.println("Ingresa un nombre de usuario correcto: ");
            userName = scanner.nextLine().trim().toLowerCase();
        }

        final String userNameFinal = userName;

        //LOGICA DE LA EDICION DE LOS CAMPOS
        userList.ifPresentOrElse(users -> {
            //NOS AYUDA A SABER SI HAY O NO UN REGISTRO
            boolean userFound = false;

            //RECORRE POR CADA USUARIO ENCONTRADO
            for(User user : users) {
                //SI ALGUNO COINCIDE CON LA ENTRADA DEL USUARIO SE EJECUTA:
                if(user.getUsername().equalsIgnoreCase(userNameFinal)){
                    //SI SE ENCUENTRA SE ACTUALIZA:
                    userFound = true;

                    //SE IMPRIME EL USUARIO ENCONTRADO
                    System.out.println("Usuario encontrado: " + user);
                    int idUserFound = user.getIdUsuario();

                    //DECIDE SI EDITAR O NO EL NOMBRE DE USARIO
                    System.out.println("¿Deseas editar el nombre de usuario?");
                    System.out.println("nombre actual: " + user.getUsername());
                    respuestaUsuario = scanner.nextLine().trim().toLowerCase();

                    while (!respuestaUsuario.equalsIgnoreCase("si") && !respuestaUsuario.equalsIgnoreCase("no")){
                        System.out.println("Ingresa si o no para continuar: ");
                        respuestaUsuario = scanner.nextLine().trim().toLowerCase();
                    }

                    switch (respuestaUsuario){
                        case "si":
                            try {
                                System.out.println("Ingresa el nuevo nombre de usuario");
                                String newUserName = scanner.nextLine().trim().toLowerCase();

                                user.setUsername(newUserName);
                                userCrudOperations.update(user);
                                System.out.println("---CAMPO ACTUALIZADO---");
                            } catch (Exception e) {
                                throw new ExcepcionesPersonalizadas("Error el editar el campo : ", e);
                            }
                            break;
                        case "no":
                            System.out.println("---SIN CAMBIOS PARA ESTE CAMPO---");
                            break;
                        default:
                            System.out.println("Ingresa un opcion correcta");
                    }

                    //DECIDE SI EDITAR O NO EL EMAIL
                    System.out.println("¿Deseas editar el correo electronico registrado?: ");
                    System.out.println("correo actual: " + user.getEmail());
                    respuestaUsuario = scanner.nextLine().trim().toLowerCase();

                    while (!respuestaUsuario.equalsIgnoreCase("si") && !respuestaUsuario.equalsIgnoreCase("no")){
                        System.out.println("Ingresa si o no para continuar: ");
                        respuestaUsuario = scanner.nextLine().trim().toLowerCase();
                    }

                    switch (respuestaUsuario){
                        case "si":
                            try {
                                System.out.println("Ingresa el nuevo correo electronico: ");
                                String newUserEmail = scanner.nextLine().trim().toLowerCase();
                                System.out.println("---CAMPO ACTUALIZADO--");
                                user.setEmail(newUserEmail);
                                userCrudOperations.update(user);
                            } catch (Exception e){
                                throw new ExcepcionesPersonalizadas("Error al editar el camppo : ", e);
                            }
                            break;
                        case "no":
                            System.out.println("---SIN CAMBIOS PARA ESTE CAMPO---");
                            break;
                        default:
                            System.out.println("Ingresa una opcion correcta");
                    }

                    //DECIDE SI EDITAR O NO LA CONTRASEÑA
                    System.out.println("¿Deseas actualizar la contrasaña?: ");
                    respuestaUsuario = scanner.nextLine().trim().toLowerCase();

                    while(!respuestaUsuario.equalsIgnoreCase("si") && !respuestaUsuario.equalsIgnoreCase("no")){
                        System.out.println("Ingresa si o no para continuar: ");
                        respuestaUsuario = scanner.nextLine().trim().toLowerCase();
                    }

                    switch (respuestaUsuario){
                        case "si":
                            try {
                                System.out.println("Ingresa la nueva contraseña: ");
                                String newUserPassword = scanner.nextLine().trim().toLowerCase();

                                while (!VerificateStringMethods.isValidPassword(newUserPassword)){
                                    System.out.println("Ingresa una Contraseña valida\nDebe tener al menos una letra mayuscula y minuscula\nAl menos un numero y 8 caracteres");
                                    newUserPassword = scanner.nextLine().trim().toLowerCase();
                                }

                                user.setPassword(newUserPassword);
                                userCrudOperations.update(user);
                                System.out.println("---CAMPO ACTUALIZADO--");
                            }catch (Exception e){
                                throw new ExcepcionesPersonalizadas("Error al editar el campo : ", e);
                            }
                            System.out.println("---USUARIO ACTUALIZADO---");
                            break;
                        case "no":
                            System.out.println("---SIN CAMBIOS PARA ESTE CAMPO---");
                            System.out.println("---FIN DE LA OPERACION---");
                            break;
                        default:
                            System.out.println("Ingresa una opcion correcta");
                    }
                    scanner.nextLine();
                }
            }

            //SI NO HAY USUARIO COINCIDENTE
            if(!userFound) {
                System.out.println("USUARIO NO LOCALIZADO");
                scanner.nextLine();
            }

        }, () -> System.out.println("SIN USUARIOS ENCONTRADOS"));
    }

    //METODO QUE RELAIZA TODO EL PROCESO DE ELIMINCION DE UN REGISTRO
    public static void deleteUser(EntityManager entityManager){
        System.out.println(".....Eliminar Usuario.....");
        userCrudOperations = new Library<>(User.class, entityManager);
        Optional<List<User>> userList = userCrudOperations.getAll();

        if (userList.isEmpty() || userList.get().isEmpty()){
            System.out.println("No hay registros que mostrar");
            scanner.nextLine();
            return;
        }

        System.out.println("-----USUARIOS REGISTRADOS-----");
        for (User user : userList.get()){
            System.out.println(user);
        }

        System.out.println("Ingresa el ID del usuario a eliminar: ");
        int idUserDelete;

        try {
            idUserDelete = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e){
            scanner.nextLine();
            System.out.println("ID NO VALIDO");
            return;
        }

        Optional<User> userFound = userCrudOperations.getById(idUserDelete);

        userFound.ifPresentOrElse(user -> {
            if (!user.getBorrowedBooks().isEmpty()){
                System.out.println("usuario a eliminar : " + user);
                System.out.println("No se puede eliminar porque tiene libros en renta");
                scanner.nextLine();
                return;
            }

            try {
                userCrudOperations.delete(user);
                System.out.println("Usuario eliminado con exito");
                scanner.nextLine();
            } catch (Exception e) {
                throw new ExcepcionesPersonalizadas("Error al ejecutar el delete : ", e);
            }
        }, () -> {
            System.out.println("NO HAY REGISTRO COINCIDENTE CON ESTE ID : " + idUserDelete);
            scanner.nextLine();
        });

    }


}

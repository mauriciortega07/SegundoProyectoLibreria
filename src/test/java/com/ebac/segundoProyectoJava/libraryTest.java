package com.ebac.segundoProyectoJava;

import com.ebac.segundoProyectoJava.dao.Library;
import com.ebac.segundoProyectoJava.dao.MySqlConnection;
import com.ebac.segundoProyectoJava.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;


public class libraryTest {
    private static EntityManager entityManager;
    private static Library library;
    private static User user1;

    @BeforeAll
    static void setUp(){
        entityManager = MySqlConnection.getConnection();
        library = new Library<>(User.class, entityManager);

    }

    @BeforeEach
    void beforeEach(){
        System.out.println("Comienzan las pruebas");
    }

    @AfterAll()
    static void afterAll(){
        System.out.println("Pruebas finalizdas");
        entityManager.close();
    }

    @Test
    public void testSave(){
        User user1 = new User();
        user1.setUsername("mauricio prueba");
        user1.setEmail("prueba@gmail.com");
        user1.setPassword("BAbasonicos7");

        library.save(user1);

        int idGenerado = user1.getIdUsuario();

        Optional<User> resultado = library.getById(idGenerado);

        String expected = "mauricio prueba";
        String actual = resultado.get().getUsername();


        //Assertions.assertTrue(true);
        Assertions.assertEquals(expected,actual, "test fallido");
    }

    @Test
    public void testDelete() {

        User user1 = new User();
        user1.setUsername("Jose Luis");
        user1.setEmail("jos@gmail.com");
        user1.setPassword("BAabasonicos7");

        User user2 = new User();
        user2.setUsername("Pamela Chavez");
        user2.setEmail("pam@gmail.com");
        user2.setPassword("BabaSonicos7");

        library.save(user1);
        library.save(user2);

        library.delete(user1);
        //library.delete(user2);

        Optional<User> result1 = library.getById(user1.getIdUsuario());
        Optional<User> result2 = library.getById(user2.getIdUsuario());

        Boolean expected = true;
        Boolean actual = result1.isEmpty() || result2.isEmpty() ? true : false;

        Assertions.assertEquals(expected, actual, "Test fallido");

    }

    @Test
    public void testUpdate(){
        //CREAMOS UN USUARIO
        User user1 = new User();
        user1.setUsername("brian wilson");
        user1.setEmail("br@gmail.com");
        user1.setPassword("BAbasonicos7");

        //GUARDAMOS EL USUARIO
        library.save(user1);

        //DATOS A ACTUALIZAR
        user1.setUsername("Carl Johnson");

        //ACTUALIZAMOS EL USUARIO
        library.update(user1);

        String expected = "Carl Johnson";
        String actual = user1.getUsername();

        Assertions.assertEquals(expected, actual, "Test fallido : ");


    }

}

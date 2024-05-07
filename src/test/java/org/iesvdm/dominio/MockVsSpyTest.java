package org.iesvdm.dominio;


import static org.assertj.core.api.Assertions.*;
import org.iesvdm.dominio.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class MockVsSpyTest
{
    @Test
    void createMock() {
        User user = Mockito.mock(User.class);

        //mock crea un doble (test double) vacio
        assertThat(user.getUsername()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getRole()).isNull();
    }

    @Test
    void createSpyFromEmptyConstructor(){
        User user = Mockito.spy(new User());

        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getPassword()).isNull();
        assertThat(user.getRole()).isNull();
    }

    @Test
    void createSpyFromNonEmptyConstructor(){
        User user = Mockito.spy(new User("jose", "pass", "admin"));

        assertThat(user.getUsername()).isEqualTo("jose");
        assertThat(user.getPassword()).isEqualTo("pass");
        assertThat(user.getRole()).isEqualTo("user");
    }

    @Test
    void whenThenReturn(){
        User user = Mockito.spy(new User("jose", null, "admin"));

        //whenThenReturn implementa seguridad de tipo devuelto
        Mockito.when(user.getRole()).thenReturn("admin");  //OK
//        Mockito.when(user.getUsername()).thenReturn(1); //FAIL

        assertThat(user.getUsername()).isEqualTo("jose");
        assertThat(user.getPassword()).isNull();
        assertThat(user.getRole()).isEqualTo("admin");
        System.out.println("Después de getRole, pero al ser un mock sin efecto colateral");
        System.out.println(", es decir, sin mensaje previo de role");
    }

    @Test
    void doReturnWhen() {
        User user = Mockito.mock(User.class);

        Mockito.doReturn("1234").when(user).getPassword();

        assertThat(user.getPassword()).isEqualTo("1234");

        //doReturnWhen no implementa seguridad de tipo devuelto
        Mockito.doReturn(true).when(user).getUsername();//FAIL en tiempo de ejecución
                                                                   // , no falla en tiempo de compilación
                assertThat(user.getUsername()).isEqualTo(true);

        //Captura de la exception lanzada por la falta de seguridad en el tipo de doReturnWhen
        assertThatThrownBy( () -> {
                            Mockito.doReturn(true).when(user).getUsername();
                            })
                            .isInstanceOf(WrongTypeOfReturnValue.class);

    }

    @Test
    void whenThenReturn_doReturnWhen_mock_noSideEffect() {

        final PrintStream standardOut = System.out;
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        //mock: NO tiene efecto colateral, es decir,no se invoca
        //el codigo del metodo.. directamente se devuelve el valor fijado de return
        User user = Mockito.mock(User.class);
        Mockito.when(user.getRole()).thenReturn("root");
        assertThat(user.getRole()).isEqualTo("root");
        //             |
        //             V ¿se invoca el metodo y por tanto el println interno? No se invoca en un mock ya sea whenThenReturn o doReturnWhen.
        String role_access_msg = outputStreamCaptor.toString().trim();
        assertThat(role_access_msg).isNotEqualTo(User.ROLE_ACCESS_MSG); //outputStream NOT EQUAL!
        assertThat(role_access_msg).isEmpty();


        //doReturnWhen no implementa seguridad de tipo devuelto
        Mockito.doReturn("admin").when(user).getRole();

        assertThat(user.getRole()).isEqualTo("admin");
        //              |
        //              V ¿se invoca el metodo y por tanto el println interno? No se invoca en un mock ya sea whenThenReturn o doReturnWhen.
        role_access_msg = outputStreamCaptor.toString().trim();
        assertThat(role_access_msg).isNotEqualTo(User.ROLE_ACCESS_MSG); //outputStream NOT EQUAL!
        assertThat(role_access_msg).isEmpty();

    }

    @Test
    void whenThenReturn_doReturnWhen_spy_sideEffect() {

        final PrintStream standardOut = System.out;
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        //spy: SÍ tiene efecto colateral, es decir,se invoca
        //el codigo del metodo.. sí se ejecuta el cuerpo del metodo, pero se devuelve el valor fijado de return
        User user = Mockito.spy(new User("user","1234", "reader"));
        Mockito.when(user.getRole()).thenReturn("root");
        assertThat(user.getRole()).isEqualTo("root");
        //             |
        //             V ¿se invoca el metodo y, por tanto, el println interno? Sí se invoca en un spy ya sea whenThenReturn o doReturnWhen.
        //String role_access_msg = outputStreamCaptor.toString().trim();
        //assertThat(role_access_msg).isEqualTo(User.ROLE_ACCESS_MSG);
        //assertThat(role_access_msg).isNotEmpty();

        //doReturnWhen no implementa seguridad de tipo devuelto
       // Mockito.doReturn("admin").when(user).getRole();

       // assertThat(user.getRole()).isEqualTo("admin");
        //              |
        //              V ¿se invoca el metodo y, por tanto, el println interno? Sí se invoca en un spy ya sea whenThenReturn o doReturnWhen.
        //role_access_msg = outputStreamCaptor.toString().trim();
        //assertThat(role_access_msg).isEqualTo(User.ROLE_ACCESS_MSG);
       // assertThat(role_access_msg).isNotEmpty();

    }

}

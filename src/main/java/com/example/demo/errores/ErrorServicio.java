
package com.example.demo.errores;


public class ErrorServicio extends Exception { //clase que va a informar el error, que hereda de excepcion
    public ErrorServicio(String mensaje){
       super(mensaje); 
    }

}

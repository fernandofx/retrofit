package com.codigo.retrofit.aggregates;

public class ConexionDb {

    //Primero crear una variable private y estatica de la misma clase
    private static ConexionDb conexionDb;
    //Segundo COntructor privado para evitar que otros creen instancias
    private ConexionDb(){
        System.out.println("Conexion a BD creada!!");
    }
    //Tercero crear metodo publico para obtener la unica isntancia
    public static ConexionDb obtenerInstancia(){
        if(conexionDb == null){
            conexionDb = new ConexionDb();
        }
        return conexionDb;
    }
}

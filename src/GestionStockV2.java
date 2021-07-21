/*
        CARLOS GONZÁLEZ VAN LIEMPT
            Proyecto de Gestión de Stock
            Fundamentos de Programación y computadores
*/

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GestionStockV2 {

    public static void crearProducto(String ruta, String nombreEstablecimiento){
        
        Scanner teclado = new Scanner(System.in);

        File archivoEstablecimiento = new File(ruta);
        int filas;
        if(archivoEstablecimiento.exists()){ //Comprueba que el Establecimiento esté creado
        }else{
            System.out.println("El establecimiento que desea crear no está dado de alto. Porfavor los datos del mismo:");
            System.out.print("-> ");
            crearFichero(ruta,"ID Producto;Nombre;Stock;Precio;Información Extra\n" ); //En caso negativo crea el fichero del establecimiento
            escribirEstablecimientoLista(nombreEstablecimiento); //Escribe los datos del Establecimiento en la Lista
        }

        System.out.print("       Pulse cualquier otra tecla para volver al menu principal \n "
                + "¿Cuántos productos desea introducir?"
                + "\n ->");
        if(!teclado.hasNextInt()) {

            System.out.println(" - Volviendo al menu principal... - ");
            //Permite salir al menu principal
            pulsaCualquierTeclaParaContinuar();
        }
        else {
             filas = teclado.nextInt();

            while (filas<= 0) { 			//Valida que el numero introducido por el usuario es de carácter positivo
                System.out.print("Valor no válido, compruebe que el valor introducido es positivo");

                while(!teclado.hasNextInt()) { 			//Se realiza la comprobacion del numero entero
                    System.out.print("Error, compruebe que el valor intrucido sea válido porfavor");
                    teclado.next();

                }
            }


            String[][] nuevaLinea = new String[filas][5];
        for(int i=0; i<nuevaLinea.length; i++){
            System.out.println("--> Producto "+ (i+1) + " <--");
            for(int j=0; j<nuevaLinea[i].length; j++){
                if(j==0){
                    nuevaLinea[i][j] = String.valueOf(contarFilas(ruta));
                }
                if(j==1){
                    System.out.println("Introduzca el nombre del producto de " + nombreEstablecimiento + ":");
                    System.out.print("-> ");

                    nuevaLinea[i][j] = teclado.next();
                }
                if(j==2){
                    System.out.println("Introduzca la cantidad de " + nuevaLinea[i][1] + " (Stock):");
                    System.out.print("-> ");
                    nuevaLinea[i][j] = teclado.next();
                }
                if (j==3) {
                    System.out.print("Introduzca el precio del producto (Si es decimal porfavor utilice ' , ' comas): \n ->");
                    while (!teclado.hasNextDouble()) {
                        System.out.print("Error. Introduzca un precio válido: \n ->");
                        teclado.next();
                    }
                    nuevaLinea[i][j] = teclado.next();
                }
                if(j==4){
                    System.out.println("""
                            ¿Desea añadir alguna información extra?
                            -> 1 - Si
                            -> 2 - No""");
                    System.out.print("--> ");
                    String decisionInfoExtra;
                    while(!((teclado.hasNext("1"))||(teclado.hasNext("2")))){
                        System.out.println("Opción Incorrecta. Porfavor Revise...");
                        System.out.print("-> ");
                        teclado.next();

                    }decisionInfoExtra = teclado.next();
                    teclado.nextLine();
                    if(decisionInfoExtra.equals("1")){
                        System.out.println("Introduzca la Información Extra que desee:");
                        System.out.print("-> ");
                        nuevaLinea[i][j] = teclado.nextLine();

                    }else{
                        System.out.println("Información por defecto.");
                        nuevaLinea[i][j] = "No presenta ninguna información extra";
                    }

                }
            }
        }
        escribirEnFichero(nuevaLinea, ruta,true); //Escribe los datos en el fichero correspondiente del Establecimiento
    }
    }

    public static void crearEstablecimiento (String nombreEstablecimiento, String ruta){

        File archivoEstablecimiento = new File(ruta);

        if(archivoEstablecimiento.exists()){ //Comprueba que el Establecimento esté creado
            System.out.println("El Establecimiento que desea crear ya está dado de alta!"); //De ser cierto, avisa al usuario
        }else{
            System.out.println("El Establecimiento que desea crear no está dado de alta. Porfavor introduzca los datos del mismo.");
            crearFichero(ruta,"ID Producto;Nombre;Stock;Precio;Información Extra\n" ); //En caso negativo crea el fichero Establecimiento
            escribirEstablecimientoLista(nombreEstablecimiento); //Escribe los datos del Establecimiento en la Lista
        }
    }

    public static void crearEstablecimientosinPedirNombre (String nombreEstablecimiento){
        String ruta = nombreEstablecimiento + ".csv";
        File archivoEstablecimiento = new File(ruta);

        if(archivoEstablecimiento.exists()){ //Comprueba que el Establecimiento esté creado
            System.out.println("El Establecimiento que desea crear ya está dado de alta!"); //De ser cierto, avisa al usuario
        }else{
            System.out.println("El Establecimiento que desea crear no está dado de alta.\n Porfavor introduzca los datos del mismo.");
            crearFichero(ruta,"ID Producto;Nombre;Stock;Precio;Información Extra\n" ); //En caso negativo crea el fichero Establecimiento
            escribirEstablecimientoLista(nombreEstablecimiento); //Escribe los datos del Establecimiento en la Lista
        }
    }

    public static void crearFichero(String ruta, String texto){

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(ruta, true));
            bw.write(texto); // escribe el contenido indicado como parametro en el documento indicado
            bw.flush(); // guarda el archivo
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally { // Cierra el BufferedWriter
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException ioe) {
                    System.out.println("No se ha podido cerrar el lector de ficheros.");
                }
        }
    }

    public static void modificarProducto(int opcionModificarProducto, String nombreEstablecimiento){

        Scanner teclado = new Scanner(System.in);
        String [][] fichero_a_Modificar = leerFichero(nombreEstablecimiento + ".csv");
        String modificacion;
        String producto;

        LocalDateTime FechaActual = LocalDateTime.now();
        DateTimeFormatter FechaFormateada = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String FechaFinal = FechaActual.format(FechaFormateada);

        switch (opcionModificarProducto) {
            case 0 -> //Atrás
                    System.out.println("Volviendo al menu Modificar...");
            case 1 -> {//Modificar Producto
                System.out.println("¿A qué producto desea cambiarle el nombre?");
                System.out.print("->");
                producto = teclado.next();
                for (int i = 0; i < fichero_a_Modificar.length; i++) {

                    if (fichero_a_Modificar[i][1].matches(producto)) {
                        System.out.println("Actualmente tenemos este nombre: " + producto + ".\n" +
                                "Escriba como quiere renombrarlo:");
                        System.out.print("->");
                        modificacion = teclado.next();
                        fichero_a_Modificar[i][1] = modificacion;
                        escribirEnFichero(fichero_a_Modificar, nombreEstablecimiento + ".csv", false);
                        escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Modificando nombre de " + producto + " a '" + modificacion+"' en " + nombreEstablecimiento + ".");
                    }
                }
            }
            case 2 -> {//Modificar Stock del Producto
                System.out.println("¿A qué Producto desea cambiarle el Stock?");
                System.out.print("->");
                producto = teclado.next();
                for (int i = 0; i < fichero_a_Modificar.length; i++) {

                    if (fichero_a_Modificar[i][1].matches(producto)) {
                        System.out.println("Actualmente tiene de Stock : " + fichero_a_Modificar[i][2] +" de "+ producto +".\n" +
                                "Escriba la modificación de Stock:");
                        System.out.print("->");
                        modificacion = teclado.next();
                        fichero_a_Modificar[i][2] = modificacion;
                        escribirEnFichero(fichero_a_Modificar, nombreEstablecimiento + ".csv", false);
                        escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Modificando Stock de " + producto + " a '" + modificacion+"' en " + nombreEstablecimiento + ".");
                    }
                }
            }
            case 3 -> {//Modificar Precio
                System.out.println("¿A qué Producto desea cambiarle el precio?");
                System.out.print("->");
                producto = teclado.next();
                for (int i = 0; i < fichero_a_Modificar.length; i++) {

                    if (fichero_a_Modificar[i][1].matches(producto)) {
                        System.out.println("Actualmente tenemos este precio: " + fichero_a_Modificar[i][3] + " euros." + "\n" +
                                "Escriba el precio nueva que desea guardar:");
                        System.out.print("->");
                        modificacion = teclado.next();
                        fichero_a_Modificar[i][3] = modificacion;
                        escribirEnFichero(fichero_a_Modificar, nombreEstablecimiento + ".csv", false);
                        escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Modificando precio de " + producto + " a '" + modificacion+"' en " + nombreEstablecimiento + ".");
                    }
                }
            }
            case 4 -> {//Modificar Información Extra
                System.out.println("¿A qué Producto desea cambiarle la Información Extra?");
                System.out.print("->");
                producto = teclado.next();
                teclado.nextLine();
                for (int i = 0; i < fichero_a_Modificar.length; i++) {

                    if (fichero_a_Modificar[i][1].matches(producto)) {
                        System.out.println("Actualmente tenemos esta información: " + fichero_a_Modificar[i][3] + ".\n" +
                                "Escriba la información nueva que desea guardar:");
                        System.out.print("->");
                        modificacion = teclado.nextLine();
                        fichero_a_Modificar[i][4] = modificacion;
                        escribirEnFichero(fichero_a_Modificar, nombreEstablecimiento + ".csv", false);
                        escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Modificando información extra  de " + producto + " a '" + modificacion+"' en " + nombreEstablecimiento + ".");
                    }
                }
            }
        }
    }

    public static void modificarEstablecimiento(int opcionModificarEstablecimiento, String nombreEstablecimiento){

        Scanner teclado = new Scanner(System.in);
        String [][] fichero_a_Modificar = leerFichero("listaEstablecimientos.csv");
        String modificacion;
        LocalDateTime FechaActual = LocalDateTime.now();
        DateTimeFormatter FechaFormateada = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String FechaFinal = FechaActual.format(FechaFormateada);

        switch (opcionModificarEstablecimiento){
            case 0: //Atrás
                System.out.println("Volviendo al menu principal...");
                break;
            case 1://Modificar Nombre
                for(int i=0; i<fichero_a_Modificar.length; i++){

                    if(fichero_a_Modificar[i][1].matches(nombreEstablecimiento)){
                        System.out.println("Actualmente tenemos este nombre: " + nombreEstablecimiento + ".\n" +
                                "Escriba como quiere renombrarlo:");
                        System.out.print("->");
                        modificacion = teclado.next();
                        fichero_a_Modificar[i][1] = modificacion;
                        File fichero_a_Renombrar = new File(nombreEstablecimiento + ".csv");
                        File fichero_Renombrado = new File(modificacion + ".csv");
                        fichero_a_Renombrar.renameTo(fichero_Renombrado);
                        escribirEnFichero(fichero_a_Modificar,"listaEstablecimientos.csv", false);
                        escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Modificando nombre de " + nombreEstablecimiento + " a '" +modificacion+"'.");
                    }
                }
                break;
            case 2://Modificar Código Postal
                for(int i=0; i<fichero_a_Modificar.length; i++){

                    if(fichero_a_Modificar[i][1].matches(nombreEstablecimiento)){
                        System.out.println("Actualmente tenemos este código postal: " + fichero_a_Modificar[i][2] + ".\n" +
                                "Escriba como quiere modificarlo:");
                        System.out.print("->");
                        modificacion = teclado.next();
                        fichero_a_Modificar[i][2] = modificacion;
                        escribirEnFichero(fichero_a_Modificar,"listaEstablecimientos.csv", false);
                        escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Modificando código postal  de " + nombreEstablecimiento + " a '" + modificacion+"'.");
                    }
                }
                break;
            case 3://Modificar Información Extra
                for(int i=0; i<fichero_a_Modificar.length; i++){

                    if(fichero_a_Modificar[i][1].matches(nombreEstablecimiento)){
                        System.out.println("Actualmente tenemos esta información: " + fichero_a_Modificar[i][3] + ".\n" +
                                "Escriba la información nueva que desea guardar:");
                        System.out.print("->");
                        modificacion = teclado.nextLine();
                        fichero_a_Modificar[i][3] = modificacion;
                        escribirEnFichero(fichero_a_Modificar,"listaEstablecimientos.csv", false);
                        escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Modificando información extra  de " + nombreEstablecimiento + " a '" + modificacion+"'.");
                    }
                }
                break;
        }
    }

    public static void eliminarProducto (String nombreEstablecimiento){
        Scanner teclado = new Scanner(System.in);
        String [][] fichero_a_Modificar = leerFichero(nombreEstablecimiento + ".csv");
        LocalDateTime FechaActual = LocalDateTime.now();
        DateTimeFormatter FechaFormateada = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String FechaFinal = FechaActual.format(FechaFormateada);

        String producto;
        System.out.println("¿Qué Producto desea eliminar de " + nombreEstablecimiento + "?");
        System.out.print("->");
        producto = teclado.next();
        int i=0;
        while(!fichero_a_Modificar[i][1].equals(producto)){
            i++;
        }
        int numero_fila = i;
        String [][] fichero_Nuevo =  eliminarFila( fichero_a_Modificar, numero_fila);
        escribirEnFichero(fichero_Nuevo, nombreEstablecimiento + ".csv", false);
            escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Eliminando el producto '" + producto + "' de " + nombreEstablecimiento + ".");
    }

    public static void eliminarEstablecimiento (String nombreEstablecimiento){
        String [][] fichero_a_Modificar = leerFichero("listaEstablecimientos.csv"); //Lee la Lista para borrar el Establecimiento de la misma
        int i=0;
        LocalDateTime FechaActual = LocalDateTime.now();
        DateTimeFormatter FechaFormateada = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String FechaFinal = FechaActual.format(FechaFormateada);

        while(!fichero_a_Modificar[i][1].equals(nombreEstablecimiento)){
            i++;
        }
        int numero_fila = i;
        String [][] fichero_Nuevo =  eliminarFila( fichero_a_Modificar, numero_fila);
        escribirEnFichero(fichero_Nuevo, "listaEstablecimientos.csv", false);
        File establecimiento = new File(nombreEstablecimiento + ".csv");
        establecimiento.delete();
        escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Eliminando " + nombreEstablecimiento + " de nuestro datos.");
    }

    public static String[][] eliminarFila(String[][] fichero_a_Modificar, int numero_fila) {
        if (numero_fila < 0 || numero_fila >= fichero_a_Modificar.length) {
            return fichero_a_Modificar;
        } else {
            String[][] nueva = new String[fichero_a_Modificar.length - 1][fichero_a_Modificar[0].length];
            int i;
            for (i = 0; i < numero_fila; i++) {
                nueva[i] = fichero_a_Modificar[i];
            }
            for (; i < nueva.length; i++) {
                nueva[i] = fichero_a_Modificar[i + 1];
            }
            return nueva;
        }
    }

    public static void escribirEstablecimientoLista(String nombreEstablecimiento){

        Scanner teclado = new Scanner(System.in);
        String[][] nuevaLineaLista = new String[1][4];

        for(int i=0; i<nuevaLineaLista.length; i++){
            for(int j=0; j<nuevaLineaLista[i].length; j++){
                if(j==0){
                    nuevaLineaLista[i][j] = String.valueOf(contarFilas("listaEstablecimientos.csv"));
                }
                if(j==1){
                    nuevaLineaLista[i][j] = nombreEstablecimiento;
                }
                if(j==2){
                    System.out.println("Introduzca el Código Postal de " + nombreEstablecimiento +":");
                    System.out.print("-> ");
                    nuevaLineaLista[i][j] = teclado.next();
                }
                if(j==3){
                    System.out.println("""
                                ¿Desea añadir alguna información extra?
                                -> 1 - Si
                                -> 2 - No""");
                    System.out.print("--> ");
                    int decisionInfoExtra;
                    while(!teclado.hasNextInt(1|2)){
                        System.out.println("Opción Incorrecta. Porfavor Revise...");
                        System.out.print("-> ");
                        teclado.next();

                    }decisionInfoExtra = teclado.nextInt();
                    teclado.nextLine();
                    if(decisionInfoExtra==1){
                        System.out.println("Introduzca la Información Extra que desee:");
                        nuevaLineaLista[i][j] = teclado.nextLine();

                    }else{
                        nuevaLineaLista[i][j] = "No presenta ninguna información extra";
                    }

                }
            }
        }

        escribirEnFichero(nuevaLineaLista, "listaEstablecimientos.csv", true);

    }

    public static void escribirEnFichero (String [][] nuevaLinea, String ruta, boolean NoSobreEscribir){

        try {
            FileWriter fr = new FileWriter(ruta,NoSobreEscribir);
            BufferedWriter escribir = new BufferedWriter(fr);
            for(int i=0;i<nuevaLinea.length;i++){
                for(int j=0; j<nuevaLinea[i].length; j++){
                    escribir.write(nuevaLinea[i][j]);
                    if(j<4){
                        escribir.write(";");
                    }


                }
                escribir.write("\n");
            }
            System.out.println("Guardado con éxito!");
            escribir.close();
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error al guardar los datos en el fichero");
        }

    }


    public static String[][] leerFichero(String ruta){

        String[][] matriz_Fichero = {};
        File fichero = new File(ruta);

        try {
            Scanner sc = new Scanner(fichero);
            matriz_Fichero = new String[contarFilas(ruta)][contarColumnas(ruta)];

            int i=0;
            String fila;
            String [] array_fila;

            while (sc.hasNextLine()){
                fila = sc.nextLine();
                array_fila = fila.split(";");

                System.arraycopy(array_fila, 0, matriz_Fichero[i], 0, array_fila.length);
                i++;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ha ocurrido un error en la lectura del fichero");
        }

        return matriz_Fichero;
    }

    public static void imprimirFichero(String[][] archivo){
        //Imprimir el fichero
        System.out.println("-------------------------------------------------- |");
        for (int i = 0; i < archivo.length; i++) {

            for (int j = 0; j < archivo[i].length; j++) {
                System.out.print(archivo[i][j] + "\t\t");
            }

            System.out.println();
            if(i==0){
                System.out.println("-------------------------------------------------- |");
            }
        }System.out.println("--------------------------------------------------");
    }


    public static int contarFilas(String ruta){
        int numeroFilas = 0;
        try { //Lector utilizado para contar las filas de la Lista de Establecimientos
            FileReader fr = new FileReader(ruta);
            BufferedReader contarLineas = new BufferedReader(fr);
            while(contarLineas.readLine()!=null){
                numeroFilas++;
            }
            return numeroFilas;
        } catch (IOException e) { //Excepciones en caso de que la lectura del fichero
            System.out.println("Ha ocurrido un error en la lectura de la Lista de Establecimientos");
        }

        return numeroFilas;
    }

    public static int contarColumnas(String ruta) {
        int numeroColumnas;
        String fila = "";

        File fichero = new File(ruta);
        try {
            Scanner sc = new Scanner(fichero);
            if (sc.hasNextLine()) {
                fila = sc.nextLine(); // se guarda la primera linea del archivo en el string fila
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error, no se puede abrir el fichero.");
        }
        numeroColumnas = fila.split(";").length; //se separa el contenido del string y se crea un array en con cada elemento
        // separado por ';' en el sting y el numero de elementos de ese String corresponde
        // al numero de columnas que debe tener la matriz

        return numeroColumnas;
    }

    public static String menuInicial(){

        Scanner teclado = new Scanner(System.in);
        String nombreEstablecimiento;
        String ruta;

        //Informar de las opciones
        System.out.print(

                """

                        ---------------- Bienvenido a Gestion de Stock V2 ----------------
                         --> 0 - Apagar\s
                         --> Introduzca el establecimiento que desea gestionar:
                        Opción:"""

        );
        //recoger la opción

        nombreEstablecimiento = teclado.next();

        return nombreEstablecimiento;
    }


    public static int menuMain(String nombreMunicipio){

        int opcion;
        Scanner teclado = new Scanner(System.in);

        //Informar de las opciones
        System.out.print("  ---------------- Bienvenido a Gestion de Stock de "+ nombreMunicipio + " ----------------\n" +
                            "  --> 0 - Atrás" + "\n" +
                            "  --> 1 - Consultar" + "\n" +
                            "  --> 2 - Añadir" + "\n" +
                            "  --> 3 - Modificar" + "\n" +
                            "  --> 4 - Eliminar" + "\n" +
                            "  Opción:");
        //recoger la opción

        while(!teclado.hasNextInt(1 | 2 | 3 | 4)){ //Verifica que el valor introducido es válido para este Menu
            System.out.println("Opción Incorrecta, porfavor revise...");
            System.out.print("-> ");
            teclado.next(); //Limpiamos la basura del Scanner
        }
        opcion = teclado.nextInt();
        return opcion;


    }

    public static int menuConsultar(){

        int opcionConsultar;
        Scanner teclado = new Scanner(System.in);

        //Informar de las opciones
        System.out.print(

                """

                        ---------------- Consultar ----------------
                         --> 0 - Atrás
                         --> 1 - Productos
                         --> 2 - Lista Establecimientos
                        Opción:"""

        );
        //recoger la opción

        while(!teclado.hasNextInt(1 | 2)){ //Verifica que el valor introducido es válido para este Menu
            System.out.println("Opción Incorrecta, porfavor revise...");
            System.out.print("->");
            teclado.next(); //Limpiamos la basura del Scanner
        }
        opcionConsultar = teclado.nextInt();
        return opcionConsultar;


    }

    public static int menuAñadir(){

        int opcionAñadir=-1;
        Scanner teclado = new Scanner(System.in);

        //Informar de las opciones
        System.out.print(

                """

                        ---------------- Añadir ----------------
                         --> 0 - Atrás
                         --> 1 - Producto
                        Opción:"""

        );
        //recoger la opción

       /* while(!teclado.hasNextInt(1)){ //Verifica que el valor introducido es válido para este Menu
            System.out.println("Opción Incorrecta, porfavor revise...");
            System.out.print("->");
            teclado.next(); //Limpiamos la basura del Scanner
        }*/
        opcionAñadir = teclado.nextInt();
        return opcionAñadir;


    }

    public static int menuModificar(){

        int opcionModificar;
        Scanner teclado = new Scanner(System.in);

        //Informar de las opciones
        System.out.print(

                """

                        ---------------- Modificar ----------------
                         --> 0 - Atrás
                         --> 1 - Producto
                         --> 2 - Establecimiento
                        Opción:"""

        );
        //recoger la opción

        while(!teclado.hasNextInt(1 | 2)){ //Verifica que el valor introducido es válido para este Menu
            System.out.println("Opción Incorrecta, porfavor revise...");
            System.out.print("->");
            teclado.next(); //Limpiamos la basura del Scanner
        }
        opcionModificar = teclado.nextInt();
        return opcionModificar;


    }

    public static int menuModificarProducto( String nombreEstablecimiento){

        int opcionModificarProducto;
        Scanner teclado = new Scanner(System.in);

        //Informar de las opciones
        System.out.print(

                "\n---------------- Modificar Producto (" + nombreEstablecimiento + ") ----------------" + "\n" +
                        " --> 0 - Atrás" + "\n" +
                        " --> 1 - Nombre" + "\n" +
                        " --> 2 - Stock de Producto" + "\n" +
                        " --> 3 - Precio" + "\n" +
                        " --> 4 - Información Extra" + "\n" +
                        "Opción:"

        );
        //recoger la opción

        while(!teclado.hasNextInt(0|1|2|3|4)){ //Verifica que el valor introducido es válido para este Menu
            System.out.println("Opción Incorrecta, porfavor revise...");
            System.out.print("->");
            teclado.next(); //Limpiamos la basura del Scanner
        }
        opcionModificarProducto = teclado.nextInt();
        return opcionModificarProducto;
    }

    public static int menuModificarEstablecimiento( String nombreEstablecimiento){

        int opcionModificarEstablecimiento;
        Scanner teclado = new Scanner(System.in);

        //Informar de las opciones
        System.out.print(

                "\n---------------- Modificar " + nombreEstablecimiento + " ----------------" + "\n" +
                        " --> 0 - Atrás" + "\n" +
                        " --> 1 - Nombre Localidad" + "\n" +
                        " --> 2 - Código Postal" + "\n" +
                        " --> 3 - Información Extra" + "\n" +
                        "Opción:"

        );
        //recoger la opción

        while(!teclado.hasNextInt(0|1|2|3)){ //Verifica que el valor introducido es válido para este Menu
            System.out.println("Opción Incorrecta, porfavor revise...");
            System.out.print("->");
            teclado.next(); //Limpiamos la basura del Scanner
        }
        opcionModificarEstablecimiento = teclado.nextInt();
        return opcionModificarEstablecimiento;
    }

    public static int menuEliminar(){

        int opcionEliminar;
        Scanner teclado = new Scanner(System.in);

        //Informar de las opciones
        System.out.print(

                """

                        ---------------- Eliminar  ----------------
                         --> 0 - Atrás
                         --> 1 - Producto
                         --> 2 - Establecimiento
                        Opción:"""

        );
        //recoger la opción

        while(!teclado.hasNextInt(1 | 2)){ //Verifica que el valor introducido es válido para este Menu
            System.out.println("Opción Incorrecta, porfavor revise...");
            System.out.print("->");
            teclado.next(); //Limpiamos la basura del Scanner
        }
        opcionEliminar = teclado.nextInt();
        return opcionEliminar;
    }

    public static void escribirEnLog(String ruta, String texto){

        BufferedWriter bw = null;
        try {
            // --------------------
            // APPEND MODE SET HERE  src:https://alvinalexander.com/java/how-to-append-text-file-filewriter-bufferedwriter/
            // --------------------
            bw = new BufferedWriter(new FileWriter(ruta, true));
            bw.newLine(); // añade una separacion entre lineas
            bw.write(texto); // escribe el contenido indicado como parametro en el documento indicado
            bw.flush(); // guarda el archivo
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally { // always close the file
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException ioe2) {
                    // just ignore it
                }
        } // end try/catch/finally
    }

    public static void pulsaCualquierTeclaParaContinuar() {
        System.out.println("pulsa la tecla enter para continuar...");
        try {
            System.in.read(); //espera una entrada por el usuario para continuar la ejecucion del programa
        } catch (Exception e) {
        }
    }

    public static void main (String[] args){

        int opcion = -1;
        String nombreEstablecimiento="-1";
        File archivoLista = new File("listaEstablecimientos.csv");
        if(!archivoLista.exists()){ //Comprueba si la Lista existe
            crearFichero("listaEstablecimientos.csv","IDEstablecimiento; Nombre; Código Postal; Información Extra\n" ); //Si es negativo lo crea
        }//En ambos casos sigue el curso normal
        LocalDateTime FechaActual = LocalDateTime.now();
        DateTimeFormatter FechaFormateada = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String FechaFinal = FechaActual.format(FechaFormateada);
        escribirEnLog("log.txt","[  " + FechaFinal +" ] " +  "Encendiendo Prometheus v2...");

        while(!nombreEstablecimiento.equalsIgnoreCase("0")){
                opcion = -1; // Declaramos que la variable del bucle menus es negativa para que no entre en el, se utiliza a partir de la segunda vuelta de bucle del Inicial
            nombreEstablecimiento = menuInicial(); // MENU INICIAL
            String ruta = nombreEstablecimiento + ".csv";
            File fichero = new File(ruta);
            Scanner tecladoInicial = new Scanner(System.in);

            if(!nombreEstablecimiento.equalsIgnoreCase("0")){
                int decisionCrearStock = -1;
                if(!fichero.exists()){
                    System.out.println("No se ha podido encontrar el almacenamiento de " + nombreEstablecimiento + " en nuestros datos.");
                    System.out.println("¿Desea darlo de alta?.");
                    System.out.print("1. Si.\n" +
                            "2. No.\n" +
                            "->");
                    escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "No se ha encontrado el establecimiento " + nombreEstablecimiento + ", ¿Desea crearlo?.");
                    while(!tecladoInicial.hasNextInt(1|2)){
                        System.out.println("Opción Incorrecta. Porfavor revise...");
                    }
                    decisionCrearStock = tecladoInicial.nextInt();
                    if(decisionCrearStock==1){
                        escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Si. Se crea el establecimiento.");
                        crearEstablecimiento(nombreEstablecimiento,ruta); // Crea el almacenamiento y sigue con el siguiente bucle de menus
                    } else { opcion = 0; escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "No. Volviendo al menu Inicial..."); }  // Se especifica que no se desea dar de alta el establecimiento , declaramos la variable del siguiente bucle de
                                             // menus como negativo y por lo tanto se mantendria en el bucle Inicial
                }
            }   else {  break;  } // Se especifica que Apagar = 0 ,escribimos un break para salir del bucle Inicial y por lo tanto el programa se apaga
        while(opcion!=0){
            opcion = menuMain(nombreEstablecimiento);

            switch (opcion){

                case 1: //CONSULTAR
                    System.out.println("Accediendo al menu de Consultar...");
                    escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Accediendo al menu Consultar...");
                    int opcionConsultar = -1;


                    while(opcionConsultar!=0){
                        opcionConsultar = menuConsultar();
                        Scanner tecladoConsultar = new Scanner(System.in);
                        switch (opcionConsultar) {
                            case 0 -> //Atrás
                                    System.out.println("Volviendo al menu principal...");
                            case 1 -> { //Consultar Productos por cada Establecimiento


                                File archivoProducto = new File(ruta);
                                if (!archivoProducto.exists()) {
                                    System.out.println("El Establecimiento " + nombreEstablecimiento + " no se encuentra en nuestros datos.");
                                    escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "No se encuentra el establecimiento en los datos.");
                                    break;
                                }
                                String[][] archivo = leerFichero(ruta);
                                imprimirFichero(archivo);
                                escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Mostrando lista de productos de " + nombreEstablecimiento);
                                pulsaCualquierTeclaParaContinuar();
                            }
                            case 2 -> {//Consultar todos los Establecimientos
                                File archivoListaEstablecimientos = new File("listaEstablecimientos.csv");
                                if (!archivoListaEstablecimientos.exists()) {
                                    System.out.println("No se ha podido encontrar la lista de Establecimientos");
                                    escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "No se encuentra la lista de establecimientos.");
                                    break;
                                }
                                String[][] ficheroLista = leerFichero("listaEstablecimientos.csv");
                                imprimirFichero(ficheroLista);
                                escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Mostrando lista de establecimientos.");
                                pulsaCualquierTeclaParaContinuar();
                            }
                        }
                    }escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Volviendo al menu principal...");
                    break;

                case 2: //AÑADIR
                    System.out.println("Accediendo al menu de Añadir...");
                    escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Accediendo al menu Añadir...");
                    int opcionAñadir = -1;
                    while(opcionAñadir!=0){
                        opcionAñadir = menuAñadir();
                        Scanner tecladoAñadir = new Scanner(System.in);
                        switch (opcionAñadir) {
                            case 0 -> //Atrás
                                    System.out.println("Volviendo al menu principal...");
                            case 1 -> { // AÑADIR Producto
                                crearProducto(ruta, nombreEstablecimiento);
                                escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Se añaden nuevos productos al almacenamiento de " + nombreEstablecimiento);
                            }
                        }//FIN Switch Añadir
                    }escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Volviendo al menu principal...");


                    break;
                case 3: //MODIFICAR
                    System.out.println("Accediendo al menu de Modificar...");
                    escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Accediendo al menu Modificar...");
                    Scanner tecladoModificar = new Scanner(System.in);
                    int opcionEstablecimientos = -1;
                    while(opcionEstablecimientos!=0){
                        opcionEstablecimientos = menuModificar();
                        switch (opcionEstablecimientos) {
                            case 0 -> //Atrás
                                    System.out.println("Volviendo al menu principal...");
                            case 1 -> { //MODIFICAR Producto
                                int opcionModificarProducto = -1;
                                while (opcionModificarProducto != 0) {
                                    opcionModificarProducto = menuModificarProducto(nombreEstablecimiento);
                                    modificarProducto(opcionModificarProducto, nombreEstablecimiento);
                                }
                            }
                            case 2 -> { //MODIFICAR Establecimiento
                                int opcionModificarEstablecimiento = -1;
                                while (opcionModificarEstablecimiento != 0) {
                                    opcionModificarEstablecimiento = menuModificarEstablecimiento(nombreEstablecimiento);
                                    modificarEstablecimiento(opcionModificarEstablecimiento, nombreEstablecimiento);
                                }
                            }
                        }  escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Volviendo al menu principal...");//FIN Switch modificarEstablecimientos
                    }
                    break;
                case 4://ELIMINAR
                    System.out.println("Accediendo al menu de Eliminar...");
                    escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Volviendo al menu Eliminar...");
                    int opcionEliminar = -1;
                    while(opcionEliminar!=0){
                        opcionEliminar = menuEliminar();
                        Scanner tecladoEliminar = new Scanner(System.in);
                        switch (opcionEliminar) {
                            case 0 -> //Atrás
                                    System.out.println("Volviendo al menu principal...");
                            case 1 -> { // ELIMINAR Producto

                                eliminarProducto(nombreEstablecimiento);
                            }
                            case 2 -> {//ELIMINAR Establecimiento

                                eliminarEstablecimiento(nombreEstablecimiento);
                            }
                        }
                    }
                    break;
            } escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Volviendo al menu principal...");// FIN Main Switch
          }     nombreEstablecimiento = "-1"; // Declaramos que es -1 para que no se salga del bucle del menu Inicial
            escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Volviendo al menu Inicial...");
        }
        System.out.println("Hasta luego...");
        escribirEnLog("log.txt", "[  " + FechaFinal +" ] " +  "Apagando Prometheus v2...");
    }
}

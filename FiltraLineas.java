import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class FiltraLineas {
    public static void main(String[] args) {
        
        /* Esto lo que hace es comprobar que al ejecutar el programa se le haya
        pasado al menos 1 argumento, ese argumento va a ser el comando grep, si no
        se introduce ninguno el programa se va a detener */
        if (args.length == 0) {
            System.out.println("Tienes que introducir un parámetro");
            return;
        }


        String patron = args[0];

        /* Utilizamos la clae scanner con el paramentro system.in para leer los datos
        que metamos por le teclado */
        Scanner scanner = new Scanner(System.in);

        //creamos un contador
        int contador = 0;
        System.out.println("Introduce las lineas (maximo 6 o enter para salir)");

        /* Hacemos un bucle while para leer linea por linea. No se pueden superar
        las 6 lineas escritas y si le damos a enter y está vacía la linea se sale */
        while (contador < 6) {
            String linea = scanner.nextLine();

            //si la línea está vacía se sale del programa
            if (linea.isEmpty()) {
                break;
            }

            contador ++;
            
            try {
                /* Creamos el proceso para poder ejecutar comando grep y el parámetro
                que hemos recibido. Lo lanza para poder devolver el process que es el 
                proceso hijo.
                El linux el comando es grep pero en windows el comando es findstr y 
                para que no me diera problemas lo he cambiado*/
                ProcessBuilder processBuilder = new ProcessBuilder("findstr", patron);
                Process process = processBuilder.start();

                /* Enviamos algo al proceso hijo para poder obtener la salida del hijo */
                PrintWriter salida = new PrintWriter(process.getOutputStream());
                salida.println(linea);
                salida.flush(); //Esto sirve para asegurarnos de que la informacion llega directamente
                salida.close(); //Cerramos el flujo de salida para que el hijo sepa que no hay mas nada

                /* Abrimos el flujo de entrada para leer lo que nos ha mandado el
                hijo, lo lee y guarda la respuesta que nos ha dado el hijo */
                BufferedReader entrada = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String respuesta = entrada.readLine();

                /* Esto nos sirve para comprobar que si el proceso hijo ha devuelto
                algo, si es distinto de nulo significa que ha encontrado la palabra */
                if (respuesta != null) {
                    System.out.println("La linea " + contador + " si contiene");
                } else {
                    System.out.println("La linea " + contador + " no contiene");
                }

                /* El padre espera a que el hijo termine */
                process.waitFor();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }

        //Cerramos el scanner y mostramos un mensaje final como que todo ha ido bien
        scanner.close();
        System.out.println("El programa ha funcionado bien");

    }
}

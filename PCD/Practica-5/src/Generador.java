import javax.swing.*;
import java.util.Random;

public class Generador {
    private static final int NUMEROCOCHES = 15;
    private static final int NUMEROCAMIONES = 15;
    private static final int NUMEROVEHICULOS = 30;

    public static void main(String[] args) throws InterruptedException {
        Random rd = new Random();
        CanvasGasolinera canvas = new CanvasGasolinera(900, 600);

        Gasolinera gasolinera = new Gasolinera(canvas);

        JFrame frame = new JFrame("Simulación Gasolinera");
        frame.add(canvas);
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Thread[] coches = new Thread[NUMEROCOCHES];
        Camion[] camiones = new Camion[NUMEROCAMIONES];
        Thread[] camionThreads = new Thread[NUMEROCAMIONES];

        for (int i = 0; i < NUMEROCOCHES; i++) {
            coches[i] = new Thread(new Coche(i, gasolinera, canvas, true));
        }

        for (int i = 0; i < NUMEROCAMIONES; i++) {
            camiones[i] = new Camion(i + NUMEROCOCHES, gasolinera, canvas, false);
            camionThreads[i] = new Thread(camiones[i]);
        }

        int numeroCamionesLanzados = 0, numeroCochesLanzados = 0;

        for (int i = 0; i < NUMEROVEHICULOS; i++) {
            if (rd.nextBoolean() && numeroCamionesLanzados < NUMEROCAMIONES) {
                camionThreads[numeroCamionesLanzados].start(); // Iniciar hilo de camión
                numeroCamionesLanzados++;
            } else if (numeroCochesLanzados < NUMEROCOCHES) {
                coches[numeroCochesLanzados].start(); // Iniciar hilo de coche
                numeroCochesLanzados++;
            }

            Thread.sleep(500); // Esperar antes de lanzar el siguiente vehículo
        }

        // Esperar a que todos los coches y camiones terminen
        for (int i = 0; i < numeroCochesLanzados; i++) {
            coches[i].join();
        }

        for (int i = 0; i < numeroCamionesLanzados; i++) {
            camionThreads[i].join();
        }

        System.out.println("Termina MAIN.");
    }
}

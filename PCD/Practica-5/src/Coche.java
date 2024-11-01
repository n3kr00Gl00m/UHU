import java.util.Random;

public class Coche implements Runnable {
    private final int id;
    private final Gasolinera gasolinera;
    private final CanvasGasolinera canvas;
    private boolean enCentrales;

    public Coche(int id, Gasolinera gasolinera, CanvasGasolinera canvas, boolean enCentrales) {
        this.id = id;
        this.gasolinera = gasolinera;
        this.canvas = canvas;
        this.enCentrales = enCentrales;
    }

    @Override
    public void run() {
        try {
            int posicion = gasolinera.entraCoche(id); // El coche entra a la gasolinera y obtiene la posición
            Thread.sleep(new Random().nextInt(1000) + 2000); // Simula el tiempo de repostaje
            gasolinera.saleCoche(this.id, posicion); // El coche sale de la gasolinera
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

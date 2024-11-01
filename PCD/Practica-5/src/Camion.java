import java.util.Random;

public class Camion implements Runnable {
    private final int id;
    private final Gasolinera gasolinera;
    private final CanvasGasolinera canvas;
    private boolean enCentrales;

    public Camion(int id, Gasolinera gasolinera, CanvasGasolinera canvas, boolean b) {
        this.id = id;
        this.gasolinera = gasolinera;
        this.canvas = canvas;
        this.enCentrales = b;
    }

    @Override
    public void run() {
        try {
            int posicion = gasolinera.entraCamion(id); // El camion entra a la gasolinera y obtiene la posicion
            Thread.sleep(new Random().nextInt(1000) + 1000); // Simula el tiempo de repostaje
            gasolinera.saleCamion(this.id, posicion); // El camión sale de la gasolinera
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

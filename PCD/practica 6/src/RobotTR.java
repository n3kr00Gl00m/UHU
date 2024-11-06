import java.util.Random;
import java.util.concurrent.Semaphore;

public class RobotTR extends Thread {
    private final Semaphore nBlanco, cBlanco;
    private final Semaphore nRojo, cRojo;
    private final CanvasPintura canvas;
    private boolean parar = false;
    private final Random rd;

    public RobotTR(Semaphore nBlanco, Semaphore cBlanco, Semaphore nRojo, Semaphore cRojo, CanvasPintura canvas) {
        this.nBlanco = nBlanco;
        this.cBlanco = cBlanco;
        this.nRojo = nRojo;
        this.cRojo = cRojo;
        this.canvas = canvas;
        this.rd = new Random();
    }

    public void detener() {
        parar = true;
    }

    @Override
    public void run() {
        while (!parar) {
            try {
                //Ponemos la espera aquí y no al final para que los tanques no aparezcan rellenos "previamente"
                Thread.sleep((rd.nextInt(4) + 2) * 1000);


                //Pintar camion
                int cantidadBlanco = rd.nextInt(4) + 3;
                int cantidadRojo = rd.nextInt(4) + 3;


                this.canvas.pintarCamion(cantidadBlanco, cantidadRojo);

                // Reponer de 3 a 6 litros de blanco
                for (int i = 0; i < cantidadBlanco; i++) {
                    cBlanco.acquire();
                    this.canvas.rellenaColor('b');
                    nBlanco.release();
                }

                // Reponer de 3 a 6 litros de rojo
                for (int i = 0; i < cantidadRojo; i++) {
                    cRojo.acquire();
                    this.canvas.rellenaColor('r');

                    nRojo.release();
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

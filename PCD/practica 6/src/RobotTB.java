import java.util.Random;
import java.util.concurrent.Semaphore;

public class RobotTB implements Runnable {
    private final Semaphore nBlanco, cBlanco;
    private final Semaphore nRojo, cRojo;
    private final CanvasPintura canvas;
    private final String id;
    private final Random rd;

    private int idRobot;


    public RobotTB(Semaphore nBlanco, Semaphore cBlanco, Semaphore nRojo, Semaphore cRojo, int id, CanvasPintura canvas) {
        this.nBlanco = nBlanco;
        this.cBlanco = cBlanco;
        this.nRojo = nRojo;
        this.cRojo = cRojo;
        this.id = "ROBOT-TB-" + id;
        this.rd = new Random();
        this.canvas = canvas;
        this.idRobot = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < 6; i++) {
            try {
                // Coger 2 de blanco
                this.nBlanco.acquire();
                this.canvas.cogeBlanco(idRobot, 'b');

                this.cBlanco.release();

                this.nBlanco.acquire();
                this.canvas.cogeBlanco(idRobot, 'b');

                this.cBlanco.release();

                // Coger 1 de rojo
                this.nRojo.acquire();
                this.canvas.cogeRojo(idRobot, 'b');

                this.cRojo.release();

                // Esperar
                Thread.sleep((rd.nextInt(3) + 1) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

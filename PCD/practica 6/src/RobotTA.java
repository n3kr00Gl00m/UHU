import java.util.Random;
import java.util.concurrent.Semaphore;

public class RobotTA extends Thread {
    private final Semaphore nBlanco, cBlanco;
    private final Semaphore nRojo, cRojo;
    private final CanvasPintura canvas;
    private final String id;
    private int idRobot;
    private final Random rd;

    public RobotTA(Semaphore nBlanco, Semaphore cBlanco, Semaphore nRojo, Semaphore cRojo, int id, CanvasPintura canvas) {
        this.nBlanco = nBlanco;
        this.cBlanco = cBlanco;
        this.nRojo = nRojo;
        this.cRojo = cRojo;
        this.idRobot = id;

        this.id = "ROBOT-TA-" + id;
        this.rd = new Random();
        this.canvas = canvas;
    }

    @Override
    public void run() {
        for (int i = 0; i < 6; i++) {
            try {
                // Coger 2 de rojo
                this.nRojo.acquire();
                this.canvas.cogeRojo(idRobot, 'a');
                this.cRojo.release();

                this.nRojo.acquire();
                this.canvas.cogeRojo(idRobot, 'a');

                this.cRojo.release();

                // Coger 1 de blanco
                this.nBlanco.acquire();
                this.canvas.cogeBlanco(idRobot, 'a');

                this.cBlanco.release();

                // Esperar
                Thread.sleep((rd.nextInt(3) + 1) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

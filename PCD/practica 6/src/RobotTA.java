import java.util.Random;
import java.util.concurrent.Semaphore;

public class RobotTA extends Thread{
    private final Semaphore nBlanco, cBlanco;
    private final Semaphore nRojo, cRojo;

    private Random rd;

    public RobotTA(Semaphore nBlanco, Semaphore cBlanco, Semaphore nRojo, Semaphore cRojo) {
        this.nBlanco = nBlanco;
        this.cBlanco = cBlanco;
        this.nRojo = nRojo;
        this.cRojo = cRojo;
        rd = new Random();
    }

    @Override
    public void run() {
            for(int i = 0; i<6; i++){
            try {
                //Dos blancos
                this.nBlanco.acquire();
                this.cBlanco.release();
                this.nBlanco.acquire();
                this.cBlanco.release();

                //Uno rojo
                this.nRojo.acquire();
                this.cRojo.release();

                //Esperar

                sleep((rd.nextInt(1)+1)*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

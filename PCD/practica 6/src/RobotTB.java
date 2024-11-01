import java.util.Random;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class RobotTB implements Runnable{
    final Semaphore nBlanco, cBlanco;
    final Semaphore nRojo, cRojo;
    private Random rd;

    public RobotTB(Semaphore nBlanco, Semaphore cBlanco, Semaphore nRojo, Semaphore cRojo) {
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
                this.nRojo.acquire();
                this.cRojo.release();
                this.nRojo.acquire();
                this.cRojo.release();

                //Uno rojo
                this.nBlanco.acquire();
                this.cBlanco.release();

                //Esperar
                sleep((rd.nextInt(1)+1)*1000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

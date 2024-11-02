import java.util.Random;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class RobotTB implements Runnable{
    final Semaphore nBlanco, cBlanco;
    final Semaphore nRojo, cRojo;
    private Random rd;
    private String id;

    public RobotTB(Semaphore nBlanco, Semaphore cBlanco, Semaphore nRojo, Semaphore cRojo, int id) {
        this.nBlanco = nBlanco;
        this.cBlanco = cBlanco;
        this.nRojo = nRojo;
        this.cRojo = cRojo;
        this.id = "GRANATE-" + id;

        rd = new Random();
    }

    @Override
    public void run() {
        for(int i = 0; i<6; i++){
            try {

                //Dos blancos
                this.nBlanco.acquire();
                System.out.println(id + "-> Coge BLANCO ");
                this.cBlanco.release();

                this.nBlanco.acquire();
                System.out.println(id + "-> Coge BLANCO ");
                this.cBlanco.release();

                //Uno rojo
                this.nRojo.acquire();
                System.out.println(id + "-> Coge ROJO ");
                this.cRojo.release();

                //Esperar

                sleep((rd.nextInt(1)+1)*1000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

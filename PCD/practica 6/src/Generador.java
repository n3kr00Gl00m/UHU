    import javax.swing.*;
    import java.util.concurrent.Semaphore;

    public class Generador {

        /**
         * @cRojo -> capacidad rojo se inicializa a 7 y será lo
         *           que indique al camion que no puede recargar mas combustible mediante una espera.
         *
         * @cBlanco -> capacidad blanco se inicializa a 7 y será lo
         *             que indique al camion que no puede recargar mas combustible mediante una espera.
         *
         * @nRojo -> nivel que está disponible para la recarga y que será la que coordine directamente
         *           a los robots para que utilicen el color rojo
         *
         * @nBlanco -> nivel que está disponible para la recarga y que será la que coordine directamente
         *             a los robots para que utilicen el color rojo
         */
        public static void main(String[] args) throws InterruptedException {
            Semaphore nRojo, nBlanco,cBlanco,cRojo;
            CanvasPintura canvasPintura = new CanvasPintura();
            JFrame frame = new JFrame("Simulación de Generador de Pintura");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(canvasPintura);
            frame.pack();
            frame.setVisible(true);

            nRojo = new Semaphore(0);
            nBlanco = new Semaphore(0);
            cRojo = new Semaphore(7);
            cBlanco = new Semaphore(7);

            RobotTA[] robotsTA = new RobotTA[2];
            Thread[] robotsTB = new Thread[2];
            RobotTR reponedor = new RobotTR(nBlanco,cBlanco,nRojo,cRojo, canvasPintura);

            for(int i = 0; i<2; i++) {
                robotsTB[i] = new Thread(new RobotTB(nBlanco,cBlanco,nRojo,cRojo, i,canvasPintura));
                robotsTA[i] = new RobotTA(nBlanco,cBlanco,nRojo,cRojo, i, canvasPintura);
            }

            for(int i = 0; i<2; i++) {
                robotsTB[i].start();
                robotsTA[i].start();
            }

            reponedor.start();

            for(int i = 0; i<2; i++) {
                robotsTB[i].join();
            }
            for(int i = 0; i<2; i++) {
                robotsTA[i].join();
            }
            reponedor.detener();
            reponedor.join();
        }
    }
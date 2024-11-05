import javax.swing.*;
import java.awt.*;

public class CanvasPintura extends JPanel {

    private int[] pinturaEnRobotsRosa = new int[2];
    private int[] pinturaEnRobotsGranate = new int[2];

    private int botesDePinturaGranate = 0;
    private int botesDePinturaRosa = 0;

    private int pinturaEnTanqueBlanco = 0; // Nivel inicial
    private int pinturaEnTanqueRojo = 0; // Nivel inicial

    private final int maxPinturaRobot = 3;
    private final int maxNivelTanque = 7; // Máximo nivel de pintura en los tanques
    private int camionPinturaRoja;
    private int camionPinturaBlanca;


    public CanvasPintura() {
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.LIGHT_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int robotWidth = 50;
        int robotHeight = 50;
        int tanqueWidth = 100;
        int tanqueHeight = 200;
        int spacingX = 100;
        int spacingY = 150;
        int spacingXTanque = 150;

        // Dibuja los robots TA
        for (int i = 0; i < 2; i++) {
            int xPosition = 50 + i * spacingX;
            int yPosition = 50;

            // Dibuja robot TA con su cantidad de pintura
            g.setColor(Color.BLUE);
            g.fillRect(xPosition, yPosition, robotWidth, robotHeight);
            g.setColor(Color.BLACK);
            g.drawString("Robot TA " + i, xPosition, yPosition - 5);

            for (int j = 0; j < pinturaEnRobotsGranate[i]; j++) {
                g.setColor(Color.RED);
                g.fillRect(xPosition + 5, yPosition + 5 + j * 10, 10, 10);
            }
        }

        // Dibuja los robots TB
        for (int i = 0; i < 2; i++) {
            int xPosition = 50 + i * spacingX;
            int yPosition = 50 + spacingY;

            // Dibuja robot TB con su cantidad de pintura
            g.setColor(Color.GREEN);
            g.fillRect(xPosition, yPosition, robotWidth, robotHeight);
            g.setColor(Color.BLACK);
            g.drawString("Robot TB " + i, xPosition, yPosition - 5);

            for (int j = 0; j < pinturaEnRobotsRosa[i]; j++) {
                g.setColor(Color.PINK);
                g.fillRect(xPosition + 5, yPosition + 5 + j * 10, 10, 10);
            }
        }

        // Dibuja los tanques en el lado derecho
        int xPositionTanque = 300;
        int yPositionTanque = 50;

        // Tanque Rojo
        drawTanque(g, xPositionTanque, yPositionTanque, tanqueWidth, tanqueHeight, pinturaEnTanqueRojo, Color.RED, "Tanque Rojo");

        // Tanque Blanco
        drawTanque(g, xPositionTanque + tanqueWidth + spacingXTanque, yPositionTanque, tanqueWidth, tanqueHeight, pinturaEnTanqueBlanco, Color.WHITE, "Tanque Blanco");

        // Dibuja los rectángulos que representan la pintura del camión
        int camionWidth = 40; // Ancho de cada rectángulo
        int maxCamionHeight = 100; // Altura máxima de los rectángulos
        int spacingEntreCamiones = 20; // Espacio entre los rectángulos

        // Posiciones X para los rectángulos del camión
        int camionXRojo = (xPositionTanque + (tanqueWidth / 2)) - (camionWidth + spacingEntreCamiones); // A la izquierda
        int camionXBlanco = camionXRojo + camionWidth + spacingEntreCamiones; // A la derecha del rojo

        // Altura de pintura roja y blanca en el camión
        int camionRojoHeight = (camionPinturaRoja * maxCamionHeight) / maxNivelTanque;
        int camionBlancoHeight = (camionPinturaBlanca * maxCamionHeight) / maxNivelTanque;

        // Dibuja el rectángulo de pintura roja
        g.setColor(Color.RED);
        g.fillRect(camionXRojo, yPositionTanque + tanqueHeight - camionRojoHeight, camionWidth, camionRojoHeight);

        // Dibuja el rectángulo de pintura blanca
        g.setColor(Color.WHITE);
        g.fillRect(camionXBlanco, yPositionTanque + tanqueHeight - camionBlancoHeight, camionWidth, camionBlancoHeight);

        // Mostrar contadores de botes de pintura
        g.drawString("Botes Granate: " + botesDePinturaGranate, xPositionTanque, yPositionTanque + tanqueHeight + 20);
        g.drawString("Botes Rosa: " + botesDePinturaRosa, xPositionTanque + tanqueWidth + spacingXTanque, yPositionTanque + tanqueHeight + 20);
    }

    private void drawTanque(Graphics g, int x, int y, int width, int height, int nivelPintura, Color colorPintura, String label) {
        // Fondo gris para el tanque
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);

        // Dibuja el nivel de pintura en bloques separados por divisores
        int unidadAltura = height / maxNivelTanque; // Altura de cada unidad de pintura
        for (int i = 0; i < nivelPintura; i++) {
            int yPos = y + height - (i + 1) * unidadAltura;
            g.setColor(colorPintura);
            g.fillRect(x, yPos, width, unidadAltura);

            // Línea divisoria
            g.setColor(Color.DARK_GRAY);
            g.drawLine(x, yPos, x + width, yPos);
        }

        // Bordes y etiqueta del tanque
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(label + ": " + nivelPintura, x, y - 5);
    }

    public synchronized void cogeRojo(int idRobot, char tipo) {
        if (tipo == 'a') {

            pinturaEnRobotsGranate[idRobot]++;
            if (pinturaEnRobotsGranate[idRobot] == maxPinturaRobot) {
                pinturaEnRobotsGranate[idRobot] = 0;
                botesDePinturaGranate++;
                pinturaEnTanqueRojo--;
            }
        } else {

            pinturaEnRobotsRosa[idRobot]++;
            if (pinturaEnRobotsRosa[idRobot] == maxPinturaRobot) {
                pinturaEnRobotsRosa[idRobot] = 0;
                botesDePinturaRosa++;
                pinturaEnTanqueRojo--;
            }
        }
        repaint();
    }

    public synchronized void cogeBlanco(int idRobot, char tipo) {
        if (tipo == 'a') {

            pinturaEnRobotsGranate[idRobot]++;
            if (pinturaEnRobotsGranate[idRobot] == maxPinturaRobot) {
                pinturaEnRobotsGranate[idRobot] = 0;
                botesDePinturaGranate++;
                pinturaEnTanqueBlanco--;
            }
        } else {

            pinturaEnRobotsRosa[idRobot]++;
            if (pinturaEnRobotsRosa[idRobot] == maxPinturaRobot) {
                pinturaEnRobotsRosa[idRobot] = 0;
                botesDePinturaRosa++;
                pinturaEnTanqueBlanco--;
            }
        }
        repaint();
    }

    public synchronized void rellenaColor(char color) {
        if (color == 'r' && pinturaEnTanqueRojo < maxNivelTanque) {
            pinturaEnTanqueRojo++;
            this.camionPinturaRoja--;
        } else if (color == 'b' && pinturaEnTanqueBlanco < maxNivelTanque) {
            pinturaEnTanqueBlanco++;
            this.camionPinturaBlanca--;
        }
        repaint();
    }

    public void pintarCamion(int cantidadBlanco, int cantidadRojo) {
        this.camionPinturaBlanca = cantidadBlanco;
        this.camionPinturaRoja = cantidadRojo;
    }
}

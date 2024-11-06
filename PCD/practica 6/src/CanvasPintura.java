import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CanvasPintura extends JPanel {

    private int[] pinturaEnRobotsRosa = new int[2];
    private int[] pinturaEnRobotsGranate = new int[2];

    //private static HashMap<Integer, ArrayList<Character>> registroColores = new HashMap<>(); // Almacena el idRobot y su lista de colores

    private int botesDePinturaGranate = 0;
    private int botesDePinturaRosa = 0;

    private int pinturaEnTanqueBlanco = 0; // Nivel inicial
    private int pinturaEnTanqueRojo = 0; // Nivel inicial

    private final int maxPinturaRobot = 3;
    private final int maxNivelTanque = 7; // Máximo nivel de pintura en los tanques
    private int camionPinturaRoja = 0;
    private int camionPinturaBlanca = 0;


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
        int spacingXTanque = 200;

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


        // Dibuja los camiones con pintura roja y blanca
        int camionWidth = 40; // Ancho de cada camión
        int maxCamionHeight = 100; // Altura máxima de cada camión
        int spacingEntreCamiones = 20; // Espacio entre los camiones
        int xPositionCamion = xPositionTanque + camionWidth+ (spacingXTanque/3)+ spacingEntreCamiones ;

        drawCamion(g, xPositionCamion, yPositionTanque, camionWidth, maxCamionHeight,
                camionPinturaRoja, camionPinturaBlanca, maxNivelTanque,
                Color.RED, Color.WHITE, "Rojo", "Blanco", spacingEntreCamiones);


        // Mostrar contadores de botes de pintura
        g.drawString("Botes Granate: " + botesDePinturaGranate, xPositionTanque, yPositionTanque + tanqueHeight + 20);
        g.drawString("Botes Rosa: " + botesDePinturaRosa, xPositionTanque + tanqueWidth + spacingXTanque, yPositionTanque + tanqueHeight + 20);
    }


    private void drawCamion(Graphics g, int x, int y, int camionWidth, int maxCamionHeight, int nivelPinturaRoja, int nivelPinturaBlanca, int maxNivelTanque, Color colorRojo, Color colorBlanco, String labelRojo, String labelBlanco, int spacingEntreCamiones) {
        // Posiciones X para los rectángulos del camión
        int camionXRojo = x; // Posición para el camión rojo
        int camionXBlanco = x + camionWidth + spacingEntreCamiones; // A la derecha del camión rojo

        // Altura de cada unidad de pintura
        int unidadAltura = maxCamionHeight / maxNivelTanque;

        // Dibuja los bloques de pintura roja con divisores
        for (int i = 0; i < nivelPinturaRoja; i++) {
            int yPos = y + maxCamionHeight - (i + 1) * unidadAltura;
            g.setColor(colorRojo);
            g.fillRect(camionXRojo, yPos, camionWidth, unidadAltura);

            // Línea divisoria
            g.setColor(Color.DARK_GRAY);
            g.drawLine(camionXRojo, yPos, camionXRojo + camionWidth, yPos);
        }

        // Dibuja los bloques de pintura blanca con divisores
        for (int i = 0; i < nivelPinturaBlanca; i++) {
            int yPos = y + maxCamionHeight - (i + 1) * unidadAltura;
            g.setColor(colorBlanco);
            g.fillRect(camionXBlanco, yPos, camionWidth, unidadAltura);

            // Línea divisoria
            g.setColor(Color.DARK_GRAY);
            g.drawLine(camionXBlanco, yPos, camionXBlanco + camionWidth, yPos);
        }

        // Bordes y etiquetas de los niveles de pintura
        g.setColor(Color.BLACK);
        g.drawRect(camionXRojo, y, camionWidth, maxCamionHeight);
        g.drawRect(camionXBlanco, y, camionWidth, maxCamionHeight);

        g.drawString(labelRojo + ": " + nivelPinturaRoja, camionXRojo, y - 5);
        g.drawString(labelBlanco + ": " + nivelPinturaBlanca, camionXBlanco, y - 5);
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

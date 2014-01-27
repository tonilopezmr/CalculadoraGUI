/*
 * Copyright 2013 Antonio López Marín
 * CalculadoraGUI 3.4
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package calculadoragui.vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

/**
 * GlassPane del JFrame de la vista, que siempre esta visible y pinta un punto
 * rojo en el boton que se pulsa.
 *
 * Tiene una clase interna que es el que escucha los eventos que recibe el
 * GlassPane.
 *
 * @see GlassListener
 * @see Vista
 * @author Antonio López Marín
 */
public class GlassPane extends JComponent {
    
    //Componente pulsado
    Component componente;
    JMenuBar menuBar;
    
    //Diferentes estados de la calculadora
    boolean isBasic = true;
    boolean isProgramador = false;
    boolean isCientifica = false;
    
    //X e Y auxiliares, que ayudan a los ejes x e y de los componentes
    public Point point;
    
    //Constantes de las x e y segun los contenedores de los componentes
    final public static int BASICy = 145;
    final public static int CIENTIFICy = 195;
    final public static int TRIGONOMETICy = 130;
    
    /**
     * Contructor de GlassPane que recibe el panel del JFrame y se lo pasa a el
     * GlassListener que es el que permanece a la escucha de eventos.
     *
     * @param contenedor
     */
    public GlassPane(Container contenedor, JMenuBar menuBar) {
        GlassListener listener = new GlassListener(contenedor, this);
        addMouseListener(listener);
        addMouseMotionListener(listener);
        this.menuBar = menuBar;
        point = new Point(0, 136);
    }
        
    /**
     * Metodo redefinido de JComponente que pinta el punto redondo en el boton
     * que se pulsa.
     *
     * @param g Graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        //Si componente no es nulo que pinte
        if (componente != null && componente instanceof JButton) {
            modoCalculadora();      //Comprueba en que modo se situa la calculadora
            if (igualPaint(g)) {
                g.setColor(Color.red);
                int x = componente.getX() + point.x;
                int y = componente.getY() + point.y;
                g.fillOval(x, y, 10, 10);
            }
        }
    }
    
    /**
     * Metodo que comprueba si el componente es el boton igual que es mas
     * grande de lo normal, y hay que decirle cuanto mas tiene que bajar
     * el punto para estar abajo a la izquierda.
     * 
     * @param g Graphics 
     * @return true si no es igual, false si es igual
     */
    private boolean igualPaint(Graphics g) {
        JButton btn = (JButton) componente;
        if (btn.getText().equals("=")) {
            g.setColor(Color.red);
            int x = componente.getX() + point.x;
            int y = componente.getY() + (62 + point.y);
            g.fillOval(x, y, 10, 10);

            return false;
        }

        return true;
    }
    
    /**
     * Modo cientifico le añade sus ejes x e y auxiliares, que ayudaran a 
     * encontrar el punto de abajo de cada boton.
     */
    private void modoCientifico() {
        Container c = componente.getParent();
        String name = c.getName();
        
        if (name != null && name.equals("trigonometricas")) {
            point.x = 0;
            point.y = TRIGONOMETICy;
        } else {
            point.x = 0;
            point.y = CIENTIFICy;
        }
    }
    
    /**
     * Modo programador le añade sus ejes x e y auxiliares, que ayudaran a 
     * encontrar el punto de abajo de cada boton.
     */
    private void modoProgramador() {
        Container c = componente.getParent();
        String name = c.getName();
        
        if (name != null){
            switch (name) {
                case "north":
                    point.x = 293;
                    point.y  = 134;
                    break;
                case "left":
                    point.x = 14;
                    point.y  = 235;
                    break;
                case "basic":
                    point.x = 280;
                    point.y  = 220;
                    break;
            }
        } 
    }
    
    /**
     * Segun los modos que tiene la calculadora los ejes X e Y de donde se 
     * situan los componentes es diferente, ya que los ejes de los componentes
     * son segun su contenedor padre, pero no el del GlassPane.
     * 
     * Por ello hay que calcular o hacer aproximaciones de donde se situaran
     * los componentes segun en el contenedor en el que esten.
     */
    private void modoCalculadora() {
        //Si el modo es basic solo hay que controlar la y
        if (isBasic) {
            point.x = 0;
            point.y = BASICy;
        //Si el modo es programador, se controla de distinta forma
        } else if (isProgramador) {
            modoProgramador();
        //Si el modo es cientifica, se controla tambien de distinta forma
        } else if (isCientifica) {
            modoCientifico();
        }
    }

    /**
     * Modifica el componente sobre el que se pulso.
     *
     * @param componente
     */
    private void setComponente(Component componente) {
        this.componente = componente;
    }
    
    /**
     * Cambia el estado a basic.
     * 
     * @param estado boolean
     */
    public void setBasic(boolean estado) {
        this.isBasic = estado;
        this.isCientifica = !estado;
        this.isProgramador = !estado;
    }
    
    /**
     * Cambia el estado a cientifica.
     * 
     * @param estado boolean
     */
    public void setCientifica(boolean estado) {
        this.isCientifica = estado;
        this.isBasic = !estado;
        this.isProgramador = !estado;
    }
    
    /**
     * Cambia el estado a programador.
     * 
     * @param estado boolean
     */
    public void setProgramador(boolean estado) {
        this.isProgramador = estado;
        this.isBasic = !estado;
        this.isCientifica = !estado;
    }

    /**
     * Captura todos los eventos que se producen en la calculadora y los reenvia
     * al componente que le pertenezca.
     *
     * Inner class de GlassPane.
     */
    private class GlassListener implements MouseInputListener {

        Container contenedor;
        GlassPane glass;

        /**
         * Contructor de GlassListener que recibe el GlassPane y el contenedor
         * sobre el que se va a escuchar y obtener los componentes sobre los que
         * se hace click sobre ellos.
         *
         * @param contenedor ContentPane
         * @param glass GlassPane
         */
        public GlassListener(Container contenedor, GlassPane glass) {
            this.contenedor = contenedor;
            this.glass = glass;
        }

        /**
         * Metodo que reenvia los eventos.
         *
         * Obtiene el punto y componente sobre el que se hizo click, para poder
         * reenviar los eventos a ese componente y pasarselos al GlassPane.
         *
         *
         * @param e MouseEvent Evento que se disparo.
         * @param repaint boolean Si hay que pintar o no segun que evento.
         */
        private void redispachEvent(MouseEvent e, boolean repaint) {
            //Obtiene el punto donde se hizo click
            Point glassPanePoint = e.getPoint();
            //Obtiene el contenedor del punto
            Point containerPoint = SwingUtilities.convertPoint(
                    glass,
                    glassPanePoint,
                    contenedor);
            //De aqui saca el componente donde se pulso                            
            Component component =
                    SwingUtilities.getDeepestComponentAt(
                    contenedor,
                    containerPoint.x,
                    containerPoint.y);

            //Si el punto y es menor de 0 y esta dentro del menu 
            if (containerPoint.y < 0 && containerPoint.y + menuBar.getHeight() >= 0) {
                Component menu = menuBar.getComponentAt(glassPanePoint.x,
                        glassPanePoint.y);

                if (menu != null) {
                    menu.dispatchEvent(new MouseEvent(menu,
                            e.getID(),
                            e.getWhen(),
                            e.getModifiers(),
                            containerPoint.x,
                            containerPoint.y,
                            e.getClickCount(),
                            e.isPopupTrigger()));
                    setVisible(false); //Desactivo el GlassPane para que funcione
                                       //la barra de menu correctamente!!! 
                }
            } else //Si es distinto de null
            if (component != null) {
                //Forward events over the check box.
                Point componentPoint = SwingUtilities.convertPoint(
                        glass,
                        glassPanePoint,
                        component);

                //Redispara el evento del componente
                component.dispatchEvent(new MouseEvent(component,
                        e.getID(),
                        e.getWhen(),
                        e.getModifiers(),
                        componentPoint.x,
                        componentPoint.y,
                        e.getClickCount(),
                        e.isPopupTrigger()));
            }

            if (repaint) {
                glass.setComponente(component);
                glass.repaint();
            }
        }

        /**
         * Cuando se mueve el cursor sobre el GlassPane.
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            redispachEvent(e, false);
        }

        /**
         * Si se mantiene el dedo pulsado en el raton/mouse.
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            redispachEvent(e, true);
        }

        /**
         * Si se hizo click sobre el GlassPane.
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            redispachEvent(e, false);
        }

        /**
         * Si el cursor entra sobre el GlassPane.
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            redispachEvent(e, false);
        }

        /**
         * Cuando el cursor sale del GlassPane.
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseExited(MouseEvent e) {
            redispachEvent(e, false);
        }

        /**
         * Cuando presionas sobre el GlassPane.
         *
         * @param e MouseEvent
         */
        @Override
        public void mousePressed(MouseEvent e) {
            redispachEvent(e, true);
        }

        /**
         * Cuando dejas de pulsar sobre el GlassPane.
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            redispachEvent(e, true);
        }
    }
}
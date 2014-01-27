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

import calculadoragui.controlador.ControladorGUI;
import calculadoragui.controlador.command.AccionNumerico;
import calculadoragui.controlador.command.CalcActionsCommand;
import calculadoragui.controlador.listeners.CalcKeyListener;
import calculadoragui.controlador.listeners.DecimaListener;
import calculadoragui.controlador.listeners.OperacionListener;
import calculadoragui.vista.formatters.myDecimalFormat;
import calculadoragui.vista.formatters.myJFormattedTextField;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

/**
 * Vista que contrulle la ventana de la calculadora y se comunica con el
 * controlador y el GlassPane, para mostrar los numeros y soluciones.
 *
 * La vista contiene un GlassPane y interactua con el.
 *
 * @see GlassPane GlassPane de la vista
 * @see ControladorGUI Controlador
 * @author Antonio López Marín
 */
public class Vista extends JFrame {

    /**
     * Enumeador de operaciones, que contienen su texto en el boton y su
     * identificador unico para cada operacion.
     *
     * Devuelve el operador en String porque pueden haber operadores con mas de
     * un caracter. Y su identificador para comparar.
     */
    public enum Operaciones {
        //Basic

        SUMA("+"),
        RESTA("-"),
        MULTIPLICACION("*"),
        DIVISION("/"),
        POTENCIA("<html>X<sup>2</sup></html>"),
        PUNTO("."),
        IGUAL("="),
        CLEAR("C"),
        //Programer
        AND("And"),
        OR("Or"),
        NOT("Not"),
        XOR("Xor"),
        HEXADECIMAL("Hex"),
        DECIMAL("Dec"),
        OCTAL("Oct"),
        BINARIO("Bin"),
        //Cientifica
        SENO("Sin"),
        COSENO("Cos"),
        TANGENTE("Tan"),
        RAIZ("<html><sup>n</sup>&#8730;a</html>"),
        RESTO("%"),
        NONE(""); //Cuando no hay operacion
        //Digito del Operador
        final String digito;

        Operaciones(String digito) {
            this.digito = digito;
        }

        public String getOp() {
            return digito;
        }
    }
    //Comunicacion con el controlador
    private ControladorGUI controlador;
    //La pantalla de la calculadora
    private myJFormattedTextField pantalla;
    //El ContentPane del JFrame 
    private Container contenedor;
    //Restricciones del GridBagLayout
    private GridBagConstraints gridBagCons;
    //JPanel's de los diferentes modos de calculadora
    private JPanel calcBasic;
    //JPanel contenedores de los componentes
    private JPanel cards, contenedorPantalla;
    //El GlassPane de la calculadora
    private GlassPane glassPane;
    //Acciones de SUMA, RESTA, MULTIPLICACION, DIVISION
    private Action sumaAct, restaAct, multiplicacionAct, divisionAct;
    //Etiqueta para mostrar calculos en la pantalla
    private JLabel calculo;
    //Botones de igual de diferentes modos
    private JButton igualBasic;
    //Cuenta las veces que se abrio el fichero README.md
    private int contREADME;
    /*
     * Vistas
     */
    private VistaProgramador calcProgramador;
    private VistaCientifico calcCientifico;
    private NumeroPrimo calcNumPrimos;
    /*
     * Listeners
     */
    private DecimaListener nListener;           //Listener numerico
    private OperacionListener oListener;        //Listener Operadores
    private CalcKeyListener keylistener;        //Listener teclado

    /*
     * Formatters
     */
    private myDecimalFormat decimalFormat;
    /*
     * Action Commands
     */
    private AccionNumerico insertNum;
    /*
     * Dimensiones de los diferentes modos de la calduladora 
     */
    private Dimension basicDimen;
    private Dimension programadorDimen;
    private Dimension cientificDimen;

    /*
     * ** Constantes ***
     */
    //Modos de la calculadora
    final private String BASIC = "Basica";
    final private String PROGRAMADOR = "Programador";
    final private String CIENTIFICO = "Cientifica";
    //Mensaje de error
    final public static String ERROR = "Err";
    //width y height de las dimensiones de las calculadoras
    //Basica
    final private int BASICWIDTH = 365;
    final private int BASICHEIGHT = 380;
    //Programador
    final private int PROGRAMWIDTH = 644;
    final private int PROGRAMHEIGHT = 454;
    //Cientifica
    final private int CIENTIFYWIDTH = 365;
    final private int CIENTIFYHEIGHT = 445;

    /**
     * Constructor que instancia la comunicacion con el controlador y monta la
     * vista e inicia los demas atributos.
     *
     */
    public Vista() {
        //Recojo el contenedor
        contenedor = getContentPane();

        //Inicializo los paneles de la ventana
        calcBasic = new JPanel();

        //Inicio las vistas
        calcProgramador = new VistaProgramador(this);
        calcCientifico = new VistaCientifico(this);
        calcNumPrimos = new NumeroPrimo();

        //Recojo el controlador
        controlador = calcProgramador.getControlador();

        //Inicio las dimensiones de las diferentes cards
        basicDimen = new Dimension(BASICWIDTH, BASICHEIGHT);
        programadorDimen = new Dimension(PROGRAMWIDTH, PROGRAMHEIGHT);
        cientificDimen = new Dimension(CIENTIFYWIDTH, CIENTIFYHEIGHT);

        //Cargo la ventana
        loadWindows();
//        construirVentana();
    }

    /**
     * Metodo estatico que inicia la vista.
     *
     */
    public static void mostrar() {
        Vista vista = new Vista();
    }

    /**
     * Cuando pulsan un operador, hay que distinguir si es un punto o lo demas
     * que se trata de distinta forma, que se le pasa al controlador.
     *
     * @see ControladorGUI#accionNumerico(java.lang.String)
     * @see ControladorGUI#accionPunto()
     * @exception ArithmeticException Errores aritmetricos
     * @exception NumberFormatException Errores de numeros demasiado grandes
     * @param digito int Id/Codigo del operador
     */
    public void accionOperador(Operaciones digito) {
        try {
            //Si es un punto se le pasa el controlador del punto
            if (Operaciones.PUNTO == digito) {
                //Llamar al metodo del punto en el controlador
                controlador.accionPunto();
            } else {
                //Llama al controlador
                controlador.accionOperando(digito);
            }
        } catch (ArithmeticException e) {
            String error = e.getMessage();
            //Para mostrar errores muy largos
            longitudTextPantalla(error);
            pantalla.setText(error);
        } catch (NumberFormatException nfe) {
            Logger.getLogger(Vista.class.getName())
                    .log(Level.SEVERE, "Error al intentar hacer una accion de "
                    + "operacion");
            setTextPantalla(ERROR);
        } catch (Exception ex) {
            Logger.getLogger(Vista.class.getName())
                    .log(Level.SEVERE, "Error al intentar hacer una accion de "
                    + "operacion");
            setTextPantalla(ERROR);
        }
    }

    /**
     * Crea la pantalla de la calculadora, con sus configuraciones.
     *
     * @see JTextField
     */
    private myJFormattedTextField crearTextField() {
        //JTextField
        myJFormattedTextField textField = new myJFormattedTextField();
        decimalFormat.setMaximumFractionDigits(6);
        decimalFormat.setMaximumIntegerDigits(10);
        textField.setFormatter(decimalFormat);

        //Etiqueta de la pantalla
        calculo = new JLabel();

        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setFont(new Font("Arial", Font.BOLD, 27));
        textField.setBackground(Color.WHITE);
        textField.setEditable(false);
        textField.setSize(new Dimension(365, 60));
        textField.setMinimumSize(new Dimension(345, 60));
        textField.setPreferredSize(new Dimension(345, 60));
        textField.setValue(0);
        textField.setText("0");  //Por defecto pone un 0
        textField.addKeyListener(keylistener);
        textField.setLayout(new FlowLayout(FlowLayout.LEFT));
        textField.add(calculo);

        return textField;
    }

    /* ----------------------------------------------------------
     * Getters y Setters pantalla, para sus diferentes formatos.
     * ----------------------------------------------------------
     */
    /**
     * Metodo que devuelve el texto que tiene la pantalla.
     *
     * @return String texto que tiene la pantalla
     */
    public String getTextPantalla() {
        return pantalla.getText();  //Cambiar
    }
    
    /**
     * Metodo que devuelve el numero que hay en la pantalla.
     *
     * @return Numero de pantalla
     * @throws ArithmeticException
     */
    public double getDecimalPantalla() throws ArithmeticException {
        try {
            return ((Number) pantalla.getValue()).doubleValue();
        } catch (Exception e) { //Controlar si es un numero o no
            Logger.getLogger(Vista.class.getName())
                    .log(Level.SEVERE, "Error al devolver el numero de pantalla");
            setTextPantalla(ERROR);
            return 0;
        }
    }

    /**
     * Metodo que devuelve la conversion que hay en pantalla.
     *
     * @return
     */
    public String getConversionPantalla() {
        Logger.getLogger(Vista.class.getName())
                .log(Level.INFO, "Recogio el valor de la conversion");
        return pantalla.getText().replaceAll(" ", "");
    }

    /**
     * Metodo que devuelve el booleano que hay en pantalla.
     *
     * @return
     * @throws ArithmeticException
     */
    public boolean getBooleanPantalla() throws ArithmeticException {
        try {
            return (Boolean) pantalla.getValue();
        } catch (Exception e) {   //Controlar si es un booleano o no
            Logger.getLogger(Vista.class.getName())
                    .log(Level.SEVERE, "Error al devolver el booleano de pantalla");
            throw new ArithmeticException(ERROR);
        }
    }

    /**
     * Metodo que muestra en la pantalla un texto.
     *
     * @see Font Fuente del texto de la pantalla
     * @param texto String el numero que hay que añadir en la pantalla
     */
    public void setTextPantalla(String texto) {
        longitudTextPantalla(texto);
        if (maxSizePantalla(texto) || texto.equals("0")) {
            pantalla.setText(texto);
        }
    }

    /**
     * Metodo que añade a la pantalla un valor numerico.
     *
     * @param value
     */
    public void setDecimalPantalla(double value) {
        try {
            if (value < 1000000000) {
                pantalla.setValue(value);
                Logger.getLogger(Vista.class.getName())
                        .log(Level.INFO, "Valor decimal puesto en pantalla");
            } else {
                setTextPantalla("Numero demasiado grande");
            }
        } catch (Exception e) {
            Logger.getLogger(Vista.class.getName())
                    .log(Level.SEVERE, "Error al poner un decimal en pantalla");
            pantalla.setValue(0);
            pantalla.setText(ERROR);
        }
    }

    /**
     * Metodo que muestra en pantalla un valor que no es decimal, como puede ser
     * binario, octal o hexadecimal.
     *
     */
    public void setConversionPantalla(String value) {
        longitudTextPantalla(value);
        pantalla.setValue(value);
        Logger.getLogger(Vista.class.getName())
                .log(Level.INFO, "Valor conversion puesto en pantalla");
    }

    /**
     * Metodo que muestra en pantalla un valor booleano.
     *
     * @param value
     */
    public void setBooleanoPantalla(boolean value) {
        pantalla.setValue(value);
        Logger.getLogger(Vista.class.getName())
                .log(Level.INFO, "Valor booleano puesto en pantalla");
    }

    /**
     * Metodo que muestra en la etiqueta de la pantalla informacion, de algunas
     * operaciones.
     *
     * Ej: coseno, tangente, etc...
     *
     * @param calculo String
     */
    public void setEtiquetaPantalla(String calculo) {
        this.calculo.setText(calculo);
    }

    /**
     * Metodo que cambia a la pantalla el formato del texto.
     *
     * @param format Format
     */
    public void setPantallaFormat(Format format) {
        pantalla.setFormatter(format);
    }

    /**
     * Metodo que activa o desactiva las acciones de las operaciones.
     *
     * @param enable boolean
     */
    public void setAccionOperaciones(boolean enable) {
        sumaAct.setEnabled(enable);
        restaAct.setEnabled(enable);
        multiplicacionAct.setEnabled(enable);
        divisionAct.setEnabled(enable);
    }

    /**
     * Metodo que devuelve si estan activadas o desactivadas todas las acciones.
     *
     * @return
     */
    public boolean isActionOperaciones() {
        return sumaAct.isEnabled()
                && restaAct.isEnabled()
                && multiplicacionAct.isEnabled()
                && divisionAct.isEnabled();
    }

    /**
     * Pregunta a la vista programador si esta activado el binario.
     *
     * @return
     */
    public boolean isBinSelected() {
        return calcProgramador.isBinSelected();
    }

    /**
     * Cambia la accion del keylistener, puede ser numerico o de conversor.
     *
     * @param accion
     */
    public void setAccionKeyListener(CalcActionsCommand accion) {
        keylistener.setAccion(accion);
    }

    /**
     * Cambia la accion del keyListener a accion numerica, que es a la que
     * pertenece en la calculadora principal.
     *
     */
    public void cambiaAccionNumerica() {
        keylistener.setAccion(insertNum);
    }

    /**
     * Devuelve el keyListener de la pantalla, calculadora.
     *
     * @return
     */
    public CalcKeyListener getKeylistener() {
        return keylistener;
    }

    /**
     * Controlo el maximo de numeros que se puede poner segun la proporcion de
     * la pantalla.
     *
     * @param texto String los numeros a escribir en la pantalla
     * @return boolean true si es menor de 40 digitos, si es mayor false
     */
    private boolean maxSizePantalla(String texto) {
        return texto.length() < (pantalla.getWidth() / 40) * 3;
    }

    /**
     * Segun lo ancho de la pantalla, la fuente varia de tamaño.
     *
     * Si el texto a mostrar es demasiado grande, se hace el tamaño del texto
     * mas pequeño.
     *
     * @param texto String el numero que hay que añadir en la pantalla
     */
    public void longitudTextPantalla(String texto) {
        //Si el texto es mayor que la longitud de la pantalla, se reduce 
        //el tamaño de la fuente, sabiendo que cada 20px es una letra de fuente 27
        if (pantalla.getFont().getSize() == 17 || pantalla.getFont().getSize() == 27) {
            if (texto.length() > pantalla.getWidth() / 20) {
                pantalla.setFont(new Font("Arial", Font.BOLD, 17));
            } else {
                pantalla.setFont(new Font("Arial", Font.BOLD, 27));
            }
        }
    }

    /**
     * Configura la ventana del JFrame y añade el GlassPane.
     *
     * @see GlassPane
     * @see JFrame
     */
    private void configuracionVentana(JMenuBar menuBar) {
        super.setTitle("Calculadora GUI");

        //Funcionalidad del botón Cerrar
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        //Dimension por defecto de la ventana
        Dimension dimen = new Dimension(365, 380);
        setSize(dimen);
        setPreferredSize(dimen);

        //Para que se situe por el medio de la pantalla
        setLocation(699, 300);

        //GlassPane del ContentPane
        glassPane = new GlassPane(contenedor, menuBar);
        //Añade el GlassPane a la ventana
        setGlassPane(glassPane);
        glassPane.setVisible(true); //Muestro el GlassPane
    }

    /**
     * Crea los botones numericos y estan a la escucha de eventos.
     *
     * @param digit String digito del boton
     * @param r Rectangle de donde se situa el boton
     * @see JComponent#setBounds(java.awt.Rectangle)
     * @see JButton#addActionListener(java.awt.event.ActionListener)
     * @deprecated ya no se utiliza el setBounds
     */
    private void crearBotonNumerico(String digit, Rectangle r) {
        JButton btn = new JButton(digit);
        btn.setBounds(r);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.addActionListener(nListener);
        contenedor.add(btn);
    }

    /**
     * Crea los botones de las operaciones y estan a la escucha de eventos.
     *
     * @param digit String digito del boton
     * @param r Rectangle de donde se situa el boton
     * @see JComponent#setBounds(java.awt.Rectangle)
     * @see JButton#addActionListener(java.awt.event.ActionListener)
     * @deprecated ya no se utiliza el setBounds
     */
    private void crearBotonOperador(String digit, Rectangle r) {
        JButton btn = new JButton(digit);
        btn.setBounds(r);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.addActionListener(oListener);
        contenedor.add(btn);
    }

    /**
     * Crea los botones de las operaciones, pero apartir de una accion que se le
     * pasa por parametros.
     *
     * @see OperacionListener Listener de las operaciones
     * @param op enum Operaciones una operacion que esta previamente definida
     * @param accion Action Accion del boton
     * @param keyEven mNemonic
     * @return JButton boton creado con los parametros de entrada
     */
    public JButton crearBotonOperador(Operaciones op, Action accion,
            int keyEvent) {
        BotonOperacion btn = new BotonOperacion(accion);
        btn.setOperacion(op);
        btn.setMnemonic(keyEvent);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setText(op.getOp());
        btn.addKeyListener(keylistener);
        return btn;
    }

    /**
     * Crea los botones de las operaciones, con todos los parametros que hagan
     * falta.
     *
     * @see OperacionListener Listener de las operaciones
     * @param op enum Operaciones una operacion que esta previamente definida
     * @param keyEven mNemonic
     * @return JButton boton creado con los parametros de entrada
     */
    public JButton crearBotonOperador(Operaciones op, int keyEvent) {
        BotonOperacion btn = new BotonOperacion(op.getOp());
        btn.setOperacion(op);
        btn.setMnemonic(keyEvent);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.addActionListener(oListener);
        btn.addKeyListener(keylistener);
        return btn;
    }

    /**
     * Crea los botones de las operaciones, con todos los parametros que hagan
     * falta.
     *
     * @see OperacionListener Listener de las operaciones
     * @param op enum Operaciones una operacion que esta previamente definida
     * @return JButton boton creado con los parametros de entrada
     */
    public JButton crearBotonOperador(Operaciones op) {
        BotonOperacion btn = new BotonOperacion(op.getOp());
        btn.setOperacion(op);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.addActionListener(oListener);
        btn.addKeyListener(keylistener);
        return btn;
    }

    /**
     * Crea los botones de operadores, a partir de una acción que se le pasa por
     * parametros.
     *
     * @see OperacionListener Listener de las operaciones
     * @param op enum Operaciones una operacion que esta previamente definida
     * @param accion Action accion del boton
     * @return JButton boton creado con los parametros de entrada
     */
    public JButton crearBotonOperador(Operaciones op, Action accion) {
        BotonOperacion btn = new BotonOperacion(accion);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setOperacion(op);
        btn.setText(op.getOp());
        btn.addActionListener(oListener);
        btn.addKeyListener(keylistener);
        return btn;
    }

    /**
     * Crea los botones numericos, a partir de una acción que se le pasa por
     * parametros.
     *
     * @see DecimaListener Listener de los numeros
     * @param text texto que contendra el boton
     * @param accion Action accion del boton
     * @return JButton boton creado con los parametros de entrada
     */
    public JButton crearBotonNumerico(String text, Action accion,
            ActionListener listener) {
        JButton btn = new JButton(accion);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setText(text);
        btn.addActionListener(listener);
        btn.addKeyListener(keylistener);
        return btn;
    }

    /**
     * Crea los botones numericos, con todos los parametros que hagan falta.
     *
     * @see DecimaListener Listener de los numeros
     * @param text texto que contendra el boton
     * @param keyEvent mNemonic
     * @return JButton boton creado con los parametros de entrada
     */
    public JButton crearBotonNumerico(String text, int keyEvent) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setMnemonic(keyEvent);
        btn.addActionListener(nListener);
        btn.addKeyListener(keylistener);
        return btn;
    }

    /**
     * Crea los botones numericos, con todos los parametros que hagan falta.
     *
     * @see DecimaListener Listener de los numeros
     * @param text texto que contendra el boton
     * @return JButton boton creado con los parametros de entrada
     */
    public JButton crearBotonNumerico(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.addActionListener(nListener);
        btn.addKeyListener(keylistener);
        return btn;
    }

    /**
     * Distribucion de la pantalla con el layout BoxLayout utilizando la clase
     * Box.
     *
     * Pone la pantalla y el panel de botones debajo.
     */
    private void distribucionBox() {
        //BoxLayout
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(10));
        box.add(contenedorPantalla);
        box.add(Box.createVerticalStrut(5));
        box.add(cards);
        box.add(Box.createVerticalStrut(10));
        contenedor.add(box);
    }

    /**
     * Crea la barra de menu de la calculadora.
     *
     * Va a tener los campos ver, edicion, configuracion y ayuda.
     *
     * @see JMenuBar
     * @see JRadioButtonMenuItem
     */
    private JMenuBar loadMenuBar() {
        //Creo la barra de menu
        JMenuBar menuBar = new JMenuBar();

        //Cargo las Acciones de Operadores
        sumaAct = new SumaAction("Sumar");
        restaAct = new RestaAction("Restar");
        multiplicacionAct = new MultiplicacionAction("Multiplicar");
        divisionAct = new DivisionAction("Dividir");

        JMenu ver = new JMenu("Ver");
        ver.setMnemonic(KeyEvent.VK_V);

        //Buttongroup
        ButtonGroup grupo = new ButtonGroup();

        //Radiobuttons de opciones de calculadora
        JRadioButtonMenuItem basic = new JRadioButtonMenuItem(BASIC, true);
        basic.setMnemonic(KeyEvent.VK_B);
        basic.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
                ActionEvent.CTRL_MASK));
        basic.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //Restablezco los valores por defecto
                controlador.limpiarCalculadora();
                //Le indico al GlassPane que ha cambiado de estado
                glassPane.setBasic(true);

                //Cambia de card y restablezco sus dimensiones
                setSize(basicDimen);
                setPreferredSize(basicDimen);
                CardLayout card = (CardLayout) cards.getLayout();
                card.show(cards, BASIC);

                //Cambio el boton por defecto al igual de la calculadora basic
                getRootPane().setDefaultButton(igualBasic);
                igualBasic.requestFocus();
            }
        });

        //Programador
        JRadioButtonMenuItem programador = new JRadioButtonMenuItem(PROGRAMADOR);
        programador.setMnemonic(KeyEvent.VK_P);
        programador.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
                ActionEvent.CTRL_MASK));
        programador.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //Restablezco los valores por defecto
                controlador.limpiarCalculadora();
                //Le indico al GlassPane que ha cambiado de estado
                glassPane.setProgramador(true);

                //Cambia de card y restablezco sus dimensiones
                setSize(programadorDimen);
                setPreferredSize(programadorDimen);
                CardLayout card = (CardLayout) cards.getLayout();
                card.show(cards, PROGRAMADOR);
                pack();

                //Recojo el boton igual de la calculadora programador
                JButton igualProgram = calcProgramador.getIgualProgram();

                //El boton por defecto al igual de la calculadora programador
                getRootPane().setDefaultButton(igualProgram);
                igualProgram.requestFocus();

                //Si se deselecciona, y se cambia de card, pone por defecto
                //de nuevo el decimal
                if (ItemEvent.DESELECTED == e.getStateChange()) {
                    calcProgramador.salir();
                }
            }
        });

        //Cientifico
        JRadioButtonMenuItem cientifico = new JRadioButtonMenuItem(CIENTIFICO);
        cientifico.setMnemonic(KeyEvent.VK_E);
        cientifico.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
                ActionEvent.CTRL_MASK));
        cientifico.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //Restablezco los valores por defecto
                controlador.limpiarCalculadora();
                //Le indico al GlassPane que ha cambiado de estado
                glassPane.setCientifica(true);

                //Cambio de card y establezco sus dimensiones
                setSize(cientificDimen);
                setPreferredSize(cientificDimen);
                CardLayout card = (CardLayout) cards.getLayout();
                card.show(cards, CIENTIFICO);

                JButton igualCientific = calcCientifico.getIgualCientific();

                //El boton por defecto al igual de la calculadora cientifica
                getRootPane().setDefaultButton(igualCientific);
                igualCientific.requestFocus();
            }
        });

        //Añado al grupo los radiobuttons
        grupo.add(basic);
        grupo.add(programador);
        grupo.add(cientifico);

        //Añado los items al menu ver
        ver.add(basic);
        ver.add(programador);
        ver.add(cientifico);

        //Menu editar
        JMenu editar = new JMenu("Editar");
        editar.setMnemonic(KeyEvent.VK_E);

        //Copiar
        JMenuItem copy = new JMenuItem("Copiar");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                ActionEvent.CTRL_MASK));
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Recoje todo lo que hay en pantalla
                String selection = pantalla.getText();
                //Con lo que habia creo una seleccion
                StringSelection data = new StringSelection(selection);
                //Recojo el clipboard
                Clipboard clipboard =
                        Toolkit.getDefaultToolkit().getSystemClipboard();
                //Y le añado lo que habia en pantalla
                clipboard.setContents(data, data);
            }
        });

        //Pegar
        JMenuItem paste = new JMenuItem("Pegar");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                ActionEvent.CTRL_MASK));
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = getToolkit().getSystemClipboard();
                Transferable t = clipboard.getContents(this);
                //Si soporta el DataFlavor..
                if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        String portaPapeles = (String) t.getTransferData(DataFlavor.stringFlavor);
                        if (!portaPapeles.equals("")) {
                            if (calcProgramador.isDecimalSelected()) {
                                pegarDecimal(portaPapeles);
                            } else {
                                pegarConversion(portaPapeles);
                            }
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    } catch (UnsupportedFlavorException | IOException ex) {
                        Logger.getLogger(Vista.class.getName())
                                .log(Level.WARNING, null, ex);
                    }
                }
            }
        });

        //Añado los botones de copiar y pegar
        editar.add(copy);
        editar.add(paste);

        //Menu operaciones
        JMenu operaciones = new JMenu("Operaciones");
        operaciones.setMnemonic(KeyEvent.VK_O);

        //Items de operaciones
        JMenuItem suma = new JMenuItem(sumaAct);
        suma.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.CTRL_MASK));
        JMenuItem resta = new JMenuItem(restaAct);
        resta.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                ActionEvent.CTRL_MASK));
        JMenuItem multiplicacion = new JMenuItem(multiplicacionAct);
        multiplicacion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
                ActionEvent.CTRL_MASK));
        JMenuItem division = new JMenuItem(divisionAct);
        division.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                ActionEvent.CTRL_MASK));
        JMenuItem calcNumPrimosItem = new JMenuItem("Numeros primos");
        calcNumPrimosItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                ActionEvent.CTRL_MASK));
        calcNumPrimosItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcNumPrimos.setVisible(true);
            }
        });

        //Se añaden los items de operaciones al menu
        operaciones.add(suma);
        operaciones.add(resta);
        operaciones.add(multiplicacion);
        operaciones.add(division);
        operaciones.addSeparator();
        operaciones.add(calcNumPrimosItem);

        //Menu configuracion
        JMenu configuracion = new JMenu("Configuración");
        configuracion.setMnemonic(KeyEvent.VK_C);

        //Submenu de configuracion
        JMenu confPantalla = new JMenu("Pantalla");
        confPantalla.setMnemonic(KeyEvent.VK_P);

        //Items de pantalla

        //Creo un panel para poder cambiar la fuente con un Spinner
        final JPanel panelFuente = new JPanel(new BorderLayout());

        //Creo el modelo del Jspinner
        SpinnerNumberModel modelSpinner = new SpinnerNumberModel(27, 11, 27, 1);
        final JSpinner spinner = new JSpinner(modelSpinner);

        //Añado los componentes al panel
        panelFuente.add(new JLabel("Fuente de pantalla:"), BorderLayout.NORTH);
        panelFuente.add(spinner, BorderLayout.CENTER);
        panelFuente.add(new JLabel("                   "), BorderLayout.EAST);

        //Cambiar la fuente al texto
        JMenuItem fuente = new JMenuItem("Fuente");
        fuente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int texto = JOptionPane.showConfirmDialog(contenedor,
                        panelFuente,
                        "Fuente del texto de pantalla",
                        JOptionPane.OK_CANCEL_OPTION);
                if (texto == JOptionPane.OK_OPTION) {
                    pantalla.setFont(new Font("Arial", Font.BOLD, (int) spinner.getValue()));
                }
            }
        });

        JMenuItem color = new JMenuItem("Color");
        color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(contenedor,
                        "Cambiar color de pantalla", Color.WHITE);
                if (color != null) {
                    pantalla.setBackground(color);
                }
            }
        });

        confPantalla.add(fuente);
        confPantalla.add(color);

        configuracion.add(confPantalla);

        //menu ayuda
        JMenu ayuda = new JMenu("Ayuda");
        ayuda.setMnemonic(KeyEvent.VK_A);

        //Ayuda sobre los numeros primos
        JMenuItem infoPrimos = new JMenuItem("Numeros primos");
        infoPrimos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(contenedor,
                        "<html>Se dice que un número primo es un número natural "
                        + "mayor que 1 <br>que tiene únicamente dos divisores "
                        + "distintos: él mismo y el 1.<br><br> "
                        + "En la CalculadoraGUI se calcula el numero primo que "
                        + "ocupa <br>una posicion determinada, es decir, si queremos "
                        + "saber que<br> numero primo es el noveno, o el vigésimo "
                        + "de la lista <br>de los numeros primos.<br><br>"
                        + "Ejemplo: El numero primo que esta en la novena "
                        + "posicion <br>es el numero 23.</html>",
                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //Mostrar documentacion
        JMenuItem javadoc = new JMenuItem("Documentacion");
        javadoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J,
                ActionEvent.CTRL_MASK));
        javadoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //Abre con el navegador predeterminado, la documentacion javadoc
                    File file = new File("javadoc/index.html");
                    if (file.exists()) {
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "
                                + "file:///" + file.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(contenedor,
                                "No se encuentra la carpeta javadoc.",
                                "¡Error!", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(contenedor,
                            "Hubo un error al intentar ejecutar el navegador.",
                            "¡Error!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        //Fichero donde esta toda la informacion
        final File file = new File("README.md");

        //Mostrar acerca de
        //Creo el Jlabel que va a mostrarse
        final JLabel info = new JLabel("<html>Calculadora GUI 3.4<br><br>"
                + "Programador: Antonio López Marín<br>"
                + "Diseñador: Antonio Rodrigo Gea López<br><br>&nbsp&nbsp&nbsp&nbsp"
                + "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href=\"#\" align=\"center\">"
                + "Más informacion</a></html>");

        //Cuando al jlabel por la parte de abajo donde pone "mas informacion"
        info.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (e.getY() > 75 && contREADME < 3) {
                        if (file.exists() && Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(file);
                            contREADME++;
                        } else {
                            JOptionPane.showMessageDialog(contenedor,
                                    "No se encuentra el fichero README.md",
                                    "¡Error!", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Vista.class.getName()).log(Level.WARNING, null, ex);
                }
            }
        });

        //Acerca de, le añado la etiqueta personalizada
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(contenedor,
                        info,
                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //Añado el item de ayuda al menu
        ayuda.add(infoPrimos);
        ayuda.add(javadoc);
        ayuda.addSeparator();
        ayuda.add(about);

        //Añado los menus a la barra de menu
        menuBar.add(ver);
        menuBar.add(editar);
        menuBar.add(operaciones);
        menuBar.add(configuracion);
        menuBar.add(ayuda);
        menuBar.setEnabled(true);

        return menuBar;
    }
    
    /**
     * Pega un numero decimal, sino es un numero da error y informa con un
     * pitidito de que no es correcto.
     * 
     * @param decimal 
     */
    private void pegarDecimal(String decimal) {
        try {
            String test = decimal.replaceAll("\\.", "");
            double num = Double.parseDouble(test);
            pantalla.setValue(num);
            //False porque no es nueva operacion
            controlador.setNewOperacion(false);
        } catch (NumberFormatException ex) {
            //Que suene cuando no se le permita pegar
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    /**
     * Pega un numero para una conversion, puede ser una palabra entera, 
     * por ahorrar tiempo no hago la comprobacion de si es un numero correcto
     * y dejo que con el transcurso de vida del programa decida si es un 
     * numero valido o no saltando un error.
     * 
     * Se que no tengo que dar incapie a que el usuario pueda fallar, pero 
     * como no es algo de vital importancia, ya lo mejorare mas adelante.
     * 
     * @param convert 
     */
    private void pegarConversion(String convert) {
        try {
            String test = convert.replaceAll("\\.", "");
            double num = Double.parseDouble(test);
            setConversionPantalla(test);
            //False porque no es nueva operacion
            controlador.setNewOperacion(false);
        } catch (NumberFormatException ex) {
            setConversionPantalla(convert);
            //False porque no es nueva operacion
            controlador.setNewOperacion(false);
        }
    }

    /**
     * Metodo que crea una calculadora basica en un panel, con los botones
     * basicos de los numeros y operaciones Aritmetricas.
     *
     * @return JPanel panel con la calculadora basica.
     */
    private JPanel loadCalcBasic() {
        JPanel calculadoraBasica = new JPanel();

        //Le pongo layout a calcBasic
        calculadoraBasica.setLayout(new GridBagLayout());
        //Inserto la dimension que tiene que tener la botonera de la calculadora
        Dimension dimen = new Dimension(359, 254);
        calculadoraBasica.setSize(dimen);
        calculadoraBasica.setPreferredSize(dimen);

        //Espacios entre componentes
        gridBagCons.insets = new Insets(3, 4, 3, 4);

        //Rellenar componentes
        gridBagCons.fill = GridBagConstraints.BOTH;
        gridBagCons.weightx = 1; //Se ajuste a la ventana
        gridBagCons.weighty = 1; //Se ajuste a la ventana

        //Row1 añade los botones en la priemra fila
        addGridBag(crearBotonNumerico("7"), calculadoraBasica, 0, 0);
        addGridBag(crearBotonNumerico("8"), calculadoraBasica, 1, 0);
        addGridBag(crearBotonNumerico("9"), calculadoraBasica, 2, 0);
        addGridBag(crearBotonOperador(Operaciones.DIVISION, divisionAct,
                KeyEvent.VK_DIVIDE),
                calculadoraBasica, 3, 0);
        addGridBag(crearBotonOperador(Operaciones.CLEAR),
                calculadoraBasica, 4, 0);

        //Row2
        addGridBag(crearBotonNumerico("4"), calculadoraBasica, 0, 1);
        addGridBag(crearBotonNumerico("5"), calculadoraBasica, 1, 1);
        addGridBag(crearBotonNumerico("6"), calculadoraBasica, 2, 1);
        addGridBag(crearBotonOperador(Operaciones.MULTIPLICACION,
                multiplicacionAct, KeyEvent.VK_MULTIPLY), calculadoraBasica, 3, 1);
        addGridBag(crearBotonOperador(Operaciones.POTENCIA,
                KeyEvent.VK_CIRCUMFLEX), calculadoraBasica, 4, 1);

        //Row3
        addGridBag(crearBotonNumerico("1"), calculadoraBasica, 0, 2);
        addGridBag(crearBotonNumerico("2"), calculadoraBasica, 1, 2);
        addGridBag(crearBotonNumerico("3"), calculadoraBasica, 2, 2);
        addGridBag(crearBotonOperador(Operaciones.RESTA, restaAct),
                calculadoraBasica, 3, 2);
        gridBagCons.gridheight = 2;
        igualBasic = crearBotonOperador(Operaciones.IGUAL, KeyEvent.VK_ENTER);
        addGridBag(igualBasic, calculadoraBasica, 4, 2);
        gridBagCons.gridheight = 1;

        //Row4
        addGridBag(crearBotonOperador(Operaciones.PUNTO),
                calculadoraBasica, 0, 3);
        gridBagCons.gridwidth = 2;
        addGridBag(crearBotonNumerico("0"), calculadoraBasica, 1, 3);
        gridBagCons.gridwidth = 1;
        addGridBag(crearBotonOperador(Operaciones.SUMA, sumaAct, KeyEvent.VK_PLUS),
                calculadoraBasica, 3, 3);

        return calculadoraBasica;
    }

    /**
     * Metodo que llama el constructor que monta/construye la calculadora, en
     * una vantana.
     *
     * Crea la estructura de como va a ser la ventana, utilizando metodos de la
     * vista.
     *
     * @see GlassPane
     * @see JFrame
     */
    private void loadWindows() {
        //Primero ponemos la barra de menu
        JMenuBar menuBar = loadMenuBar();
        setJMenuBar(menuBar);

        //Pongo las configuraciones de la ventana.
        configuracionVentana(menuBar);

        /*
         * Action Commands
         */
        insertNum = new AccionNumerico(controlador);

        /*
         * Listeners 
         */

        //Listener Decimal
        nListener = new DecimaListener(controlador);
        //Listener Operadores
        oListener = new OperacionListener(this);
        //Keylistener para las pulsaciones de teclado, por defecto se pulsan numeros
        keylistener = new CalcKeyListener(insertNum, calcProgramador.getControlador(),
                calcProgramador);

        /*
         * Formatters
         */

        //DecimalFormat
        decimalFormat = new myDecimalFormat();

        //Crea la pantalla y la añado a un panel 
        pantalla = crearTextField();

        //Inicio las restricciones de GridBagLayout
        gridBagCons = new GridBagConstraints();

        //Añado la pantalla a un contenedor con gridbaglayout para que se
        //expanda a toda la pantalla
        contenedorPantalla = new JPanel(new GridBagLayout());
        gridBagCons.fill = GridBagConstraints.BOTH;
        gridBagCons.insets = new Insets(5, 5, 5, 5);

        gridBagCons.weightx = 1;
        contenedorPantalla.add(pantalla, gridBagCons);

        //Creo y cargo los modos de calculadora
        calcBasic = loadCalcBasic();
        getRootPane().setDefaultButton(igualBasic);

        //Le paso por parametros una nueva calculadora basica
        JPanel panelProgramador = calcProgramador.loadCalcProgramador(loadCalcBasic());
        //Le paso por parametros una nueva calculadora basica
        JPanel panelCientifica = calcCientifico.loadCalcCientifica(loadCalcBasic());

        //Cargo la etiqueta de cardLayout
        cards = new JPanel(new CardLayout());
        cards.add(calcBasic, BASIC);
        cards.add(panelProgramador, PROGRAMADOR);
        cards.add(panelCientifica, CIENTIFICO);

        //Metodo que añade el panel y la pantalla en una caja con BoxLayout
        distribucionBox();

        //Añado el listener para confirmar la salida del programa
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int exit = JOptionPane.showConfirmDialog(contenedor,
                        "¿Seguro que quieres salir?", "¿Seguro?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (exit == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        //Al contenedor lo suscribo al moseListener para cuando el cursor
        //entre dentro de el, activa el Glass Pane ya que cada vez que el 
        //cursor este en la barra de menu se desactivara el Glass Pane, y 
        //la forma de activarla es esta.
        contenedor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                glassPane.setVisible(true);
            }
        });

        //Muestro la vista, importante siempre al final.
        setVisible(true);
        pack();
    }

    /**
     * Añade un componente al panel que se le pasa por parametros, que
     * implemente un GridBagLayout, segun sus restricciones.
     *
     * @param com Component componente
     * @param panel Panel al que se le añade el componente
     * @param x int posicion x del boton en el gridBag
     * @param y int posicion y del boton en el gridBag
     */
    private void addGridBag(Component com, JPanel panel, int x, int y) {
        gridBagCons.gridx = x;
        gridBagCons.gridy = y;
        panel.add(com, gridBagCons);
    }

    /**
     * Metodo que llama el constructor que monta la calculadora, en una ventana.
     *
     * Configura la ventana, crea los botones y añade el GlassPane.
     *
     * @see GlassPane
     * @see JFrame
     * @see Vista#construir()
     * @deprecated Utilizo un Layout para distribuir la vista
     */
    private void construirVentana() {
        super.setTitle("Calculadora GUI");

        //Funcionalidad del botón Cerrar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(365, 380);
        setLocation(699, 300);  //Para que se situe por el medio de la pantalla
        contenedor.setLayout(null);

        //Pantalla 
        crearTextField();
        pantalla.setBounds(20, 20, 320, 50);
        contenedor.add(pantalla);

        //Listener Numericos
        nListener = new DecimaListener(controlador);

        //Botones Numericos
        crearBotonNumerico("7", new Rectangle(20, 80, 60, 60));
        crearBotonNumerico("8", new Rectangle(85, 80, 60, 60));
        crearBotonNumerico("9", new Rectangle(150, 80, 60, 60));
        crearBotonNumerico("4", new Rectangle(20, 145, 60, 60));
        crearBotonNumerico("5", new Rectangle(85, 145, 60, 60));
        crearBotonNumerico("6", new Rectangle(150, 145, 60, 60));
        crearBotonNumerico("1", new Rectangle(20, 210, 60, 60));
        crearBotonNumerico("2", new Rectangle(85, 210, 60, 60));
        crearBotonNumerico("3", new Rectangle(150, 210, 60, 60));
        crearBotonNumerico("0", new Rectangle(85, 275, 125, 60));

        //Listener Operadores
        oListener = new OperacionListener(this);

        //Botones Operadores
        crearBotonOperador("/", new Rectangle(215, 80, 60, 60));
        crearBotonOperador("C", new Rectangle(280, 80, 60, 60));
        crearBotonOperador("*", new Rectangle(215, 145, 60, 60));

        //x2
        crearBotonOperador("<html>X<sup>2</sup></html>",
                new Rectangle(280, 145, 60, 60));
        crearBotonOperador("+", new Rectangle(215, 275, 60, 60));
        crearBotonOperador("-", new Rectangle(215, 210, 60, 60));
        crearBotonOperador("=", new Rectangle(280, 210, 60, 125));
        crearBotonOperador(".", new Rectangle(20, 275, 60, 60));

        setVisible(true); //Muestro la vista
    }

    //////////////////////////
    ///////// Actions
    //////////////////////////
    /**
     * Acciones de SUMA, RESTA, MULTIPLICACION Y DIVISION.
     *
     * Hacen las acciones de dichas Operaciones.
     */
    class SumaAction extends AbstractAction {

        public SumaAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            accionOperador(Operaciones.SUMA);
        }
    }

    class RestaAction extends AbstractAction {

        public RestaAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            accionOperador(Operaciones.RESTA);
        }
    }

    class MultiplicacionAction extends AbstractAction {

        public MultiplicacionAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            accionOperador(Operaciones.MULTIPLICACION);
        }
    }

    class DivisionAction extends AbstractAction {

        public DivisionAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            accionOperador(Operaciones.DIVISION);
        }
    }
}
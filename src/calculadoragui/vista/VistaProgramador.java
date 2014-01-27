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

import calculadoragui.controlador.ControladorProgramador;
import calculadoragui.controlador.command.AccionConversor;
import calculadoragui.controlador.listeners.ControlFormatterListener;
import calculadoragui.controlador.listeners.ConversorListener;
import calculadoragui.controlador.listeners.DecimaListener;
import calculadoragui.controlador.listeners.TablasVerdadListener;
import static calculadoragui.vista.Vista.ERROR;
import calculadoragui.vista.formatters.BooleanFormat;
import calculadoragui.vista.formatters.NoDecimalFormat;
import calculadoragui.vista.formatters.myDecimalFormat;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;

/**
 * Vista en modo Programador, en esta clase se crea el panel de modo Programador
 * y se controla todos los elementos de la vista que son necesarios.
 * 
 * Para la visualizacion de la pantalla y los diferentes formatos.
 * 
 * @author Antonio López Marín
 */
public class VistaProgramador {
    //Panel calcProgramador
    private JPanel calcProgramador;
    //Controlador Programador
    private ControladorProgramador controlador;
    //La vista principal
    private Vista vista;
    //Boton de igual
    private JButton igualProgram;
    //JRadioButtons del conversor
    private JRadioButton hexadecimal, decimal, octal, binario;
    //Acciones de los botones de hexadecimal y las tablas de verdad
    private Action hexAct, tbVerdadAct;
    //Action Command
    private AccionConversor insertConversor;
    
    /*
     * Listeners
     */
    private ConversorListener convertListener;          //Listener conversiones
    private DecimaListener nListener;                   //Listener decimal
    private ControlFormatterListener formatListener;    //Listener de formato
    
    /*
     * Formatters
     */
    private BooleanFormat boolFormat;
    private NoDecimalFormat noDecimalFormat;
    private myDecimalFormat decimalFormat;

    /**
     * Constructor que recibe el controlador, la vista.
     *
     * @param vista
     */
    public VistaProgramador(Vista vista) {
        this.controlador = new ControladorProgramador(vista);
        this.vista = vista;

        //Listener para conversiones
        convertListener = new ConversorListener(controlador);
        nListener = new DecimaListener(controlador);
        formatListener = new ControlFormatterListener(this);

        //BooleanFormat
        boolFormat = new BooleanFormat();
        //NoDecimalFormat
        noDecimalFormat = new NoDecimalFormat();
        //DecimalFormat
        decimalFormat = new myDecimalFormat();

        //Action Command
        insertConversor = new AccionConversor(controlador);
    }

    /**
     * Devuelve el controlador.
     *
     * @return ControladorProgramador
     */
    public ControladorProgramador getControlador() {
        return controlador;
    }

    /**
     * Metodo que carga el modo de la calculadora Programador, con los botones
     * de hexadecimal, conversor entre hex, dec, oct, bin y operaciones de las
     * Tablas de verdad.
     *
     * @param botoneraBasica JPanel que van a ser sus botones basicos.
     *
     */
    public JPanel loadCalcProgramador(JPanel botoneraBasica) {
        //Inicio el panel de calcProgramador
        calcProgramador = new JPanel(new BorderLayout());

        //Le añado el layout a calcProgramador
        calcProgramador.setLayout(new BorderLayout(15, 15));

        //Creo una fuente para los botones de las tablas de verdad.
        Font fuente = new Font("Arial", Font.BOLD, 20);

        //Panel contenedor de botones de hexadecimal y conversor
        JPanel north = new JPanel(new FlowLayout());

        /**
         * ** Botones de hexadecimal, NORT ***
         */
        hexAct = new HexadecimalAction();
        hexAct.setEnabled(false); //Por defecto la accion esta desactivada

        JPanel letrasHex = new JPanel(new FlowLayout());
        letrasHex.setName("north");

        letrasHex.add(vista.crearBotonNumerico("A", hexAct, convertListener));
        letrasHex.add(vista.crearBotonNumerico("B", hexAct, convertListener));
        letrasHex.add(vista.crearBotonNumerico("C", hexAct, convertListener));
        letrasHex.add(vista.crearBotonNumerico("D", hexAct, convertListener));
        letrasHex.add(vista.crearBotonNumerico("E", hexAct, convertListener));
        letrasHex.add(vista.crearBotonNumerico("F", hexAct, convertListener));

        /**
         * ** Tablas de verdad, Center ***
         */
        tbVerdadAct = new TablasVerdadAction(); //Instancio la accion

        JPanel tablasVerdad = new JPanel(new GridLayout(3, 2, 6, 6));
        tablasVerdad.setName("left");
        
        //Instancio el listener de las tablas de verdad
        TablasVerdadListener tvListener = new TablasVerdadListener(controlador,
                vista, boolFormat);
        
        //True
        JButton btnTrue = new JButton(tbVerdadAct);
        btnTrue.setFont(fuente);
        btnTrue.setText("True");
        btnTrue.addActionListener(tvListener);
        btnTrue.addKeyListener(vista.getKeylistener());

        //False
        JButton btnFalse = new JButton(tbVerdadAct);
        btnFalse.setFont(fuente);
        btnFalse.setText("False");
        btnFalse.addActionListener(tvListener);
        btnFalse.addKeyListener(vista.getKeylistener());

        //Tablas de verdad
        JButton and = vista.crearBotonOperador(Vista.Operaciones.AND, tbVerdadAct);
        and.setFont(fuente);
        //OR
        JButton or = vista.crearBotonOperador(Vista.Operaciones.OR, tbVerdadAct);
        or.setFont(fuente);
        //XOR
        JButton xor = vista.crearBotonOperador(Vista.Operaciones.XOR, tbVerdadAct);
        xor.setFont(fuente);
        //NOT
        JButton not = vista.crearBotonOperador(Vista.Operaciones.NOT, tbVerdadAct);
        not.setFont(fuente);

        //Añado los botones en el panel de las tablas de verdad
        tablasVerdad.add(btnTrue);
        tablasVerdad.add(btnFalse);
        tablasVerdad.add(and);
        tablasVerdad.add(or);
        tablasVerdad.add(xor);
        tablasVerdad.add(not);

        /**
         * ** Conversor WEST + BOX ***
         */
        JPanel radioConvert = new JPanel(new FlowLayout());
        radioConvert.setName("radioButtons");
        radioConvert.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
                
        //Grupo de los radio button
        ButtonGroup grupo = new ButtonGroup();

        //HEX
        hexadecimal = new JRadioButton(Vista.Operaciones.HEXADECIMAL.getOp());
        hexadecimal.setFont(fuente);
        hexadecimal.setToolTipText("Hexadecimal");
        hexadecimal.addKeyListener(vista.getKeylistener());
        hexadecimal.setMnemonic(KeyEvent.VK_F1);
        hexadecimal.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    hexFocus(false);
                    controlador.setOrigen(Vista.Operaciones.HEXADECIMAL);
                } else if (e.getStateChange() == ItemEvent.SELECTED) {
                    //Cambia la accion del keylistener a conversor
                    vista.setAccionKeyListener(insertConversor);
                    hexFocus(true);
                    controlador.setDestino(Vista.Operaciones.HEXADECIMAL);
                    calcularConversion();
                }
            }
        });

        //DEC
        decimal = new JRadioButton(Vista.Operaciones.DECIMAL.getOp(), true);
        decimal.setFont(fuente);
        decimal.setToolTipText("Decimal");
        decimal.addKeyListener(vista.getKeylistener());
        decimal.setMnemonic(KeyEvent.VK_F2);
        decimal.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    decimalFocus(false);
                    controlador.setOrigen(Vista.Operaciones.DECIMAL);
                } else if (e.getStateChange() == ItemEvent.SELECTED) {
                    //Cambia la accion del keylistener a numerica
                    vista.cambiaAccionNumerica();
                    //Cambia de estado
                    decimalFocus(true);
                    //Cambio el destino
                    controlador.setDestino(Vista.Operaciones.DECIMAL);
                    //Calcula la conversion
                    calcularConversion();
                    //Cambia el formato de la pantalla a decimal
                    try {
                        vista.setPantallaFormat(decimalFormat);
                    } catch (IllegalArgumentException iae) {
                    }
                }
            }
        });

        //OCT
        octal = new JRadioButton(Vista.Operaciones.OCTAL.getOp());
        octal.setFont(fuente);
        octal.setToolTipText("Octal");
        octal.addKeyListener(vista.getKeylistener());
        octal.setMnemonic(KeyEvent.VK_F3);
        octal.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    octalFocus(false);
                    controlador.setOrigen(Vista.Operaciones.OCTAL);
                } else if (e.getStateChange() == ItemEvent.SELECTED) {
                    vista.setAccionKeyListener(insertConversor);
                    octalFocus(true);
                    controlador.setDestino(Vista.Operaciones.OCTAL);
                    calcularConversion();
                }
            }
        });

        //BIN
        binario = new JRadioButton(Vista.Operaciones.BINARIO.getOp());
        binario.setFont(fuente);
        binario.setToolTipText("Binario");
        binario.addKeyListener(vista.getKeylistener());
        binario.setMnemonic(KeyEvent.VK_F4);
        binario.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    binFocus(false);
                    controlador.setOrigen(Vista.Operaciones.BINARIO);
                } else if (e.getStateChange() == ItemEvent.SELECTED) {
                    vista.setAccionKeyListener(insertConversor);
                    binFocus(true);
                    controlador.setDestino(Vista.Operaciones.BINARIO);
                    calcularConversion();
                }
            }
        });

        //Añado los radio buttons al grupo
        grupo.add(hexadecimal);
        grupo.add(decimal);
        grupo.add(octal);
        grupo.add(binario);

        //Los añado al panel
        radioConvert.add(hexadecimal);
        radioConvert.add(decimal);
        radioConvert.add(octal);
        radioConvert.add(binario);

        //Los añado al panel de la parte de arriba
        north.add(radioConvert);
        north.add(letrasHex);


        //Añado la botonera basica
        botoneraBasica.setName("basic"); //Le pongo el nombre de basic
        calcProgramador.add(botoneraBasica, BorderLayout.EAST);
        calcBotoneraBasica(nListener); //Pongo la calculadora basica de programador

        //Añado los demas controles propios de programador ***
        calcProgramador.add(north, BorderLayout.NORTH);
        calcProgramador.add(tablasVerdad, BorderLayout.CENTER);

        //Añado un espacio a la izquierda (west)
        calcProgramador.add(new JLabel(), BorderLayout.WEST);

        igualProgram = (JButton) botoneraBasica.getComponent(14);

        return calcProgramador;
    }

    /**
     * Devuelve el boton igual de la vista de programador.
     *
     * @return JButton
     */
    public JButton getIgualProgram() {
        return igualProgram;
    }

    /**
     * Cuando sale de la calculadora de programador se pone por default el
     * radiobutton de decimal.
     *
     */
    public void salir() {
        decimal.setSelected(true);
    }
     
    /**
     * Devuelve si esta en modo Hexadecimal, en este momento.
     *
     * @return
     */
    public boolean isHexActive() {
        return hexAct.isEnabled();
    }
    
    /**
     * Devuelve si esta seleccionado el conversor de Hexadecimal.
     * 
     * @return 
     */
    public boolean isHexSelected(){
        return hexadecimal.isSelected();
    }
    
    /**
     * Devuelve si esta seleccionado el conversor de Decimal.
     *
     * @return
     */
    public boolean isDecimalSelected(){
        return decimal.isSelected();
    }
    
    /**
     * Devuelve si esta seleccionado el conversor de octal.
     *
     * @return
     */
    public boolean isOctalSelected() {
        return octal.isSelected();
    }

    /**
     * Devuelve si esta seleccionado el conversor de binario.
     *
     * @return
     */
    public boolean isBinSelected() {
        return binario.isSelected();
    }

    /**
     * Metodo que consulta de la vista principal si las acciones de las
     * operaciones estan activas.
     *
     * @return
     */
    public boolean isOperacionesActivas() {
        return vista.isActionOperaciones();
    }
    
    /**
     * Cambia el formato de pantalla, segun con las operaciones que se este
     * manejando.
     * 
     */
    public void cambiarFormatoProgramador() {
        //Si el decimal esta seleccionado, esta en formato decimal
        if (decimal.isSelected()) {
            vista.setPantallaFormat(decimalFormat);
        } else {      //Sino esta en formato no decimal
            vista.setPantallaFormat(noDecimalFormat);
        }
    }

    /**
     * Metodo que llama al controlador para ejecutar el calculo de una
     * conversion.
     */
    private void calcularConversion() {
        try {
            controlador.calcularConversion(vista.getConversionPantalla());
        } catch (ArithmeticException | NumberFormatException err) {
            Logger.getLogger(Vista.class.getName())
                    .log(Level.INFO, "Error al intentar calcular una conversion");
            vista.setConversionPantalla("0");
            vista.setTextPantalla(ERROR);
        } catch (Exception ex) {
            Logger.getLogger(Vista.class.getName())
                    .log(Level.INFO, "Error al intentar calcular una conversion, "
                    + "no de AritmeticException ni NumberFormatException");
            vista.setConversionPantalla("0");
            vista.setTextPantalla(ERROR);
        }
    }
    
    /**
     * Solo desactivo el del punto y potencia ya que los demas los desactivocon
     * las acciones.
     *
     * Estos botones en la calculadora Programador siempre estan desactivados.
     *
     * @param boton
     */
    public void disabledPuntoPotencia(JButton boton) {
        if (boton.getText().equals(Vista.Operaciones.PUNTO.getOp())
                || boton.getText().equals(Vista.Operaciones.POTENCIA.getOp())) {
            boton.setEnabled(false);
        }
    }

    /**
     * Pongo la botonera basica como deberia de estar en modo programador, y
     * decimal por defecto.
     *
     * Se le pasa por parametros el ActionListener que se quiere añadir a los
     * botones numericos.
     *
     * @param add ActionListener que se quiere añadir
     */
    private void calcBotoneraBasica(ActionListener add) {
        JPanel btns = (JPanel) calcProgramador.getComponent(0);
        Component[] botones = btns.getComponents();

        for (int i = 0; i < botones.length; i++) {
            JButton boton = (JButton) botones[i];
            //Si es una operacion y es el punto o la potencia, la desactiva
            if (boton instanceof BotonOperacion) {
                disabledPuntoPotencia(boton);
            } else {
                boton.setEnabled(true);
                //Borro todos los actionlistener que tenga
                for (ActionListener act : boton.getActionListeners()) {
                    boton.removeActionListener(act);
                }
                //Importante poner este listener primero en la lista
                //Siempre tiene el listener de formato               
                boton.addActionListener(formatListener);
                //Le pongo el listener decimal normal
                boton.addActionListener(add);
            }
        }
    }

    /**
     * Activa los botones que tiene los hexadecimales.
     *
     * @param enable
     */
    private void hexFocus(boolean enable) {
        vista.setPantallaFormat(noDecimalFormat);
        Logger.getLogger(Vista.class.getName())
                .log(Level.INFO, "Cambio al foco hexadecimal con su formato.");

        //Activo y desactivo los controles necesarios
        hexAct.setEnabled(enable);
        vista.setAccionOperaciones(!enable);

        //Pongo la calculadora basica de programador
        calcBotoneraBasica(convertListener);
    }

    /**
     * Quita el punto flotante y la potencia, ya que en la calculadora
     * cientifica no debe estar activado ni el punto ni la potencia.
     *
     * Restablece todos los demas.
     *
     * @param enable
     */
    private void decimalFocus(boolean enable) {
        Logger.getLogger(Vista.class.getName())
                .log(Level.INFO, "Cambio al foco decimal con su formato.");

        calcBotoneraBasica(nListener);
    }

    /**
     * Controla cuando el boton de octal tiene el foco, desactiva los botones
     * que no deben aparecer.
     *
     * Botones que no hacen falta en octal: 8, 9.
     *
     * De esta forma no confundimos al usuario, así sabe que en octal esos
     * numeros no existen.
     *
     * @param enable boolean determina si hay que activarlos o desactivarlos
     */
    private void octalFocus(boolean enable) {
        vista.setPantallaFormat(noDecimalFormat);
        Logger.getLogger(Vista.class.getName())
                .log(Level.INFO, "Cambio al foco octal con su formato.");

        vista.setAccionOperaciones(!enable);

        JPanel btns = (JPanel) calcProgramador.getComponent(0);
        Component[] botones = btns.getComponents();

        for (int i = 0; i < botones.length; i++) {
            JButton btn = (JButton) botones[i];
            //Si es 8 o 9 los desactivo
            if (btn.getText().equals("8") || btn.getText().equals("9")) {
                btn.setEnabled(!enable);
            } else if (btn instanceof BotonOperacion) {
                disabledPuntoPotencia(btn);
            } else {  //Si no son botones operadores les cambio el listener
                //Borro todos los actionlistener que tenga
                for (ActionListener act : btn.getActionListeners()) {
                    btn.removeActionListener(act);
                }
                //Le pongo el listener decimal normal
                btn.addActionListener(convertListener);
                //Siempre tiene el listener de formato
                btn.addActionListener(formatListener);
            }
        }
    }

    /**
     * Controla cuando el boton de binario tiene el foco, desactiva los botones
     * que no deben aparecer.
     *
     * Botones que no hacen falta en binario: todos menos el 0 y 1.
     *
     * De esta forma no confundimos al usuario, así sabe que en binario solo se
     * utiliza el 0 y 1.
     *
     * @param enable boolean determina si hay que activarlos o desactivarlos
     */
    private void binFocus(boolean enable) {
        vista.setPantallaFormat(noDecimalFormat);
        Logger.getLogger(Vista.class.getName())
                .log(Level.INFO, "Cambio al foco binario con su formato.");

        //Activo y desactivo los controles necesarios
        tbVerdadAct.setEnabled(!enable);
        vista.setAccionOperaciones(!enable);

        JPanel btns = (JPanel) calcProgramador.getComponent(0);
        Component[] bt = btns.getComponents();

        for (int i = 0; i < bt.length; i++) {
            JButton btn = (JButton) bt[i];
            String texto = btn.getText();

            if (!texto.equals("1") && !texto.equals("0") && !texto.equals("C")) {
                //Debe de ser lo contrario ya que si es bin, que los quite
                btn.setEnabled(!enable);
            } else if (texto.equals("1") || texto.equals("0")) {
                //Borro todos los actionlistener que tenga
                for (ActionListener act : btn.getActionListeners()) {
                    btn.removeActionListener(act);
                }
                //Le pongo el listener decimal normal
                btn.addActionListener(convertListener);
            }
        }
    }
    
    //////////////////////////
    ///////// Actions
    //////////////////////////
    /**
     * Accion para los botones hexadecimales, para que tengan la misma accion, y
     * controlarlos mejor, todos a la vez.
     *
     * La clase esta vacia, no hace nada.
     *
     * @see AbstractAction
     */
    class HexadecimalAction extends AbstractAction {

        public HexadecimalAction() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

    /**
     * Accion para los botones de la tabla de verdad, para que tengan la misma
     * accion y controlarlos mejor, todos a la vez.
     *
     * La clase esta vacia, no hace nada.
     *
     * @see AbstractAction
     */
    class TablasVerdadAction extends AbstractAction {

        public TablasVerdadAction() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
}
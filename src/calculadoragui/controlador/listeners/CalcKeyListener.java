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
package calculadoragui.controlador.listeners;

import calculadoragui.controlador.ControladorProgramador;
import calculadoragui.controlador.command.CalcActionsCommand;
import calculadoragui.vista.Vista;
import calculadoragui.vista.VistaProgramador;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Clase que contiene el KeyListener que escucha los eventos de teclado y segun
 * la tecla de pulsen hace una accion u otra.
 *
 * En el constructor se le pasa la interfaz CalcActionsCommand.
 *
 * En este caso se implementa para facilitar al KeyListener en que casos tiene
 * que llamar a un metodo determinado del controlador, ya que al tener
 * diferentes modos de la calculadora las teclas no siempre tienen el mismo
 * control
 *
 * @see KeyListener
 * @see KeyEvent
 * @see ControladorProgramador#accionConversion(java.lang.String)
 * @see Vista
 * @author Antonio López Marín
 */
public class CalcKeyListener implements KeyListener {

    //Controlador
    ControladorProgramador controlador;
    //VistaProgramador
    VistaProgramador vista;
    //A que metodos del controlador hay que llamar
    //"Variable que contiene la llamada a un metodo"
    CalcActionsCommand accion;
    //Cuando se le da a borrar, en el teclado es el codigo 8
    final public int CLEAR = 8;
    
    /**
     * El constructor recoje el controlador para comunicarse con el y la vista
     * de la calculadora de programador para cuando esta activada y se escriben
     * las letras.
     *
     * Se le pasa el controlador programador para poder llamar a todos los
     * metodos del controlador, ya sean modo programador o del controlador
     * padre.
     *
     * Tambien se le pasa la vista para poder consultar en que modo de la
     * calculadora esta, y lo mas importante en las conversiones cual esta
     * seleccionada.
     *
     * @param accion CalcActionsCommand
     * @param controlador ControladorProgramador
     * @param vista VistaProgramador
     */
    public CalcKeyListener(CalcActionsCommand accion, ControladorProgramador controlador,
            VistaProgramador vista) {
        this.controlador = controlador;
        this.vista = vista;
        this.accion = accion;
    }

    public void setAccion(CalcActionsCommand accion) {
        this.accion = accion;
    }

    /**
     * Controla las teclas pulsadas que se pueden utilizar.
     *
     * Segun este en binario o no se utilizan unas teclas u otras.
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char tecla = e.getKeyChar();

        if (!vista.isBinSelected()) {
            noBinario(tecla);
            //Si se pulso una operacion numerica
            if (tecla > 0x30 && tecla < 0x39) {
                //Cambio el formato dependiendo de que esta seleccionado
                //por si se pulso la tabla de verdad y hay que volver a numerico
                vista.cambiarFormatoProgramador();
            }
        } else if (tecla == '1') {
            accion.doIt(tecla);
        } else if (tecla == '0') {
            accion.doIt(tecla);
        }
    }
    
    /**
     * Cuando se pulsa una tecla, comprueba que se le dio a la de borrar.
     * 
     * @param e 
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == CLEAR) {
            controlador.limpiarCalculadora();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Metodo que comprueba las teclas cuando no esta en modo binario (0 y 1).
     *
     * @param tecla
     */
    public void noBinario(char tecla) {
        if (tecla == '0') {
            accion.doIt(tecla);
        } else if (tecla == '1') {
            accion.doIt(tecla);
        } else if (tecla == '2') {
            accion.doIt(tecla);
        } else if (tecla == '3') {
            accion.doIt(tecla);
        } else if (tecla == '4') {
            accion.doIt(tecla);
        } else if (tecla == '5') {
            accion.doIt(tecla);
        } else if (tecla == '6') {
            accion.doIt(tecla);
        } else if (tecla == '7') {
            accion.doIt(tecla);
            //Si esta en el modo octal no se pueden pulsar estas teclas
        } else if (tecla == '8' && !vista.isOctalSelected()) {
            accion.doIt(tecla);
        } else if (tecla == '9' && !vista.isOctalSelected()) {
            accion.doIt(tecla);
        } else if (vista.isOperacionesActivas()) {  //Si se pulsa una operacion
            operaciones(tecla);
        } else if (vista.isHexActive()) {           //Si hexadecimal esta activo
            hexadecimal(tecla);
        }
    }

    /**
     * Operaciones las peraciones siempre llaman al mismo metodo del
     * controlador.
     *
     * @param tecla
     */
    public void operaciones(char tecla) {
        if (tecla == '+') {
            controlador.accionOperando(Vista.Operaciones.SUMA);
        } else if (tecla == '-') {
            controlador.accionOperando(Vista.Operaciones.RESTA);
        } else if (tecla == '*') {
            controlador.accionOperando(Vista.Operaciones.MULTIPLICACION);
        } else if (tecla == '/') {
            controlador.accionOperando(Vista.Operaciones.DIVISION);
        } else if (tecla == '.') {
            controlador.accionPunto();
        }
    }

    /**
     * Metodo que comprueba si la tecla es una de las letras que se utilizan en
     * hexadecimal.
     *
     * @param tecla
     */
    public void hexadecimal(char tecla) {
        if (tecla == 'a') {
            controlador.accionConversion("A");
        } else if (tecla == 'b') {
            controlador.accionConversion("B");
        } else if (tecla == 'c') {
            controlador.accionConversion("C");
        } else if (tecla == 'd') {
            controlador.accionConversion("D");
        } else if (tecla == 'e') {
            controlador.accionConversion("E");
        } else if (tecla == 'f') {
            controlador.accionConversion("F");
        }
    }
}

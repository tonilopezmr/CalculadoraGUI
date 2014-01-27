/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadoragui.controlador.listeners;

import calculadoragui.vista.VistaProgramador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener que controla en que formato esta la calculadora programador en cada
 * momento, ya que puede tener distintos formatos a la vez, y este listener los
 * reparte.
 *
 * @see myDecimalFormat
 * @see NoDecimalFormat
 * @see ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
public class ControlFormatterListener implements ActionListener {

    VistaProgramador vista;

    public ControlFormatterListener(VistaProgramador vista) {
        this.vista = vista;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        vista.cambiarFormatoProgramador();
    }
}

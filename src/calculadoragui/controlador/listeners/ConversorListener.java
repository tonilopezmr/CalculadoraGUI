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

import calculadoragui.controlador.ControladorGUI;
import calculadoragui.controlador.ControladorProgramador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * Clase que implementa ActionListener para los eventos de las operaciones de
 * las conversiones.
 * 
 * @see ActionListener
 * @see ControladorGUI#accionConversion(java.lang.String) 
 * @author Antonio López Marín
 */
public class ConversorListener implements ActionListener {

    ControladorProgramador controlador;    
    
    /**
     * Constructor que recibe el controlador del modo programador.
     * 
     * @param controlador 
     */
    public ConversorListener(ControladorProgramador controlador) {
        this.controlador = controlador;
    }
    
    /**
     * Cuando se lanza el evento, se captura y le pasa a la accionConversion
     * el valor que tiene un boton.
     * 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnAct = (JButton) e.getSource();
        //Llama al controlador
        controlador.accionConversion(btnAct.getText());
    }
}

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * Clase que implementa el ActionListener para los eventos del los botones
 * numericos.
 *
 * @see ActionListener
 * @see ActionEvent
 * @see ControladorGUI#accionNumerico(java.lang.String)
 * @author Antonio López Marín
 */
public class DecimaListener implements ActionListener {
    
    ControladorGUI controlador;
    
    /**
     * Constructor que recibe el controlador para que pueda pasarle las
     * acciones de los numeros;
     * 
     * @param controlador 
     */
    public DecimaListener(ControladorGUI controlador) {
        this.controlador = controlador;
    }
    
    /**
     * Recoje el boton pulsado y lo envia al controlador.
     * 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnAct = (JButton) e.getSource();
        //Llama al controlador
        controlador.accionNumerico(Integer.parseInt(btnAct.getText()));
    }
}
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
import calculadoragui.vista.BotonOperacion;
import calculadoragui.vista.Vista;
import calculadoragui.vista.formatters.BooleanFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * Clase que implementa el ActionListener para los eventos de los botones
 * de las tablas de verdad.
 *
 * @see ActionListener
 * @see ActionEvent
 * @author Antonio López Marín
 */
public class TablasVerdadListener implements ActionListener {

    ControladorProgramador controlador;
    Vista vista;
    BooleanFormat boolFormat;
    
    /**
     * Constructor que recibe el controaldor, la vista, y el formato de las 
     * tablas de verdad.
     * 
     * @param controlador
     * @param vista
     * @param boolFormat 
     */
    public TablasVerdadListener(ControladorProgramador controlador, Vista vista,
            BooleanFormat boolFormat) {
        this.controlador = controlador;
        this.vista = vista;
        this.boolFormat = boolFormat;
    }
    
    /**
     * Recoje la operacion y las acciones de true o false para operar.
     * 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        if (btn instanceof BotonOperacion) {
            BotonOperacion btnAct = (BotonOperacion) btn;
            //Le paso la operacion
            vista.accionOperador(btnAct.getOperacion());
        } else {
            try {
                vista.setPantallaFormat(boolFormat);
                controlador.accionBooleano(Boolean.parseBoolean(btn.getText()));
            } catch (ArithmeticException ae) {
                vista.setTextPantalla(ae.getMessage());
            }
        }
    }
}
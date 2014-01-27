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

import calculadoragui.vista.BotonOperacion;
import calculadoragui.vista.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Inner Class que implementa el ActionListener para los eventos del los botones
 * de las operaciones.
 *
 * @see ActionListener
 * @see ActionEvent
 * @see ControladorGUI#accionOperando(java.lang.String)
 * @see Vista
 * @author Antonio López Marín
 */
public class OperacionListener implements ActionListener {
    
    Vista vista;
    
    /**
     * Constructor que recibe la vista para tratar con operaciones.
     * 
     * @param vista 
     */
    public OperacionListener(Vista vista) {
        this.vista = vista;
    }
    
    /**
     * Recoje la operacion y la vista para que trate las excepciones.
     * 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        BotonOperacion btnAct = (BotonOperacion) e.getSource();
        //recojo la id del boton y se la paso al metodo accionOperacion
        vista.accionOperador(btnAct.getOperacion());
    }
}
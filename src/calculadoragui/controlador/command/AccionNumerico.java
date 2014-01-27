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
package calculadoragui.controlador.command;

import calculadoragui.controlador.ControladorGUI;

/**
 * Clase que extiende de la interfaz CalcActionsCommand para guardar
 * la llamada al metodo de accion numerica, que añade un numero decimal
 * a la pantalla de la calculadora para operar.
 * 
 * @author Antonio López Marín
 */
public class AccionNumerico implements CalcActionsCommand<Character>{

    ControladorGUI controlador;
    
    /**
     * Constructor que recibe el controlador, para poder llamar al metodo
     * de accion numerico.
     * 
     * @param controlador ControladorGUI
     */
    public AccionNumerico(ControladorGUI controlador) {
        this.controlador = controlador;
    }   
    
    /**
     * El metodo doIt ejecutara el accion numerico pasandole un numero por 
     * parametros.
     * 
     * @param value 
     */
    @Override
    public void doIt(Character value) {
        controlador.accionNumerico(Integer.parseInt(""+value));
    }    
}

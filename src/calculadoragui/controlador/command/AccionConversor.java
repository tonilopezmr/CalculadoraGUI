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

import calculadoragui.controlador.ControladorProgramador;

/**
 * Clase que extiende de la interfaz CalcActionsCommand para guardar
 * la llamada al metodo de accion de conversion, que añade un numero 
 * de una conversion a la pantalla de la calculadora para operar.
 * 
 * @author Antonio López Marín
 */
public class AccionConversor implements CalcActionsCommand<Character>{

    ControladorProgramador controlador;
    
    /**
     * Constructor que recibe el controlador Programador, para poder llamar
     * al metodo de conversion.
     * 
     * @param controlador ControladorProgramador
     */
    public AccionConversor(ControladorProgramador controlador) {
        this.controlador = controlador;
    }       
        
    /**
     * El metodo doIt ejecutara la accionConversion pasandole el valor.
     * 
     * @param value 
     */
    @Override
    public void doIt(Character value) {
        controlador.accionConversion(String.valueOf(value));
    }    
}

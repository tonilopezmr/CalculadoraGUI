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

import calculadoragui.vista.Vista.Operaciones;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Clase que extiende de JButton y añade una funcionalidad que almacena
 * una operacion de tipo ENUM, y la devuelve.
 * 
 * @author Antonio López Marín
 */
public class BotonOperacion extends JButton{
    
    //Enum de operaciones
    Operaciones operacion;
    
    public BotonOperacion() {
    }

    public BotonOperacion(Icon icon) {
        super(icon);
    }

    public BotonOperacion(String text) {
        super(text);
    }

    public BotonOperacion(Action a) {
        super(a);
    }

    public BotonOperacion(String text, Icon icon) {
        super(text, icon);
    }
    
    /**
     * Metodo que devuelve la operacion almacenada.
     * @return 
     */
    public Operaciones getOperacion() {
        return operacion;
    }
    
    /**
     * Metodo que inserta o cambia la operacion a almacenar.
     * 
     * @param operacion 
     */
    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }
}

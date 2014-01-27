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
package calculadoragui.vista.formatters;

import java.awt.event.FocusEvent;
import java.text.Format;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.InternationalFormatter;

/**
 * Clase que extiende de JFormattedTextField para añadir la funcionalidad
 * de poder cambiar el Formatter de la interfaz Format.
 * 
 * @author Antonio López Marín
 */
public class myJFormattedTextField extends JFormattedTextField{

    public myJFormattedTextField() {
        super();
    }

    public myJFormattedTextField(Object value) {
        super(value);
    }

    public myJFormattedTextField(Format format) {
        super(format);
    }

    public myJFormattedTextField(AbstractFormatter formatter) {
        super(formatter);
    }

    public myJFormattedTextField(AbstractFormatterFactory factory) {
        super(factory);
    }

    public myJFormattedTextField(AbstractFormatterFactory factory, Object currentValue) {
        super(factory, currentValue);
    }
    
    /**
     * Creo un metodo que pueda cambiar el formato del Field mediante la Interfaz
     * Format.
     * 
     * Por default solo se puede cambiar una vez instanciada la clase con 
     * AbstractFormatter.
     * 
     * @param format Format
     */
    public void setFormatter(Format format){
        super.setFormatterFactory(new DefaultFormatterFactory(
                new InternationalFormatter(format)));
    }
    
    /**
     * Cambia el formato de modo dinamico a lo largo de la aplicacion.
     * 
     * @param format 
     */
    @Override
    public void setFormatter(AbstractFormatter format) {
        super.setFormatter(format); 
    }
    
    /**
     * Mantengo esta implementacion ahora inecesaria a modo de escarmiento,
     * ya que anteriormente habia problemas con los tipos de datos ya que
     * la pantalla estaba suscrita al Listener del foco y cada vez que cambiaba
     * de foco se actualizaban los valores, y en ocasiones al haber mensajes
     * de errores en pantalla, era imcompatible con el formato numerico y daba
     * fallo.
     * 
     * Importante que no haga nada este metodo ya que cada vez que se cambia
     * el foco, de un boton de hexadecimal refresca el valor con lo que hay 
     * puesto en pantalla y puede ser que alguna vez haya un error, y no este
     * el valor que se quiere poner a la pantalla.
     *
     * @param e 
     * @deprecated Ya no esta suscrito a cambio de foco
     */
    @Override
    protected void processFocusEvent(FocusEvent e) {
        //Do nothing
    }
}

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

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * Clase para para los formatos que no son decimales, es decir, hex, bin, oct
 * que se muestran de cuatro en cuatro.
 *
 * @author Antonio López Marín
 */
public class NoDecimalFormat extends Format {
    
    /**
     * Metodo que reordena los espacios que debe de tener el formato, 
     * mediante un algoritmo.
     * 
     * Recorre desde el final la cadena y va poniendo espacios cada 4 
     * caracteres.
     * 
     * @param cadena
     * @return 
     */
    public static StringBuffer reordenarEspacios(String cadena) {
        StringBuffer aux = new StringBuffer(cadena);

        int cont = 0;
        for (int i = cadena.length(); i > 0; i--) {
            if (cont % 4 == 0 && cont != 0) {
                aux.insert(i, " ");
            }
            cont++;
        }

        return aux;
    }
    
    /**
     * Metodo que recibe un Objeto y si es un String lo formatea cada cuatro
     * letras un espacio entre medias.
     * 
     * Ej: 1001 1001 1101
     * 
     * @param obj Object
     * @param toAppendTo StringBuffer
     * @param pos FieldPosition
     * @return  StringBuffer
     */
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj != null) {
            if (obj instanceof String) {
                String stringValue = (String) obj;
                
                return reordenarEspacios(stringValue);
            }
        }
        return new StringBuffer(String.valueOf(obj));
    }
    
    /**
     * Metodo que hace una conversion de String con espacios a Sring sin 
     * espacios.
     * 
     * @param source String
     * @param pos ParsePosition
     * @return Object
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return source.replaceAll(" ", "");
    }
}

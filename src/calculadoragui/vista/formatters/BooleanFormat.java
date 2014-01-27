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
 * Poner la primera letra en mayuscula.
 * 
 * Clase del formato de un numero booleano, simplemente pasa un booleano 
 * a texto con la primera letra mayuscula.
 * 
 * @author Antonio López Marín
 */
public class BooleanFormat extends Format{

    public BooleanFormat() {
    }
    
    /**
     * Metodo que recibe por parametros el valor, que sera un String booleano,
     * y pone la primera letra en mayusculas.
     * 
     * @param obj Object
     * @param toAppendTo StringBuffer
     * @param pos FieldPosition
     * @return 
     */
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        String string = obj.toString();
        StringBuffer buffer = new StringBuffer(String.valueOf(
                string.substring(0, 1).toUpperCase() + string.substring(1)));
        return buffer;
    }
    
    /**
     * Metodo que hace una conversion de String a booleano.
     * 
     * @param source String
     * @param pos ParsePosition
     * @return 
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return Boolean.parseBoolean(source);
    }

}

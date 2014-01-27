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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;

/**
 * Clase que extiende de DecimalFormat, para no tener problemas a la hora de
 * poner un punto en pantalla, que en DecimalFortam es una coma.
 * 
 * Formatea segun los miles con punto y los decimales con coma.
 * 
 * @author Antonio López Marín
 */
public class myDecimalFormat extends DecimalFormat {

    public myDecimalFormat() {
    }

    public myDecimalFormat(String pattern) {
        super(pattern);
    }

    public myDecimalFormat(String pattern, DecimalFormatSymbols symbols) {
        super(pattern, symbols);
    }
    
    /**
     * Compruebo si tiene ,0 y le pongo el punto, al personalizar con este metodo
     * como quiero que quede el numero tengo mayor control de la calculadora
     * y poder tener menos fallos por cosas raras que implementa esta clase.
     * 
     * @param number double
     * @param result StringBuffer
     * @param fieldPosition FieldPosition
     * @return  StringBuffer
     */
    @Override
    public StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition) {
        StringBuffer numero = super.format(number, result, fieldPosition);
        String numeroFormateado;
        //Recorto el numero a los dos ultimos digitos, normalmente ".0"
        if (numero.length() > 2) {
            String recortado = numero.substring(numero.length() - 2,
                    numero.length());
            //Si tiene .0, se los quito
            numeroFormateado = String.valueOf(recortado.equals(".0")? 
                    numero.substring(0, numero.length() - 2) : numero);
            return new StringBuffer(numeroFormateado);
        }

        return numero;
    }
}

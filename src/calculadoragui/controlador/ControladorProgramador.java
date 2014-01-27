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
package calculadoragui.controlador;

import calculadoragui.vista.Vista;
import static calculadoragui.vista.Vista.Operaciones.AND;
import static calculadoragui.vista.Vista.Operaciones.BINARIO;
import static calculadoragui.vista.Vista.Operaciones.DECIMAL;
import static calculadoragui.vista.Vista.Operaciones.HEXADECIMAL;
import static calculadoragui.vista.Vista.Operaciones.NOT;
import static calculadoragui.vista.Vista.Operaciones.OCTAL;
import static calculadoragui.vista.Vista.Operaciones.OR;
import static calculadoragui.vista.Vista.Operaciones.XOR;

/**
 * Controlador del modo Programador, que extiende de ControladorGUI, para poder
 * tener sus propiedades y tenga el mismo funcionamiento excepto algunos casos.
 *
 * @author Antonio López Marín
 */
public class ControladorProgramador extends ControladorGUI {
    
    /**
     * Constructor que le pasa la vista al controlador padre.
     *
     * @param vista
     */
    public ControladorProgramador(Vista vista) {
        super(vista);
    }

    /**
     * Sobrescribe el metodo AccionOperando y comprueba que se le da al igual
     * para calcular una operacion Booleana.
     *
     * @param operador
     * @throws ArithmeticException
     */
    @Override
    public void accionOperando(Vista.Operaciones operador) throws ArithmeticException {
        if (operador == Vista.Operaciones.IGUAL && isBooleanOperation) {
            calcularTablaVerdad();
        } else {
            super.accionOperando(operador);
        }
    }

    /**
     * Si el nuevo Operador es NOT, calcula las tablas de verdad.
     *
     * @param operador
     */
    @Override
    protected void nuevoOperador(Vista.Operaciones operador) {
        super.nuevoOperador(operador);
        if (Vista.Operaciones.NOT == operador) {
            calcularTablaVerdad();
        }
    }

    /**
     * La accion que realiza un boton de una conversion, que añade el numero en
     * la pantalla.
     *
     * @param numero String numero hex o bin o oct
     */
    public void accionConversion(String numero) {
        String numeroPantalla = vista.getConversionPantalla();
        //Si es nueva operacion que ponga solo un numero
        if (newOperacion || isError()) {
            hasPressIgual();
            vista.setConversionPantalla(numero);
            newOperacion = false;
        } else {
            if (numeroPantalla.length() < 15
                    || (vista.isBinSelected() && numeroPantalla.length() < 50)) {
                vista.setConversionPantalla(numeroPantalla + numero);
            }
        }
    }

    /**
     * Metodo que recoje el booleano que hay en pantalla y verifica que es
     * correcto para guardarlo.
     *
     * @param booleano String texto que deberia contener un booleano
     * @return el resultado que se recogio en pantalla
     * @throws ArithmeticException
     */
    public void accionBooleano(boolean booleano) throws ArithmeticException {
        vista.setBooleanoPantalla(booleano);
        //Siempre que se pulse un booleano es una nueva operacion
        newOperacion = true;

        if (!isBooleanOperation) {
            //Indico que comenzo una operacion de las tablas de verdad
            isBooleanOperation = true;

            booleanoCache = booleano;
        }
    }

    /**
     * Metodo que envia a la vista el resultado, ya en forma de String
     *
     * @param resultado boolean el resultado de las tablas de verdad
     */
    private void setResultado(String resultado) {
        vista.setConversionPantalla(resultado);
    }

    /**
     * Metodo que envia a la vista el resultado con el formato correcto.
     *
     * @param resultado boolean el resultado de las tablas de verdad
     */
    private void setResultado(boolean resultado) {
        vista.setBooleanoPantalla(resultado);
    }

    /**
     * Modificar el origen.
     *
     * @param origen
     */
    public void setOrigen(Vista.Operaciones origen) {
        this.origen = origen;
    }

    /**
     * Modificar el destino.
     *
     * @param destino
     */
    public void setDestino(Vista.Operaciones destino) {
        this.destino = destino;
    }

    /**
     * Metodo qeu calcula la conversion segun el origen y destino, que se
     * encuentre en ese momento.
     *
     */
    public void calcularConversion(String numero) throws ArithmeticException,
            NumberFormatException {

        //Si es distinto de un error, que hay en pantalla..
        if (!isError()) {
            //Preparo el resultado
            String resultado = "";
            //Preparo el resultado decimal
            double decimalResultado = 0;

            numero = numero.replace(".0", "");

            //Calcula segun los valores de las propiedades
            switch (origen) {
                case DECIMAL:
                    //Paso el numero de la cache a long.
                    double numeroDecimal = Double.parseDouble(numero);
                    switch (destino) {
                        case BINARIO:
                            resultado = calc.deDecimalABinario((long) numeroDecimal);
                            break;
                        case OCTAL:
                            resultado = calc.deDecimalAOctal((long) numeroDecimal);
                            break;
                        case HEXADECIMAL:
                            resultado = calc.deDecimalAHexadecimal((long) numeroDecimal);
                            break;
                        default:
                            newOperacion = true;
                            throw new ArithmeticException(Vista.ERROR);
                    }
                    break;
                case BINARIO:
                    switch (destino) {
                        case DECIMAL:
                            decimalResultado = calc.deBinarioADecimal(numero);
                            break;
                        case OCTAL:
                            resultado = calc.deBinarioAOctal(numero);
                            break;
                        case HEXADECIMAL:
                            resultado = calc.deBinarioAHexadecimal(numero);
                            break;
                        default:
                            newOperacion = true;
                            throw new ArithmeticException(Vista.ERROR);
                    }

                    break;
                case OCTAL:
                    switch (destino) {
                        case DECIMAL:
                            decimalResultado = calc.deOctalADecimal(numero);
                            break;
                        case BINARIO:
                            resultado = calc.deOctalABinario(numero);
                            break;
                        case HEXADECIMAL:
                            resultado = calc.deOctalAHexadecimal(numero);
                            break;
                        default:
                            newOperacion = true;
                            throw new ArithmeticException(Vista.ERROR);
                    }

                    break;
                case HEXADECIMAL:
                    switch (destino) {
                        case DECIMAL:
                            decimalResultado = calc.deHexadecimalADecimal(numero);
                            break;
                        case BINARIO:
                            resultado = calc.deHexadecimalABinario(numero);
                            break;
                        case OCTAL:
                            resultado = calc.deHexadecimalAOctal(numero);
                            break;
                        default:
                            newOperacion = true;
                            throw new ArithmeticException(Vista.ERROR);
                    }
                    break;
                default:
                    newOperacion = true;
                    throw new ArithmeticException(Vista.ERROR);
            }

            //Todas se muestra con resultado no decimal menos el decimal
            //con su formato
            if (destino == Vista.Operaciones.DECIMAL) {
                setResultado(decimalResultado);
            } else {
                setResultado(resultado);
            }
        }
    }

    /**
     * Metodo que calcula las operaciones de las tablas de verdad, segun su
     * operacion.
     *
     * @see Calculadora
     * @throws ArithmeticException
     */
    public void calcularTablaVerdad() throws ArithmeticException {
        //Si se pulso un operador de las tablas de verdad sin poner antes
        //un valor, devuelve error
        if (!isBooleanOperation) {
            newOperacion = true;
            throw new ArithmeticException(Vista.ERROR);
        }

        boolean resultado;

        //Indico que termino de ser una operacion de las tablas de verdad
        isBooleanOperation = false;

        //Guardo lo que habia en la cache
        boolean booleano1 = booleanoCache;
        //Recojo el booleano que hay en pantalla
        boolean booleano2 = vista.getBooleanPantalla();

        //Segun operador
        switch (operador) {
            case AND:
                resultado = calc.and(booleano1, booleano2);
                break;
            case OR:
                resultado = calc.or(booleano1, booleano2);
                break;
            case XOR:
                resultado = calc.xor(booleano1, booleano2);
                break;
            case NOT:
                //Directamente calcula segun lo que hay en la pantalla
                resultado = calc.not(vista.getBooleanPantalla());
                break;
            default:
                newOperacion = true;
                throw new ArithmeticException(Vista.ERROR);
        }

        //Poner el resultado booleano
        setResultado(resultado);
    }
}

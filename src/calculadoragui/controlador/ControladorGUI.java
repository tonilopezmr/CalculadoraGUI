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

import calculadoragui.modelo.Calculadora;
import calculadoragui.vista.Vista;
import calculadoragui.vista.Vista.Operaciones;

/**
 * Controlador de la Calculadora GUI, mantiene comunicaciones con la vista para
 * modificarla y con el modelo para poder realizar los calculos.
 *
 * Sus metodos y variables son protected, para que se pueda añadir nueva 
 * funcionalidad facilmente a la calculadora extendiendo del controlador.
 * 
 * @see Calculadora Modelo
 * @see Vista
 * @author Antonio López Marín
 */
public class ControladorGUI {
    //Comunicacion con la vista

    protected Vista vista;
    //Comunicacion con el modelo
    protected Calculadora calc;
    //Controlo cuando es una nueva operacion
    protected boolean newOperacion;
    //Controlo si pulsaron el igual
    protected boolean pressIgual;
    //Guardo el resultado
    protected double numeroCache;
    //Guardo el primer booleano de las tablas de verdad
    protected boolean booleanoCache;
    //Conversores
    protected String conversorCache;
    //Origen y destino de las conversiones
    protected Operaciones origen, destino;
    //Guardo el operador
    protected Operaciones operador;
    //Si es una operacion de las tablas de verdad
    protected boolean isBooleanOperation;
    //Si pulsaron el punto
    protected boolean pressPunto;

    /**
     * Contructor que inicia la comunicacion con la vista y crea instancia del
     * modelo de la calculadora.
     *
     * @param vista Vista
     */
    public ControladorGUI(Vista vista) {
        this.vista = vista;
        calc = new Calculadora();
        newOperacion = true;
        numeroCache = 0;
        operador = Operaciones.NONE;
    }
    
    /**
     * Metodo que me devuelve si lo que hay en pantalla es un cero.
     *
     * @return true si es 0, sino false
     */
    protected boolean isCero() {
        return vista.getTextPantalla().equals("0");
    }

    /**
     * Metodo que devuelve true si hay un punto decimal ya puesto en la
     * pantalla.
     *
     * @return true si en la pantalla hay un punto decimal, sino false
     */
    protected boolean hasPunto() {
        return vista.getTextPantalla().contains(",");
    }

    /**
     * Metodo que devuelve si lo que hay en la pantalla es un error.
     *
     * Puede que haya un mensaje de error, o puede ser que el resultado fuera
     * Infinito, compruebo los dos casos.
     *
     * @see Vista#ERROR
     * @return true si la pantalla esta mostrando un error.
     */
    protected boolean isError() {
        return vista.getTextPantalla().equals(Vista.ERROR)
                || vista.getTextPantalla().equals("Numero demasiado grande");
    }
    
    /**
     * Si se pulso la tecla igual se pone el numeroCache a 0.
     */
    protected void hasPressIgual() {
        if (pressIgual) {
            numeroCache = 0;
        }
    }
    
    /**
     * Metodo que quita el .0 al numero que se le pasa por pantalla.
     * 
     * @param numero
     * @return 
     */
    protected String quitarCero(String numero) {
        //Recorto el numero a los dos ultimos digitos, normalmente ".0"
        String recortado = numero.substring(numero.length() - 2,
                numero.length());
        //Si tiene .0, se los quito
        numero = recortado.equals(".0")
                ? numero.substring(0, numero.length() - 2) : numero;
        return numero;
    }
    
    /**
     * Pone el punto en pantalla, seguido de un numero.
     * 
     * @param numPantalla
     * @param numero
     * @return 
     */
    public String ponerPunto(String numPantalla, int numero){        
        return quitarCero(numPantalla)+"." + numero;
    }
    
    /**
     * Muy peligroso este metodo, hay que tener cuidado como se usa, 
     * principalmente es porque al añadir la funcionalidad de pegar del porta
     * pales hay que informar al controlador que se hizo.
     * 
     * @param bool 
     */
    public void setNewOperacion(boolean bool){
        newOperacion = bool;
    }
    
    /**
     * La accion que realiza un boton numerico, que añade el numero en la
     * pantalla.
     *
     * @param numero El numero que se pulso
     */
    public void accionNumerico(int numero) {
        //Lo paso a string para poder controlar mejro el numero para unirlo
        String numPantalla = String.valueOf(vista.getDecimalPantalla());
        //Si es nueva operacion que ponga solo un numero
        if (newOperacion) {
            hasPressIgual();
            if (pressPunto) {
                vista.setDecimalPantalla(Double.parseDouble(
                        ponerPunto(numPantalla, numero)));
                pressPunto = false;
            } else {
                vista.setDecimalPantalla(numero);
            }
            newOperacion = false;
        } else {
            if (pressPunto) {
                vista.setDecimalPantalla(Double.parseDouble(
                        ponerPunto(numPantalla, numero)));
                pressPunto = false;
            } else {
                vista.setDecimalPantalla(Double.parseDouble(
                        quitarCero(numPantalla) + numero));
            }
        }
    }

    /**
     * Restablece los valores por defecto.
     *
     * Restablece numeroCache, la operacion, la pantalla y si pulsaron igual.
     */
    public void limpiarCalculadora() {
        numeroCache = 0;
        this.operador = Operaciones.NONE;
        vista.setDecimalPantalla(0);
        vista.setTextPantalla("0");
        vista.setEtiquetaPantalla("");
        pressIgual = false;
        pressPunto = false;
        isBooleanOperation = false;
    }

    /**
     * La accion que realiza un boton de un operador, que segun el boton que se
     * haya pulsado la calculadora realiza una u otra cosa.
     *
     * Si se pulso el boton igual, y no hay un error en pantalla, calcula el
     * resultado de una operacion.
     *
     * Si se pulso el boton de limpiar la pantalla, se limpia la pantalla.
     *
     * Y si es cualquier otra cosa es un operador.
     *
     * @param operador int El operador que se pulso, su id
     */
    public void accionOperando(Operaciones operador) throws ArithmeticException {
        //Segun el operador
         switch (operador) {
            case IGUAL:
                //Si lo que hay en pantalla es un error que no haga nada
                if (!isError()) {
                    calcular();
                    pressIgual = true;
                }
                break;
            case CLEAR: //Limpiar pantalla y restablece los valores por defecto
                limpiarCalculadora();
                break;
            default:
                pressIgual = false;
                nuevoOperador(operador);    //Nuevo operador
                break;
        }

        newOperacion = true;
    }

    /**
     * La accion que realiza el punto, no se debe de poder poner mas de un punto
     * decimal en la pantalla.
     *
     */
    public void accionPunto() {
        if (!hasPunto()) {
            pressPunto = true;
        }else{
            pressPunto = false;
        }
    }

    /**
     * Metodo que controla los operadores, cuando deben de añadir un resulatado
     * o tiene que calcular las operaciones.
     *
     * @param operador
     */
    protected void nuevoOperador(Operaciones operador) {
        controlCalcular();          //Primero se controla si se debe de calcular antes
        this.operador = operador; //Se añade el operador
        //Si es una operacion booleana que no recoja este numero en la cache
        if (!isBooleanOperation) {
            addNumCache();
        }
        calcularExcepcional();  //Segun el nuevo operador, si se debe calcular ya
    }

    /**
     * Metodo que calcula segun la operacion y los numeros que se reciban por
     * pantalla y el numero que estaba guardado en resultados anteriores.
     *
     * @see Calculadora
     * @see Double#parseDouble(java.lang.String)
     * @throws ArithmeticException
     */
    protected void calcular() throws ArithmeticException {
        //Primer numero
        double numero1 = numeroCache;
        //Segundo numero
        double numero2 = vista.getDecimalPantalla();
        //Variable para el resultado
        double resultado;

        //Como se vuelve a calcular vacio la etiqueta de la pantalla
        vista.setEtiquetaPantalla("");

        //Segun operador
        switch (operador) {
            case SUMA:
                resultado = calc.suma(numero1, numero2);
                break;
            case RESTA:
                //Si pulso el igual se intercambian los numeros para que se pueda
                //seguir restando entre ese numero y puedan ser negativos.
                if (pressIgual) {
                    resultado = calc.resta(numero2, numero1);
                } else {
                    resultado = calc.resta(numero1, numero2);
                }
                break;
            case MULTIPLICACION:
                resultado = calc.multiplicacion(numero1, numero2);
                break;
            case DIVISION:
                resultado = calc.division(numero1, numero2);
                break;
            case POTENCIA:
                resultado = calc.potencia(numero1, numero2);
                break;
            case RESTO:
                resultado = calc.resto(numero1, numero2);
                break;
            case RAIZ:
                resultado = calc.raiz(numero1, (int) numero2);
                break;
            case SENO:
                resultado = calc.seno(numero2);
                vista.setEtiquetaPantalla("sin(" + numero2 + ")");
                break;
            case COSENO:
                resultado = calc.coseno(numero2);
                vista.setEtiquetaPantalla("cos(" + numero2 + ")");
                break;
            case TANGENTE:
                resultado = calc.tangente(numero2);
                vista.setEtiquetaPantalla("tan(" + numero2 + ")");
                break;
            default:
                //Añado lo que haya en pantalla para posteriormente, poder
                //seguir con la operacion.
                addNumCache();
                newOperacion = true;
                throw new ArithmeticException(Vista.ERROR);
        }

        setResultado(resultado);//Muestra el resultado
    }

    /**
     * Hay excepciones que se calcula sin dar al igual.
     *
     */
    protected void calcularExcepcional() {
        if (Operaciones.SENO == operador) {
            calcular();
        } else if (Operaciones.COSENO == operador) {
            calcular();
        } else if (Operaciones.TANGENTE == operador) {
            calcular();
        }
    }

    /**
     * Mediante este metodo se controla si se calculo antes, de intentar
     * calcular o no, ya que hay ocasiones excepcionales que se necesita
     * calcular antes de darle al igual.
     *
     * Y devuelve si se realizo un calculo o no.
     */
    protected boolean controlCalcular() {
        boolean salida;

        if ((numeroCache != 0) && !newOperacion && !isError()) {
            calcular();
            salida = true;
        } else if ((Operaciones.RESTA == operador) && (numeroCache == 0)) {
            calcular();
            salida = true;
        } else {
            salida = false;
        }

        return salida;
    }

    /**
     * Metodo que envia a la vista el resultado con el numero correcto.
     * 
     * @param numeros Resultado de las operaciones
     */
    protected void setResultado(double numeros) {
        vista.setDecimalPantalla(numeros);
    }

    /**
     * Añade el numero de la cache a la variable controlando si hay un error en
     * pantalla.
     *
     * @see Double#parseDouble(java.lang.String)
     * @throws NumberFormatException
     */
    protected void addNumCache() throws NumberFormatException {
        if (!isError()) {
            numeroCache = vista.getDecimalPantalla();
        }
    }
}
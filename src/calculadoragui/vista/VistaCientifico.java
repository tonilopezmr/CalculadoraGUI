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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Antonio López Marín
 */
public class VistaCientifico {
   
    private Vista vista;    
    private JButton igualCientific;
    
    public VistaCientifico(Vista vista){
        this.vista = vista;
    }
    
    
        /**
     * Metodo que carga el modo de la calculadora Cientifica, con los botones de
     * las operaciones Trigonometricas.
     *
     * @param botoneraBasica JPanel que van a ser sus botones basicos.
     *
     */
    public JPanel loadCalcCientifica(JPanel botoneraBasica) {
        //Creo el panelCientifico con un borderLayout
        JPanel calcCientifico = new JPanel(new BorderLayout());

        //Panel que contiene los botones de operaciones trigonometricas
        //y el resto y raiz
        JPanel opTrigonometricas = new JPanel(new GridLayout(1, 5, 7, 7));
        opTrigonometricas.setName("trigonometricas");
                
        //Creo la caja horizontal para meter los botones
        Box box = Box.createHorizontalBox();
        box.add(vista.crearBotonOperador(Vista.Operaciones.SENO));
        box.add(vista.crearBotonOperador(Vista.Operaciones.COSENO));
        box.add(vista.crearBotonOperador(Vista.Operaciones.TANGENTE));
        box.add(vista.crearBotonOperador(Vista.Operaciones.RAIZ));
        box.add(vista.crearBotonOperador(Vista.Operaciones.RESTO));

        //Añado la caja 
        opTrigonometricas.add(vista.crearBotonOperador(Vista.Operaciones.SENO));
        opTrigonometricas.add(vista.crearBotonOperador(Vista.Operaciones.COSENO));
        opTrigonometricas.add(vista.crearBotonOperador(Vista.Operaciones.TANGENTE));
        opTrigonometricas.add(vista.crearBotonOperador(Vista.Operaciones.RAIZ));
        opTrigonometricas.add(vista.crearBotonOperador(Vista.Operaciones.RESTO));

        //Añado al panel de calculadora cientifica los paneles
        calcCientifico.add(opTrigonometricas, BorderLayout.NORTH);
        calcCientifico.add(botoneraBasica, BorderLayout.SOUTH);

        igualCientific = (JButton) botoneraBasica.getComponent(14);
        
        return calcCientifico;
    }
    
    /**
     * Devuelve el boton del igual de la calculadora cientifica
     * 
     */
    public JButton getIgualCientific() {
        return igualCientific;
    }

}
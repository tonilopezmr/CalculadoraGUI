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
package calculadoragui.App;

import calculadoragui.vista.Vista;

/**
 * Aplicacion de la Calculadora Graphical User Interface.
 * 
 * Lanza la aplicacion en el hilo de eventos.
 * 
 * @see Vista
 * @author Antonio López Marín
 * @version 3.4
 */
public class CalculadoraGUI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Windows".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//        } catch (InstantiationException ex) {
//        } catch (IllegalAccessException ex) {
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Vista.mostrar();
            }
        });
    }
}

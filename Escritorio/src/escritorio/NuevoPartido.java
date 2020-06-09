/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escritorio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.sql.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

/**
 *
 * @author sergio
 */
public class NuevoPartido extends javax.swing.JPanel {

    /**
     * Creates new form NuevoPartido
     */
    private VentanaPrincipal parent;

    public NuevoPartido() {
        initComponents();
    }

    public void setParent(VentanaPrincipal p) {
        parent = p;
    }

    public JComboBox<String> getjComboBoxDeportes() {
        return jComboBoxDeportes;
    }

    public JSpinner getjSpinnerBase() {
        return jSpinnerBase;
    }

    public JSpinner getjSpinnerMax() {
        return jSpinnerMax;
    }

    public JButton getjButtonReservar() {
        return jButtonReservar;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelDeporte = new javax.swing.JLabel();
        jComboBoxDeportes = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerMax = new javax.swing.JSpinner();
        jButtonDisponibilidad = new javax.swing.JButton();
        dateChooser = new datechooser.beans.DateChooserCombo();
        jLabel1 = new javax.swing.JLabel();
        jSpinnerBase = new javax.swing.JSpinner();
        jButtonReservar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(345, 352));
        setMinimumSize(new java.awt.Dimension(345, 352));

        jLabelDeporte.setText("Deporte");

        jComboBoxDeportes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un deporte", "Fútbol", "Padel", "Basket", "Balonmano" }));
        jComboBoxDeportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDeportesActionPerformed(evt);
            }
        });

        jLabel2.setText("Fecha");

        jLabel3.setText("Pista");

        jLabel4.setText("Máx Jugadores");

        jSpinnerMax.setModel(new javax.swing.SpinnerNumberModel(4, 4, 12, 1));

        jButtonDisponibilidad.setText("Disponibilidad");
        jButtonDisponibilidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisponibilidadActionPerformed(evt);
            }
        });

        jLabel1.setText("Jugadores Inicio");

        jSpinnerBase.setModel(new javax.swing.SpinnerNumberModel(0, 0, 11, 1));

        jButtonReservar.setText("Reservar");
        jButtonReservar.setEnabled(false);
        jButtonReservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReservarActionPerformed(evt);
            }
        });

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButtonCancelar)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addComponent(jLabel3)
                        .addComponent(jLabel2)
                        .addComponent(jLabelDeporte)
                        .addComponent(jLabel1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jComboBoxDeportes, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonDisponibilidad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSpinnerMax)
                            .addComponent(jSpinnerBase, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(8, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonReservar)
                        .addGap(42, 42, 42))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDeporte)
                    .addComponent(jComboBoxDeportes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonDisponibilidad)
                    .addComponent(jLabel3))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonReservar)
                    .addComponent(jButtonCancelar))
                .addContainerGap(39, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxDeportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDeportesActionPerformed

        //
    }//GEN-LAST:event_jComboBoxDeportesActionPerformed

    private void jButtonDisponibilidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisponibilidadActionPerformed
        if (jComboBoxDeportes.getSelectedIndex() != 0) {
            Calendar fechaSeleccionada = dateChooser.getCurrent();
            LocalDate localDate = LocalDateTime.ofInstant(fechaSeleccionada.toInstant(),
                    fechaSeleccionada.getTimeZone().toZoneId()).toLocalDate();
            Date date = Date.valueOf(localDate);
            int deporte_id = jComboBoxDeportes.getSelectedIndex();

            parent.refrescarTablaDisponibilidad(deporte_id, date);

        } else
            JOptionPane.showMessageDialog(null, "Debes escoger un deporte", "ERROR",
                    JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonDisponibilidadActionPerformed

    private void jButtonReservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReservarActionPerformed
        if (jComboBoxDeportes.getSelectedIndex() != 0) {
            if (JOptionPane.showConfirmDialog(null, "¿Deséa realizar la reserva con los datos introducidos?", "",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Calendar fechaSeleccionada = dateChooser.getCurrent();
                LocalDate localDate = LocalDateTime.ofInstant(fechaSeleccionada.toInstant(),
                        fechaSeleccionada.getTimeZone().toZoneId()).toLocalDate();
                Date date = Date.valueOf(localDate);
                parent.crearReserva(date, (Integer) jSpinnerMax.getValue(), (Integer) jSpinnerBase.getValue());
            }
        } else
            JOptionPane.showMessageDialog(null, "Debes escoger un deporte", "ERROR",
                    JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonReservarActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        if (JOptionPane.showConfirmDialog(null, "¿Seguro que deséa cancelar?", "",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            parent.cerrarCreacionPartido();
        }
    }//GEN-LAST:event_jButtonCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo dateChooser;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonDisponibilidad;
    private javax.swing.JButton jButtonReservar;
    private javax.swing.JComboBox<String> jComboBoxDeportes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelDeporte;
    private javax.swing.JSpinner jSpinnerBase;
    private javax.swing.JSpinner jSpinnerMax;
    // End of variables declaration//GEN-END:variables
}

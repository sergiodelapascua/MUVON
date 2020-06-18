/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escritorio;

import Util.Constantes;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import modelo.Cliente;
import modelo.PistaDisponible;
import modelo.Reserva;

/**
 *
 * @author sergio
 */
public class VentanaPrincipal extends javax.swing.JFrame implements Constantes {

    /**
     * Creates new form VentanaPrincipal
     */
    private DataOutputStream flujosalida = null;
    private DataInputStream flujoentrada = null;
    private Socket socket = null;
    private boolean tabla_clientes_seleccionada;
    private ModeloTablaClientes modeloTablaClientes;
    private ModeloTablaReservas modeloTablaReservas;
    private ModeloTablaDisponibilidad modeloTablaDisponibilidad;
    private boolean logeado;
    private PistaDisponible pistaEscogida;
    private Cliente clienteReserva;

    public VentanaPrincipal() {
        this.tabla_clientes_seleccionada = false;
        this.logeado = false;
        this.modeloTablaClientes = new ModeloTablaClientes();
        this.modeloTablaReservas = new ModeloTablaReservas();
        this.modeloTablaDisponibilidad = new ModeloTablaDisponibilidad();
        this.pistaEscogida = new PistaDisponible();
        this.clienteReserva = new Cliente();

        try {
            socket = new Socket(host, puerto);
            flujosalida = new DataOutputStream(socket.getOutputStream());
            flujoentrada = new DataInputStream(socket.getInputStream());
            String m = flujoentrada.readUTF();
            //System.out.println(m);

            initComponents();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido establecer la conexión", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    class ModeloTablaClientes extends AbstractTableModel {

        String[] columnNames = {"NOMBRE",
            "APELLIDOS",
            "CORREO",
            "BORRAR"};

        private Object[][] data = {};

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        //@Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public String getColumnName(int c) {
            return columnNames[c];
        }

        @Override
        public Class getColumnClass(int colum) {
            return getValueAt(0, colum).getClass();
        }

        public void refreshTableModel(TreeSet<Cliente> lista) {

            if (!lista.isEmpty()) {
                int numCol = columnNames.length + 1;
                int numFilas = lista.size();

                data = new Object[numFilas][numCol];
                ImageIcon icono = new ImageIcon("icon.png");
                ImageIcon iconoEscala = new ImageIcon(icono.getImage().getScaledInstance(18, -1, java.awt.Image.SCALE_DEFAULT));

                int i = 0;
                for (Cliente c : lista) {
                    data[i][0] = c.getNombre();
                    data[i][1] = c.getApellidos();
                    data[i][2] = c.getCorreo();
                    data[i][3] = new JButton(iconoEscala);
                    data[i][4] = c;
                    final Cliente c2 = c;
                    ((JButton) data[i][3]).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            int opcion = JOptionPane.showConfirmDialog(null, "¿Quiéres borrar al cliente: " + c.getNombre() + "?",
                                    "",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                            if (opcion == JOptionPane.YES_OPTION) {
                                borrarCliente(c2);
                                refrescarTable();
                            } else if (opcion == JOptionPane.NO_OPTION) {
                                JOptionPane.showMessageDialog(null, "No hemos borrado a" + c.getNombre() + ".", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    i++;
                }
            } else {
                data = new Object[0][0];
            }

            fireTableDataChanged();
        }

    }

    class ModeloTablaReservas extends AbstractTableModel {

        String[] columnNames = {"USUARIO",
            "PISTA",
            "HORARIO",
            "FECHA",
            "JUGADORES",
            "BORRAR"};

        private Object[][] data = {};

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        //@Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public String getColumnName(int c) {
            return columnNames[c];
        }

        @Override
        public Class getColumnClass(int colum) {
            return getValueAt(0, colum).getClass();
        }

        public void refreshTableModel(List<Reserva> lista) {

            if (!lista.isEmpty()) {
                int numCol = columnNames.length + 1;
                int numFilas = lista.size();

                data = new Object[numFilas][numCol];
                ImageIcon icono = new ImageIcon("icon.png");
                ImageIcon iconoEscala = new ImageIcon(icono.getImage().getScaledInstance(18, -1, java.awt.Image.SCALE_DEFAULT));

                for (int i = 0; i < numFilas; i++) {
                    data[i][0] = lista.get(i).getNombre();
                    data[i][1] = lista.get(i).getPista();
                    data[i][2] = lista.get(i).getHorario();
                    data[i][3] = lista.get(i).getFecha();
                    data[i][4] = lista.get(i).getJugadores();
                    data[i][5] = new JButton(iconoEscala);
                    data[i][6] = lista.get(i);
                    final Reserva r = lista.get(i);
                    ((JButton) data[i][5]).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            int opcion = JOptionPane.showConfirmDialog(null, "¿Quiéres borrar la reserva de: " + r.getNombre() + "?",
                                    "",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                            if (opcion == JOptionPane.YES_OPTION) {
                                borrarReserva(r);
                                refrescarTable();
                            } else if (opcion == JOptionPane.NO_OPTION) {
                                JOptionPane.showMessageDialog(null, "No hemos borrado la reserva de " + r.getNombre() + ".", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                }
            } else {
                data = new Object[0][0];
            }

            fireTableDataChanged();
        }

    }

    class ModeloTablaDisponibilidad extends AbstractTableModel {

        String[] columnNames = {"PISTA",
            "HORARIO"};

        private Object[][] data = {};

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public String getColumnName(int c) {
            return columnNames[c];
        }

        public void refreshTableModel(List<PistaDisponible> lista) {

            if (!lista.isEmpty()) {
                int numCol = columnNames.length + 1;
                int numFilas = lista.size();

                data = new Object[numFilas][numCol];
                int i = 0;
                for (PistaDisponible p : lista) {
                    data[i][0] = p.getId_pista();
                    data[i][1] = p.getFranja();
                    data[i][2] = p;
                    final PistaDisponible pista = p;
                    //System.out.println(pista.toString());
                    i++;
                }
            } else {
                data = new Object[0][0];
            }

            //fireTableDataChanged();
        }

    }

    public void refrescarTable() {
        if (tabla_clientes_seleccionada) {
            modeloTablaClientes.refreshTableModel(getClientes());
            jTable.setModel(modeloTablaClientes);
            jTextFieldBarraBusqueda.setText("");
            jLabelTitulo.setText("Clientes");
        } else {
            modeloTablaReservas.refreshTableModel(getReservas());
            jTable.setModel(modeloTablaReservas);
            jTextFieldBarraBusqueda.setText("");
            jLabelTitulo.setText("Reservas");
        }
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        jTable.getColumn("BORRAR").setCellRenderer(buttonRenderer);
    }

    private void refrescarTableClientes() {
        modeloTablaClientes.refreshTableModel(getClientes(jTextFieldBarraBusqueda.getText()));
        jTable.setModel(modeloTablaClientes);
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        jTable.getColumn("BORRAR").setCellRenderer(buttonRenderer);
    }

    private void refrescarTalaReservas() {
        modeloTablaReservas.refreshTableModel(getReservas(jTextFieldBarraBusqueda.getText()));
        jTable.setModel(modeloTablaReservas);
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        jTable.getColumn("BORRAR").setCellRenderer(buttonRenderer);
    }

    private static class JTableButtonRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton) value;
            return button;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogNuevoUsuario = new javax.swing.JDialog();
        nuevoUsuario = new escritorio.NuevoUsuario();
        jDialogLogin = new javax.swing.JDialog();
        login = new escritorio.Login();
        jDialogNuevoPartido = new javax.swing.JDialog();
        nuevoPartido = new escritorio.NuevoPartido();
        jMenu1 = new javax.swing.JMenu();
        jDialogDisponibilidad = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDisponibilidad = new javax.swing.JTable();
        jPanelVentanaPrincipal = new javax.swing.JPanel();
        jLabelMuvon = new javax.swing.JLabel();
        jButtonReservas = new javax.swing.JButton();
        jButtonClientes = new javax.swing.JButton();
        jButtonAñadirPartido = new javax.swing.JButton();
        jButtonAñadirCliente = new javax.swing.JButton();
        jLabelTitulo = new javax.swing.JLabel();
        jTextFieldBarraBusqueda = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu = new javax.swing.JMenu();
        jMenuItemCerrarSesion = new javax.swing.JMenuItem();

        jDialogNuevoUsuario.setTitle("Nuevo Usuario");
        jDialogNuevoUsuario.setModal(true);
        jDialogNuevoUsuario.setResizable(false);

        nuevoUsuario.setParent(this);

        javax.swing.GroupLayout jDialogNuevoUsuarioLayout = new javax.swing.GroupLayout(jDialogNuevoUsuario.getContentPane());
        jDialogNuevoUsuario.getContentPane().setLayout(jDialogNuevoUsuarioLayout);
        jDialogNuevoUsuarioLayout.setHorizontalGroup(
            jDialogNuevoUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(nuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jDialogNuevoUsuarioLayout.setVerticalGroup(
            jDialogNuevoUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(nuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogLogin.setTitle("Login");
        jDialogLogin.setMinimumSize(new java.awt.Dimension(325, 260));
        jDialogLogin.setModal(true);
        jDialogLogin.setResizable(false);

        login.setParent(this);

        javax.swing.GroupLayout jDialogLoginLayout = new javax.swing.GroupLayout(jDialogLogin.getContentPane());
        jDialogLogin.getContentPane().setLayout(jDialogLoginLayout);
        jDialogLoginLayout.setHorizontalGroup(
            jDialogLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jDialogLoginLayout.setVerticalGroup(
            jDialogLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogNuevoPartido.setMinimumSize(new java.awt.Dimension(345, 352));

        nuevoPartido.setParent(this);

        javax.swing.GroupLayout jDialogNuevoPartidoLayout = new javax.swing.GroupLayout(jDialogNuevoPartido.getContentPane());
        jDialogNuevoPartido.getContentPane().setLayout(jDialogNuevoPartidoLayout);
        jDialogNuevoPartidoLayout.setHorizontalGroup(
            jDialogNuevoPartidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogNuevoPartidoLayout.createSequentialGroup()
                .addComponent(nuevoPartido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jDialogNuevoPartidoLayout.setVerticalGroup(
            jDialogNuevoPartidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogNuevoPartidoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(nuevoPartido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenu1.setText("jMenu1");

        jDialogDisponibilidad.setTitle("Escoge un horario");

        jScrollPane1.setMinimumSize(new java.awt.Dimension(302, 300));
        jScrollPane1.setName(""); // NOI18N
        jScrollPane1.setOpaque(false);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(302, 300));

        jTableDisponibilidad.setModel(modeloTablaDisponibilidad);
        jTableDisponibilidad.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jTableDisponibilidad.setMinimumSize(new java.awt.Dimension(302, 300));
        jTableDisponibilidad.setName(""); // NOI18N
        jTableDisponibilidad.setPreferredSize(new java.awt.Dimension(302, 300));
        jTableDisponibilidad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDisponibilidadMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableDisponibilidad);

        javax.swing.GroupLayout jDialogDisponibilidadLayout = new javax.swing.GroupLayout(jDialogDisponibilidad.getContentPane());
        jDialogDisponibilidad.getContentPane().setLayout(jDialogDisponibilidadLayout);
        jDialogDisponibilidadLayout.setHorizontalGroup(
            jDialogDisponibilidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
        );
        jDialogDisponibilidadLayout.setVerticalGroup(
            jDialogDisponibilidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogDisponibilidadLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelVentanaPrincipal.setBackground(new java.awt.Color(61, 138, 247));

        jLabelMuvon.setFont(new java.awt.Font("Roboto Medium", 1, 64)); // NOI18N
        jLabelMuvon.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMuvon.setText("MUVON");

        jButtonReservas.setBackground(new java.awt.Color(187, 187, 187));
        jButtonReservas.setForeground(new java.awt.Color(0, 0, 0));
        jButtonReservas.setText("Reservas");
        jButtonReservas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReservasActionPerformed(evt);
            }
        });

        jButtonClientes.setBackground(new java.awt.Color(187, 187, 187));
        jButtonClientes.setForeground(new java.awt.Color(0, 0, 0));
        jButtonClientes.setText("Clientes");
        jButtonClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClientesActionPerformed(evt);
            }
        });

        jButtonAñadirPartido.setBackground(new java.awt.Color(187, 187, 187));
        jButtonAñadirPartido.setForeground(new java.awt.Color(0, 0, 0));
        jButtonAñadirPartido.setText("Añadir Partido");
        jButtonAñadirPartido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAñadirPartidoActionPerformed(evt);
            }
        });

        jButtonAñadirCliente.setBackground(new java.awt.Color(187, 187, 187));
        jButtonAñadirCliente.setForeground(new java.awt.Color(0, 0, 0));
        jButtonAñadirCliente.setText("Añadir Cliente");
        jButtonAñadirCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAñadirClienteActionPerformed(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Chandas", 1, 24)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 0));
        jLabelTitulo.setText("Clientes");

        jButtonBuscar.setText("Buscar");
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable);

        javax.swing.GroupLayout jPanelVentanaPrincipalLayout = new javax.swing.GroupLayout(jPanelVentanaPrincipal);
        jPanelVentanaPrincipal.setLayout(jPanelVentanaPrincipalLayout);
        jPanelVentanaPrincipalLayout.setHorizontalGroup(
            jPanelVentanaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelVentanaPrincipalLayout.createSequentialGroup()
                .addGroup(jPanelVentanaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelVentanaPrincipalLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanelVentanaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelVentanaPrincipalLayout.createSequentialGroup()
                                .addComponent(jLabelMuvon)
                                .addGap(215, 215, 215)
                                .addComponent(jLabelTitulo))
                            .addGroup(jPanelVentanaPrincipalLayout.createSequentialGroup()
                                .addGroup(jPanelVentanaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonReservas)
                                    .addComponent(jButtonClientes)
                                    .addComponent(jButtonAñadirPartido)
                                    .addComponent(jButtonAñadirCliente))
                                .addGap(41, 41, 41)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanelVentanaPrincipalLayout.createSequentialGroup()
                        .addGap(388, 388, 388)
                        .addComponent(jTextFieldBarraBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButtonBuscar)))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jPanelVentanaPrincipalLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonAñadirCliente, jButtonAñadirPartido, jButtonClientes, jButtonReservas});

        jPanelVentanaPrincipalLayout.setVerticalGroup(
            jPanelVentanaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelVentanaPrincipalLayout.createSequentialGroup()
                .addGroup(jPanelVentanaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelVentanaPrincipalLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jLabelMuvon))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelVentanaPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelTitulo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelVentanaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBarraBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanelVentanaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelVentanaPrincipalLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jButtonClientes)
                        .addGap(26, 26, 26)
                        .addComponent(jButtonReservas)
                        .addGap(28, 28, 28)
                        .addComponent(jButtonAñadirPartido)
                        .addGap(27, 27, 27)
                        .addComponent(jButtonAñadirCliente))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );

        jPanelVentanaPrincipalLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonAñadirCliente, jButtonAñadirPartido, jButtonClientes, jButtonReservas});

        jMenu.setText("Opciones");

        jMenuItemCerrarSesion.setText("Cerrar sesión");
        jMenuItemCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCerrarSesionActionPerformed(evt);
            }
        });
        jMenu.add(jMenuItemCerrarSesion);

        jMenuBar1.add(jMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelVentanaPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelVentanaPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonReservasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReservasActionPerformed
        // TODO add your handling code here:
        this.tabla_clientes_seleccionada = false;
        jLabelTitulo.setText("Reservas");
        refrescarTable();
    }//GEN-LAST:event_jButtonReservasActionPerformed

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        if (!jTextFieldBarraBusqueda.getText().equals(""))
            if (tabla_clientes_seleccionada) {
                refrescarTableClientes();
            } else {
                refrescarTalaReservas();
            }
        else
            refrescarTable();
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    private void jButtonAñadirClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAñadirClienteActionPerformed
        limpiarNuevoUsuario();

        jDialogNuevoUsuario.setSize(384, 720);
        jDialogNuevoUsuario.setLocationRelativeTo(null);
        jDialogNuevoUsuario.setVisible(true);
    }//GEN-LAST:event_jButtonAñadirClienteActionPerformed

    private void jButtonAñadirPartidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAñadirPartidoActionPerformed
        try {
            String email = JOptionPane.showInputDialog("Email del usuario");
            if (email != null) {
                if (!email.equals("")) {
                    Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                    Matcher mather = pattern.matcher(email);

                    if (mather.find() == true) {
                        flujosalida.writeUTF("3," + email);
                        String mensaje = flujoentrada.readUTF();
                        clienteReserva = new Cliente(mensaje + "," + email);

                        if (mensaje.equals("No se ha encontrado ningún usuario con ese nombre")) {
                            JOptionPane.showMessageDialog(null, "El email introducido no es válido", "ERROR",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (JOptionPane.showConfirmDialog(null, "La reserva se hará a nombre de: " + clienteReserva.getNombre() + " " + clienteReserva.getApellidos() + ", estás de acuerdo?", "ADVERTENCIA",
                                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                limpiarNuevoPartido();
                                jDialogNuevoPartido.setSize(345, 352);
                                jDialogNuevoPartido.setLocationRelativeTo(null);
                                jDialogNuevoPartido.setVisible(true);
                            } else {
                                email = "";
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "El email introducido no es válido", "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonAñadirPartidoActionPerformed

    private void jButtonClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClientesActionPerformed
        // TODO add your handling code here:
        tabla_clientes_seleccionada = true;
        jLabelTitulo.setText("Clientes");
        refrescarTable();
    }//GEN-LAST:event_jButtonClientesActionPerformed

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        // TODO add your handling code here:
        int column = jTable.getColumnModel().getColumnIndexAtX(evt.getX()); // get the coloum of the button
        int row = evt.getY() / jTable.getRowHeight(); //get the row of the button

        /*Checking the row or column is valid or not*/
        if (row < jTable.getRowCount() && row >= 0 && column < jTable.getColumnCount() && column >= 0) {
            Object value = jTable.getValueAt(row, column);
            if (value instanceof JButton) {
                ((JButton) value).doClick();
                refrescarTable();
            }
        }
    }//GEN-LAST:event_jTableMouseClicked

    private void jMenuItemCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCerrarSesionActionPerformed
        logeado = false;
        solicitarLogin();
    }//GEN-LAST:event_jMenuItemCerrarSesionActionPerformed

    private void jTableDisponibilidadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDisponibilidadMouseClicked
        int row = evt.getY() / jTableDisponibilidad.getRowHeight(); //get the row of the button

        pistaEscogida = (PistaDisponible) modeloTablaDisponibilidad.getValueAt(row, 2);
        nuevoPartido.getjButtonReservar().setEnabled(true);
        jDialogDisponibilidad.setVisible(false);
    }//GEN-LAST:event_jTableDisponibilidadMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
        * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaPrincipal v1 = new VentanaPrincipal();
                v1.setLocationRelativeTo(null);
                v1.setVisible(true);

                v1.solicitarLogin();
            }
        });

        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAñadirCliente;
    private javax.swing.JButton jButtonAñadirPartido;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonClientes;
    private javax.swing.JButton jButtonReservas;
    private javax.swing.JDialog jDialogDisponibilidad;
    private javax.swing.JDialog jDialogLogin;
    private javax.swing.JDialog jDialogNuevoPartido;
    private javax.swing.JDialog jDialogNuevoUsuario;
    private javax.swing.JLabel jLabelMuvon;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JMenu jMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemCerrarSesion;
    private javax.swing.JPanel jPanelVentanaPrincipal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable;
    private javax.swing.JTable jTableDisponibilidad;
    private javax.swing.JTextField jTextFieldBarraBusqueda;
    private escritorio.Login login;
    private escritorio.NuevoPartido nuevoPartido;
    private escritorio.NuevoUsuario nuevoUsuario;
    // End of variables declaration//GEN-END:variables

    private void solicitarLogin() {
        jTable.setModel(new DefaultTableModel());
        this.login.getjTextFieldContrasena().setText("");
        while (!logeado) {
            mostrarLogin();
        }
        refrescarTable();
    }

    private void mostrarLogin() {
        jDialogLogin.setLocationRelativeTo(null);
        jDialogLogin.setVisible(true);
    }

    public void crearUsuario() {
        String nombre = nuevoUsuario.getNombre().getText();
        String apellidos = nuevoUsuario.getApellidos().getText();
        String pwd = nuevoUsuario.getPwd1().getText();

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        String email = nuevoUsuario.getCorreo().getText();
        Matcher mather = pattern.matcher(email);

        int sexo = (nuevoUsuario.isjRadioButtonHSelected()) ? 0 : 1;

        int nivelFutbol = nuevoUsuario.getjSliderFutbol().getValue();
        int nivelPadel = nuevoUsuario.getjSliderPadel().getValue();
        int nivelBasket = nuevoUsuario.getjSliderBasket().getValue();
        int nivelBalonmano = nuevoUsuario.getjSliderBalonmano().getValue();

        if (mather.find() == true) {
            if (pwd.equals(nuevoUsuario.getPwd2().getText())) {
                try {
                    flujosalida.writeUTF("2," + nombre + "," + apellidos + "," + email + "," + pwd + "," + sexo + "," + nivelFutbol
                            + "," + nivelPadel + "," + nivelBasket + "," + nivelBalonmano);
                    String mensaje = "";
                    mensaje = flujoentrada.readUTF();
                    //System.out.println("EL MENSAJE: " + mensaje);
                    if (mensaje.equals("Insertado nuevo usuario correctamente")) {
                        JOptionPane.showMessageDialog(null, "Usuario creado");
                        cerrarCreacionUsuario();
                    } else if (mensaje.equals("Correo repetido")) {
                        JOptionPane.showMessageDialog(null, "El email introducido está repetido", "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "", "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (IOException ex) {
                    Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "El email introducido no es válido", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void hacerLogin() {
        try {
            flujosalida.writeUTF("1," + login.getCorreo() + "," + login.getPwd());
            String mensaje = "";
            mensaje = flujoentrada.readUTF();
            //System.out.println("EL MENSAJE: " + mensaje);
            if (mensaje.equals("true")) {
                logeado = true;
                jDialogLogin.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "ERROR");
            }

        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cerrarCreacionPartido() {
        jDialogDisponibilidad.setVisible(false);
        jDialogNuevoPartido.setVisible(false);
    }

    public void cerrarCreacionUsuario() {
        jDialogNuevoUsuario.setVisible(false);
    }

    private TreeSet<Cliente> getClientes() {
        TreeSet<Cliente> lista = new TreeSet<>();
        String[] mensajes = null;
        try {
            flujosalida.writeUTF("4, ");

            String mensaje = "";
            mensaje = flujoentrada.readUTF();
            if (mensaje.equals("No se han encontrado clientes almacenados")) {
                JOptionPane.showMessageDialog(null, "No se han encontrado clientes almacenados");
            } else {
                mensajes = mensaje.split(";");
                for (String arg : mensajes) {
                    lista.add(new Cliente(arg));
                }
            }
            return lista;
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private TreeSet<Cliente> getClientes(String busqueda) {
        TreeSet<Cliente> lista = new TreeSet<>();
        String[] mensajes = null;
        try {
            flujosalida.writeUTF("4," + busqueda);

            String mensaje = "";
            mensaje = flujoentrada.readUTF();
            if (mensaje.equals("No se han encontrado clientes almacenados")) {
                JOptionPane.showMessageDialog(null, "No se han encontrado clientes almacenados");
            } else {
                mensajes = mensaje.split(";");
                for (String arg : mensajes) {
                    lista.add(new Cliente(arg));
                }
            }
            return lista;
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Reserva> getReservas(String text) {
        List<Reserva> lista = new ArrayList<>();
        String[] mensajes = null;
        try {
            flujosalida.writeUTF("19," + text);

            String mensaje = "";
            mensaje = flujoentrada.readUTF();
            if (mensaje.equals("No se han encontrado reservas almacenadas")) {
                JOptionPane.showMessageDialog(null, "No se han encontrado clientes almacenados");
            } else {
                mensajes = mensaje.split(";");
                for (String arg : mensajes) {
                    lista.add(new Reserva(arg));
                }
            }
            return lista;
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Reserva> getReservas() {
        List<Reserva> lista = new ArrayList<>();
        String[] mensajes = null;
        try {
            flujosalida.writeUTF("22,");

            String mensaje = "";
            mensaje = flujoentrada.readUTF();
            if (mensaje.equals("No se han encontrado reservas almacenadas")) {
                JOptionPane.showMessageDialog(null, "No se han encontrado reservas almacenadas");
            } else {
                mensajes = mensaje.split(";");
                for (String arg : mensajes) {
                    lista.add(new Reserva(arg));
                }
            }
            return lista;
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void borrarCliente(Cliente c) {
        try {
            flujosalida.writeUTF("5," + c.getCorreo());
            String mensaje = flujoentrada.readUTF();
            if (mensaje.equals("OK")) {
                JOptionPane.showMessageDialog(null, "Se ha borrado el cliente correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error al borrar el cliente");
            }
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        refrescarTable();
    }

    private void borrarReserva(Reserva r) {
        try {
            flujosalida.writeUTF("7," + r.getId());
            String mensaje = flujoentrada.readUTF();
            if (mensaje.equals("OK")) {
                JOptionPane.showMessageDialog(null, "Se ha borrado la reserva correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error al borrar la reserva");
            }
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        refrescarTable();
    }

    private List<PistaDisponible> getDisponibilidad(int deporte_id, Date date) {
        List<PistaDisponible> listaPistasOcupadas = new ArrayList<>();
        List<PistaDisponible> lista = new ArrayList<>();
        String[] mensajes = null;
        try {
            flujosalida.writeUTF("8," + deporte_id + "," + date);

            String mensaje = "";
            mensaje = flujoentrada.readUTF();
            mensajes = mensaje.split(";");

            if (!mensaje.equals("No se ha encontrado ninguna reserva ese día")) {

                for (String arg : mensajes) {
                    listaPistasOcupadas.add(new PistaDisponible(arg));
                }

                List<Integer> pistasDeportivas = getPistasDeportivas(deporte_id);
                for (int i = 1; i < 7; i++) {
                    for (Integer id : pistasDeportivas) {
                        lista.add(new PistaDisponible(id, i));
                    }
                }

                List<PistaDisponible> listaRepetidas = new ArrayList<>();

                for (PistaDisponible p : listaPistasOcupadas) {
                    for (PistaDisponible p2 : lista) {
                        if (p2.getId_pista() == p.getId_pista() && p2.getId_horario() == p.getId_horario()) {
                            listaRepetidas.add(p2);
                        }
                    }
                }

                for (PistaDisponible p : listaRepetidas) {
                    lista.remove(p);
                }

            } else {
                for (Integer id : getPistasDeportivas(deporte_id)) {
                    for (int i = 1; i < 7; i++) {
                        lista.add(new PistaDisponible(id, i));
                    }
                }
            }
            return lista;

        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void crearReserva(Date date, Integer max, Integer base) {
        try {
            flujosalida.writeUTF("9," + clienteReserva.getCorreo() + "," + pistaEscogida.getId_pista() + "," + pistaEscogida.getId_horario()
                    + "," + date + "," + max + "," + base);
            String mensaje = flujoentrada.readUTF();
            if (mensaje.equals("OK")) {
                JOptionPane.showMessageDialog(null, "Se ha creado la reserva correctamente");
                jDialogNuevoPartido.setVisible(false);
                refrescarTable();
            } else {
                JOptionPane.showMessageDialog(null, "Error al crear la reserva");
            }
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refrescarTablaDisponibilidad(int deporte_id, Date date) {
        modeloTablaDisponibilidad.refreshTableModel(getDisponibilidad(deporte_id, date));
        jTableDisponibilidad.setModel(modeloTablaDisponibilidad);

        jDialogDisponibilidad.setSize(302, 300);
        jDialogDisponibilidad.setLocationRelativeTo(null);
        jDialogDisponibilidad.setVisible(true);

        //System.out.println(jTableDisponibilidad.getRowCount());
    }

    private List<Integer> getPistasDeportivas(int deporte_id) {
        List<Integer> lista = new ArrayList<>();
        switch (deporte_id) {
            case 1:
                lista.add(4);
                lista.add(5);
                return lista;
            case 2:
                lista.add(1);
                lista.add(2);
                lista.add(3);
                return lista;
            case 3:
                lista.add(6);
                return lista;
            case 4:
                lista.add(4);
                lista.add(5);
                return lista;
            default:
                return null;
        }
    }

    private void limpiarNuevoPartido() {
        nuevoPartido.getjComboBoxDeportes().setSelectedIndex(0);
        nuevoPartido.getjSpinnerBase().setValue(1);
        nuevoPartido.getjSpinnerMax().setValue(2);
        nuevoPartido.getjButtonReservar().setFocusable(false);
    }

    private void limpiarNuevoUsuario() {
        nuevoUsuario.getNombre().setText("");
        nuevoUsuario.getApellidos().setText("");
        nuevoUsuario.getCorreo().setText("");
        nuevoUsuario.getPwd1().setText("");
        nuevoUsuario.getPwd2().setText("");
        nuevoUsuario.getjSliderBalonmano().setValue(0);
        nuevoUsuario.getjSliderBasket().setValue(0);
        nuevoUsuario.getjSliderPadel().setValue(0);
        nuevoUsuario.getjSliderFutbol().setValue(0);
    }
}

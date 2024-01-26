/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package form;

import com.formdev.flatlaf.FlatIntelliJLaf;
import config.koneksi;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Rifal
 */
public class Dialog_DataSupplier extends javax.swing.JDialog {

    private boolean supplierSelected = false;
    private String selectedIdSupplier = null;
    private String selectedNamaToko = null;
    

    public boolean isSupplierSelected() {
        return supplierSelected;
    }

   
    public String getSelectedIdSupplier() {
        return selectedIdSupplier;
    }

    public String getSelectedNamaToko() {
        return selectedNamaToko;
    }
private void pilihBaris() {
    int selectedRow = tbl_supplierdialog.getSelectedRow();
    if (selectedRow != -1) {
        selectedIdSupplier = tbl_supplierdialog.getValueAt(selectedRow, 0).toString();
        selectedNamaToko = tbl_supplierdialog.getValueAt(selectedRow, 2).toString();

        supplierSelected = true;
        this.dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu.");
    }
}

    private void autoResizeAllColumns() {
        int columns = tbl_supplierdialog.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_supplierdialog.getColumnModel().getColumn(i);
            int width = (int) tbl_supplierdialog.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_supplierdialog, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_supplierdialog.getRowCount(); row++) {
                int preferedWidth = (int) tbl_supplierdialog.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_supplierdialog, tbl_supplierdialog.getValueAt(row, i), false, false, row, i)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            column.setPreferredWidth(width);
        }
    }

    private void loadData() {
        DefaultTableModel supModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        supModel.addColumn("ID Supplier");
        supModel.addColumn("Nama Supplier");
        supModel.addColumn("Toko Supplier");

        tbl_supplierdialog.setAutoCreateRowSorter(true); 
        autoResizeAllColumns(); 

        try {
            String sql = "SELECT * FROM supplier";
            Connection mysqlconfig = koneksi.getConnection();
            java.sql.PreparedStatement pst = mysqlconfig.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                supModel.addRow(new Object[]{
                    rs.getString("idsupplier"),
                    rs.getString("namasupplier"),
                    rs.getString("tokosupplier")

                });
            }
            tbl_supplierdialog.setModel(supModel);

            
            for (int i = 0; i < supModel.getColumnCount(); i++) {
                tbl_supplierdialog.getColumnModel().getColumn(i).setCellEditor(null);
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

            for (int i = 0; i < supModel.getColumnCount(); i++) {
                tbl_supplierdialog.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    private void filterDataSupplier(String keyword, String selectedField) {
        DefaultTableModel supplierModel = (DefaultTableModel) tbl_supplierdialog.getModel();
        supplierModel.setRowCount(0);

        String sql = "SELECT * FROM SUPPLIER WHERE ";

        switch (selectedField) {
            case "ID Supplier":
                sql += "LOWER(IDSUPPLIER) LIKE LOWER(?)";
                break;
            case "Nama Supplier":
                sql += "LOWER(NAMASUPPLIER) LIKE LOWER(?)";
                break;
            case "Toko Supplier":
                sql += "LOWER(TOKOSUPPLIER) LIKE LOWER(?)";
                break;

            default:
                return;
        }

        try (Connection conn = koneksi.getConnection(); PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, "%" + keyword + "%");

            try (ResultSet res = stm.executeQuery()) {
                int no = 1;
                while (res.next()) {
                    supplierModel.addRow(new Object[]{
                        res.getString("IDSUPPLIER"),
                        res.getString("NAMASUPPLIER"),
                        res.getString("TOKOSUPPLIER")

                    });
                }
                autoResizeAllColumns(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dialog_DataSupplier(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
          tbl_supplierdialog.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                pilihBaris();
            }
        }
    });
        Color headerColor = new Color(194, 217, 255);
        tbl_supplierdialog.getTableHeader().setBackground(headerColor);
        loadData();
        getRootPane().setDefaultButton(btn_pilih);
        txt_cari.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String selectedField = (String) cb_cari.getSelectedItem();
                filterDataSupplier(txt_cari.getText(), selectedField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String selectedField = (String) cb_cari.getSelectedItem();
                filterDataSupplier(txt_cari.getText(), selectedField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_supplierdialog = new javax.swing.JTable();
        btn_pilih = new javax.swing.JButton();
        cb_cari = new javax.swing.JComboBox<>();
        txt_cari = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(182, 210, 244));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Data Supplier");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(166, 166, 166))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        tbl_supplierdialog.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_supplierdialog);

        btn_pilih.setText("Pilih");
        btn_pilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pilihActionPerformed(evt);
            }
        });

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Supplier", "Nama Supplier", "Toko Supplier" }));

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pilih, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btn_pilih)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_pilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pilihActionPerformed
        
        int selectedRow = tbl_supplierdialog.getSelectedRow();
        if (selectedRow != -1) {
            
            selectedIdSupplier = tbl_supplierdialog.getValueAt(selectedRow, 0).toString();
            selectedNamaToko = tbl_supplierdialog.getValueAt(selectedRow, 2).toString();
        
            supplierSelected = true;

            
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu.");
        }

    }//GEN-LAST:event_btn_pilihActionPerformed

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        
    }//GEN-LAST:event_txt_cariActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("Button.arc", 20);
            UIManager.put("Component.arc", 20);
            UIManager.put("ProgressBar.arc", 20);
            UIManager.put("Component.arrowType", "chevron");
            UIManager.put("Component.innerFocusWidth", 0);
            UIManager.put("Button.innerFocusWidth", 0);
            UIManager.put("ScrollBar.trackArc", 999);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.track", new Color(0xe0e0e0));

        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Dialog_DataSupplier dialog = new Dialog_DataSupplier(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_pilih;
    private javax.swing.JComboBox<String> cb_cari;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_supplierdialog;
    private javax.swing.JTextField txt_cari;
    // End of variables declaration//GEN-END:variables
}

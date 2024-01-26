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
import java.sql.Statement;
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
public class Dialog_DataGaji extends javax.swing.JDialog {
    private boolean gajiSelected = false;
    private String selectedidGaji = null;
    private float selectedGajiPokok = 0.0f;
    private float selectedTunjangan = 0.0f;
    private float selectedPotongan = 0.0f;

    
    public boolean isGajiSelected() {
        return gajiSelected;
    }

    
    public String getSelectedidGaji() {
        return selectedidGaji;
    }

    
    public float getSelectedGajiPokok() {
        return selectedGajiPokok;
    }

    
    public float getSelectedTunjangan() {
        return selectedTunjangan;
    }

    
    public float getSelectedPotongan() {
        return selectedPotongan;
    }

    private void autoResizeAllColumns() {
        int columns = tbl_gajidialog.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_gajidialog.getColumnModel().getColumn(i);
            int width = (int) tbl_gajidialog.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_gajidialog, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_gajidialog.getRowCount(); row++) {
                int preferedWidth = (int) tbl_gajidialog.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_gajidialog, tbl_gajidialog.getValueAt(row, i), false, false, row, i)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            column.setPreferredWidth(width);
        }
    }    
   private void loadData() {
    DefaultTableModel gajiModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    gajiModel.addColumn("ID Gaji");
    gajiModel.addColumn("Gaji Pokok");
    gajiModel.addColumn("Tunjangan");
    gajiModel.addColumn("Potongan");

    tbl_gajidialog.setAutoCreateRowSorter(true); 
    autoResizeAllColumns(); 

    try {
        String sql = "SELECT * from gajikaryawan";
        Connection mysqlconfig = koneksi.getConnection();
        Statement stm = mysqlconfig.createStatement();
        java.sql.ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            gajiModel.addRow(new Object[]{
                rs.getString("idgaji"),
                rs.getFloat("gajipokok"),
                rs.getFloat("tunjangan"),
                rs.getFloat("potongan")
            });
        }

        tbl_gajidialog.setModel(gajiModel);

      
        for (int i = 0; i < gajiModel.getColumnCount(); i++) {
            tbl_gajidialog.getColumnModel().getColumn(i).setCellEditor(null);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        for (int i = 0; i < gajiModel.getColumnCount(); i++) {
            tbl_gajidialog.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    } catch (Exception e) {
        e.printStackTrace(); 
    }
}
private void filterData(String keyword, String selectedField) {
    DefaultTableModel gajiModel = (DefaultTableModel) tbl_gajidialog.getModel();
    gajiModel.setRowCount(0);

    String sql = "SELECT * FROM GAJIKARYAWAN WHERE ";

    switch (selectedField) {
        case "ID":
            sql += "LOWER(IDGAJI) LIKE LOWER(?)";
            break;
        default:
            return;
    }

    try (Connection conn = koneksi.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {

        stm.setString(1, "%" + keyword + "%");

        try (ResultSet res = stm.executeQuery()) {
            int no = 1;
            while (res.next()) {
                gajiModel.addRow(new Object[]{
                        res.getString("IDGAJI"),
                        res.getDouble("GAJIPOKOK"),
                        res.getDouble("TUNJANGAN"),
                        res.getDouble("POTONGAN")
                });
            }
            autoResizeAllColumns(); 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    public Dialog_DataGaji(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
          tbl_gajidialog.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                pilihBaris();
            }
        }
    });
        Color headerColor = new Color(194, 217, 255);
        tbl_gajidialog.getTableHeader().setBackground(headerColor);
        loadData();
        getRootPane().setDefaultButton(btn_pilih);
           txt_cari.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        String selectedField = (String) cb_cari.getSelectedItem();
        filterData(txt_cari.getText(), selectedField);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        String selectedField = (String) cb_cari.getSelectedItem();
        filterData( txt_cari.getText(), selectedField);
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
        tbl_gajidialog = new javax.swing.JTable();
        btn_pilih = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txt_cari = new javax.swing.JTextField();
        cb_cari = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(182, 210, 244));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Data Gaji");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(225, 225, 225)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        tbl_gajidialog.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_gajidialog);

        btn_pilih.setText("Pilih");
        btn_pilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pilihActionPerformed(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pilih, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btn_pilih)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_pilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pilihActionPerformed
      
int selectedRow = tbl_gajidialog.getSelectedRow();
if (selectedRow != -1) {
    
    selectedidGaji = tbl_gajidialog.getValueAt(selectedRow, 0).toString();
    selectedGajiPokok = Float.parseFloat(tbl_gajidialog.getValueAt(selectedRow, 1).toString()); 
    selectedTunjangan = Float.parseFloat(tbl_gajidialog.getValueAt(selectedRow, 2).toString());
    selectedPotongan = Float.parseFloat(tbl_gajidialog.getValueAt(selectedRow, 3).toString()); 

    
    gajiSelected = true;

   
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
                Dialog_DataGaji dialog = new Dialog_DataGaji(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_gajidialog;
    private javax.swing.JTextField txt_cari;
    // End of variables declaration//GEN-END:variables
private void pilihBaris() {
    int selectedRow = tbl_gajidialog.getSelectedRow();
    if (selectedRow != -1) {
        selectedidGaji = tbl_gajidialog.getValueAt(selectedRow, 0).toString();
        selectedGajiPokok = Float.parseFloat(tbl_gajidialog.getValueAt(selectedRow, 1).toString());
        selectedTunjangan = Float.parseFloat(tbl_gajidialog.getValueAt(selectedRow, 2).toString());
        selectedPotongan = Float.parseFloat(tbl_gajidialog.getValueAt(selectedRow, 3).toString());

        gajiSelected = true;
        this.dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu.");
    }
}

}

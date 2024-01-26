/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package form;

import config.koneksi;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Rifal
 */
public class Form_BahanBaku extends javax.swing.JPanel {

    private void autoResizeAllColumns() {
        int columns = tbl_bahanbaku.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_bahanbaku.getColumnModel().getColumn(i);
            int width = (int) tbl_bahanbaku.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_bahanbaku, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_bahanbaku.getRowCount(); row++) {
                int preferedWidth = (int) tbl_bahanbaku.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_bahanbaku, tbl_bahanbaku.getValueAt(row, i), false, false, row, i)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            column.setPreferredWidth(width);
        }
    }

    private void loadData() {
        DefaultTableModel bahanBakuModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        bahanBakuModel.addColumn("No");
        bahanBakuModel.addColumn("ID Bahan");
        bahanBakuModel.addColumn("Nama Bahan");
        bahanBakuModel.addColumn("Stock Bahan");

        tbl_bahanbaku.setAutoCreateRowSorter(true); 

        try {
            int no = 1;
            String sql = "SELECT * FROM BAHANBAKU"; 
            java.sql.Connection mysqlconfig = (Connection) koneksi.getConnection();
            java.sql.Statement stm = mysqlconfig.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                bahanBakuModel.addRow(new Object[]{no++,
                    res.getString("IDBAHAN"),
                    res.getString("NAMABAHAN"),
                    res.getInt("STOCKBAHAN")});
            }
            tbl_bahanbaku.setModel(bahanBakuModel);
            autoResizeAllColumns();

            
            for (int i = 0; i < bahanBakuModel.getColumnCount(); i++) {
                tbl_bahanbaku.getColumnModel().getColumn(i).setCellEditor(null);
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

           
            for (int i = 0; i < bahanBakuModel.getColumnCount(); i++) {
                tbl_bahanbaku.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        } catch (Exception e) {
            
        }
    }

    private void tambahData() {
        try {
            
            if (txt_idbahan.getText().isEmpty()
                    || txt_namabahan.getText().isEmpty() || txt_stockbahan.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menyimpan data.");
                return;
            }

            String sql = "INSERT INTO BAHANBAKU (IDBAHAN, NAMABAHAN, STOCKBAHAN) VALUES (?, ?, ?)";
            Connection mysqlconfig = koneksi.getConnection();
            PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, txt_idbahan.getText());
            pst.setString(2, txt_namabahan.getText());
            pst.setInt(3, Integer.parseInt(txt_stockbahan.getText()));

            pst.execute();
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        loadData();
        bersihform();
    }

   private void hapusData() {
    int selectedRow = tbl_bahanbaku.getSelectedRow();

    if (selectedRow != -1) {
        String idToDelete = tbl_bahanbaku.getValueAt(selectedRow, 1).toString();

        int confirmResult = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data dengan ID Bahan " + idToDelete + "?",
                "Konfirmasi Hapus Data",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmResult == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM BAHANBAKU WHERE IDBAHAN=?";
                Connection mysqlconfig = koneksi.getConnection();
                java.sql.PreparedStatement pst = mysqlconfig.prepareStatement(sql);
                pst.setString(1, idToDelete);
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data with ID Bahan " + idToDelete + " has been deleted.");
                    loadData();
                    bersihform();
                } else {
                    JOptionPane.showMessageDialog(this, "Data with ID Bahan " + idToDelete + " not found.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Pilih baris untuk dihapus.");
    }
}



    private void ubahData() {
        try {
            
            if (txt_idbahan.getText().isEmpty() 
                    || txt_namabahan.getText().isEmpty() || txt_stockbahan.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum mengubah data.");
                return;
            }

            String sql = "UPDATE BAHANBAKU SET IDBAHAN=?, NAMABAHAN=?, STOCKBAHAN=? WHERE IDBAHAN=?";
            java.sql.Connection mysqlconfig = koneksi.getConnection();
            java.sql.PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, txt_idbahan.getText());
            pst.setString(2, txt_namabahan.getText());
            pst.setInt(3, Integer.parseInt(txt_stockbahan.getText()));
            pst.setString(4, txt_idbahan.getText());

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang cocok dengan ID Bahan yang diberikan.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Perubahan data gagal: " + e.getMessage());
        }
        loadData();
        bersihform();
    }

    private void bersihform() {
        txt_idbahan.setText("");
        txt_namabahan.setText("");
        txt_stockbahan.setText("");
    }
private void filterData(String keyword, String selectedField) {
    DefaultTableModel bahanModel = (DefaultTableModel) tbl_bahanbaku.getModel();
    bahanModel.setRowCount(0); 

    String sql = "SELECT * FROM BAHANBAKU WHERE ";

    switch (selectedField) {
        case "ID":
            sql += "LOWER(IDBAHAN) LIKE LOWER(?)";
            break;
        case "Nama":
            sql += "LOWER(NAMABAHAN) LIKE LOWER(?)";
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
                bahanModel.addRow(new Object[]{
                        no++,
                        res.getString("IDBAHAN"),
                        res.getString("NAMABAHAN"),
                        res.getInt("STOCKBAHAN")
                });
            }
            autoResizeAllColumns();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    /**
     * Creates new form Form_Customer
     */
    public Form_BahanBaku() {
        initComponents();
        btn_hapus.setVisible(false);
                btn_batal.setVisible(false);
        Color headerColor = new Color(194, 217, 255);
        tbl_bahanbaku.getTableHeader().setBackground(headerColor);
        loadData();
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

        mainPanel = new javax.swing.JPanel();
        dataBahanBaku = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_bahanbaku = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txt_cari = new javax.swing.JTextField();
        cb_cari = new javax.swing.JComboBox<>();
        tambahBahanBaku = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_idbahan = new javax.swing.JTextField();
        txt_namabahan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_stockbahan = new javax.swing.JTextField();
        btn_ubah = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();
        btn_clear = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        dataBahanBaku.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tbl_bahanbaku.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_bahanbaku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_bahanbakuMouseClicked(evt);
            }
        });
        tbl_bahanbaku.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_bahanbakuKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_bahanbaku);

        jLabel1.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel1.setText("Data Bahan Baku");

        btn_tambah.setBackground(new java.awt.Color(95, 189, 255));
        btn_tambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icnbtn/icons8_add_20px.png"))); // NOI18N
        btn_tambah.setText("Tambah");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_hapus.setBackground(new java.awt.Color(239, 64, 64));
        btn_hapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icnbtn/icons8_delete_20px.png"))); // NOI18N
        btn_hapus.setText("Hapus");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        btn_batal.setBackground(new java.awt.Color(221, 242, 253));
        btn_batal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icnbtn/icons8_cancel_20px_1.png"))); // NOI18N
        btn_batal.setText("Batal");
        btn_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_batalActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nama" }));

        javax.swing.GroupLayout dataBahanBakuLayout = new javax.swing.GroupLayout(dataBahanBaku);
        dataBahanBaku.setLayout(dataBahanBakuLayout);
        dataBahanBakuLayout.setHorizontalGroup(
            dataBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataBahanBakuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                    .addGroup(dataBahanBakuLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataBahanBakuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        dataBahanBakuLayout.setVerticalGroup(
            dataBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataBahanBakuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel8)
                    .addGroup(dataBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(dataBahanBaku, "card2");

        tambahBahanBaku.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Tambah Bahan Baku");

        jLabel3.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel3.setText("ID Bahan");

        txt_idbahan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        txt_namabahan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel5.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel5.setText("Nama Bahan");

        jLabel6.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel6.setText("Stock Bahan");

        txt_stockbahan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        btn_ubah.setBackground(new java.awt.Color(95, 189, 255));
        btn_ubah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icnbtn/icons8_save_20px.png"))); // NOI18N
        btn_ubah.setText("Ubah");
        btn_ubah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ubahActionPerformed(evt);
            }
        });

        btn_back.setBackground(new java.awt.Color(221, 242, 253));
        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icnbtn/icons8_cancel_20px_1.png"))); // NOI18N
        btn_back.setText("Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        btn_clear.setBackground(new java.awt.Color(255, 229, 229));
        btn_clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icnbtn/icons8_broom_20px.png"))); // NOI18N
        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tambahBahanBakuLayout = new javax.swing.GroupLayout(tambahBahanBaku);
        tambahBahanBaku.setLayout(tambahBahanBakuLayout);
        tambahBahanBakuLayout.setHorizontalGroup(
            tambahBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahBahanBakuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tambahBahanBakuLayout.createSequentialGroup()
                        .addGroup(tambahBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txt_idbahan, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(tambahBahanBakuLayout.createSequentialGroup()
                        .addGroup(tambahBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6)
                            .addComponent(txt_stockbahan, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txt_namabahan, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(267, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahBahanBakuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        tambahBahanBakuLayout.setVerticalGroup(
            tambahBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahBahanBakuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(40, 40, 40)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_idbahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_namabahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_stockbahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addGroup(tambahBahanBakuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(tambahBahanBaku, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
    
        
        if (btn_tambah.getText().equals("Tambah")) {
            btn_ubah.setText("Tambah");
            txt_idbahan.setEditable(true);
       btn_clear.setVisible(true);
        } else if (btn_tambah.getText().equals("Edit")) {
            btn_ubah.setText("Ubah");
            txt_idbahan.setEditable(false);
       btn_clear.setVisible(false);          
        }
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(tambahBahanBaku);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
    
        hapusData();
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed
        btn_tambah.setText("Tambah");
        btn_hapus.setVisible(false);
                btn_batal.setVisible(false);   
        loadData();
        bersihform();
    }//GEN-LAST:event_btn_batalActionPerformed

    private void btn_ubahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ubahActionPerformed
        if (btn_ubah.getText().equals("Ubah")) {
            
            ubahData();
        } else if (btn_ubah.getText().equals("Tambah")) {
            
            tambahData();
        }
    }//GEN-LAST:event_btn_ubahActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
    
        mainPanel.removeAll();
        mainPanel.add(dataBahanBaku);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_backActionPerformed

    private void tbl_bahanbakuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_bahanbakuMouseClicked
btn_hapus.setVisible(true);
btn_batal.setVisible(true);
         btn_tambah.setText("Edit");
        int baris = tbl_bahanbaku.getSelectedRow();
        String idbahan = tbl_bahanbaku.getValueAt(baris, 1).toString();
        txt_idbahan.setText(idbahan);
                String namabahan = tbl_bahanbaku.getValueAt(baris, 2).toString();
        txt_namabahan.setText(namabahan);
        String stockbahan = tbl_bahanbaku.getValueAt(baris, 3).toString();
        txt_stockbahan.setText(stockbahan);
    }//GEN-LAST:event_tbl_bahanbakuMouseClicked

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        
    }//GEN-LAST:event_txt_cariActionPerformed

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
    bersihform();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void tbl_bahanbakuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_bahanbakuKeyPressed
        btn_tambahActionPerformed(null);
    }//GEN-LAST:event_tbl_bahanbakuKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JComboBox<String> cb_cari;
    private javax.swing.JPanel dataBahanBaku;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel tambahBahanBaku;
    private javax.swing.JTable tbl_bahanbaku;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_idbahan;
    private javax.swing.JTextField txt_namabahan;
    private javax.swing.JTextField txt_stockbahan;
    // End of variables declaration//GEN-END:variables
}

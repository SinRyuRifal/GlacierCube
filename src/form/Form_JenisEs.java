/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package form;

import config.koneksi;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.Cursor;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Rifal
 */
public class Form_JenisEs extends javax.swing.JPanel {

    private boolean isjenisesMirip(String jenisEs) {
        try {
            String sql = "SELECT COUNT(*) FROM jenisproduk WHERE jenises=?";
            Connection mysqlconfig = koneksi.getConnection();
            PreparedStatement pst = mysqlconfig.prepareStatement(sql);
            pst.setString(1, jenisEs);
            ResultSet resultSet = pst.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            return count > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }

    private void autoResizeAllColumns() {
        int columns = tbl_jenises.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_jenises.getColumnModel().getColumn(i);
            int width = (int) tbl_jenises.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_jenises, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_jenises.getRowCount(); row++) {
                int preferedWidth = (int) tbl_jenises.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_jenises, tbl_jenises.getValueAt(row, i), false, false, row, i)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            column.setPreferredWidth(width);
        }
    }

    private void loadData() {
        DefaultTableModel brg = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        brg.addColumn("No");
        brg.addColumn("Kode Jenis");
        brg.addColumn("Jenis Es Batu");

        tbl_jenises.setAutoCreateRowSorter(true);

        try {
            int no = 1;
            String sql = "SELECT * FROM jenisproduk";
            java.sql.Connection mysqlconfig = (Connection) koneksi.getConnection();
            java.sql.Statement stm = mysqlconfig.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                brg.addRow(new Object[]{no++,
                    res.getString("kodejenis"),
                    res.getString("jenises")});
            }
            tbl_jenises.setModel(brg);
            autoResizeAllColumns();

            for (int i = 0; i < brg.getColumnCount(); i++) {
                tbl_jenises.getColumnModel().getColumn(i).setCellEditor(null);
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

            for (int i = 0; i < brg.getColumnCount(); i++) {
                tbl_jenises.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        } catch (Exception e) {

        }
    }

    private void tambahData() {
        try {

            if (txt_kodejenis.getText().isEmpty() || txt_jenises.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menyimpan data.");
                return;
            }

            String jenisEs = txt_jenises.getText();
            if (isjenisesMirip(jenisEs)) {
                JOptionPane.showMessageDialog(this, "Jenis es sudah ada. Harap gunakan jenis es yang berbeda.");
                return;
            }

            String sql = "INSERT INTO jenisproduk (kodejenis, jenises) VALUES (?, ?)";
            Connection mysqlconfig = koneksi.getConnection();
            PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, txt_kodejenis.getText());
            pst.setString(2, txt_jenises.getText());

            pst.execute();
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        loadData();
        bersihform();
    }

    private void bersihform() {
        txt_kodejenis.setText("");
        txt_jenises.setText("");
    }

  private void hapusData() {
    int selectedRow = tbl_jenises.getSelectedRow();

    if (selectedRow != -1) {
        String idToDelete = tbl_jenises.getValueAt(selectedRow, 1).toString();

        int confirmResult = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data dengan Kode Jenis " + idToDelete + "?",
                "Konfirmasi Hapus Data",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmResult == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM jenisproduk WHERE kodejenis=?";
                Connection mysqlconfig = koneksi.getConnection();
                java.sql.PreparedStatement pst = mysqlconfig.prepareStatement(sql);
                pst.setString(1, idToDelete);
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data with Kode Jenis " + idToDelete + " has been deleted.");
                    loadData();
                    bersihform();
                } else {
                    JOptionPane.showMessageDialog(this, "Data with Kode Jenis " + idToDelete + " not found.");
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

            if (txt_kodejenis.getText().isEmpty() || txt_jenises.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menyimpan data.");
                return;
            }
            
            
            String kodeJenis = txt_kodejenis.getText();
            String jenisEs = txt_jenises.getText();

            try {

                String sql = "SELECT COUNT(*) FROM jenisproduk WHERE jenises=? AND kodejenis != ?";
                Connection mysqlconfig = koneksi.getConnection();
                PreparedStatement pst = mysqlconfig.prepareStatement(sql);
                pst.setString(1, jenisEs);
                pst.setString(2, kodeJenis);
                ResultSet resultSet = pst.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    JOptionPane.showMessageDialog(this, "Nama barang sudah ada. Harap gunakan nama barang yang berbeda.");
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }

            String sql = "UPDATE barang SET kodejenis = ?, jenises = ? WHERE kodejenis = ?";
            Connection mysqlconfig = koneksi.getConnection();
            PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, txt_kodejenis.getText());
            pst.setString(2, txt_jenises.getText());
            pst.setString(3, txt_kodejenis.getText());

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang cocok dengan ID Barang yang diberikan.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Perubahan data gagal: " + e.getMessage());
        }
        loadData();
        bersihform();
    }

    private void filterData(String keyword, String selectedField) {
        DefaultTableModel jenisProdukModel = (DefaultTableModel) tbl_jenises.getModel();
        jenisProdukModel.setRowCount(0);

        String sql = "SELECT * FROM JENISPRODUK WHERE ";

        switch (selectedField) {
            case "Kode Jenis":
                sql += "LOWER(KODEJENIS) LIKE LOWER(?)";
                break;
            case "Jenis ES":
                sql += "LOWER(JENISES) LIKE LOWER(?)";
                break;
            default:
                return;
        }

        try (Connection conn = koneksi.getConnection(); PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, "%" + keyword + "%");

            try (ResultSet res = stm.executeQuery()) {
                int no = 1;
                while (res.next()) {
                    jenisProdukModel.addRow(new Object[]{
                        no++,
                        res.getString("KODEJENIS"),
                        res.getString("JENISES")
                    });
                }
                autoResizeAllColumns();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Form_JenisEs() {
        initComponents();
        btn_hapus.setVisible(false);
        btn_batal.setVisible(false);
        Color headerColor = new Color(194, 217, 255);
        tbl_jenises.getTableHeader().setBackground(headerColor);
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
                filterData(txt_cari.getText(), selectedField);
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
        dataJenis = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_jenises = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txt_cari = new javax.swing.JTextField();
        cb_cari = new javax.swing.JComboBox<>();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        tambahJenis = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_jenises = new javax.swing.JTextField();
        txt_kodejenis = new javax.swing.JTextField();
        btn_clear = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        dataJenis.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel9.setText("Jenis Es");

        tbl_jenises.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_jenises.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_jenisesMouseClicked(evt);
            }
        });
        tbl_jenises.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_jenisesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_jenises);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kode Jenis", "Jenis ES" }));

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

        javax.swing.GroupLayout dataJenisLayout = new javax.swing.GroupLayout(dataJenis);
        dataJenis.setLayout(dataJenisLayout);
        dataJenisLayout.setHorizontalGroup(
            dataJenisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataJenisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataJenisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dataJenisLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataJenisLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        dataJenisLayout.setVerticalGroup(
            dataJenisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataJenisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataJenisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addGroup(dataJenisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataJenisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(dataJenis, "card2");

        tambahJenis.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Tambah Produk");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel3.setText("Kode Jenis");

        jLabel4.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel4.setText("Jenis Es Batu");

        txt_jenises.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        txt_kodejenis.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_kodejenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kodejenisActionPerformed(evt);
            }
        });
        txt_kodejenis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_kodejenisKeyTyped(evt);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4)
                        .addComponent(txt_jenises, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                        .addComponent(txt_kodejenis)))
                .addContainerGap(157, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(256, Short.MAX_VALUE)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_kodejenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_jenises, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 365, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout tambahJenisLayout = new javax.swing.GroupLayout(tambahJenis);
        tambahJenis.setLayout(tambahJenisLayout);
        tambahJenisLayout.setHorizontalGroup(
            tambahJenisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahJenisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahJenisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tambahJenisLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tambahJenisLayout.setVerticalGroup(
            tambahJenisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahJenisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(47, 47, 47)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(tambahJenis, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void txt_kodejenisKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_kodejenisKeyTyped

    }//GEN-LAST:event_txt_kodejenisKeyTyped

    private void txt_kodejenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodejenisActionPerformed

    }//GEN-LAST:event_txt_kodejenisActionPerformed

    private void tbl_jenisesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_jenisesMouseClicked
        btn_hapus.setVisible(true);
                btn_batal.setVisible(true);
        btn_tambah.setText("Edit");
        int baris = tbl_jenises.getSelectedRow();
        String kodejenis = tbl_jenises.getValueAt(baris, 1).toString();
        txt_kodejenis.setText(kodejenis);
        String jenises = tbl_jenises.getValueAt(baris, 2).toString();
        txt_jenises.setText(jenises);
        
    }//GEN-LAST:event_tbl_jenisesMouseClicked

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed

    }//GEN-LAST:event_txt_cariActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed

        if (btn_tambah.getText().equals("Tambah")) {
            btn_ubah.setText("Tambah");
            txt_kodejenis.setEditable(true);
            btn_clear.setVisible(true);
        } else if (btn_tambah.getText().equals("Edit")) {
            btn_ubah.setText("Ubah");
            txt_kodejenis.setEditable(false);
            btn_clear.setVisible(false);
        }
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(tambahJenis);
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

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        bersihform();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_ubahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ubahActionPerformed
        if (btn_ubah.getText().equals("Ubah")) {

            ubahData();
        } else if (btn_ubah.getText().equals("Tambah")) {

            tambahData();
        }
    }//GEN-LAST:event_btn_ubahActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed

        mainPanel.removeAll();
        mainPanel.add(dataJenis);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_backActionPerformed

    private void tbl_jenisesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_jenisesKeyPressed
        // TODO add your handling code here:
           btn_tambahActionPerformed(null);
    }//GEN-LAST:event_tbl_jenisesKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JComboBox<String> cb_cari;
    private javax.swing.JPanel dataJenis;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel tambahJenis;
    private javax.swing.JTable tbl_jenises;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_jenises;
    private javax.swing.JTextField txt_kodejenis;
    // End of variables declaration//GEN-END:variables

}

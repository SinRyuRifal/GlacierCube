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
import java.sql.ResultSet;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Rifal
 */
public class Form_Supplier extends javax.swing.JPanel {

    private void autoResizeAllColumns() {
        int columns = tbl_supplier.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_supplier.getColumnModel().getColumn(i);
            int width = (int) tbl_supplier.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_supplier, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_supplier.getRowCount(); row++) {
                int preferedWidth = (int) tbl_supplier.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_supplier, tbl_supplier.getValueAt(row, i), false, false, row, i)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            column.setPreferredWidth(width);
        }
    }

    private void loadData() {
        DefaultTableModel supplierModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        supplierModel.addColumn("No");
        supplierModel.addColumn("ID Supplier");
        supplierModel.addColumn("Nama Supplier");
        supplierModel.addColumn("Nama Toko");
        supplierModel.addColumn("Alamat Supplier");
        supplierModel.addColumn("Telepon Supplier");

        tbl_supplier.setAutoCreateRowSorter(true);

        try {
            int no = 1;
            String sql = "SELECT * FROM supplier";
            java.sql.Connection mysqlconfig = (Connection) koneksi.getConnection();
            java.sql.Statement stm = mysqlconfig.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                supplierModel.addRow(new Object[]{no++,
                    res.getString("idsupplier"),
                    res.getString("namasupplier"),
                    res.getString("tokosupplier"),
                    res.getString("alamatsupplier"),
                    res.getString("telpsupplier")});
            }
            tbl_supplier.setModel(supplierModel);
            autoResizeAllColumns();

            for (int i = 0; i < supplierModel.getColumnCount(); i++) {
                tbl_supplier.getColumnModel().getColumn(i).setCellEditor(null);
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

            for (int i = 0; i < supplierModel.getColumnCount(); i++) {
                tbl_supplier.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        } catch (Exception e) {

        }
    }

    private void tambahData() {
        try {

            if (txt_idsupplier.getText().isEmpty() || txt_namasupplier.getText().isEmpty() || txt_alamatsupplier.getText().isEmpty() || txt_telpsupplier.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menyimpan data.");
                return;
            }

            String sql = "INSERT INTO supplier (idsupplier, namasupplier, tokosupplier, alamatsupplier, telpsupplier) VALUES (?, ?, ?, ?, ?)";
            Connection mysqlconfig = koneksi.getConnection();
            PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, txt_idsupplier.getText());
            pst.setString(2, txt_namasupplier.getText());
            pst.setString(3, txt_tokosupplier.getText());
            pst.setString(4, txt_alamatsupplier.getText());
            pst.setString(5, txt_telpsupplier.getText());

            pst.execute();
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        loadData();
        bersihform();
    }

    private void bersihform() {
        txt_idsupplier.setText("");
        txt_namasupplier.setText("");
        txt_tokosupplier.setText("");
        txt_alamatsupplier.setText("");
        txt_telpsupplier.setText("");
    }

    private void hapusData() {
        int selectedRow = tbl_supplier.getSelectedRow();

        if (selectedRow != -1) {
            String idToDelete = tbl_supplier.getValueAt(selectedRow, 1).toString();

            int confirmResult = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin menghapus data dengan ID Supplier " + idToDelete + "?",
                    "Konfirmasi Hapus Data",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmResult == JOptionPane.YES_OPTION) {
                try {
                    String sql = "DELETE FROM supplier WHERE idsupplier=?";
                    Connection mysqlconfig = koneksi.getConnection();
                    java.sql.PreparedStatement pst = mysqlconfig.prepareStatement(sql);
                    pst.setString(1, idToDelete);
                    int rowsAffected = pst.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Data with ID Supplier " + idToDelete + " has been deleted.");
                        loadData();
                        bersihform();
                    } else {
                        JOptionPane.showMessageDialog(this, "Data with ID Supplier " + idToDelete + " not found.");
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

            if (txt_idsupplier.getText().isEmpty() || txt_namasupplier.getText().isEmpty() || txt_alamatsupplier.getText().isEmpty() || txt_telpsupplier.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum mengubah data.");
                return;
            }

            String sql = "UPDATE supplier SET idsupplier = ?, namasupplier = ?, tokosupplier=?, alamatsupplier = ?, telpsupplier = ? WHERE idsupplier = ?";
            java.sql.Connection mysqlconfig = koneksi.getConnection();
            java.sql.PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, txt_idsupplier.getText());
            pst.setString(2, txt_namasupplier.getText());
            pst.setString(3, txt_tokosupplier.getText());
            pst.setString(4, txt_alamatsupplier.getText());
            pst.setString(5, txt_telpsupplier.getText());
            pst.setString(6, txt_idsupplier.getText());

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang cocok dengan ID Supplier yang diberikan.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Perubahan data gagal: " + e.getMessage());
        }
        loadData();
        bersihform();
    }

    private void filterDataSupplier(String keyword, String selectedField) {
        DefaultTableModel supplierModel = (DefaultTableModel) tbl_supplier.getModel();
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
            case "Alamat Supplier":
                sql += "LOWER(ALAMATSUPPLIER) LIKE LOWER(?)";
                break;
            case "Telp Supplier":
                sql += "LOWER(TELPSUPPLIER) LIKE LOWER(?)";
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
                        no++,
                        res.getString("IDSUPPLIER"),
                        res.getString("NAMASUPPLIER"),
                        res.getString("TOKOSUPPLIER"),
                        res.getString("ALAMATSUPPLIER"),
                        res.getString("TELPSUPPLIER")
                    });
                }
                autoResizeAllColumns();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Form_Supplier() {
        initComponents();
        btn_hapus.setVisible(false);
        btn_batal.setVisible(false);
        Color headerColor = new Color(194, 217, 255);
        tbl_supplier.getTableHeader().setBackground(headerColor);
        loadData();
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

        mainPanel = new javax.swing.JPanel();
        dataSupplier = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_supplier = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cb_cari = new javax.swing.JComboBox<>();
        txt_cari = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        tambahSupplier = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_idsupplier = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_namasupplier = new javax.swing.JTextField();
        txt_alamatsupplier = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_telpsupplier = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_tokosupplier = new javax.swing.JTextField();
        btn_clear = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        dataSupplier.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tbl_supplier.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_supplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_supplierMouseClicked(evt);
            }
        });
        tbl_supplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_supplierKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_supplier);

        jLabel1.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel1.setText("Data Supplier");

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Supplier", "Nama Supplier", "Toko Supplier", "Alamat Supplier", "Telp Supplier" }));

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

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

        javax.swing.GroupLayout dataSupplierLayout = new javax.swing.GroupLayout(dataSupplier);
        dataSupplier.setLayout(dataSupplierLayout);
        dataSupplierLayout.setHorizontalGroup(
            dataSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataSupplierLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                    .addGroup(dataSupplierLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataSupplierLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        dataSupplierLayout.setVerticalGroup(
            dataSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataSupplierLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel21)
                    .addGroup(dataSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addGroup(dataSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(dataSupplier, "card2");

        tambahSupplier.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Tambah Supplier");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel3.setText("ID Supplier");

        txt_idsupplier.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_idsupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_idsupplierKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel4.setText("Nama Supplier");

        txt_namasupplier.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txt_alamatsupplier.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel5.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel5.setText("Alamat Supplier");

        jLabel6.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel6.setText("Telp Supplier");

        txt_telpsupplier.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_telpsupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telpsupplierKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel7.setText("Nama Toko");

        txt_tokosupplier.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

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
                    .addComponent(txt_idsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txt_namasupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txt_tokosupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txt_alamatsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txt_telpsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 347, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(txt_idsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_namasupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_tokosupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_alamatsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_telpsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout tambahSupplierLayout = new javax.swing.GroupLayout(tambahSupplier);
        tambahSupplier.setLayout(tambahSupplierLayout);
        tambahSupplierLayout.setHorizontalGroup(
            tambahSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahSupplierLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tambahSupplierLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tambahSupplierLayout.setVerticalGroup(
            tambahSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahSupplierLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(47, 47, 47)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(tambahSupplier, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void txt_idsupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_idsupplierKeyTyped

        String idsupplier = txt_idsupplier.getText();
        if (idsupplier.length() >= 7) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_idsupplierKeyTyped

    private void tbl_supplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_supplierMouseClicked

        btn_hapus.setVisible(true);
        btn_batal.setVisible(true);
        btn_tambah.setText("Edit");
        int baris = tbl_supplier.getSelectedRow();
        String idsupplier = tbl_supplier.getValueAt(baris, 1).toString();
        txt_idsupplier.setText(idsupplier);
        String namasupplier = tbl_supplier.getValueAt(baris, 2).toString();
        txt_namasupplier.setText(namasupplier);
        String tokosupplier = tbl_supplier.getValueAt(baris, 3).toString();
        txt_tokosupplier.setText(tokosupplier);
        String alamatsupplier = tbl_supplier.getValueAt(baris, 4).toString();
        txt_alamatsupplier.setText(alamatsupplier);
        String telpsupplier = tbl_supplier.getValueAt(baris, 5).toString();
        txt_telpsupplier.setText(telpsupplier);


    }//GEN-LAST:event_tbl_supplierMouseClicked

    private void txt_telpsupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telpsupplierKeyTyped

        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_telpsupplierKeyTyped

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed

    }//GEN-LAST:event_txt_cariActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed

        if (btn_tambah.getText().equals("Tambah")) {
            btn_ubah.setText("Tambah");
            txt_idsupplier.setEditable(true);
            btn_clear.setVisible(true);
        } else if (btn_tambah.getText().equals("Edit")) {
            btn_ubah.setText("Ubah");
            txt_idsupplier.setEditable(false);
            btn_clear.setVisible(false);
        }
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(tambahSupplier);
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
        mainPanel.add(dataSupplier);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_backActionPerformed

    private void tbl_supplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_supplierKeyPressed
        // TODO add your handling code here:
           btn_tambahActionPerformed(null);
    }//GEN-LAST:event_tbl_supplierKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JComboBox<String> cb_cari;
    private javax.swing.JPanel dataSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel tambahSupplier;
    private javax.swing.JTable tbl_supplier;
    private javax.swing.JTextField txt_alamatsupplier;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_idsupplier;
    private javax.swing.JTextField txt_namasupplier;
    private javax.swing.JTextField txt_telpsupplier;
    private javax.swing.JTextField txt_tokosupplier;
    // End of variables declaration//GEN-END:variables
}

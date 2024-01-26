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
import java.awt.event.KeyEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Rifal
 */
public class Form_Produk extends javax.swing.JPanel {
 private boolean isCursorInStockProduk = false;
    private boolean isprodukMirip(String jenisEs) {
        try {
            String sql = "SELECT COUNT(*) FROM produk WHERE namaproduk=?";
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
        int columns = tbl_produk.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_produk.getColumnModel().getColumn(i);
            int width = (int) tbl_produk.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_produk, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_produk.getRowCount(); row++) {
                int preferedWidth = (int) tbl_produk.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_produk, tbl_produk.getValueAt(row, i), false, false, row, i)
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
        brg.addColumn("ID Produk");
        brg.addColumn("Kode Jenis");
        brg.addColumn("Nama Produk");
        brg.addColumn("Berat Produk(Kg)");
        brg.addColumn("Stock Produk");

        tbl_produk.setAutoCreateRowSorter(true);

        try {
            int no = 1;
            String sql = "SELECT * FROM produk";
            java.sql.Connection mysqlconfig = (Connection) koneksi.getConnection();
            java.sql.Statement stm = mysqlconfig.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                brg.addRow(new Object[]{no++,
                    res.getString("idproduk"),
                    res.getString("kodejenis"),
                    res.getString("namaproduk"),
                    res.getString("beratproduk"),
                    res.getString("stockproduk")});
            }
            tbl_produk.setModel(brg);
            autoResizeAllColumns();

            for (int i = 0; i < brg.getColumnCount(); i++) {
                tbl_produk.getColumnModel().getColumn(i).setCellEditor(null);
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

            for (int i = 0; i < brg.getColumnCount(); i++) {
                tbl_produk.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        } catch (Exception e) {

        }
    }

    private void tambahData() {
        try {

            if (txt_idproduk.getText().isEmpty() || txt_kodejenis.getText().isEmpty() || txt_namaproduk.getText().isEmpty() || txt_beratproduk.getText().isEmpty() || txt_stockproduk.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menyimpan data.");
                return;
            }
            String namaProduk = txt_namaproduk.getText();
            if (isprodukMirip(namaProduk)) {
                JOptionPane.showMessageDialog(this, "Produk sudah ada. Harap tambahkan produk yang berbeda.");
                return;
            }

            String sql = "INSERT INTO produk (idproduk, kodejenis, namaproduk, beratproduk, stockproduk) VALUES (?, ?, ?, ?, ?)";
            Connection mysqlconfig = koneksi.getConnection();
            PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, txt_idproduk.getText());
            pst.setString(2, txt_kodejenis.getText());
            pst.setString(3, txt_namaproduk.getText());
            pst.setFloat(4, Integer.parseInt(txt_beratproduk.getText()));
            pst.setFloat(5, Integer.parseInt(txt_stockproduk.getText()));

            pst.execute();
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        loadData();
        bersihform();
        
    }

    private void bersihform() {
        txt_idproduk.setText("");
        txt_kodejenis.setText("");
        txt_namaproduk.setText("");
        txt_beratproduk.setText("");
        txt_stockproduk.setText("");
    }

    private void hapusData() {
        int selectedRow = tbl_produk.getSelectedRow();

        if (selectedRow != -1) {
            String idToDelete = tbl_produk.getValueAt(selectedRow, 1).toString();

            int confirmResult = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin menghapus data dengan ID Produk " + idToDelete + "?",
                    "Konfirmasi Hapus Data",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmResult == JOptionPane.YES_OPTION) {
                try {
                    String sql = "DELETE FROM produk WHERE idproduk=?";
                    Connection mysqlconfig = koneksi.getConnection();
                    java.sql.PreparedStatement pst = mysqlconfig.prepareStatement(sql);
                    pst.setString(1, idToDelete);
                    int rowsAffected = pst.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Data with ID Produk " + idToDelete + " has been deleted.");
                        loadData();
                        bersihform();
                    } else {
                        JOptionPane.showMessageDialog(this, "Data with ID Produk " + idToDelete + " not found.");
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

            if (txt_idproduk.getText().isEmpty() || txt_kodejenis.getText().isEmpty() || txt_namaproduk.getText().isEmpty() || txt_beratproduk.getText().isEmpty() || txt_stockproduk.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menyimpan data.");
                return;
            }
         
            String idProduk = txt_idproduk.getText();
            String kodeJenis = txt_kodejenis.getText();
            String namaProduk = txt_namaproduk.getText();
            int beratProduk = Integer.parseInt(txt_beratproduk.getText());
            int stockProduk = Integer.parseInt(txt_stockproduk.getText());

            try {

                String sqlCheckExistence = "SELECT COUNT(*) FROM PRODUK WHERE idproduk=? AND namaproduk != ?";
                Connection mysqlconfigCheckExistence = koneksi.getConnection();
                PreparedStatement pstCheckExistence = mysqlconfigCheckExistence.prepareStatement(sqlCheckExistence);
                pstCheckExistence.setString(1, idProduk);
                pstCheckExistence.setString(2, namaProduk);
                ResultSet resultSetExistence = pstCheckExistence.executeQuery();
                resultSetExistence.next();
                int countExistence = resultSetExistence.getInt(1);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }

            String sql = "UPDATE PRODUK SET IDPRODUK = ?, KODEJENIS = ?, NAMAPRODUK = ?, BERATPRODUK = ?, STOCKPRODUK = ? WHERE IDPRODUK = ?";
            Connection mysqlconfig = koneksi.getConnection();
            PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, idProduk);
            pst.setString(2, kodeJenis);
            pst.setString(3, namaProduk);
            pst.setInt(4, beratProduk);
            pst.setInt(5, stockProduk);
            pst.setString(6, idProduk);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang cocok dengan ID Produk yang diberikan.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Perubahan data gagal: " + e.getMessage());
        }
        loadData();
        bersihform();
          
    }

    private void filterData(String keyword, String selectedField) {
        DefaultTableModel produkModel = (DefaultTableModel) tbl_produk.getModel();
        produkModel.setRowCount(0);

        String sql = "SELECT * FROM PRODUK WHERE ";

        switch (selectedField) {
            case "ID Produk":
                sql += "LOWER(IDPRODUK) LIKE LOWER(?)";
                break;
            case "Kode Jenis":
                sql += "LOWER(KODEJENIS) LIKE LOWER(?)";
                break;
            case "Nama Produk":
                sql += "LOWER(NAMAPRODUK) LIKE LOWER(?)";
                break;
            default:
                return;
        }

        try (Connection conn = koneksi.getConnection(); PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, "%" + keyword + "%");

            try (ResultSet res = stm.executeQuery()) {
                int no = 1;
                while (res.next()) {
                    produkModel.addRow(new Object[]{
                        no++,
                        res.getString("IDPRODUK"),
                        res.getString("KODEJENIS"),
                        res.getString("NAMAPRODUK"),
                        res.getInt("BERATPRODUK"),
                        res.getInt("STOCKPRODUK")
                    });
                }
                autoResizeAllColumns();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
private void txt_stockprodukFocusGained(java.awt.event.FocusEvent evt) {
        isCursorInStockProduk = true;
    }

    private void txt_stockprodukKeyPressed(java.awt.event.KeyEvent evt) {
        if (isCursorInStockProduk && evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btn_ubahActionPerformed(null); // Pass null to simulate the action event
        }
    }

    private void txt_stockprodukFocusLost(java.awt.event.FocusEvent evt) {
        isCursorInStockProduk = false;
    }
    public Form_Produk() {
        initComponents();
          txt_stockproduk.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_stockprodukFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_stockprodukFocusLost(evt);
            }
        });

        txt_stockproduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_stockprodukKeyPressed(evt);
            }
        });

        txt_kodejenis.setEditable(false);
        btn_hapus.setVisible(false);
        btn_batal.setVisible(false);
        Color headerColor = new Color(194, 217, 255);
        tbl_produk.getTableHeader().setBackground(headerColor);
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
        dataStock = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_produk = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cb_cari = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txt_cari = new javax.swing.JTextField();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        tambahStock = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_stockproduk = new javax.swing.JTextField();
        txt_namaproduk = new javax.swing.JTextField();
        txt_idproduk = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_beratproduk = new javax.swing.JTextField();
        btn_otherjenis = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txt_kodejenis = new javax.swing.JTextField();
        btn_clear = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        dataStock.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tbl_produk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "ID Barang", "Nama Barang", "Jenis Barang", "Stok Barang", "Satuan Barang", "Harga Barang"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_produk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_produkMouseClicked(evt);
            }
        });
        tbl_produk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_produkKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_produk);
        if (tbl_produk.getColumnModel().getColumnCount() > 0) {
            tbl_produk.getColumnModel().getColumn(0).setResizable(false);
            tbl_produk.getColumnModel().getColumn(0).setPreferredWidth(2);
            tbl_produk.getColumnModel().getColumn(1).setResizable(false);
            tbl_produk.getColumnModel().getColumn(1).setPreferredWidth(20);
            tbl_produk.getColumnModel().getColumn(2).setResizable(false);
            tbl_produk.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbl_produk.getColumnModel().getColumn(3).setResizable(false);
            tbl_produk.getColumnModel().getColumn(3).setPreferredWidth(30);
            tbl_produk.getColumnModel().getColumn(4).setResizable(false);
            tbl_produk.getColumnModel().getColumn(4).setPreferredWidth(50);
            tbl_produk.getColumnModel().getColumn(5).setResizable(false);
            tbl_produk.getColumnModel().getColumn(5).setPreferredWidth(20);
            tbl_produk.getColumnModel().getColumn(6).setResizable(false);
            tbl_produk.getColumnModel().getColumn(6).setPreferredWidth(50);
        }

        jLabel1.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel1.setText("Data Produk");

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Produk", "Kode Jenis", "Nama Produk" }));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout dataStockLayout = new javax.swing.GroupLayout(dataStock);
        dataStock.setLayout(dataStockLayout);
        dataStockLayout.setHorizontalGroup(
            dataStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataStockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addGroup(dataStockLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataStockLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        dataStockLayout.setVerticalGroup(
            dataStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataStockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel12)
                    .addGroup(dataStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(dataStock, "card2");

        tambahStock.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Tambah Produk");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel3.setText("ID Produk");

        jLabel4.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel4.setText("Nama Produk");

        jLabel7.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel7.setText("Stock Produk");

        txt_stockproduk.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_stockproduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_stockprodukKeyTyped(evt);
            }
        });

        txt_namaproduk.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        txt_idproduk.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_idproduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idprodukActionPerformed(evt);
            }
        });
        txt_idproduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_idprodukKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel8.setText("Berat Produk (Kg)");

        txt_beratproduk.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_beratproduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_beratprodukKeyTyped(evt);
            }
        });

        btn_otherjenis.setText("...");
        btn_otherjenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_otherjenisActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel9.setText("Kode Jenis");

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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txt_kodejenis, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_otherjenis))
                                    .addComponent(txt_idproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel4))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_namaproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8)
                                .addComponent(txt_beratproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7)
                                .addComponent(txt_stockproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 193, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_idproduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_kodejenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_otherjenis))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_namaproduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_beratproduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_stockproduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout tambahStockLayout = new javax.swing.GroupLayout(tambahStock);
        tambahStock.setLayout(tambahStockLayout);
        tambahStockLayout.setHorizontalGroup(
            tambahStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahStockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tambahStockLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tambahStockLayout.setVerticalGroup(
            tambahStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahStockLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(47, 47, 47)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(tambahStock, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_produkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_produkMouseClicked

        btn_hapus.setVisible(true);
        btn_batal.setVisible(true);
        btn_tambah.setText("Edit");
        int baris = tbl_produk.getSelectedRow();
        String idproduk = tbl_produk.getValueAt(baris, 1).toString();
        txt_idproduk.setText(idproduk);

        String kodejenis = tbl_produk.getValueAt(baris, 2).toString();
        txt_kodejenis.setText(kodejenis);

        String namaproduk = tbl_produk.getValueAt(baris, 3).toString();
        txt_namaproduk.setText(namaproduk);

        String beratproduk = tbl_produk.getValueAt(baris, 4).toString();
        txt_beratproduk.setText(beratproduk);

        String stockproduk = tbl_produk.getValueAt(baris, 5).toString();
        txt_stockproduk.setText(stockproduk);


    }//GEN-LAST:event_tbl_produkMouseClicked

    private void txt_idprodukKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_idprodukKeyTyped


    }//GEN-LAST:event_txt_idprodukKeyTyped

    private void txt_idprodukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idprodukActionPerformed

    }//GEN-LAST:event_txt_idprodukActionPerformed

    private void btn_otherjenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_otherjenisActionPerformed

        Dialog_DataJenis dialogDataJenis = new Dialog_DataJenis(new javax.swing.JFrame(), true);
        dialogDataJenis.setVisible(true);

        if (dialogDataJenis.isJenisSelected()) {

            String selectedkodeJenis = dialogDataJenis.getSelectedkodeJenis();

            txt_kodejenis.setText(selectedkodeJenis);

        }
    }//GEN-LAST:event_btn_otherjenisActionPerformed

    private void txt_kodejenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodejenisActionPerformed

    }//GEN-LAST:event_txt_kodejenisActionPerformed

    private void txt_kodejenisKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_kodejenisKeyTyped

    }//GEN-LAST:event_txt_kodejenisKeyTyped

    private void txt_beratprodukKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_beratprodukKeyTyped

        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }

    }//GEN-LAST:event_txt_beratprodukKeyTyped

    private void txt_stockprodukKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_stockprodukKeyTyped

        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }

    }//GEN-LAST:event_txt_stockprodukKeyTyped

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed

    }//GEN-LAST:event_txt_cariActionPerformed

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
        mainPanel.add(dataStock);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed

        if (btn_tambah.getText().equals("Tambah")) {
            btn_ubah.setText("Tambah");
            txt_idproduk.setEditable(true);
            btn_clear.setVisible(true);
        } else if (btn_tambah.getText().equals("Edit")) {
            btn_ubah.setText("Ubah");
            txt_idproduk.setEditable(false);
            btn_clear.setVisible(false);
        }
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(tambahStock);
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

    private void tbl_produkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_produkKeyPressed
        btn_tambahActionPerformed(null);
    }//GEN-LAST:event_tbl_produkKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_otherjenis;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JComboBox<String> cb_cari;
    private javax.swing.JPanel dataStock;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel tambahStock;
    private javax.swing.JTable tbl_produk;
    private javax.swing.JTextField txt_beratproduk;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_idproduk;
    private javax.swing.JTextField txt_kodejenis;
    private javax.swing.JTextField txt_namaproduk;
    private javax.swing.JTextField txt_stockproduk;
    // End of variables declaration//GEN-END:variables

}

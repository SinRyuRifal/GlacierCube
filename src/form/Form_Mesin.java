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
public class Form_Mesin extends javax.swing.JPanel {

    private void autoResizeAllColumns() {
        int columns = tbl_mesin.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_mesin.getColumnModel().getColumn(i);
            int width = (int) tbl_mesin.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_mesin, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_mesin.getRowCount(); row++) {
                int preferedWidth = (int) tbl_mesin.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_mesin, tbl_mesin.getValueAt(row, i), false, false, row, i)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            column.setPreferredWidth(width);
        }
    }

    private void loadData() {
        DefaultTableModel mesinModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        mesinModel.addColumn("No");
        mesinModel.addColumn("ID Mesin");
        mesinModel.addColumn("Kode Jenis");
        mesinModel.addColumn("Nama Mesin");
        mesinModel.addColumn("Berat Mesin");
        mesinModel.addColumn("Tahun Mesin");
        mesinModel.addColumn("Asal Mesin");
        mesinModel.addColumn("Merk Mesin");

        tbl_mesin.setAutoCreateRowSorter(true); 
        autoResizeAllColumns();
        try {
            int no = 1;
            String sql = "SELECT * FROM MESIN";
            java.sql.Connection mysqlconfig = koneksi.getConnection();
            java.sql.Statement stm = mysqlconfig.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                mesinModel.addRow(new Object[]{no++,
                    res.getString("idmesin"),
                    res.getString("kodejenis"),
                    res.getString("namamesin"),
                    res.getString("beratmesin"),
                    res.getString("tahunmesin"),
                    res.getString("asalmesin"),
                    res.getString("merkmesin")});
            }
            tbl_mesin.setModel(mesinModel);

          
            for (int i = 0; i < mesinModel.getColumnCount(); i++) {
                tbl_mesin.getColumnModel().getColumn(i).setCellEditor(null);
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

            for (int i = 0; i < mesinModel.getColumnCount(); i++) {
                tbl_mesin.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        } catch (Exception e) {
           
        }
    }

    private void tambahData() {
        try {
            
            if (txt_idmesin.getText().isEmpty() || txt_kodejenis.getText().isEmpty() || txt_namamesin.getText().isEmpty()
                    || txt_beratmesin.getText().isEmpty() || txt_tahunmesin.getText().isEmpty()
                    || txt_asalmesin.getText().isEmpty() || txt_merkmesin.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menyimpan data.");
                return;
            }

            String sql = "INSERT INTO mesin (idmesin, kodejenis, namamesin, beratmesin, tahunmesin, asalmesin, merkmesin) VALUES (?,?,?,?,?,?,?)";
            Connection mysqlconfig = koneksi.getConnection();
            PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, txt_idmesin.getText());
            pst.setString(2, txt_kodejenis.getText());
            pst.setString(3, txt_namamesin.getText());
            pst.setInt(4, Integer.parseInt(txt_beratmesin.getText()));
            pst.setInt(5, Integer.parseInt(txt_tahunmesin.getText()));
            pst.setString(6, txt_asalmesin.getText());
            pst.setString(7, txt_merkmesin.getText());

            pst.execute();
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        loadData();
        bersihform();
    }

    private void bersihform() {
        txt_idmesin.setText("");
        txt_namamesin.setText("");
        txt_kodejenis.setText("");
        txt_beratmesin.setText("");
        txt_tahunmesin.setText("");
        txt_asalmesin.setText("");
        txt_merkmesin.setText("");
    }

  private void hapusData() {
    int selectedRow = tbl_mesin.getSelectedRow();

    if (selectedRow != -1) {
        String idToDelete = tbl_mesin.getValueAt(selectedRow, 1).toString();

        int confirmResult = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data dengan ID Mesin " + idToDelete + "?",
                "Konfirmasi Hapus Data",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmResult == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM mesin WHERE idmesin=?";
                Connection mysqlconfig = koneksi.getConnection();
                java.sql.PreparedStatement pst = mysqlconfig.prepareStatement(sql);
                pst.setString(1, idToDelete);
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data with ID Mesin " + idToDelete + " has been deleted.");
                    loadData();
                    bersihform();
                } else {
                    JOptionPane.showMessageDialog(this, "Data with ID Mesin " + idToDelete + " not found.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Pilih baris untuk dihapus.");
    }
}


private void filterData(String keyword, String selectedField) {
    DefaultTableModel mesinModel = (DefaultTableModel) tbl_mesin.getModel();
    mesinModel.setRowCount(0);

    String sql = "SELECT * FROM MESIN WHERE ";

    switch (selectedField) {
        case "ID Mesin":
            sql += "LOWER(IDMESIN) LIKE LOWER(?)";
            break;
        case "Kode Jenis":
            sql += "LOWER(KODEJENIS) LIKE LOWER(?)";
            break;
        case "Nama Mesin":
            sql += "LOWER(NAMAMESIN) LIKE LOWER(?)";
            break;
        case "Tahun Mesin":
            sql += "LOWER(TAHUNMESIN) LIKE LOWER(?)";
            break;
        case "Asal Mesin":
            sql += "LOWER(ASALMESIN) LIKE LOWER(?)";
            break;
        case "Merk Mesin":
            sql += "LOWER(MERKMESIN) LIKE LOWER(?)";
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
                mesinModel.addRow(new Object[]{
                        no++,
                        res.getString("IDMESIN"),
                        res.getString("KODEJENIS"),
                        res.getString("NAMAMESIN"),
                        res.getDouble("BERATMESIN"),
                        res.getInt("TAHUNMESIN"),
                        res.getString("ASALMESIN"),
                        res.getString("MERKMESIN")
                });
            }
            autoResizeAllColumns(); 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void ubahData() {
        try {
           
            if (txt_idmesin.getText().isEmpty() || txt_kodejenis.getText().isEmpty() || txt_namamesin.getText().isEmpty()
                    || txt_beratmesin.getText().isEmpty() || txt_tahunmesin.getText().isEmpty()
                    || txt_asalmesin.getText().isEmpty() || txt_merkmesin.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menyimpan data.");
                return;
            }

            String sql = "UPDATE mesin SET idmesin = ?, kodejenis = ?, namamesin = ?, beratmesin = ?, tahunmesin = ?, asalmesin = ?, merkmesin = ? WHERE idmesin = ?";
            java.sql.Connection mysqlconfig = koneksi.getConnection();
            java.sql.PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, txt_idmesin.getText());
            pst.setString(2, txt_kodejenis.getText());
            pst.setString(3, txt_namamesin.getText());
            pst.setInt(4, Integer.parseInt(txt_beratmesin.getText()));
            pst.setInt(5, Integer.parseInt(txt_tahunmesin.getText()));
            pst.setString(6, txt_asalmesin.getText());
            pst.setString(7, txt_merkmesin.getText());
            pst.setString(8, txt_idmesin.getText());

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang cocok dengan ID Mesin yang diberikan.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Perubahan data gagal: " + e.getMessage());
        }
        loadData();
        bersihform();
    }

    public Form_Mesin() {
        initComponents();
        btn_hapus.setVisible(false);
        btn_batal.setVisible(false);
        txt_kodejenis.setEditable(false);
        Color headerColor = new Color(194, 217, 255);
        tbl_mesin.getTableHeader().setBackground(headerColor);
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
        dataMesin = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_mesin = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cb_cari = new javax.swing.JComboBox<>();
        txt_cari = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        tambahMesin = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_idmesin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_namamesin = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_beratmesin = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_asalmesin = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_merkmesin = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_tahunmesin = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_kodejenis = new javax.swing.JTextField();
        btn_otherjenis = new javax.swing.JButton();
        btn_clear = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        dataMesin.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tbl_mesin.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_mesin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_mesinMouseClicked(evt);
            }
        });
        tbl_mesin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_mesinKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_mesin);

        jLabel1.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel1.setText("Data Mesin");

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Mesin", "Kode Jenis", "Nama Mesin", "Berat Mesin", "Tahun Mesin", "Asal Mesin", "Merk Mesin" }));

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

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

        javax.swing.GroupLayout dataMesinLayout = new javax.swing.GroupLayout(dataMesin);
        dataMesin.setLayout(dataMesinLayout);
        dataMesinLayout.setHorizontalGroup(
            dataMesinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataMesinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataMesinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                    .addGroup(dataMesinLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataMesinLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        dataMesinLayout.setVerticalGroup(
            dataMesinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataMesinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataMesinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel12)
                    .addGroup(dataMesinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addGroup(dataMesinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(dataMesin, "card2");

        tambahMesin.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Tambah Mesin");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel3.setText("ID Mesin");

        txt_idmesin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txt_idmesin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idmesinActionPerformed(evt);
            }
        });
        txt_idmesin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_idmesinKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel4.setText("Nama Mesin");

        txt_namamesin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel6.setText("Berat Mesin (KG)");

        txt_beratmesin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_beratmesin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_beratmesinKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel8.setText("Asal Mesin");

        txt_asalmesin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel9.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel9.setText("Merk Mesin");

        txt_merkmesin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel10.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel10.setText("Tahun Pembelian");

        txt_tahunmesin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_tahunmesin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_tahunmesinKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel7.setText("Kode Jenis");

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

        btn_otherjenis.setText("...");
        btn_otherjenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_otherjenisActionPerformed(evt);
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_tahunmesin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                                .addComponent(txt_merkmesin, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_asalmesin, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_beratmesin, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel10)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6))
                        .addContainerGap(249, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txt_idmesin, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(txt_namamesin, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(txt_kodejenis, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_otherjenis)))
                        .addGap(0, 249, Short.MAX_VALUE))))
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
                .addComponent(txt_idmesin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_otherjenis)
                    .addComponent(txt_kodejenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(8, 8, 8)
                .addComponent(txt_namamesin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_beratmesin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_tahunmesin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_asalmesin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_merkmesin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout tambahMesinLayout = new javax.swing.GroupLayout(tambahMesin);
        tambahMesin.setLayout(tambahMesinLayout);
        tambahMesinLayout.setHorizontalGroup(
            tambahMesinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahMesinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahMesinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tambahMesinLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tambahMesinLayout.setVerticalGroup(
            tambahMesinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahMesinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(47, 47, 47)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(tambahMesin, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_mesinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_mesinMouseClicked
        btn_hapus.setVisible(true);
        btn_batal.setVisible(true);
        btn_tambah.setText("Edit");

        int row = tbl_mesin.getSelectedRow();

        String idmesin = tbl_mesin.getValueAt(row, 1).toString();
        txt_idmesin.setText(idmesin);
        String kodejenis = tbl_mesin.getValueAt(row, 2).toString();
        txt_kodejenis.setText(kodejenis);
        String namamesin = tbl_mesin.getValueAt(row, 3).toString();
        txt_namamesin.setText(namamesin);

        String beratmesin = tbl_mesin.getValueAt(row, 4).toString();
        txt_beratmesin.setText(beratmesin);

        String tahunmesin = tbl_mesin.getValueAt(row, 5).toString();
        txt_tahunmesin.setText(tahunmesin);

        String asalmesin = tbl_mesin.getValueAt(row, 6).toString();
        txt_asalmesin.setText(asalmesin);

        String merkmesin = tbl_mesin.getValueAt(row, 7).toString();
        txt_merkmesin.setText(merkmesin);
    }//GEN-LAST:event_tbl_mesinMouseClicked

    private void txt_idmesinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idmesinActionPerformed
       
    }//GEN-LAST:event_txt_idmesinActionPerformed

    private void txt_idmesinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_idmesinKeyTyped
        
        if (txt_idmesin.getText().length() >= 7) {
            evt.consume();
        }

    }//GEN-LAST:event_txt_idmesinKeyTyped

    private void txt_beratmesinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_beratmesinKeyTyped
        
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_beratmesinKeyTyped

    private void txt_tahunmesinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_tahunmesinKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            evt.consume();
        }

      
        if (txt_tahunmesin.getText().length() >= 4) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_tahunmesinKeyTyped

    private void txt_kodejenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodejenisActionPerformed
       
    }//GEN-LAST:event_txt_kodejenisActionPerformed

    private void txt_kodejenisKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_kodejenisKeyTyped
        
    }//GEN-LAST:event_txt_kodejenisKeyTyped

    private void btn_otherjenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_otherjenisActionPerformed
        
        Dialog_DataJenis dialogDataJenis = new Dialog_DataJenis(new javax.swing.JFrame(), true);
        dialogDataJenis.setVisible(true);

       
        if (dialogDataJenis.isJenisSelected()) {
            
            String selectedkodeJenis = dialogDataJenis.getSelectedkodeJenis();
            String selectedjenisEs = dialogDataJenis.getSelectedjenisEs();
            txt_kodejenis.setText(selectedkodeJenis);
          

        }
    }//GEN-LAST:event_btn_otherjenisActionPerformed

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        
    }//GEN-LAST:event_txt_cariActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed

        if (btn_tambah.getText().equals("Tambah")) {
            btn_ubah.setText("Tambah");
            txt_idmesin.setEditable(true);
            btn_clear.setVisible(true);
        } else if (btn_tambah.getText().equals("Edit")) {
            btn_ubah.setText("Ubah");
            txt_idmesin.setEditable(false);
            btn_clear.setVisible(false);
        }
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(tambahMesin);
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
        mainPanel.add(dataMesin);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_backActionPerformed

    private void tbl_mesinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_mesinKeyPressed
        // TODO add your handling code here:
           btn_tambahActionPerformed(null);
    }//GEN-LAST:event_tbl_mesinKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_otherjenis;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JComboBox<String> cb_cari;
    private javax.swing.JPanel dataMesin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel tambahMesin;
    private javax.swing.JTable tbl_mesin;
    private javax.swing.JTextField txt_asalmesin;
    private javax.swing.JTextField txt_beratmesin;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_idmesin;
    private javax.swing.JTextField txt_kodejenis;
    private javax.swing.JTextField txt_merkmesin;
    private javax.swing.JTextField txt_namamesin;
    private javax.swing.JTextField txt_tahunmesin;
    // End of variables declaration//GEN-END:variables
}

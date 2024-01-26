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
public class Form_GajiKaryawan extends javax.swing.JPanel {
 private void autoResizeAllColumns() {
    int columns = tbl_gaji.getColumnCount();
    for (int i = 0; i < columns; i++) {
        TableColumn column = tbl_gaji.getColumnModel().getColumn(i);
        int width = (int) tbl_gaji.getTableHeader().getDefaultRenderer()
                .getTableCellRendererComponent(tbl_gaji, column.getHeaderValue(), false, false, -1, i)
                .getPreferredSize().getWidth();
        for (int row = 0; row < tbl_gaji.getRowCount(); row++) {
            int preferedWidth = (int) tbl_gaji.getCellRenderer(row, i)
                    .getTableCellRendererComponent(tbl_gaji, tbl_gaji.getValueAt(row, i), false, false, row, i)
                    .getPreferredSize().getWidth();
            width = Math.max(width, preferedWidth);
        }
        column.setPreferredWidth(width);
    }
}
    private boolean isIdGajiExists(String idGaji) {
        boolean exists = false;
        try {
            String sql = "SELECT COUNT(*) FROM gajikaryawan WHERE idgaji = ?";
            Connection connection = koneksi.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, idGaji);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                exists = (count > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            }
        return exists;
    }

    private void tambahData() {
        try {
            
            if (txt_idgaji.getText().isEmpty()||txt_gajipokok.getText().isEmpty()||txt_tunjangan.getText().isEmpty()||txt_potongan.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menyimpan data.");
                return;
            }

            String idGaji = txt_idgaji.getText();

            if (isIdGajiExists(idGaji)) {
                JOptionPane.showMessageDialog(this, "ID Gaji sudah ada dalam database. Harap gunakan kode yang berbeda.");
                return;
            }

       
            String sql = "INSERT INTO gajikaryawan (idgaji, gajipokok, tunjangan, potongan) VALUES ( ?, ?, ?, ?)";
            Connection connection = koneksi.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, txt_idgaji.getText());
            pst.setFloat(2, Float.parseFloat(txt_gajipokok.getText()));
            pst.setFloat(3, Float.parseFloat(txt_tunjangan.getText()));
            pst.setFloat(4, Float.parseFloat(txt_potongan.getText()));

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
        } catch (SQLException e) {
            e.printStackTrace();
           }
        loadData();
        bersihform();
    }

    private void loadData() {
    DefaultTableModel gajiKaryawanModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    gajiKaryawanModel.addColumn("No");
    gajiKaryawanModel.addColumn("ID Gaji");
    gajiKaryawanModel.addColumn("Gaji Pokok");
    gajiKaryawanModel.addColumn("Tunjangan");
    gajiKaryawanModel.addColumn("Potongan");

    tbl_gaji.setAutoCreateRowSorter(true);

    try {
        int no = 1;
        String sql = "SELECT * FROM gajikaryawan";
        Connection mysqlconfig = koneksi.getConnection();
        Statement stm = mysqlconfig.createStatement();
        java.sql.ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            gajiKaryawanModel.addRow(new Object[]{no++,
                rs.getString("idgaji"),
                rs.getFloat("gajipokok"),
                rs.getFloat("tunjangan"),
                rs.getFloat("potongan")});
        }
        tbl_gaji.setModel(gajiKaryawanModel);
        autoResizeAllColumns();
      
        for (int i = 0; i < gajiKaryawanModel.getColumnCount(); i++) {
            tbl_gaji.getColumnModel().getColumn(i).setCellEditor(null);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        for (int i = 0; i < gajiKaryawanModel.getColumnCount(); i++) {
            tbl_gaji.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    } catch (Exception e) {
      
    }
}


    private void bersihform() {
        txt_idgaji.setText("");
        txt_gajipokok.setText("");
        txt_tunjangan.setText("");
        txt_potongan.setText("");
    }

 private void hapusData() {
    int selectedRow = tbl_gaji.getSelectedRow();

    if (selectedRow != -1) {
        String idGajiToDelete = tbl_gaji.getValueAt(selectedRow, 1).toString();

        int confirmResult = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data dengan ID Gaji " + idGajiToDelete + "?",
                "Konfirmasi Hapus Data",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmResult == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM gajikaryawan WHERE idgaji=?";
                Connection mysqlconfig = koneksi.getConnection();
                PreparedStatement pst = mysqlconfig.prepareStatement(sql);
                pst.setString(1, idGajiToDelete);
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data dengan ID Gaji " + idGajiToDelete + " telah dihapus.");
                    loadData();
                    bersihform();
                } else {
                    JOptionPane.showMessageDialog(this, "Data dengan ID Gaji " + idGajiToDelete + " tidak ditemukan.");
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
           
                       if (txt_idgaji.getText().isEmpty()||txt_gajipokok.getText().isEmpty()||txt_tunjangan.getText().isEmpty()||txt_potongan.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menyimpan data.");
                return;
            }


            String sql = "UPDATE gajikaryawan SET idgaji = ?, gajipokok = ?, tunjangan = ?, potongan = ? WHERE idgaji = ?";
            Connection mysqlconfig = koneksi.getConnection();
            PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, txt_idgaji.getText());
            pst.setFloat(2, Float.parseFloat(txt_gajipokok.getText()));
            pst.setFloat(3, Float.parseFloat(txt_tunjangan.getText()));
            pst.setFloat(4, Float.parseFloat(txt_potongan.getText()));
            pst.setString(5, txt_idgaji.getText());

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang cocok dengan Kode Gaji yang diberikan.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Perubahan data gagal: " + e.getMessage());
        }
        loadData(); 
        bersihform(); 
    }
private void filterData(String keyword, String selectedField) {
    DefaultTableModel gajiModel = (DefaultTableModel) tbl_gaji.getModel();
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
                        no++,
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

    public Form_GajiKaryawan() {
        initComponents();
        btn_hapus.setVisible(false);
        btn_batal.setVisible(false);
        Color headerColor = new Color(194, 217, 255);
        tbl_gaji.getTableHeader().setBackground(headerColor);

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

        jkStsGaji = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        dataGaji = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_gaji = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_cari = new javax.swing.JTextField();
        cb_cari = new javax.swing.JComboBox<>();
        btn_batal = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_tambah = new javax.swing.JButton();
        tambahGaji = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_idgaji = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_gajipokok = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_tunjangan = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_potongan = new javax.swing.JTextField();
        btn_clear = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 500));
        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        dataGaji.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tbl_gaji.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_gaji.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_gajiMouseClicked(evt);
            }
        });
        tbl_gaji.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_gajiKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_gaji);

        jLabel1.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel1.setText("Detail Gaji Karyawan");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID" }));

        btn_batal.setBackground(new java.awt.Color(221, 242, 253));
        btn_batal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icnbtn/icons8_cancel_20px_1.png"))); // NOI18N
        btn_batal.setText("Batal");
        btn_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_batalActionPerformed(evt);
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

        btn_tambah.setBackground(new java.awt.Color(95, 189, 255));
        btn_tambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icnbtn/icons8_add_20px.png"))); // NOI18N
        btn_tambah.setText("Tambah");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dataGajiLayout = new javax.swing.GroupLayout(dataGaji);
        dataGaji.setLayout(dataGajiLayout);
        dataGajiLayout.setHorizontalGroup(
            dataGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataGajiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addGroup(dataGajiLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataGajiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        dataGajiLayout.setVerticalGroup(
            dataGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataGajiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9)
                    .addGroup(dataGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addGroup(dataGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(dataGaji, "card2");

        tambahGaji.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Tambah Detail Gaji");

        jLabel3.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel3.setText("ID Gaji");

        txt_idgaji.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel4.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel4.setText("Gaji Pokok");

        txt_gajipokok.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_gajipokok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_gajipokokKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel8.setText("Tunjangan");

        txt_tunjangan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_tunjangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_tunjanganKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel10.setText("Potongan");

        txt_potongan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_potongan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_potonganKeyTyped(evt);
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

        javax.swing.GroupLayout tambahGajiLayout = new javax.swing.GroupLayout(tambahGaji);
        tambahGaji.setLayout(tambahGajiLayout);
        tambahGajiLayout.setHorizontalGroup(
            tambahGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahGajiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tambahGajiLayout.createSequentialGroup()
                        .addGroup(tambahGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txt_gajipokok, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(tambahGajiLayout.createSequentialGroup()
                        .addGroup(tambahGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_idgaji, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8)
                            .addComponent(txt_tunjangan, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(txt_potongan, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addContainerGap(329, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahGajiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        tambahGajiLayout.setVerticalGroup(
            tambahGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahGajiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(44, 44, 44)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_idgaji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(11, 11, 11)
                .addComponent(txt_gajipokok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_tunjangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_potongan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                .addGroup(tambahGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(tambahGaji, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_gajiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_gajiMouseClicked
btn_hapus.setVisible(true);
                btn_batal.setVisible(true);        
        btn_tambah.setText("Edit"); 
        int selectedRow = tbl_gaji.getSelectedRow();

        if (selectedRow >= 0) {
           
            String idGaji = tbl_gaji.getValueAt(selectedRow, 1).toString();
            txt_idgaji.setText(idGaji);

            String gajiPokok = tbl_gaji.getValueAt(selectedRow, 2).toString();
            txt_gajipokok.setText(gajiPokok);

            String tunjangan = tbl_gaji.getValueAt(selectedRow, 3).toString();
            txt_tunjangan.setText(tunjangan);
            String potongan = tbl_gaji.getValueAt(selectedRow, 4).toString();

            txt_potongan.setText(potongan);
            

         
        }
    }//GEN-LAST:event_tbl_gajiMouseClicked

    private void txt_gajipokokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_gajipokokKeyTyped
        
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_gajipokokKeyTyped

    private void txt_tunjanganKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_tunjanganKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }

    }//GEN-LAST:event_txt_tunjanganKeyTyped

    private void txt_potonganKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_potonganKeyTyped
       
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume(); 
        }
    }//GEN-LAST:event_txt_potonganKeyTyped

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
       
    }//GEN-LAST:event_txt_cariActionPerformed

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed
        btn_tambah.setText("Tambah");
        btn_hapus.setVisible(false);
        btn_batal.setVisible(false);
        loadData();
        bersihform();
    }//GEN-LAST:event_btn_batalActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed

        hapusData();
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed

        if (btn_tambah.getText().equals("Tambah")) {
            btn_ubah.setText("Tambah");
            txt_idgaji.setEditable(true);
btn_clear.setVisible(true);
        } else if (btn_tambah.getText().equals("Edit")) {
            btn_ubah.setText("Ubah");
            txt_idgaji.setEditable(false);
btn_clear.setVisible(false);
        }
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(tambahGaji);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_tambahActionPerformed

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
        mainPanel.add(dataGaji);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_backActionPerformed

    private void tbl_gajiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_gajiKeyPressed
       btn_tambahActionPerformed(null);
    }//GEN-LAST:event_tbl_gajiKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JComboBox<String> cb_cari;
    private javax.swing.JPanel dataGaji;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup jkStsGaji;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel tambahGaji;
    private javax.swing.JTable tbl_gaji;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_gajipokok;
    private javax.swing.JTextField txt_idgaji;
    private javax.swing.JTextField txt_potongan;
    private javax.swing.JTextField txt_tunjangan;
    // End of variables declaration//GEN-END:variables
}

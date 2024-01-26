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

public class Form_Karyawan extends javax.swing.JPanel {

    private void autoResizeAllColumns() {
        int columns = tbl_kar.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_kar.getColumnModel().getColumn(i);
            int width = (int) tbl_kar.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_kar, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_kar.getRowCount(); row++) {
                int preferedWidth = (int) tbl_kar.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_kar, tbl_kar.getValueAt(row, i), false, false, row, i)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            column.setPreferredWidth(width);
        }
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+(\\.com|\\.co\\.id|\\.ac\\.id|\\.uinsby\\.ac\\.id))$";
        return email.matches(emailPattern);
    }

    private void tambahData() {
        try {

            if (txt_idkaryawan.getText().isEmpty() || txt_namakaryawan.getText().isEmpty() || txt_alamatkaryawan.getText().isEmpty() || txt_telpkaryawan.getText().isEmpty() || cb_divisi.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menambahkan data.");
                return;
            }
             String namaKar = txt_namakaryawan.getText();
            if (isnamaMirip(namaKar)) {
                JOptionPane.showMessageDialog(this, "Nama karyawan sudah ada. Harap gunakan nama yang berbeda.");
                return;
            }

            String email = txt_emailkaryawan.getText();
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Email karyawan tidak valid.");
                return;
            }

            String divisi = cb_divisi.getSelectedItem().toString();
            if (divisi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap pilih divisi karyawan.");
                return;
            }

            if (!rd_lklk.isSelected() && !rd_prmpn.isSelected()) {
                JOptionPane.showMessageDialog(this, "Harap pilih jenis kelamin karyawan.");
                return;
            }

            String jenisKelamin = rd_lklk.isSelected() ? "L" : (rd_prmpn.isSelected() ? "P" : "");

            String sql = "INSERT INTO karyawan (idkaryawan, namakaryawan, jeniskelamin, divisi, alamatkaryawan, emailkaryawan, telpkaryawan) VALUES (?, ?, ?, ?, ?, ?, ?)";
            Connection conn = koneksi.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, txt_idkaryawan.getText());
            pst.setString(2, txt_namakaryawan.getText());
            pst.setString(3, jenisKelamin);
            pst.setString(4, cb_divisi.getSelectedItem().toString());
            pst.setString(5, txt_alamatkaryawan.getText());
            pst.setString(6, txt_emailkaryawan.getText());
            pst.setString(7, txt_telpkaryawan.getText());

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Penyimpanan Data Berhasil");
                loadData();
                bersihform();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void ubahData() {
    try {
        if (txt_idkaryawan.getText().isEmpty() || txt_namakaryawan.getText().isEmpty() || txt_alamatkaryawan.getText().isEmpty() || txt_telpkaryawan.getText().isEmpty() || cb_divisi.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum mengubah data.");
            return;
        }

        String email = txt_emailkaryawan.getText();
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email karyawan tidak valid.");
            return;
        }

        if (!rd_lklk.isSelected() && !rd_prmpn.isSelected()) {
            JOptionPane.showMessageDialog(this, "Harap pilih jenis kelamin karyawan.");
            return;
        }

        String jenisKelamin = rd_lklk.isSelected() ? "L" : (rd_prmpn.isSelected() ? "P" : "");

        
        if (isNamaKaryawanExists(txt_namakaryawan.getText(), txt_idkaryawan.getText())) {
            JOptionPane.showMessageDialog(this, "Nama karyawan sudah ada, silakan pilih nama lain.");
            return;
        }

        String sql = "UPDATE karyawan SET namakaryawan = ?, jeniskelamin = ?, divisi = ?, alamatkaryawan = ?, emailkaryawan = ?, telpkaryawan = ? WHERE idkaryawan = ?";
        Connection conn = koneksi.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, txt_namakaryawan.getText());
        pst.setString(2, jenisKelamin);
        pst.setString(3, cb_divisi.getSelectedItem().toString());
        pst.setString(4, txt_alamatkaryawan.getText());
        pst.setString(5, txt_emailkaryawan.getText());
        pst.setString(6, txt_telpkaryawan.getText());
        pst.setString(7, txt_idkaryawan.getText());

        int rowsUpdated = pst.executeUpdate();

        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            loadData();
            bersihform();
        } else {
            JOptionPane.showMessageDialog(this, "Tidak ada data yang cocok dengan ID Karyawan yang diberikan.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Perubahan data gagal: " + e.getMessage());
    }
}

private boolean isNamaKaryawanExists(String namaKaryawan, String currentId) throws SQLException {
    String sql = "SELECT COUNT(*) FROM karyawan WHERE namakaryawan = ? AND idkaryawan <> ?";
    Connection conn = koneksi.getConnection();
    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, namaKaryawan);
        pst.setString(2, currentId);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        }
    }
    return false;
}

    private void loadData() {
        DefaultTableModel karyawanModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        karyawanModel.addColumn("No");
        karyawanModel.addColumn("ID Karyawan");
        karyawanModel.addColumn("Nama Karyawan");
        karyawanModel.addColumn("Jenis Kelamin");
        karyawanModel.addColumn("Divisi");
        karyawanModel.addColumn("Alamat Karyawan");
        karyawanModel.addColumn("Email Karyawan");
        karyawanModel.addColumn("No Telp Karyawan");

        tbl_kar.setAutoCreateRowSorter(true);

        try {
            int no = 1;
            String sql = "SELECT * FROM karyawan";
            Connection conn = koneksi.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                karyawanModel.addRow(new Object[]{
                    no++,
                    res.getString("idkaryawan"),
                    res.getString("namakaryawan"),
                    res.getString("jeniskelamin"),
                    res.getString("divisi"),
                    res.getString("alamatkaryawan"),
                    res.getString("emailkaryawan"),
                    res.getString("telpkaryawan")
                });
            }
            tbl_kar.setModel(karyawanModel);
            autoResizeAllColumns();

            for (int i = 0; i < karyawanModel.getColumnCount(); i++) {
                tbl_kar.getColumnModel().getColumn(i).setCellEditor(null);
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

            for (int i = 0; i < karyawanModel.getColumnCount(); i++) {
                tbl_kar.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void bersihform() {
        txt_idkaryawan.setText("");
        txt_namakaryawan.setText("");
        cb_divisi.setSelectedIndex(0);
        txt_alamatkaryawan.setText("");
        txt_emailkaryawan.setText("");
        txt_telpkaryawan.setText("");
        jkKaryawan.clearSelection();
    }

  private void hapusData() {
    int selectedRow = tbl_kar.getSelectedRow();

    if (selectedRow != -1) {
        String idToDelete = tbl_kar.getValueAt(selectedRow, 1).toString();

        int confirmResult = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data dengan ID Karyawan " + idToDelete + "?",
                "Konfirmasi Hapus Data",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmResult == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM karyawan WHERE idkaryawan=?";
                Connection conn = koneksi.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, idToDelete);
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data dengan ID Karyawan " + idToDelete + " telah dihapus.");
                    loadData();
                    bersihform();
                } else {
                    JOptionPane.showMessageDialog(this, "Data dengan ID Karyawan " + idToDelete + " tidak ditemukan.");
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
        DefaultTableModel karyawanModel = (DefaultTableModel) tbl_kar.getModel();
        karyawanModel.setRowCount(0);

        String sql = "SELECT * FROM karyawan WHERE ";

        switch (selectedField) {
            case "ID":
                sql += "LOWER(idkaryawan) LIKE LOWER(?)";
                break;
            case "Nama":
                sql += "LOWER(namakaryawan) LIKE LOWER(?)";
                break;
            case "Jenis Kelamin":
                sql += "LOWER(jeniskelamin) LIKE LOWER(?)";
                break;
            case "Divisi":
                sql += "LOWER(divisi) LIKE LOWER(?)";
                break;
            case "Alamat":
                sql += "LOWER(alamatkaryawan) LIKE LOWER(?)";
                break;
            case "Email":
                sql += "LOWER(emailkaryawan) LIKE LOWER(?)";
                break;
            case "Telp":
                sql += "LOWER(telpkaryawan) LIKE LOWER(?)";
                break;
            default:

                return;
        }

        try (Connection conn = koneksi.getConnection(); PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, "%" + keyword + "%");

            try (ResultSet res = stm.executeQuery()) {
                int no = 1;
                while (res.next()) {
                    karyawanModel.addRow(new Object[]{
                        no++,
                        res.getString("idkaryawan"),
                        res.getString("namakaryawan"),
                        res.getString("jeniskelamin"),
                        res.getString("divisi"),
                        res.getString("alamatkaryawan"),
                        res.getString("emailkaryawan"),
                        res.getString("telpkaryawan")
                    });
                }
                autoResizeAllColumns();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Form_Karyawan() {
        initComponents();
        btn_hapus.setVisible(false);
        btn_batal.setVisible(false);
        Color headerColor = new Color(194, 217, 255);
        tbl_kar.getTableHeader().setBackground(headerColor);
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

        jkKaryawan = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        dataKaryawan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_kar = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txt_cari = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cb_cari = new javax.swing.JComboBox<>();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        tambahKaryawan = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_idkaryawan = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_namakaryawan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cb_divisi = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txt_alamatkaryawan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        rd_lklk = new javax.swing.JRadioButton();
        rd_prmpn = new javax.swing.JRadioButton();
        txt_emailkaryawan = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_telpkaryawan = new javax.swing.JTextField();
        btn_clear = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 500));
        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        dataKaryawan.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tbl_kar.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_kar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_karMouseClicked(evt);
            }
        });
        tbl_kar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_karKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_kar);

        jLabel1.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel1.setText("Data Karyawan");

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nama", "Jenis Kelamin", "Divisi", "Alamat", "Email", "Telp" }));

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

        javax.swing.GroupLayout dataKaryawanLayout = new javax.swing.GroupLayout(dataKaryawan);
        dataKaryawan.setLayout(dataKaryawanLayout);
        dataKaryawanLayout.setHorizontalGroup(
            dataKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataKaryawanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
                    .addGroup(dataKaryawanLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataKaryawanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        dataKaryawanLayout.setVerticalGroup(
            dataKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataKaryawanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(dataKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addGroup(dataKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(dataKaryawan, "card2");

        tambahKaryawan.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Tambah Data Karyawan");

        jLabel3.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel3.setText("ID Karyawan");

        txt_idkaryawan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_idkaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idkaryawanActionPerformed(evt);
            }
        });
        txt_idkaryawan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_idkaryawanKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel4.setText("Nama Karyawan");

        txt_namakaryawan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_namakaryawan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_namakaryawanKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel5.setText("Divisi");

        cb_divisi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Pilih-", "Admin", "Operator" }));
        cb_divisi.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel6.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel6.setText("Jenis Kelamin (L/P)");

        txt_alamatkaryawan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel7.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel7.setText("Alamat Karyawan");

        jLabel9.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel9.setText("Email Karyawan");

        jkKaryawan.add(rd_lklk);
        rd_lklk.setText("Laki-Laki");

        jkKaryawan.add(rd_prmpn);
        rd_prmpn.setText("Perempuan");

        txt_emailkaryawan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel10.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel10.setText("No Telp Karyawan");

        txt_telpkaryawan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_telpkaryawan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telpkaryawanKeyTyped(evt);
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

        javax.swing.GroupLayout tambahKaryawanLayout = new javax.swing.GroupLayout(tambahKaryawan);
        tambahKaryawan.setLayout(tambahKaryawanLayout);
        tambahKaryawanLayout.setHorizontalGroup(
            tambahKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahKaryawanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tambahKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4)
                        .addComponent(txt_namakaryawan)
                        .addComponent(jLabel5)
                        .addComponent(cb_divisi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel7)
                        .addComponent(txt_alamatkaryawan)
                        .addComponent(jLabel9)
                        .addGroup(tambahKaryawanLayout.createSequentialGroup()
                            .addComponent(rd_lklk)
                            .addGap(18, 18, 18)
                            .addComponent(rd_prmpn))
                        .addComponent(txt_emailkaryawan)
                        .addComponent(jLabel10)
                        .addComponent(txt_telpkaryawan, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addComponent(txt_idkaryawan))
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahKaryawanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        tambahKaryawanLayout.setVerticalGroup(
            tambahKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahKaryawanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(35, 35, 35)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_idkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_namakaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_divisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tambahKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rd_lklk)
                    .addComponent(rd_prmpn))
                .addGap(13, 13, 13)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_alamatkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_emailkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_telpkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(tambahKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanel.add(tambahKaryawan, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_karMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_karMouseClicked
        
        btn_hapus.setVisible(true);
        btn_batal.setVisible(true);
        btn_tambah.setText("Edit");
        int baris = tbl_kar.getSelectedRow();

        String idkaryawan = tbl_kar.getValueAt(baris, 1).toString();
        txt_idkaryawan.setText(idkaryawan);

        String namakaryawan = tbl_kar.getValueAt(baris, 2).toString();
        txt_namakaryawan.setText(namakaryawan);

        String jenisKelamin = tbl_kar.getValueAt(baris, 3).toString();
        if (jenisKelamin.equals("L")) {
            rd_lklk.setSelected(true);
        } else if (jenisKelamin.equals("P")) {
            rd_prmpn.setSelected(true);
        }

        String divisi = tbl_kar.getValueAt(baris, 4).toString();
        cb_divisi.setSelectedItem(divisi);

        String alamatkaryawan = tbl_kar.getValueAt(baris, 5).toString();
        txt_alamatkaryawan.setText(alamatkaryawan);

        String emailkaryawan = tbl_kar.getValueAt(baris, 6).toString();
        txt_emailkaryawan.setText(emailkaryawan);

        String telpkaryawan = tbl_kar.getValueAt(baris, 7).toString();
        txt_telpkaryawan.setText(telpkaryawan);


    }//GEN-LAST:event_tbl_karMouseClicked

    private void txt_idkaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idkaryawanActionPerformed

    }//GEN-LAST:event_txt_idkaryawanActionPerformed

    private void txt_idkaryawanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_idkaryawanKeyTyped

        String idkaryawan = txt_idkaryawan.getText();
        if (idkaryawan.length() >= 7) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_idkaryawanKeyTyped

    private void txt_telpkaryawanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telpkaryawanKeyTyped

        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }

    }//GEN-LAST:event_txt_telpkaryawanKeyTyped

    private void txt_namakaryawanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_namakaryawanKeyTyped

        char c = evt.getKeyChar();

        if (!(Character.isLetter(c) || c == '\'')) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_namakaryawanKeyTyped

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed

    }//GEN-LAST:event_txt_cariActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed

        if (btn_tambah.getText().equals("Tambah")) {
            btn_ubah.setText("Tambah");
            txt_idkaryawan.setEditable(true);
            btn_clear.setVisible(true);
        } else if (btn_tambah.getText().equals("Edit")) {
            btn_ubah.setText("Ubah");
            txt_idkaryawan.setEditable(false);
            btn_clear.setVisible(false);
        }
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(tambahKaryawan);
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
        mainPanel.add(dataKaryawan);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_backActionPerformed

    private void tbl_karKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_karKeyPressed
        // TODO add your handling code here:
           btn_tambahActionPerformed(null);
    }//GEN-LAST:event_tbl_karKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JComboBox<String> cb_cari;
    private javax.swing.JComboBox<String> cb_divisi;
    private javax.swing.JPanel dataKaryawan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup jkKaryawan;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JRadioButton rd_lklk;
    private javax.swing.JRadioButton rd_prmpn;
    private javax.swing.JPanel tambahKaryawan;
    private javax.swing.JTable tbl_kar;
    private javax.swing.JTextField txt_alamatkaryawan;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_emailkaryawan;
    private javax.swing.JTextField txt_idkaryawan;
    private javax.swing.JTextField txt_namakaryawan;
    private javax.swing.JTextField txt_telpkaryawan;
    // End of variables declaration//GEN-END:variables
 private boolean isnamaMirip(String jenisEs) {
        try {
            String sql = "SELECT COUNT(*) FROM karyawan WHERE namakaryawan=?";
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
}

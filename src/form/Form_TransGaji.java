/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package form;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import static org.codehaus.groovy.ast.tools.GeneralUtils.param;
import javax.swing.event.ListSelectionListener;
import java.util.HashMap;
import java.sql.Connection;
import java.util.Map;
import config.koneksi;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Rifal
 */
public class Form_TransGaji extends javax.swing.JPanel {

    private int counterTransaksiGaji = 1;

    private boolean isTransaksiGajiExistsInDatabase(String idTransaksiGaji, int index) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        java.sql.ResultSet resultSet = null;

        try {
            connection = koneksi.getConnection();

            String sql = "SELECT idtransaksigaji FROM transaksigaji WHERE idtransaksigaji = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, idTransaksiGaji);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
    }

    private String generateidTransaksiGaji(Date tanggalProduksi) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePart = dateFormat.format(tanggalProduksi);

        String transgajiCode = "TRXG-" + datePart + "-" + counterTransaksiGaji;

        return transgajiCode;
    }

    private void generateIDTransaksiGaji() {

        Date selectedDate = txt_tanggalgajian.getDate();

        counterTransaksiGaji = 1;

        if (selectedDate != null) {
            String transaksiGaji = generateidTransaksiGaji(selectedDate);

            while (isTransaksiGajiExistsInDatabase(transaksiGaji, 1)) {

                counterTransaksiGaji++;
                transaksiGaji = generateidTransaksiGaji(selectedDate);
            }

            txt_idtransaksigaji.setText(transaksiGaji);
        } else {

        }
    }

    private float loadGajiDetails(String idGaji) {
        try {
            String sql = "SELECT gajipokok, tunjangan, potongan FROM gajikaryawan WHERE idgaji = ?";
            Connection mysqlconfig = koneksi.getConnection();
            PreparedStatement pst = mysqlconfig.prepareStatement(sql);
            pst.setString(1, idGaji);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                float gajipokok = rs.getFloat("gajipokok");
                float tunjangan = rs.getFloat("tunjangan");
                float potongan = rs.getFloat("potongan");

                float gajibersih = gajipokok + tunjangan - potongan;
                return gajibersih;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
        return 0.0f;
    }

    private void calculateGajiBersih() {
        String idGaji = txt_idgaji.getText();
        if (!idGaji.isEmpty()) {
            float gajibersih = loadGajiDetails(idGaji);
            txt_gajibersih.setText(String.valueOf(gajibersih));
        }
    }

    private void autoResizeAllColumns() {
        int columns = tbl_transgaji.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_transgaji.getColumnModel().getColumn(i);
            int width = (int) tbl_transgaji.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_transgaji, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_transgaji.getRowCount(); row++) {
                int preferedWidth = (int) tbl_transgaji.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_transgaji, tbl_transgaji.getValueAt(row, i), false, false, row, i)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            column.setPreferredWidth(width);
        }
    }

    private void loadData() {
        DefaultTableModel transGajiModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transGajiModel.addColumn("No");
        transGajiModel.addColumn("ID Transaksi Gaji");
        transGajiModel.addColumn("ID Karyawan");
        transGajiModel.addColumn("ID Gaji");
        transGajiModel.addColumn("Tanggal Gajian");
        transGajiModel.addColumn("Gaji Bersih");

        tbl_transgaji.setAutoCreateRowSorter(true);
        autoResizeAllColumns();

        try {
            int no = 1;
            String selectSql = "SELECT * FROM transaksigaji";

            java.sql.Connection mysqlconfig = koneksi.getConnection();
            java.sql.Statement stm = mysqlconfig.createStatement();
            java.sql.ResultSet res = stm.executeQuery(selectSql);

            while (res.next()) {
                transGajiModel.addRow(new Object[]{no++,
                    res.getString("idtransaksigaji"),
                    res.getString("idkaryawan"),
                    res.getString("idgaji"),
                    res.getDate("tanggalgajian"),
                    res.getFloat("gajibersih")});
            }

            tbl_transgaji.setModel(transGajiModel);

            for (int i = 0; i < transGajiModel.getColumnCount(); i++) {
                tbl_transgaji.getColumnModel().getColumn(i).setCellEditor(null);
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

            for (int i = 0; i < transGajiModel.getColumnCount(); i++) {
                tbl_transgaji.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        } catch (Exception e) {
        }
    }

    private void tambahData() {
        Connection conn = null;
        try {
            conn = koneksi.getConnection();
            conn.setAutoCommit(false);

            if (txt_idtransaksigaji.getText().isEmpty() || txt_tanggalgajian.getDate() == null
                    || txt_idgaji.getText().isEmpty() || txt_idkaryawan.getText().isEmpty() || txt_gajibersih.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom sebelum menyimpan data.");
                return;
            }

            String idtransaksigaji = txt_idtransaksigaji.getText();
            String idkaryawan = txt_idkaryawan.getText();
            String idgaji = txt_idgaji.getText();
            java.util.Date tanggalGaji = txt_tanggalgajian.getDate();
            float gajiBersih = Float.parseFloat(txt_gajibersih.getText());

            String sql = "INSERT INTO transaksigaji (idtransaksigaji, idkaryawan, idgaji, tanggalgajian, gajibersih) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, idtransaksigaji);
            pst.setString(2, idkaryawan);
            pst.setString(3, idgaji);
            pst.setDate(4, new java.sql.Date(tanggalGaji.getTime()));
            pst.setFloat(5, gajiBersih);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Penyimpanan Data Berhasil");
                conn.commit();
            } else {
                JOptionPane.showMessageDialog(this, "Penyimpanan Data Gagal");
                conn.rollback();
            }
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        loadData();
        bersihform();
    }

    private void bersihform() {
        Date tglGajian = txt_tanggalgajian.getDate();
        txt_idtransaksigaji.setText("");
        txt_idgaji.setText("");
        txt_idkaryawan.setText("");
        txt_tanggalgajian.setDate(null);
        txt_gajibersih.setText("");
        txt_tanggalgajian.setDate(tglGajian);

    }

    private void hapusData() {
        int selectedRow = tbl_transgaji.getSelectedRow();

        if (selectedRow != -1) {
            String kodepenggajianToDelete = tbl_transgaji.getValueAt(selectedRow, 1).toString();

            int confirmResult = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin menghapus data dengan Kode Transaksi Gaji " + kodepenggajianToDelete + "?",
                    "Konfirmasi Hapus Data",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmResult == JOptionPane.YES_OPTION) {
                try {
                    String sql = "DELETE FROM transaksigaji WHERE idtransaksigaji=?";
                    Connection mysqlconfig = koneksi.getConnection();
                    PreparedStatement pst = mysqlconfig.prepareStatement(sql);
                    pst.setString(1, kodepenggajianToDelete);
                    int rowsAffected = pst.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Data with Kode Transaksi Gaji " + kodepenggajianToDelete + " has been deleted.");
                        loadData();
                        bersihform();
                    } else {
                        JOptionPane.showMessageDialog(this, "Data with Kode Transaksi Gaji " + kodepenggajianToDelete + " not found.");
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

            if (txt_idtransaksigaji.getText().isEmpty() || txt_tanggalgajian.getDate() == null
                    || txt_idgaji.getText().isEmpty() || txt_gajibersih.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom sebelum menyimpan data.");
                return;
            }

            String kodepenggajian = txt_idtransaksigaji.getText();
            String kodegaji = txt_idgaji.getText();
            java.util.Date tanggalGaji = txt_tanggalgajian.getDate();
            float gajiBersih = Float.parseFloat(txt_gajibersih.getText());

            String sql = "UPDATE transaksigaji SET kodegaji = ?, tanggalgaji = ?, gajibersih = ? WHERE kodepenggajian = ?";
            java.sql.Connection mysqlconfig = koneksi.getConnection();
            java.sql.PreparedStatement pst = mysqlconfig.prepareStatement(sql);

            pst.setString(1, kodegaji);
            pst.setDate(2, new java.sql.Date(tanggalGaji.getTime()));
            pst.setFloat(3, gajiBersih);
            pst.setString(4, kodepenggajian);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang cocok dengan Kode Transaksi Gaji yang diberikan.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Perubahan data gagal: " + e.getMessage());
        }

        loadData();
        bersihform();
    }

    private void filterDataTransaksiGaji(String keyword, String selectedField) {
        DefaultTableModel transaksiGajiModel = (DefaultTableModel) tbl_transgaji.getModel();
        transaksiGajiModel.setRowCount(0);

        String sql = "SELECT * FROM TRANSAKSIGAJI WHERE ";

        switch (selectedField) {
            case "ID Transaksi Gaji":
                sql += "LOWER(IDTRANSAKSIGAJI) LIKE LOWER(?)";
                break;
            case "ID Karyawan":
                sql += "LOWER(IDKARYAWAN) LIKE LOWER(?)";
                break;
            case "ID Gaji":
                sql += "LOWER(IDGAJI) LIKE LOWER(?)";
                break;
            case "Tanggal Gajian":
                sql += "LOWER(TANGGALGAJIAN) LIKE LOWER(?)";
                break;
            default:
                return;
        }

        try (Connection conn = koneksi.getConnection(); PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, "%" + keyword + "%");

            try (ResultSet res = stm.executeQuery()) {
                int no = 1;
                while (res.next()) {
                    transaksiGajiModel.addRow(new Object[]{
                        no++,
                        res.getString("IDTRANSAKSIGAJI"),
                        res.getString("IDKARYAWAN"),
                        res.getString("IDGAJI"),
                        res.getString("TANGGALGAJIAN"),
                        res.getDouble("GAJIBERSIH")
                    });
                }
                autoResizeAllColumns();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates new form Form_Karyawan
     */
    public Form_TransGaji() {
        initComponents();
        txt_idkaryawan.setEditable(false);
                txt_idtransaksigaji.setEditable(false);
        txt_gajibersih.setEditable(false);
        txt_idgaji.setEditable(false);
        btn_hapus.setVisible(false);
        btn_batal.setVisible(false);
        Color headerColor = new Color(194, 217, 255);
        tbl_transgaji.getTableHeader().setBackground(headerColor);
        loadData();
        txt_idgaji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idgajiActionPerformed(evt);
            }
        });
        txt_idgaji.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                calculateGajiBersih();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateGajiBersih();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateGajiBersih();
            }
        });

        txt_tanggalgajian.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName()) && evt.getNewValue() != null) {

                    generateIDTransaksiGaji();
                }
            }
        });
        txt_cari.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String selectedField = (String) cb_cari.getSelectedItem();
                filterDataTransaksiGaji(txt_cari.getText(), selectedField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String selectedField = (String) cb_cari.getSelectedItem();
                filterDataTransaksiGaji(txt_cari.getText(), selectedField);
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
        dataTransGaji = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_transgaji = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btn_cetak = new javax.swing.JButton();
        txttanggal1 = new com.toedter.calendar.JDateChooser();
        txttanggal2 = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cb_cari = new javax.swing.JComboBox<>();
        txt_cari = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        tambahTransGaji = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_idtransaksigaji = new javax.swing.JTextField();
        txt_tanggalgajian = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_idgaji = new javax.swing.JTextField();
        btn_othergaji = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txt_gajibersih = new javax.swing.JTextField();
        txt_idkaryawan = new javax.swing.JTextField();
        btn_otherkar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btn_clear = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 500));
        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        dataTransGaji.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tbl_transgaji.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_transgaji.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_transgajiMouseClicked(evt);
            }
        });
        tbl_transgaji.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_transgajiKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_transgaji);

        jLabel1.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel1.setText("Data Transaksi Gaji");

        btn_cetak.setText("Cetak");
        btn_cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cetakActionPerformed(evt);
            }
        });

        jLabel8.setText("Tanggal Awal");

        jLabel9.setText("Tanggal Akhir");

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Transaksi Gaji", "ID Karyawan", "ID Gaji", "Tanggal Gajian" }));

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

        javax.swing.GroupLayout dataTransGajiLayout = new javax.swing.GroupLayout(dataTransGaji);
        dataTransGaji.setLayout(dataTransGajiLayout);
        dataTransGajiLayout.setHorizontalGroup(
            dataTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataTransGajiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addGroup(dataTransGajiLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dataTransGajiLayout.createSequentialGroup()
                        .addGroup(dataTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dataTransGajiLayout.createSequentialGroup()
                                .addComponent(txttanggal1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(txttanggal2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_cetak))
                            .addGroup(dataTransGajiLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(80, 80, 80)
                                .addComponent(jLabel9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)))
                .addContainerGap())
        );
        dataTransGajiLayout.setVerticalGroup(
            dataTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataTransGajiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(dataTransGajiLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(dataTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(dataTransGajiLayout.createSequentialGroup()
                        .addGroup(dataTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel21)
                            .addGroup(dataTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dataTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_cetak)
                            .addComponent(txttanggal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttanggal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        mainPanel.add(dataTransGaji, "card2");

        tambahTransGaji.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Tambah Transaksi Gaji");

        jLabel3.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel3.setText("ID Transaksi Gaji");

        txt_idtransaksigaji.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_idtransaksigaji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idtransaksigajiActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel4.setText("Tanggal Gajian");

        jLabel5.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel5.setText("Kode Gaji");

        txt_idgaji.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_idgaji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idgajiActionPerformed(evt);
            }
        });

        btn_othergaji.setText("...");
        btn_othergaji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_othergajiActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel6.setText("Gaji Bersih");

        txt_gajibersih.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_gajibersih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_gajibersihActionPerformed(evt);
            }
        });

        txt_idkaryawan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        btn_otherkar.setText("...");
        btn_otherkar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_otherkarActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel7.setText("ID Karyawan");

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

        javax.swing.GroupLayout tambahTransGajiLayout = new javax.swing.GroupLayout(tambahTransGaji);
        tambahTransGaji.setLayout(tambahTransGajiLayout);
        tambahTransGajiLayout.setHorizontalGroup(
            tambahTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahTransGajiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(txt_gajibersih, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txt_idtransaksigaji, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tambahTransGajiLayout.createSequentialGroup()
                        .addGroup(tambahTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tambahTransGajiLayout.createSequentialGroup()
                                .addComponent(txt_idgaji, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_othergaji))
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tambahTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(tambahTransGajiLayout.createSequentialGroup()
                                .addComponent(txt_idkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_otherkar))))
                    .addComponent(jLabel4)
                    .addComponent(txt_tanggalgajian, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(322, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahTransGajiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        tambahTransGajiLayout.setVerticalGroup(
            tambahTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahTransGajiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(39, 39, 39)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_tanggalgajian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_idtransaksigaji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(tambahTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_idgaji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_othergaji)
                    .addComponent(txt_idkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_otherkar))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_gajibersih, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                .addGroup(tambahTransGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        mainPanel.add(tambahTransGaji, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_othergajiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_othergajiActionPerformed

        Dialog_DataGaji dialogDataGaji = new Dialog_DataGaji(new javax.swing.JFrame(), true);
        dialogDataGaji.setVisible(true);

        if (dialogDataGaji.isGajiSelected()) {

            String selectedidGaji = dialogDataGaji.getSelectedidGaji();

            txt_idgaji.setText(selectedidGaji);

        }
    }//GEN-LAST:event_btn_othergajiActionPerformed

    private void btn_cetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cetakActionPerformed
        try {
            Connection conn = koneksi.getConnection();
            String reportPath = "/report/TransaksiGaji.jrxml";

            Date tgl1 = txttanggal1.getDate();
            Date tgl2 = txttanggal2.getDate();

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("tgl1", tgl1);
            parameters.put("tgl2", tgl2);

            JasperPrint print = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(getClass().getResourceAsStream(reportPath)), parameters, conn);

            JasperViewer.viewReport(print, false);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

    }//GEN-LAST:event_btn_cetakActionPerformed

    private void txt_gajibersihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_gajibersihActionPerformed


    }//GEN-LAST:event_txt_gajibersihActionPerformed

    private void txt_idgajiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idgajiActionPerformed

    }//GEN-LAST:event_txt_idgajiActionPerformed

    private void tbl_transgajiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_transgajiMouseClicked
        btn_hapus.setVisible(true);
        btn_batal.setVisible(true);
        btn_tambah.setText("Edit");
        int selectedRow = tbl_transgaji.getSelectedRow();
        if (selectedRow != -1) {
            String idtransaksigaji = tbl_transgaji.getValueAt(selectedRow, 1).toString();
            String idgaji = tbl_transgaji.getValueAt(selectedRow, 2).toString();
            String namakaryawan = tbl_transgaji.getValueAt(selectedRow, 3).toString();
            java.util.Date tanggalgaji = (java.util.Date) tbl_transgaji.getValueAt(selectedRow, 4);
            float gajibersih = (float) tbl_transgaji.getValueAt(selectedRow, 5);

            txt_idtransaksigaji.setText(idtransaksigaji);
            txt_idgaji.setText(idgaji);
            txt_idkaryawan.setText(namakaryawan);
            txt_tanggalgajian.setDate(tanggalgaji);
            txt_gajibersih.setText(String.valueOf(gajibersih));

        }
    }//GEN-LAST:event_tbl_transgajiMouseClicked

    private void txt_idtransaksigajiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idtransaksigajiActionPerformed

    }//GEN-LAST:event_txt_idtransaksigajiActionPerformed

    private void btn_otherkarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_otherkarActionPerformed

        Dialog_DataKaryawan dialogDataKaryawan = new Dialog_DataKaryawan(new javax.swing.JFrame(), true);
        dialogDataKaryawan.setVisible(true);

        if (dialogDataKaryawan.isKaryawanSelected()) {

            String selectedIdKaryawan = dialogDataKaryawan.getSelectedIdKaryawan();

            txt_idkaryawan.setText(selectedIdKaryawan);

        }
    }//GEN-LAST:event_btn_otherkarActionPerformed

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed

    }//GEN-LAST:event_txt_cariActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed

        if (btn_tambah.getText().equals("Tambah")) {
            btn_ubah.setText("Tambah");
            txt_tanggalgajian.setEnabled(true);
            btn_clear.setVisible(true);
        } else if (btn_tambah.getText().equals("Edit")) {
            btn_ubah.setText("Ubah");
            txt_tanggalgajian.setEnabled(false);
            btn_clear.setVisible(false);
        }
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(tambahTransGaji);
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
        mainPanel.add(dataTransGaji);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_backActionPerformed

    private void tbl_transgajiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_transgajiKeyPressed
        // TODO add your handling code here:
           btn_tambahActionPerformed(null);
    }//GEN-LAST:event_tbl_transgajiKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_cetak;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_othergaji;
    private javax.swing.JButton btn_otherkar;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JComboBox<String> cb_cari;
    private javax.swing.JPanel dataTransGaji;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JPanel tambahTransGaji;
    private javax.swing.JTable tbl_transgaji;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_gajibersih;
    private javax.swing.JTextField txt_idgaji;
    private javax.swing.JTextField txt_idkaryawan;
    private javax.swing.JTextField txt_idtransaksigaji;
    private com.toedter.calendar.JDateChooser txt_tanggalgajian;
    private com.toedter.calendar.JDateChooser txttanggal1;
    private com.toedter.calendar.JDateChooser txttanggal2;
    // End of variables declaration//GEN-END:variables

}

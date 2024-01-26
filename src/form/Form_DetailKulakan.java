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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Rifal
 */
public class Form_DetailKulakan extends javax.swing.JPanel {

    private int counterPembayaran = 1;

    private void autoResizeAllColumns() {
        int columns = tbl_pembelian.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_pembelian.getColumnModel().getColumn(i);
            int width = (int) tbl_pembelian.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_pembelian, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_pembelian.getRowCount(); row++) {
                int preferedWidth = (int) tbl_pembelian.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_pembelian, tbl_pembelian.getValueAt(row, i), false, false, row, i)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            column.setPreferredWidth(width);
        }
    }

     private boolean isIdBayarPembelianExistsInDatabase(String idBayarPembelian) {
        try (
                Connection connection = koneksi.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM pembayaranpembelian WHERE idpembayaranpembelian = ?");) {
            preparedStatement.setString(1, idBayarPembelian);
            try (
                    ResultSet resultSet = preparedStatement.executeQuery()) {
               
                resultSet.next();
                int rowCount = resultSet.getInt(1);

               
                return rowCount > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            return false;
        }
    }

    private void generateIdPembayaranPembelian() {
        Date selectedDate = txt_tglpembayaran.getDate();
        counterPembayaran = 1;

        if (selectedDate != null) {
            String idpembayaranpembelian;
            do {
                idpembayaranpembelian = generateIdPembayaranPembelian(selectedDate);
                counterPembayaran++;
            } while (isIdBayarPembelianExistsInDatabase(idpembayaranpembelian));

            txt_idpembayaran.setText(idpembayaranpembelian);
        } else {
           //System.out.println("Oke sip.");
        }
    }

    private String generateIdPembayaranPembelian(Date tanggalpembayaranPesanan) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String bayarCode = String.format("BYRB-%s-%d", dateFormat.format(tanggalpembayaranPesanan), counterPembayaran);
        return bayarCode;
    }


    private void loadPembelian() {
    DefaultTableModel pembelianModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; 
        }
    };
    pembelianModel.addColumn("No");
    pembelianModel.addColumn("ID Pembelian");
    pembelianModel.addColumn("ID Supplier");
    pembelianModel.addColumn("ID Karyawan");
    pembelianModel.addColumn("Tanggal Pembelian");
    pembelianModel.addColumn("Total Pembelian");
    pembelianModel.addColumn("Total Harga Pembelian");
    pembelianModel.addColumn("Status Pembelian");

    tbl_pembelian.setAutoCreateRowSorter(true);

    try {
        int no = 1;
        String sql = "SELECT * FROM PEMBELIAN"; 
        java.sql.Connection mysqlconfig = (Connection) koneksi.getConnection();
        java.sql.Statement stm = mysqlconfig.createStatement();
        java.sql.ResultSet res = stm.executeQuery(sql);
        while (res.next()) {
            pembelianModel.addRow(new Object[]{no++,
                res.getString("IDPEMBELIAN"),
                res.getString("IDSUPPLIER"),
                res.getString("IDKARYAWAN"),
                res.getString("TGLPEMBELIAN"),
                res.getInt("TOTALPEMBELIAN"),
                res.getDouble("TOTALHARGAPEMBELIAN"),
                res.getString("STATUSPEMBELIAN")});
        }
        tbl_pembelian.setModel(pembelianModel);
        autoResizeAllColumns();

        
        for (int i = 0; i < pembelianModel.getColumnCount(); i++) {
            tbl_pembelian.getColumnModel().getColumn(i).setCellEditor(null);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

      
        for (int i = 0; i < pembelianModel.getColumnCount(); i++) {
            tbl_pembelian.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    } catch (Exception e) {
        
    }
}

  private void loadDetailPembelian(String selectedIdPembelian) {
    DefaultTableModel detPembModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; 
        }
    };
    detPembModel.addColumn("No");
    detPembModel.addColumn("Kode Detail Pembelian");
    detPembModel.addColumn("ID Bahan");
    detPembModel.addColumn("Harga Beli");
    detPembModel.addColumn("Subtotal Pembelian");
    detPembModel.addColumn("Subtotal Harga Pembelian");

    tbl_detPemb.setAutoCreateRowSorter(true);

    try {
        int no = 1;
        String sql = "SELECT * FROM DETAILPEMBELIAN WHERE IDPEMBELIAN = '" + selectedIdPembelian + "'";
        java.sql.Connection mysqlconfig = (Connection) koneksi.getConnection();
        java.sql.Statement stm = mysqlconfig.createStatement();
        java.sql.ResultSet res = stm.executeQuery(sql);
        while (res.next()) {
            detPembModel.addRow(new Object[]{no++,
                res.getString("KODEDETAILBELI"),
                res.getString("IDBAHAN"),
                res.getDouble("HARGABELI"),
                res.getInt("SUBTOTALPEMBELIAN"),
                res.getDouble("SUBTOTALHARGAPEMBELIAN")});
        }
        tbl_detPemb.setModel(detPembModel);
        autoResizeAllColumns();

        
        for (int i = 0; i < detPembModel.getColumnCount(); i++) {
            tbl_detPemb.getColumnModel().getColumn(i).setCellEditor(null);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

       
        for (int i = 0; i < detPembModel.getColumnCount(); i++) {
            tbl_detPemb.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    } catch (Exception e) {
       
    }
}


    private void loadDetailPembayaran(String selectedIdPembelian) {
    DefaultTableModel pembayaranPembelianModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; 
        }
    };
    pembayaranPembelianModel.addColumn("No");
    pembayaranPembelianModel.addColumn("ID Pembayaran Pembelian");
    pembayaranPembelianModel.addColumn("ID Pembelian");
    pembayaranPembelianModel.addColumn("Tanggal Pembayaran");
    pembayaranPembelianModel.addColumn("Total Pembayaran");
    pembayaranPembelianModel.addColumn("Sisa Pembayaran");
    pembayaranPembelianModel.addColumn("Kembalian Pembayaran");

    tbl_pembayaran.setAutoCreateRowSorter(true);

    try {
        int no = 1;
        String sql = "SELECT * FROM PEMBAYARANPEMBELIAN WHERE IDPEMBELIAN = '" + selectedIdPembelian + "'";
        java.sql.Connection mysqlconfig = (Connection) koneksi.getConnection();
        java.sql.Statement stm = mysqlconfig.createStatement();
        java.sql.ResultSet res = stm.executeQuery(sql);
        while (res.next()) {
            pembayaranPembelianModel.addRow(new Object[]{no++,
                res.getString("IDPEMBAYARANPEMBELIAN"),
                res.getString("IDPEMBELIAN"),
                res.getString("TGLPEMBAYARANPEMBELIAN"),
                res.getDouble("TOTALPEMBAYARANPEMBELIAN"),
                res.getDouble("SISAPEMBAYARANPEMBELIAN"),
                res.getDouble("KEMBALIANPEMBAYARANPEMBELIAN")});
        }
        tbl_pembayaran.setModel(pembayaranPembelianModel);
        autoResizeAllColumns();

       
        for (int i = 0; i < pembayaranPembelianModel.getColumnCount(); i++) {
            tbl_pembayaran.getColumnModel().getColumn(i).setCellEditor(null);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        
        for (int i = 0; i < pembayaranPembelianModel.getColumnCount(); i++) {
            tbl_pembayaran.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    } catch (Exception e) {
        
    }
}


    private void hapusData() {
    int selectedRow = tbl_pembelian.getSelectedRow();

    if (selectedRow != -1) {
        int option = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            String selectedIdPembelian = tbl_pembelian.getValueAt(selectedRow, 1).toString();

            try (Connection connection = koneksi.getConnection()) {
                // No need to retrieve and update stock for each detail, as it is handled by cascade delete.

                // Delete the purchase order
                String deletePembelianSQL = "DELETE FROM PEMBELIAN WHERE IDPEMBELIAN = ?";
                try (PreparedStatement statementPembelian = connection.prepareStatement(deletePembelianSQL)) {
                    statementPembelian.setString(1, selectedIdPembelian);
                    statementPembelian.executeUpdate();
                }

                loadPembelian();

                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
}


    private void updateSisaKembalian() {
        try {
            String totalHargaStr = txt_totalharga.getText();
            String totalBayarStr = txt_totalbayar.getText();

            
            if (totalBayarStr.isEmpty()) {
                
                txt_sisa.setText("");
                txt_kembalian.setText("");
                rd_belum.setSelected(true);
                return;
            }

            double totalHarga = Double.parseDouble(totalHargaStr);
            double totalBayar = Double.parseDouble(totalBayarStr);

            double sisa = Math.max(0, totalHarga - totalBayar);
            double kembalian = Math.max(0, totalBayar - totalHarga);

            txt_sisa.setText(String.valueOf(sisa));
            txt_kembalian.setText(String.valueOf(kembalian));

          
            if (sisa == 0) {
                rd_lunas.setSelected(true);
            } else {
                rd_belum.setSelected(true);
            }
        } catch (NumberFormatException ex) {
      
            txt_sisa.setText("");
            txt_kembalian.setText("");
            rd_belum.setSelected(true);
        }
    }
private void filterData(String keyword, String selectedField) {
    DefaultTableModel pembelianModel = (DefaultTableModel) tbl_pembelian.getModel();
    pembelianModel.setRowCount(0);

    String sql = "SELECT * FROM PEMBELIAN WHERE ";

    switch (selectedField) {
        case "ID Pembelian":
            sql += "LOWER(IDPEMBELIAN) LIKE LOWER(?)";
            break;
        case "ID Supplier":
            sql += "LOWER(IDSUPPLIER) LIKE LOWER(?)";
            break;
        case "ID Karyawan":
            sql += "LOWER(IDKARYAWAN) LIKE LOWER(?)";
            break;
        case "Tanggal Pembelian":
            sql += "LOWER(TGLPEMBELIAN) LIKE LOWER(?)";
            break;
        case "Status Pembelian":
          if ("Lunas".equalsIgnoreCase(keyword)) {
                sql += "LOWER(statuspembelian) LIKE LOWER(?) AND LOWER(statuspembelian) = 'lunas'";
            } else {
                sql += "LOWER(statuspembelian) LIKE LOWER(?)";
            }
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
                pembelianModel.addRow(new Object[]{
                        no++,
                        res.getString("IDPEMBELIAN"),
                        res.getString("IDSUPPLIER"),
                        res.getString("IDKARYAWAN"),
                        res.getString("TGLPEMBELIAN"),
                        res.getInt("TOTALPEMBELIAN"),
                        res.getDouble("TOTALHARGAPEMBELIAN"),
                        res.getString("STATUSPEMBELIAN")
                });
            }
            autoResizeAllColumns(); 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    /**
     * Creates new form Form_TambahPesan
     */
    public Form_DetailKulakan() {
        initComponents();
        txt_idcetak.setVisible(false);
        btn_batal.setVisible(false);
        btn_hapus.setVisible(false);
        txt_idpembayaran.setEditable(false);
btn_bayar.setVisible(false);
        tbl_pembelian.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tbl_pembelian.getSelectedRow();
                    if (selectedRow != -1) {
                        String selectedIdPembelian = tbl_pembelian.getValueAt(selectedRow, 1).toString();
                        loadDetailPembelian(selectedIdPembelian);
                        loadDetailPembayaran(selectedIdPembelian);
                    }
                }
            }
        });

        loadPembelian();

        txt_tglpembayaran.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                
                if ("date".equals(evt.getPropertyName())) {
                   
                    generateIdPembayaranPembelian();
                }
            }
        });
        txt_totalbayar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSisaKembalian();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSisaKembalian();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                
            }
        });
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

    private void bersihkanTabel(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }

    private void bersihForm() {
        txt_idpembayaran.setText("");
        txt_idpembelian.setText("");
        txt_totalbayar.setText("");
        txt_sisa.setText("");
        txt_kembalian.setText("");
        txt_totalharga.setText("");
        txt_tglpembayaran.setDate(null);
        statusPembayaran.clearSelection();
 btn_bayar.setVisible(false);
  btn_batal.setVisible(false);
        btn_hapus.setVisible(false);
        txt_idcetak.setText("");
    }

 private void kurangiStokBahan(String idPembelian) {
    try {
      
        String selectDetailPembelianSQL = "SELECT IDBAHAN, SUBTOTALPEMBELIAN FROM DETAILPEMBELIAN WHERE IDPEMBELIAN = ?";
        try (Connection connection = koneksi.getConnection(); PreparedStatement statement = connection.prepareStatement(selectDetailPembelianSQL)) {
            statement.setString(1, idPembelian);
            ResultSet resultSet = statement.executeQuery();

           
            while (resultSet.next()) {
                String idBahan = resultSet.getString("IDBAHAN");
                int subtotalPembelian = resultSet.getInt("SUBTOTALPEMBELIAN");

               
                String updateStokBahanSQL = "UPDATE BAHANBAKU SET STOCKBAHAN = STOCKBAHAN - ? WHERE IDBAHAN = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateStokBahanSQL)) {
                    updateStatement.setInt(1, subtotalPembelian);
                    updateStatement.setString(2, idBahan);
                    updateStatement.executeUpdate();
                }
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error while updating raw material stock: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void bayarPelunasanPembelian() {
    Connection connection = null;
    try {
        connection = koneksi.getConnection();
        connection.setAutoCommit(false); // Set auto-commit to false

        String idPembelian = txt_idpembelian.getText();
        String idPembayaran = txt_idpembayaran.getText();
        Date tanggalPembayaran = txt_tglpembayaran.getDate();
        double totalBayar = Double.parseDouble(txt_totalbayar.getText());
        double sisaPembayaran = Double.parseDouble(txt_sisa.getText());
        double kembalian = Double.parseDouble(txt_kembalian.getText());

        if (sisaPembayaran != 0) {
            JOptionPane.showMessageDialog(this, "Sisa pembayaran harus 0 untuk melakukan pembayaran.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String updateStatusSQL = "UPDATE PEMBELIAN SET STATUSPEMBELIAN = 'Lunas' WHERE IDPEMBELIAN = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateStatusSQL)) {
            statement.setString(1, idPembelian);
            statement.executeUpdate();
        }

        kurangiStokBahan(idPembelian);

        String insertPembayaranSQL = "INSERT INTO PEMBAYARANPEMBELIAN (IDPEMBAYARANPEMBELIAN, IDPEMBELIAN, TGLPEMBAYARANPEMBELIAN, TOTALPEMBAYARANPEMBELIAN, SISAPEMBAYARANPEMBELIAN, KEMBALIANPEMBAYARANPEMBELIAN) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertPembayaranSQL)) {
            statement.setString(1, idPembayaran);
            statement.setString(2, idPembelian);
            statement.setDate(3, new java.sql.Date(tanggalPembayaran.getTime()));
            statement.setDouble(4, totalBayar);
            statement.setDouble(5, sisaPembayaran);
            statement.setDouble(6, kembalian);
            statement.executeUpdate();
        }

        loadDetailPembayaran(idPembelian);

        connection.commit();
        JOptionPane.showMessageDialog(this, "Pembayaran lunas berhasil.", "Informasi", JOptionPane.INFORMATION_MESSAGE);

        bersihForm();
    } catch (NumberFormatException | SQLException ex) {
       
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace(); 
            }
        }
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle closing connection error if necessary
            }
        }
    }
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statusPembayaran = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        dataPesanan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_pembelian = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btn_hapus = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_pembayaran = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_detPemb = new javax.swing.JTable();
        btn_bayar = new javax.swing.JButton();
        btn_cetak = new keeptoo.KButton();
        txt_idcetak = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_cari = new javax.swing.JTextField();
        cb_cari = new javax.swing.JComboBox<>();
        tambahBayar = new javax.swing.JPanel();
        btn_back = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        kGradientPanel5 = new keeptoo.KGradientPanel();
        jLabel26 = new javax.swing.JLabel();
        txt_idpembelian = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_totalharga = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        kGradientPanel3 = new keeptoo.KGradientPanel();
        jLabel6 = new javax.swing.JLabel();
        txt_idpembayaran = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txt_tglpembayaran = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        txt_sisa = new javax.swing.JTextField();
        txt_totalbayar = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txt_kembalian = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        rd_lunas = new javax.swing.JRadioButton();
        rd_belum = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        btn_lunasi = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        dataPesanan.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tbl_pembelian.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_pembelian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_pembelianMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_pembelian);

        jLabel1.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel1.setText("Detail Pembelian");

        btn_hapus.setText("Hapus");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        btn_batal.setText("Batal");
        btn_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_batalActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Data Pembelian");

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

        tbl_pembayaran.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_pembayaran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_pembayaranMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_pembayaran);

        jLabel3.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel3.setText("Riwayat Pembayaran");

        tbl_detPemb.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tbl_detPemb);

        btn_bayar.setText("Bayar");
        btn_bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bayarActionPerformed(evt);
            }
        });

        btn_cetak.setText("Cetak");
        btn_cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cetakActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Pembelian", "ID Supplier", "ID Karyawan", "Tanggal Pembelian", "Status Pembelian" }));

        javax.swing.GroupLayout dataPesananLayout = new javax.swing.GroupLayout(dataPesanan);
        dataPesanan.setLayout(dataPesananLayout);
        dataPesananLayout.setHorizontalGroup(
            dataPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPesananLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane4)
                    .addGroup(dataPesananLayout.createSequentialGroup()
                        .addGroup(dataPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(dataPesananLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cetak, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(txt_idcetak, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(btn_bayar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_hapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_batal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        dataPesananLayout.setVerticalGroup(
            dataPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataPesananLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, dataPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_idcetak, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_cetak, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(dataPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dataPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_batal)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(dataPesanan, "card2");

        tambahBayar.setBackground(new java.awt.Color(255, 255, 255));

        btn_back.setText("Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        kGradientPanel5.setkBorderRadius(0);
        kGradientPanel5.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel5.setkStartColor(new java.awt.Color(153, 255, 153));

        jLabel26.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Kulakan");

        javax.swing.GroupLayout kGradientPanel5Layout = new javax.swing.GroupLayout(kGradientPanel5);
        kGradientPanel5.setLayout(kGradientPanel5Layout);
        kGradientPanel5Layout.setHorizontalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel5Layout.setVerticalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        txt_idpembelian.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel5.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel5.setText("ID Pembelian");

        jLabel21.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel21.setText("Total yang belum dibayar");

        txt_totalharga.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_idpembelian, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(kGradientPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(11, 11, 11)
                .addComponent(txt_idpembelian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(30, 30, 30)))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        kGradientPanel3.setkBorderRadius(0);
        kGradientPanel3.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel3.setkStartColor(new java.awt.Color(153, 255, 153));

        jLabel6.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Pembayaran");

        javax.swing.GroupLayout kGradientPanel3Layout = new javax.swing.GroupLayout(kGradientPanel3);
        kGradientPanel3.setLayout(kGradientPanel3Layout);
        kGradientPanel3Layout.setHorizontalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(321, 321, 321))
        );
        kGradientPanel3Layout.setVerticalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel6)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jLabel15.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel15.setText("ID Pembayaran");

        jLabel16.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel16.setText("Tanggal pembayaran");

        jLabel17.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel17.setText("Total Pembayaran");

        txt_sisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_sisaActionPerformed(evt);
            }
        });

        txt_totalbayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_totalbayarKeyTyped(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel18.setText("Sisa yang harus dibayar");

        jLabel19.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel19.setText("Kembalian");

        jLabel22.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel22.setText("Status Pembayaran");

        statusPembayaran.add(rd_lunas);
        rd_lunas.setText("Lunas");
        rd_lunas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_lunasActionPerformed(evt);
            }
        });

        statusPembayaran.add(rd_belum);
        rd_belum.setText("Belum Lunas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_idpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(txt_tglpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(txt_totalbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(txt_sisa, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)))
                    .addComponent(jLabel22)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(rd_lunas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rd_belum)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(7, 7, 7)
                        .addComponent(txt_idpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_tglpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_totalbayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_sisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(jLabel22)
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rd_lunas)
                    .addComponent(rd_belum))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel7.setText("Lunasi Kulakan");

        btn_lunasi.setText("Bayar");
        btn_lunasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lunasiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tambahBayarLayout = new javax.swing.GroupLayout(tambahBayar);
        tambahBayar.setLayout(tambahBayarLayout);
        tambahBayarLayout.setHorizontalGroup(
            tambahBayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahBayarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahBayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahBayarLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_lunasi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_back))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahBayarLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(tambahBayarLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tambahBayarLayout.setVerticalGroup(
            tambahBayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahBayarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(42, 42, 42)
                .addGroup(tambahBayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 567, Short.MAX_VALUE)
                .addGroup(tambahBayarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back)
                    .addComponent(btn_lunasi))
                .addContainerGap())
        );

        mainPanel.add(tambahBayar, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_pembelianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_pembelianMouseClicked
        int baris = tbl_pembelian.getSelectedRow();
         if (baris != -1) {
        btn_batal.setVisible(true);
        btn_hapus.setVisible(true);
        
         String cetak = tbl_pembelian.getValueAt(baris, 1).toString();
txt_idcetak.setText(cetak);
        
        String idkulakan = tbl_pembelian.getValueAt(baris, 1).toString();
        txt_idpembelian.setText(idkulakan);

        String statusPembayaran = tbl_pembelian.getValueAt(baris, 7).toString();

        
        if ("Lunas".equals(statusPembayaran)) {
            rd_lunas.setSelected(true);
        } else {
            rd_belum.setSelected(true);
             btn_bayar.setVisible(true);
        }
        
        loadDetailPembayaran(idkulakan);

int rowCountPembayaran = tbl_pembayaran.getRowCount();
if (rowCountPembayaran > 0) {
    int lastRowPembayaran = rowCountPembayaran - 1;
    String totalharga = tbl_pembayaran.getValueAt(lastRowPembayaran, 5).toString();
    txt_totalharga.setText(totalharga);
} else {
    
    txt_totalharga.setText("0");
}
       
        rd_lunas.setEnabled(false);
        rd_belum.setEnabled(false);

        txt_idpembelian.setEditable(false);
        txt_totalharga.setEditable(false);
        txt_sisa.setEditable(false);
        txt_kembalian.setEditable(false);
         }
    }//GEN-LAST:event_tbl_pembelianMouseClicked

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        
        hapusData();
        bersihkanTabel(tbl_detPemb);
        bersihkanTabel(tbl_pembayaran);
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed

        loadPembelian();
        bersihkanTabel(tbl_detPemb);
        bersihkanTabel(tbl_pembayaran);
        bersihForm();
    }//GEN-LAST:event_btn_batalActionPerformed

    private void tbl_pembayaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_pembayaranMouseClicked
        
    }//GEN-LAST:event_tbl_pembayaranMouseClicked

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        
        mainPanel.removeAll();
        mainPanel.add(dataPesanan);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_bayarActionPerformed
        
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(tambahBayar);
        mainPanel.repaint();
        mainPanel.revalidate();

    }//GEN-LAST:event_btn_bayarActionPerformed

    private void txt_sisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_sisaActionPerformed
        
    }//GEN-LAST:event_txt_sisaActionPerformed

    private void rd_lunasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd_lunasActionPerformed
        
    }//GEN-LAST:event_rd_lunasActionPerformed

    private void btn_lunasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lunasiActionPerformed
       
        bayarPelunasanPembelian();
        loadPembelian();
    }//GEN-LAST:event_btn_lunasiActionPerformed

    private void txt_totalbayarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_totalbayarKeyTyped
        
        char c = evt.getKeyChar();

       
        if (!Character.isDigit(c)) {
            evt.consume();  
        }
    }//GEN-LAST:event_txt_totalbayarKeyTyped

    private void btn_cetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cetakActionPerformed
       
        try {
            Connection conn = koneksi.getConnection();
            String reportPath = "/report/Kulakan.jrxml";

            
            String idpembelian = txt_idcetak.getText();

            
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("kode", idpembelian);

     
            JasperPrint print = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(getClass().getResourceAsStream(reportPath)), parameters, conn);

            JasperViewer.viewReport(print, false);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_cetakActionPerformed

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        
    }//GEN-LAST:event_txt_cariActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_bayar;
    private keeptoo.KButton btn_cetak;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_lunasi;
    private javax.swing.JComboBox<String> cb_cari;
    private javax.swing.JPanel dataPesanan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private keeptoo.KGradientPanel kGradientPanel3;
    private keeptoo.KGradientPanel kGradientPanel5;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JRadioButton rd_belum;
    private javax.swing.JRadioButton rd_lunas;
    private javax.swing.ButtonGroup statusPembayaran;
    private javax.swing.JPanel tambahBayar;
    private javax.swing.JTable tbl_detPemb;
    private javax.swing.JTable tbl_pembayaran;
    private javax.swing.JTable tbl_pembelian;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_idcetak;
    private javax.swing.JTextField txt_idpembayaran;
    private javax.swing.JTextField txt_idpembelian;
    private javax.swing.JTextField txt_kembalian;
    private javax.swing.JTextField txt_sisa;
    private com.toedter.calendar.JDateChooser txt_tglpembayaran;
    private javax.swing.JTextField txt_totalbayar;
    private javax.swing.JTextField txt_totalharga;
    // End of variables declaration//GEN-END:variables
}

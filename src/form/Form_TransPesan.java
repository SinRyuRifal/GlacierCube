/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package form;

import com.toedter.components.JSpinField;
import config.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Rifal
 */
public class Form_TransPesan extends javax.swing.JPanel {

    private int counterPesanan = 1;
    private int counterPembayaran = 1;
    private int counterDetail = 1;

    private void updateTotalPesanan() {
        int totalPesanan = 0;
        int rowCount = keranjangModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            totalPesanan += (int) keranjangModel.getValueAt(i, 5); 
        }
        txt_totalpesanan.setText(String.valueOf(totalPesanan));
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

    private void updateTotalHarga() {
        double totalHarga = 0.0;
        int rowCount = keranjangModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            totalHarga += (double) keranjangModel.getValueAt(i, 6); 
        }
        txt_totalharga.setText(String.valueOf(totalHarga));
        updateSisaKembalian();
    }

    private boolean isProductInKeranjang(String idProduk) {
        for (int i = 0; i < tbl_keranjang.getRowCount(); i++) {
            String existingIdProduk = tbl_keranjang.getValueAt(i, 2).toString(); 
            if (existingIdProduk.equals(idProduk)) {
                return true;
            }
        }
        return false;
    }

   private void tambahKeranjang() {
    if (txt_kodedetail.getText().isEmpty() || txt_idpesanan.getText().isEmpty() || txt_idproduk.getText().isEmpty()
            || txt_namaproduk.getText().isEmpty() || txt_hargajual.getText().isEmpty() || txt_subtotalpes.getText().isEmpty()
            || txt_subtotalharga.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum menambahkan data.");
        return;
    }
    try {
        Double.parseDouble(txt_hargajual.getText());
        Integer.parseInt(txt_subtotalpes.getText());
        Double.parseDouble(txt_subtotalharga.getText());
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Format harga atau subtotal tidak valid.");
        return;
    }

    String kodeDetail = txt_kodedetail.getText();
    String idPesanan = txt_idpesanan.getText();
    String idProduk = txt_idproduk.getText();
    String namaProduk = txt_namaproduk.getText();
    double hargaJual = Double.parseDouble(txt_hargajual.getText());
    int subtotalPesanan = Integer.parseInt(txt_subtotalpes.getText());
    double subtotalHarga = Double.parseDouble(txt_subtotalharga.getText());

    
    int stockProduk = getStockProdukFromDatabase(idProduk);

    
    if (subtotalPesanan > stockProduk) {
        JOptionPane.showMessageDialog(this, "Subtotal Pesanan melebihi stock produk yang tersedia.");
        return;
    }

    
    if (isProductInKeranjang(idProduk)) {
        JOptionPane.showMessageDialog(this, "Produk dengan ID Produk yang sama sudah ada dalam keranjang.");
        return;
    }

    
    updateKeranjangTable(kodeDetail, idPesanan, idProduk, namaProduk, hargaJual, subtotalPesanan, subtotalHarga);

   
    updateTotalPesanan();
    updateTotalHarga();

    
    bersihform();
}
private int getStockProdukFromDatabase(String idProduk) {
    int stockProduk = 0;

    try {
       
        String sql = "SELECT stockproduk FROM produk WHERE idproduk = ?";
        try (Connection conn = koneksi.getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, idProduk);

            try (ResultSet res = stm.executeQuery()) {
                if (res.next()) {
                    stockProduk = res.getInt("stockproduk");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return stockProduk;
}


    private void ubahKeranjang() {
       
        if (txt_kodedetail.getText().isEmpty() || txt_idpesanan.getText().isEmpty() || txt_idproduk.getText().isEmpty()
                || txt_namaproduk.getText().isEmpty() || txt_hargajual.getText().isEmpty() || txt_subtotalpes.getText().isEmpty()
                || txt_subtotalharga.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap isi semua kolom teks sebelum mengubah data.");
            return;
        }

       
        try {
            Double.parseDouble(txt_hargajual.getText());
            Integer.parseInt(txt_subtotalpes.getText());
            Double.parseDouble(txt_subtotalharga.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format harga atau subtotal tidak valid.");
            return;
        }

      
        String kodeDetail = txt_kodedetail.getText();
        String idPesanan = txt_idpesanan.getText();
        String idProduk = txt_idproduk.getText();
        String namaProduk = txt_namaproduk.getText();
        double hargaJual = Double.parseDouble(txt_hargajual.getText());
        int subtotalPesanan = Integer.parseInt(txt_subtotalpes.getText());
        double subtotalHarga = Double.parseDouble(txt_subtotalharga.getText());

     
        if (!isProductInKeranjang(idProduk)) {
            JOptionPane.showMessageDialog(this, "Produk dengan ID Produk yang sama tidak ditemukan dalam keranjang.");
            return;
        }

        
        updateKeranjangTable(kodeDetail, idPesanan, idProduk, namaProduk, hargaJual, subtotalPesanan, subtotalHarga);

      
        updateTotalPesanan();
        updateTotalHarga();

      
        bersihform();
    }

    private void hapusBarisKeranjang() {
        
        int selectedRow = tbl_keranjang.getSelectedRow();

       
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus.");
            return;
        }

     
        keranjangModel.removeRow(selectedRow);

   
        updateTotalPesanan();
        updateTotalHarga();
    }

    private void updateKeranjangTable(String kodeDetailPesanan, String idPesanan, String idProduk, String namaProduk, double hargaJual, int subtotalPesanan, double subtotalHargaPesanan) {
        boolean isProductExist = false;
        int existingRow = -1;

      
        for (int i = 0; i < keranjangModel.getRowCount(); i++) {
            String existingIdProduk = (String) keranjangModel.getValueAt(i, 2); 

            if (existingIdProduk.equals(idProduk)) {
                isProductExist = true;
                existingRow = i;
                break;
            }
        }

        if (isProductExist) {
            keranjangModel.setValueAt(kodeDetailPesanan, existingRow, 0);
            keranjangModel.setValueAt(idPesanan, existingRow, 1);
            keranjangModel.setValueAt(idProduk, existingRow, 2);
            keranjangModel.setValueAt(namaProduk, existingRow, 3);
            keranjangModel.setValueAt(hargaJual, existingRow, 4);
            keranjangModel.setValueAt(subtotalPesanan, existingRow, 5);
            keranjangModel.setValueAt(subtotalHargaPesanan, existingRow, 6);
        } else {
            keranjangModel.addRow(new Object[]{kodeDetailPesanan, idPesanan, idProduk, namaProduk, hargaJual, subtotalPesanan, subtotalHargaPesanan});
        }
        updateTotalPesanan();
        updateTotalHarga();
    }

    private void bersihform() {
        txt_kodedetail.setText("");
        txt_idproduk.setText("");
        txt_namaproduk.setText("");
        txt_hargajual.setText("");
        txt_subtotalpes.setText("");
        txt_subtotalharga.setText("");
    }

    private boolean isIdPesananExistsInDatabase(String idpesanan) {
        try (
                Connection connection = koneksi.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM pesanan WHERE idpesanan = ?");) {
            preparedStatement.setString(1, idpesanan);
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

    private void generateIdPesanan() {
        Date selectedDate = txt_tglpesanan.getDate();
        counterPesanan = 1;

        if (selectedDate != null) {
            String idpesanan;
            do {
                idpesanan = generateIdPesanan(selectedDate);
                counterPesanan++;
            } while (isIdPesananExistsInDatabase(idpesanan));

            txt_idpesanan.setText(idpesanan);
        } else {

        }
    }

    private String generateIdPesanan(Date tanggalPesanan) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String pesananCode = String.format("PESN-%s-%d", dateFormat.format(tanggalPesanan), counterPesanan);
        return pesananCode;
    }

    private boolean isIdBayarPesananExistsInDatabase(String idBayarPesanan) {
        try (
                Connection connection = koneksi.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM pembayaranpesanan WHERE idpembayaranpesanan = ?");) {
            preparedStatement.setString(1, idBayarPesanan);
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

    private void generateIdPembayaranPesanan() {
        Date selectedDate = txt_tglpembayaran.getDate();
        counterPembayaran = 1;

        if (selectedDate != null) {
            String idpembayaranpesanan;
            do {
                idpembayaranpesanan = generateIdPembayaranPesanan(selectedDate);
                counterPembayaran++;
            } while (isIdBayarPesananExistsInDatabase(idpembayaranpesanan));

            txt_idpembayaran.setText(idpembayaranpesanan);
        } else {

        }
    }

    private String generateIdPembayaranPesanan(Date tanggalpembayaranPesanan) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String bayarCode = String.format("BYRP-%s-%d", dateFormat.format(tanggalpembayaranPesanan), counterPembayaran);
        return bayarCode;
    }

    private void updateKodeDetail() {
        String idPesanan = txt_idpesanan.getText();
        String idProduk = txt_idproduk.getText();

      
        if (!idPesanan.isEmpty() && !idProduk.isEmpty()) {
            String kodeDetail = idPesanan + "//" + idProduk;
      txt_kodedetail.setText(kodeDetail);
        }
    }

    private void updateSubtotal() {
        String hargaJualText = txt_hargajual.getText();
        String subtotalPesText = txt_subtotalpes.getText();

        
        if (!hargaJualText.isEmpty() && !subtotalPesText.isEmpty()) {
            try {
                
                double hargaJual = Double.parseDouble(hargaJualText);
                double subtotalPes = Double.parseDouble(subtotalPesText);

              
                double subtotalHarga = hargaJual * subtotalPes;
                txt_subtotalharga.setText(String.valueOf(subtotalHarga));
            } catch (NumberFormatException ex) {
                
                txt_subtotalharga.setText("Invalid input");
            }
        } else {
      
            txt_subtotalharga.setText("");
        }
    }

   private void tambahPesanan() {
        Connection conn = null;
        try {
            conn = koneksi.getConnection();
            conn.setAutoCommit(false);

            if (txt_idpesanan.getText().isEmpty() || txt_idkaryawan.getText().isEmpty()
                    || txt_tglpesanan.getDate() == null || txt_totalpesanan.getText().isEmpty()
                    || txt_totalharga.getText().isEmpty() || txt_idpembayaran.getText().isEmpty()
                    || txt_tglpembayaran.getDate() == null || txt_totalbayar.getText().isEmpty()
                    || txt_sisa.getText().isEmpty() || txt_kembalian.getText().isEmpty()
                    || tbl_keranjang.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom sebelum menyimpan data.");
                return;
            }
            String insertPesananSQL = "INSERT INTO PESANAN (IDPESANAN, IDKARYAWAN, TGLPESANAN, TOTALPESANAN, TOTALHARGAPESANAN, STATUSPESANAN) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstPesanan = conn.prepareStatement(insertPesananSQL)) {
                pstPesanan.setString(1, txt_idpesanan.getText());
                pstPesanan.setString(2, txt_idkaryawan.getText());
                pstPesanan.setDate(3, new java.sql.Date(txt_tglpesanan.getDate().getTime()));
                pstPesanan.setInt(4, Integer.parseInt(txt_totalpesanan.getText()));
                pstPesanan.setDouble(5, Double.parseDouble(txt_totalharga.getText()));
                pstPesanan.setString(6, rd_belum.isSelected() ? "Belum Lunas" : "Lunas");

                pstPesanan.executeUpdate();
            }
            String insertDetailPesananSQL = "INSERT INTO DETAILPESANAN (KODEDETAILPESAN, IDPESANAN, IDPRODUK, HARGAJUAL, SUBTOTALPESANAN, SUBTOTALHARGAPESANAN) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstDetailPesanan = conn.prepareStatement(insertDetailPesananSQL)) {
                for (int i = 0; i < tbl_keranjang.getRowCount(); i++) {
                    pstDetailPesanan.setString(1, tbl_keranjang.getValueAt(i, 0).toString());
                    pstDetailPesanan.setString(2, tbl_keranjang.getValueAt(i, 1).toString());
                    pstDetailPesanan.setString(3, tbl_keranjang.getValueAt(i, 2).toString());
                    pstDetailPesanan.setDouble(4, Double.parseDouble(tbl_keranjang.getValueAt(i, 4).toString()));
                    pstDetailPesanan.setInt(5, Integer.parseInt(tbl_keranjang.getValueAt(i, 5).toString()));
                    pstDetailPesanan.setDouble(6, Double.parseDouble(tbl_keranjang.getValueAt(i, 6).toString()));

                    pstDetailPesanan.executeUpdate();
                }
            }

            if (rd_lunas.isSelected()) {
                String updateStockSQL = "UPDATE PRODUK SET STOCKPRODUK = STOCKPRODUK - ? WHERE IDPRODUK = ?";
                try (PreparedStatement pstUpdateStock = conn.prepareStatement(updateStockSQL)) {
                    for (int i = 0; i < tbl_keranjang.getRowCount(); i++) {
                        int subtotalPesan = Integer.parseInt(tbl_keranjang.getValueAt(i, 5).toString());
                        String idProduk = tbl_keranjang.getValueAt(i, 2).toString();

                        
                        pstUpdateStock.setInt(1, subtotalPesan);
                        pstUpdateStock.setString(2, idProduk);
                        pstUpdateStock.executeUpdate();
                    }
                }
            }

            String insertPembayaranPesananSQL = "INSERT INTO PEMBAYARANPESANAN (IDPEMBAYARANPESANAN, IDPESANAN, TGLPEMBAYARANPESANAN, TOTALPEMBAYARANPESANAN, SISAPEMBAYARANPESANAN, KEMBALIANPEMBAYARANPESANAN) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstPembayaranPesanan = conn.prepareStatement(insertPembayaranPesananSQL)) {
                pstPembayaranPesanan.setString(1, txt_idpembayaran.getText());
                pstPembayaranPesanan.setString(2, txt_idpesanan.getText());
                pstPembayaranPesanan.setDate(3, new java.sql.Date(txt_tglpembayaran.getDate().getTime()));
                pstPembayaranPesanan.setDouble(4, Double.parseDouble(txt_totalbayar.getText()));
                pstPembayaranPesanan.setDouble(5, Double.parseDouble(txt_sisa.getText()));
                pstPembayaranPesanan.setDouble(6, Double.parseDouble(txt_kembalian.getText()));

                pstPembayaranPesanan.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
        } catch (SQLException e) {
            try {       
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }

        bersihForm();
    }

    private void bersihForm() {
        txt_idpesanan.setText("");
        txt_namakaryawan.setText("");
        txt_idkaryawan.setText("");
        txt_totalpesanan.setText("");
        txt_totalharga.setText("");
        txt_idproduk.setText("");
        txt_namaproduk.setText("");
        txt_hargajual.setText("");
        txt_subtotalpes.setText("");
        txt_subtotalharga.setText("");
        txt_idpembayaran.setText("");
        txt_totalbayar.setText("");
        txt_sisa.setText("");
        txt_kembalian.setText("");
        Date tglPesan = txt_tglpesanan.getDate();
        txt_tglpesanan.setDate(null);
        if (tglPesan != null) {
            txt_tglpesanan.setDate(tglPesan);
        }
        Date tglBayar = txt_tglpembayaran.getDate();

        txt_tglpembayaran.setDate(null);

        if (tglBayar != null) {
            txt_tglpembayaran.setDate(tglBayar);
        }
        StatusPembayaran.clearSelection();
        DefaultTableModel model = (DefaultTableModel) tbl_keranjang.getModel();
        model.setRowCount(0);
    }

    DefaultTableModel keranjangModel;
private void updateTotalBayar() {
    try {
        String totalHargaText = txt_totalharga.getText();

        if (!totalHargaText.isEmpty()) {
            double totalHarga = Double.parseDouble(totalHargaText);
            int percentage = spinDP.getValue();

            if (percentage == 0) {
                txt_totalbayar.setText(String.format("%.2f", totalHarga));
            } else {
                double totalBayar = totalHarga * (percentage / 100.0);
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                txt_totalbayar.setText(decimalFormat.format(totalBayar));
            }
        } else {
            txt_totalbayar.setText("");
        }
    } catch (NumberFormatException ex) {
       
        txt_totalbayar.setText("Invalid input");
    }
}


    public Form_TransPesan() {
        initComponents();
        spinDP.setMinimum(0);
spinDP.setMaximum(100);
spinDP.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        updateTotalBayar();
    }
});
spinDP.setInputVerifier(new InputVerifier() {
    public boolean verify(JComponent input) {
        JSpinField spinField = (JSpinField) input;
        int value = spinField.getValue();

        if (value < 0) {
            spinField.setValue(0); 
            return false;
        }
        else if (value > 100) {
            spinField.setValue(100);
            return false;
        }
        return true;
    }
});
        tbl_keranjang.setDefaultEditor(Object.class, null);
        rd_lunas.setEnabled(false);
        rd_belum.setEnabled(false);
        txt_sisa.setEditable(false);
        txt_namakaryawan.setEditable(false);
        txt_kembalian.setEditable(false);
        txt_totalpesanan.setEditable(false);
        txt_totalharga.setEditable(false);
        txt_idpembayaran.setEditable(false);
        txt_idkaryawan.setEditable(false);
        txt_namaproduk.setEditable(false);
        txt_kodedetail.setEditable(false);
        txt_idproduk.setEditable(false);
        txt_idpesanan.setEditable(false);
        txt_subtotalharga.setEditable(false);
        txt_hargajual.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSubtotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSubtotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        txt_subtotalpes.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSubtotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSubtotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        txt_idproduk.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateKodeDetail();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateKodeDetail();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        txt_tglpesanan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    generateIdPesanan();
                    updateKodeDetail();
                }
            }
        });

        txt_tglpembayaran.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    generateIdPembayaranPesanan();
                    updateKodeDetail(); 
                }
            }
        });
        txt_tglpesanan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                
                if ("date".equals(evt.getPropertyName())) {
                    
                    generateIdPesanan();
                }
            }
        });
        txt_tglpembayaran.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    generateIdPembayaranPesanan();
                }
            }
        });
        keranjangModel = new DefaultTableModel();
        keranjangModel.setColumnIdentifiers(new Object[]{"Kode Detail Pesanan", "ID Pesanan", "ID Produk", "Nama Produk", "Harga Jual", "Subtotal Pesanan", "Subtotal Harga Pesanan"});
        tbl_keranjang.setModel(keranjangModel);

     
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        StatusPembayaran = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        tambahTransJual = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txt_namaproduk = new javax.swing.JTextField();
        otherpes = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txt_idproduk = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_hargajual = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_subtotalpes = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_subtotalharga = new javax.swing.JTextField();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txt_kodedetail = new javax.swing.JTextField();
        btn_add = new keeptoo.KButton();
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
        spinDP = new com.toedter.components.JSpinField();
        jLabel27 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        kGradientPanel4 = new keeptoo.KGradientPanel();
        jLabel23 = new javax.swing.JLabel();
        txt_idkaryawan = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txt_namakaryawan = new javax.swing.JTextField();
        otherkar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        kGradientPanel5 = new keeptoo.KGradientPanel();
        jLabel26 = new javax.swing.JLabel();
        txt_idpesanan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_tglpesanan = new com.toedter.calendar.JDateChooser();
        jLabel20 = new javax.swing.JLabel();
        txt_totalpesanan = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txt_totalharga = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_keranjang = new javax.swing.JTable();
        btn_hapus = new keeptoo.KButton();
        btn_pesan = new keeptoo.KButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 500));
        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        tambahTransJual.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Tambahkan Pesanan");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        otherpes.setText("...");
        otherpes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherpesActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel7.setText("Nama Produk");

        jLabel8.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel8.setText("ID Produk");

        txt_hargajual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_hargajualKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel9.setText("Harga Jual");

        jLabel10.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel10.setText("Sub Total Pesanan");

        txt_subtotalpes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_subtotalpesKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel11.setText("=");

        jLabel12.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel12.setText("Sub Total Harga Pesanan");

        txt_subtotalharga.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_subtotalhargaMouseClicked(evt);
            }
        });
        txt_subtotalharga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_subtotalhargaActionPerformed(evt);
            }
        });

        kGradientPanel2.setkBorderRadius(0);
        kGradientPanel2.setkEndColor(new java.awt.Color(0, 51, 255));
        kGradientPanel2.setkStartColor(new java.awt.Color(102, 102, 255));

        jLabel14.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Detail Pesanan");

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(355, 355, 355))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel14)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel13.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel13.setText("Kode Detail Pesanan");

        btn_add.setText("Add");
        btn_add.setkBackGroundColor(new java.awt.Color(51, 0, 204));
        btn_add.setkEndColor(new java.awt.Color(102, 51, 255));
        btn_add.setkHoverEndColor(new java.awt.Color(153, 102, 255));
        btn_add.setkHoverForeGround(new java.awt.Color(153, 153, 255));
        btn_add.setkHoverStartColor(new java.awt.Color(102, 102, 255));
        btn_add.setkSelectedColor(new java.awt.Color(255, 153, 255));
        btn_add.setkStartColor(new java.awt.Color(0, 204, 204));
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_kodedetail))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_idproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_namaproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(otherpes, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_hargajual, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(23, 23, 23)
                                .addComponent(jLabel12)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_subtotalpes, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_subtotalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_add, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(kGradientPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(7, 7, 7)
                        .addComponent(txt_kodedetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_idproduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_namaproduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(otherpes)
                            .addComponent(txt_hargajual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_subtotalpes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(txt_subtotalharga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_add, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        kGradientPanel3.setkBorderRadius(0);
        kGradientPanel3.setkStartColor(new java.awt.Color(153, 153, 255));

        jLabel6.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Pembayaran");

        javax.swing.GroupLayout kGradientPanel3Layout = new javax.swing.GroupLayout(kGradientPanel3);
        kGradientPanel3.setLayout(kGradientPanel3Layout);
        kGradientPanel3Layout.setHorizontalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGap(428, 428, 428)
                .addComponent(jLabel6)
                .addContainerGap(361, Short.MAX_VALUE))
        );
        kGradientPanel3Layout.setVerticalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel6)
                .addContainerGap(25, Short.MAX_VALUE))
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

        jLabel18.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel18.setText("Sisa yang harus dibayar");

        jLabel19.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel19.setText("Kembalian");

        jLabel22.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel22.setText("Status Pembayaran");

        StatusPembayaran.add(rd_lunas);
        rd_lunas.setText("Lunas");
        rd_lunas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_lunasActionPerformed(evt);
            }
        });

        StatusPembayaran.add(rd_belum);
        rd_belum.setText("Belum Lunas");

        spinDP.setBackground(new java.awt.Color(255, 255, 255));
        spinDP.setFocusable(false);
        spinDP.setRequestFocusEnabled(false);

        jLabel27.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel27.setText("DP %");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 873, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel16)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)
                        .addGap(21, 21, 21)
                        .addComponent(jLabel19))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txt_idpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(txt_tglpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_totalbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_sisa, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(txt_kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(rd_lunas)
                                .addGap(6, 6, 6)
                                .addComponent(rd_belum)))
                        .addGap(205, 205, 205)
                        .addComponent(jLabel27)
                        .addGap(8, 8, 8)
                        .addComponent(spinDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txt_idpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_tglpembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_totalbayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_sisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(spinDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rd_lunas)
                            .addComponent(rd_belum))))
                .addGap(12, 12, 12))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        kGradientPanel4.setkBorderRadius(0);
        kGradientPanel4.setkStartColor(new java.awt.Color(204, 204, 255));

        jLabel23.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Karyawan");

        javax.swing.GroupLayout kGradientPanel4Layout = new javax.swing.GroupLayout(kGradientPanel4);
        kGradientPanel4.setLayout(kGradientPanel4Layout);
        kGradientPanel4Layout.setHorizontalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(jLabel23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel4Layout.setVerticalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel23)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jLabel24.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel24.setText("Id Karyawan");

        jLabel25.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel25.setText("Nama Karyawan");

        otherkar.setText("...");
        otherkar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherkarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_idkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txt_namakaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(otherkar))
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(kGradientPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(kGradientPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_idkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_namakaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(otherkar)))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        kGradientPanel5.setkBorderRadius(0);
        kGradientPanel5.setkStartColor(new java.awt.Color(204, 204, 255));

        jLabel26.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Pesanan");

        javax.swing.GroupLayout kGradientPanel5Layout = new javax.swing.GroupLayout(kGradientPanel5);
        kGradientPanel5.setLayout(kGradientPanel5Layout);
        kGradientPanel5Layout.setHorizontalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addGap(147, 147, 147))
        );
        kGradientPanel5Layout.setVerticalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel26)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        txt_idpesanan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_idpesanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idpesananActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel5.setText("ID Pesanan");

        jLabel4.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel4.setText("Tanggal Pesanan");

        jLabel20.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel20.setText("Total Pesanan");

        txt_totalpesanan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel21.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel21.setText("Total Harga Pesanan");

        txt_totalharga.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_idpesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txt_tglpesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txt_totalpesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(kGradientPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_idpesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tglpesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_totalpesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20))
                        .addGap(30, 30, 30)))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Keranjang Biru"));

        tbl_keranjang.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_keranjang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_keranjangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_keranjang);

        btn_hapus.setText("Hapus");
        btn_hapus.setkBackGroundColor(new java.awt.Color(51, 0, 204));
        btn_hapus.setkEndColor(new java.awt.Color(102, 51, 255));
        btn_hapus.setkHoverEndColor(new java.awt.Color(153, 102, 255));
        btn_hapus.setkHoverForeGround(new java.awt.Color(153, 153, 255));
        btn_hapus.setkHoverStartColor(new java.awt.Color(102, 102, 255));
        btn_hapus.setkSelectedColor(new java.awt.Color(255, 153, 255));
        btn_hapus.setkStartColor(new java.awt.Color(255, 51, 51));
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btn_pesan.setText("Tambah Transaksi");
        btn_pesan.setkBackGroundColor(new java.awt.Color(51, 0, 204));
        btn_pesan.setkEndColor(new java.awt.Color(102, 51, 255));
        btn_pesan.setkHoverEndColor(new java.awt.Color(153, 102, 255));
        btn_pesan.setkHoverForeGround(new java.awt.Color(153, 153, 255));
        btn_pesan.setkHoverStartColor(new java.awt.Color(102, 102, 255));
        btn_pesan.setkSelectedColor(new java.awt.Color(255, 153, 255));
        btn_pesan.setkStartColor(new java.awt.Color(0, 204, 204));
        btn_pesan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pesanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tambahTransJualLayout = new javax.swing.GroupLayout(tambahTransJual);
        tambahTransJual.setLayout(tambahTransJualLayout);
        tambahTransJualLayout.setHorizontalGroup(
            tambahTransJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahTransJualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahTransJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pesan, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addGroup(tambahTransJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tambahTransJualLayout.createSequentialGroup()
                            .addGroup(tambahTransJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(tambahTransJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(0, 14, Short.MAX_VALUE))
        );
        tambahTransJualLayout.setVerticalGroup(
            tambahTransJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahTransJualLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahTransJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahTransJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_pesan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.add(tambahTransJual, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void otherpesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherpesActionPerformed
       
         Dialog_DataBarang dialogDataBarang = new Dialog_DataBarang(new javax.swing.JFrame(), true);
    dialogDataBarang.setVisible(true);

    if (dialogDataBarang.isBarangSelected()) {
        String selectedIdBarang = dialogDataBarang.getSelectedIdBarang();
        String selectedNamaBarang = dialogDataBarang.getSelectedNamaBarang();

       
        if (checkStok(selectedIdBarang)) {
            txt_idproduk.setText(selectedIdBarang);
            txt_namaproduk.setText(selectedNamaBarang);
        } else {
            JOptionPane.showMessageDialog(this, "Stok produk habis.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
    }//GEN-LAST:event_otherpesActionPerformed

    private void otherkarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherkarActionPerformed
       
        Dialog_DataAdmin dialogDataAdmin = new Dialog_DataAdmin(new javax.swing.JFrame(), true);
        dialogDataAdmin.setVisible(true);

        
        if (dialogDataAdmin.isKaryawanSelected()) {
          
            String selectedIdKaryawan = dialogDataAdmin.getSelectedIdKaryawan();
            String selectedNamaKaryawan = dialogDataAdmin.getSelectedNamaKaryawan();

            
            txt_idkaryawan.setText(selectedIdKaryawan);
            txt_namakaryawan.setText(selectedNamaKaryawan);
        }

    }//GEN-LAST:event_otherkarActionPerformed

    private void txt_subtotalhargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_subtotalhargaActionPerformed
       
    }//GEN-LAST:event_txt_subtotalhargaActionPerformed

    private void txt_subtotalhargaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_subtotalhargaMouseClicked

    }//GEN-LAST:event_txt_subtotalhargaMouseClicked

    private void txt_hargajualKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_hargajualKeyTyped
       
        char c = evt.getKeyChar();

       
        if (!Character.isDigit(c)) {
            evt.consume(); 
        }
    }//GEN-LAST:event_txt_hargajualKeyTyped

    private void txt_subtotalpesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_subtotalpesKeyTyped
    
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume(); 
        }
    }//GEN-LAST:event_txt_subtotalpesKeyTyped

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        if (btn_add.getText().equals("Ubah")) {
           
            ubahKeranjang();
        } else if (btn_add.getText().equals("Add")) {
 
            tambahKeranjang();
        }
    }//GEN-LAST:event_btn_addActionPerformed

    private void rd_lunasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd_lunasActionPerformed
      
    }//GEN-LAST:event_rd_lunasActionPerformed

    private void btn_pesanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pesanActionPerformed
        tambahPesanan();
    }//GEN-LAST:event_btn_pesanActionPerformed

    private void txt_sisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_sisaActionPerformed
      
    }//GEN-LAST:event_txt_sisaActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
       
        hapusBarisKeranjang();
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void tbl_keranjangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_keranjangMouseClicked
       
        btn_add.setText("Ubah");
        int baris = tbl_keranjang.getSelectedRow();

        String idproduk = tbl_keranjang.getValueAt(baris, 2).toString();
        txt_idproduk.setText(idproduk);
        String namaProduk = tbl_keranjang.getValueAt(baris, 3).toString();
        txt_namaproduk.setText(namaProduk);

        String hargajual = tbl_keranjang.getValueAt(baris, 4).toString();
        txt_hargajual.setText(hargajual);

        String subpesan = tbl_keranjang.getValueAt(baris, 5).toString();
        txt_subtotalpes.setText(subpesan);

        String subharga = tbl_keranjang.getValueAt(baris, 6).toString();
        txt_subtotalharga.setText(subharga);
    }//GEN-LAST:event_tbl_keranjangMouseClicked

    private void txt_idpesananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idpesananActionPerformed
       
    }//GEN-LAST:event_txt_idpesananActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup StatusPembayaran;
    private keeptoo.KButton btn_add;
    private keeptoo.KButton btn_hapus;
    private keeptoo.KButton btn_pesan;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private keeptoo.KGradientPanel kGradientPanel2;
    private keeptoo.KGradientPanel kGradientPanel3;
    private keeptoo.KGradientPanel kGradientPanel4;
    private keeptoo.KGradientPanel kGradientPanel5;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton otherkar;
    private javax.swing.JButton otherpes;
    private javax.swing.JRadioButton rd_belum;
    private javax.swing.JRadioButton rd_lunas;
    private com.toedter.components.JSpinField spinDP;
    private javax.swing.JPanel tambahTransJual;
    private javax.swing.JTable tbl_keranjang;
    private javax.swing.JTextField txt_hargajual;
    private javax.swing.JTextField txt_idkaryawan;
    private javax.swing.JTextField txt_idpembayaran;
    private javax.swing.JTextField txt_idpesanan;
    private javax.swing.JTextField txt_idproduk;
    private javax.swing.JTextField txt_kembalian;
    private javax.swing.JTextField txt_kodedetail;
    private javax.swing.JTextField txt_namakaryawan;
    private javax.swing.JTextField txt_namaproduk;
    private javax.swing.JTextField txt_sisa;
    private javax.swing.JTextField txt_subtotalharga;
    private javax.swing.JTextField txt_subtotalpes;
    private com.toedter.calendar.JDateChooser txt_tglpembayaran;
    private com.toedter.calendar.JDateChooser txt_tglpesanan;
    private javax.swing.JTextField txt_totalbayar;
    private javax.swing.JTextField txt_totalharga;
    private javax.swing.JTextField txt_totalpesanan;
    // End of variables declaration//GEN-END:variables
private boolean checkStok(String idProduk) {
    try {
        String sql = "SELECT STOCKPRODUK FROM PRODUK WHERE IDPRODUK = ?";
        Connection conn = koneksi.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, idProduk);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            int stok = rs.getInt("STOCKPRODUK");
            return stok > 0;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
}

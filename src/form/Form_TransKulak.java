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

/**
 *
 * @author Rifal
 */
public class Form_TransKulak extends javax.swing.JPanel {

    private int counterPesanan = 1;
    private int counterPembayaran = 1;
    private int counterDetail = 1;

    private void updateTotalPembelian() {
        int totalPesanan = 0;
        int rowCount = keranjangModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            totalPesanan += (int) keranjangModel.getValueAt(i, 6); 
        }
        txt_totalpembelian.setText(String.valueOf(totalPesanan));
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
            totalHarga += (double) keranjangModel.getValueAt(i, 7); 
        }
        txt_totalharga.setText(String.valueOf(totalHarga));
        updateSisaKembalian();
    }

   

    private void tambahKeranjang() {
        if (txt_kodedetail.getText().isEmpty() || txt_idpembelian.getText().isEmpty() || txt_idbahan.getText().isEmpty()|| txt_idsupplier.getText().isEmpty()
                || txt_namabahan.getText().isEmpty() || txt_hargajual.getText().isEmpty() || txt_subtotalpes.getText().isEmpty()
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
        String idPembelian = txt_idpembelian.getText();
        String idSupplier = txt_idsupplier.getText();
        String idBahanBaku = txt_idbahan.getText(); 
        String namaBahanBaku = txt_namabahan.getText(); 
        double hargaJual = Double.parseDouble(txt_hargajual.getText());
        int subtotalPesanan = Integer.parseInt(txt_subtotalpes.getText());
        double subtotalHarga = Double.parseDouble(txt_subtotalharga.getText());

        if (isProductInKeranjang(idBahanBaku, idSupplier)) {
            JOptionPane.showMessageDialog(this, "Bahan Baku dengan ID yang sama sudah ada dalam keranjang.");
            bersihKeranjang();
            return;
            
        }

        updateKeranjangTable(kodeDetail, idPembelian, idSupplier, idBahanBaku, namaBahanBaku, hargaJual, subtotalPesanan, subtotalHarga);

        updateTotalPembelian();
        updateTotalHarga();

        bersihKeranjang();
    }
private boolean isProductInKeranjang(String idBahanBaku, String idSupplier) {
    for (int i = 0; i < tbl_keranjang.getRowCount(); i++) {
        String existingIdBahanBaku = tbl_keranjang.getValueAt(i, 3).toString(); 
        String existingIdSupplier = tbl_keranjang.getValueAt(i, 2).toString(); 

        if (existingIdBahanBaku.equals(idBahanBaku)) {
            return true;
        }
    }
    return false;
}
   private void updateKeranjangTable(String kodeDetailPesanan, String idPembelian, String idSupplier, String idBahanBaku, String namaBahanBaku, double hargaJual, int subtotalPesanan, double subtotalHargaPesanan) {
        boolean isProductExist = false;
        int existingRow = -1;

        for (int i = 0; i < keranjangModel.getRowCount(); i++) {
            String existingIdBahanBaku = (String) keranjangModel.getValueAt(i, 3); 

            if (existingIdBahanBaku.equals(idBahanBaku)) {
                isProductExist = true;
                existingRow = i;
                break;
            }
        }

        if (isProductExist) {
            keranjangModel.setValueAt(kodeDetailPesanan, existingRow, 0);
            keranjangModel.setValueAt(idPembelian, existingRow, 1);
            keranjangModel.setValueAt(idSupplier, existingRow, 2);
            keranjangModel.setValueAt(idBahanBaku, existingRow, 3);
            keranjangModel.setValueAt(namaBahanBaku, existingRow, 4);
            keranjangModel.setValueAt(hargaJual, existingRow, 5);
            keranjangModel.setValueAt(subtotalPesanan, existingRow, 6);
            keranjangModel.setValueAt(subtotalHargaPesanan, existingRow, 7);
        } else {
            keranjangModel.addRow(new Object[]{kodeDetailPesanan, idPembelian, idSupplier, idBahanBaku, namaBahanBaku, hargaJual, subtotalPesanan, subtotalHargaPesanan});
        }
        updateTotalPembelian();
        updateTotalHarga();
    }

    private void ubahKeranjang() {

        if (txt_kodedetail.getText().isEmpty() || txt_idpembelian.getText().isEmpty() || txt_idbahan.getText().isEmpty()|| txt_idsupplier.getText().isEmpty() 
                || txt_namabahan.getText().isEmpty() || txt_hargajual.getText().isEmpty() || txt_subtotalpes.getText().isEmpty()
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
        String idPembelian = txt_idpembelian.getText();
        String idSupplier = txt_idsupplier.getText();
        String idBahan = txt_idbahan.getText();
        String namaBahan = txt_namabahan.getText();
        double hargaJual = Double.parseDouble(txt_hargajual.getText());
        int subtotalPesanan = Integer.parseInt(txt_subtotalpes.getText());
        double subtotalHarga = Double.parseDouble(txt_subtotalharga.getText());

        if (!isProductInKeranjang(idBahan, idSupplier)) {
            JOptionPane.showMessageDialog(this, "Produk dengan ID Produk yang sama tidak ditemukan dalam keranjang.");
            return;
        }

        updateKeranjangTable(kodeDetail, idPembelian, idSupplier, idBahan, namaBahan, hargaJual, subtotalPesanan, subtotalHarga);

        updateTotalPembelian();
        updateTotalHarga();

        bersihKeranjang();
    }

    private void hapusBarisKeranjang() {

        int selectedRow = tbl_keranjang.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus.");
            return;
        }

        keranjangModel.removeRow(selectedRow);

        updateTotalPembelian();
        updateTotalHarga();
    }

 
    private void bersihKeranjang() {
        txt_kodedetail.setText("");
        txt_idbahan.setText("");
        txt_namabahan.setText("");
        txt_hargajual.setText("");
        txt_subtotalpes.setText("");
        txt_subtotalharga.setText("");
    }

    private boolean isIdPembelianExistsInDatabase(String idpembelian) {
        try (
                Connection connection = koneksi.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM pembelian WHERE idpembelian = ?");) {
            preparedStatement.setString(1, idpembelian);
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

    private void generateIdPembelian() {
        Date selectedDate = txt_tglpembelian.getDate();
        counterPesanan = 1;

        if (selectedDate != null) {
            String idpembelian;
            do {
                idpembelian = generateIdPembelian(selectedDate);
                counterPesanan++;
            } while (isIdPembelianExistsInDatabase(idpembelian));

            txt_idpembelian.setText(idpembelian);
        } else {

        }
    }

    private String generateIdPembelian(Date tanggalPesanan) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String pesananCode = String.format("BELI-%s-%d", dateFormat.format(tanggalPesanan), counterPesanan);
        return pesananCode;
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

        }
    }

    private String generateIdPembayaranPembelian(Date tanggalpembayaranPesanan) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String bayarCode = String.format("BYRB-%s-%d", dateFormat.format(tanggalpembayaranPesanan), counterPembayaran);
        return bayarCode;
    }

    private void updateKodeDetail() {
        String idPesanan = txt_idpembelian.getText();
        String idSup = txt_idsupplier.getText();
        String idProduk = txt_idbahan.getText();

        if (!idPesanan.isEmpty() && !idProduk.isEmpty()) {
            String kodeDetail = idPesanan + "//" + idSup + "//" + idProduk;
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

            if (txt_idpembelian.getText().isEmpty() || txt_idkaryawan.getText().isEmpty() || txt_idsupplier.getText().isEmpty()
                    || txt_tglpembelian.getDate() == null || txt_totalpembelian.getText().isEmpty()
                    || txt_totalharga.getText().isEmpty() || txt_idpembayaran.getText().isEmpty()
                    || txt_tglpembayaran.getDate() == null || txt_totalbayar.getText().isEmpty()
                    || txt_sisa.getText().isEmpty() || txt_kembalian.getText().isEmpty()
                    || tbl_keranjang.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom sebelum menyimpan data.");
                return;
            }
            String insertPembelianSQL = "INSERT INTO PEMBELIAN (IDPEMBELIAN, IDSUPPLIER, IDKARYAWAN, TGLPEMBELIAN, TOTALPEMBELIAN, TOTALHARGAPEMBELIAN, STATUSPEMBELIAN) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstPembelian = conn.prepareStatement(insertPembelianSQL)) {
                pstPembelian.setString(1, txt_idpembelian.getText());
                pstPembelian.setString(2, txt_idsupplier.getText());
                pstPembelian.setString(3, txt_idkaryawan.getText());
                pstPembelian.setDate(4, new java.sql.Date(txt_tglpembelian.getDate().getTime()));
                pstPembelian.setInt(5, Integer.parseInt(txt_totalpembelian.getText()));
                pstPembelian.setDouble(6, Double.parseDouble(txt_totalharga.getText()));
                pstPembelian.setString(7, rd_belum.isSelected() ? "Belum Lunas" : "Lunas");

                pstPembelian.executeUpdate();
            }
            String insertDetailPembelianSQL = "INSERT INTO DETAILPEMBELIAN (KODEDETAILBELI, IDPEMBELIAN, IDBAHAN, HARGABELI, SUBTOTALPEMBELIAN, SUBTOTALHARGAPEMBELIAN) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstDetailPembelian = conn.prepareStatement(insertDetailPembelianSQL)) {
                for (int i = 0; i < tbl_keranjang.getRowCount(); i++) {
                    pstDetailPembelian.setString(1, tbl_keranjang.getValueAt(i, 0).toString());
                    pstDetailPembelian.setString(2, tbl_keranjang.getValueAt(i, 1).toString());
                    pstDetailPembelian.setString(3, tbl_keranjang.getValueAt(i, 3).toString());
                    pstDetailPembelian.setDouble(4, Double.parseDouble(tbl_keranjang.getValueAt(i, 5).toString()));
                    pstDetailPembelian.setInt(5, Integer.parseInt(tbl_keranjang.getValueAt(i, 6).toString()));
                    pstDetailPembelian.setDouble(6, Double.parseDouble(tbl_keranjang.getValueAt(i, 7).toString()));

                    pstDetailPembelian.executeUpdate();
                }
            }

            if (rd_lunas.isSelected()) {
                String updateStockSQL = "UPDATE BAHANBAKU SET STOCKBAHAN = STOCKBAHAN + ? WHERE IDBAHAN = ?";
                try (PreparedStatement pstUpdateStock = conn.prepareStatement(updateStockSQL)) {
                    for (int i = 0; i < tbl_keranjang.getRowCount(); i++) {
                        int jumlahBahan = Integer.parseInt(tbl_keranjang.getValueAt(i, 6).toString());
                        String idBahan = tbl_keranjang.getValueAt(i, 3).toString();  

                        pstUpdateStock.setInt(1, jumlahBahan);
                        pstUpdateStock.setString(2, idBahan);
                        pstUpdateStock.executeUpdate();
                    }
                }
            }

            String insertPembayaranPembelianSQL = "INSERT INTO PEMBAYARANPEMBELIAN (IDPEMBAYARANPEMBELIAN, IDPEMBELIAN, TGLPEMBAYARANPEMBELIAN, TOTALPEMBAYARANPEMBELIAN, SISAPEMBAYARANPEMBELIAN, KEMBALIANPEMBAYARANPEMBELIAN) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstPembayaranPembelian = conn.prepareStatement(insertPembayaranPembelianSQL)) {
                pstPembayaranPembelian.setString(1, txt_idpembayaran.getText());
                pstPembayaranPembelian.setString(2, txt_idpembelian.getText());
                pstPembayaranPembelian.setDate(3, new java.sql.Date(txt_tglpembayaran.getDate().getTime()));
                pstPembayaranPembelian.setDouble(4, Double.parseDouble(txt_totalbayar.getText()));
                pstPembayaranPembelian.setDouble(5, Double.parseDouble(txt_sisa.getText()));
                pstPembayaranPembelian.setDouble(6, Double.parseDouble(txt_kembalian.getText()));

                pstPembayaranPembelian.executeUpdate();
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
        
        txt_idpembelian.setText("");
        txt_namakaryawan.setText("");
        txt_idkaryawan.setText("");
        txt_idsupplier.setText("");
        txt_namatoko.setText("");
        txt_totalpembelian.setText("");
        txt_totalharga.setText("");
        txt_idbahan.setText("");
        txt_namabahan.setText("");
        txt_hargajual.setText("");
        txt_subtotalpes.setText("");
        txt_subtotalharga.setText("");
        txt_idpembayaran.setText("");
        txt_totalbayar.setText("");
        txt_sisa.setText("");
        txt_kembalian.setText("");
        Date tglBeli = txt_tglpembelian.getDate();
        txt_tglpembelian.setDate(null);
        if (tglBeli != null) {
            txt_tglpembelian.setDate(tglBeli);
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
    public Form_TransKulak() {
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
        txt_namakaryawan.setEditable(false);
        txt_idsupplier.setEditable(false);
        txt_namatoko.setEditable(false);
        txt_sisa.setEditable(false);
        txt_kembalian.setEditable(false);
        txt_totalpembelian.setEditable(false);
        txt_totalharga.setEditable(false);
        txt_idpembayaran.setEditable(false);
        txt_idkaryawan.setEditable(false);
        txt_namabahan.setEditable(false);
        txt_kodedetail.setEditable(false);
        txt_idbahan.setEditable(false);
        txt_idpembelian.setEditable(false);
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
        txt_idbahan.getDocument().addDocumentListener(new DocumentListener() {
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

        txt_tglpembelian.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    generateIdPembelian();
                    updateKodeDetail();
                }
            }
        });

        txt_tglpembayaran.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    generateIdPembayaranPembelian();
                    updateKodeDetail(); 
                }
            }
        });
        txt_tglpembelian.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
               
                if ("date".equals(evt.getPropertyName())) {
                   
                    generateIdPembelian();
                }
            }
        });
        txt_tglpembayaran.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    generateIdPembayaranPembelian();
                }
            }
        });
        keranjangModel = new DefaultTableModel();
        keranjangModel.setColumnIdentifiers(new Object[]{"Kode Detail Pembelian", "ID Pembelian", "ID Supplier", "ID Bahan", "Nama Bahan", "Harga Beli", "Subtotal Pembelian", "Subtotal Harga Pembelian"});
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
        txt_namabahan = new javax.swing.JTextField();
        otherpes = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txt_idbahan = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_hargajual = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_subtotalpes = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_subtotalharga = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txt_kodedetail = new javax.swing.JTextField();
        btn_add = new keeptoo.KButton();
        kGradientPanel8 = new keeptoo.KGradientPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
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
        kGradientPanel9 = new keeptoo.KGradientPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        spinDP = new com.toedter.components.JSpinField();
        jPanel3 = new javax.swing.JPanel();
        kGradientPanel4 = new keeptoo.KGradientPanel();
        jLabel23 = new javax.swing.JLabel();
        kGradientPanel6 = new keeptoo.KGradientPanel();
        jLabel27 = new javax.swing.JLabel();
        txt_idkaryawan = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txt_namakaryawan = new javax.swing.JTextField();
        otherkar = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txt_namatoko = new javax.swing.JTextField();
        txt_idsupplier = new javax.swing.JTextField();
        otherkar1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        txt_idpembelian = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_tglpembelian = new com.toedter.calendar.JDateChooser();
        jLabel20 = new javax.swing.JLabel();
        txt_totalpembelian = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txt_totalharga = new javax.swing.JTextField();
        kGradientPanel7 = new keeptoo.KGradientPanel();
        jLabel28 = new javax.swing.JLabel();
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
        jLabel2.setText("Tambahkan Pembelian");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        otherpes.setText("...");
        otherpes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherpesActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel7.setText("Nama Bahan");

        jLabel8.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel8.setText("ID Bahan");

        txt_hargajual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_hargajualKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel9.setText("Harga Beli");

        jLabel10.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel10.setText("Sub Total Pembelian");

        txt_subtotalpes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_subtotalpesKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel11.setText("=");

        jLabel12.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel12.setText("Sub Total Harga Pembelian");

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

        jLabel13.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel13.setText("Kode Detail Pembelian");

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

        kGradientPanel8.setkBorderRadius(0);
        kGradientPanel8.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel8.setkStartColor(new java.awt.Color(153, 255, 153));

        jLabel29.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Detail Pembelian");

        javax.swing.GroupLayout kGradientPanel8Layout = new javax.swing.GroupLayout(kGradientPanel8);
        kGradientPanel8.setLayout(kGradientPanel8Layout);
        kGradientPanel8Layout.setHorizontalGroup(
            kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addGap(352, 352, 352))
        );
        kGradientPanel8Layout.setVerticalGroup(
            kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel29)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_kodedetail))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_idbahan, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_namabahan, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(otherpes, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_hargajual, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_subtotalpes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_subtotalharga)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_add, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(kGradientPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(kGradientPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(7, 7, 7)
                        .addComponent(txt_kodedetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_idbahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_namabahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(txt_idpembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 103, 169, -1));

        jLabel15.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel15.setText("ID Pembayaran");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 77, 169, -1));

        jLabel16.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel16.setText("Tanggal pembayaran");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(182, 77, -1, -1));
        jPanel2.add(txt_tglpembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(182, 102, 156, -1));

        jLabel17.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel17.setText("Total Pembayaran");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(356, 77, -1, -1));

        txt_sisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_sisaActionPerformed(evt);
            }
        });
        jPanel2.add(txt_sisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(496, 102, 173, -1));
        jPanel2.add(txt_totalbayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(356, 102, 122, -1));

        jLabel18.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel18.setText("Sisa yang harus dibayar");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(496, 77, -1, -1));

        jLabel19.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel19.setText("Kembalian");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(675, 77, -1, -1));
        jPanel2.add(txt_kembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(675, 102, 110, -1));

        jLabel22.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel22.setText("Status Pembayaran");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        StatusPembayaran.add(rd_lunas);
        rd_lunas.setText("Lunas");
        rd_lunas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_lunasActionPerformed(evt);
            }
        });
        jPanel2.add(rd_lunas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        StatusPembayaran.add(rd_belum);
        rd_belum.setText("Belum Lunas");
        jPanel2.add(rd_belum, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, -1, -1));

        kGradientPanel9.setkBorderRadius(0);
        kGradientPanel9.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel9.setkStartColor(new java.awt.Color(153, 255, 153));

        jLabel30.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Pembayaran");

        javax.swing.GroupLayout kGradientPanel9Layout = new javax.swing.GroupLayout(kGradientPanel9);
        kGradientPanel9.setLayout(kGradientPanel9Layout);
        kGradientPanel9Layout.setHorizontalGroup(
            kGradientPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel30)
                .addGap(352, 352, 352))
        );
        kGradientPanel9Layout.setVerticalGroup(
            kGradientPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel9Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel30)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel2.add(kGradientPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 873, 51));

        jLabel33.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel33.setText("DP %");
        jPanel2.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, -1, -1));

        spinDP.setBackground(new java.awt.Color(255, 255, 255));
        spinDP.setFocusable(false);
        spinDP.setRequestFocusEnabled(false);
        spinDP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                spinDPKeyTyped(evt);
            }
        });
        jPanel2.add(spinDP, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 130, -1, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        kGradientPanel4.setkBorderRadius(0);
        kGradientPanel4.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel4.setkStartColor(new java.awt.Color(153, 255, 153));

        jLabel23.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Karyawan");

        kGradientPanel6.setkBorderRadius(0);
        kGradientPanel6.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel6.setkStartColor(new java.awt.Color(153, 255, 153));

        jLabel27.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Operator");

        javax.swing.GroupLayout kGradientPanel6Layout = new javax.swing.GroupLayout(kGradientPanel6);
        kGradientPanel6.setLayout(kGradientPanel6Layout);
        kGradientPanel6Layout.setHorizontalGroup(
            kGradientPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel6Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(jLabel27)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel6Layout.setVerticalGroup(
            kGradientPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel27)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout kGradientPanel4Layout = new javax.swing.GroupLayout(kGradientPanel4);
        kGradientPanel4.setLayout(kGradientPanel4Layout);
        kGradientPanel4Layout.setHorizontalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(jLabel23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(kGradientPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
        );
        kGradientPanel4Layout.setVerticalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addComponent(kGradientPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel24.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel24.setText("ID Karyawan");

        jLabel25.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel25.setText("Nama Karyawan");

        otherkar.setText("...");
        otherkar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherkarActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel31.setText("ID Supplier");

        jLabel32.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel32.setText("Nama Toko");

        otherkar1.setText("...");
        otherkar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherkar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_idkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txt_namakaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(otherkar))
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_idsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txt_namatoko, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(otherkar1))
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(12, Short.MAX_VALUE))
            .addComponent(kGradientPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(kGradientPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_idkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_namakaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(otherkar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_idsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_namatoko, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(otherkar1)))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txt_idpembelian.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel5.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel5.setText("ID Pembelian");

        jLabel4.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel4.setText("Tanggal Pembelian");

        jLabel20.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel20.setText("Total Pembelian");

        txt_totalpembelian.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel21.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel21.setText("Total Harga Pembelian");

        txt_totalharga.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        kGradientPanel7.setkBorderRadius(0);
        kGradientPanel7.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel7.setkStartColor(new java.awt.Color(153, 255, 153));

        jLabel28.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Pembelian");

        javax.swing.GroupLayout kGradientPanel7Layout = new javax.swing.GroupLayout(kGradientPanel7);
        kGradientPanel7.setLayout(kGradientPanel7Layout);
        kGradientPanel7Layout.setHorizontalGroup(
            kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel7Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(jLabel28)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel7Layout.setVerticalGroup(
            kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel28)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_idpembelian, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txt_tglpembelian, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txt_totalpembelian, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(kGradientPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(kGradientPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_idpembelian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tglpembelian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_totalpembelian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20))
                        .addGap(30, 30, 30)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Keranjang Hijau"));

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1245, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 875, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(0, 8, Short.MAX_VALUE))
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
        
        Dialog_DataBahan dialogDataBahan = new Dialog_DataBahan(new javax.swing.JFrame(), true);
        dialogDataBahan.setVisible(true);

      
        if (dialogDataBahan.isBahanSelected()) {
            
            String selectedIdBahan = dialogDataBahan.getSelectedIdBahan();
            String selectedNamaBahan = dialogDataBahan.getSelectedNamaBahan();

          
            txt_idbahan.setText(selectedIdBahan);
            txt_namabahan.setText(selectedNamaBahan);
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

        String idsup = tbl_keranjang.getValueAt(baris, 2).toString();
        txt_idsupplier.setText(idsup);
        
        String idbahan = tbl_keranjang.getValueAt(baris, 3).toString();
        txt_idbahan.setText(idbahan);
        
        String namaBahan = tbl_keranjang.getValueAt(baris, 4).toString();
        txt_namabahan.setText(namaBahan);

        String hargajual = tbl_keranjang.getValueAt(baris, 5).toString();
        txt_hargajual.setText(hargajual);

        String subpesan = tbl_keranjang.getValueAt(baris, 6).toString();
        txt_subtotalpes.setText(subpesan);

        String subharga = tbl_keranjang.getValueAt(baris, 7).toString();
        txt_subtotalharga.setText(subharga);
    }//GEN-LAST:event_tbl_keranjangMouseClicked

    private void otherkar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherkar1ActionPerformed
        
        Dialog_DataSupplier dialogDataSupplier = new Dialog_DataSupplier(new javax.swing.JFrame(), true);
        dialogDataSupplier.setVisible(true);

      
        if (dialogDataSupplier.isSupplierSelected()) {
          
            String selectedIdSupplier = dialogDataSupplier.getSelectedIdSupplier();
            String selectedNamaToko = dialogDataSupplier.getSelectedNamaToko();

           
            txt_idsupplier.setText(selectedIdSupplier);
            txt_namatoko.setText(selectedNamaToko);
        }
    }//GEN-LAST:event_otherkar1ActionPerformed

    private void spinDPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spinDPKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_spinDPKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup StatusPembayaran;
    private keeptoo.KButton btn_add;
    private keeptoo.KButton btn_hapus;
    private keeptoo.KButton btn_pesan;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private keeptoo.KGradientPanel kGradientPanel4;
    private keeptoo.KGradientPanel kGradientPanel6;
    private keeptoo.KGradientPanel kGradientPanel7;
    private keeptoo.KGradientPanel kGradientPanel8;
    private keeptoo.KGradientPanel kGradientPanel9;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton otherkar;
    private javax.swing.JButton otherkar1;
    private javax.swing.JButton otherpes;
    private javax.swing.JRadioButton rd_belum;
    private javax.swing.JRadioButton rd_lunas;
    private com.toedter.components.JSpinField spinDP;
    private javax.swing.JPanel tambahTransJual;
    private javax.swing.JTable tbl_keranjang;
    private javax.swing.JTextField txt_hargajual;
    private javax.swing.JTextField txt_idbahan;
    private javax.swing.JTextField txt_idkaryawan;
    private javax.swing.JTextField txt_idpembayaran;
    private javax.swing.JTextField txt_idpembelian;
    private javax.swing.JTextField txt_idsupplier;
    private javax.swing.JTextField txt_kembalian;
    private javax.swing.JTextField txt_kodedetail;
    private javax.swing.JTextField txt_namabahan;
    private javax.swing.JTextField txt_namakaryawan;
    private javax.swing.JTextField txt_namatoko;
    private javax.swing.JTextField txt_sisa;
    private javax.swing.JTextField txt_subtotalharga;
    private javax.swing.JTextField txt_subtotalpes;
    private com.toedter.calendar.JDateChooser txt_tglpembayaran;
    private com.toedter.calendar.JDateChooser txt_tglpembelian;
    private javax.swing.JTextField txt_totalbayar;
    private javax.swing.JTextField txt_totalharga;
    private javax.swing.JTextField txt_totalpembelian;
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package form;

import config.koneksi;
import java.awt.Color;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Rifal
 */
public class Form_Produksi extends javax.swing.JPanel {

    private int counterProduksi = 1;

    private boolean isKodeProduksiExistsInDatabase(String kodeProduksi, int index) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        java.sql.ResultSet resultSet = null;

        try {
            connection = koneksi.getConnection(); 

           
            String sql = "SELECT KODEPRODUKSI FROM PRODUKSI WHERE KODEPRODUKSI = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, kodeProduksi);

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

    private void generateKodeProduksi() {
        
        Date selectedDate = txt_tglproduksi.getDate();

   
        counterProduksi = 1;

       
        if (selectedDate != null) {
            String kodeProduksi = generateKodeProduksi(selectedDate);

          
            while (isKodeProduksiExistsInDatabase(kodeProduksi, 1)) {
              
                counterProduksi++;
                kodeProduksi = generateKodeProduksi(selectedDate);
            }

           
            txt_kodeproduksi.setText(kodeProduksi);
        } else {

        }
    }

    private String generateKodeProduksi(Date tanggalProduksi) {
      
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePart = dateFormat.format(tanggalProduksi);

       
        String productionCode = "PROD-" + datePart + "-" + counterProduksi;

        return productionCode;
    }

    private void autoResizeAllColumns() {
        int columns = tbl_produksi.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn column = tbl_produksi.getColumnModel().getColumn(i);
            int width = (int) tbl_produksi.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(tbl_produksi, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < tbl_produksi.getRowCount(); row++) {
                int preferedWidth = (int) tbl_produksi.getCellRenderer(row, i)
                        .getTableCellRendererComponent(tbl_produksi, tbl_produksi.getValueAt(row, i), false, false, row, i)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            column.setPreferredWidth(width);
        }
    }

    private void loadData() {
        DefaultTableModel prdks = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        prdks.addColumn("No");
        prdks.addColumn("Kode Produksi");
        prdks.addColumn("Tanggal Produksi");
        prdks.addColumn("ID Karyawan");
        prdks.addColumn("ID Mesin");
        prdks.addColumn("ID Bahan");
        prdks.addColumn("Bahan Diperlukan");
        prdks.addColumn("ID Produk");
        prdks.addColumn("Jumlah Produksi");

        tbl_produksi.setAutoCreateRowSorter(true); 

        try {
            int no = 1;
            String sql = "SELECT * FROM PRODUKSI";
            java.sql.Connection mysqlconfig = (Connection) koneksi.getConnection();
            java.sql.Statement stm = mysqlconfig.createStatement();
            java.sql.ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                prdks.addRow(new Object[]{no++,
                    res.getString("kodeproduksi"),
                    res.getDate("tglproduksi"),
                    res.getString("idkaryawan"),
                    res.getString("idmesin"),
                    res.getString("idbahan"),
                    res.getInt("jumlahbahan"),
                    res.getString("idproduk"),
                    res.getInt("jumlahproduksi")});
            }
            tbl_produksi.setModel(prdks);
            autoResizeAllColumns();

          
            for (int i = 0; i < prdks.getColumnCount(); i++) {
                tbl_produksi.getColumnModel().getColumn(i).setCellEditor(null);
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

            for (int i = 0; i < prdks.getColumnCount(); i++) {
                tbl_produksi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        } catch (Exception e) {
            
        }
    }

private void tambahData() {
    Connection koneksiPostgreSQL = null;
    try {
        koneksiPostgreSQL = koneksi.getConnection();
        koneksiPostgreSQL.setAutoCommit(false);

      
        String insertProduksiSql = "INSERT INTO produksi (kodeproduksi, idmesin, idbahan, idproduk, idkaryawan, tglproduksi, jumlahbahan, jumlahproduksi) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertProduksiStatement = koneksiPostgreSQL.prepareStatement(insertProduksiSql)) {
            java.util.Date tglproduksi = txt_tglproduksi.getDate();

            insertProduksiStatement.setString(1, txt_kodeproduksi.getText());
            insertProduksiStatement.setString(2, txt_idmesin.getText());
            insertProduksiStatement.setString(3, txt_idbahan.getText());
            insertProduksiStatement.setString(4, txt_idproduk.getText());
            insertProduksiStatement.setString(5, txt_idkaryawan.getText());
            insertProduksiStatement.setDate(6, new java.sql.Date(tglproduksi.getTime()));
            insertProduksiStatement.setInt(7, Integer.parseInt(txt_bahandiperlukan.getText()));
            insertProduksiStatement.setInt(8, Integer.parseInt(txt_jumlahproduksi.getText()));

            insertProduksiStatement.executeUpdate();
        }

       
        String pemanggilanFungsiTrigger = "SELECT perbarui_stok_setelah_produksi(?, ?, ?, ?)";
        try (PreparedStatement panggilFungsiTrigger = koneksiPostgreSQL.prepareStatement(pemanggilanFungsiTrigger)) {
            panggilFungsiTrigger.setString(1, txt_idbahan.getText());
            panggilFungsiTrigger.setInt(2, Integer.parseInt(txt_bahandiperlukan.getText()));
            panggilFungsiTrigger.setString(3, txt_idproduk.getText());
            panggilFungsiTrigger.setInt(4, Integer.parseInt(txt_jumlahproduksi.getText()));

            panggilFungsiTrigger.executeQuery();
        }

        koneksiPostgreSQL.commit();
        JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");

    } catch (SQLException | NumberFormatException e) {
        if (koneksiPostgreSQL != null) {
            try {
                koneksiPostgreSQL.rollback();
            } catch (SQLException ex) {
               
            }
        }
        JOptionPane.showMessageDialog(this, e.getMessage());

    } finally {
        if (koneksiPostgreSQL != null) {
            try {
                koneksiPostgreSQL.setAutoCommit(true);
                koneksiPostgreSQL.close();
            } catch (SQLException e) {
               
            }
        }
    }

    loadData();
    bersihform();
}



    private void bersihform() {
         Date tglProduksi = txt_tglproduksi.getDate();
        txt_kodeproduksi.setText("");
        txt_idmesin.setText("");
        txt_idbahan.setText("");
        txt_idproduk.setText("");
        txt_idkaryawan.setText("");
        txt_bahandiperlukan.setText("");
        txt_jumlahproduksi.setText("");
        txt_kodejenis.setText("");
        txt_tglproduksi.setDate(null);
        txt_namabahan.setText("");
        txt_namakaryawan.setText("");
        txt_namamesin.setText("");
        txt_namaproduk.setText("");
        txt_tglproduksi.setDate(tglProduksi);
    }


private void hapusData() {
    
    int selectedRowIndex = tbl_produksi.getSelectedRow();

    if (selectedRowIndex != -1) {

        String kodeproduksiToDelete = tbl_produksi.getValueAt(selectedRowIndex, 1).toString();

        try (Connection koneksiPostgreSQL = koneksi.getConnection()) {
            String pemanggilanFungsiTrigger = "SELECT hapus_data_setelah_produksi(?)";

            try (PreparedStatement panggilFungsiTrigger = koneksiPostgreSQL.prepareStatement(pemanggilanFungsiTrigger)) {
                panggilFungsiTrigger.setString(1, kodeproduksiToDelete);
                panggilFungsiTrigger.executeQuery();

                JOptionPane.showMessageDialog(this, "Data dengan Kode Produksi " + kodeproduksiToDelete + " telah dihapus. Stok diperbarui.");
                loadData();
                bersihform();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus.");
    }
}
private void filterDataProduksi(String keyword, String selectedField) {
    DefaultTableModel produksiModel = (DefaultTableModel) tbl_produksi.getModel();
    produksiModel.setRowCount(0);

    String sql = "SELECT * FROM PRODUKSI WHERE ";

    switch (selectedField) {
        case "Kode Produksi":
            sql += "LOWER(KODEPRODUKSI) LIKE LOWER(?)";
            break;
        case "ID Mesin":
            sql += "LOWER(IDMESIN) LIKE LOWER(?)";
            break;
        case "ID Bahan":
            sql += "LOWER(IDBAHAN) LIKE LOWER(?)";
            break;
        case "ID Produk":
            sql += "LOWER(IDPRODUK) LIKE LOWER(?)";
            break;
        case "ID Karyawan":
            sql += "LOWER(IDKARYAWAN) LIKE LOWER(?)";
            break;
        case "Tanggal Produksi":
            sql += "LOWER(TGLPRODUKSI) LIKE LOWER(?)";
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
                produksiModel.addRow(new Object[]{
                        no++,
                           res.getString("kodeproduksi"),
                    res.getDate("tglproduksi"),
                    res.getString("idkaryawan"),
                    res.getString("idmesin"),
                    res.getString("idbahan"),
                    res.getInt("jumlahbahan"),
                    res.getString("idproduk"),
                    res.getInt("jumlahproduksi")
                });
            }
            autoResizeAllColumns(); 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    public Form_Produksi() {
        initComponents();
         txt_bahandiperlukan.setEditable(false);
         txt_idproduk.setEditable(false);
          txt_idbahan.setEditable(false);
           txt_namabahan.setEditable(false);
           txt_idmesin.setEditable(false);
           txt_namamesin.setEditable(false);
           txt_namakaryawan.setEditable(false);
           txt_idkaryawan.setEditable(false);
           
         txt_kodejenis.setEditable(false);
         txt_namaproduk.setEditable(false);
        txt_tglproduksi.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                
                if ("date".equals(evt.getPropertyName())) {
                    
                    generateKodeProduksi();
                }
            }
        });
      
        txt_jumlahproduksi.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateBahandiperlukan();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateBahandiperlukan();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateBahandiperlukan();
            }
        });
        Color headerColor = new Color(194, 217, 255);
        tbl_produksi.getTableHeader().setBackground(headerColor);
        loadData();
                     txt_cari.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        String selectedField = (String) cb_cari.getSelectedItem();
        filterDataProduksi(txt_cari.getText(), selectedField);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        String selectedField = (String) cb_cari.getSelectedItem();
        filterDataProduksi( txt_cari.getText(), selectedField);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    
    }
});
    }

    private void updateBahandiperlukan() {
       
        String jumlahProduksiText = txt_jumlahproduksi.getText();

        
        txt_bahandiperlukan.setText(jumlahProduksiText);
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
        tambahProduksi = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_kodeproduksi = new javax.swing.JTextField();
        txt_tglproduksi = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        jLabel5 = new javax.swing.JLabel();
        txt_idmesin = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_namamesin = new javax.swing.JTextField();
        btn_othermesin = new javax.swing.JButton();
        kGradientPanel3 = new keeptoo.KGradientPanel();
        jLabel8 = new javax.swing.JLabel();
        txt_idkaryawan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_namakaryawan = new javax.swing.JTextField();
        btn_otherkar = new javax.swing.JButton();
        kGradientPanel4 = new keeptoo.KGradientPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txt_idproduk = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_namaproduk = new javax.swing.JTextField();
        btn_otherbrg = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txt_jumlahproduksi = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txt_kodejenis = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_produksi = new javax.swing.JTable();
        btn_start = new keeptoo.KButton();
        kGradientPanel5 = new keeptoo.KGradientPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txt_idbahan = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txt_namabahan = new javax.swing.JTextField();
        btn_otherbahan = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txt_bahandiperlukan = new javax.swing.JTextField();
        btn_hapus = new keeptoo.KButton();
        txttanggal1 = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txttanggal2 = new com.toedter.calendar.JDateChooser();
        btn_cetak = new javax.swing.JButton();
        cb_cari = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        txt_cari = new javax.swing.JTextField();

        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        tambahProduksi.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel2.setText("Tambah Produksi");

        kGradientPanel1.setBackground(new java.awt.Color(255, 255, 255));
        kGradientPanel1.setBorder(new javax.swing.border.MatteBorder(null));
        kGradientPanel1.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel1.setkStartColor(new java.awt.Color(51, 153, 255));

        jLabel3.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel3.setText("Kode Produksi");

        txt_kodeproduksi.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_kodeproduksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kodeproduksiActionPerformed(evt);
            }
        });

        txt_tglproduksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_tglproduksiMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel4.setText("Tanggal Produksi");

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_kodeproduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 100, Short.MAX_VALUE))
                    .addComponent(txt_tglproduksi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_tglproduksi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_kodeproduksi))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        kGradientPanel2.setBackground(new java.awt.Color(255, 255, 255));
        kGradientPanel2.setBorder(new javax.swing.border.MatteBorder(null));
        kGradientPanel2.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel2.setkStartColor(new java.awt.Color(255, 204, 153));

        jLabel5.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel5.setText("ID Mesin");

        txt_idmesin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel6.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel6.setText("Nama Mesin");

        txt_namamesin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        btn_othermesin.setText("...");
        btn_othermesin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_othermesinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(105, 105, 105))
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addComponent(txt_idmesin, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_namamesin, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_othermesin)
                        .addGap(13, 13, 13))))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_idmesin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_namamesin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(btn_othermesin)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        kGradientPanel3.setBackground(new java.awt.Color(255, 255, 255));
        kGradientPanel3.setBorder(new javax.swing.border.MatteBorder(null));
        kGradientPanel3.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel3.setkStartColor(new java.awt.Color(102, 255, 102));

        jLabel8.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel8.setText("ID Karyawan");

        txt_idkaryawan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel7.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel7.setText("Nama Karyawan");

        txt_namakaryawan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        btn_otherkar.setText("...");
        btn_otherkar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_otherkarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel3Layout = new javax.swing.GroupLayout(kGradientPanel3);
        kGradientPanel3.setLayout(kGradientPanel3Layout);
        kGradientPanel3Layout.setHorizontalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(81, 81, 81))
                    .addGroup(kGradientPanel3Layout.createSequentialGroup()
                        .addComponent(txt_idkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_namakaryawan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_otherkar)
                        .addGap(11, 11, 11))))
        );
        kGradientPanel3Layout.setVerticalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_idkaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_namakaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(btn_otherkar)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        kGradientPanel4.setBackground(new java.awt.Color(255, 255, 255));
        kGradientPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(0, 0, 0)));
        kGradientPanel4.setkBorderRadius(0);
        kGradientPanel4.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel4.setkStartColor(new java.awt.Color(255, 153, 153));

        jLabel11.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel11.setText("Produk");

        javax.swing.GroupLayout kGradientPanel4Layout = new javax.swing.GroupLayout(kGradientPanel4);
        kGradientPanel4.setLayout(kGradientPanel4Layout);
        kGradientPanel4Layout.setHorizontalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addComponent(jLabel11)
                .addContainerGap(203, Short.MAX_VALUE))
        );
        kGradientPanel4Layout.setVerticalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 0, 0)));

        jLabel9.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel9.setText("ID Produk");

        txt_idproduk.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel10.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel10.setText("Nama Produk");

        txt_namaproduk.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        btn_otherbrg.setText("...");
        btn_otherbrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_otherbrgActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel13.setText("Jumlah yang ingin diproduksi");

        txt_jumlahproduksi.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel12.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel12.setText("Kode Jenis Es");

        txt_kodejenis.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        txt_kodejenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kodejenisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txt_idproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_namaproduk)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_otherbrg))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(13, 13, 13))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_kodejenis, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_jumlahproduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_idproduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_namaproduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_otherbrg))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_kodejenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_jumlahproduksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel14.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 24)); // NOI18N
        jLabel14.setText("Data Produksi");

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

        tbl_produksi.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tbl_produksi);

        btn_start.setText("START");
        btn_start.setkBackGroundColor(new java.awt.Color(255, 255, 255));
        btn_start.setkEndColor(new java.awt.Color(255, 204, 204));
        btn_start.setkHoverEndColor(new java.awt.Color(255, 255, 255));
        btn_start.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btn_start.setkHoverStartColor(new java.awt.Color(51, 102, 255));
        btn_start.setkPressedColor(new java.awt.Color(102, 255, 102));
        btn_start.setkSelectedColor(new java.awt.Color(51, 51, 255));
        btn_start.setkStartColor(new java.awt.Color(255, 51, 102));
        btn_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_startActionPerformed(evt);
            }
        });

        kGradientPanel5.setBackground(new java.awt.Color(255, 255, 255));
        kGradientPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(0, 0, 0)));
        kGradientPanel5.setkBorderRadius(0);
        kGradientPanel5.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel5.setkStartColor(new java.awt.Color(255, 255, 153));

        jLabel15.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel15.setText("Bahan Baku");

        javax.swing.GroupLayout kGradientPanel5Layout = new javax.swing.GroupLayout(kGradientPanel5);
        kGradientPanel5.setLayout(kGradientPanel5Layout);
        kGradientPanel5Layout.setHorizontalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel5Layout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(jLabel15)
                .addContainerGap(184, Short.MAX_VALUE))
        );
        kGradientPanel5Layout.setVerticalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 0, 0)));

        jLabel16.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel16.setText("ID Bahan");

        txt_idbahan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel17.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel17.setText("Nama Bahan");

        txt_namabahan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        btn_otherbahan.setText("...");
        btn_otherbahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_otherbahanActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Geometr212 BkCn BT", 1, 14)); // NOI18N
        jLabel18.setText("Jumlah bahan yang diperlukan");

        txt_bahandiperlukan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(txt_idbahan, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txt_namabahan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_otherbahan))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(13, 13, 13))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_bahandiperlukan, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_idbahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_namabahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_otherbahan))
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_bahandiperlukan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        btn_hapus.setText("HAPUS");
        btn_hapus.setkBackGroundColor(new java.awt.Color(255, 255, 255));
        btn_hapus.setkEndColor(new java.awt.Color(204, 204, 204));
        btn_hapus.setkHoverEndColor(new java.awt.Color(255, 255, 255));
        btn_hapus.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btn_hapus.setkHoverStartColor(new java.awt.Color(51, 102, 255));
        btn_hapus.setkPressedColor(new java.awt.Color(102, 255, 102));
        btn_hapus.setkSelectedColor(new java.awt.Color(51, 51, 255));
        btn_hapus.setkStartColor(new java.awt.Color(51, 51, 51));
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        jLabel19.setText("Tanggal Awal");

        jLabel20.setText("Tanggal Akhir");

        btn_cetak.setText("Cetak");
        btn_cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cetakActionPerformed(evt);
            }
        });

        cb_cari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kode Produksi", "ID Mesin", "ID Bahan", "ID Produk", "ID Karyawan", "Tanggal Produksi" }));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_database_30px.png"))); // NOI18N

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tambahProduksiLayout = new javax.swing.GroupLayout(tambahProduksi);
        tambahProduksi.setLayout(tambahProduksiLayout);
        tambahProduksiLayout.setHorizontalGroup(
            tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahProduksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(tambahProduksiLayout.createSequentialGroup()
                            .addComponent(btn_start, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(53, 53, 53)
                            .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(kGradientPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                            .addComponent(kGradientPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(kGradientPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(kGradientPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tambahProduksiLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
                            .addGroup(tambahProduksiLayout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(tambahProduksiLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tambahProduksiLayout.createSequentialGroup()
                                .addComponent(txttanggal1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(txttanggal2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_cetak)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tambahProduksiLayout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(80, 80, 80)
                                .addComponent(jLabel20)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        tambahProduksiLayout.setVerticalGroup(
            tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahProduksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel14))
                .addGap(32, 32, 32)
                .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tambahProduksiLayout.createSequentialGroup()
                        .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kGradientPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kGradientPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kGradientPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_start, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel21)
                        .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(tambahProduksiLayout.createSequentialGroup()
                            .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19)
                                .addComponent(jLabel20))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(tambahProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btn_cetak)
                                .addComponent(txttanggal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txttanggal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        mainPanel.add(tambahProduksi, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_othermesinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_othermesinActionPerformed

        String kodeJenis = txt_kodejenis.getText();

     
        if (!kodeJenis.isEmpty()) {
           
            Dialog_DataMesin dialogDataMesin = new Dialog_DataMesin(new javax.swing.JFrame(), true);

           
            dialogDataMesin.loadMesinByKodeJenis(kodeJenis);

            
            dialogDataMesin.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                 
                    if (dialogDataMesin.isMachineSelected()) {
                      
                        String selectedIdMesin = dialogDataMesin.getSelectedIdMesin();
                        String selectedNamaMesin = dialogDataMesin.getSelectedNamaMesin();

                        txt_idmesin.setText(selectedIdMesin);
                        txt_namamesin.setText(selectedNamaMesin);
                    }
                }
            });
            dialogDataMesin.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Masukkan kodejenis terlebih dahulu.");
        }


    }//GEN-LAST:event_btn_othermesinActionPerformed

    private void btn_otherkarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_otherkarActionPerformed
        
        Dialog_DataOperator dialogDataOperator = new Dialog_DataOperator(new javax.swing.JFrame(), true);
        dialogDataOperator.setVisible(true);

        
        if (dialogDataOperator.isKaryawanSelected()) {
           
            String selectedIdKaryawan = dialogDataOperator.getSelectedIdKaryawan();
            String selectedNamaKaryawan = dialogDataOperator.getSelectedNamaKaryawan();

            
            txt_idkaryawan.setText(selectedIdKaryawan);
            txt_namakaryawan.setText(selectedNamaKaryawan);
        }
    }//GEN-LAST:event_btn_otherkarActionPerformed

    private void btn_otherbrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_otherbrgActionPerformed
     
        Dialog_DataBarang dialogDataBarang = new Dialog_DataBarang(new javax.swing.JFrame(), true);
        dialogDataBarang.setVisible(true);

        
        if (dialogDataBarang.isBarangSelected()) {
            
            String selectedIdBarang = dialogDataBarang.getSelectedIdBarang();
            String selectedKodeJenis = dialogDataBarang.getSelectedKodeJenis();
            String selectedNamaBarang = dialogDataBarang.getSelectedNamaBarang();

            
            txt_idproduk.setText(selectedIdBarang);
            txt_kodejenis.setText(selectedKodeJenis);
            txt_namaproduk.setText(selectedNamaBarang);
        }
    }//GEN-LAST:event_btn_otherbrgActionPerformed

    private void txt_kodejenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodejenisActionPerformed
       
    }//GEN-LAST:event_txt_kodejenisActionPerformed

    private void btn_otherbahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_otherbahanActionPerformed
       
        Dialog_DataBahan dialogDataBahan = new Dialog_DataBahan(new javax.swing.JFrame(), true);
        dialogDataBahan.setVisible(true);

       
        if (dialogDataBahan.isBahanSelected()) {
         
            String selectedIdBahan = dialogDataBahan.getSelectedIdBahan();
            String selectedNamaBahan = dialogDataBahan.getSelectedNamaBahan();

          
            txt_idbahan.setText(selectedIdBahan);
            txt_namabahan.setText(selectedNamaBahan);
        }
    }//GEN-LAST:event_btn_otherbahanActionPerformed

    private void btn_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_startActionPerformed
    
        tambahData();
    }//GEN-LAST:event_btn_startActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
       
        hapusData();
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void txt_tglproduksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_tglproduksiMouseClicked
       
    }//GEN-LAST:event_txt_tglproduksiMouseClicked

    private void txt_kodeproduksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodeproduksiActionPerformed
        
    }//GEN-LAST:event_txt_kodeproduksiActionPerformed

    private void btn_cetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cetakActionPerformed
        try {
            Connection conn = koneksi.getConnection();
            String reportPath = "/report/Produksi.jrxml";

            
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

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
       
    }//GEN-LAST:event_txt_cariActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cetak;
    private keeptoo.KButton btn_hapus;
    private javax.swing.JButton btn_otherbahan;
    private javax.swing.JButton btn_otherbrg;
    private javax.swing.JButton btn_otherkar;
    private javax.swing.JButton btn_othermesin;
    private keeptoo.KButton btn_start;
    private javax.swing.JComboBox<String> cb_cari;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private keeptoo.KGradientPanel kGradientPanel1;
    private keeptoo.KGradientPanel kGradientPanel2;
    private keeptoo.KGradientPanel kGradientPanel3;
    private keeptoo.KGradientPanel kGradientPanel4;
    private keeptoo.KGradientPanel kGradientPanel5;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel tambahProduksi;
    private javax.swing.JTable tbl_produksi;
    private javax.swing.JTextField txt_bahandiperlukan;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_idbahan;
    private javax.swing.JTextField txt_idkaryawan;
    private javax.swing.JTextField txt_idmesin;
    private javax.swing.JTextField txt_idproduk;
    private javax.swing.JTextField txt_jumlahproduksi;
    private javax.swing.JTextField txt_kodejenis;
    private javax.swing.JTextField txt_kodeproduksi;
    private javax.swing.JTextField txt_namabahan;
    private javax.swing.JTextField txt_namakaryawan;
    private javax.swing.JTextField txt_namamesin;
    private javax.swing.JTextField txt_namaproduk;
    private com.toedter.calendar.JDateChooser txt_tglproduksi;
    private com.toedter.calendar.JDateChooser txttanggal1;
    private com.toedter.calendar.JDateChooser txttanggal2;
    // End of variables declaration//GEN-END:variables
}

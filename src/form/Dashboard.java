/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package form;

import Grafik.GrafikBahanDibeli;
import Grafik.GrafikGajian;
import Grafik.GrafikTotalGajian;
import config.koneksi;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;
import Grafik.GrafikPembelian;
import Grafik.GrafikPesanan;
import Grafik.GrafikProdukTerjual;
import Grafik.GrafikProduksi;
import Grafik.GrafikTotalProduksi;
import Grafik.GrafikTotalPemasukan;
import Grafik.GrafikTotalPengeluaran;
import Grafik.PieChartBahan;
import Grafik.PieChartProduk;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Rifal
 */
public class Dashboard extends javax.swing.JPanel {

    private void updateJumlahKaryawan() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = koneksi.getConnection();
            String query = "SELECT COUNT(*) AS jumlahkaryawan FROM karyawan";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int jumlahKaryawan = rs.getInt("jumlahkaryawan");
                lb_jumlahkaryawan.setText(Integer.toString(jumlahKaryawan));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {

            closeResources(stmt, rs);
        }
    }

    private void updateTotalPembelianNgutang() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = koneksi.getConnection();
            String query = "SELECT COUNT(*) AS totalpembelianngutang FROM pembelian WHERE statuspembelian = 'Belum Lunas'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int totalPembelianNgutang = rs.getInt("totalpembelianngutang");
                lb_beliNgutang.setText(Integer.toString(totalPembelianNgutang));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
    }

    private void updateTotalPembelian() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = koneksi.getConnection();
            String query = "SELECT COUNT(*) AS totalpembelianlunas FROM pembelian WHERE statuspembelian = 'Lunas'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int totalPembelian = rs.getInt("totalpembelianlunas");
                lb_totalpembelian.setText(Integer.toString(totalPembelian));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
    }

    private void updateTotalPesanan() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = koneksi.getConnection();
            String query = "SELECT COUNT (*) AS totalpesananlunas FROM pesanan WHERE statuspesanan = 'Lunas'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int totalPesanan = rs.getInt("totalpesananlunas");
                lb_totalpesanan.setText(Integer.toString(totalPesanan));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
    }

    private void updateTotalPesananNgutang() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = koneksi.getConnection();
            String query = "SELECT COUNT (*) AS totalpesananngutang FROM pesanan WHERE statuspesanan = 'Belum Lunas'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int totalUtang = rs.getInt("totalpesananngutang");
                lb_pesananutang.setText(Integer.toString(totalUtang));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
    }

    private void updateJumlahProduksi() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = koneksi.getConnection();
            String query = "SELECT COUNT (*) AS jumlahproduksi FROM produksi";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int jumlahProduksi = rs.getInt("jumlahproduksi");
                lb_jumlahproduksi.setText(Integer.toString(jumlahProduksi));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
    }

    private void closeResources(PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

   
    public Dashboard() {
        initComponents();
        
        updateJumlahKaryawan();
        updateTotalPembelianNgutang();
        updateTotalPembelian();
        updateTotalPesanan();
        updateTotalPesananNgutang();
        updateJumlahProduksi();
updateTopProducts();
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
        Dashboard = new javax.swing.JPanel();
        btnShow = new rojeru_san.RSButton();
        comboGrafik = new rojerusan.RSComboMetro();
        panelCustomTransparanGradien7 = new form.PanelCustomTransparanGradien();
        jLabel13 = new javax.swing.JLabel();
        lbProd1 = new javax.swing.JLabel();
        lbProd2 = new javax.swing.JLabel();
        lbProd3 = new javax.swing.JLabel();
        lbProd4 = new javax.swing.JLabel();
        lbProd5 = new javax.swing.JLabel();
        subTotal1 = new javax.swing.JLabel();
        subTotal2 = new javax.swing.JLabel();
        subTotal3 = new javax.swing.JLabel();
        subTotal4 = new javax.swing.JLabel();
        subTotal5 = new javax.swing.JLabel();
        panelCustomTransparanGradien6 = new form.PanelCustomTransparanGradien();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lb_jumlahproduksi = new javax.swing.JLabel();
        panelCustomTransparanGradien5 = new form.PanelCustomTransparanGradien();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lb_totalpesanan = new javax.swing.JLabel();
        panelCustomTransparanGradien4 = new form.PanelCustomTransparanGradien();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lb_totalpembelian = new javax.swing.JLabel();
        panelCustomTransparanGradien3 = new form.PanelCustomTransparanGradien();
        jLabel2 = new javax.swing.JLabel();
        lb_jumlahkaryawan = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelCustomTransparanGradien2 = new form.PanelCustomTransparanGradien();
        lb_pesananutang = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        panelCustomTransparanGradien1 = new form.PanelCustomTransparanGradien();
        lb_beliNgutang = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        Dashboard.setBackground(new java.awt.Color(255, 255, 255));
        Dashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnShow.setText("Show Graphics");
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });
        Dashboard.add(btnShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 710, 140, 30));

        comboGrafik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih", "Total Gajian", "Gajian Terlaksana", "Pengeluaran", "Bahan Dibeli", "Pembelian Terlaksana", "Pemasukan", "Produk Terjual", "Pesanan Terlaksana", "Total Produksi", "Produksi Terlaksana", "Produk Terlaku", "Bahan Sering Dibeli" }));
        Dashboard.add(comboGrafik, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 710, 300, 30));

        panelCustomTransparanGradien7.setEndColor(new java.awt.Color(204, 204, 204));
        panelCustomTransparanGradien7.setRoundBottomLeft(20);
        panelCustomTransparanGradien7.setRoundBottomRight(20);
        panelCustomTransparanGradien7.setRoundTopLeft(20);
        panelCustomTransparanGradien7.setRoundTopRight(20);
        panelCustomTransparanGradien7.setStartColor(new java.awt.Color(204, 204, 204));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setText("Leaderboard Produk Terjual");

        lbProd1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbProd1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbProd1.setText("Urutan 1");

        lbProd2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbProd2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbProd2.setText("Urutan 2");

        lbProd3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbProd3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbProd3.setText("Urutan 3");

        lbProd4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbProd4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbProd4.setText("Urutan 4");

        lbProd5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbProd5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbProd5.setText("Urutan 5");

        subTotal1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        subTotal1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        subTotal1.setText("Jumlah 1");

        subTotal2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        subTotal2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        subTotal2.setText("Jumlah 2");

        subTotal3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        subTotal3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        subTotal3.setText("Jumlah 3");

        subTotal4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        subTotal4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        subTotal4.setText("Jumlah 4");

        subTotal5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        subTotal5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        subTotal5.setText("Jumlah 5");

        javax.swing.GroupLayout panelCustomTransparanGradien7Layout = new javax.swing.GroupLayout(panelCustomTransparanGradien7);
        panelCustomTransparanGradien7.setLayout(panelCustomTransparanGradien7Layout);
        panelCustomTransparanGradien7Layout.setHorizontalGroup(
            panelCustomTransparanGradien7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelCustomTransparanGradien7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbProd1)
                    .addComponent(lbProd5)
                    .addComponent(lbProd4)
                    .addComponent(lbProd3)
                    .addComponent(lbProd2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                .addGroup(panelCustomTransparanGradien7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subTotal1)
                    .addComponent(subTotal2)
                    .addComponent(subTotal3)
                    .addComponent(subTotal4)
                    .addComponent(subTotal5))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCustomTransparanGradien7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(38, 38, 38))
        );
        panelCustomTransparanGradien7Layout.setVerticalGroup(
            panelCustomTransparanGradien7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addGap(65, 65, 65)
                .addGroup(panelCustomTransparanGradien7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbProd1)
                    .addComponent(subTotal1))
                .addGap(58, 58, 58)
                .addGroup(panelCustomTransparanGradien7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbProd2)
                    .addComponent(subTotal2))
                .addGap(49, 49, 49)
                .addGroup(panelCustomTransparanGradien7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbProd3)
                    .addComponent(subTotal3))
                .addGap(61, 61, 61)
                .addGroup(panelCustomTransparanGradien7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbProd4)
                    .addComponent(subTotal4))
                .addGap(66, 66, 66)
                .addGroup(panelCustomTransparanGradien7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbProd5)
                    .addComponent(subTotal5))
                .addContainerGap(148, Short.MAX_VALUE))
        );

        Dashboard.add(panelCustomTransparanGradien7, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 40, 400, 610));

        panelCustomTransparanGradien6.setEndColor(new java.awt.Color(255, 117, 130));
        panelCustomTransparanGradien6.setRoundBottomLeft(20);
        panelCustomTransparanGradien6.setRoundBottomRight(20);
        panelCustomTransparanGradien6.setRoundTopLeft(20);
        panelCustomTransparanGradien6.setRoundTopRight(20);
        panelCustomTransparanGradien6.setStartColor(new java.awt.Color(114, 90, 122));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setText("Total Produksi");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_ice_30px.png"))); // NOI18N

        lb_jumlahproduksi.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_jumlahproduksi.setText("0");

        javax.swing.GroupLayout panelCustomTransparanGradien6Layout = new javax.swing.GroupLayout(panelCustomTransparanGradien6);
        panelCustomTransparanGradien6.setLayout(panelCustomTransparanGradien6Layout);
        panelCustomTransparanGradien6Layout.setHorizontalGroup(
            panelCustomTransparanGradien6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien6Layout.createSequentialGroup()
                .addGroup(panelCustomTransparanGradien6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustomTransparanGradien6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addGroup(panelCustomTransparanGradien6Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(lb_jumlahproduksi)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCustomTransparanGradien6Layout.setVerticalGroup(
            panelCustomTransparanGradien6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCustomTransparanGradien6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(lb_jumlahproduksi)
                .addGap(56, 56, 56))
        );

        Dashboard.add(panelCustomTransparanGradien6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 480, 310, 170));

        panelCustomTransparanGradien5.setEndColor(new java.awt.Color(153, 153, 255));
        panelCustomTransparanGradien5.setRoundBottomLeft(20);
        panelCustomTransparanGradien5.setRoundBottomRight(20);
        panelCustomTransparanGradien5.setRoundTopLeft(20);
        panelCustomTransparanGradien5.setRoundTopRight(20);
        panelCustomTransparanGradien5.setStartColor(new java.awt.Color(204, 204, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setText("Pesanan Lunas");

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_Order_Completed_30px.png"))); // NOI18N

        lb_totalpesanan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_totalpesanan.setText("0");

        javax.swing.GroupLayout panelCustomTransparanGradien5Layout = new javax.swing.GroupLayout(panelCustomTransparanGradien5);
        panelCustomTransparanGradien5.setLayout(panelCustomTransparanGradien5Layout);
        panelCustomTransparanGradien5Layout.setHorizontalGroup(
            panelCustomTransparanGradien5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien5Layout.createSequentialGroup()
                .addGroup(panelCustomTransparanGradien5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustomTransparanGradien5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11))
                    .addGroup(panelCustomTransparanGradien5Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(lb_totalpesanan)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCustomTransparanGradien5Layout.setVerticalGroup(
            panelCustomTransparanGradien5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelCustomTransparanGradien5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(lb_totalpesanan)
                .addGap(51, 51, 51))
        );

        Dashboard.add(panelCustomTransparanGradien5, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 260, 310, 170));

        panelCustomTransparanGradien4.setEndColor(new java.awt.Color(0, 0, 255));
        panelCustomTransparanGradien4.setRoundBottomLeft(20);
        panelCustomTransparanGradien4.setRoundBottomRight(20);
        panelCustomTransparanGradien4.setRoundTopLeft(20);
        panelCustomTransparanGradien4.setRoundTopRight(20);
        panelCustomTransparanGradien4.setStartColor(new java.awt.Color(153, 255, 255));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_Create_Order_30px.png"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel14.setText("Pembelian Lunas");

        lb_totalpembelian.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_totalpembelian.setText("0");

        javax.swing.GroupLayout panelCustomTransparanGradien4Layout = new javax.swing.GroupLayout(panelCustomTransparanGradien4);
        panelCustomTransparanGradien4.setLayout(panelCustomTransparanGradien4Layout);
        panelCustomTransparanGradien4Layout.setHorizontalGroup(
            panelCustomTransparanGradien4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien4Layout.createSequentialGroup()
                .addGroup(panelCustomTransparanGradien4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCustomTransparanGradien4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCustomTransparanGradien4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(lb_totalpembelian)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        panelCustomTransparanGradien4Layout.setVerticalGroup(
            panelCustomTransparanGradien4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCustomTransparanGradien4Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(panelCustomTransparanGradien4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCustomTransparanGradien4Layout.createSequentialGroup()
                        .addGroup(panelCustomTransparanGradien4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(117, 117, 117))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCustomTransparanGradien4Layout.createSequentialGroup()
                        .addComponent(lb_totalpembelian)
                        .addGap(44, 44, 44))))
        );

        Dashboard.add(panelCustomTransparanGradien4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 310, 170));

        panelCustomTransparanGradien3.setEndColor(new java.awt.Color(255, 227, 179));
        panelCustomTransparanGradien3.setFocusCycleRoot(true);
        panelCustomTransparanGradien3.setRoundBottomLeft(20);
        panelCustomTransparanGradien3.setRoundBottomRight(20);
        panelCustomTransparanGradien3.setRoundTopLeft(20);
        panelCustomTransparanGradien3.setRoundTopRight(20);
        panelCustomTransparanGradien3.setStartColor(new java.awt.Color(221, 68, 112));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Jumlah Karyawan");

        lb_jumlahkaryawan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_jumlahkaryawan.setText("0");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_people_30px.png"))); // NOI18N

        javax.swing.GroupLayout panelCustomTransparanGradien3Layout = new javax.swing.GroupLayout(panelCustomTransparanGradien3);
        panelCustomTransparanGradien3.setLayout(panelCustomTransparanGradien3Layout);
        panelCustomTransparanGradien3Layout.setHorizontalGroup(
            panelCustomTransparanGradien3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelCustomTransparanGradien3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_jumlahkaryawan)
                    .addGroup(panelCustomTransparanGradien3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        panelCustomTransparanGradien3Layout.setVerticalGroup(
            panelCustomTransparanGradien3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelCustomTransparanGradien3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(34, 34, 34)
                .addComponent(lb_jumlahkaryawan)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        Dashboard.add(panelCustomTransparanGradien3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 310, 170));

        panelCustomTransparanGradien2.setEndColor(new java.awt.Color(140, 215, 122));
        panelCustomTransparanGradien2.setRoundBottomLeft(20);
        panelCustomTransparanGradien2.setRoundBottomRight(20);
        panelCustomTransparanGradien2.setRoundTopLeft(20);
        panelCustomTransparanGradien2.setRoundTopRight(20);
        panelCustomTransparanGradien2.setStartColor(new java.awt.Color(250, 159, 66));

        lb_pesananutang.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_pesananutang.setText("0");

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_Cancel_Order_30px.png"))); // NOI18N

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setText("Pesanan Nunggak");

        javax.swing.GroupLayout panelCustomTransparanGradien2Layout = new javax.swing.GroupLayout(panelCustomTransparanGradien2);
        panelCustomTransparanGradien2.setLayout(panelCustomTransparanGradien2Layout);
        panelCustomTransparanGradien2Layout.setHorizontalGroup(
            panelCustomTransparanGradien2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien2Layout.createSequentialGroup()
                .addGroup(panelCustomTransparanGradien2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustomTransparanGradien2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18))
                    .addGroup(panelCustomTransparanGradien2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(lb_pesananutang)))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        panelCustomTransparanGradien2Layout.setVerticalGroup(
            panelCustomTransparanGradien2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelCustomTransparanGradien2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel17))
                .addGap(44, 44, 44)
                .addComponent(lb_pesananutang)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        Dashboard.add(panelCustomTransparanGradien2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 310, 170));

        panelCustomTransparanGradien1.setEndColor(new java.awt.Color(141, 58, 236));
        panelCustomTransparanGradien1.setRoundBottomLeft(20);
        panelCustomTransparanGradien1.setRoundBottomRight(20);
        panelCustomTransparanGradien1.setRoundTopLeft(20);
        panelCustomTransparanGradien1.setRoundTopRight(20);
        panelCustomTransparanGradien1.setStartColor(new java.awt.Color(234, 128, 252));

        lb_beliNgutang.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_beliNgutang.setText("0");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_Cancel_Order_30px.png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText("Pembelian Nunggak");

        javax.swing.GroupLayout panelCustomTransparanGradien1Layout = new javax.swing.GroupLayout(panelCustomTransparanGradien1);
        panelCustomTransparanGradien1.setLayout(panelCustomTransparanGradien1Layout);
        panelCustomTransparanGradien1Layout.setHorizontalGroup(
            panelCustomTransparanGradien1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien1Layout.createSequentialGroup()
                .addGroup(panelCustomTransparanGradien1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustomTransparanGradien1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addGroup(panelCustomTransparanGradien1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lb_beliNgutang)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        panelCustomTransparanGradien1Layout.setVerticalGroup(
            panelCustomTransparanGradien1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomTransparanGradien1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelCustomTransparanGradien1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(lb_beliNgutang)
                .addGap(43, 43, 43))
        );

        Dashboard.add(panelCustomTransparanGradien1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 310, 170));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/5031659.jpg"))); // NOI18N
        Dashboard.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1373, 813));

        mainPanel.add(Dashboard, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed
       String selectedGrafik = (String) comboGrafik.getSelectedItem();

        if (selectedGrafik.equals("Pilih")) {
        JOptionPane.showMessageDialog(this, "Pilih grafik yang ingin ditampilkan.", "Pilihan Invalid", JOptionPane.WARNING_MESSAGE);
        return; 
    }
        
    switch (selectedGrafik) {
        case "Total Gajian":
            showFrame(new GrafikTotalGajian());
            break;
        case "Gajian Terlaksana":
            showFrame(new GrafikGajian());
            break;
        case "Pengeluaran":
            showFrame(new GrafikTotalPengeluaran());
            break;
        case "Bahan Dibeli":
            showFrame(new GrafikBahanDibeli());
            break;
        case "Pembelian Terlaksana":
            showFrame(new GrafikPembelian());
            break;
        case "Pemasukan":
            showFrame(new GrafikTotalPemasukan());
            break;
        case "Produk Terjual":
            showFrame(new GrafikProdukTerjual());
            break;
        case "Pesanan Terlaksana":
            showFrame(new GrafikPesanan());
            break;
        case "Total Produksi":
            showFrame(new GrafikTotalProduksi());
            break;
        case "Produksi Terlaksana":
            showFrame(new GrafikProduksi());
            break;
        case "Produk Terlaku":
           showFrame(new PieChartProduk());
            break;
             case "Bahan Sering Dibeli":
           showFrame(new PieChartBahan());
            break;
        default:

            break;
    }
    }//GEN-LAST:event_btnShowActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Dashboard;
    private rojeru_san.RSButton btnShow;
    private rojerusan.RSComboMetro comboGrafik;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbProd1;
    private javax.swing.JLabel lbProd2;
    private javax.swing.JLabel lbProd3;
    private javax.swing.JLabel lbProd4;
    private javax.swing.JLabel lbProd5;
    private javax.swing.JLabel lb_beliNgutang;
    private javax.swing.JLabel lb_jumlahkaryawan;
    private javax.swing.JLabel lb_jumlahproduksi;
    private javax.swing.JLabel lb_pesananutang;
    private javax.swing.JLabel lb_totalpembelian;
    private javax.swing.JLabel lb_totalpesanan;
    private javax.swing.JPanel mainPanel;
    private form.PanelCustomTransparanGradien panelCustomTransparanGradien1;
    private form.PanelCustomTransparanGradien panelCustomTransparanGradien2;
    private form.PanelCustomTransparanGradien panelCustomTransparanGradien3;
    private form.PanelCustomTransparanGradien panelCustomTransparanGradien4;
    private form.PanelCustomTransparanGradien panelCustomTransparanGradien5;
    private form.PanelCustomTransparanGradien panelCustomTransparanGradien6;
    private form.PanelCustomTransparanGradien panelCustomTransparanGradien7;
    private javax.swing.JLabel subTotal1;
    private javax.swing.JLabel subTotal2;
    private javax.swing.JLabel subTotal3;
    private javax.swing.JLabel subTotal4;
    private javax.swing.JLabel subTotal5;
    // End of variables declaration//GEN-END:variables

    private void showFrame(JFrame frame) {
    frame.setVisible(true);
}
    private void updateTopProducts() {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        conn = koneksi.getConnection();

       
        String query = "SELECT p.namaproduk, SUM(d.subtotalPesanan) AS subtotal "
                     + "FROM detailpesanan d "
                     + "JOIN produk p ON d.idproduk = p.idproduk "
                     + "GROUP BY p.namaproduk, d.idproduk "
                     + "ORDER BY subtotal DESC "
                     + "LIMIT 5";

        stmt = conn.prepareStatement(query);
        rs = stmt.executeQuery();

         int counter = 1;
        while (rs.next() && counter <= 5) {
            String productName = rs.getString("namaproduk");
            int subtotal = rs.getInt("subtotal");

            updateProductLabel(counter, productName);
            updateSubtotalLabel(counter, subtotal);

            counter++;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        closeResources(stmt, rs);
    }
}

private void updateProductLabel(int counter, String productName) {
    switch (counter) {
        case 1:
            lbProd1.setText(productName);
            break;
        case 2:
            lbProd2.setText(productName);
            break;
        case 3:
            lbProd3.setText(productName);
            break;
        case 4:
            lbProd4.setText(productName);
            break;
        case 5:
            lbProd5.setText(productName);
            break;
        default:
            break;
    }
}

private void updateSubtotalLabel(int counter, int subtotal) {
    switch (counter) {
        case 1:
            subTotal1.setText(String.valueOf(subtotal));
            break;
        case 2:
            subTotal2.setText(String.valueOf(subtotal));
            break;
        case 3:
            subTotal3.setText(String.valueOf(subtotal));
            break;
        case 4:
            subTotal4.setText(String.valueOf(subtotal));
            break;
        case 5:
            subTotal5.setText(String.valueOf(subtotal));
            break;
        default:
            break;
    }
}
}

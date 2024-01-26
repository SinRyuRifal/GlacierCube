package Grafik;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import chart.ModelChart;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class GrafikTotalProduksi extends javax.swing.JFrame {

    
    public GrafikTotalProduksi() {
        initComponents();
        chart.setTitle("Chart Data");
        chart.addLegend("Barang Diproduksi", Color.decode("#7b4397"), Color.decode("#dc2430"));
        chart.addLegend("Bahan Diperlukan", Color.decode("#e65c00"), Color.decode("#F9D423"));
       
        setDataProduksi();
    }

 public void setDataProduksi() {
    try {
            JTextField inputFieldYear = new JTextField();
             inputFieldYear.setColumns(10); 
            inputFieldYear.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                        e.consume();
                    }
                }
            });

            JPanel panelYear = new JPanel();
            panelYear.add(new JLabel("Masukkan Tahun:"));
            panelYear.add(inputFieldYear);

            int optionYear = JOptionPane.showOptionDialog(null, panelYear, "Input Tahun", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            if (optionYear == JOptionPane.OK_OPTION) {
                String inputYear = inputFieldYear.getText().trim();

                if (inputYear.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Invalid Input");
                    return;
                }

                int year = Integer.parseInt(inputYear);

        List<ModelDataProduksi> lists = new ArrayList<>();
        DatabaseConnection.getInstance().connectToDatabase();
        String sql = "SELECT TO_CHAR(\"tglproduksi\", 'Month') AS \"Month\",\n" +
"       SUM(\"jumlahproduksi\") AS \"Barang Diproduksi\",\n" +
"       SUM(\"jumlahbahan\") AS \"Bahan Diperlukan\"\n" +
"FROM produksi\n" +
"WHERE EXTRACT(YEAR FROM \"tglproduksi\") = ?\n" +
"GROUP BY TO_CHAR(\"tglproduksi\", 'MMYYYY'), TO_CHAR(\"tglproduksi\", 'Month')\n" +
"ORDER BY TO_CHAR(\"tglproduksi\", 'MMYYYY') DESC, TO_CHAR(\"tglproduksi\", 'Month') DESC";

        PreparedStatement p = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
        p.setInt(1, year); 
        ResultSet r = p.executeQuery();

        while (r.next()) {
            String month = r.getString("Month");
            double amount = r.getDouble("Barang Diproduksi");
            double cost = r.getDouble("Bahan Diperlukan");
           
            lists.add(new ModelDataProduksi(month, amount, cost));
        }
        r.close();
        p.close();

        if (lists.size() == 1) {
            JOptionPane.showMessageDialog(null, "Data Kurang");
        } else {
            for (int i = lists.size() - 1; i >= 0; i--) {
                ModelDataProduksi d = lists.get(i);
                chart.addData(new ModelChart(d.getMonth(), new double[]{d.getAmount(), d.getCost()}));
            }
            chart.start();
       }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Inputan Tidak Valid");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.dispose();
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

        panelShadow1 = new panel.PanelShadow();
        chart = new chart.CurveLineChart();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelShadow1.setBackground(new java.awt.Color(0, 153, 153));
        panelShadow1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelShadow1.setForeground(new java.awt.Color(0, 255, 255));
        panelShadow1.setColorGradient(new java.awt.Color(0, 204, 204));

        chart.setForeground(new java.awt.Color(237, 237, 237));
        chart.setFillColor(true);

        javax.swing.GroupLayout panelShadow1Layout = new javax.swing.GroupLayout(panelShadow1);
        panelShadow1.setLayout(panelShadow1Layout);
        panelShadow1Layout.setHorizontalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShadow1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelShadow1Layout.setVerticalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
   public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("Button.arc", 20);
            UIManager.put("Component.arc", 20);
            UIManager.put("ProgressBar.arc", 20);
            UIManager.put("Component.arrowType", "chevron");
            UIManager.put("Component.innerFocusWidth", 0);
            UIManager.put("Button.innerFocusWidth", 0);
            UIManager.put("ScrollBar.trackArc", 999);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.track", new Color(0xe0e0e0));

        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GrafikTotalProduksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private chart.CurveLineChart chart;
    private panel.PanelShadow panelShadow1;
    // End of variables declaration//GEN-END:variables
}

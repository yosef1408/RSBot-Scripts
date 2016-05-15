package is0lates.GrandExchangeAlcher;

import is0lates.GrandExchangeAlcher.model.AlchItem;
import is0lates.GrandExchangeAlcher.model.AlchItemTableModel;
import org.powerbot.script.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author is0lates
 */
public class GUI extends javax.swing.JFrame {

    public static boolean f2pItemsOnly = false;
    public static boolean maximizeProfit = false;
    public static boolean buyLowerThanGuidePrice = false;
    public static boolean valid = false;
    public static GrandExchangeAlcher grandExchangeAlcher;


    public void timeoutItems() {
        try {
            fetchItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates new form GUI
     */
    public GUI(GrandExchangeAlcher instance) {
        grandExchangeAlcher = instance;
        initComponents();
        jTextField2.setText("" + ((int)(instance.natureRunePrice * 1.10)));
        jLabel7.setVisible(false);
        jTextField1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if (!(ke.getKeyChar() == 27 || ke.getKeyChar() == 65535)) {
                    timeoutItems();
                }
            }
        });
        jTextField2.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if (!(ke.getKeyChar() == 27 || ke.getKeyChar() == 65535)) {
                    timeoutItems();
                    grandExchangeAlcher.buyNatureRunePrice = Integer.parseInt(jTextField2.getText());
                }
            }
        });
        jTextField3.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if (!(ke.getKeyChar() == 27 || ke.getKeyChar() == 65535)) {
                    timeoutItems();
                }
            }
        });
        jTextField4.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if (!(ke.getKeyChar() == 27 || ke.getKeyChar() == 65535)) {
                    timeoutItems();
                }
            }
        });
        jTextField5.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if (!(ke.getKeyChar() == 27 || ke.getKeyChar() == 65535)) {
                    timeoutItems();
                }
            }
        });

        timeoutItems();
    }

    public void fetchItems() throws IOException {
        List<AlchItem> alchItemList = new ArrayList<AlchItem>();
        String[] rows = grandExchangeAlcher.downloadedItems.split("\n");
        int totalPrice = 0;
        for (String row : rows) {
            String[] item = row.split(",");
            AlchItem alchItem = new AlchItem();
            alchItem.id = Integer.parseInt(item[0]);
            alchItem.name = item[1];
            alchItem.price = Integer.parseInt(item[2]);
            alchItem.alchPrice = Integer.parseInt(item[3]);
            alchItem.profit = Integer.parseInt(item[4]);
            alchItem.maxProfit = Integer.parseInt(item[6]);
            alchItem.limit = Integer.parseInt(item[5]);
            alchItem.members = item[7].equals("1");
            alchItem.buyPrice = Integer.parseInt(item[3]) - ((jTextField1.getText().equals("") ? 0 : Integer.parseInt(jTextField1.getText())) + (jTextField2.getText().equals("") ? 0 : Integer.parseInt(jTextField2.getText())));
            if (maximizeProfit) {
                if (alchItem.buyPrice > alchItem.price) {
                    alchItem.buyPrice = alchItem.price;
                }
            }
            alchItem.calcMaxProfit(Integer.parseInt(jTextField2.getText()));
            if (alchItem.members && f2pItemsOnly || (jTextField3.getText().equals("") ? 0 : Integer.parseInt(jTextField3.getText())) > alchItem.limit || (jTextField4.getText().equals("") ? 0 : Integer.parseInt(jTextField4.getText())) < alchItem.limit) {
                continue;
            }
            if (alchItem.buyPrice < 1) {
                continue;
            }
            if ((alchItem.buyPrice < alchItem.price) && !buyLowerThanGuidePrice) {
                continue;
            }
            alchItemList.add(alchItem);
            totalPrice += alchItem.buyPrice * alchItem.limit;
        }
        jLabel8.setText("Use options to filter items then sort items in the order you want alched.");
        jTable1.setModel(new AlchItemTableModel(alchItemList, (jTextField2.getText().equals("") ? 0 : Integer.parseInt(jTextField2.getText()))));
        jTable1.setAutoCreateRowSorter(true);

        jButton1.setEnabled(true);
        jLabel7.setVisible(false);
        int totalProfit = 0;
        int totalItems = 0;
        for (AlchItem alchItem : alchItemList) {
            totalProfit = totalProfit + (int) alchItem.calcMaxProfit;
            totalItems = totalItems + (int) alchItem.limit;
        }
        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        if (alchItemList.isEmpty()) {
            jButton1.setEnabled(false);
            jLabel7.setVisible(true);
            jLabel7.setText("No items to alch, please adjust options.");
            jLabel9.setText("...");
        } else {
            int alchsPerHour = (jTextField5.getText().equals("") ? 1 : Integer.parseInt(jTextField5.getText()));
            double timeRequired = (double) ((((totalItems < 1 ? 1 : totalItems) / alchsPerHour) * 100.0) / 100.0);
            double timeDecimal = (double) (timeRequired - Math.floor(timeRequired));
            int hours = (int) (timeRequired - timeDecimal);
            
            jLabel9.setText("Total Items: " + alchItemList.size()
                    + ", Max Alchs: " + formatter.format(totalItems)
                    + ", Potential Profit: " + formatter.format(totalProfit)
                    + ", Potential XP: " + formatter.format(totalItems * 65)
            );
            jLabel12.setText("Estimated time required: " + hours + " hours"
                    + ", Estimated profit/h: " + formatter.format((totalProfit < 1 ? 1 : totalProfit) / (((totalItems < 1 ? 1 : totalItems) / alchsPerHour) < 1 ? 1 : ((totalItems < 1 ? 1 : totalItems) / alchsPerHour)))
                    + ", Estimated XP/h: " + formatter.format((totalItems < alchsPerHour ? (totalItems * 65) : (alchsPerHour * 65)))
            );
        }
        jButton2.setEnabled(true);
        
        jLabel14.setText("" + formatter.format( (totalPrice / (alchItemList.isEmpty() ? 1 : alchItemList.size()) * 7)));
        
        jButton1.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1003, 1087));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Grand Exchange Alcher");

        jEditorPane1.setEditable(false);
        jEditorPane1.setBackground(new java.awt.Color(240, 240, 240));
        jEditorPane1.setBorder(null);
        jEditorPane1.setText("Caution!\n1. This script interacts with the Grand Exchange.\n2. Cancel all open Buy or Sell orders.\n3. This script alchs all items in you inventory.\n4. Bank any items you do not want alched.\n\nBefore you start\n1. Equip a fire staff.\n2. Place High Alchemy in the first slot of your action bar.\n3. Stand next to a Grand Exchange clerk.");
        jScrollPane1.setViewportView(jEditorPane1);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Min Profit");

        jTextField1.setText("100");
        jTextField1.setToolTipText("The minimum profit to make per item.");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Nature Rune Price");

        jTextField2.setText("365");
        jTextField2.setToolTipText("The price nature runes are bought at, affects the buy price of items.");

        jLabel2.setText("Options");

        jCheckBox1.setText("Maximize Profit");
        jCheckBox1.setToolTipText("Will decrease the chance that items will buy.");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("F2P Items Only");
        jCheckBox2.setToolTipText("Remove members items.");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Min Limit");

        jTextField3.setText("1");
        jTextField3.setToolTipText("The minimum item limit.");

        jLabel11.setText("Max Limit");

        jTextField4.setText("25000");
        jTextField4.setToolTipText("The maximum item limit.");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Buy lower than guide price");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jButton2.setText("~");
        jButton2.setToolTipText("Average Guide Profit as Min Profit.");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(jTextField2)
                            .addComponent(jTextField1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jCheckBox1))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jCheckBox3)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton2.getAccessibleContext().setAccessibleDescription("Average Guide Profit as Min Profit");

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jLabel5.setText("Alch Items");

        jLabel8.setText("Fetching Items...");

        jButton1.setText("Start Script");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("Error");

        jLabel9.setText("...");

        jLabel10.setText("Estimate Alchs per hour");

        jLabel12.setText("...");

        jTextField5.setText("800");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel13.setText("Recommended GP in money pouch:");

        jLabel14.setText("...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13))
                    .addComponent(jScrollPane2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(134, 134, 134)
                        .addComponent(jLabel14)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        maximizeProfit = jCheckBox1.isSelected();
        timeoutItems();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        timeoutItems();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        f2pItemsOnly = jCheckBox2.isSelected();
        grandExchangeAlcher.f2pItemsOnly = jCheckBox2.isSelected();
        timeoutItems();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        buyLowerThanGuidePrice = jCheckBox3.isSelected();
        timeoutItems();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        timeoutItems();
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int totalGuideProfitPerItem = 0;
        AlchItemTableModel model = (AlchItemTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            totalGuideProfitPerItem = totalGuideProfitPerItem + Integer.parseInt(model.getValueAt(i, AlchItemTableModel.COLUMN_GUIDE_RROFIT).toString());
        }
        jTextField1.setText("" + totalGuideProfitPerItem / model.getRowCount());
        timeoutItems();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        timeoutItems();
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        AlchItemTableModel model = (AlchItemTableModel)jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            AlchItem alchItem = new AlchItem();
            alchItem.index = Integer.parseInt(jTable1.getValueAt(i, AlchItemTableModel.COLUMN_NO).toString());
            alchItem.id = Integer.parseInt(jTable1.getValueAt(i, AlchItemTableModel.COLUMN_ID).toString());
            alchItem.name = jTable1.getValueAt(i, AlchItemTableModel.COLUMN_NAME).toString();
            alchItem.limit = Integer.parseInt(jTable1.getValueAt(i, AlchItemTableModel.COLUMN_LIMIT).toString());
            alchItem.alchPrice = Integer.parseInt(jTable1.getValueAt(i, AlchItemTableModel.COLUMN_ALCH_PRICE).toString());
            alchItem.price = Integer.parseInt(jTable1.getValueAt(i, AlchItemTableModel.COLUMN_GUIDE_PRICE).toString());
            alchItem.profit = Integer.parseInt(jTable1.getValueAt(i, AlchItemTableModel.COLUMN_GUIDE_RROFIT).toString());
            alchItem.maxProfit = Integer.parseInt(jTable1.getValueAt(i, AlchItemTableModel.COLUMN_GUIDE_MAX_PROFIT).toString());
            alchItem.members = (Boolean)jTable1.getValueAt(i, AlchItemTableModel.COLUMN_MEMBERS);
            alchItem.buyPrice = Integer.parseInt(jTable1.getValueAt(i, AlchItemTableModel.COLUMN_BUY_PRICE).toString());
            alchItem.calcMaxProfit(Integer.parseInt(jTextField2.getText()));
            grandExchangeAlcher.alchItemList.add(alchItem);
        }
        this.valid = true;
        grandExchangeAlcher.started = true;
        grandExchangeAlcher.buyNatureRunePrice = Integer.parseInt(jTextField2.getText());
        this.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}

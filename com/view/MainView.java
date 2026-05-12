package com.view;

import com.model.User;
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private User currentUser;

    public MainView(User user) {
        this.currentUser = user;
        setTitle("Cửa hàng Thức ăn nhanh - Xin chào " + user.getFullName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(6, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(180, 700));
        sidebar.setBackground(new Color(236, 240, 241));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.LIGHT_GRAY));

        String[] menuItems = {"Trang chủ", "Bán hàng (POS)", "Quản lý món ăn", "Quản lý loại món", "Thống kê", "Đăng xuất"};
        for (String item : menuItems) {
            if (!currentUser.isRole() && (item.equals("Quản lý món ăn") || item.equals("Quản lý loại món") || item.equals("Thống kê"))) {
                continue; 
            }

            JButton btn = new JButton(item);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            sidebar.add(btn);
            
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (!btn.getBackground().equals(new Color(189, 195, 199))) {
                        btn.setBackground(new Color(224, 224, 224));
                    }
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (!btn.getBackground().equals(new Color(189, 195, 199))) {
                        btn.setBackground(Color.WHITE);
                    }
                }
            });
            
            if (item.equals("Đăng xuất")) {
                btn.addActionListener(e -> {
                    new LoginView().setVisible(true);
                    this.dispose();
                });
            }
        }

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JLabel lblWelcome = new JLabel("CHÀO MỪNG ĐẾN VỚI CỬA HÀNG THỨC ĂN NHANH", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(lblWelcome, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Xử lý sự kiện cho từng nút
        for (Component comp : sidebar.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.addActionListener(e -> {
                    for (Component c : sidebar.getComponents()) {
                        if (c instanceof JButton) {
                            c.setBackground(Color.WHITE);
                        }
                    }
                    btn.setBackground(new Color(189, 195, 199));
                    
                    String cmd = btn.getText();
                    if (cmd.equals("Quản lý món ăn")) {
                        showProductManagement(contentPanel);
                    } else if (cmd.equals("Quản lý loại món")) {
                        showCategoryManagement(contentPanel);
                    } else if (cmd.equals("Bán hàng (POS)")) {
                        showPOS(contentPanel);
                    } else if (cmd.equals("Quản lý đơn hàng") || cmd.equals("Thống kê")) {
                        showOrderManagement(contentPanel);
                    } else if (cmd.equals("Trang chủ")) {
                        contentPanel.removeAll();
                        contentPanel.add(lblWelcome, BorderLayout.CENTER);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                    }
                });
            }
        }
    }

    private void showPOS(JPanel contentPanel) {
        contentPanel.removeAll();
        contentPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JPanel productListPanel = new JPanel(new BorderLayout());
        productListPanel.setBorder(BorderFactory.createTitledBorder("Thực đơn"));
        
        // POS Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Chọn loại món: "));
        JComboBox<String> cbCategories = new JComboBox<>();
        cbCategories.addItem("--- Tất cả ---");
        
        try (java.sql.Connection conn = com.util.DBConnection.getConnection();
             java.sql.Statement st = conn.createStatement();
             java.sql.ResultSet rs = st.executeQuery("SELECT * FROM categories")) {
            while (rs.next()) {
                cbCategories.addItem(rs.getInt("categoryId") + " - " + rs.getString("categoryName"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        filterPanel.add(cbCategories);
        productListPanel.add(filterPanel, BorderLayout.NORTH);

        String[] prodCols = {"ID", "Tên món", "Giá"};
        javax.swing.table.DefaultTableModel prodModel = new javax.swing.table.DefaultTableModel(prodCols, 0);
        JTable prodTable = new JTable(prodModel);
        prodTable.setFont(new Font("Arial", Font.PLAIN, 16));
        prodTable.setRowHeight(30);
        prodTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        
        prodTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        prodTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        prodTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        com.dao.ProductDAO prodDAO = new com.dao.ProductDAO();
        
        // Load products
        Runnable loadProducts = () -> {
            prodModel.setRowCount(0);
            String selected = (String) cbCategories.getSelectedItem();
            java.util.List<com.model.Product> list;
            if (selected.equals("--- Tất cả ---")) {
                list = prodDAO.getAllProducts();
            } else {
                int catId = Integer.parseInt(selected.split(" - ")[0]);
                list = prodDAO.getProductsByCategory(catId);
            }
            for (com.model.Product p : list) {
                prodModel.addRow(new Object[]{p.getProductId(), p.getProductName(), p.getPrice()});
            }
        };

        cbCategories.addActionListener(e -> loadProducts.run());
        loadProducts.run();

        productListPanel.add(new JScrollPane(prodTable), BorderLayout.CENTER);
        
        JButton btnAddToCart = new JButton("Thêm vào giỏ hàng >>");
        btnAddToCart.setFont(new Font("Arial", Font.BOLD, 14));
        productListPanel.add(btnAddToCart, BorderLayout.SOUTH);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Giỏ hàng hiện tại"));
        String[] cartCols = {"ID", "Tên món", "SL", "Thành tiền"};
        javax.swing.table.DefaultTableModel cartModel = new javax.swing.table.DefaultTableModel(cartCols, 0);
        JTable cartTable = new JTable(cartModel);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 16));
        cartTable.setRowHeight(30);
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        JLabel lblTotal = new JLabel("Tổng tiền: 0 VNĐ", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        JButton btnCheckout = new JButton("THANH TOÁN & IN HÓA ĐƠN");
        btnCheckout.setBackground(new Color(46, 204, 113));
        btnCheckout.setForeground(Color.BLACK);
        southPanel.add(lblTotal);
        southPanel.add(btnCheckout);
        cartPanel.add(southPanel, BorderLayout.SOUTH);

        btnAddToCart.addActionListener(e -> {
            int row = prodTable.getSelectedRow();
            if (row != -1) {
                int id = (int) prodTable.getValueAt(row, 0);
                String name = (String) prodTable.getValueAt(row, 1);
                double price = (double) prodTable.getValueAt(row, 2);
                cartModel.addRow(new Object[]{id, name, 1, price});
                updateTotal(cartModel, lblTotal);
            }
        });

        btnCheckout.addActionListener(e -> {
            if (cartModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
                return;
            }
            double total = calculateTotal(cartModel);
            com.dao.OrderDAO orderDAO = new com.dao.OrderDAO();
            int orderId = orderDAO.createOrder(total, currentUser.getUserId());
            
            if (orderId != -1) {
                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    int pId = (int) cartModel.getValueAt(i, 0);
                    int qty = (int) cartModel.getValueAt(i, 2);
                    double price = (double) cartModel.getValueAt(i, 3);
                    orderDAO.createOrderDetail(orderId, pId, qty, price);
                }
                JOptionPane.showMessageDialog(this, "Thanh toán thành công! Mã HĐ: " + orderId);
                cartModel.setRowCount(0);
                lblTotal.setText("Tổng tiền: 0 VNĐ");
            }
        });

        contentPanel.add(productListPanel);
        contentPanel.add(cartPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void updateTotal(javax.swing.table.DefaultTableModel model, JLabel label) {
        label.setText("Tổng tiền: " + calculateTotal(model) + " VNĐ");
    }

    private double calculateTotal(javax.swing.table.DefaultTableModel model) {
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += (double) model.getValueAt(i, 3);
        }
        return total;
    }

    private void showOrderManagement(JPanel contentPanel) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        String[] cols = {"Mã HĐ", "Ngày đặt", "Tổng tiền", "ID NV"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0);
        
        try (java.sql.Connection conn = com.util.DBConnection.getConnection();
             java.sql.Statement st = conn.createStatement();
             java.sql.ResultSet rs = st.executeQuery("SELECT * FROM orders ORDER BY orderDate DESC")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("orderId"), rs.getTimestamp("orderDate"), rs.getDouble("priceTotal"), rs.getInt("useId")});
            }
        } catch (Exception e) { e.printStackTrace(); }

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showProductManagement(JPanel contentPanel) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Tên món", "Giá tiền", "Mã loại"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0);
        com.dao.ProductDAO dao = new com.dao.ProductDAO();
        
        java.util.List<com.model.Product> list = dao.getAllProducts();
        for (com.model.Product p : list) {
            model.addRow(new Object[]{p.getProductId(), p.getProductName(), p.getPrice(), p.getCategoryId()});
        }
        
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Actions
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm món");
        JButton btnDelete = new JButton("Xóa món");
        actionPanel.add(btnAdd);
        actionPanel.add(btnDelete);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nhập tên món:");
            String priceStr = JOptionPane.showInputDialog("Nhập giá:");
            if (name != null && priceStr != null) {
                dao.addProduct(name, Double.parseDouble(priceStr), 1);
                showProductManagement(contentPanel);
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) table.getValueAt(row, 0);
                dao.deleteProduct(id);
                showProductManagement(contentPanel);
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showCategoryManagement(JPanel contentPanel) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Tên loại món"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0);
        
        try (java.sql.Connection conn = com.util.DBConnection.getConnection();
             java.sql.Statement st = conn.createStatement();
             java.sql.ResultSet rs = st.executeQuery("SELECT * FROM categories")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("categoryId"), rs.getString("categoryName")});
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm loại");
        JButton btnDelete = new JButton("Xóa loại");
        actionPanel.add(btnAdd);
        actionPanel.add(btnDelete);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        com.dao.CategoryDAO catDAO = new com.dao.CategoryDAO();
        btnAdd.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nhập tên loại món mới:");
            if (name != null && !name.isEmpty()) {
                catDAO.addCategory(name);
                showCategoryManagement(contentPanel);
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) table.getValueAt(row, 0);
                catDAO.deleteCategory(id);
                showCategoryManagement(contentPanel);
            }
        });
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}

package com.view;

import com.model.User;
import javax.swing.*;
import java.awt.*;
import com.model.*;
import com.dao.*;
import java.util.List;

public class MainView extends JFrame {
    private User currentUser;

    public MainView(User user) {
        this.currentUser = user;
        setTitle("Cửa hàng Thức ăn nhanh - Xin chào " + user.getFullName());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setBackground(new Color(236, 240, 241));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.LIGHT_GRAY));

        String[] menuItems = {"Trang chủ", "Bán hàng (POS)", "Lịch sử đơn hàng", "Quản lý món ăn", "Quản lý loại món", "Quản lý người dùng", "Thống kê", "Đăng xuất"};
        for (String item : menuItems) {
            if (!currentUser.isRole() && (item.equals("Quản lý món ăn") || item.equals("Quản lý loại món") || item.equals("Quản lý người dùng") || item.equals("Thống kê"))) {
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
                    } else if (cmd.equals("Lịch sử đơn hàng")) {
                        showOrderManagement(contentPanel);
                    } else if (cmd.equals("Thống kê")) {
                        showStatistics(contentPanel);
                    } else if (cmd.equals("Quản lý người dùng")) {
                        showUserManagement(contentPanel);
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

        JLabel lblTotal = new JLabel("Tổng tiền: 0 VNĐ", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));

        String[] cartCols = {"ID", "Tên món", "SL", "Tiền", "Xóa"};
        javax.swing.table.DefaultTableModel cartModel = new javax.swing.table.DefaultTableModel(cartCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable cartTable = new JTable(cartModel);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 16));
        cartTable.setRowHeight(30);
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        
        cartTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int column = cartTable.columnAtPoint(e.getPoint());
                int row = cartTable.rowAtPoint(e.getPoint());
                if (row >= 0 && column == 4) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa món này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        cartModel.removeRow(row);
                        updateTotal(cartModel, lblTotal);
                    }
                }
            }
        });
        
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new GridLayout(2, 1));
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
                
                boolean found = false;
                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    if ((int) cartModel.getValueAt(i, 0) == id) {
                        int currentQty = (int) cartModel.getValueAt(i, 2);
                        cartModel.setValueAt(currentQty + 1, i, 2);
                        cartModel.setValueAt((currentQty + 1) * price, i, 3);
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    cartModel.addRow(new Object[]{id, name, 1, price, "X"});
                }
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
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Danh sách đơn hàng"));
        
        String[] cols = {"Mã HĐ", "Ngày đặt", "Tổng tiền", currentUser.isRole() ? "Khách hàng" : "Người đặt", "Thao tác"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        com.dao.OrderDAO orderDAO = new com.dao.OrderDAO();
        java.util.List<Object[]> list = currentUser.isRole() ? orderDAO.getAllOrders() : orderDAO.getOrdersByUser(currentUser.getUserId());

        for (Object[] row : list) {
            Object[] newRow = new Object[5];
            System.arraycopy(row, 0, newRow, 0, 4);
            newRow[4] = "Xóa đơn";
            model.addRow(newRow);
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && column == 4) {
                    int id = (int) table.getValueAt(row, 0);
                    int confirm = JOptionPane.showConfirmDialog(MainView.this, "Xóa đơn hàng " + id + " và tất cả chi tiết?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (orderDAO.deleteOrder(id)) {
                            javax.swing.SwingUtilities.invokeLater(() -> showOrderManagement(contentPanel));
                        }
                    }
                }
            }
        });
        topPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết các món trong đơn hàng (Chọn đơn hàng ở trên để xem)"));
        String[] detailCols = {"Tên món", "Số lượng", "Giá bán"};
        javax.swing.table.DefaultTableModel detailModel = new javax.swing.table.DefaultTableModel(detailCols, 0);
        JTable detailTable = new JTable(detailModel);
        detailTable.setFont(new Font("Arial", Font.PLAIN, 16));
        detailTable.setRowHeight(30);
        detailTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        bottomPanel.add(new JScrollPane(detailTable), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setDividerLocation(300);
        contentPanel.add(splitPane, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int orderId = (int) table.getValueAt(row, 0);
                    detailModel.setRowCount(0);
                    java.util.List<Object[]> details = orderDAO.getOrderDetails(orderId);
                    for (Object[] d : details) {
                        detailModel.addRow(d);
                    }
                }
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showStatistics(JPanel contentPanel) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(245, 246, 250));

        com.dao.OrderDAO dao = new com.dao.OrderDAO();
        
        // Header
        JLabel lblTitle = new JLabel("BÁO CÁO CHI TIẾT HOẠT ĐỘNG KINH DOANH", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        contentPanel.add(lblTitle, BorderLayout.NORTH);

        // Main Scrollable Body
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBackground(new Color(245, 246, 250));
        JScrollPane scrollPane = new JScrollPane(bodyPanel);
        scrollPane.setBorder(null);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Row 1: Key Metrics
        JPanel metricsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        metricsPanel.setOpaque(false);
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        double daily = dao.getDailyRevenue();
        int ordersToday = dao.getTotalOrdersToday();
        double monthly = dao.getMonthlyRevenue();
        double avgValue = dao.getAverageOrderValue();

        metricsPanel.add(createStatCard("DOANH THU HÔM NAY", daily, new Color(52, 152, 219)));
        metricsPanel.add(createStatCard("SỐ ĐƠN HÔM NAY", (double)ordersToday, new Color(230, 126, 34), false));
        metricsPanel.add(createStatCard("DOANH THU THÁNG", monthly, new Color(46, 204, 113)));
        metricsPanel.add(createStatCard("TRUNG BÌNH/ĐƠN", avgValue, new Color(155, 89, 182)));
        
        bodyPanel.add(metricsPanel);

        // Row 2: Charts
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        chartsPanel.setOpaque(false);
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        chartsPanel.setPreferredSize(new Dimension(1100, 350));
        
        chartsPanel.add(new SimpleBarChart(dao.getRevenueLast7Days(), "Xu hướng doanh thu 7 ngày qua"));
        chartsPanel.add(new SimpleBarChart(dao.getRevenueLast6Months(), "Xu hướng doanh thu 6 tháng qua"));
        
        bodyPanel.add(chartsPanel);

        // Row 3: Top Products & Total
        JPanel bottomPanel = new JPanel(new BorderLayout(20, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Top 5 Products Table
        JPanel topProductsPanel = new JPanel(new BorderLayout());
        topProductsPanel.setBackground(Color.WHITE);
        topProductsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "TOP 5 SẢN PHẨM BÁN CHẠY NHẤT"));
        
        String[] columns = {"Sản phẩm", "Số lượng bán", "Tổng doanh thu"};
        javax.swing.table.DefaultTableModel topModel = new javax.swing.table.DefaultTableModel(columns, 0);
        for (Object[] row : dao.getTopSellingProducts()) {
            topModel.addRow(new Object[]{row[0], row[1], String.format("%,.0f VNĐ", row[2])});
        }
        JTable topTable = new JTable(topModel);
        topTable.setRowHeight(30);
        topProductsPanel.add(new JScrollPane(topTable), BorderLayout.CENTER);
        topProductsPanel.setPreferredSize(new Dimension(700, 200));

        // Total Cumulative Revenue
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(44, 62, 80));
        totalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTotalLabel = new JLabel("TỔNG DOANH THU TÍCH LŨY", SwingConstants.CENTER);
        lblTotalLabel.setForeground(Color.WHITE);
        lblTotalLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel lblTotalVal = new JLabel(String.format("%,.0f VNĐ", dao.getTotalRevenue()), SwingConstants.CENTER);
        lblTotalVal.setForeground(new Color(241, 196, 15));
        lblTotalVal.setFont(new Font("Arial", Font.BOLD, 36));
        
        totalPanel.add(lblTotalLabel, BorderLayout.NORTH);
        totalPanel.add(lblTotalVal, BorderLayout.CENTER);

        bottomPanel.add(topProductsPanel, BorderLayout.CENTER);
        bottomPanel.add(totalPanel, BorderLayout.EAST);

        bodyPanel.add(bottomPanel);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatCard(String title, double value, Color color) {
        return createStatCard(title, value, color, true);
    }

    private JPanel createStatCard(String title, double value, Color color, boolean isMoney) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        
        String valStr = isMoney ? String.format("%,.0f VNĐ", value) : String.format("%.0f Đơn", value);
        JLabel lblValue = new JLabel(valStr, SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 22));
        lblValue.setForeground(Color.WHITE);
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private void showUserManagement(JPanel contentPanel) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Tên đăng nhập", "Họ tên", "Vai trò"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0);
        com.dao.UserDAO dao = new com.dao.UserDAO();
        
        java.util.List<com.model.User> list = dao.getAllUsers();
        for (com.model.User u : list) {
            model.addRow(new Object[]{u.getUserId(), u.getUserName(), u.getFullName(), u.isRole() ? "Admin" : "Khách hàng"});
        }
        
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton btnDelete = new JButton("Xóa người dùng");
        actionPanel.add(btnDelete);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) table.getValueAt(row, 0);
                if (id == currentUser.getUserId()) {
                    JOptionPane.showMessageDialog(this, "Bạn không thể tự xóa chính mình!");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa người dùng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.deleteUser(id);
                    showUserManagement(contentPanel);
                }
            }
        });

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
           
            JTextField txtName = new JTextField(20);
            JTextField txtPrice = new JTextField(20);
            JComboBox<Categories> cbCategory = new JComboBox<>();

         
            CategoryDAO catDao = new CategoryDAO();
            List<Categories> listCat = catDao.getAllCategories();
            for (Categories cat : listCat) {
                cbCategory.addItem(cat);
            }

            JPanel myPanel = new JPanel(new java.awt.GridLayout(0, 1, 5, 5));
            txtName.setBorder(BorderFactory.createTitledBorder("Tên món"));
            myPanel.add(txtName);
            txtPrice.setBorder(BorderFactory.createTitledBorder("Giá Tiền"));
            myPanel.add(txtPrice);
            cbCategory.setBorder(BorderFactory.createTitledBorder("Chọn loại"));
            myPanel.add(cbCategory);

        
            int result = JOptionPane.showConfirmDialog(null, myPanel, 
                    "Thêm món mới", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String name = txtName.getText().trim();
                    double price = Double.parseDouble(txtPrice.getText().trim());
                    
                  
                    Categories selected = (Categories) cbCategory.getSelectedItem();
                    int catId = selected.getCategoryId(); 

                
                    if (!name.isEmpty()) {
                        dao.addProduct(name, price, catId);
                        JOptionPane.showMessageDialog(null, "Thêm thành công!");
                        showProductManagement(contentPanel); 
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Giá tiền phải là số hợp lệ!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
    class SimpleBarChart extends JPanel {
        private java.util.LinkedHashMap<String, Double> data;
        private String title;

        public SimpleBarChart(java.util.LinkedHashMap<String, Double> data, String title) {
            this.data = data;
            this.title = title;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.isEmpty()) {
                g.drawString("Không có dữ liệu", getWidth()/2 - 40, getHeight()/2);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 50;

            double max = 0;
            for (double v : data.values()) max = Math.max(max, v);
            if (max == 0) max = 1;

            // Trục
            g2.setColor(Color.BLACK);
            g2.drawLine(padding, height - padding, padding, padding); // Y
            g2.drawLine(padding, height - padding, width - padding, height - padding); // X

            int barWidth = (width - 2 * padding) / Math.max(1, data.size()) - 20;
            int x = padding + 10;

            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(title, width/2 - 80, 30);

            for (java.util.Map.Entry<String, Double> entry : data.entrySet()) {
                int barHeight = (int) ((entry.getValue() / max) * (height - 2 * padding - 20));
                
                // Vẽ cột
                g2.setColor(new Color(52, 152, 219, 200));
                g2.fillRect(x, height - padding - barHeight, barWidth, barHeight);
                g2.setColor(new Color(41, 128, 185));
                g2.drawRect(x, height - padding - barHeight, barWidth, barHeight);

                // Nhãn ngày/tháng
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                String label = entry.getKey();
                if (label.length() > 5) label = label.substring(label.length() - 5);
                g2.drawString(label, x, height - padding + 15);
                
                // Giá trị trên cột
                g2.setFont(new Font("Arial", Font.BOLD, 9));
                String valStr = String.format("%.0fk", entry.getValue()/1000);
                g2.drawString(valStr, x, height - padding - barHeight - 5);
                
                x += barWidth + 20;
            }
        }
    }
}

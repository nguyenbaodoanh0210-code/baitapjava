package com.view;

import com.dao.UserDAO;
import com.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginView() {
        setTitle("Đăng nhập - Cửa hàng Thức ăn nhanh");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        
        Font textFont = new Font("Arial", Font.PLAIN, 16);
        
        txtUsername = new JTextField();
        txtUsername.setFont(textFont);
        txtUsername.setBorder(BorderFactory.createTitledBorder("Tên đăng nhập"));
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(textFont);
        txtPassword.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));
        
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBackground(new Color(0, 123, 255));
        btnLogin.setForeground(Color.BLACK); 

        JButton btnRegister = new JButton("Chưa có tài khoản? Đăng ký");
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegister.setForeground(Color.BLUE);
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);

        panel.add(lblTitle);
        panel.add(txtUsername);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnRegister);

        add(panel);

        btnRegister.addActionListener(e -> new RegisterView().setVisible(true));
        btnLogin.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        UserDAO userDAO = new UserDAO();
        User user = userDAO.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            new MainView(user).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}

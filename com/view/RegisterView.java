package com.view;

import com.dao.UserDAO;
import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {
    private JTextField txtUsername, txtFullName;
    private JPasswordField txtPassword;
    private JButton btnRegister, btnBack;

    public RegisterView() {
        setTitle("Đăng ký tài khoản mới");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Font textFont = new Font("Arial", Font.PLAIN, 16);
       
        txtFullName = new JTextField();
        txtFullName.setFont(textFont);
        txtFullName.setBorder(BorderFactory.createTitledBorder("Họ và tên"));

        txtUsername = new JTextField();
        txtUsername.setFont(textFont);
        txtUsername.setBorder(BorderFactory.createTitledBorder("Tên đăng nhập"));
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(textFont);
        txtPassword.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));
        
       
        btnRegister = new JButton("ĐĂNG KÝ NGAY");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegister.setBackground(new Color(46, 204, 113));
        btnRegister.setForeground(Color.BLACK);

        btnBack = new JButton("Quay lại Đăng nhập");
        panel.add(txtFullName);
        panel.add(txtUsername);
        panel.add(txtPassword);
        panel.add(btnRegister);
        panel.add(btnBack);

        add(panel);

        btnRegister.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            String name = txtFullName.getText();

            if (user.isEmpty() || pass.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            UserDAO dao = new UserDAO();
            if (dao.register(user, pass, name)) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Tên đăng nhập đã tồn tại!");
            }
        });

        btnBack.addActionListener(e -> this.dispose());
    }
}

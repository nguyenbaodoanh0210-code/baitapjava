package com.btnhom.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;

import com.btnhom.dao.*;
import com.btnhom.entity.*;

public class GUI extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    public GUI() {
       
        setTitle("Đăng nhập hệ thống");
        setSize(450, 250); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        this.setLayout(new BorderLayout(10, 10));

       
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
     
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        this.add(lblTitle, BorderLayout.NORTH);

        
        JPanel pnlInput = new JPanel(new GridLayout(2, 2, 5, 5));
 
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        pnlInput.add(new JLabel("Tài khoản: ", SwingConstants.LEFT)); // Căn phải cho nhãn
        pnlInput.add(txtUsername);
        pnlInput.add(new JLabel("Mật khẩu: ", SwingConstants.LEFT));
        pnlInput.add(txtPassword);

      
        JPanel pnlCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //tạo khoảng cách trên trái dưới phải
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        pnlCenter.add(pnlInput);
        
     
        txtUsername.setPreferredSize(new Dimension(200, 30));
        txtPassword.setPreferredSize(new Dimension(200, 30));

        this.add(pnlCenter, BorderLayout.CENTER);

     
        JPanel pnlButtons = new JPanel(); 
        btnLogin = new JButton("Đăng nhập");
        btnRegister = new JButton("Đăng Ký");
        
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                performLogin();
            }
        });
        pnlButtons.add(btnLogin);
        pnlButtons.add(btnRegister);
        
     // Tạo khoảng cách dưới đáy
        pnlButtons.add(Box.createVerticalStrut(50));
        this.add(pnlButtons, BorderLayout.SOUTH);
    }
    private void performLogin () {
    	String usename = txtUsername.getText().trim();
    	String password = new String(txtPassword.getPassword());
    	if(usename.isEmpty() || password.isEmpty()) {
    		JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
    		return;	
    	}
    	AccountDAO dao = new AccountDAO();
    	try {
    		Account acc = dao.checkLogin(usename, password);
    		if (acc != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Chào " + acc.getFullName());
                
                // Mở màn hình chính 
                // MainView main = new MainView(acc); // Truyền đối tượng acc sang để biết ai đang đăng nhập
                // main.setVisible(true);
                
                this.dispose(); // Đóng form Login
            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
    	}catch (Exception e) {
    		JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!");
            e.printStackTrace();
		}
    	
    	
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new GUI().setVisible(true);
        });
    }
}
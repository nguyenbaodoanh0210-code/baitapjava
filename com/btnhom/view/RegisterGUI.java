package com.btnhom.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.btnhom.dao.*;
import com.btnhom.entity.*;
import javax.swing.*;

public class RegisterGUI extends JFrame {
	   private JTextField txtUsername,txtFullName;
	    private JPasswordField txtPassword;
	    private JButton  btnRegister,btnBack;

	 
	       public RegisterGUI(){
	        setTitle("Register");
	        setSize(450, 300); 
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null); 
	        this.setLayout(new BorderLayout(10, 10));

	       
	        JLabel lblTitle = new JLabel("ĐĂNG KÝ", SwingConstants.CENTER);
	        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
	        lblTitle.setForeground(Color.BLUE);
	     
	        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
	        this.add(lblTitle, BorderLayout.NORTH);

	        
	        JPanel pnlInput = new JPanel(new GridLayout(3, 2, 5, 5));
	 
	        txtUsername = new JTextField();
	        txtFullName = new JTextField();
	        
	        txtPassword = new JPasswordField();
	        pnlInput.add(new JLabel("Họ và Tên: ", SwingConstants.LEFT));
	        pnlInput.add(txtFullName);
	        pnlInput.add(new JLabel("Tài khoản: ", SwingConstants.LEFT)); // Căn phải cho nhãn
	        pnlInput.add(txtUsername);
	        pnlInput.add(new JLabel("Mật khẩu: ", SwingConstants.LEFT));
	        pnlInput.add(txtPassword);

	      
	        JPanel pnlCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
	        //tạo khoảng cách trên trái dưới phải
	        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
	        pnlCenter.add(pnlInput);
	        
	     
	        txtUsername.setPreferredSize(new Dimension(200, 30));
	        txtFullName.setPreferredSize(new Dimension(200, 30));
	        txtPassword.setPreferredSize(new Dimension(200, 30));

	        this.add(pnlCenter, BorderLayout.CENTER);

	     
	        JPanel pnlButtons = new JPanel(); 
	        
	        btnRegister = new JButton("Đăng Ký");
	        btnBack = new JButton("Quay Lại");
	        btnRegister.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					performRegister();
					
				}
			});
	        btnBack.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// 1. Khởi tạo đối tượng màn hình Đăng ký
			        GUI login = new GUI();
			        
			        // 2. Hiển thị màn hình Đăng ký
			        login.setVisible(true);
			        
			    
			        
			        // 4. Ẩn màn hình Đăng nhập hiện tại đi
			       
			       dispose();
				}
			});
	       
	        pnlButtons.add(btnRegister);
	        pnlButtons.add(btnBack);
	        
	        
	     // Tạo khoảng cách dưới đáy
	        pnlButtons.add(Box.createVerticalStrut(50));
	        this.add(pnlButtons, BorderLayout.SOUTH);
}
	       private void performRegister() {
	    	    String username = txtUsername.getText().trim();
	    	    String password = new String(txtPassword.getPassword());
	    	    String fullName = txtFullName.getText().trim();
	    	    
	    	   
	    	    if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() ) {
	    	        JOptionPane.showMessageDialog(this, "Không được để trống thông tin!");
	    	        return;
	    	    }

	    	    AccountDAO dao = new AccountDAO();

	    	   
	    	    if (dao.isExisted(username)) {
	    	        JOptionPane.showMessageDialog(this, 
	    	            "Tên đăng nhập '" + username + "' đã tồn tại. Vui lòng chọn tên khác!", 
	    	            "Lỗi đăng ký", 
	    	            JOptionPane.WARNING_MESSAGE);
	    	        txtUsername.requestFocus(); // Đưa con trỏ chuột về ô nhập username
	    	        return;
	    	    }

	    	    //Nếu không trùng thì mới thực hiện đăng ký
	    	    Account newAcc = new Account();
	    	    newAcc.setUseName(username);
	    	    newAcc.setPassword(password);
	    	    newAcc.setFullName(txtFullName.getText());
	    	    newAcc.setRole(true); // Mặc định là khách hàng/user thường

	    	    if (dao.register(newAcc)) {
	    	        JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công!");
	    	    } else {
	    	        JOptionPane.showMessageDialog(this, "Đăng ký thất bại, vui lòng thử lại sau.");
	    	    }
	    	}
	       }

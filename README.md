# Fast Food Store Management System (Hệ thống Quản lý Cửa hàng Thức ăn nhanh)

Hệ thống quản lý và bán hàng tại cửa hàng thức ăn nhanh là ứng dụng desktop viết bằng ngôn ngữ **Java** sử dụng thư viện **Swing** cho giao diện người dùng (GUI) và **MySQL** làm cơ sở dữ liệu. Dự án được thiết kế theo mô hình **layered architecture (MVC-like)** giúp quản lý sản phẩm, đơn hàng, người dùng và báo cáo doanh thu trực quan.

---

## 🌟 Tính năng chính

### 1. Phân quyền người dùng (Role-Based Access Control)
*   **Admin (Quản trị viên):** Có toàn quyền truy cập tất cả chức năng trong hệ thống, bao gồm bán hàng, quản lý món ăn, loại món, tài khoản người dùng và xem thống kê báo cáo chi tiết.
*   **Nhân viên / Khách hàng (User):** Chỉ có quyền truy cập vào các chức năng cơ bản như:
    *   **Trang chủ** (Welcome screen).
    *   **Bán hàng (POS):** Gọi món, quản lý giỏ hàng, thanh toán và tạo hóa đơn.
    *   **Lịch sử đơn hàng:** Xem lịch sử mua hàng cá nhân và xem chi tiết từng đơn hàng.

### 2. Quản lý bán hàng (POS Screen)
*   Xem danh sách thực đơn (món ăn) theo từng loại danh mục (Categories).
*   Thêm món vào giỏ hàng, tự động cộng dồn số lượng và cập nhật tổng tiền.
*   Xóa món khỏi giỏ hàng.
*   **Thanh toán & In hóa đơn:** Lưu trữ thông tin hóa đơn và chi tiết hóa đơn vào CSDL MySQL.

### 3. Lịch sử đơn hàng (Order History)
*   Hiển thị danh sách hóa đơn trực quan.
*   Chọn một hóa đơn để xem chi tiết danh sách món ăn, số lượng và giá bán tại thời điểm mua.
*   Hỗ trợ chức năng xóa đơn hàng (chỉ dành cho tài khoản Admin).

### 4. Quản lý món ăn & Danh mục (Food & Category Management)
*   **Quản lý món ăn (Products):** Admin có thể xem danh sách sản phẩm, thêm món mới (nhập tên, giá, chọn loại món từ dropdown list) và xóa món ăn.
*   **Quản lý loại món (Categories):** Admin có thể thêm mới hoặc xóa các loại danh mục món ăn (ví dụ: Hamburger, Đồ uống, Gà rán...).

### 5. Quản lý người dùng (User Management)
*   Đăng ký tài khoản mới trực tiếp từ màn hình đăng ký.
*   Admin có thể xem danh sách tài khoản đã đăng ký trên hệ thống và xóa tài khoản của người dùng (không được tự xóa chính mình).

### 6. Báo cáo & Thống kê trực quan (Analytics & Charts)
*   Hiển thị các chỉ số kinh doanh cốt lõi (Key Metrics):
    *   *Doanh thu hôm nay* (VNĐ).
    *   *Số đơn hàng hôm nay*.
    *   *Doanh thu tháng hiện tại* (VNĐ).
    *   *Giá trị trung bình trên mỗi đơn hàng* (VNĐ).
*   **Biểu đồ doanh thu tự vẽ (Custom Bar Chart):** 
    *   Xu hướng doanh thu trong 7 ngày gần nhất.
    *   Xu hướng doanh thu trong 6 tháng gần nhất.
*   **Top 5 sản phẩm bán chạy nhất:** Bảng danh sách các sản phẩm bán chạy kèm theo số lượng bán và doanh thu đóng góp.
*   **Tổng doanh thu tích lũy:** Hiển thị tổng số tiền mà cửa hàng đã thu được từ trước đến nay.

---

## 🛠️ Công nghệ sử dụng

*   **Ngôn ngữ lập trình:** Java (JDK 8 hoặc mới hơn).
*   **Giao diện người dùng:** Java Swing (GUI) với System Look and Feel đem lại trải nghiệm mượt mà trên hệ điều hành.
*   **Cơ sở dữ liệu:** MySQL (hoặc MariaDB).
*   **Thư viện kết nối:** JDBC (MySQL Connector/J).

---

## 📁 Cấu trúc thư mục dự án

```text
baitapjava/
├── .classpath              # File cấu hình classpath của Eclipse
├── .project                # File thông tin dự án Eclipse
├── .gitignore              # Cấu hình bỏ qua các file không cần thiết khi git push
├── database.sql            # Script khởi tạo cơ sở dữ liệu MySQL
└── com/                    # Source code chính của ứng dụng
    ├── model/              # Các lớp đối tượng (Entities)
    │   ├── User.java       # Thông tin tài khoản người dùng
    │   ├── Product.java    # Thông tin món ăn
    │   └── Categories.java # Thông tin danh mục món ăn
    ├── dao/                # Data Access Objects (Tương tác database)
    │   ├── UserDAO.java
    │   ├── ProductDAO.java
    │   ├── CategoryDAO.java
    │   └── OrderDAO.java
    ├── util/               # Các công cụ hỗ trợ tiện ích
    │   └── DBConnection.java # Cấu hình kết nối MySQL JDBC
    └── view/               # Giao diện Swing (Views)
        ├── LoginView.java     # Màn hình đăng nhập & Điểm chạy chính (Main Class)
        ├── RegisterView.java  # Màn hình đăng ký tài khoản
        └── MainView.java      # Màn hình làm việc chính (Dashboard)
```

---

## 🗄️ Cấu trúc Cơ sở dữ liệu

Dự án sử dụng cơ sở dữ liệu tên là `fastfood_db` bao gồm các bảng:
*   `account`: Quản lý tài khoản đăng nhập (Admin có `role = 1`, User thường có `role = 0`).
*   `categories`: Danh mục phân loại các món ăn.
*   `products`: Danh sách món ăn chi tiết thuộc các danh mục.
*   `orders`: Quản lý thông tin đơn hàng chung (ngày đặt, tổng tiền, người mua).
*   `detailOrder`: Quản lý chi tiết từng món ăn trong một đơn hàng (liên kết món ăn, đơn hàng, số lượng và giá lúc bán).

---

## 🚀 Hướng dẫn cài đặt và Cấu hình

### 1. Chuẩn bị môi trường
*   Cài đặt **Java Development Kit (JDK)** bản 8 trở lên.
*   Cài đặt máy chủ **MySQL Server** (bạn có thể dùng XAMPP, Laragon, MySQL Installer...).

### 2. Khởi tạo Cơ sở dữ liệu
*   Mở MySQL Client (hoặc phpMyAdmin, DBeaver, Navicat...).
*   Import file [database.sql](file:///c:/Users/ANH%20HOA/Documents/DUT/baitapjava/database.sql) vào máy chủ MySQL của bạn để tạo database `fastfood_db` và các bảng dữ liệu mẫu.

### 3. Cấu hình kết nối Database trong code
*   Mở file [DBConnection.java](file:///c:/Users/ANH%20HOA/Documents/DUT/baitapjava/com/util/DBConnection.java).
*   Chỉnh sửa thông số kết nối phù hợp với máy chủ MySQL của bạn (URL, username, password):
    ```java
    String url = "jdbc:mysql://localhost:3306/fastfood_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    String user = "root";   // Tên người dùng database
    String pass = "";       // Mật khẩu database
    ```

### 4. Cài đặt thư viện JDBC Driver
*   Dự án yêu cầu thư viện **MySQL JDBC Connector** (như `mysql-connector-j-*.jar`).
*   Nếu bạn sử dụng Eclipse hoặc các IDE khác:
    1.  Tải xuống MySQL JDBC Connector phù hợp.
    2.  Nhấp chuột phải vào Project -> **Build Path** -> **Configure Build Path**.
    3.  Tại tab **Libraries**, thêm file Jar JDBC Connector vừa tải vào classpath của dự án.

---

## 🏃 Hướng dẫn chạy chương trình

1.  Mở IDE của bạn (Eclipse, VS Code, IntelliJ IDEA) và import dự án.
2.  Đảm bảo máy chủ MySQL đã được khởi chạy.
3.  Tìm đến file [LoginView.java](file:///c:/Users/ANH%20HOA/Documents/DUT/baitapjava/com/view/LoginView.java).
4.  Nhấp chuột phải và chọn **Run As** -> **Java Application** (hoặc chạy hàm `public static void main`).
5.  Giao diện đăng nhập hiện ra:
    *   Bạn có thể đăng ký tài khoản mới trực tiếp từ màn hình đăng ký (tài khoản đăng ký mới sẽ mặc định là quyền **Khách hàng**).
    *   Để truy cập với quyền **Admin**, bạn hãy cập nhật trực tiếp cột `role = 1` trong bảng `account` của MySQL cho tài khoản của bạn.
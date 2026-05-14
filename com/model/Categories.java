package com.model;

public class Categories {
    private int categoryId;
    private String categoryName;

    public Categories() {}

    public Categories(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    // Khi đưa object vào JComboBox, nó sẽ gọi hàm này để hiển thị chữ trên màn hình
    @Override
    public String toString() {
        return categoryName; 
    }
}
use java 
go 
create table account (
   useId int identity(1,1) primary key,
   useName nvarchar(255) not null,
   password nvarchar(255) not null,
   fullName varchar(255) not null ,
   role bit default 0

);
create table categories (
    categoryId int identity(1,1) primary key,
    categoryName nvarchar(255) not null,
);
create table products (
    productId int identity(1,1) primary key,
    productName nvarchar(255) not null,
    price decimal(18,2) not null,
    categoryId int ,
    foreign key (categoryId) references Categories(categoryId)
);
create table  orders(
     orderId int identity(1,1) primary key,
     orderDate datetime default getDate(),
     priceTotal decimal(18,2) not null ,
     useId int ,
     foreign key (useId) references account(useId)

);
create table detailOrder(
      id int identity(1,1) primary key,
      productId int,
      orderId int ,
      quantity int not null,
      priceAtSale decimal(18,2),
      foreign key (productId) references products(productId),
      foreign key (orderId) references Orders(orderId)
    );
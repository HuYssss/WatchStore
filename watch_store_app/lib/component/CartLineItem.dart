// Import các thư viện cần thiết
import 'dart:convert';
import 'package:flutter/material.dart';

// StatefulWidget để hiển thị một mục trong giỏ hàng
class CartLineItem extends StatelessWidget {
  final dynamic item;

  const CartLineItem({Key? key, required this.item}) : super(key: key);
  
  @override
  Widget build(BuildContext context) {
    // Lấy thông tin của mục trong giỏ hàng
    String id = item['id'];
    int quantity = item['quantity'];
    dynamic product = item['product'];
    String productId = product['id'].toString();
    List<dynamic> imgList = product['img'];
    List<String> imgLink = convertImg(imgList);
    String image = imgLink[0];
    String productName = product['productName'];
    double price = product['price']?.toDouble();

    // Hiển thị các thông tin của mục trong giỏ hàng
    return Row(
      children: [
        Image.network(image, width: 80, height: 80), // Hiển thị hình ảnh của sản phẩm
        const SizedBox(width: 10), // Khoảng cách giữa hình ảnh và nội dung
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(productName, style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)), // Hiển thị tên sản phẩm
              Text('Price: ' + price.toString() + " VND", style: TextStyle(fontSize: 14)), // Hiển thị giá của sản phẩm
              Text('Quantity: ' + quantity.toString()) // Hiển thị số lượng sản phẩm
            ],
          ),
        ),
      ],
    );
  }

  // Phương thức để chuyển đổi danh sách hình ảnh từ dạng dynamic sang String
  List<String> convertImg(List<dynamic> list) {
    List<String> stringImgList = [];
      for (dynamic img in list) {
        stringImgList.add(img.toString());
      }
    return stringImgList;
  }
}

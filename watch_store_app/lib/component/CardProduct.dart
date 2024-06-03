// Import các thư viện cần thiết
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:watch_store_app/page/ProductDetail.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

// StatelessWidget để hiển thị một card sản phẩm
class CardProduct extends StatelessWidget {
  final dynamic product;

  const CardProduct({Key? key, required this.product}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Lấy thông tin của sản phẩm từ dữ liệu đầu vào
    String productId = product['id'].toString();
    List<dynamic> imgList = product['img'] ?? '';
    String img = imgList[0].toString();
    String productName = product['productName'] ?? 'No Title';
    double price = product['price']?.toDouble() ?? 0.0;

    // Trả về một InkWell để bắt sự kiện khi nhấn vào card
    return InkWell(
      onTap: () {
        Navigator.push(
          context, 
          MaterialPageRoute(builder: (context) => ProductDetail(product: product)));
      },
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Card(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          clipBehavior: Clip.antiAliasWithSaveLayer,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Image.network(
                img,
                height: 160,
                width: double.infinity,
                fit: BoxFit.cover,
              ),
              Container(
                padding: const EdgeInsets.fromLTRB(15, 15, 15, 0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    // Hiển thị tên sản phẩm
                    Text(
                      productName,
                      style: TextStyle(
                        fontSize: 20,
                        color: Colors.grey[800],
                      ),
                    ),
                    Container(height: 10), // Khoảng cách giữa các phần tử
                    // Hiển thị giá sản phẩm
                    Text(
                      "Price: " + price.toString() + " VND",
                      style: TextStyle(
                        fontSize: 15,
                        color: Colors.grey[700],
                      ),
                    ),
                    Container(height: 10),
                    // Dòng chứa các nút View Detail và Add to cart
                    Row(
                      children: [
                        Expanded(
                          child: TextButton(
                            onPressed: () {
                              Navigator.push(
                                context, 
                                MaterialPageRoute(builder: (context) => ProductDetail(product: product))
                              );
                            },
                            child: Text("View Detail"),
                          ),
                        ),
                        Expanded(
                          child: TextButton(
                            onPressed: () async {
                              http.Response response = await addProductToCart(productId);
                              if(response.statusCode == 200)
                              {
                                print("Add product to cart success");
                                showSuccessDialog(context);
                              }
                            },
                            child: Text("Add to cart"),
                          ),
                        ),
                      ],
                    )
                  ],
                ),
              ),
              Container(height: 5), // Khoảng cách giữa card sản phẩm
            ],
          ),
        ),
      ),
    ); 
  }

  // Phương thức để thêm sản phẩm vào giỏ hàng
  Future<http.Response> addProductToCart(String productId) async {
    final String addProductToCartUrl = "http://localhost:8080/cart/addProductToCart";
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    final String? token = prefs.getString('token');
    try {
      final response = await http.post(
        Uri.parse(addProductToCartUrl),
        headers: <String, String>{
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ${token!}',
        },
        body: jsonEncode(<String, dynamic> {
          "product": productId,
          "quantity": 1,
        }),
      );
      return response;
    } catch (error) {
      // Xử lý lỗi mạng
      throw Exception('Failed to add product to cart: $error');
    }
  }

  // Phương thức để hiển thị hộp thoại thông báo khi sản phẩm được thêm vào giỏ hàng thành công
  void showSuccessDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Success"),
          content: Text("Product added to cart successfully!"),
          actions: <Widget>[
            TextButton(
              child: Text("OK"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }
}

// Import các thư viện cần thiết
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:watch_store_app/component/ProductGridView.dart';
import 'package:watch_store_app/page/ProductDetail.dart';
import 'package:http/http.dart' as http;

// StatelessWidget để hiển thị một card danh mục sản phẩm
class CardCategory extends StatelessWidget {
  final dynamic category;

  const CardCategory({Key? key, required this.category}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Lấy thông tin của danh mục từ dữ liệu đầu vào
    String id = category['id'];
    String categoryName = category['categoryName'] ?? 'No Title';
    List<dynamic> listProduct = category['productId'] ?? [];
    List<String> idStr = convertProductId(listProduct);

    // Trả về một InkWell để bắt sự kiện khi nhấn vào card
    return InkWell(
      onTap: () async {
        // Lấy danh sách sản phẩm dựa trên danh mục được chọn
        List<dynamic> products = await _fetchProductByCategory(id) as List;
        // Chuyển hướng đến trang hiển thị danh sách sản phẩm
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => ProductGridView(products: products)),
        );
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
              Container(
                padding: const EdgeInsets.fromLTRB(15, 15, 15, 0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    // Hiển thị tên danh mục sản phẩm
                    Row(
                      children: [
                        Expanded(
                          child: Text(
                            categoryName,
                            style: TextStyle(
                              fontSize: 20,
                              color: Colors.grey[800],
                            ),
                            textAlign: TextAlign.left, // Canh lề trái cho categoryName
                          ),
                        ),
                        Spacer(), // Thêm khoảng trống giữa các văn bản
                        Text(
                          idStr.length.toString(),
                          style: TextStyle(
                            fontSize: 15,
                            color: Colors.grey[700],
                          ),
                          textAlign: TextAlign.right, // Canh lề phải cho idStr.length.toString()
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              // Thêm một khoảng trống nhỏ giữa card và widget tiếp theo
              Container(height: 5),
            ],
          ),
        ),
      ),
    ); 
  }

  // Phương thức để chuyển đổi danh sách productId từ dạng dynamic sang String
  List<String> convertProductId(List<dynamic> listId) {
    List<String> result = [];
    for (int i = 0; i < listId.length; i++) {
      dynamic d = listId[i];
      String id = d.toString();
      result.add(id);
    }
    return result;
  }

  // Phương thức để lấy danh sách sản phẩm theo danh mục từ server
  Future<List<dynamic>> _fetchProductByCategory(String categoryId) async {
    // URL thực API của bạn
    String apiUrl = 'http://localhost:8080/category/' + categoryId;

    try {
      final response = await http.get(
        Uri.parse(apiUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseData = jsonDecode(response.body);
        List<dynamic> result = responseData['data']['product'];
        return result;
      } else {
        print('Error fetching products: ${response.statusCode}');
        return [];
      }
    } catch (error) {
      print('Error fetching products: $error');
      return [];
    } 
  }
}

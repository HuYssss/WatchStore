import 'dart:convert'; // Thư viện để xử lý JSON

import 'package:flutter/material.dart'; // Thư viện chính của Flutter cho giao diện người dùng
import 'package:http/http.dart' as http; // Thư viện để gửi yêu cầu HTTP
import 'package:watch_store_app/component/ProductGridView.dart'; // Import component ProductGridView

// Khởi tạo một StatefulWidget có tên là Home
class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  State<Home> createState() => _HomeState();
}

// Khởi tạo state của Home
class _HomeState extends State<Home> {
  List<dynamic> _products = []; // Biến lưu danh sách sản phẩm
  bool _isLoading = true; // Biến lưu trạng thái đang tải dữ liệu

  @override
  void initState() {
    super.initState();
    _fetchProducts(); // Gọi hàm _fetchProducts khi khởi tạo
  }

  // Hàm lấy danh sách sản phẩm từ API
  Future<void> _fetchProducts() async {
    // Thay thế bằng URL endpoint API thực tế của bạn
    const String apiUrl = 'http://localhost:8080/product/getAll';

    try {
      // Gửi yêu cầu GET đến API
      final response = await http.get(
        Uri.parse(apiUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        // Nếu thành công, giải mã JSON và cập nhật danh sách sản phẩm
        final Map<String, dynamic> responseData = jsonDecode(response.body);
        _products = responseData['data'] ?? [];
      } else {
        print('Error fetching products: ${response.statusCode}');
      }
    } catch (error) {
      print('Error fetching products: $error');
    } finally {
      // Cập nhật trạng thái đang tải dữ liệu
      setState(() {
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ProductGridView(products: _products), // Hiển thị danh sách sản phẩm trong ProductGridView
    );
  }
}

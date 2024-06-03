import 'dart:convert'; // Thư viện để xử lý JSON

import 'package:flutter/material.dart'; // Thư viện chính của Flutter cho giao diện người dùng
import 'package:http/http.dart' as http; // Thư viện để gửi yêu cầu HTTP
import 'package:watch_store_app/component/CardCategory.dart'; // Import component CardCategory

// Khởi tạo một StatefulWidget có tên là CategoryTab
class CategoryTab extends StatefulWidget {
  const CategoryTab({Key? key}) : super(key: key);

  @override
  State<CategoryTab> createState() => _CategoryTabState();
}

// Khởi tạo state của CategoryTab
class _CategoryTabState extends State<CategoryTab> {
  List<dynamic> _categories = []; // Biến lưu danh sách danh mục
  bool _isLoading = true; // Biến lưu trạng thái đang tải dữ liệu

  @override
  void initState() {
    super.initState();
    _fetchCategories(); // Gọi hàm _fetchCategories khi khởi tạo
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Category"), // Thanh tiêu đề của Scaffold
      ),
      body: ListView.builder(
        itemCount: _categories.length, // Số lượng danh mục
        itemBuilder: (context, index) {
          final category = _categories[index]; // Lấy danh mục tại vị trí index
          return CardCategory(category: category); // Trả về CardCategory để hiển thị danh mục
        },
      ),
    );
  }

  // Hàm lấy danh sách danh mục từ API
  Future<void> _fetchCategories() async {
    const String apiUrl = 'http://localhost:8080/category'; // Địa chỉ API

    try {
      // Gửi yêu cầu GET đến API
      final response = await http.get(
        Uri.parse(apiUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        // Nếu thành công, giải mã JSON và cập nhật danh sách danh mục
        final Map<String, dynamic> responseData = jsonDecode(response.body);
        _categories = responseData['data'] ?? [];
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
}

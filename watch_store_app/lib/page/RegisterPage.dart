import 'dart:convert'; // Thư viện để xử lý JSON

import 'package:flutter/material.dart'; // Thư viện chính của Flutter cho giao diện người dùng
import 'package:http/http.dart' as http; // Thư viện để gửi yêu cầu HTTP
import 'package:watch_store_app/tab/Home.dart'; // Import trang chủ của ứng dụng
import 'package:watch_store_app/page/LoginPage.dart'; // Import trang đăng nhập

// Khởi tạo một StatefulWidget có tên là RegisterPage
class RegisterPage extends StatefulWidget {
  const RegisterPage({Key? key}) : super(key: key);

  @override
  State<RegisterPage> createState() => _RegisterPageState();
}

// Khởi tạo state của RegisterPage
class _RegisterPageState extends State<RegisterPage> {
  // Khởi tạo các TextEditingController để xử lý đầu vào từ người dùng
  final _emailController = TextEditingController();
  final _phoneController = TextEditingController();
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();

  String _errorMessage = ''; // Biến lưu thông báo lỗi cho các trường nhập liệu
  String _errorPassMessage = ''; // Biến lưu thông báo lỗi cho mật khẩu và xác nhận mật khẩu

  dynamic response;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Register'), // Tiêu đề của AppBar
      ),
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 15, vertical: 10), // Khoảng cách lề cho Container
        child: Column(
          children: <Widget>[
            TextField(
              controller: _emailController,
              decoration: InputDecoration(
                labelText: 'Email', // Nhãn của trường nhập liệu
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorMessage.isEmpty ? null : _errorMessage, // Hiển thị lỗi nếu có
              ),
            ),
            const SizedBox(height: 20.0), // Khoảng cách giữa các trường nhập liệu
            TextField(
              controller: _phoneController,
              decoration: InputDecoration(
                labelText: 'Number Phone', // Nhãn của trường nhập liệu
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorMessage.isEmpty ? null : _errorMessage, // Hiển thị lỗi nếu có
              ),
            ),
            const SizedBox(height: 20.0),
            TextField(
              controller: _usernameController,
              decoration: InputDecoration(
                labelText: 'Username', // Nhãn của trường nhập liệu
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorMessage.isEmpty ? null : _errorMessage, // Hiển thị lỗi nếu có
              ),
            ),
            const SizedBox(height: 20.0),
            TextField(
              controller: _passwordController,
              decoration: InputDecoration(
                labelText: 'Password', // Nhãn của trường nhập liệu
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorPassMessage.isEmpty ? null : _errorPassMessage, // Hiển thị lỗi nếu có
              ),
            ),
            const SizedBox(height: 20.0),
            TextField(
              controller: _confirmPasswordController,
              decoration: InputDecoration(
                labelText: 'Confirm Password', // Nhãn của trường nhập liệu
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorPassMessage.isEmpty ? null : _errorPassMessage, // Hiển thị lỗi nếu có
              ),
            ),
            const SizedBox(height: 20.0),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                minimumSize: const Size(350, 75), // Kích thước tối thiểu của nút
              ),
              onPressed: () async {
                // Kiểm tra xem các trường nhập liệu có trống không
                if (_usernameController.text.isEmpty ||
                    _passwordController.text.isEmpty ||
                    _phoneController.text.isEmpty ||
                    _passwordController.text.isEmpty ||
                    _confirmPasswordController.text.isEmpty) {
                  setState(() {
                    _errorMessage = 'Please fill in all required fields.'; // Hiển thị lỗi nếu có trường nhập liệu trống
                    _errorPassMessage = 'Please fill in all required fields.';
                  });
                } else if (_passwordController.text != _confirmPasswordController.text) {
                  setState(() {
                    _errorPassMessage = "Password and Confirm Password aren't match."; // Hiển thị lỗi nếu mật khẩu và xác nhận mật khẩu không khớp
                  });
                } else {
                  // Gọi hàm register để gửi yêu cầu đăng ký tới API
                  final dynamic response = await register(_emailController.text, _phoneController.text, _usernameController.text, _passwordController.text);
                  if (response.statusCode == 200) {
                    Navigator.pop(context); // Điều hướng quay lại màn hình trước nếu đăng ký thành công
                  }
                }
              },
              child: Text('Register', style: TextStyle(fontSize: 20)), // Nhãn của nút bấm
            ),
          ],
        ),
      ),
    );
  }

  // Hàm gửi yêu cầu đăng ký tới API
  Future<dynamic> register(String email, String phone, String username, String password) async {
    final String registerUrl = "http://localhost:8080/auth/register";
    try {
      final response = await http.post(
        Uri.parse(registerUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8', // Định dạng nội dung của yêu cầu
        },
        body: jsonEncode(<String, dynamic>{
          "email": email, // Địa chỉ email của người dùng
          "phone": phone, // Số điện thoại của người dùng
          "username": username, // Tên đăng nhập của người dùng
          "password": password, // Mật khẩu của người dùng
        }),
      );
      return response; // Trả về phản hồi từ API
    } catch (e) {
      throw Exception(e); // Ném lỗi nếu yêu cầu thất bại
    }
  }
}

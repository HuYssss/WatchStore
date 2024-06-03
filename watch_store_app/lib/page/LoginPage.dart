// Thư viện để giải mã JSON
import 'dart:convert';

// Thư viện chính của Flutter để xây dựng giao diện
import 'package:flutter/material.dart';

// Thư viện để thực hiện các request HTTP
import 'package:http/http.dart' as http;

// Thư viện để lưu trữ dữ liệu cục bộ (shared preferences)
import 'package:shared_preferences/shared_preferences.dart';

// Import các màn hình khác của ứng dụng
import 'package:watch_store_app/page/ForgotPassword.dart';
import 'package:watch_store_app/tab/Home.dart';
import 'package:watch_store_app/page/DashBoard.dart';
import 'package:watch_store_app/page/RegisterPage.dart';

// Khởi tạo StatefulWidget cho trang Login
class LoginPage extends StatefulWidget {
  const LoginPage({Key? key}) : super(key: key);

  @override
  State<LoginPage> createState() => _LoginPageState();
}

// State của trang Login
class _LoginPageState extends State<LoginPage> {
  // Khai báo TextEditingController cho username và password
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();

  // Biến lưu trữ message lỗi
  String _errorMessage = '';

  // Xây dựng giao diện của trang Login
  @override
  Widget build(BuildContext context) {
    return Center(
      // Hiển thị nội dung Login ở giữa màn hình
      child: SingleChildScrollView(
        // Cho phép cuộn nội dung nếu vượt quá kích thước màn hình
        padding: const EdgeInsets.all(20.0),
        // Khoảng trống xung quanh các nội dung con
        child: Column(
          // Các nội dung con xếp theo chiều dọc
          children: <Widget>[
            // Hiển thị logo của cửa hàng đồng hồ
            Container(
              padding: const EdgeInsets.symmetric(vertical: 10.0),
              child: Image.asset(
                'assets/image/WatchesStoreLogo.png',
                height: 300.0,
              ),
            ),
            // TextField để nhập username
            TextField(
              controller: _usernameController,
              decoration: InputDecoration(
                labelText: 'Username',
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorMessage.isEmpty ? null : _errorMessage,
              ),
            ),
            const SizedBox(height: 20.0),
            // TextField để nhập password (ẩn ký tự)
            TextField(
              controller: _passwordController,
              decoration: InputDecoration(
                labelText: 'Password',
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorMessage.isEmpty ? null : _errorMessage,
              ),
              obscureText: true,
            ),
            const SizedBox(height: 20.0),
            // Nút Login
            SizedBox(
              width: 200,
              height: 25,
              child: ElevatedButton(
                onPressed: () async {
                  // Kiểm tra username và password có trống không
                  if (_usernameController.text.isEmpty ||
                      _passwordController.text.isEmpty) {
                    setState(() {
                      _errorMessage =
                          'Username and password are required.';
                    });
                  } else {
                    // Xóa message lỗi cũ
                    _errorMessage = '';
                    // Thực hiện đăng nhập (gọi hàm login)
                    final response = await login(
                        _usernameController.text, _passwordController.text);
                    if (response.statusCode == 200) {
                      // Giải mã JSON trả về từ server
                      final Map<String, dynamic> jsonMap =
                          jsonDecode(response.body);
                      // Lấy token từ JSON
                      final String token = jsonMap['data']['token'];
                      // Lưu token vào shared preferences
                      final SharedPreferences prefs =
                          await SharedPreferences.getInstance();
                      await prefs.setString('token', token);

                      // Chuyển hướng sang trang DashBoard và xóa trang Login khỏi stack navigation
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(builder: (context) => DashBoard()),
                      );
                    } else {
                      // Hiển thị message lỗi đăng nhập thất bại
                      setState(() {
                        _errorMessage =
                            'Login failed. Invalid username or password.';
                      });
                    }
                  }
                },
                child: const Text('Login'),
              ),
            ),
            const SizedBox(height: 20.0),
            // Nút Register
            SizedBox(
              width: 200,
              height: 25,
              child: ElevatedButton(
                child: const Text('Register'),
                onPressed: () {
                  // Chuyển hướng sang trang Register
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const RegisterPage()),
                  );
                },
              ),
            ),
            const SizedBox(height: 20.0),
            // Nút Forgot Password
            SizedBox(
              width: 200,
              height: 25,
              child: ElevatedButton(
                onPressed: () {
                  // Chuyển hướng sang trang Forgot Password
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => ForgotPassword()),
                  );
                },
                child: const Text("Forgot Password"),
              ),
            ),
          ],
        ),
      ),
    );
  }

  // Hàm thực hiện đăng nhập
  Future<http.Response> login(String username, String password) async {
    // URL API đăng nhập
    const String loginUrl = "http://localhost:8080/auth/login";

    try {
      // Gửi request POST đến URL API đăng nhập
      final response = await http.post(
        Uri.parse(loginUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, dynamic>{
          "username": username,
          "password": password,
        }),
      );

      // Trả về response
      return response;
    } catch (error) {
      // Xử lý lỗi kết nối mạng
      throw Exception('Failed to login: $error');
    }
  }
}

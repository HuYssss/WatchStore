import 'dart:convert'; // Thư viện để xử lý JSON

import 'package:flutter/material.dart'; // Thư viện chính của Flutter cho giao diện người dùng
import 'package:http/http.dart' as http; // Thư viện để gửi yêu cầu HTTP
import 'package:watch_store_app/page/ResetPassword.dart'; // Import trang đặt lại mật khẩu

// Khởi tạo một StatefulWidget có tên là ForgotPassword
class ForgotPassword extends StatefulWidget {
  @override
  _ForgotPasswordState createState() => _ForgotPasswordState();
}

// Khởi tạo state của ForgotPassword
class _ForgotPasswordState extends State<ForgotPassword> {
  TextEditingController _emailController = new TextEditingController(); // Bộ điều khiển để lấy giá trị email từ TextField
  String _errorMessage = ''; // Thông báo lỗi để hiển thị dưới TextField
  bool isChecked = false; // Biến để kiểm tra xem yêu cầu đặt lại mật khẩu có thành công hay không

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Forgot Password'), // Tiêu đề của AppBar
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(10.0),
          child: Column(
            children: [
              Container(
                padding: const EdgeInsets.symmetric(vertical: 10.0),
                child: Image.asset(
                  'assets/image/WatchesStoreLogo.png', // Hiển thị logo của cửa hàng
                  height: 300.0,
                ),
              ),
              TextField(
                controller: _emailController, // Gán bộ điều khiển cho TextField
                decoration: InputDecoration(
                  labelText: 'Email', // Nhãn của TextField
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(10.0),
                  ),
                  errorText: _errorMessage.isEmpty ? null : _errorMessage, // Hiển thị lỗi nếu có
                ),
                obscureText: false, // Không ẩn nội dung của TextField
              ),
              const SizedBox(height: 20.0), // Khoảng cách giữa TextField và nút
              SizedBox(
                width: 200,
                height: 25,
                child: ElevatedButton(
                  onPressed: () async {
                    if (_emailController.text.isEmpty) {
                      setState(() {
                        _errorMessage = "Email is required."; // Hiển thị lỗi nếu email trống
                      });
                    } else {
                      await _getResetTokenReset(); // Gọi hàm lấy mã đặt lại mật khẩu
                      // ignore: use_build_context_synchronously
                      if (isChecked) {
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => ResetPassword()), // Điều hướng đến trang đặt lại mật khẩu
                        );
                      }
                    }
                  },
                  child: const Text("Get reset password"), // Nội dung của nút
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  // Hàm gửi yêu cầu lấy mã đặt lại mật khẩu tới API
  Future<void> _getResetTokenReset() async {
    const String loginUrl = "http://localhost:8080/auth/forgotPassword"; // Đường dẫn API
    try {
      final response = await http.post(
        Uri.parse(loginUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, dynamic>{
          "email": _emailController.text // Dữ liệu gửi đi là email người dùng nhập
        }),
      );
      if (response.statusCode == 200) {
        setState(() {
          isChecked = true; // Đặt isChecked thành true nếu yêu cầu thành công
        });
      }
      else
      {
        setState(() {
          _errorMessage = "Email not found !!!";
        });
      }
    } catch (error) {
      // Xử lý lỗi mạng
      throw Exception('Failed to get reset password: $error');
      
    }
  }
}

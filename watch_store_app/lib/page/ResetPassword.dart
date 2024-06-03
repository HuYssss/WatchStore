import 'dart:convert'; // Thư viện để xử lý JSON

import 'package:flutter/material.dart'; // Thư viện chính của Flutter cho giao diện người dùng
import 'package:http/http.dart' as http; // Thư viện để gửi yêu cầu HTTP
import 'package:watch_store_app/main.dart'; // Import trang chính của ứng dụng
import 'package:watch_store_app/page/LoginPage.dart'; // Import trang đăng nhập

// Khởi tạo một StatefulWidget có tên là ResetPassword
class ResetPassword extends StatefulWidget {
  @override
  _ResetPasswordState createState() => _ResetPasswordState();
}

// Khởi tạo state của ResetPassword
class _ResetPasswordState extends State<ResetPassword> {
  // Khởi tạo các TextEditingController để xử lý đầu vào từ người dùng
  TextEditingController _tokenController = new TextEditingController();
  TextEditingController _newPasswordController = new TextEditingController();
  TextEditingController _confirmNewPassController = new TextEditingController();

  String _errorMessage = ''; // Biến lưu thông báo lỗi cho token
  String _passErrorMessage = ''; // Biến lưu thông báo lỗi cho mật khẩu
  bool isChecked = false; // Biến lưu trạng thái kiểm tra reset password thành công

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(10.0),
          child: Column(
            children: [
              Container(
                padding: const EdgeInsets.symmetric(vertical: 10.0),
                child: Image.asset(
                  'assets/image/WatchesStoreLogo.png', // Đường dẫn đến hình ảnh logo
                  height: 300.0,
                ),
              ),
              TextField(
                controller: _tokenController,
                decoration: InputDecoration(
                  labelText: 'Token', // Nhãn của trường nhập liệu
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(10.0),
                  ),
                  errorText: _errorMessage.isEmpty ? null : _errorMessage, // Hiển thị lỗi nếu có
                ),
                obscureText: false,
              ),
              SizedBox(height: 10.0),
              TextField(
                controller: _newPasswordController,
                decoration: InputDecoration(
                  labelText: 'New Password', // Nhãn của trường nhập liệu
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(10.0),
                  ),
                  errorText: _passErrorMessage.isEmpty ? null : _errorMessage, // Hiển thị lỗi nếu có
                ),
                obscureText: false,
              ),
              SizedBox(height: 10.0),
              TextField(
                controller: _confirmNewPassController,
                decoration: InputDecoration(
                  labelText: 'Confirm New Password', // Nhãn của trường nhập liệu
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(10.0),
                  ),
                  errorText: _passErrorMessage.isEmpty ? null : _errorMessage, // Hiển thị lỗi nếu có
                ),
                obscureText: false,
              ),
              SizedBox(height: 10.0),
              SizedBox(
                width: 200,
                height: 25,
                child: ElevatedButton(
                  onPressed: () async {
                    if (_tokenController.text.isEmpty || _newPasswordController.text.isEmpty || _confirmNewPassController.text.isEmpty) {
                      setState(() {
                        _errorMessage = 'This field is required'; // Hiển thị lỗi nếu trường nhập liệu bị trống
                        _passErrorMessage = 'This field is required'; // Hiển thị lỗi nếu trường nhập liệu bị trống
                      });
                    } else if (_newPasswordController.text != _confirmNewPassController.text) {
                      setState(() {
                        _passErrorMessage = 'New Pass and Password does not match.'; // Hiển thị lỗi nếu mật khẩu không khớp
                      });
                    } else {
                      await _resetPassword(); // Gọi hàm reset password
                      if (isChecked) {
                        // ignore: use_build_context_synchronously
                        Navigator.pushReplacement(
                          context,
                          MaterialPageRoute(builder: (context) => MyApp()), // Điều hướng đến trang chính của ứng dụng sau khi reset password thành công
                        );
                      }
                    }
                  },
                  child: const Text("Reset Password"), // Nhãn của nút bấm
                ),
              )
            ],
          ),
        ),
      ),
    );
  }

  // Hàm gửi yêu cầu reset password tới API
  Future<void> _resetPassword() async {
    const String loginUrl = "http://localhost:8080/auth/resetPassword";
    try {
      final response = await http.post(
        Uri.parse(loginUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, dynamic>{
          "token": _tokenController.text, // Token từ người dùng
          "newPass": _newPasswordController.text, // Mật khẩu mới từ người dùng
        }),
      );
      if (response.statusCode == 200) {
        setState(() {
          isChecked = true; // Đặt trạng thái kiểm tra thành công
        });
      }
    } catch (error) {
      throw Exception('Failed to get reset password: $error'); // Ném lỗi nếu yêu cầu thất bại
    }
  }
}

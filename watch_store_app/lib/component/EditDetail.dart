// Import các thư viện và package cần thiết
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

// StatefulWidget để chỉnh sửa chi tiết người dùng
class EditDetail extends StatefulWidget {
  final dynamic user;

  const EditDetail({Key? key, required this.user}) : super(key: key);

  @override
  _EditDetailState createState() => _EditDetailState();
}

// State của EditDetail StatefulWidget
class _EditDetailState extends State<EditDetail> {
  // Khai báo các biến controller và errorMessage
  TextEditingController _phoneController = new TextEditingController();
  TextEditingController _fNameController = new TextEditingController();
  TextEditingController _lNameController = new TextEditingController();
  TextEditingController _avartaImgController = new TextEditingController();
  String _errorMessage = '';

  // Phương thức initState để thiết lập giá trị ban đầu cho các controller khi widget được khởi tạo
  @override
  void initState() {
    super.initState();
    setState(() {
      _phoneController.text = widget.user['phone'].toString();
      _fNameController.text = widget.user['firstname'].toString();
      _lNameController.text = widget.user['lastname'].toString();
      _avartaImgController.text = widget.user['avatarImg'].toString();
    });
  }

  // Phương thức build để xây dựng giao diện chỉnh sửa chi tiết người dùng
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Edit Details'), // Tiêu đề của trang
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.all(20.0),
        child: Column(
          children: [
            // TextField để chỉnh sửa số điện thoại
            TextField(
              controller: _phoneController,
              decoration: InputDecoration(
                labelText: 'Phone', // Nhãn cho TextField
                border: OutlineInputBorder( // Hiệu ứng viền
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorMessage.isEmpty ? null : _errorMessage, // Hiển thị thông báo lỗi nếu có
              ),
              obscureText: false, // Ẩn văn bản nhập
            ),
            SizedBox(height: 10.0),
            // TextField để chỉnh sửa tên
            TextField(
              controller: _fNameController,
              decoration: InputDecoration(
                labelText: 'First Name',
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorMessage.isEmpty ? null : _errorMessage,
              ),
              obscureText: false,
            ),
            SizedBox(height: 10.0),
            // TextField để chỉnh sửa họ
            TextField(
              controller: _lNameController,
              decoration: InputDecoration(
                labelText: 'Last Name',
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorMessage.isEmpty ? null : _errorMessage,
              ),
              obscureText: false,
            ),
            SizedBox(height: 10.0),
            // TextField để chỉnh sửa URL hình đại diện
            TextField(
              controller: _avartaImgController,
              decoration: InputDecoration(
                labelText: 'Avatar Image',
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10.0),
                ),
                errorText: _errorMessage.isEmpty ? null : _errorMessage,
              ),
              obscureText: false,
            ),
            SizedBox(height: 10.0),
            // Button để lưu các thay đổi
            ElevatedButton(
              onPressed: () async {
                // Kiểm tra và hiển thị thông báo lỗi nếu có trường trống
                if (_phoneController == null || _fNameController == null || _lNameController == null || _avartaImgController == null) {
                  setState(() {
                    _errorMessage = "This field is required.";
                  });
                }
                else {
                  // Gửi yêu cầu HTTP POST để cập nhật chi tiết người dùng
                  String apiUrl = 'http://localhost:8080/user/editDetail';
                  final SharedPreferences prefs = await SharedPreferences.getInstance();
                  final String? token = prefs.getString('token');

                  try {
                    final response = await http.post(
                      Uri.parse(apiUrl),
                      headers: <String, String>{
                        'Content-Type': 'application/json; charset=UTF-8',
                        'Authorization': 'Bearer ${token!}',
                      },
                      body: jsonEncode(<String, dynamic>{
                        "phone": _phoneController.text,
                        "firstname": _fNameController.text,
                        "lastname": _lNameController.text,
                        "avatarImg": _avartaImgController.text
                      }),
                    );

                    // Nếu cập nhật thành công, quay lại màn hình trước đó
                    if (response.statusCode == 200) {
                      Navigator.pop(context);
                    } else {
                      print('Error update userDetail: ${response.statusCode}'); // Log lỗi nếu cập nhật không thành công
                    }
                  } catch (error) {
                    print('Error update userDetail: $error'); // Log lỗi nếu có lỗi trong quá trình gửi yêu cầu
                  }
                }            
              },
              child: Text('Save Changes'), // Text của Button
            ),
          ],
        ),
      ),
    );
  }
}

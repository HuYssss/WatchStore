import 'package:flutter/material.dart';
import 'package:watch_store_app/tab/Home.dart';
import 'package:watch_store_app/page/LoginPage.dart';

void main() {
  // Điểm khởi chạy của ứng dụng
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // Widget chính của ứng dụng
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      // Cấu hình ứng dụng
      home: Scaffold(
        // Hiển thị nội dung chính của ứng dụng
        body: LoginPage(),
      ),
    );
  }
}

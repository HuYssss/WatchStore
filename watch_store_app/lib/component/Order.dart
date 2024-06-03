// Import các thư viện và package cần thiết
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:watch_store_app/component/OrderLineItem.dart';

// Định nghĩa một StatefulWidget để hiển thị danh sách đơn hàng
class Order extends StatefulWidget {
  @override
  _OrderState createState() => _OrderState();
}

// Định nghĩa trạng thái của StatefulWidget Order
class _OrderState extends State<Order> {
  // Khai báo biến orders để lưu danh sách các đơn hàng và biến _isLoading để kiểm soát việc hiển thị vòng tròn tiến trình
  List<dynamic> orders = [];
  bool _isLoading = true;

  // Phương thức initState được gọi khi widget được khởi tạo, được sử dụng để tải danh sách đơn hàng
  @override
  void initState() {
    super.initState();
    _fetchOrder();
  }

  // Phương thức build để xây dựng giao diện của widget Order
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Order"),
      ),
      // Hiển thị danh sách đơn hàng bằng ListView.builder
      body: ListView.builder(
        itemCount: orders.length,
        itemBuilder: (context, index) {
          final order = orders[index];
          return OrderLineItem(order: order); // Mỗi đơn hàng được hiển thị bằng OrderLineItem
        }
      ),
    );
  }

  // Phương thức để gửi yêu cầu HTTP GET để lấy danh sách đơn hàng từ API
  Future<void> _fetchOrder() async {
    const String apiUrl = 'http://localhost:8080/order';
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    final String? token = prefs.getString('token');
    
    try {
      // Gửi yêu cầu HTTP GET với token được đính kèm trong header
      final response = await http.get(
        Uri.parse(apiUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer ${token!}',
        },
      );

      // Nếu yêu cầu thành công (status code 200), cập nhật danh sách đơn hàng và đặt _isLoading thành false
      if (response.statusCode == 200) {
        final Map<String, dynamic> responseData = jsonDecode(response.body);
        setState(() {
          orders = responseData['data'] ?? [];
        });
      } else {
        print('Error fetching orders: ${response.statusCode}'); // Log lỗi nếu yêu cầu không thành công
      }
    } catch (error) {
      print('Error fetching orders: $error'); // Log lỗi nếu xảy ra lỗi trong quá trình gửi yêu cầu
    } finally {
      setState(() {
        _isLoading = false; // Đặt _isLoading thành false để dừng hiển thị vòng tròn tiến trình
      });
    }
  }
}

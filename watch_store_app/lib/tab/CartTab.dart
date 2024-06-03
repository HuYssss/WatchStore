import 'dart:convert'; // Thư viện để xử lý JSON

import 'package:flutter/material.dart'; // Thư viện chính của Flutter cho giao diện người dùng
import 'package:http/http.dart' as http; // Thư viện để gửi yêu cầu HTTP
import 'package:shared_preferences/shared_preferences.dart'; // Thư viện để sử dụng SharedPreferences
import 'package:watch_store_app/component/CartLineItem.dart'; // Import component CartLineItem

// Khởi tạo một StatefulWidget có tên là CartTab
class CartTab extends StatefulWidget {
  const CartTab({Key? key}) : super(key: key);

  @override
  State<CartTab> createState() => _CartTabState();
}

// Khởi tạo state của CartTab
class _CartTabState extends State<CartTab> {
  List<dynamic> _items = []; // Biến lưu danh sách sản phẩm trong giỏ hàng
  List<String> isChoose = []; // Biến lưu danh sách sản phẩm đã chọn
  List<bool> isSelected = []; // Biến lưu trạng thái đã chọn của từng sản phẩm
  List<bool> isAddressSelected = []; // Biến lưu trạng thái đã chọn của địa chỉ
  List<dynamic> addresses = []; // Biến lưu danh sách địa chỉ
  String addressId = ''; // Biến lưu ID địa chỉ đã chọn
  int length = 0; // Biến lưu chiều dài danh sách sản phẩm

  @override
  void initState() {
    super.initState();
    _fectchCartUser(); // Gọi hàm _fectchCartUser khi khởi tạo
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Cart"), // Thanh tiêu đề của Scaffold
      ),
      body: ListView.builder(
        itemCount: length, // Số lượng sản phẩm trong giỏ hàng
        itemBuilder: (context, index) {
          final item = _items[index]; // Lấy sản phẩm tại vị trí index
          return Padding(
            padding: const EdgeInsets.fromLTRB(10, 10, 10, 10),
            child: Container(
              decoration: BoxDecoration(
                color: isSelected[index] ? Colors.grey[200] : Colors.white, // Màu nền thay đổi khi chọn sản phẩm
                borderRadius: BorderRadius.circular(20),
              ),
              child: InkWell(
                onTap: () {
                  setState(() {
                    if (isSelected[index]) {
                      isSelected[index] = false;
                      isChoose.remove(item['id']); // Bỏ chọn sản phẩm
                    } else {
                      isSelected[index] = true;
                      isChoose.add(item['id']); // Chọn sản phẩm
                    }
                  });
                },
                child: CartLineItem(item: item), // Hiển thị sản phẩm
              ),
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          if (isChoose.isEmpty) {
            showErrorDialog(context); // Hiển thị lỗi nếu không chọn sản phẩm nào
          } else {
            chooseAddressDialog(context); // Hiển thị hộp thoại chọn địa chỉ
          }
        },
        child: Icon(Icons.shopping_cart_checkout),
        backgroundColor: Colors.blue,
        foregroundColor: Colors.white,
      ),
    );
  }

  // Hàm lấy danh sách sản phẩm trong giỏ hàng từ API
  Future<void> _fectchCartUser() async {
    const String apiUrl = 'http://localhost:8080/cart';
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    final String? token = prefs.getString('token');

    try {
      final response = await http.get(
        Uri.parse(apiUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer ${token!}',
        },
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseData = jsonDecode(response.body);
        _items = responseData['data'] ?? [];
        setState(() {
          _items = responseData['data'] ?? [];
          length = _items.length;
          isSelected = List.filled(length, false); // Khởi tạo danh sách isSelected với giá trị ban đầu là false
        });
      } else {
        print('Error fetching products: ${response.statusCode}');
      }
    } catch (error) {
      print('Error fetching products: $error');
    }
  }

  // Hàm lấy danh sách địa chỉ từ API
  Future<void> _fetchAddresses() async {
    const String apiUrl = 'http://localhost:8080/address';
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    final String? token = prefs.getString('token');

    try {
      final response = await http.get(
        Uri.parse(apiUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer ${token!}',
        },
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseData = jsonDecode(response.body);
        setState(() {
          addresses = responseData['data'];
          isAddressSelected = List.filled(addresses.length, false); // Khởi tạo danh sách isAddressSelected với giá trị ban đầu là false
          isAddressSelected[0] = true; // Địa chỉ đầu tiên được chọn mặc định
          dynamic addressItem = addresses[0];
          addressId = addressItem['id']; // Lưu ID của địa chỉ đầu tiên
        });
      } else {
        print('Error fetching addresses: ${response.statusCode}');
      }
    } catch (error) {
      print('Error fetching addresses: $error');
    }
  }

  // Hộp thoại chọn địa chỉ
  void chooseAddressDialog(BuildContext context) async {
    await _fetchAddresses();
    // ignore: use_build_context_synchronously
    showDialog(
      context: context,
      builder: (BuildContext buildContext) {
        return Dialog(
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              mainAxisSize: MainAxisSize.min, // Bao bọc nội dung theo chiều dọc
              children: [
                const Text(
                  'Choose your addresses',
                  style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                ),
                const Divider(thickness: 1),
                ListView.builder(
                  shrinkWrap: true,
                  itemCount: addresses.length, // Số lượng địa chỉ
                  itemBuilder: (context, index) {
                    final address = addresses[index]; // Lấy địa chỉ tại vị trí index
                    Color? _backgroundColor = isAddressSelected[index] ? Colors.blue[100] : null; // Thay đổi màu nền khi chọn địa chỉ
                    return Container(
                      color: _backgroundColor,
                      padding: const EdgeInsets.all(8.0),
                      child: InkWell(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text("Address: " + address['address']),
                            Text("City: " + address['city']),
                            Text("Country: " + address['country']),
                          ],
                        ),
                        onTap: () {
                          if (!isAddressSelected[index]) {
                            setState(() {
                              isAddressSelected = List.filled(isAddressSelected.length, false); // Reset tất cả các địa chỉ khác
                              isAddressSelected[index] = true; // Chọn địa chỉ hiện tại
                              addressId = address['id']; // Lưu ID của địa chỉ đã chọn
                            });
                          }
                        },
                      ),
                    );
                  },
                ),
                SizedBox(
                  width: 150,
                  height: 25,
                  child: ElevatedButton(
                    child: const Text('Purchase'),
                    onPressed: () async {
                      await _purchaseProduct(); // Gọi hàm mua sản phẩm
                      // ignore: use_build_context_synchronously
                      Navigator.pop(context); // Đóng hộp thoại
                      _fectchCartUser(); // Cập nhật lại giỏ hàng
                    },
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  // Hàm mua sản phẩm
  Future<void> _purchaseProduct() async {
    const String apiUrl = 'http://localhost:8080/order/create';
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
          "productItem": isChoose, // Danh sách sản phẩm đã chọn
          "address": addressId // ID của địa chỉ đã chọn
        }),
      );

      if (response.statusCode == 200) {
        // ignore: use_build_context_synchronously
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text("Purchase products success !!!"),
            backgroundColor: Colors.green,
            duration: Duration(seconds: 3),
          ),
        );
      } else {
        print('Error purchase products: ${response.statusCode}');
      }
    } catch (error) {
      print('Error purchase products: $error');
    }
  }

  // Hộp thoại hiển thị lỗi khi không chọn sản phẩm nào
  void showErrorDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Error !!!'),
          content: Text("Please choose product"),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: const Text('OK'),
            ),
          ],
        );
      },
    );
  }
}

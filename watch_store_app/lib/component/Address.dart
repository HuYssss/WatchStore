// Import các thư viện cần thiết
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

// Lớp StatefulWidget để quản lý trạng thái của widget
class Address extends StatefulWidget {
  @override
  _AddressState createState() => _AddressState();
}

// Lớp State của Address
class _AddressState extends State<Address> {
  // Danh sách các địa chỉ và biến xác định trạng thái tải dữ liệu
  List<dynamic> addresses = [];
  bool _isLoading = true;

  // Phương thức khởi tạo, được gọi khi widget được tạo ra
  @override
  void initState() {
    super.initState();
    _fetchAddresses(); // Gọi hàm để tải dữ liệu địa chỉ
  }

  // Phương thức xây dựng giao diện của widget
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Address"),
      ),
      body: ListView.builder(
        itemCount: addresses.length,
        itemBuilder: (context, index) {
          final addressItem = addresses[index];
          String id = addressItem['id'];
          String address = addressItem['address'];
          String city = addressItem['city'];
          String country = addressItem['country'];
          return Container(
            padding: const EdgeInsets.all(10.0),
            color: Color.fromRGBO(213, 235, 235, 0.392),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text("Address: $address"), 
                      Text("City: $city"),
                      Text("Country: $country"),
                    ],
                  ),
                ),
                const SizedBox(width: 10.0),
                Column(
                  children: [
                    TextButton(
                      onPressed: () => showDeleteConfirmationDialog(id),
                      child: const Text('Delete'),
                    ),
                  ],
                ),
              ],
            ),
          );
        }
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          _buildFormAdd(); // Gọi hàm để hiển thị form thêm địa chỉ mới
        },
        child: Icon(Icons.add),
        backgroundColor: Colors.blue,
        foregroundColor: Colors.white,
      ),
    );
  }

  // Phương thức để tải danh sách địa chỉ từ máy chủ
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
          addresses = responseData['data'] ?? [];
        });
      } else {
        print('Error fetching addresses: ${response.statusCode}');
      }
    } catch (error) {
      print('Error fetching addresses: $error');
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  // Phương thức hiển thị form thêm địa chỉ mới
  void _buildFormAdd() {
    TextEditingController _addressController = TextEditingController();
    TextEditingController _cityController = TextEditingController();
    TextEditingController _countryController = TextEditingController();
    String _errorMessage = '';

    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Add New Address'),
          content: SingleChildScrollView(
            child: Column(
              children: [
                TextField(
                  controller: _addressController,
                  decoration: InputDecoration(
                    labelText: 'Address',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(10.0),
                    ),
                    errorText: _errorMessage.isEmpty ? null : _errorMessage,
                  ),
                ),
                const SizedBox(height: 20.0),
                TextField(
                  controller: _cityController,
                  decoration: InputDecoration(
                    labelText: 'City',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(10.0),
                    ),
                    errorText: _errorMessage.isEmpty ? null : _errorMessage,
                  ),
                ),
                const SizedBox(height: 20.0),
                TextField(
                  controller: _countryController,
                  decoration: InputDecoration(
                    labelText: 'Country',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(10.0),
                    ),
                    errorText: _errorMessage.isEmpty ? null : _errorMessage,
                  ),
                ),
              ],
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () {
                String address = _addressController.text;
                String city = _cityController.text;
                String country = _countryController.text;

                if(address.isEmpty || city.isEmpty || country.isEmpty)
                {
                  setState(() {
                    _errorMessage = "This field is required";
                  });
                }
                else
                {
                  _addNewAddress(address, country, city); // Gọi hàm để thêm địa chỉ mới
                }

                _addressController.clear();
                _cityController.clear();
                _countryController.clear();

                Navigator.pop(context);
              },
              child: const Text('Save'),
            ),
          ],
        );
      },
    );
  }
  
  // Phương thức gửi yêu cầu thêm địa chỉ mới đến server
  Future<void> _addNewAddress(String address, String country, String city) async {
    const String apiUrl = 'http://localhost:8080/address/create';
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    final String? token = prefs.getString('token');
    
    try {
      final response = await http.post(
        Uri.parse(apiUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer ${token!}',
        },
        body: jsonEncode(<String, dynamic> {
          "address": address,
          "city": city,
          "country": country
        }),
      );

      if (response.statusCode == 200) {
        successAddDialog(); // Hiển thị dialog thông báo thêm địa chỉ thành công
      } else {
        print('Error fetching orders: ${response.statusCode}');
      }
    } catch (error) {
      print('Error fetching orders: $error');
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  // Phương thức hiển thị dialog thông báo thêm địa chỉ thành công
  void successAddDialog() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Success!'),
          content: const Text('Address added successfully.'),
          actions: [
            TextButton(
              onPressed: () async { 
                Navigator.pop(context);
                await _fetchAddresses(); // Gọi hàm để tải lại danh sách địa chỉ sau khi thêm mới
              },
              child: const Text('OK'),
            ),
          ],
        );
      },
    );
  }

  // Phương thức hiển thị dialog xác nhận xóa địa chỉ
  bool showDeleteConfirmationDialog(String addressId) {
    bool stateDialog = false;
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Confirm Delete'),
          content: const Text('Are you sure you want to delete this address?'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () {
                _deleteAddress(addressId); // Gọi hàm để xóa địa chỉ
                Navigator.pop(context);
                stateDialog = true;
              },
              child: const Text('Delete'),
            ),
          ],
        );
      },
    );
    return stateDialog;
  }

  // Phương thức gửi yêu cầu xóa địa chỉ đến server
  Future<void> _deleteAddress(String id) async {
    String apiUrl = 'http://localhost:8080/address/delete/$id';
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    final String? token = prefs.getString('token');

    try {
      final response = await http.post(
        Uri.parse(apiUrl),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer ${token!}',
        },
      );

      if (response.statusCode == 200) {
        successDeleteDialog(); // Hiển thị dialog thông báo xóa địa chỉ thành công
      } else {
        print('Error deleting address: ${response.statusCode}');
      }
    } catch (error) {
      print('Error deleting address: $error');
    }
  }

  // Phương thức hiển thị dialog thông báo xóa địa chỉ thành công
  void successDeleteDialog() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Success!'),
          content: const Text('Address deleted successfully.'),
          actions: [
            TextButton(
              onPressed: () async { 
                Navigator.pop(context);
                await _fetchAddresses(); // Gọi hàm để tải lại danh sách địa chỉ sau khi xóa
              },
              child: const Text('OK'),
            ),
          ],
        );
      },
    );
  }
}


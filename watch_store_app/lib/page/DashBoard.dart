import 'package:flutter/material.dart';
import 'package:watch_store_app/tab/CartTab.dart';
import 'package:watch_store_app/tab/CategoryTab.dart';
import 'package:watch_store_app/tab/Home.dart';
import 'package:watch_store_app/tab/PersonTab.dart';

// Định nghĩa một StatefulWidget có tên là DashBoard
class DashBoard extends StatefulWidget {
  @override
  _DashBoardState createState() => _DashBoardState();
}

// Định nghĩa trạng thái của widget DashBoard
class _DashBoardState extends State<DashBoard> {
  int _selectedIndex = 0; // Chỉ mục của tab hiện đang được chọn

  // Định nghĩa các tùy chọn widget cho mỗi tab
  static const List<Widget> _widgetOptions = <Widget>[
    Home(), // Tab Trang chủ
    CategoryTab(), // Tab Danh mục
    CartTab(), // Tab Giỏ hàng
    PersonTab() // Tab Cá nhân
  ];

  // Hàm xử lý việc chọn tab
  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index; // Cập nhật chỉ mục đã chọn
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: _widgetOptions.elementAt(_selectedIndex), // Hiển thị tab được chọn
      ),
      bottomNavigationBar: BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        items: const <BottomNavigationBarItem>[
          BottomNavigationBarItem(
            icon: Icon(Icons.home),
            label: 'Trang chủ',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.category),
            label: 'Danh mục',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.shopping_cart),
            label: 'Giỏ hàng',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.person),
            label: 'Cá nhân',
          ),
        ],
        currentIndex: _selectedIndex,
        selectedItemColor: Colors.amber[800],
        onTap: _onItemTapped,
      ),
    );
  }
}

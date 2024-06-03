import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:watch_store_app/component/CartLineItem.dart';

// Widget OrderLineItem là một StatelessWidget
class OrderLineItem extends StatelessWidget {
  final dynamic order;

  const OrderLineItem({Key? key, required this.order}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Trích xuất thông tin từ order
    String id = order['id'];
    double totalPrice = order['totalPrice'];
    List<dynamic> items = order['productItems'];
    dynamic address = order['address'];
    String state = order['state'];

    // Trả về một Container chứa thông tin của đơn hàng
    return Container(
      decoration: BoxDecoration(
        border: Border.all(
          color: const Color.fromARGB(255, 182, 179, 179),
          width: 2.0,
        ),
        borderRadius: BorderRadius.circular(10.0),
      ),
      height: 200,
      margin: const EdgeInsets.symmetric(vertical: 20),
      child: Flex(
        direction: Axis.vertical,
        children: [
          Expanded(
            child: Text("Order ID: " + id, style: TextStyle(color: Colors.black), textAlign: TextAlign.start),
            flex: 1,
          ),
          Expanded(
            child: Text("State: " + state, style: TextStyle(color: Colors.red, fontWeight: FontWeight.bold), textAlign: TextAlign.end),
            flex: 1,
          ),
          Expanded(
            flex: 7,
            child: ListView.builder(
              itemCount: items.length,
              itemBuilder: (context, index) {
                final item = items[index];
                return CartLineItem(item: item); // Hiển thị mỗi sản phẩm trong đơn hàng dưới dạng CartLineItem
              },
            ),
          ),
          Expanded(
            child: Text("Total price: " + totalPrice.toString() + "VND", style: TextStyle(color: Colors.black)),
            flex: 1,
          ),
        ],
      ),
    );
  }

  // Hàm chuyển đổi danh sách hình ảnh từ dạng dynamic sang List<String>
  List<String> convertImg(List<dynamic> list) {
    List<String> stringImgList = [];
    for (dynamic img in list) {
      stringImgList.add(img.toString());
    }
    return stringImgList;
  }
}
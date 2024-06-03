import 'dart:convert'; // Thư viện để xử lý JSON

import 'package:carousel_slider/carousel_slider.dart'; // Thư viện để tạo carousel slider
import 'package:flutter/material.dart'; // Thư viện chính của Flutter cho giao diện người dùng
import 'package:http/http.dart' as http; // Thư viện để gửi yêu cầu HTTP
import 'package:shared_preferences/shared_preferences.dart'; // Thư viện để xử lý dữ liệu shared preferences

// Khởi tạo một StatefulWidget có tên là ProductDetail
class ProductDetail extends StatefulWidget {
  final dynamic product; // Đối tượng sản phẩm được truyền vào từ widget cha
  const ProductDetail({Key? key, required this.product}) : super(key: key);

  @override
  _ProductDetailState createState() => _ProductDetailState();
}

// Khởi tạo state của ProductDetail
class _ProductDetailState extends State<ProductDetail> {
  late String productId;
  late List<String> imgLink;
  late String productName;
  late double price;
  late String brand;
  late String origin;
  late String thickness;
  late String size;
  late String wireMaterial;
  late String shellMaterial;
  late String style;
  late String feature;
  late String shape;
  late String condition;
  late String weight;
  late String genderUser;
  late String description;
  late String color;

  @override
  void initState() {
    super.initState();
    // Khởi tạo các biến từ widget.product
    productId = widget.product['id'].toString();
    imgLink = convertImg(widget.product['img']);
    productName = widget.product['productName'];
    price = widget.product['price']?.toDouble();
    brand = widget.product['brand'];
    origin = widget.product['origin'];
    thickness = widget.product['thickness'];
    size = widget.product['size'] ?? 'No size';
    wireMaterial = widget.product['wireMaterial'];
    shellMaterial = widget.product['shellMaterial'];
    style = widget.product['style'];
    feature = widget.product['feature'];
    shape = widget.product['shape'];
    condition = widget.product['condition'];
    weight = widget.product['weight'];
    genderUser = widget.product['genderUser'];
    description = widget.product['description'];
    color = widget.product['color'];
  }

  @override
  Widget build(BuildContext context) {
    final List<List<String>> detailProduct = [
      ['brand', brand],
      ['origin', origin],
      ['thickness', thickness],
      ['size', size],
      ['wireMaterial', wireMaterial],
      ['shellMaterial', shellMaterial],
      ['style', style],
      ['feature', feature],
      ['shape', shape],
      ['condition', condition],
      ['weight', weight],
      ['genderUser', genderUser],
      ['color', color],
    ];

    return Scaffold(
      appBar: AppBar(
        title: Text(productName), // Tiêu đề của AppBar
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(9.0),
        child: Column(
          children: [
            CarouselSlider(
              items: imgLink
                  .map((e) => Image.network(
                        e,
                        fit: BoxFit.cover,
                      ))
                  .toList(),
              options: CarouselOptions(
                height: 330,
                viewportFraction: 1.0,
                autoPlay: true,
                enlargeCenterPage: false,
              ),
            ),
            SizedBox(height: 20),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 15),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    productName,
                    style: const TextStyle(
                      fontSize: 25,
                      color: Colors.black,
                    ),
                  ),
                  Text(
                    "Price: " + price.toString() + " VND",
                    style: const TextStyle(
                      fontSize: 15,
                      color: Colors.black,
                    ),
                  ),
                  SizedBox(
                    height: 15,
                  ),
                  Text(
                    description,
                    style: const TextStyle(
                      fontSize: 20,
                      color: Colors.black,
                    ),
                  ),
                  SizedBox(
                    height: 5,
                  ),
                  Divider(),
                  SizedBox(
                    height: 5,
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: Builder(
        builder: (BuildContext ctx) {
          return Container(
            height: 70,
            width: double.infinity,
            decoration: BoxDecoration(color: Colors.white, boxShadow: [
              BoxShadow(
                color: Colors.grey.withOpacity(.2),
                blurRadius: 2,
                spreadRadius: 1,
                offset: Offset(2, -2),
              ),
            ]),
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  primary: Colors.red,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(40.0),
                  ),
                ),
                onPressed: () async {
                  showQuantityDialog(context); // Hiển thị hộp thoại chọn số lượng
                },
                child: const Text("Add To Cart", style: TextStyle(color: Colors.white)),
              ),
            ),
          );
        },
      ),
    );
  }

  // Hàm hiển thị hộp thoại chọn số lượng sản phẩm
  void showQuantityDialog(BuildContext context) {
    int selectedQuantity = 1; // Số lượng mặc định là 1
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Select Quantity"),
          content: StatefulBuilder(
            builder: (BuildContext context, StateSetter setState) {
              return Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  IconButton(
                    icon: Icon(Icons.remove),
                    onPressed: () {
                      setState(() {
                        if (selectedQuantity > 1) selectedQuantity--; // Giảm số lượng nếu lớn hơn 1
                      });
                    },
                  ),
                  Text(
                    selectedQuantity.toString(),
                    style: TextStyle(fontSize: 20),
                  ),
                  IconButton(
                    icon: Icon(Icons.add),
                    onPressed: () {
                      setState(() {
                        selectedQuantity++; // Tăng số lượng
                      });
                    },
                  ),
                ],
              );
            },
          ),
          actions: <Widget>[
            TextButton(
              child: Text("Cancel"),
              onPressed: () {
                Navigator.of(context).pop(); // Đóng hộp thoại
              },
            ),
            TextButton(
              child: Text("Add to Cart"),
              onPressed: () async {
                // Gọi hàm thêm sản phẩm vào giỏ hàng
                final response = await addProductToCart(productId, selectedQuantity);
                if (response.statusCode == 200) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text("Add product to cart success !!!"),
                      backgroundColor: Colors.green,
                      duration: const Duration(seconds: 3),
                    ),
                  );
                }
                Navigator.of(context).pop(); // Đóng hộp thoại sau khi thêm vào giỏ hàng
              },
            ),
          ],
        );
      },
    );
  }

  // Hàm chuyển đổi danh sách ảnh từ dạng động sang dạng chuỗi
  List<String> convertImg(List<dynamic> list) {
    List<String> stringImgList = [];
    for (dynamic img in list) {
      stringImgList.add(img.toString());
    }
    return stringImgList;
  }

  // Hàm gửi yêu cầu thêm sản phẩm vào giỏ hàng tới API
  Future<http.Response> addProductToCart(String productId, int quantity) async {
    print(productId + " " + quantity.toString());
    const String addProductToCartUrl = "http://localhost:8080/cart/addProductToCart";
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    final String? token = prefs.getString('token'); // Lấy token từ SharedPreferences
    try {
      final response = await http.post(
        Uri.parse(addProductToCartUrl),
        headers: <String, String>{
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ${token!}', // Thêm token vào header để xác thực
        },
        body: jsonEncode(<String, dynamic>{
          "product": productId, // ID sản phẩm
          "quantity": quantity, // Số lượng sản phẩm
        }),
      );
      return response; // Trả về phản hồi từ API
    } catch (error) {
      // Xử lý lỗi mạng
      throw Exception('Failed to add product to cart: $error');
    }
  }
}

import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';
import 'package:watch_store_app/component/CardProduct.dart';

// Widget ProductGridView là một StatelessWidget
class ProductGridView extends StatelessWidget {
  final List<dynamic> products; // Danh sách các sản phẩm
  const ProductGridView({Key? key, required this.products}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Danh sách các đường dẫn hình ảnh mẫu
    List<String> imgLink = ['image/introImg1.webp', 'image/introImg2.webp', 'image/introImg3.webp'];
    return Scaffold(
      appBar: AppBar(
        title: const Text("WatcHes"), // Tiêu đề thanh ứng dụng
      ),
      body: ListView.builder(
        itemCount: products.length, // Số lượng sản phẩm
        itemBuilder: (context, index) {
          final product = products[index]; // Lấy sản phẩm tương ứng với chỉ mục
          if (index == 0) {
            // Nếu là sản phẩm đầu tiên, hiển thị carousel slider và sản phẩm
            return Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                CarouselSlider(
                  items: imgLink
                      .map((e) => Image.asset(
                            e,
                            fit: BoxFit.cover,
                          ))
                      .toList(),
                  options: CarouselOptions(
                    height: 110,
                    viewportFraction: 1.0,
                    autoPlay: true,
                    enlargeCenterPage: false,
                  ),
                ),
                SizedBox(height: 10),
                CardProduct(product: product) // Hiển thị sản phẩm dưới dạng thẻ
              ],
            );
          } else {
            // Nếu không phải sản phẩm đầu tiên, chỉ hiển thị sản phẩm
            return CardProduct(product: product); // Hiển thị sản phẩm dưới dạng thẻ
          }
        },
      ),
    );
  }
}

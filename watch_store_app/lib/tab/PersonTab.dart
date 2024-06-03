import 'dart:convert'; // Thư viện để xử lý JSON
import 'package:flutter/material.dart'; // Thư viện chính của Flutter cho giao diện người dùng
import 'package:http/http.dart' as http; // Thư viện để gửi yêu cầu HTTP
import 'package:shared_preferences/shared_preferences.dart'; // Thư viện để lưu trữ dữ liệu đơn giản
import 'package:watch_store_app/component/Address.dart'; // Import component Address
import 'package:watch_store_app/component/EditDetail.dart'; // Import component EditDetail
import 'package:watch_store_app/component/Order.dart'; // Import component Order
import 'package:watch_store_app/main.dart'; // Import file main.dart của dự án

// Khởi tạo một StatefulWidget có tên là PersonTab
class PersonTab extends StatefulWidget {
  const PersonTab({Key? key}) : super(key: key);
  @override
  _PersonTabState createState() => _PersonTabState();
}

// Khởi tạo state của PersonTab
class _PersonTabState extends State<PersonTab> {
  dynamic _user; // Biến lưu thông tin người dùng
  String name = ''; // Biến lưu tên người dùng
  String email = ''; // Biến lưu email người dùng
  String imgProfile = ''; // Biến lưu URL ảnh đại diện người dùng
  Map<String, String> listInfomation = {}; // Biến lưu các thông tin khác của người dùng

  @override
  void initState() {
    super.initState();
    _fetchUser(); // Gọi hàm _fetchUser khi khởi tạo
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
      child: Column(
        children: [
          const SizedBox(height: 50),
          Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                width: 70,
                height: 70,
                decoration: BoxDecoration(
                  image: DecorationImage(
                    image: NetworkImage(imgProfile),
                    fit: BoxFit.cover,
                  ),
                  shape: BoxShape.circle,
                ),
              ),
              const SizedBox(width: 20),
              Padding(
                padding: const EdgeInsets.only(top: 5),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      name,
                      style: const TextStyle(
                        fontFamily: 'Metro',
                        fontWeight: FontWeight.w400,
                        color: Colors.black,
                        fontSize: 18,
                      ),
                    ),
                    const SizedBox(height: 5),
                    Text(
                      email,
                      style: const TextStyle(
                        fontFamily: 'Metro',
                        fontWeight: FontWeight.w400,
                        color: Colors.black,
                        fontSize: 18,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
          Expanded(
            child: ListView.separated(
              shrinkWrap: true,
              itemCount: listInfomation.length,
              separatorBuilder: (BuildContext context, int index) => Divider(),
              itemBuilder: (BuildContext context, int index) {
                var title = listInfomation.keys.elementAt(index);
                var subtitle = listInfomation.values.elementAt(index);
                return InkWell(
                  onTap: () {
                    switch (index) {
                      case 0:
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => Order()),
                        );
                        break;
                      case 1:
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => Address()),
                        );
                        break;
                      case 3:
                        Navigator
                            .push(
                              context,
                              MaterialPageRoute(
                                  builder: (context) =>
                                      EditDetail(user: _user)),
                            )
                            .then((value) => _fetchUser());
                        break;
                    }
                  },
                  child: ListTile(
                    contentPadding: const EdgeInsets.all(0.0),
                    title: Text(
                      title,
                      style: const TextStyle(
                        fontFamily: 'Metro',
                        fontWeight: FontWeight.w400,
                        color: Colors.black,
                        fontSize: 18,
                      ),
                    ),
                    subtitle: Text(
                      subtitle,
                      style: const TextStyle(
                        fontFamily: 'Metro',
                        fontWeight: FontWeight.w400,
                        color: Colors.black,
                        fontSize: 18,
                      ),
                    ),
                    trailing: IconButton(
                      onPressed: () {},
                      icon: const Icon(
                        Icons.arrow_forward_ios,
                        size: 18,
                        color: Colors.grey,
                      ),
                    ),
                  ),
                );
              },
            ),
          ),
          const SizedBox(height: 100),
          SizedBox(
            width: 200,
            height: 50,
            child: ElevatedButton(
              onPressed: () async {
                final SharedPreferences prefs =
                    await SharedPreferences.getInstance();
                prefs.remove("token");
                // ignore: use_build_context_synchronously
                Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(builder: (context) => const MyApp()),
                );
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.red,
              ),
              child:
                  const Text('Logout', style: TextStyle(color: Colors.white)),
            ),
          ),
        ],
      ),
    );
  }

  // Hàm lấy thông tin người dùng từ API
  Future<void> _fetchUser() async {
    const String apiUrl = 'http://localhost:8080/user'; // Địa chỉ API
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    final String? token = prefs.getString('token'); // Lấy token từ SharedPreferences
    try {
      // Gửi yêu cầu GET đến API
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
          _user = responseData['data'] ?? [];
          String fName = _user['firstname'] ?? '';
          String lName = _user['lastname'] ?? '';
          name = '$fName $lName';
          imgProfile = _user['avatarImg'] ??
              "https://res.cloudinary.com/dybeglkyi/image/upload/v1717156786/R_pw4y4z.jpg";
          email = _user['email'];
          int order = _user['order'].length;
          int address = _user['address'].length;

          if (name == ' ') {
            name = 'Unknow';
          }

          listInfomation = {
            'My orders': 'Already have $order orders',
            'Shipping addresses': '$address address',
            'Payment methods': 'Visa ',
            'Edit your profile': name
          };
        });
      } else {
        print('Error fetching products: ${response.statusCode}');
      }
    } catch (error) {
      print('Error fetching products: $error');
    }
  }
}

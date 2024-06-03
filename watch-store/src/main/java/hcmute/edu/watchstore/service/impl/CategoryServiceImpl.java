package hcmute.edu.watchstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.constants.ResponseCode;
import hcmute.edu.watchstore.dto.response.CategoryResponse;
import hcmute.edu.watchstore.dto.response.CategoryResponseSimple;
import hcmute.edu.watchstore.dto.response.ProductResponse;
import hcmute.edu.watchstore.entity.Category;
import hcmute.edu.watchstore.entity.Product;
import hcmute.edu.watchstore.repository.CategoryRepository;
import hcmute.edu.watchstore.service.CategoryService;
import hcmute.edu.watchstore.service.ProductService;

@Service
public class CategoryServiceImpl extends ServiceBase implements CategoryService  {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    // tìm category bằng id
    @Override
    public Category findCategory(ObjectId categoryId) {
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        return category.orElse(null);
    }

    // lấy danh sách các sản phẩm có trong category
    @Override
    public ResponseEntity<?> getCategoryResp(ObjectId categoryId) {
        Category category = findCategory(categoryId);
        List<ProductResponse> listProducts = this.productService.findAll();
        if (category == null || listProducts.isEmpty()) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        List<ProductResponse> products = findProductAdvance(category.getProduct(), listProducts);

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(categoryId.toHexString());
        categoryResponse.setCategoryName(category.getCategoryName());
        categoryResponse.setProduct(products);

        return success(categoryResponse);
    }

    // handle khi xóa category
    public void handleDeleteCategory(List<ObjectId> productList) {
        if (!productList.isEmpty()) {
            List<Product> allProduct = this.productService.findAllNormal();
            for (ObjectId id : productList) {
                Product p = findProduct(id, allProduct);
                p.setCategory(new ObjectId("662a058d43948d98f91010b8"));
                this.productService.saveOrUpdate(p);
            }

            Category category = findCategory(new ObjectId("662a058d43948d98f91010b8"));
            List<ObjectId> products = category.getProduct();
            products.addAll(productList);
            this.categoryRepository.save(category);
        }
    }

    // tìm nhiều product một lúc
    public List<ProductResponse> findProductAdvance(List<ObjectId> listId, List<ProductResponse> products) {
        List<ProductResponse> result = new ArrayList<>();
        for(ObjectId id : listId) {
            for(ProductResponse p : products) {
                if (p.getId().equals(id.toHexString())) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    // tìm sản phẩm trong danh sách theo id
    public Product findProduct(ObjectId id, List<Product> products) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // lấy danh sách các category và các id sản phẩm có trong category
    @Override
    public ResponseEntity<?> findAll() {
        List<Category> categories = this.categoryRepository.findAll();;
        if (!categories.isEmpty()) {
            List<CategoryResponseSimple> response = new ArrayList<>();
            for(Category c : categories) {
                CategoryResponseSimple resp = new CategoryResponseSimple(
                    c.getId().toHexString(),
                    c.getCategoryName(),
                    convertListProductId(c.getProduct())
                );
                response.add(resp);
            }
            return success(response);
        }
        return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
    }

    // chuyển đổi danh sách ObjectId sang String
    public List<String> convertListProductId(List<ObjectId> listId) {
        List<String> result = new ArrayList<>();
        for(ObjectId id : listId) {
            result.add(id.toHexString());
        }
        return result;
    }
}

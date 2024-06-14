package hcmute.edu.watchstore.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.dto.request.AddProdToCateReq;
import hcmute.edu.watchstore.entity.Product;
import hcmute.edu.watchstore.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> findProuctById(@PathVariable(name = "id") ObjectId productId) {
        return this.productService.findProductById(productId);
    }

    @GetMapping("/find/{keyword}")
    public ResponseEntity<?> finddProuctByKeyword(@PathVariable(name = "keyword") String keyword) {
        return this.productService.findProductByKeyword(keyword);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String testAdmin() {
        return "You are Admin !!!";
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> findAllProduct() {
        return this.productService.getAll();
    }

    @GetMapping("/getByOption")
    public ResponseEntity<?> getByOption(@RequestParam(value = "option", defaultValue = "empty") String option
                                        , @RequestParam(value = "value", defaultValue = "empty") String value
                                        , @RequestParam(value = "from", defaultValue = "0") double from
                                        , @RequestParam(value = "to", defaultValue = "0") double to) {
        return this.productService.getByOption(option, value, from, to);
    }
    

    @GetMapping("/count")
    public long countAllProduct() {
        return this.productService.countAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        return this.productService.createOrUpdateProduct(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit")
    public ResponseEntity<?> editProduct(@RequestBody Product product) {
        return this.productService.createOrUpdateProduct(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable ObjectId productId) {
        return this.productService.delete(productId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addProductToCategory")
    public ResponseEntity<?> addProductToCategory(@RequestBody AddProdToCateReq req) {
        return this.productService.addProductToCategory(req.getProductId(), req.getCategoryId());
    }
}
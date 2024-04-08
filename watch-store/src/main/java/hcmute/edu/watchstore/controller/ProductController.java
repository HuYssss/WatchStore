package hcmute.edu.watchstore.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.service.ProductService;

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
    public String testAdmin(Pageable pageable) {
        return "You are Admin !!!";
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAllProduct() {
        return this.productService.findAll();
    }

    @GetMapping("/count")
    public long countAllProduct() {
        return this.productService.countAll();
    }
}
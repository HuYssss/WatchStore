package hcmute.edu.watchstore.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.base.ControllerBase;
import hcmute.edu.watchstore.entity.Category;
import hcmute.edu.watchstore.service.CategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/category")
public class CategoryController extends ControllerBase {

    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryResponse(@PathVariable ObjectId id) {
        return this.categoryService.getCategoryResp(id);
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return this.categoryService.findAll();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createNewCategory(@RequestBody Category category) {
        return this.categoryService.createCategory(category);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategorry(@PathVariable ObjectId categoryId) {
        return this.categoryService.deleteCategory(categoryId);
    }
}

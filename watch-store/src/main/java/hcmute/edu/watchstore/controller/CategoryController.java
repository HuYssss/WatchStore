package hcmute.edu.watchstore.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.base.ControllerBase;
import hcmute.edu.watchstore.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController extends ControllerBase {

    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryResponse(@PathVariable ObjectId id) {
        return this.categoryService.getCategoryResp(id);
    }
}

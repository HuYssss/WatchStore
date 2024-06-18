package hcmute.edu.watchstore.dto.response;

import java.util.List;

import hcmute.edu.watchstore.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;

    private String productName;

    private List<String> img;

    private double price;

    private String brand;

    private String origin;

    private String thickness;

    private String size;

    private String wireMaterial;

    private String shellMaterial;

    private String style;

    private String feature;

    private String shape;

    private String condition;

    private String weight;

    private String genderUser;

    private String description;

    private String color;

    private String category;

    private int amount;

    private double discount;

    private int waterproof;

    private String state;

    public ProductResponse(Product product) {
        this.id = product.getId().toHexString();
        this.productName = product.getProductName();
        this.img = product.getImg();
        this.price = product.getPrice();
        this.brand = product.getBrand();
        this.origin = product.getOrigin();
        this.thickness = product.getThickness();
        this.size = product.getSize();
        this.wireMaterial = product.getWireMaterial();
        this.shellMaterial = product.getShellMaterial();
        this.style = product.getStyle();
        this.feature = product.getFeature();
        this.shape = product.getShape();
        this.condition = product.getCondition();
        this.weight = product.getWeight();
        this.genderUser = product.getGenderUser();
        this.description = product.getDescription();
        this.color = product.getColor();
        this.category = product.getCategory().toHexString();
        this.state = product.getState();
        this.amount = product.getAmount();
        this.discount = product.getDiscount();
        this.waterproof = product.getWaterproof();
    }
    
}

package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.models.Product;
import mgkm.smsbackend.models.ProductImage;
import mgkm.smsbackend.services.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/products")
@AllArgsConstructor
public class ProductsController extends BaseController {

    private final ProductsService productsService;

    @GetMapping("/allProducts")
    public List<Product> getAllProducts() {
        return this.productsService.getAllProducts();
    }

    @GetMapping("/allProductsImages")
    public List<ProductImage> getAllProductsImages() throws IOException {
        return this.productsService.getAllProductsImages();
    }

    @GetMapping("{productId}")
    public Product getProduct(Integer productId) {
        return this.productsService.getProduct(productId);
    }

    @PostMapping("/addNewProduct")
    public Product addNewProduct(@RequestBody Product product) {
        return this.productsService.addNewProduct(product);
    }

    @PostMapping("/{productId}/addProductImages")
    public void addProductImages(@PathVariable("productId") Integer productId,
                                 @RequestParam(value = "imagesFiles") List<MultipartFile> imagesFiles)
            throws IOException {
        this.productsService.addProductImages(productId, imagesFiles);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteProduct(@PathVariable("productId") Integer productId) throws IOException {
        this.productsService.deleteProduct(productId);
    }


}

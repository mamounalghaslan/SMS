package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.models.Product;
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
    public List<Product> getAllProducts() throws IOException {
        return this.productsService.getAllProducts();
    }

    @PostMapping("/addNewProduct")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Product addNewProduct(@RequestBody Product product) {
        return this.productsService.addNewProduct(product);
    }

    @PostMapping("/{productId}/addProductDisplayImage")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void addProductDisplayImage(@PathVariable("productId") Integer productId,
                                       @RequestParam(value = "imageFile") MultipartFile displayImageFile)
            throws IOException {
        this.productsService.addProductDisplayImage(this.productsService.getProduct(productId), displayImageFile);
    }

    @DeleteMapping("/deleteProduct")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteProduct(@RequestBody Product product) throws IOException {
        this.productsService.deleteProduct(product);
    }


}

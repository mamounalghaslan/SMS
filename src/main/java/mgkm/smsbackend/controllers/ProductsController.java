package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.models.Product;
import mgkm.smsbackend.services.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/addNewProduct")
    public Product addNewProduct(@RequestBody Product product) {
        return this.productsService.addNewProduct(product);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteProduct(@PathVariable("productId") Integer productId) throws IOException {
        this.productsService.deleteProduct(productId);
    }


}

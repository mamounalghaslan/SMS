package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Product;
import mgkm.smsbackend.models.ProductImage;
import mgkm.smsbackend.repositories.ProductImageRepository;
import mgkm.smsbackend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {

    @Value("${sms-root-images-path}")
    private String rootImagesPath;

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    public ProductsService(ProductRepository productRepository,
                           ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    public List<Product> getAllProducts() {
        return (List<Product>) this.productRepository.findAll();
    }

    public void addNewProduct(Product product, List<ProductImage> productImages) {
        this.productImageRepository.saveAll(productImages);
        this.productRepository.save(product);
    }


}

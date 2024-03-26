package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Product;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.repositories.ProductReferenceRepository;
import mgkm.smsbackend.repositories.ProductRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductsService extends ImageBase64Service {

    @Value("${sms-root-images-path}")
    private String rootImagesPath;

    private final ProductRepository productRepository;

    private final ProductReferenceRepository productReferenceRepository;

    public ProductsService(ProductRepository productRepository,
                           ProductReferenceRepository productReferenceRepository) {
        this.productRepository = productRepository;
        this.productReferenceRepository = productReferenceRepository;
    }

    private String getProductDisplayImageUrl(Integer productId) {
        return this.rootImagesPath + "/productsDisplayImages/" + productId + "/";
    }

    private String getProductsReferencesImageUrl(Integer productId) {
        return this.rootImagesPath + "/productsReferencesImages/" + productId + "/";
    }

    public List<Product> getAllProducts() {
        return (List<Product>) this.productRepository.findAll();
    }

    public Product addNewProduct(Product product) {
        return this.productRepository.save(product);
    }

    public void deleteProduct(Integer productId) throws IOException {

        Product product = this.productRepository.findById(productId).orElse(null);
        assert product != null;

        String productDisplayImageUrl = this.getProductDisplayImageUrl(productId);

        Path path = Paths.get(productDisplayImageUrl);
        if(Files.exists(path)) {
            FileUtils.cleanDirectory(path.toFile());
            Files.delete(path);
        }

        this.resetProductReferencesByProduct(product);
        this.productRepository.delete(product);

    }

    private void resetProductReferencesByProduct(Product product) throws IOException {

        String productsReferencesImageUrl = this.getProductsReferencesImageUrl(product.getSystemId());

        Path path = Paths.get(productsReferencesImageUrl);
        if(Files.exists(path)) {
            FileUtils.cleanDirectory(path.toFile());
            Files.delete(path);
        }

        List<ProductReference> productReferences =
                (List<ProductReference>) this.productReferenceRepository.findAllByProduct(product);

        productReferences.forEach(productReference -> productReference.setProduct(null));

        this.productReferenceRepository.saveAll(productReferences);

    }

}

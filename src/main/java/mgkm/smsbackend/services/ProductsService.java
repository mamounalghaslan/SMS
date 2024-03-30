package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Product;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.repositories.ProductReferenceRepository;
import mgkm.smsbackend.repositories.ProductRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductsService {

    @Value("${sms-root-images-path}")
    private String rootImagesPath;

    private final ProductRepository productRepository;

    private final ProductReferenceRepository productReferenceRepository;

    public ProductsService(ProductRepository productRepository,
                           ProductReferenceRepository productReferenceRepository) {
        this.productRepository = productRepository;
        this.productReferenceRepository = productReferenceRepository;
    }

    private String getProductsDisplayImagesUrl(Integer productId) {
        return this.rootImagesPath + "/productsDisplayImages/" + productId + "/";
    }

    private String getProductsImagesUrl(Integer productId) {
        return this.rootImagesPath + "/productsImages/" + productId + "/";
    }

    public List<Product> getAllProducts() {
        return (List<Product>) this.productRepository.findAll();
    }

    public Product getProduct(Integer productId) {
        return this.productRepository.findById(productId).orElse(null);
    }

    public Product addNewProduct(Product product) {
        return this.productRepository.save(product);
    }

    public void addProductDisplayImage(Product product, MultipartFile productDisplayImageFile) throws IOException {

        String productDisplayImageUrl = this.getProductsDisplayImagesUrl(product.getSystemId());

        Path path = Paths.get(productDisplayImageUrl);

        if(!Files.exists(path)) {
            Files.createDirectories(path);
        } else {
            FileUtils.cleanDirectory(path.toFile());
        }

        Files.write(Paths.get(productDisplayImageUrl + productDisplayImageFile.getOriginalFilename()),
                productDisplayImageFile.getBytes());

        product.setImageFileName(productDisplayImageFile.getOriginalFilename());

        this.productRepository.save(product);

    }

    public void deleteProduct(Product product) throws IOException {

        // delete display image
        String productDisplayImageUrl = this.getProductsDisplayImagesUrl(product.getSystemId());
        Path displayImagePath = Paths.get(productDisplayImageUrl);
        if(Files.exists(displayImagePath)) {
            FileUtils.cleanDirectory(displayImagePath.toFile());
            Files.delete(displayImagePath);
        }

        // delete reference images
        String productsReferencesImageUrl = this.getProductsImagesUrl(product.getSystemId());
        Path productImagesPath = Paths.get(productsReferencesImageUrl);
        if(Files.exists(productImagesPath)) {
            FileUtils.cleanDirectory(productImagesPath.toFile());
            Files.delete(productImagesPath);
        }

        // reset product references to empty
        List<ProductReference> productReferences =
                (List<ProductReference>) this.productReferenceRepository.findAllByProduct(product);
        productReferences.forEach(productReference -> productReference.setProduct(null));

        this.productReferenceRepository.saveAll(productReferences);
        this.productRepository.delete(product);

    }

}

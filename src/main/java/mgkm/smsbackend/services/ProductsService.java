package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Product;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.repositories.ProductReferenceRepository;
import mgkm.smsbackend.repositories.ProductRepository;
import mgkm.smsbackend.utilities.ImageUtilities;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductsService {

    private final ProductRepository productRepository;

    private final ProductReferenceRepository productReferenceRepository;

    public ProductsService(ProductRepository productRepository,
                           ProductReferenceRepository productReferenceRepository) {
        this.productRepository = productRepository;
        this.productReferenceRepository = productReferenceRepository;
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

        String productDisplayImageUrl = ImageUtilities.getProductsDisplayImagesUrl(product.getSystemId());

        ImageUtilities.saveMultipartFileImage(productDisplayImageUrl, productDisplayImageFile);

        product.setImageFileName(productDisplayImageFile.getOriginalFilename());

        this.productRepository.save(product);

    }

    public void deleteProduct(Product product) throws IOException {

        // delete display image
        String productDisplayImageUrl = ImageUtilities.getProductsDisplayImagesUrl(product.getSystemId());
        Path displayImagePath = Paths.get(productDisplayImageUrl);
        if(Files.exists(displayImagePath)) {
            FileUtils.cleanDirectory(displayImagePath.toFile());
            Files.delete(displayImagePath);
        }

        // reset product references to empty
        List<ProductReference> productReferences =
                (List<ProductReference>) this.productReferenceRepository.findAllByProduct(product);
        productReferences.forEach(productReference -> productReference.setProduct(null));

        this.productReferenceRepository.saveAll(productReferences);
        this.productRepository.delete(product);

    }

}

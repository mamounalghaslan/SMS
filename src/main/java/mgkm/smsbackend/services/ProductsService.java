package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Product;
import mgkm.smsbackend.models.ProductImage;
import mgkm.smsbackend.repositories.ProductImageRepository;
import mgkm.smsbackend.repositories.ProductRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductsService extends ImageBase64Service {

    @Value("${sms-root-images-path}")
    private String rootImagesPath;

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    public ProductsService(ProductRepository productRepository,
                           ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    private String getProductImagesUrl(Integer productId) {
        return this.rootImagesPath + "/productImages/" + productId + "/";
    }

    public List<Product> getAllProducts() {
        return (List<Product>) this.productRepository.findAll();
    }

    public Product addNewProduct(Product product) {
        return this.productRepository.save(product);
    }

    public void deleteProduct(Integer productId) throws IOException {

        String productImagesUrl = this.getProductImagesUrl(productId);

        Path path = Paths.get(productImagesUrl);
        if(Files.exists(path)) {
            FileUtils.cleanDirectory(path.toFile());
            Files.delete(path);
        }

        this.productImageRepository.findProductImageByProductSystemId(productId)
                .forEach(productImage -> this.productImageRepository.deleteById(productImage.getSystemId()));

        // TODO: delete product references

        this.productRepository.deleteById(productId);

    }

    public void addProductImages(Integer productId, List<MultipartFile> productImagesFiles) throws IOException {

        Product product = this.productRepository.findById(productId).orElse(null);
        assert product != null;

        String productImagesUrl = this.getProductImagesUrl(productId);

        Path path = Paths.get(productImagesUrl);

        if(!Files.exists(path)) {
            Files.createDirectories(path);
        } else {
            FileUtils.cleanDirectory(path.toFile());
        }

        List<ProductImage> productImages = new ArrayList<>();

        for (MultipartFile productImageFile : productImagesFiles) {

            String productImageUri = productImagesUrl + productImageFile.getOriginalFilename();

            Files.write(Paths.get(productImageUri), productImageFile.getBytes());

            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setImagePath(productImageUri);
            productImages.add(productImage);
        }

        this.productImageRepository.saveAll(productImages);

    }

    public Product getProduct(Integer productId) {
        return this.productRepository.findById(productId).orElse(null);
    }

    public List<ProductImage> getAllProductsImages() throws IOException {
        List<ProductImage> productsImages = (List<ProductImage>) this.productImageRepository.findAll();
        for (ProductImage productImage : productsImages) {
            productImage.setImageFileBase64(loadImageAsBase64(productImage.getImagePath()));
        }
        return productsImages;
    }


}

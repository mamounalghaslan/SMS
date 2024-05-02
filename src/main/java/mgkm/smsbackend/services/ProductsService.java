package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Product;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.repositories.ProductReferenceRepository;
import mgkm.smsbackend.repositories.ProductRepository;
import mgkm.smsbackend.utilities.DirectoryUtilities;
import mgkm.smsbackend.utilities.ImageUtilities;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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

        DirectoryUtilities.purgeDirectory(ImageUtilities.getProductsDisplayImagesUrl(product.getSystemId()));

        // reset product references to empty
        List<ProductReference> productReferences =
                (List<ProductReference>) this.productReferenceRepository.findAllByProduct(product);
        productReferences.forEach(productReference -> productReference.setProduct(null));

        this.productReferenceRepository.saveAll(productReferences);
        this.productRepository.delete(product);

    }

    // this should be commented unless database is purged
    public void initialize() throws IOException {
        Product p0 = new Product(null, "Jif Ultra Fast", "display.jpg");
        Product p1 = new Product(null, "Jif Hygienic Foam", "display.jpg");
        Product p2 = new Product(null, "Jif Hygienic Foam Blue", "display.jpg");
        Product p3 = new Product(null, "Jif Ultra Fast Kitchen", "display.jpg");
        Product p4 = new Product(null, "Jif Cream Spray", "display.jpg");
        Product p5 = new Product(null, "Jif Ultra Fast Multi-Purpose", "display.jpg");
        Product p6 = new Product(null, "Jif Ultra Fast Everywhere", "display.jpg");
        Product p7 = new Product(null, "Jif Ultra Fast Green", "display.jpg");
        Product p8 = new Product(null, "Jif Ultra Fast Blue", "display.jpg");
        Product p9 = new Product(null, "Harpic Bathroom Cleaner Spray", "display.jpg");
        Product p10 = new Product(null, "Harpic Bathroom Cleaner Bottle", "display.jpg");
        Product p11 = new Product(null, "Harpic Toilet Dark", "display.jpg");
        Product p12 = new Product(null, "Harpic Bathroom Cleaner Bottle Lemon", "display.jpg");
        Product p13 = new Product(null, "Dettol Spray Blue", "display.jpg");
        Product p14 = new Product(null, "Dettol Spray Orange", "display.jpg");
        Product p15 = new Product(null, "Dettol Spray Yellow", "display.jpg");
        Product p16 = new Product(null, "Dettol Spray Pink", "display.jpg");
        Product p17 = new Product(null, "Dettol Spray Green", "display.jpg");
        Product p18 = new Product(null, "Fairy Spray Yellow", "display.jpg");
        Product p19 = new Product(null, "Dettol Wipes Yellow", "display.jpg");
        Product p20 = new Product(null, "Fairy Wipes Blue", "display.jpg");
        Product p21 = new Product(null, "DAC Spray Bundle", "display.jpg");
        Product p22 = new Product(null, "GoGreen Spray", "display.jpg");
        Product p23 = new Product(null, "General Cleaner", "display.jpg");
        Product p24 = new Product(null, "Tough Job", "display.jpg");
        Product p25 = new Product(null, "Big Green", "display.jpg");
        Product p26 = new Product(null, "101", "display.jpg");
        Product p27 = new Product(null, "MrMuscle Clear", "display.jpg");
        Product p28 = new Product(null, "MrMuscle Yellow", "display.jpg");
        Product p29 = new Product(null, "MrMuscle Blue", "display.jpg");
        Product p30 = new Product(null, "MrMuscle Green", "display.jpg");
        Product p31 = new Product(null, "Ajax Blue Big", "display.jpg");
        Product p32 = new Product(null, "Ajax Green", "display.jpg");
        Product p33 = new Product(null, "DAC Small", "display.jpg");
        Product p34 = new Product(null, "Scouring Powder", "display.jpg");
        Product p35 = new Product(null, "GoGreen Light Blue", "display.jpg");
        Product p36 = new Product(null, "Clorox Orange", "display.jpg");
        Product p37 = new Product(null, "Clorox Blue", "display.jpg");
        Product p38 = new Product(null, "409", "display.jpg");
        Product p39 = new Product(null, "Harpic Blue", "display.jpg");
        Product p40 = new Product(null, "Harpic Bundle", "display.jpg");
        Product p41 = new Product(null, "Dettol Bundle 1", "display.jpg");
        Product p42 = new Product(null, "Jif Cream", "display.jpg");
        Product p43 = new Product(null, "Jif Cream Kabeer", "display.jpg");
        Product p44 = new Product(null, "Fairy Spray Blue", "display.jpg");
        Product p45 = new Product(null, "Harpic Large Bundle", "display.jpg");
        Product p46 = new Product(null, "Dettol Bundle 2", "display.jpg");
        Product p47 = new Product(null, "Dettol Clear", "display.jpg");
        Product p48 = new Product(null, "Dettol Wipes Green", "display.jpg");
        Product p49 = new Product(null, "Dettol Wipes Pink", "display.jpg");
        Product p50 = new Product(null, "SMACK!", "display.jpg");
        Product p51 = new Product(null, "Lysol", "display.jpg");
        Product p52 = new Product(null, "Lysol Blue", "display.jpg");
        Product p53 = new Product(null, "Orange Thing", "display.jpg");
        Product p54 = new Product(null, "GoGreen Blue", "display.jpg");
        Product p55 = new Product(null, "GoGreen White", "display.jpg");
        Product p56 = new Product(null, "Enzo Yellow", "display.jpg");
        Product p57 = new Product(null, "Enzo White", "display.jpg");
        Product p58 = new Product(null, "Clorox Green", "display.jpg");
        Product p59 = new Product(null, "MrMuscle Red", "display.jpg");
        Product p60 = new Product(null, "Clorox Purple", "display.jpg");


        ArrayList<Product> products = new ArrayList<>(List.of(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10,
                p11, p12, p13, p14, p15, p16, p17, p18, p19, p20,
                p21, p22, p23, p24, p25, p26, p27, p28, p29, p30,
                p31, p32, p33, p34, p35, p36, p37, p38, p39, p40,
                p41, p42, p43, p44, p45, p46, p47, p48, p49, p50,
                p51, p52, p53, p54, p55, p56, p57, p58, p59, p60));
        productRepository.saveAll(products);

        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p0.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p1.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p2.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p3.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p4.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p5.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p6.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p7.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p8.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p9.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p10.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p11.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p12.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p13.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p14.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p15.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p16.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p17.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p18.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p19.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p20.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p21.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p22.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p23.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p24.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p25.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p26.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p27.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p28.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p29.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p30.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p31.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p32.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p33.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p34.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p35.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p36.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p37.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p38.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p39.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p40.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p41.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p42.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p43.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p44.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p45.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p46.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p47.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p48.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p49.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p50.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p51.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p52.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p53.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p54.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p55.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p56.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p57.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p58.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p59.getSystemId());
        DirectoryUtilities.purgeOrCreateDirectory("F:/SMS-DATA/productsDisplayImages/" + p60.getSystemId());

        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p0.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p0.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p1.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p1.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p2.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p2.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p3.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p3.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p4.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p4.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p5.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p5.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p6.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p6.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p7.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p7.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p8.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p8.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p9.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p9.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p10.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p10.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p11.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p11.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p12.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p12.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p13.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p13.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p14.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p14.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p15.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p15.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p16.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p16.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p17.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p17.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p18.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p18.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p19.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p19.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p20.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p20.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p21.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p21.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p22.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p22.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p23.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p23.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p24.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p24.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p25.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p25.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p26.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p26.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p27.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p27.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p28.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p28.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p29.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p29.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p30.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p30.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p31.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p31.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p32.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p32.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p33.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p33.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p34.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p34.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p35.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p35.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p36.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p36.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p37.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p37.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p38.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p38.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p39.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p39.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p40.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p40.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p41.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p41.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p42.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p42.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p43.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p43.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p44.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p44.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p45.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p45.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p46.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p46.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p47.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p47.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p48.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p48.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p49.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p49.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p50.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p50.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p51.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p51.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p52.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p52.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p53.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p53.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p54.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p54.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p55.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p55.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p56.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p56.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p57.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p57.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p58.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p58.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p59.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p59.getSystemId() +"/display.jpg");
        DirectoryUtilities.copyFileToDirectory("F:/SMS/sms-backend/database/products-backup/"+ p60.getSystemId() +"/display.jpg", "F:/SMS-DATA/productsDisplayImages/"+ p60.getSystemId() +"/display.jpg");
    }

}

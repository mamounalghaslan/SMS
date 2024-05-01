package mgkm.smsbackend.models.inference;

import lombok.Data;

import java.util.List;

@Data
public class InferenceOutput {

    // misplaced products
    private List<MisplacedProduct> misplacedProducts;

    // gaps
    private List<Gap> gaps;

    // image file
    private String image_file;

}

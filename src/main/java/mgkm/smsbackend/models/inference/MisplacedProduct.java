package mgkm.smsbackend.models.inference;

import lombok.Data;

@Data
public class MisplacedProduct {

    private Integer positionProductId;
    private String positionProductName;
    private DetectedObject detectedObject;

}

package mgkm.smsbackend.models.inference;

import lombok.Data;

import java.util.List;

@Data
public class DetectedObject {

    private Integer id;
    private BoundingBox boundingBox;
    private List<Classification> classifications;

}

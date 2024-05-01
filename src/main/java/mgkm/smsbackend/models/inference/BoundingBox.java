package mgkm.smsbackend.models.inference;

import lombok.Data;

@Data
public class BoundingBox {

    private Float x1;
    private Float y1;
    private Float x2;
    private Float y2;

}

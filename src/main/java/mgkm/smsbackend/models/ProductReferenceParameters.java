package mgkm.smsbackend.models;

import lombok.Data;

import java.util.List;

@Data
public class ProductReferenceParameters {

    private List<ProductReference> inserts;
    private List<ProductReference> updates;
    private List<ProductReference> deletes;

}

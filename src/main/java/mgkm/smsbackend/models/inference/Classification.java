package mgkm.smsbackend.models.inference;

import lombok.Data;

@Data
public class Classification {

    private Float confidence;
    private String name;

}

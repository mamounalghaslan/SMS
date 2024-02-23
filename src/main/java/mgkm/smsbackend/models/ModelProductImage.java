package mgkm.smsbackend.models;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class ModelProductImage {

    @Id
    @GeneratedValue
    @Nonnull
    private Integer systemId;

    @ManyToOne(targetEntity = Model.class)
    @Nonnull
    private Integer modelId;

    @ManyToOne(targetEntity = ProductImage.class)
    @Nonnull
    private Integer productImageId;

}

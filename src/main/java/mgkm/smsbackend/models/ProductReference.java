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
public class ProductReference {

    @Id
    @GeneratedValue
    @Nonnull
    private Integer systemId;

    @ManyToOne(targetEntity = Camera.class)
    @Nonnull
    private Integer cameraId;

    @ManyToOne(targetEntity = Product.class)
    @Nonnull
    private Integer productId;

    @Nonnull
    private Float xCenter;

    @Nonnull
    private Float yCenter;

    @Nonnull
    private Float width;

    @Nonnull
    private Float height;

}

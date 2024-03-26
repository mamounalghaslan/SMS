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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer systemId;

    @ManyToOne(targetEntity = ReferenceImage.class)
    @Nonnull
    private ReferenceImage referenceImage;

    @Nonnull
    private Float xCenter;

    @Nonnull
    private Float yCenter;

    @Nonnull
    private Float width;

    @Nonnull
    private Float height;

    @ManyToOne(targetEntity = Product.class)
    private Product product;

    private String imagePath;

}

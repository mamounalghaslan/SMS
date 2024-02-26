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

    @ManyToOne(targetEntity = Camera.class)
    @Nonnull
    private Camera camera;

    @ManyToOne(targetEntity = Product.class)
    @Nonnull
    private Product product;

    @Nonnull
    private Float xCenter;

    @Nonnull
    private Float yCenter;

    @Nonnull
    private Float width;

    @Nonnull
    private Float height;

}

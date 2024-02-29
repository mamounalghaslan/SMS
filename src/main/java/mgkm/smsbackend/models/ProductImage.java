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
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer systemId;

    @ManyToOne(targetEntity = Product.class)
    @Nonnull
    private Product product;

    private String imagePath;

    private String imageFileBase64;

}

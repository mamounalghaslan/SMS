package mgkm.smsbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ManyToOne(targetEntity = ShelfImage.class)
    @Nonnull
    private ShelfImage shelfImage;

    @ManyToOne(targetEntity = Product.class)
    private Product product;

    private String imagePath;

    @Nonnull
    @JsonProperty("x1")
    private Float x1;

    @Nonnull
    @JsonProperty("y1")
    private Float y1;

    @Nonnull
    @JsonProperty("x2")
    private Float x2;

    @Nonnull
    @JsonProperty("y2")
    private Float y2;

}

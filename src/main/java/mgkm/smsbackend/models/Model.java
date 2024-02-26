package mgkm.smsbackend.models;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer systemId;

    @ManyToOne(targetEntity = ModelType.class)
    @Nonnull
    private ModelType modelType;

    @Nonnull
    private LocalDate creationDate;

    @OneToMany(targetEntity = Product.class)
    @Nonnull
    private List<Product> products;

}

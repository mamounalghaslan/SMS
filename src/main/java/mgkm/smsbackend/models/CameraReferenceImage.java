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
public class CameraReferenceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer systemId;

    @OneToOne(targetEntity = Camera.class)
    @Nonnull
    private Camera camera;

    @Nonnull
    private String imagePath;

    @Nonnull
    private LocalDate captureDate;

    @OneToMany(targetEntity = ProductReference.class)
    @Nonnull
    private List<ProductReference> productReferences;

}

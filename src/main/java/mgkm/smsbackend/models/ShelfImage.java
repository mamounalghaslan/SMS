package mgkm.smsbackend.models;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class ShelfImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer systemId;

    @ManyToOne(targetEntity = ShelfImageType.class)
    @Nonnull
    private ShelfImageType shelfImageType;

    @Nonnull
    private LocalDateTime captureDate;

    @ManyToOne(targetEntity = Camera.class)
    private Camera referencedCamera;

    private String imageFileName;

}

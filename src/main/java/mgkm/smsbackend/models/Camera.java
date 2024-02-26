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
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer systemId;

    @Nonnull
    private String ipAddress;

    @Nonnull
    @Column(unique = true)
    private String location;

    @Nonnull
    private String username;

    @Nonnull
    private String password;

    @ManyToOne(targetEntity = CameraStatusType.class)
    private CameraStatusType cameraStatusType;

    @OneToOne(targetEntity = CameraReferenceImage.class)
    private CameraReferenceImage cameraReferenceImage;

}

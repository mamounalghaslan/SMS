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
    @GeneratedValue
    @Nonnull
    private Integer systemId;

    @Nonnull
    private String ipAddress;

    @Nonnull
    private String location;

    @Nonnull
    private String username;

    @Nonnull
    private String password;

    @ManyToOne(targetEntity = CameraStatusType.class)
    @Nonnull
    private String statusTypeId;

}

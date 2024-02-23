package mgkm.smsbackend.models;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class CameraStatusType {

    @Id
    @GeneratedValue
    @Nonnull
    private Integer systemId;

    @Nonnull
    private String description;

}

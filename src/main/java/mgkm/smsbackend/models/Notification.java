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
public class Notification {

    @Id
    @GeneratedValue
    @Nonnull
    private Integer systemId;

    @Nonnull
    @ManyToOne(targetEntity = Product.class)
    private Product product;

    @ManyToOne(targetEntity = NotificationErrorType.class)
    @Nonnull
    private NotificationErrorType notificationErrorType;

    @ManyToOne(targetEntity = NotificationStatusType.class)
    @Nonnull
    private NotificationStatusType statusType;

    @Nonnull
    private String location;

}

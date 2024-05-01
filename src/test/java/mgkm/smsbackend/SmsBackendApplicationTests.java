package mgkm.smsbackend;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.repositories.CameraRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SmsBackendApplicationTests {

    @Autowired
    private CameraRepository cameraRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void createCamera() {

        Camera camera = new Camera();
        camera.setIpAddress("ip1");
        camera.setLocation("loc1");
        camera.setUsername("user1");
        camera.setPassword("pass1");

        Camera savedCamera = cameraRepository.save(camera);

        Optional<Camera> retrievedCamera = cameraRepository.findById(savedCamera.getSystemId());

        assert(retrievedCamera.isPresent());
        assertNotNull(retrievedCamera.get().getSystemId());
        assert(retrievedCamera.get().getIpAddress().equals("ip1"));

        cameraRepository.delete(savedCamera);

    }

}

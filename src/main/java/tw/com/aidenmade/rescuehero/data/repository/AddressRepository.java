package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tw.com.aidenmade.rescuehero.data.entity.Address;
import tw.com.aidenmade.rescuehero.data.projection.AddressProjection;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
}


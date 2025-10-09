package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.Country;
import tw.com.aidenmade.rescuehero.data.projection.CountryProjection;

public interface CountryRepository extends JpaRepository<Country, Long> {
    // 查詢全部 projection 分頁
    Page<CountryProjection> findAllBy(Pageable pageable);
}

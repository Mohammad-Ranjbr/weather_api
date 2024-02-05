package com.skyapi.weatherapiservice.repository;

import com.skyapi.weatherapicommon.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location,String> {

    @Query("Select l From Location l Where l.trashed = false ")
    List<Location> findUntrashed();

    @Query("Select l From Location l Where l.trashed = false AND l.code= ?1") // avalin parametr method
    Location findByCode(String code);

    @Modifying
    @Query("Update Location Set trashed = true Where code = ?1") // avalin parametr method
    void trashByCode(String code);

    @Query("Select l From Location l Where l.countryCode = ?1 And l.cityName = ?2 And l.trashed = false")
    Location findByCountryCodeAndCityName(String countryCode , String cityName);

}

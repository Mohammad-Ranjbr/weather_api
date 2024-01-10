package com.skyapi.weatherapiservice.repository;

import com.skyapi.weatherapicommon.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,String> {

}

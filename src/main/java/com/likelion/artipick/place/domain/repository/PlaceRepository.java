package com.likelion.artipick.place.domain.repository;

import com.likelion.artipick.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}

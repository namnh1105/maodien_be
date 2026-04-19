package com.hainam.worksphere.breed.mapper;

import com.hainam.worksphere.breed.domain.Breed;
import com.hainam.worksphere.breed.dto.response.BreedResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BreedMapper {

    BreedResponse toResponse(Breed breed);
}

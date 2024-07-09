package online.jeweljoust.BE.mapper;

import online.jeweljoust.BE.entity.AuctionSession;
import online.jeweljoust.BE.model.AuctionSessionDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface  AuctionSessionMapper {
    AuctionSessionMapper INSTANCE = Mappers.getMapper(AuctionSessionMapper.class);

    @Mapping(target = "register", ignore = true) // Ignoring isRegistered for now
    AuctionSessionDetailResponse toResponse(AuctionSession session);
}

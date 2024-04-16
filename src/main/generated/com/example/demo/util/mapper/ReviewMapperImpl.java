package com.example.demo.util.mapper;

import com.example.demo.domain.entity.Review;
import com.example.demo.web.result.ReviewResult;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-16T23:45:44+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResult toDto(Review entity) {
        if ( entity == null ) {
            return null;
        }

        ReviewResult reviewResult = new ReviewResult();

        reviewResult.setId( entity.getId() );
        reviewResult.setTitle( entity.getTitle() );
        reviewResult.setContent( entity.getContent() );
        reviewResult.setStar( entity.getStar() );
        reviewResult.setHeartCount( entity.getHeartCount() );

        return reviewResult;
    }

    @Override
    public List<ReviewResult> toDtoList(List<Review> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ReviewResult> list = new ArrayList<ReviewResult>( entities.size() );
        for ( Review review : entities ) {
            list.add( toDto( review ) );
        }

        return list;
    }
}

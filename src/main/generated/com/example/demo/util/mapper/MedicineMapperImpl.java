package com.example.demo.util.mapper;

import com.example.demo.domain.entity.Medicine;
import com.example.demo.web.result.MedicineResult;
import com.example.demo.web.result.MedicineResult.MedicineResultBuilder;
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
public class MedicineMapperImpl implements MedicineMapper {

    @Override
    public MedicineResult toDto(Medicine entity) {
        if ( entity == null ) {
            return null;
        }

        MedicineResultBuilder medicineResult = MedicineResult.builder();

        medicineResult.id( entity.getId() );
        medicineResult.BSSH_NM( entity.getBSSH_NM() );
        medicineResult.PRDLST_NM( entity.getPRDLST_NM() );

        return medicineResult.build();
    }

    @Override
    public List<MedicineResult> toDtoList(List<Medicine> entities) {
        if ( entities == null ) {
            return null;
        }

        List<MedicineResult> list = new ArrayList<MedicineResult>( entities.size() );
        for ( Medicine medicine : entities ) {
            list.add( toDto( medicine ) );
        }

        return list;
    }
}

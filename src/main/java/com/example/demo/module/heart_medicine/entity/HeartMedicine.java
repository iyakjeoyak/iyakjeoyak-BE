package com.example.demo.module.heart_medicine.entity;


import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.image.dto.result.ImageResult;
import com.example.demo.module.medicine.dto.result.MedicineNameImgResult;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.heart_medicine.dto.result.HeartMedicineResult;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HeartMedicine extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public HeartMedicineResult toDto() {
        Medicine m = this.medicine;
        ImageResult imageResult = m.getImage() == null ? null : ImageResult
                .builder()
                .id(this.medicine.getImage().getId())
                .fullPath(this.medicine.getImage().getFullPath())
                .build();
        return HeartMedicineResult.builder()
                .id(this.id)
                .medicineId(MedicineNameImgResult.builder()
                        .id(m.getId())
                        .BSSH_NM(m.getBSSH_NM())
                        .PRDLST_NM(m.getPRDLST_NM())
                        .image(imageResult).build())
                .build();
    }

}

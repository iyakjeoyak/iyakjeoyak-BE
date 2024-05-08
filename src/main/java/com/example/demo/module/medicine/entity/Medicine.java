package com.example.demo.module.medicine.entity;

import com.example.demo.module.category.entity.Category;
import com.example.demo.module.common.entity.BaseTimeEntity;
import com.example.demo.module.hashtag.entity.Hashtag;
import com.example.demo.module.image.entity.Image;
import com.example.demo.module.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Medicine extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String LCNS_NO;
    private String BSSH_NM;
    private String PRDLST_REPORT_NO;
    private String PRDLST_NM;
    private String PRMS_DT;
    private String POG_DAYCNT;

    @Column(columnDefinition = "text")
    private String DISPOS;
    private String NTK_MTHD;
    @Column(columnDefinition = "text")
    private String PRIMARY_FNCLTY;
    @Column(columnDefinition = "text")
    private String IFTKN_ATNT_MATR_CN;
    private String CSTDY_MTHD;
    private String PRDLST_CDNM;
    @Column(columnDefinition = "text")
    private String STDR_STND;
    private String HIENG_LNTRT_DVS_NM;
    private String PRODUCTION;
    //    private String CHILD_CRTFC_YN;
    private String PRDT_SHAP_CD_NM;
    private String RAWMTRL_NM;

    @Column(columnDefinition = "text")
    private String FRMLC_MTRQLT;
    private String INDUTY_CD_NM;
    private String LAST_UPDT_DTM;

    @Column(columnDefinition = "text")
    private String INDIV_RAWMTRL_NM;

    @Column(columnDefinition = "text")
    private String ETC_RAWMTRL_NM;

    @Column(columnDefinition = "text")
    private String CAP_RAWMTRL_NM;
    /**
     * id	데이터 PK
     * LCNS_NO	인허가번호
     * BSSH_NM	업소_명
     * PRDLST_REPORT_NO	품목제조번호
     * PRDLST_NM	품목_명
     * PRMS_DT	허가_일자
     * POG_DAYCNT	소비기한_일수
     * DISPOS	성상
     * NTK_MTHD	섭취방법
     * PRIMARY_FNCLTY	주된기능성
     * IFTKN_ATNT_MATR_CN	섭취시주의사항
     * CSTDY_MTHD	보관방법
     * PRDLST_CDNM	유형
     * STDR_STND	기준규격
     * HIENG_LNTRT_DVS_NM	고열량저영양여부
     * PRODUCTION	생산종료여부
     * CHILD_CRTFC_YN	어린이기호식품품질인증여부
     * PRDT_SHAP_CD_NM	제품형태
     * FRMLC_MTRQLT	포장재질
     * RAWMTRL_NM	품목유형(기능지표성분)
     * INDUTY_CD_NM	업종
     * LAST_UPDT_DTM	최종수정일자
     * INDIV_RAWMTRL_NM	기능성 원재료
     * ETC_RAWMTRL_NM	기타 원재료
     * CAP_RAWMTRL_NM	캡슐 원재료
     */

    private boolean isAd;

    @Setter
    private Integer heartCount;

    private Double grade;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MedicineHashtag> hashtagList = new ArrayList<>();

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MedicineCategory> categoryList = new ArrayList<>();

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Image image;

    // 추가 getter
    public List<Hashtag> getHashtags() {
        return hashtagList.stream().map(MedicineHashtag::getHashtag).toList();
    }

    public List<Category> getCategories() {
        return categoryList.stream().map(MedicineCategory::getCategory).toList();
    }

    public Integer getReviewCount() {
        return reviewList.size();
    }

    // 도메인 로직
    public void gradeAvg() {
        if (reviewList==null || reviewList.isEmpty()) {
            this.grade = 0.0;
        } else {
            this.grade = roundAvg(reviewStarSum() / reviewList.size());
        }
    }


    public void gradeAvgByDelete(Double deleteReviewStar) {
        if (reviewList.size() - 1 == 0) {
            this.grade = 0.0;
        } else {
            this.grade = roundAvg(Math.max(reviewStarSum() - deleteReviewStar, 0d) / (reviewList.size() - 1));
        }
    }
    // 내부 계산 로직용 메서드
    private Double reviewStarSum() {
        return reviewList.stream().map(Review::getStar).reduce(Double::sum).orElse(0d);
    }

    private Double roundAvg(Double grade) {
        return Math.round(grade * 10) / 10.0;
    }

    public void addHeartCount() {
        this.heartCount++;
    }

    public void decreaseHeartCount() {
        this.heartCount--;
        if (this.heartCount < 0) throw new IllegalArgumentException("좋아요가 0 이하가 되었습니다.(비정상)");
    }
}

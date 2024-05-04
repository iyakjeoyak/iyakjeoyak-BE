package com.example.demo.module.medicine.repository;

import com.example.demo.module.medicine.dto.payload.MedicineSearchCond;
import com.example.demo.module.medicine.dto.payload.MedicineOrderField;
import com.example.demo.module.medicine.dto.payload.OrderSortCond;
import com.example.demo.module.medicine.entity.Medicine;
import com.example.demo.module.medicine.entity.QMedicine;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import io.micrometer.core.annotation.Timed;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.demo.module.medicine.entity.QMedicine.medicine;
import static com.example.demo.module.medicine.entity.QMedicineCategory.medicineCategory;
import static com.example.demo.module.medicine.entity.QMedicineHashtag.medicineHashtag;

@Repository
public class QueryMedicineRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public QueryMedicineRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }
    public Page<Medicine> findAllBySearch(MedicineSearchCond medicineSearchCond, Pageable pageable) {
        OrderSortCond orderSortCond = medicineSearchCond.getOrderSortCond();
        List<Long> idList = query
                .select(medicine.id)
                .from(medicine)
                .join(medicine.categoryList, medicineCategory)
                .join(medicine.hashtagList, medicineHashtag)
                .where(
                        categoryEq(medicineSearchCond.getCategoryId())
                        , hashtagEq(medicineSearchCond.getHashtagId())
                        , nameLike(medicineSearchCond.getKeyword()))
                .groupBy(medicine.id)
                .orderBy(setOrderBy(orderSortCond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Medicine> medicines = query
                .select(medicine)
                .from(medicine)
                .where(medicine.id.in(idList))
                .orderBy(setOrderBy(orderSortCond))
                .fetch();

        // 이하 카운트용 쿼리
        JPAQuery<Long> totalIdList = query.select(medicine.id)
                .from(medicine)
                .join(medicine.categoryList, medicineCategory)
                .join(medicine.hashtagList, medicineHashtag)
                .where(categoryEq(medicineSearchCond.getCategoryId()), hashtagEq(medicineSearchCond.getHashtagId()))
                .groupBy(medicine.id)
                .from();

        JPAQuery<Long> count = query
                .select(medicine.count())
                .from(medicine)
                .where(medicine.id.in(totalIdList));

        return PageableExecutionUtils.getPage(medicines, pageable, count::fetchOne);
    }

    public List<String> getAutoComplete(String keyword, Integer limit) {
        List<String> nameResult =
                query.select(medicine.PRDLST_NM)
                        .from(medicine)
                        .orderBy(new OrderSpecifier<>(Order.ASC, medicine.PRDLST_NM))
                        .where(nameLike(keyword))
                        .offset(0)
                        .limit(limit)
                        .fetch();

//        QMedicine sub = new QMedicine("sub");
//        List<String> companyResult =
//                query.select(medicine.BSSH_NM)
//                        .from(medicine)
//                        .orderBy(new OrderSpecifier<>(Order.ASC, medicine.BSSH_NM))
//                        .where(companyStart(keyword))
//                        .offset(0)
//                        .limit(limit)
//                        .fetch();
//        return Stream.concat(companyResult.stream(), nameResult.stream())
//                .sorted(Comparator.comparing(String::length)).toList();
        nameResult.sort(Comparator.comparing(String::length));

        return nameResult;
    }

    private BooleanExpression heartCountGoe(Integer heartCount) {
        return (heartCount == null) ? null : medicine.heartCount.goe(heartCount);
    }

    private BooleanExpression companyLike(String keyword) {
        return StringUtils.isEmpty(keyword) ? null : medicine.BSSH_NM.like("%" + keyword + "%");
    }

    private BooleanExpression nameLike(String keyword) {
        return StringUtils.isEmpty(keyword) ? null : medicine.PRDLST_NM.like("%" + keyword + "%");
    }

    private BooleanExpression nameStart(String keyword) {
        return StringUtils.isEmpty(keyword) ? null : medicine.PRDLST_NM.like(keyword + "%");
    }

    private BooleanExpression companyStart(String keyword) {
        return StringUtils.isEmpty(keyword) ? null : medicine.BSSH_NM.like(keyword + "%");
    }

    private BooleanExpression keywordLike(String keyword) {
        BooleanExpression nameLike = nameLike(keyword);
        return nameLike == null ? companyLike(keyword) : nameLike(keyword).and(companyLike(keyword));
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return (categoryId == null || categoryId == 0) ? null : medicineCategory.category.id.eq(categoryId);
    }

    private BooleanExpression hashtagEq(Long hashtagId) {
        return (hashtagId == null || hashtagId == 0) ? null : medicineHashtag.hashtag.id.eq(hashtagId);
    }

    private OrderSpecifier<?> setOrderBy(OrderSortCond orderSortCond) {
        if (orderSortCond == null) return new OrderSpecifier<>(Order.ASC, medicine.id);

        MedicineOrderField medicineOrderField = orderSortCond.getMedicineOrderField() == null ? MedicineOrderField.ID : orderSortCond.getMedicineOrderField();
        Order sort = orderSortCond.getSort() == Order.ASC ? Order.ASC : Order.DESC;


        return switch (medicineOrderField) {
            case GRADE -> new OrderSpecifier<>(sort, medicine.grade);
            case HEART_COUNT -> new OrderSpecifier<>(sort, medicine.heartCount);
            case CREATED_DATE -> new OrderSpecifier<>(sort, medicine.createdDate);
            case ID -> new OrderSpecifier<>(sort, medicine.id);
        };
    }
}

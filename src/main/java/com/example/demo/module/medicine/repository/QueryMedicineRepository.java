package com.example.demo.module.medicine.repository;

import com.example.demo.module.medicine.dto.payload.MedicineSearchCond;
import com.example.demo.module.medicine.dto.payload.OrderSortCond;
import com.example.demo.module.medicine.entity.Medicine;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

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
//        OrderSortCond orderSortCond = medicineSearchCond.getOrderSortCond();
        List<Long> idList = query
                .select(medicine.id)
                .from(medicine)
                .join(medicine.categoryList, medicineCategory)
                .join(medicine.hashtagList, medicineHashtag)
                .where(categoryEq(medicineSearchCond.getCategoryId()), hashtagEq(medicineSearchCond.getHashtagId()))
                .groupBy(medicine.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Medicine> medicines = query
                .select(medicine)
                .from(medicine)
                .where(medicine.id.in(idList))
//                .orderBy(setOrderBy(orderSortCond))
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

    private BooleanExpression heartCountGoe(Integer heartCount) {
        return (heartCount == null) ? null : medicine.heartCount.goe(heartCount);
    }

    private BooleanExpression companyLike(String keyword) {
        return StringUtils.isEmpty(keyword) ? null : medicine.BSSH_NM.like("%" + keyword + "%");
    }

    private BooleanExpression nameLike(String keyword) {
        return StringUtils.isEmpty(keyword) ? null : medicine.PRDLST_NM.like("%" + keyword + "%");
    }

    private BooleanExpression keywordLike(String keyword) {
        return nameLike(keyword).and(companyLike(keyword));
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return (categoryId == null || categoryId == 0) ? null : medicineCategory.category.id.eq(categoryId);
    }

    private BooleanExpression hashtagEq(Long hashtagId) {
        return (hashtagId == null || hashtagId == 0) ? null : medicineHashtag.hashtag.id.eq(hashtagId);
    }

    private OrderSpecifier<?> setOrderBy(OrderSortCond orderSortCond) {
        Integer sortPayload = orderSortCond.getSort();
        String orderBy = orderSortCond.getOrderField();

        Order order;
        if (sortPayload.equals(1)) {
            order = Order.ASC;
        } else if (sortPayload.equals(-1)) {
            order = Order.DESC;
        } else {
            throw new IllegalArgumentException("오름차순 : 1 , 내림차순 : -1");
        }

        return switch (orderBy) {
            case "grade" -> new OrderSpecifier<>(order, medicine.grade);
            case "heartCount" -> new OrderSpecifier<>(order, medicine.heartCount);
            case "createdDate" -> new OrderSpecifier<>(order, medicine.createdDate);
            default -> new OrderSpecifier<>(order, medicine.id);
        };
    }
}

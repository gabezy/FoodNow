package com.gabezy.foodnow.infra;

import com.gabezy.foodnow.domain.entity.Restaurant;
import com.gabezy.foodnow.infra.repositories.RestaurantRepositoryCustomized;
import com.gabezy.foodnow.repositories.RestaurantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.gabezy.foodnow.infra.spec.RestaurantSpecBuilder.withFreeDelivery;
import static com.gabezy.foodnow.infra.spec.RestaurantSpecBuilder.withNameLike;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustomized {

    @PersistenceContext
    private EntityManager manager;
    @Autowired @Lazy // spring instancia a dependência apenas quando necessária
    private RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> findCustomized(String name, BigDecimal minDeliveryFee, BigDecimal maxDeliveryFee) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> criteriaQuery = cb.createQuery(Restaurant.class);
        Root<Restaurant> root = criteriaQuery.from(Restaurant.class); //from Root(Restaurant)

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasLength(name)) {
            predicates.add(cb.like(root.get("name"), "%"+name+"%"));
        }
        if (minDeliveryFee != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("deliveryFee"), minDeliveryFee));
        }
        if (maxDeliveryFee != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("deliveryFee"), maxDeliveryFee));
        }

//        predicates.forEach(criteriaQuery::where);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Restaurant> query = manager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public List<Restaurant> findWithFreeDeliveryFee(String name) {
        return restaurantRepository.findAll(withFreeDelivery().and(withNameLike(name)));
    }
}
/*
        var jpql = new StringBuilder();
        var params = new HashMap<String, Object>();

        jpql.append("from Restaurant where -1 = 0 ");
        if (StringUtils.hasLength(name)) {
            jpql.append("and lower(name) like lower(:name) ");
            params.put("name", "%" + name + "%");
        }
        if (minDeliveryFee != null) {
            jpql.append("and deliveryFee >= :min ");
            params.put("min", minDeliveryFee);
        }
        if (maxDeliveryFee != null) {
            jpql.append("and deliveryFee <= :max");
            params.put("max", maxDeliveryFee);
        }
        params.forEach(query::setParameter);
*/
